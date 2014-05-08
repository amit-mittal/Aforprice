package thrift.servers;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Category;
import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.ProductList;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.SortCriterion;
import thrift.genereated.retailer.Tick;
import util.ConfigParms;
import util.Metric;
import db.dao.ProductsDAO;
import db.dao.ProductsDAO.HISTORY_QUERY_TYPE;
import entities.ProductSummary;
import entities.Retailer;

/**
 * @author Ashish
 * Test Files:
 * ProductCacheImplTest: unit tests of productCache methods
 * ProductCacheImplUpdaterTest: updateProducts and ProductUpdater
 * ProductCacheImplThriftTest: getProducts and categoryProductsMap
 * TODO Have to add maps of treeset, and will copy the treeset
 * to the list after ProductUpdater
 */
public class ProductCacheImpl{
	private static final Logger logger = Logger.getLogger(ProductCacheImpl.class);
	private Set<String> retailerSet = new HashSet<String>(30);
	public List<Retailer> retailers = new ArrayList<Retailer>();
	private Integer numProducts = -1;
	private static final ProductsDAO prodDao = new ProductsDAO();
	private static final ProductCacheImplUtil util = new ProductCacheImplUtil();
	private ProductCacheImplHelper cacheHelp;
	public static int TIMER_INTERVAL = 5*60*1000;
	public static final double EPSILON = 0.00000001;
	private CategoryCacheImpl categoryCache;
	private TopPriceDrops2 priceDrops = new TopPriceDrops2(this);
	Metric timeIt = new Metric("TimeIt");
	
	
	private static final ThreadLocal<DecimalFormat> priceFormatter = new ThreadLocal<DecimalFormat>(){
		@Override
		protected DecimalFormat initialValue(){
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			return df;
		}
	};
	
	public static void overrideTimeIntervalForRegTest(int seconds){
		TIMER_INTERVAL = seconds * 1000;
	}
	
	public ProductCacheImpl(List<Retailer> retailers){
		this.retailers = retailers;
		for(Retailer retailer:retailers)
			retailerSet.add(retailer.getId());
		init();
	}
	
	public ProductCacheImpl(CategoryCacheImpl categoryCache, List<Retailer> retailers, Integer numProducts){
		this.categoryCache = categoryCache;
		this.retailers = retailers;
		this.numProducts = numProducts;
		for(Retailer retailer:retailers)
			retailerSet.add(retailer.getId());
		init();
	}
	
	private void init() {
		try {
			ProductData.getInstance().resetUnitTest();
			ProductData.getInstance().setPriceDropsInstance(priceDrops);
			logger.info("Loading products for retailers");
			//Cache Helper - Simply loads product summary & create various mapping ( no history yet )
			cacheHelp = new ProductCacheImplHelper(retailers, numProducts);
			// Get history data for each product
			for(Retailer retailer : retailers){
				timeIt.start();
				logger.info("getting price history for retailer: "+retailer.getId());
				//TODO: we only need to call this once even for shared retailer table like cache 2, add that logic
				prodDao.getDataForProductsByRetailer(retailer.getId(), HISTORY_QUERY_TYPE.PRICE);
				timeIt.end();
				logger.info("getting price history for retailer...done, took " + timeIt.getProcessingTimeMinsSecs());
				timeIt.reset();
				
				if(ConfigParms.getInstance().isWebsiteMode()){
					timeIt.start();
					logger.info("getting reviews for retailer: "+retailer.getId());
					prodDao.getDataForProductsByRetailer(retailer.getId(), HISTORY_QUERY_TYPE.REVIEW);
					timeIt.end();
					logger.info("getting reviews for retailer...done, took " + timeIt.getProcessingTimeMinsSecs());
					timeIt.reset();
				}else
					logger.info("Not website mode: skip getting reviews");
			}
			timeIt.start();
			processProductHistoryData(); //calc min, max, time range etc
			if(ConfigParms.getInstance().isWebsiteMode())
				ProductData.getInstance().sortAllCategoryProductsMap();
			timeIt.end();
			logger.info("process history...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();
			
			//cacheHelp.constructCategoryFilterMaps();//TODO these maps update testing is also pending
			//_categoryPriceFilterMap = cacheHelp.getCategoryPriceFilterMap();
			//_categoryReviewFilterMap = cacheHelp.getCategoryReviewFilterMap();

			timeIt.start();
			initializeAndProcessPriceDrops();
			timeIt.end();
			logger.info("price drops init...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();

			logger.info("Loading products for retailers...done");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeAndProcessPriceDrops()
	{
		logger.info("Generating price movements ");
		priceDrops.addPriceDrops(ProductData.getInstance().getProducts());
		priceDrops.printTopDropsStats();
		logger.info("Generating price movements...done");
		if(ConfigParms.getInstance().isWebsiteMode()){
			ProductData.getInstance().setCategoryProductsByPriceDropMap
				(cacheHelp.getCategoryProductsByPriceDropMap(priceDrops.mapOfCategoryToTopDropsSet));
		}
		else
			logger.info("Not website mode: skip creating category->products by price drops");
 	}
	
	public void updatePriceDrops(List<Product> products, String retailerId){
		priceDrops.updatePriceDrops(products, retailerId);
	}
	
	private class TickComparatorByTime implements Comparator<Tick>{
		@Override
		public int compare(Tick o1, Tick o2) {
			if(o1.getTime()-o2.getTime()>0)
				return 1;
			else if(o1.getTime()-o2.getTime()<0)
				return -1;
			else
				return 0;
		}
	}
	
	private class ReviewComparatorByTime implements Comparator<Review>{
		@Override
		public int compare(Review o1, Review o2) {
			if(o1.getTime()-o2.getTime()>0)
				return 1;
			else if(o1.getTime()-o2.getTime()<0)
				return -1;
			else
				return 0;
		}
	}
	
	/*
	 * find min, max value/time etc
	 */
	public void processProductHistoryData(){
		TickComparatorByTime tickComparatorByTime = new TickComparatorByTime();
		ReviewComparatorByTime reviewComparatorByTime = new ReviewComparatorByTime();
		
		//create price history which includes min, max, min time, max time etc
		logger.info("Processing PriceHistory of each Product");
		for(Product prod : ProductData.getInstance().getProducts()){
			Collections.sort(prod.getPriceHistory().getPriceTicks(), tickComparatorByTime);
			util.processPriceHistory(prod.getPriceHistory());
		}
		logger.info("Processing PriceHistory of each Product...done");
		if(ConfigParms.getInstance().isWebsiteMode()){
			logger.info("Processing ReviewHistory of each Product");
			for(Product prod : ProductData.getInstance().getProducts()){
				Collections.sort(prod.getReviewHistory(), reviewComparatorByTime);
				util.processReviewHistory(prod.getReviewHistory());
			}
			logger.info("Processing ReviewHistory of each Product...done");
		}else
			logger.info("Not website mode: skip getting reviews");

		
	}
	
	
	public void updateProducts(List<ProductSummary> products){
		for(ProductSummary prodInfo: products){
			if(!retailerSet.contains(prodInfo.getRetailerId())){
				logger.error("SERIOUS: Getting update on product("+prodInfo.getId()+") whose retailer is("+prodInfo.getRetailerId()+") not subsribed");
				continue;
			}
			if(prodInfo.isActive()){
				if(!ProductData.getInstance().containsProduct(prodInfo.getId()))
					addProduct(prodInfo);
				else
					updateProduct(prodInfo);
			}
			else
				ProductData.getInstance().removeProductCategory(Retailer.getRetailer(prodInfo.getRetailerId()), prodInfo.getCategoryId(), prodInfo.getId());
		}
	}
	
	private void addProduct(ProductSummary newProduct){
		//not seeing the case, if product goes from active -> inactive -> active
		PriceHistory priceHistory = new PriceHistory();
		priceHistory.setPriceTicks(new ArrayList<Tick>());
		priceHistory.getPriceTicks().add(new Tick(newProduct.getDownloadTime().getTime(), newProduct.getPrice()));		
		List<Review> reviewHistory = new ArrayList<Review>();
		reviewHistory.add(new Review(newProduct.getDownloadTime().getTime(), newProduct.getReviewRating(), newProduct.getNumReviews()));	
		List<Tick> sellRankHistory = new ArrayList<Tick>();	
		sellRankHistory.add(new Tick(newProduct.getDownloadTime().getTime(), newProduct.getSalesRank()));	
		Product prod = new Product(newProduct.getId(), newProduct.getName(),
				newProduct.getModel(), newProduct.getImageUrl(), newProduct.getUrl(), 
				priceHistory, sellRankHistory, reviewHistory);		
		util.processNewProduct(prod);
		ProductData.getInstance().addProduct(Retailer.getRetailer(newProduct.getRetailerId()), newProduct.getCategoryId(), prod);
	}
	
	private void updateProduct(ProductSummary inUpdatedProduct){
		Product updatedProduct = ProductData.getInstance().getProduct(inUpdatedProduct.getId());
		updatedProduct.setName(inUpdatedProduct.getName());
		updatedProduct.setModelNo(inUpdatedProduct.getModel());
		updatedProduct.setImageUrl(inUpdatedProduct.getImageUrl());
		updatedProduct.setUrl(inUpdatedProduct.getUrl());
		//we can come here more than once for same product if an intraday updated product belongs to more than one category
		if(Math.abs(inUpdatedProduct.getPrice()-updatedProduct.getPriceHistory().getCurrPrice())>EPSILON)
			updatedProduct.getPriceHistory().getPriceTicks().add(new Tick(inUpdatedProduct.getDownloadTime().getTime(), inUpdatedProduct.getPrice()));
		if(ConfigParms.getInstance().isWebsiteMode()){
			List<Review> reviews = updatedProduct.getReviewHistory();
			if( reviews.size()==0 ||
					reviews.get(reviews.size()-1).getNumReviews()!=inUpdatedProduct.getNumReviews() || 
					reviews.get(reviews.size()-1).getReviewRating()!=inUpdatedProduct.getReviewRating())
				updatedProduct.getReviewHistory().add(new Review(inUpdatedProduct.getDownloadTime().getTime(), inUpdatedProduct.getReviewRating(), inUpdatedProduct.getNumReviews()));
		}
		util.processUpdatedProduct(updatedProduct);
		ProductData.getInstance().updateProductCategory(Retailer.getRetailer(inUpdatedProduct.getRetailerId()), inUpdatedProduct.getCategoryId(), updatedProduct);
	}

	public CategoryCacheImpl getCategoryCache(){
		return categoryCache;
	}
	
	/**
	 * @param retailer
	 * @return
	 */
	public List<Category> getRootCategories(String retailer){
		return getCategoryCache().getRootCategories(retailer);
	}
	
	//return category of given categoryId
	public Category getCategory(Integer categoryId){
		return categoryCache.getCategoryIdCategoryMap().get(categoryId);
	}

	//return parent category of given categoryId
	public Category getParentCategory(Category category){
		return this.getCategoryCache().getCategoryIdCategoryMap().get(category.getParentCategoryId());
	}
	/**
	 * 
	 * @param parentId
	 * @return child categories
	 */
	public List<Category> getChildCategories(Integer parentId){
		return this.getCategoryCache().getChildCategories(parentId);
	}
	
	//return true if this category is a parent category otherwise return false
	public boolean isParentCategory(Integer categoryId){
		return this.getCategoryCache().getCategoryIdCategoryMap().get(categoryId).isParent();
	}
	public void setCategoryCache(CategoryCacheImpl categoryCache){
		this.categoryCache = categoryCache;
	}
	
	
	//thrift api start here
	@Deprecated
	public List<Product> getProductsByIds(List<Integer> productIdList) {
		logger.info("getProductsByIds called");
		List<Product> products = new ArrayList<Product>(productIdList.size());
		for(Integer productId : productIdList){
			Product p = ProductData.getInstance().getProduct(productId);
			if(p!=null)//sanity check
				products.add(p);
			else{
				logger.error("Couldn't find product with id "+productId);
			}	
		}
		logger.info("returning "+products.size()+" products");
		return products;
	}
		
	@ Deprecated
	public thrift.genereated.retailer.Retailer getRetailer(String retailerId) {
		Retailer retailer = Retailer.getRetailer(retailerId.toLowerCase());
		thrift.genereated.retailer.Retailer thriftRetailer = new thrift.genereated.retailer.Retailer();
		if(retailer==null){
			logger.error("Not able to get the retailer for retailerId: "+retailerId.toLowerCase());
			return thriftRetailer;
		}
		thriftRetailer.setId(retailer.getId());
		thriftRetailer.setDisplayName(retailer.name());
		thriftRetailer.setUrl(retailer.getLink());
		thriftRetailer.setSortsSupported(new ArrayList<SortCriterion>());
		thriftRetailer.getSortsSupported().add(SortCriterion.PRICE_ASC);
		thriftRetailer.getSortsSupported().add(SortCriterion.PRICE_DESC);
		thriftRetailer.getSortsSupported().add(SortCriterion.DROP_PERCENTAGE);
		if(retailer.isSortedBySellRank())
			thriftRetailer.getSortsSupported().add(SortCriterion.BEST_SELLERS);
		if(retailer.hasReviews())
			thriftRetailer.getSortsSupported().add(SortCriterion.REVIEW_RATINGS);
		thriftRetailer.setDefaultSort(SortCriterion.DROP_PERCENTAGE);
		return thriftRetailer;
	}
	
	/**
	 * TODO: this method was added for website, and priceDrops.getPriceDropsForCategory call is inefficient
	 * Remove this method if not used.
	 * @param categoryId
	 * @param max
	 * @return
	 */

	@Deprecated
	public Map<String, ProductList> getPriceDropsByCategory(int categoryId, int max) {
		logger.info("DEBUG getPriceDropsByCategory("+categoryId+")");
		
		Map<String, ProductList> map = new HashMap<String, ProductList>();
		Map<Integer, List<Product>> childrenMap = priceDrops.getPriceDropsForCategory(categoryId);
		if(childrenMap!=null){
			for(Integer childCategory : childrenMap.keySet()){
				ProductList list = new ProductList();
				List<Product> products = childrenMap.get(childCategory);
				int size = products.size();
				list.products = new ArrayList<Product>();
				for(int index = 0 ; index<max && index<size ; ++index)
					list.products.add(products.get(index));
				list.totalCount = size;
				if(size != 0)
				{	
					String categoryName = this.categoryCache.getCategoryFromId(childCategory).categoryName;
					map.put(childCategory.toString() + "|$|" + categoryName, list);
				}
			}	
			
			//TODO: Is this too complicated?
			int addedProducts = 0;
			Map<Integer, Integer> countByCategory = new HashMap<>();
			ProductList list = new ProductList();
			list.products = new ArrayList<Product>();
			int addedProductsTillLastIteration = 0;
			while( addedProducts <= max )
			{	
				for(Integer category:childrenMap.keySet())
				{
					int currentIdxForCat = 0;
					if( countByCategory.get( category ) != null )
						currentIdxForCat = countByCategory.get( category );
					List<Product> products = childrenMap.get(category);
					int size = products.size();
					if(currentIdxForCat  < size )
					{
						list.products.add(products.get(currentIdxForCat));
						countByCategory.put(category, ++currentIdxForCat);
						addedProducts++;
					}	
					if( addedProducts >= max ) break;
				}
				if( addedProductsTillLastIteration == addedProducts )
					break;
				addedProductsTillLastIteration = addedProducts;
			}
			map.put("ROOT", list);
			
		}else{
			logger.error("Not able to get price drops for the category id: "+categoryId);
		}		
		printTopDropsReturnValue(map);
		return map;
	}
	
	public Map<String, ProductList> getPriceDropsByRetailer(String retailer, int max) {
		logger.info("getPriceDropsByRetailer("+retailer.toLowerCase()+")");
		Map<String, ProductList> map = new HashMap<String, ProductList>();
		Map<Integer, List<Product>> childrenMap = priceDrops.getPriceDropsForRetailer(retailer.toLowerCase());
		if(childrenMap!=null){
			ProductList list = new ProductList(childrenMap.get(0), childrenMap.get(0).size());
			map.put("ROOT|$|", list);
		}else{
			logger.error("Not able to get price drops for the retailer id: "+retailer);
		}
		printTopDropsReturnValue(map);
		return map;
	}

	
	public Map<String, ProductList> getPriceDropForProductFamily(String retailerId, List<String> urls, int max)
	{
		// Get the products
		Map<String, Product> productsByUrl = getProductsForURL(retailerId, urls);
		if(productsByUrl.size()==0 && urls.size()==1){
			logger.info("Can't find any product for given URL, try to find Category");
			return getPriceDropForCategoryUrl(urls.get(0), retailerId);
		}
		// Get occurrence count by category
		Map<Integer, Integer> occurrenceCountByCategory = new HashMap<>();
		List<Integer> categories;
		for(Product p : productsByUrl.values())
		{
			categories = ProductData.getInstance().getCategoriesOfProduct(p.getProductId());
			for(Integer category : categories )
			{
				if( occurrenceCountByCategory.containsKey( category ) )
				{	
					Integer occurrenceCount = occurrenceCountByCategory.get( category );
					occurrenceCountByCategory.put( category, ++occurrenceCount );
				}
				else
					occurrenceCountByCategory.put( category, 1);
			}
		}
		
		Map< String, ProductList > returnValue = new HashMap<>();
		
		//Get Price drops & put it in a map
		for(Integer categoryId : occurrenceCountByCategory.keySet() )
		{
			List<Product> dropsForCategory = ProductData.getInstance().getPriceDropsForCategory(categoryId);
			if(dropsForCategory==null){
				logger.info("No price drops for categoryid "+categoryId);
				continue;
			}
			ProductList list = new ProductList(dropsForCategory, dropsForCategory.size());
			Category catObj =  categoryCache.getCategoryFromId(categoryId);
			if( catObj != null)
				returnValue.put( catObj.categoryName + ":" + occurrenceCountByCategory.get(categoryId), list );
		}
		printTopDropsReturnValue(returnValue);
		return returnValue;
	}

	/**
	 * 
	 * @param url
	 * @return price drops for category
	 */
	private Map<String, ProductList> getPriceDropForCategoryUrl(String url, String retailer)
	{
		Map< String, ProductList > returnValue = new HashMap<>();
		Category catObj = categoryCache.getCategoryFromUrl(url);
		if(catObj==null){
			logger.warn("getPriceDropForCategoryUrl::Returning retailer level top drops");
			return getPriceDropsByRetailer(retailer, Integer.MAX_VALUE);
		}
		List<Product> dropsForCategory = ProductData.getInstance().getPriceDropsForCategory(catObj.getCategoryId());
		if(dropsForCategory==null){
			logger.info("No price drops for categoryid "+catObj.getCategoryId());
			logger.warn("getPriceDropForCategoryUrl::Returning retailer level top drops");
			return getPriceDropsByRetailer(retailer, Integer.MAX_VALUE);
		}
		ProductList list = new ProductList(dropsForCategory, dropsForCategory.size());
		returnValue.put( catObj.categoryName + ":$", list );
		printTopDropsReturnValue(returnValue);
		return returnValue;
	}

	/**
	 * This method will be used by plugin to get product information given urls
	 * @param retailerId
	 * @param urls
	 * @return
	 */
	public Map<String, Product> getProductsForURL(String retailerId, List<String> urls)
	{
		Map<String, Product> urlToProductMap = new HashMap<>();
		for(String url:urls){
			Product p = ProductData.getInstance().getProductForURL(retailerId, url);
			if(p!=null)
				urlToProductMap.put(url, p);
			else
				logger.warn("Could not find product for URL: "+url);
		}
		return urlToProductMap;
	}

	/**
	 * @param map
	 */
	private void printTopDropsReturnValue(Map<String, ProductList> map) {
		boolean debug=true;
		if(debug){
			for(Entry<String, ProductList> entry : map.entrySet()){
				logger.info(entry.getKey());
				for(Product p : entry.getValue().getProducts()){
					logger.info("\t"+
							p.getName()+
							", tm:"+new Date(getCurrentPriceTick(p.getPriceHistory()).getTime())+
							", cur:"+priceFormatter.get().format(p.getPriceHistory().getCurrPrice())+
							", prev:"+priceFormatter.get().format(getPreviousPriceTick(p.getPriceHistory()))+
							", avg:"+priceFormatter.get().format(p.getPriceHistory().getAveragePrice()));
					}
			}
		}
	}

	private static final Tick DUMMY_TICK = new Tick(System.currentTimeMillis(), Double.NaN);
	private Tick getCurrentPriceTick(PriceHistory ph){
		List<Tick> priceTicks = ph.getPriceTicks();
		if(priceTicks.size()==0)
			return DUMMY_TICK;
		else
			return priceTicks.get(priceTicks.size()-1);
	}

	private double getPreviousPriceTick(PriceHistory ph){
			List<Tick> priceTicks = ph.getPriceTicks();
			if(priceTicks.size()>1)
				return priceTicks.get(priceTicks.size()-2).getValue();
			else
				return getCurrentPriceTick(ph).getValue();
	}

	public Map<String, Product> getProductsForReceiptIds(String retailerId, List<String> receiptIds)
	{
		Map<String, Product> receiptidToProductMap = new HashMap<>();
		Map<String, String> receiptIdToUIDMap = ProductData.getInstance().getRetailerReceiptIdUIDMap().get(retailerId);
		Map<String, Product> UIDToProductMap = ProductData.getInstance().getRetailerUIDProductMap().get(retailerId);
		
		if( receiptIdToUIDMap != null )
		{	
			for(String receiptid:receiptIds)
			{
				String UID = receiptIdToUIDMap.get(receiptid);
				Product product = UIDToProductMap.get(UID);
				receiptidToProductMap.put(receiptid, product);
			}
		}
		return receiptidToProductMap;
	}


}

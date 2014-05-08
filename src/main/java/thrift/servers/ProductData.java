/**
 * 
 */
package thrift.servers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import parsers.util.ProductA4URI;
import parsers.util.ProductUID;
import thrift.genereated.retailer.LookupIdx;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.ProductFilter;
import thrift.genereated.retailer.ProductFilterType;
import thrift.genereated.retailer.ProductList;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.SortCriterion;
import thrift.servers.ProductComparators.ProductComparatorByPrice;
import thrift.servers.ProductComparators.ProductComparatorByReview;
import util.ConfigParms;
import util.Metric;
import entities.PriceMovementSummary;
import entities.Retailer;

/**
 * @author Ashish
 *
 */
public class ProductData {
	private static final Logger logger = Logger.getLogger(ProductData.class);
	private static ProductData realInstance = new ProductData();
	private static ProductData instance = realInstance;
	private Map<String, Map<String, Product> > retailerUIDProductMap = new HashMap<String, Map<String, Product>>();
	private Map<String, Map<String, String> > retailerReceiptIdUIDMap = new HashMap<String, Map<String, String>>();

	
	private Map<Integer, List<Product>> categoryProductsByPriceMap = new HashMap<Integer, List<Product>>();
	private Map<Integer, List<Product>> categoryProductsMap = new HashMap<Integer, List<Product>>();
	private Map<Integer, List<Product>> categoryProductsByReviewMap = new HashMap<Integer, List<Product>>();
	Map<Integer, List<Product>> categoryProductsByPriceDropMap;//Amit: TODO still testing and updater left of price drop maps
	
	public void setCategoryProductsByPriceDropMap(
			Map<Integer, List<Product>> _categoryProductsByPriceDropMap) {
		this.categoryProductsByPriceDropMap = _categoryProductsByPriceDropMap;
	}

	private Map<Integer, Product> productIdProductMap = new HashMap<Integer, Product>();
	private Map<Integer, List<Integer>> productIdCatgoryMap = new HashMap<Integer, List<Integer>>();
	private ProductComparatorByPrice productComparatorByPrice = new ProductComparatorByPrice();
	private ProductComparatorByReview productComparatorByReview = new ProductComparatorByReview();
	Metric timeIt = new Metric("TimeIt");

	private ProductData(){
		
	}
	
	public static ProductData getInstance(){
		return instance;
	}
	
	public static void resetInstanceUnitTest(){
		instance = realInstance;
	}

	public static void setInstanceUnitTest(ProductData override){
		instance = override;
	}
	/**
	 * remove all data
	 */
	public void resetUnitTest(){
		retailerUIDProductMap = new HashMap<String, Map<String, Product>>();
		retailerReceiptIdUIDMap =new HashMap<String, Map<String, String>>();
		categoryProductsByPriceMap = new HashMap<Integer, List<Product>>();
		categoryProductsMap = new HashMap<Integer, List<Product>>();
		categoryProductsByReviewMap = new HashMap<Integer, List<Product>>();
		categoryProductsByPriceDropMap = new HashMap<Integer, List<Product>>();
		
		productIdProductMap = new HashMap<Integer, Product>();
		productIdCatgoryMap = new HashMap<Integer, List<Integer>>();
	}
	
	public void addProduct(Retailer retailer, Integer categoryId, Product product){
		addProduct(retailer, product);
		addProductToCategory(retailer, categoryId, product);
	}

	public void addProduct(Retailer retailer, Product product){
		String uid = ProductUID.get(retailer.getId(), product.getUrl());
		if(!retailerUIDProductMap.containsKey(retailer.getId()))
			retailerUIDProductMap.put(retailer.getId(), new HashMap<String, Product>());
		retailerUIDProductMap.get(retailer.getId()).put(uid, product);
		this.productIdProductMap.put(product.getProductId(), product);
		
		String A4URI = ProductA4URI.get(retailer.getId(), product);
		if(!this.retailerReceiptIdUIDMap.containsKey(retailer.getId()))
			retailerReceiptIdUIDMap.put(retailer.getId(), new HashMap<String, String>());
		retailerReceiptIdUIDMap.get(retailer.getId()).put(A4URI, uid);
	}
	

	/**
	 * Maintain different category -> product map and product->category maps
	 * @param retailer
	 * @param categoryId
	 * @param product
	 */
	public void addProductToCategory(Retailer ignoreRetailer, Integer categoryId, Product product){
		if(!productIdCatgoryMap.containsKey(product.getProductId()))
			productIdCatgoryMap.put(product.getProductId(), new ArrayList<Integer>());
		productIdCatgoryMap.get(product.getProductId()).add(categoryId);		
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		if(!categoryProductsMap.containsKey(categoryId)){//we are seeing this category first time
			categoryProductsMap.put(categoryId, new ArrayList<Product>());
			if(ConfigParms.getInstance().isWebsiteMode()){
				categoryProductsByPriceMap.put(categoryId, new ArrayList<Product>());
//				if(retailer.hasReviews())
//					categoryProductsByReviewMap.put(categoryId, new ArrayList<Product>());
			}else{
				logger.info("Not website mode: skip creation of category->ProductsByPrice");
			}
		}
		categoryProductsMap.get(categoryId).add(product);
		categoryProductsByPriceMap.get(categoryId).add(product);
//		if(retailer.hasReviews())
//			categoryProductsByReviewMap.get(categoryId).add(product);
	}

	/**
	 * process product update received intraday
	 * @param retailer
	 * @param categoryId
	 * @param updatedProduct
	 */
	public void updateProductCategory(Retailer retailer, Integer categoryId, Product updatedProduct){
		boolean productAndCategoryNotLinked=false;
		if(!productIdCatgoryMap.containsKey(updatedProduct.getProductId()))
			productIdCatgoryMap.put(updatedProduct.getProductId(), new ArrayList<Integer>());
		List<Integer> categoryList = productIdCatgoryMap.get(updatedProduct.getProductId());
		if(!categoryList.contains(categoryId)){
			productIdCatgoryMap.get(updatedProduct.getProductId()).add(categoryId);
			productAndCategoryNotLinked=true;
		}
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		if(categoryProductsMap.containsKey(categoryId)){
			if(productAndCategoryNotLinked){
				//TODO: worry about concurrent modification exception
				categoryProductsMap.get(categoryId).add(updatedProduct);
				categoryProductsByPriceMap.get(categoryId).add(updatedProduct);
//				if(retailer.hasReviews())
//					categoryProductsByReviewMap.get(categoryId).add(updatedProduct);
			}
		}else{//come here when existing product is found under new category
			List<Product> productsList = new ArrayList<Product>();
			productsList.add(updatedProduct);
			categoryProductsMap.put(categoryId, productsList);
			categoryProductsByPriceMap.put(categoryId, new ArrayList<Product>(productsList));
//			if(retailer.hasReviews())
//				categoryProductsByReviewMap.put(categoryId, new ArrayList<Product>(productsList));
		}
	}
	
	/**
	 * called when a products are updated in given categories, sort products for this cateogry
	 * @param categoryList
	 */
	public void sortCategoryProductData(Set<Integer> categoryList){
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		logger.info("sorting category -> products maps");
		timeIt.start();
		for(int categoryId : categoryList){
			if(categoryProductsByPriceMap.containsKey(categoryId))
				Collections.sort(categoryProductsByPriceMap.get(categoryId), productComparatorByPrice);
			if(categoryProductsByReviewMap.containsKey(categoryId))
				Collections.sort(categoryProductsByReviewMap.get(categoryId), productComparatorByReview);
			
			//_categoryPriceFilterMap.put(categoryId, cacheHelp.getPriceFilterToNumProdMap(_categoryProductsByPriceMap.get(categoryId)));
			//_categoryReviewFilterMap.put(categoryId, cacheHelp.getReviewFilterToNumProdMap(_categoryProductsMap.get(categoryId)));
		}
		timeIt.end();
		logger.info("sortCategoryProductData...done, took " + timeIt.getProcessingTimeMinsSecs());
		timeIt.reset();
	}
	
 	public void sortAllCategoryProductsMap(){
		logger.info("sorting CategoryProductsMap");		
		for(List<Product> productList : categoryProductsByPriceMap.values())
			Collections.sort(productList, productComparatorByPrice);
		for(List<Product> productList : categoryProductsByReviewMap.values())
			Collections.sort(productList, productComparatorByReview);		
		logger.info("sorting CategoryProductsMap...done");
	}

	public void removeProduct(Retailer retailer, Integer categoryId, Product product){
		removeProduct(retailer, product);
		removeProductCategory(retailer, categoryId, product.getProductId());
	}

	public void removeProduct(Retailer retailer, Product product){
		String uid = ProductUID.get(retailer.getId(), product.getUrl());
		if(retailerUIDProductMap.containsKey(retailer.getId()))
			retailerUIDProductMap.get(retailer.getId()).remove(uid);

		String A4URI = ProductA4URI.get(retailer.getId(), product);
		if(!this.retailerReceiptIdUIDMap.containsKey(retailer.getId()))
			retailerReceiptIdUIDMap.get(retailer.getId()).remove(A4URI);
		this.productIdProductMap.remove(product.getProductId());
	}
	
	public void removeTerminalCategory(int categoryId){
		logger.info("Removing stale terminal category "+categoryId);
		categoryProductsByPriceMap.remove(categoryId);
		categoryProductsByReviewMap.remove(categoryId);
		List<Product> producsInCategory = categoryProductsMap.remove(categoryId);
		for(Product product : producsInCategory){
			if(productIdCatgoryMap.containsKey(product.getProductId())){//sanity check
				productIdCatgoryMap.get(product.getProductId()).remove(Integer.valueOf(categoryId));
			}
 		}//for(Product product : producsInCategory) ends...
	}

	/**
	 * remove product category links for this proudct
	 * @param retailer
	 * @param categoryId
	 * @param updatedProduct
	 */
	public void removeProductCategory(Retailer retailer, Integer categoryId, Integer productId){
		productIdCatgoryMap.remove(productId);
		Product removedProduct = productIdProductMap.remove(productId);
		if(removedProduct==null)
			logger.error("removedProduct called for nonexistent productid:"+productId);
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		if(categoryProductsMap.get(categoryId)!=null)
			categoryProductsMap.get(categoryId).remove(removedProduct);
		else
			logger.error("removedProduct called for nonexistent categoryid:"+categoryId);
		if(categoryProductsByPriceMap.get(categoryId)!=null)
			categoryProductsByPriceMap.get(categoryId).remove(removedProduct);
		else
			logger.error("removedProduct called for nonexistent categoryid:"+categoryId);
		if(retailer.hasReviews()){
			if(categoryProductsByReviewMap.get(categoryId)!=null)
				categoryProductsByReviewMap.get(categoryId).remove(removedProduct);
		}
	}
	

	public boolean containsProduct(Integer productId){
		return this.productIdProductMap.containsKey(productId);
	}

	public Product getProduct(Integer productId){
		return this.productIdProductMap.get(productId);
	}

	/**
	 * Return product for given URL
	 * @param retailerId
	 * @param url
	 * @return
	 */
	public Product getProductForURL(String retailerId, String url){
		if(retailerUIDProductMap.get(retailerId)==null){
			logger.warn("getProductsForURL::can't find productUID data for retailer="+retailerId+",url="+url);
			return null;
		}
		String uid = ProductUID.get(retailerId, url);
		Product p = retailerUIDProductMap.get(retailerId).get(uid);
		if(p==null)
			logger.warn("getProductsForURL::can't find productUID data for retailer="+retailerId+",url="+url);
		return p;
	}
	
	public Map<String, Map<String, Product>> getRetailerUIDProductMap() {
		return this.retailerUIDProductMap;
	}
	
	public Map<String, Map<String, String>> getRetailerReceiptIdUIDMap() {
		return this.retailerReceiptIdUIDMap;
	}
	
	public Map<Integer, List<Product>> getCategoryProductsByPriceMap() {
		return categoryProductsByPriceMap;
	}

	public Map<Integer, List<Product>> getCategoryProductsMap() {
		return categoryProductsMap;
	}

	public Map<Integer, List<Product>> getCategoryProductsByReviewMap() {
		return categoryProductsByReviewMap;
	}

	public Map<Integer, List<Integer>> getProductIdCategoryMap() {
		return productIdCatgoryMap;
	}

	public Map<Integer, Product> getProductIdToProductMap(){
		return this.productIdProductMap;
	}
	
	/**
	 * return all products in this case
	 * slow method since it iterate over a map to get this collection
	 * @return
	 */
	public Collection<Product> getProducts(){
		return this.productIdProductMap.values();
	}

	/**
	 * If reviewFilterMap is null that means we don't have to show the drop down
	 * If it is empty that mean though it is supported but, no entry in drop down
	 * 
	 * @param categoryId
	 * @param lookup
	 * @param filters
	 * @param sortCriterion
	 * @return
	 */
	public ProductList getProducts(int categoryId, LookupIdx lookup, List<ProductFilter> filters, SortCriterion sortCriterion) {
		logger.info("getProducts("+categoryId+") with filter size: "+filters.size()+" and sort: "+sortCriterion);
		
		
		List<Product> products;
		if(sortCriterion == SortCriterion.DROP_PERCENTAGE)
			products = categoryProductsByPriceDropMap.get(categoryId);
		else if((sortCriterion == SortCriterion.PRICE_ASC) || (sortCriterion == SortCriterion.PRICE_DESC))
			products = categoryProductsByPriceMap.get(categoryId);
		else if(sortCriterion == SortCriterion.REVIEW_RATINGS)
			products = categoryProductsByReviewMap.get(categoryId);
		else
			products = categoryProductsMap.get(categoryId);
		
		ProductList list = new ProductList();
		if(products != null){
			//list.priceFilterToNumProdMap = _categoryPriceFilterMap.get(categoryId);
			//list.reviewFilterToNumProdMap = _categoryReviewFilterMap.get(categoryId);
			
			for(ProductFilter filter : filters){
				if(filter.getType() == ProductFilterType.PRICE){
					products = filterProductsByPrice(products, filter);
				}
				else if(filter.getType() == ProductFilterType.REVIEW_RATINGS){
					products = filterProductsByReviewRatings(products, filter);
				}
			}
			
			int size = products.size();
			lookup.endIdx = Math.min(size, lookup.endIdx);
			int count = lookup.endIdx-lookup.startIdx;
			list.products = new ArrayList<Product>(count);
			
			if(sortCriterion == SortCriterion.PRICE_DESC){
				lookup.startIdx = size - lookup.startIdx - 1;
				lookup.endIdx = Math.max(lookup.startIdx - count, -1);
				for(int index = lookup.startIdx ; index>lookup.endIdx ; --index)
					list.products.add(products.get(index));
				list.totalCount = size;
				return list;
			}
			
			for(int index = lookup.startIdx ; index<lookup.endIdx ; ++index)
				list.products.add(products.get(index));
			list.totalCount = size;
		}
		else{
			list.priceFilterToNumProdMap = new HashMap<String, Integer>();//empty list but have to show the drop down
			list.reviewFilterToNumProdMap = null;//don't have to show review filter drop down
			list.totalCount = 0;
			list.products = new ArrayList<Product>();
			logger.error("Not able to get products for the category id: "+categoryId);
		}
		return list;
	}

	/**
	 * Having value >=from and <to
	 * @param products
	 * @param filter
	 * @return
	 */
	@Deprecated
	private List<Product> filterProductsByPrice(List<Product> products, ProductFilter filter){
		List<Product> newList = new ArrayList<Product>();
		for(Product product : products){
			if((product.getPriceHistory().getCurrPrice()-filter.from>=0)&&(filter.to-product.getPriceHistory().getCurrPrice()>0))
				newList.add(product);
		}
		return newList;
	}
	
	/**
	 * Having value >from and <=to
	 * @param products
	 * @param filter
	 * @return
	 */
	@Deprecated
	private List<Product> filterProductsByReviewRatings(List<Product> products, ProductFilter filter){
		List<Product> newList = new ArrayList<Product>();
		for(Product product : products){
			List<Review> rr = product.getReviewHistory();
			if(rr.size()>0){
				if((rr.get(rr.size()-1).getReviewRating()-filter.from>0)&&(filter.to-rr.get(rr.size()-1).getReviewRating()>=0))
					newList.add(product);
			} else {//unrated prods
				if(filter.from == -1 && filter.to == 0)
					newList.add(product);
			}
		}
		return newList;
	}

	//TODO ashish: this sorting should be all built in the add/update/remove calls, not controlled from outside
	public void processUpdatedCategories(Set<Integer> categoryList){
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		ProductData.getInstance().sortCategoryProductData(categoryList);
		for(int categoryId : categoryList)
			refreshCategoryProductsByPriceDropMap(categoryId);
	}

	/**
	 * category has newer price drops, recalc its map of Price Drops for new sorted values 
	 * @param categoryId
	 */
	private void refreshCategoryProductsByPriceDropMap(int categoryId){
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;
		logger.info("sorting updated price drops maps");
		if( priceDrops.mapOfCategoryToTopDropsSet.get(categoryId) != null )
		{	
			List<PriceMovementSummary> priceMovements = new ArrayList<>(priceDrops.mapOfCategoryToTopDropsSet.get(categoryId));
			List<Product> productsForCategory = new ArrayList<Product>();
			for(PriceMovementSummary priceMovement : priceMovements){
				Product prod = ProductData.getInstance().getProduct(priceMovement.getProductId());
				if(prod!=null) 
					productsForCategory.add(prod);
			}
			categoryProductsByPriceDropMap.put(categoryId, productsForCategory);
		}
		logger.info("sorting updated price drops maps...done");
	}
	
	TopPriceDrops2 priceDrops;
	public void setPriceDropsInstance(TopPriceDrops2 priceDrops){
		this.priceDrops = priceDrops;
	}
	
	public List<Integer> getCategoriesOfProduct(Integer productId){
		return productIdCatgoryMap.get(productId);
	}
	
	public List<Product> getPriceDropsForCategory(Integer categoryId){
		return priceDrops.getPriceDropsForThisCategory(categoryId);
	}
}

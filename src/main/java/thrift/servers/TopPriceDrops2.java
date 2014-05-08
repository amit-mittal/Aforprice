package thrift.servers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import datastruct.FixedSizeTreeSet;

import thrift.genereated.retailer.Category;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;
import util.ConfigParms;
import util.Metric;
import entities.PriceMovementSummary;

public class TopPriceDrops2 {
	private static final Logger logger = Logger.getLogger(TopPriceDrops2.class);
	//Maintains list mapping of category --> set of top drops objects (sorted as we use TreeSet)
	Map<Integer, Set<PriceMovementSummary>> mapOfCategoryToTopDropsSet = new HashMap<Integer, Set<PriceMovementSummary>>();
	//category -> list of top dropped products  to send it over thrift
	Map<Integer, List<Product>> mapOfCategoryToTopDropsList = new HashMap<Integer, List<Product>>();
	Map<String, Set<PriceMovementSummary>> mapOfRetailerToTopDropsSet = new HashMap<String, Set<PriceMovementSummary>>();
	Map<String, List<Product>> mapOfRetailerToTopDropsList = new HashMap<String, List<Product>>();
	Map<Integer, List<Product>> emptyPriceDrops = new HashMap<Integer, List<Product>>();
	Set<PriceMovementSummary> emptyPriceMovements = new TreeSet<PriceMovementSummary>();
	private ProductCacheImpl prodCache;
	private static final int MAX_PRODS_IN_ONE_CHILD = 20;
	private static final int MAX_TOP_DROPS_IN_CATEGORY = 100;
	long addPriceMovementToCategoryCallCount = 0;
	long topDropTouchCount=0;
	Metric timeIt = new Metric("TimeIt");

	public TopPriceDrops2(ProductCacheImpl prodCache){
		this.prodCache = prodCache;
	}
	
	/*
	 * Add price drops to terminal categories
	 */
	private void processPriveMovementUpdates(List<PriceMovementSummary> topDrops){
		//first adding them to terminal category, keep all products here in website mode even if they are not dropped since we need in terminal page display
		Map<Category, List<PriceMovementSummary>> tempCatToNewDrops = new HashMap<Category, List<PriceMovementSummary>>();
	
		//1.add to terminal category
		for(PriceMovementSummary topDrop : topDrops){
			List<Integer> categoryIdList = ProductData.getInstance().getCategoriesOfProduct(topDrop.getProductId());
			if(categoryIdList == null ) {	
				logger.warn("Category List is null for the given product " + topDrop.getProductId());
				continue;
			}	
			for(Integer categoryId : categoryIdList) {
				Category cat = prodCache.getCategory(categoryId);
				if(cat==null){	//possible since top drops query can return inactive categories
					//logger.error("Got price drop for unknown categoryId "+categoryId);
					continue;
				}

				List<PriceMovementSummary> tempList = new ArrayList<PriceMovementSummary>();
				tempList.add(topDrop);
				addTopDropToCategory(categoryId, tempList);
				//create top drop list to pass it on to parent together
				List<PriceMovementSummary> tempDrops = tempCatToNewDrops.get(cat);
				if(tempDrops == null){
					tempDrops = new ArrayList<PriceMovementSummary>();
					tempCatToNewDrops.put(cat, tempDrops);
				}
				/*Do not add non-top drop item to parents, they don't need it
				 * non-top drop item is required at terminal level to display it on the website, not needed for plugin
				 */
				if(topDrop.isPriceDrop())	
					tempDrops.add(topDrop);
			}
		}

		//start recursive call to add to its parent
		for(Map.Entry<Category, List<PriceMovementSummary>> entry : tempCatToNewDrops.entrySet()){
			Category cat = entry.getKey();
			Category parent = prodCache.getParentCategory(cat);
			if(parent==null){
				logger.error("Category "+cat.getCategoryId()+" has parent categoryid "+cat.getParentCategoryId()+", but missing in cache");
				return;
			}
			addTopDropsToMyParent(parent, entry.getValue());
		}
		logger.info("addPriceMovementToCategoryCallCount="+addPriceMovementToCategoryCallCount);
		logger.info("topDropTouchCount="+topDropTouchCount);
	}
	
	
	/**
	 * @retailerId -> retailerId of the price movement
	 * @categoryObject -> priceDrop needs to be added to the parent of this categoryId
	 * topDrops -> list of priceDrop objects
	 * Add drops to given categoryObject and makes recursive call to add it to its parent
	 */
	private void addTopDropsToMyParent(Category categoryObject, List<PriceMovementSummary> topDrops){
		addTopDropToCategory(categoryObject.getCategoryId(), topDrops);
		if(categoryObject.isRootCategory()){
			addTopDropToRetailer(categoryObject.getRetailerId(), topDrops);
			return;
		}
		
		Category parent = prodCache.getParentCategory(categoryObject);
		if(parent==null){
			logger.error("category "+categoryObject.getCategoryId()+" has parent categoryid "+categoryObject.getParentCategoryId()+", but missing in cache");
			return;
		}
		//detect circular link
		if(parent.getParentCategoryId()==categoryObject.getCategoryId()){
			logger.info("detected circular link for categoryid "+categoryObject.getCategoryId());
			return;
		}
		//recursive call
		addTopDropsToMyParent(parent, topDrops);
	}	

	/** Add price drop to given categoryId by updating mapOfCategoryToTopDropSet and mapOfCategoryToProductsList.
	 * @param categoryId
	 * @param newDrops
	 */
	private void addTopDropToCategory(Integer categoryId, List<PriceMovementSummary> newDrops){
		addPriceMovementToCategoryCallCount++;
		//first we add this new product to this category's map which will be used by its parent to update its list
		Set<PriceMovementSummary> topDrops = mapOfCategoryToTopDropsSet.get(categoryId);
		if( topDrops ==null){
			topDrops = new FixedSizeTreeSet<PriceMovementSummary>(MAX_TOP_DROPS_IN_CATEGORY);
			mapOfCategoryToTopDropsSet.put(categoryId, topDrops);
		}
		topDrops.addAll(newDrops);

		onCategoryTopDropUpdate(categoryId);
	}

	/** Recalculate mapOfCategoryToTopDropsList for this category
	 * @param categoryId
	 * @param topDrops
	 */
	private void onCategoryTopDropUpdate(Integer categoryId) {
		//Create list to send to PHP since it doesn't support set data structure
		List<Product> productsList = new ArrayList<Product>();
		Set<PriceMovementSummary> topDrops = mapOfCategoryToTopDropsSet.get(categoryId);
		int count=0;
		for(PriceMovementSummary topDrop : topDrops){
			topDropTouchCount++;
			Integer prodId = topDrop.getProductId();
			Product product = ProductData.getInstance().getProduct(prodId);
			if(product!=null)
				productsList.add(product);
			else
				continue;//logger.error(prodId+" not found in ProductIdProductMap");
			if(count++ > MAX_PRODS_IN_ONE_CHILD)
				break;
		}
		mapOfCategoryToTopDropsList.put(categoryId, productsList);
	}
	
	/** Add price drop to given categoryId by updating mapOfRetailerToTopDropsSet
	 * @param categoryId
	 * @param newDrops
	 */
	private void addTopDropToRetailer(String retailer, List<PriceMovementSummary> newDrops){
		addPriceMovementToCategoryCallCount++;
		//first we add this new product to this category's map which will be used by its parent to update its list
		Set<PriceMovementSummary> topDrops = mapOfRetailerToTopDropsSet.get(retailer);
		if( topDrops ==null){
			topDrops = new FixedSizeTreeSet<PriceMovementSummary>(MAX_TOP_DROPS_IN_CATEGORY);
			mapOfRetailerToTopDropsSet.put(retailer, topDrops);
		}
		topDrops.addAll(newDrops);
		onRetailerTopDropUpdate(retailer);
	}

	/** Recalculate mapOfRetailerToTopDropsSet for this category
	 * @param categoryId
	 * @param topDrops
	 */
	private void onRetailerTopDropUpdate(String retailer) {
		//Create list to send to PHP since it doesn't support set data structure
		List<Product> productsList = new ArrayList<Product>();
		Set<PriceMovementSummary> topDrops = mapOfRetailerToTopDropsSet.get(retailer);
		int count=0;
		for(PriceMovementSummary topDrop : topDrops){
			topDropTouchCount++;
			Integer prodId = topDrop.getProductId();
			Product product = ProductData.getInstance().getProduct(prodId);
			if(product!=null)
				productsList.add(product);
			else
				continue;//logger.error(prodId+" not found in ProductIdProductMap");
			if(count++ > MAX_PRODS_IN_ONE_CHILD)
				break;
		}
		mapOfRetailerToTopDropsList.put(retailer, productsList);
	}

	/**
	 * return price drops attached to this category
	 * @param categoryId
	 * @return
	 */
	public List<Product> getPriceDropsForThisCategory(Integer categoryId){
		return mapOfCategoryToTopDropsList.get(categoryId);
	}
	
	/**
	 * @param categoryId
	 * @return prices drops for given categoryId, in below format <br>
	 * Map: Integer -> List(Product)<br>
	 *  childCategoryId1 -> drops of childCategoryId1<br>
	 * 	childCategoryId2 -> drops of childCategoryId2<br>
	 */
	public Map<Integer, List<Product>> getPriceDropsForCategory(Integer categoryId){
		/*creating output on the fly now, so we can look into master copy of data in mapOfCategoryToTopDropsList
		otherwise creating new map up front gives issues when removing data from mapOfCategoryToTopDropsList which is not reflected in other maps*/
		
		Map<Integer, List<Product>> result = new HashMap<Integer, List<Product>>();
		Category inCategoryObject = prodCache.getCategory(categoryId);
		if(inCategoryObject==null){
			logger.error("No category found for category: " + categoryId);
			return emptyPriceDrops;
		}
		//get list of child Categories to get top drops, except same category for terminal
		List<Category> childCategories ;
		if(inCategoryObject.isParent()){
			childCategories = prodCache.getChildCategories(categoryId);
			if(childCategories==null){
				logger.error("No child category found for category: " + categoryId);
				return emptyPriceDrops;
			}			
		}else{
			childCategories = new ArrayList<Category>();
			childCategories.add(inCategoryObject);
		}
		for(Category childCat : childCategories){
			List<Product> products = mapOfCategoryToTopDropsList.get(childCat.getCategoryId());
			if(products!=null)//sanity check
				result.put(childCat.getCategoryId(), products);				
		}
		if(result.isEmpty()){
			logger.error("No price drops for category: "+categoryId);
			return emptyPriceDrops;
		} else 
			return result;
	}
	
	/**
	 * @param retailer
	 * @return prices drops for given retailerId, in below format <br>
	 * Map: Integer -> List(Product)<br>
	 *  rootCategoryId1 -> drops of rootCategoryId1<br>
	 * 	rootCategoryId2 -> drops of rootCategoryId1<br>
	 */
	public Map<Integer, List<Product>> getPriceDropsForRetailer(String retailer){
		if(!mapOfRetailerToTopDropsList.containsKey(retailer)){
			logger.error("No price drops for retailer: "+retailer);
			return emptyPriceDrops;
		} 
		Map<Integer, List<Product>> result = new HashMap<Integer, List<Product>>();
		result.put(0, mapOfRetailerToTopDropsList.get(retailer));
		return result;
	}
 
	
	/**
	 * 
	 * @param products
	 * @param retailerId
	 */
	public void addPriceDrops(Collection<Product> products){
		timeIt.start();
		List<PriceMovementSummary> priceMovements = convertToTopDropObjects(products);
		timeIt.end();
		logger.info("creating price drop objects...done, took " + timeIt.getProcessingTimeMinsSecs());
		timeIt.reset();
		timeIt.start();
		processPriveMovementUpdates(priceMovements);
		timeIt.end();
		logger.info("creating price drop tree...done, took " + timeIt.getProcessingTimeMinsSecs());
		timeIt.reset();
	}
	
	/**
	 * 
	 * @param products
	 * @param retailerId
	 */
	public void updatePriceDrops(List<Product> products, String retailerId){
		removePriceDropsForUpdatedProducts(products, retailerId);
		List<PriceMovementSummary> priceMovements = convertToTopDropObjects(products);
		processPriveMovementUpdates(priceMovements);
	}
	
	private void removePriceDropsForUpdatedProducts(List<Product> products, String retailerId)
	{
		for(Product prod: products)
		{
			//remove from categoryidToProductsByChildCategory & categoryProducts
			List<Integer> categories = ProductData.getInstance().getCategoriesOfProduct(prod.getProductId());
			for(Integer categoryId : categories)
			{
				//remove it from topDrops structures
				Set<PriceMovementSummary> pmsForCat = mapOfCategoryToTopDropsSet.get(categoryId);
				if(pmsForCat==null){
					logger.error("removePriceDropsForUpdatedProducts: mapOfCategoryToTopDropsSet is missing TopDrops for category " + categoryId);
					continue;
				}
				Set<PriceMovementSummary> removePMS = new HashSet<PriceMovementSummary>();
				for(PriceMovementSummary pm : pmsForCat) 
				{
					if(pm.getProductId().intValue() == prod.getProductId())
						removePMS.add(pm);
				}		
				pmsForCat.removeAll(removePMS);
				
				/* we will reduce the topDrops by 1 (say 19 instead of 20) by doing this 
				 * but normally this removal is part of the update so we will be back to 20 right after this operation is done
				 */
				List<Product> productsInCategory = mapOfCategoryToTopDropsList.get(categoryId);
				if(productsInCategory!=null){ //master list
					Set<Product> removeProducs = new HashSet<Product>();
					for(Product p: productsInCategory){
						if(p.getProductId()==prod.getProductId())
							removeProducs.add(p);
					}					
					productsInCategory.removeAll(removeProducs);
				}
			}//for(Integer categoryId : categories) ends
		}
	}

	
	/** Convert products -> top drop objects
	 * @param products
	 * @return
	 */
	private List<PriceMovementSummary> convertToTopDropObjects(Collection<Product> products){
		List<PriceMovementSummary> priceMovements = new ArrayList<>();
		logger.info("Create PriceMovementSummary objects");
		Metric timeItConversion = new Metric("TimeItConversion");
		timeItConversion.start();
		for(Product product : products){
			List<Tick> priceTicks = product.getPriceHistory().getPriceTicks();
			if(priceTicks.size()==0)//sanity check 
				continue;
			PriceMovementSummary topDrop = new PriceMovementSummary(product);
			if(ConfigParms.getInstance().isWebsiteMode() || topDrop.isPriceDrop())//non-price-drop not needed for plugin or iphone
				priceMovements.add(topDrop);
		}		
		timeItConversion.end();
		logger.info("Create PriceMovementSummary objects...done, took " + timeItConversion.getProcessingTimeMinsSecs());
		return priceMovements;
	}	
	/**
	 * print all categories which has more than 100 drops
	 */
	public void printTopDropsStats(){
		//print categories which have more than 100 entries
		logger.info("Category with >100 TopDrops: categoryid, drops_in_the_category");
		for(Map.Entry<Integer, Set<PriceMovementSummary>> entry : mapOfCategoryToTopDropsSet.entrySet()){
			int size = entry.getValue().size();
			if(size>100)
				logger.info(entry.getKey()+","+size);
		}
	}
}
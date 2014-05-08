package thrift.servers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Product;
import uploader.util.CategoryType;
import util.Metric;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import db.dao.ProductsDAO;
import entities.PriceMovementSummary;
import entities.Retailer;

/**
 * 
 * @author AMIT
 * Test has not been written
 */
public class ProductCacheImplHelper {
	private static final Logger logger = Logger.getLogger(ProductCacheImplHelper.class);
	private static final ProductsDAO prodDao = new ProductsDAO();
	private final List<Retailer> retailers;
	
	public ProductCacheImplHelper(List<Retailer> retailers, Integer numProducts) {
		this.retailers = retailers;
		init(numProducts);
	}
	
	/**
	 * Constructs main maps of ProductCacheImpl
	 * Basically dependent on Product_Summary, Product_Category table
	 * @param numProducts
	 */
	private void init(Integer numProducts){
		try{
			/*
			 * 1. Get products for given retailers
			 * 2. Populate productIdProductMap
			 */
			Metric timeIt = new Metric("TimeIt", TimeUnit.MILLISECONDS);
			logger.info("loading products from product_summary");
			timeIt.start();
			for(Retailer retailer : retailers){
				logger.info("loading products for retailer "+retailer.getId());
				List<Product> products = prodDao.getProductsThriftForRetailer(retailer.getId(), numProducts);
				for(Product product : products){
					ProductData.getInstance().addProduct(retailer, product);
				}
			}
			timeIt.end();
			logger.info("loading products from product_summary...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();
			/*
			 * 1. Get all active categories for a retailer
			 * 2. If that category id is terminal
			 * 	2a. Get all the active producIds
			 *  2b. Get product from prodId from productIdProductMap
			 *  2c. Populate all the other maps
			 */
			CategoryDAO reader = new CategoryDAO();
			logger.info("loading all categories");
			timeIt.start();
			for(Retailer retailer:retailers){
				logger.info("loading categories for retailer "+retailer.getId());
				List<entities.Category> dbCategories = reader.getActiveCategoriesForRetailer(retailer.getId());
				int terminalCategories=0;
				//count categories
				for(entities.Category dbCat:dbCategories)
					if(dbCat.getType()==CategoryType.TERMINAL)
						terminalCategories++;
				
				int fetchedCategories=0;
				logger.info("Total terminal categories="+terminalCategories);
				for(entities.Category dbCat:dbCategories){
					if(dbCat.getType()!=CategoryType.TERMINAL)
						continue;
					if((++fetchedCategories)%50==0)
						logger.info("fetchedCategories=" + fetchedCategories + "/" + terminalCategories );
					List<Integer> productIds = prodDao.getProductIdsForCategory(dbCat.getCategoryId());
					for(Integer prodId : productIds){
						Product prod = ProductData.getInstance().getProduct(prodId);
						if(prod==null)//possible if product is inactive and not removed from product_category table
							continue;
						ProductData.getInstance().addProductToCategory(retailer, dbCat.getCategoryId(), prod);
					}//for(Integer prodId : productIds) ends...
				}
				logger.info("loading retailer "+retailer.getId()+" done");
			}//for ends...
			timeIt.end();
			logger.info("loading all categories...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();
		} catch(SQLException e){
			logger.error(e.getMessage());
		} catch(DAOException e){
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Return map of category->Product sorted by Top Drop 
	 */
	public Map<Integer, List<Product>> getCategoryProductsByPriceDropMap(Map<Integer, Set<PriceMovementSummary>> categoryTopDrops){
		Map<Integer, List<Product>> categoryProductsByPriceDropMap = new HashMap<Integer, List<Product>>();
		for(Integer categoryId : categoryTopDrops.keySet()){
			categoryProductsByPriceDropMap.put(categoryId, new ArrayList<Product>());
			for(PriceMovementSummary priceMovement : categoryTopDrops.get(categoryId)){
				Product prod = ProductData.getInstance().getProduct(priceMovement.getProductId());
				if(prod!=null)
					categoryProductsByPriceDropMap.get(categoryId).add(prod);
//				else
//					logger.error("ProdId "+priceMovement.getProductId()+" not found while constructing category price drop map.");
			}
		}
		return categoryProductsByPriceDropMap;
	}
	
	/**
	 * Returns PriceFilterMap from a sorted list of products by price
	 * Used while initialization & after getting an update for a product 
	 * @param products
	 * @return
	 */
	/*
	public Map<String, Integer> getPriceFilterToNumProdMap(List<Product> products){
		int size = products.size();
		Map<String, Integer> priceFilterToNumProdMap = new LinkedHashMap<String, Integer>();
		if(size == 0)
			return priceFilterToNumProdMap;
		
		double minPrice = Math.floor(products.get(0).getPriceHistory().getCurrPrice());
		double maxPrice = Math.ceil(products.get(size-1).getPriceHistory().getCurrPrice()) + 1;//adding 1 as interval is open from right
		final int NUM_INTERVALS = Math.max(1, Math.min((int)(maxPrice - minPrice)/10, 4));//making sure we have at least 1 but max 4 intervals
		double diff = Math.ceil((maxPrice - minPrice)/NUM_INTERVALS);
		
		double start = minPrice;
		double end = minPrice + diff;
		int count = 0;
		for(int index = 0 ; index<size ; ++index){
			double price = products.get(index).getPriceHistory().getCurrPrice();
			if(price>=start && price<end){
				count++;
			} else {
				if(count>0){
					String key = (int)start+"_"+(int)end;
					priceFilterToNumProdMap.put(key, count);
				}
				count = 0;
				--index;
				start = end;
				end = Math.min(end+diff, maxPrice);
				logger.info("start="+start+", end="+end+", nextPrice="+price);
				if(price >=end){
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					logger.info("infinite loop");
				}
			}
		}
		if(count>0){
			String key = (int)start+"_"+(int)end;
			priceFilterToNumProdMap.put(key, count);
		}
		return priceFilterToNumProdMap;
	}
	*/
	
	/**
	 * Returns ReviewFilterMap from a list of products(sorted NOT reqd)
	 * Used while initialisation & after getting an update for a product
	 * @param products
	 * @return
	 */
	/*
	public Map<String, Integer> getReviewFilterToNumProdMap(List<Product> products){
		if(products == null)
			return null;
		int size = products.size();
		Map<String, Integer> reviewFilterToNumProdMap = new LinkedHashMap<String, Integer>();
		if(size == 0)
			return reviewFilterToNumProdMap;
		
		int[] numProds = new int[6];
		for(int index = 0 ; index<size ; ++index){
			int reviewHistorySize = products.get(index).getReviewHistorySize();
			if(reviewHistorySize>0){
				double rating = products.get(index).getReviewHistory().get(reviewHistorySize-1).getReviewRating();
				numProds[(int)Math.ceil(rating)]++;
			} else {
				numProds[0]++;
			}
		}
		
		for(int rating = -1 ; rating<5 ; ++rating){
			String key = rating+"_"+(rating+1);
			reviewFilterToNumProdMap.put(key, numProds[rating+1]);
		}
		return reviewFilterToNumProdMap;
	}
	*/
	/**
	 * Constructs All the FilterMaps
	 * Price and Review Filter
	 */
	/*
	public void constructCategoryFilterMaps(){
		logger.info("constructing Category->PriceFilter & Category->ReviewFilter Map");
		for(Integer categoryId : _categoryProductsByPriceMap.keySet()){
			List<Product> products =  .get(categoryId);
			_categoryPriceFilterMap.put(categoryId, getPriceFilterToNumProdMap(products));
			_categoryReviewFilterMap.put(categoryId, getReviewFilterToNumProdMap(products));
		}
		logger.info("constructing Category->PriceFilter & Category->ReviewFilter Map...done");
	}
	*/

}

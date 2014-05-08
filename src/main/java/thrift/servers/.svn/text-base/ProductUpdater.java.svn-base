package thrift.servers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Product;
import util.ConfigParms;
import util.Constants;
import util.Emailer;
import util.ConfigParms.RUNTIME_MODE;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import entities.Retailer;

public class ProductUpdater extends TimerTask /*implements Runnable*/{
	private static final Logger logger = Logger.getLogger(ProductUpdater.class);
	private static final ProductsDAO prodDao = new ProductsDAO();
	private ProductCacheImpl cache;
	private Timestamp latestModifiedTime;
	private List<ProductSummary> emptyProductSummary = new ArrayList<ProductSummary>();
	
	/**
	 * @param cache - reference to the cache to be updated
	 * @latestModifiedTime - starting point in the PRODUCT_SUMMARY table
	 */
	public ProductUpdater(ProductCacheImpl cache, Timestamp latestModifiedTime ){
		this.cache = cache;
		this.latestModifiedTime = latestModifiedTime;
	}
	public void run() {
		try {
			doWork();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			StringWriter sw = new StringWriter();
			PrintWriter p = new PrintWriter(sw);
			e.printStackTrace(p);
			if(!ConfigParms.isUnitTestMode())
				Emailer.getInstance().sendText("ProductUpdater Exception restarting Retailer Cache", sw.toString());
			System.exit(Constants.RESTARTABLE_ERROR);
		}		
	}
	
	public void doWorkRegTest() throws SQLException{
		doWork();
	}
	private void doWork() throws SQLException{
		List<ProductSummary> products = pollDbForUpdates();
		if(products.size()>0)
			processUpdates(products);
	}
	
	private List<ProductSummary> pollDbForUpdates() throws SQLException{
		Timestamp newModifiedTime = prodDao.getProductMaxModifiedTime();
		if(newModifiedTime.before(latestModifiedTime)){//possible if we are not getting any products
			logger.info("no new products after "+latestModifiedTime);
			return emptyProductSummary;
		}
		
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		for(Retailer retailer : cache.retailers){
			logger.info("Getting updated products for retailer: "+retailer.getId()+" after "+latestModifiedTime);
			List<ProductSummary> updatedProducts = prodDao.getUpdatedProducts(retailer.getId(), latestModifiedTime);
			logger.info("Got "+updatedProducts.size()+" updated products");
			products.addAll(updatedProducts);
		}
		//if new time is same as old time, then add 1 sec so we don't keep getting same products over and over
		if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST && newModifiedTime.equals(latestModifiedTime))//no optimization in test mode, it messes it up 
										//since time_modified field may not be different for two different rows for one product inserted very quickly
			latestModifiedTime = new Timestamp(latestModifiedTime.getTime()+1000);
		else
			latestModifiedTime = newModifiedTime;
		return products;
	}
	
	public void processUpdates(List<ProductSummary> products){
		//update product name, url, price etc. update category-product mapping
		cache.updateProducts(products);
		//Potential impact to top drops so process that
		Map<String, List<Product>> updatedProductsByRetailer = new HashMap<>();
		for(ProductSummary prod : products )
		{	
			if(!updatedProductsByRetailer.containsKey(prod.getRetailerId()))
				updatedProductsByRetailer.put(prod.getRetailerId(), new LinkedList<Product>());
			updatedProductsByRetailer.get(prod.getRetailerId()).add(ProductData.getInstance().getProduct(prod.getId()));
		};
		for(String retailerId : updatedProductsByRetailer.keySet())
			cache.updatePriceDrops(updatedProductsByRetailer.get(retailerId), retailerId);	

		processUpdatedCategories(products);
	}

	/**
	 * re-sort relevant categories which products are updated
	 * @param products
	 */
	public void processUpdatedCategories(List<ProductSummary> products){
		if(!ConfigParms.getInstance().isWebsiteMode())
			return;	
		Set<Integer> categoryList = new HashSet<Integer>();
		for(ProductSummary product : products){
			List<Integer> productCategoryList = ProductData.getInstance().getCategoriesOfProduct(product.getId());
			if(productCategoryList == null){//TODO checking just to be sure, this error should not come in production
				logger.error("SERIOUS: ProductId "+product.getId()+" not found in _productCategoryMap");
				continue;
			}
			for(Integer categoryId : productCategoryList)
				categoryList.add(categoryId);
		}
		//re-sort category maps
		ProductData.getInstance().processUpdatedCategories(categoryList);
	}

	public Timestamp getLatestModifiedTime() {
		return latestModifiedTime;
	}
	
	public void shutdown(){
		this.cancel();
	}
}

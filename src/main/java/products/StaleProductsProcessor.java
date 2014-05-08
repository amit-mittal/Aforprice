package products;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import servers.ProductDetailsDownloadServer;
import util.ConfigParms;
import util.DateTimeUtils;
import db.dao.ProductsDAO;
import entities.ProductSummary;

public class StaleProductsProcessor {

	private static final Logger logger = Logger.getLogger(StaleProductsProcessor.class);
	private static final ProductsDAO prodDao = new ProductsDAO();

	/**
	 * Download all the stale products i.e. the products which could not be reached by traversing
	 * through categories of a retailer.
	 * @param products stale products to be download again.
	 */
	public static void process(Date reconDate, String[] retailers){
		for(String retailer: retailers){			
			try {
				Map<String, Set<ProductSummary>> products = prodDao.getDownloadedProductsOnDate(reconDate, retailer, -1, ConfigParms.DOWNLOAD_MODE.ALL);			
				logger.info("Getting all products for " + retailer);
				Map<String, Set<ProductSummary>> productsAll = prodDao.getDownloadedProductsPreviousNDays(reconDate, retailer, -1, 7, ConfigParms.DOWNLOAD_MODE.ALL);
				Set<ProductSummary> stale = getStaleProducts(products, productsAll);
				logger.info("Total stale products for " + retailer + " = " + stale.size());
				ProductDetailsDownloadServer.get().download(stale);
			} catch (SQLException e) {				
				logger.error(e.getMessage(), e);
			}		
		}
		ProductDetailsDownloadServer.get().publish(retailers);
	}
	
	private static Set<ProductSummary> getStaleProducts(Map<String, Set<ProductSummary>> products, Map<String, Set<ProductSummary>> productsAll){
		Set<ProductSummary> staleProducts = new HashSet<ProductSummary>();								
		for(Map.Entry<String, Set<ProductSummary>> entry: products.entrySet()){											
			String prodNameKey = entry.getKey();
			Set<ProductSummary> prodList = entry.getValue();			
			//remove all the products which were received today from the previous days product map.
			if(productsAll.containsKey(prodNameKey)){
				productsAll.get(prodNameKey).removeAll(prodList);
				if(productsAll.get(prodNameKey).size() == 0)
					productsAll.remove(prodNameKey);
			}			
		}
		for(Map.Entry<String, Set<ProductSummary>> entry: productsAll.entrySet()){			
			staleProducts.addAll(entry.getValue());
		}
		return staleProducts;

	}
	
	public static void main(String[] args){
		String[] retailers = {"lowes"};
		process(DateTimeUtils.dateFromyyyyMMdd("20120826"), retailers);
	}

}

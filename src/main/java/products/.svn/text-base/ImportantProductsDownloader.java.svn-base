package products;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import servers.ProductDetailsDownloadServer;
import util.ConfigParms;
import db.dao.DAOException;
import db.dao.PriceAlertDAO;
import db.dao.ProductsDAO;
import entities.PriceAlert;
import entities.ProductSummary;

public class ImportantProductsDownloader
{
	private static final Logger logger = Logger.getLogger(StaleProductsProcessor.class);
	private static final PriceAlertDAO alertDao = new PriceAlertDAO();
	private static final ProductsDAO productDao = new ProductsDAO();
	private static final int DOWNLOAD_INTERVAL = 5;
	
	/**
	 * Download all IMPORTANT products more often  
	 * @throws DAOException 
	 * 
	 */
	public static void process(String[] retailers) throws InterruptedException, SQLException, DAOException{
		
		while(true)
		{	
			//Extract important products
			Set<ProductSummary> fastDownloandProducts = getFastDownloadProducts();
			logger.info("Total products requiring frequent downloads "  + fastDownloandProducts.size());

			//Download important products
			ProductDetailsDownloadServer.get().download(fastDownloandProducts);
			ProductDetailsDownloadServer.get().publish(retailers);
			Thread.sleep(DOWNLOAD_INTERVAL*60*1000);
		}
	}
	
	private static Set<ProductSummary> getFastDownloadProducts() throws DAOException, SQLException
	{
		Set<ProductSummary> fastDownloadProducts = new HashSet<>();
		List<PriceAlert> activeAlerts = alertDao.getAllActiveAlerts();
		for(PriceAlert alert : activeAlerts)
		{
			fastDownloadProducts.add(productDao.getProductSummaryByProductId(alert.getProductId()));
		}
		return fastDownloadProducts;
	}

	public static void main(String[] args){
		try
		{
			//ConfigParms.getInstance().setMode(ConfigParms.RUNTIME_MODE.UNITTEST);
			String[] retailers = {"toysrus"};
			process( retailers);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
	}

}

		
package parsers.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import parsers.ProductsParser;
import parsers.ProductsParserFactory;

import stores.ProductStore;
import util.YesNo;
import util.build.ProductsDownloadRetryBuilder;

import db.dao.DAOException;
import db.dao.ProductsDownloadRetryDAO;
import entities.ProductsDownloadRetry;

public class ProductsDownloadRetryManager {

	private ProductsDownloadRetryDAO dao = new ProductsDownloadRetryDAO();
	private static final Logger LOGGER = Logger.getLogger(ProductsDownloadRetryManager.class);
	
	public enum RETRY_REASON{
		EXCEPTION, LESS_THAN_EXPECTED
	}
	public ProductsDownloadRetryManager() {
	}
	
	public void retryLater(String retailer, int catId, String startUrl, RETRY_REASON reason){
		try{
			ProductsDownloadRetryBuilder b = new ProductsDownloadRetryBuilder();
			b.categoryId = catId;
			b.createTime = new Date();
			b.processd = YesNo.N;
			b.retailerId = retailer;
			b.retryReason = reason.name();
			b.startUrl = startUrl;
			dao.insert(b.build());
		}catch(Exception e){
			LOGGER.error("Unable to set for retry: catId=" + catId + ", startUrl=" + startUrl, e);
		}
	}
	
	public void retryUnprocessed(String retailer){
		ProductsDownloadState state = ProductsDownloadState.get(retailer);
		Date downloadStartTime = state.getStartTime();
		try {
			List<ProductsDownloadRetry> retries = dao.getDownloadsToRetry(retailer, downloadStartTime);
			ProductsParser parser = ProductsParserFactory.get(retailer);
			ProductStore store = ProductStore.Factory.get();
			for(ProductsDownloadRetry retry: retries){
				try {
					parser.parseAndSave(retry.getCategoryId(), retry.getCategoryName(), retry.getStartUrl(), store);
				} catch (IOException | DAOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

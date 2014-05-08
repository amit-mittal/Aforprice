package db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.Metric;

import entities.ProductSummary;

public class ProductCompositeAttributeHistoryDAO {

	private static List<Metric> metricsEndInsertBatch = new ArrayList<>();
	private static List<Metric> metricsInsertBatch = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(ProductCompositeAttributeHistoryDAO.class);
	
	static{
		metricsEndInsertBatch.add(new Metric("ProductPricesHistory_endInsertBatch"));
		metricsInsertBatch.add(new Metric("ProductPricesHistory_insertBatch"));
		metricsEndInsertBatch.add(new Metric("ProductReviewsHistory_endInsertBatch"));
		metricsInsertBatch.add(new Metric("ProductReviewsHistory_insertBatch"));
		/* comment out sell rank inserts for now since we need to add category-product based sell rank
		metricsEndInsertBatch.add(new Metric("ProductSellRanksHistory_endInsertBatch"));
		metricsInsertBatch.add(new Metric("ProductSellRanksHistory_insertBatch"));
		*/
	}
	private List<IProductAttributeHistoryDAO> historyDAOs = new ArrayList<>();

	public ProductCompositeAttributeHistoryDAO() {
		historyDAOs.add(new ProductPricesHistoryDAO());
		historyDAOs.add(new ProductReviewsHistoryDAO());
		//historyDAOs.add(new ProductSellRanksHistoryDAO());
	}

	public void startInsertBatch() throws SQLException {
		for(IProductAttributeHistoryDAO dao: historyDAOs){
			dao.startInsertBatch();
		}
	}

	public void startInsertBatch(Connection sqlConn) throws SQLException {
		for(IProductAttributeHistoryDAO dao: historyDAOs){
			dao.startInsertBatch(sqlConn);
		}
	}

	public void endInsertBatch() throws SQLException {
		for(int i = 0; i < historyDAOs.size(); i++){
			IProductAttributeHistoryDAO dao =  historyDAOs.get(i);
			Metric m = metricsEndInsertBatch.get(i);
			m.start();
			dao.endInsertBatch();
			m.end();
			if(!ConfigParms.isUnitTestMode()){	
				if(metricsInsertBatch.get(i).average(TimeUnit.MILLISECONDS)>0)
					logger.info("\t" + metricsInsertBatch.get(i).currentStats());
				logger.info("\t" + m.currentStats());
			}
		}		
	}

	public void insertBatch(ProductSummary prod, ProductSummary existing,
			boolean flush) throws SQLException {
		for(int i = 0; i < historyDAOs.size(); i++){
			IProductAttributeHistoryDAO dao =  historyDAOs.get(i);
			Metric m = metricsInsertBatch.get(i);
			m.start();
			dao.insertBatch(prod, existing, flush);
			m.end();
		}
	}
	
	public void finalize(){
		for(IProductAttributeHistoryDAO dao: historyDAOs){
			dao.finalize();
		}
	}
}
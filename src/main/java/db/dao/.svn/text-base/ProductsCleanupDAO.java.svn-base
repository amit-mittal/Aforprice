/**
 * 
 */
package db.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.Queries;
import entities.ProductSummary;

import util.ConfigParms;
import util.DateTimeUtils;
import util.Emailer;
import util.Metric;
import util.ConfigParms.RUNTIME_MODE;

/**
 * @author Ashish
 *
 */
public class ProductsCleanupDAO extends DataAccessObject{
	private final static Logger logger = Logger.getLogger(ProductsCleanupDAO.class);

	//keep it very high for QA since we don't run active downloader there
	private final int PRODUCT_STALE_THRESHOLD_DAYS_QA = 30; //marked inactive if not downloaded for this period
	private final int PRODUCT_REMOVE_THRESHOLD_DAYS_QA = 365; //moved to stale table if not downloaded for this period

	private final int PRODUCT_STALE_THRESHOLD_DAYS = 7; //marked inactive if not downloaded for this period
	private final int PRODUCT_REMOVE_THRESHOLD_DAYS = 30; //moved to stale table if not downloaded for this period
	Metric metGetStaleProducts = new Metric("GetStaleProducts");
	Metric metMarkStaleProducts = new Metric("MarkStaleProducts");
	Metric metGetVeryStaleProducts = new Metric("GetVeryStaleProducts");
	Metric metMarkVeryStaleProducts = new Metric("MarkVeryStaleProducts");
	final int MAX_FAILURE = 10;
	//mark any stale products in active if not downloaded for PRODUCT_STALE_THRESHOLD_DAYS
	public int markStaleProductsInactive() throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		
		try {
			Calendar dateThreshold;
			if(ConfigParms.getInstance().isProduction())
				dateThreshold = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), PRODUCT_STALE_THRESHOLD_DAYS);
			else
				dateThreshold = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), PRODUCT_STALE_THRESHOLD_DAYS_QA);
			logger.info("Mark products inactive if not downloaded after " + dateThreshold);
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			
			stmt = sqlConn.prepareStatement(Queries.MARK_STALE_PRODUCTS_AS_INACTIVE);
			stmt.setTimestamp(1, new Timestamp(dateThreshold.getTimeInMillis()));
			String query = stmt.toString();			
			logger.info("Executing query: "+query.substring(query.indexOf("UPDATE")));
			metMarkStaleProducts.start();
			int count = stmt.executeUpdate();
			metMarkStaleProducts.end();
			closeStmt(stmt);
			
			logger.info("Marked "+count+" products inactive");
			logger.info(metMarkStaleProducts.currentStats());
			return count;
		}catch(SQLException e ){			
			logger.error(e.toString(), e);
			generateAlert("Error in marking products inactive" + e.getMessage());
			throw e;
		}
		finally{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		
	}
	
	//move products to stale tables if not downloaded for PRODUCT_REMOVE_THRESHOLD_DAYS
	public int moveVeryStaleProductsToStaleTables(){
		if(ConfigParms.getInstance().isProduction()){
			logger.info("skipping product archiving in production");
			return 0;
		}
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, stmt=null;
		ResultSet resultsRS = null;
		int count=0;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			//get products from product_summary which are stale for PRODUCT_REMOVE_THRESHOLD_DAYS
			Calendar dateThreshold;
			if(ConfigParms.getInstance().isProduction())
				dateThreshold = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), PRODUCT_REMOVE_THRESHOLD_DAYS);
			else
				dateThreshold = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), PRODUCT_REMOVE_THRESHOLD_DAYS_QA);
			logger.info("Move products to stale tables if not downloaded after " + dateThreshold.getTime());
			prodStmt = sqlConn.prepareStatement(Queries.GET_VERY_STALE_PRODUCTS);
			prodStmt.setTimestamp(1, new Timestamp(dateThreshold.getTimeInMillis()));
			String query = prodStmt.toString();			
			logger.info("Executing query: "+query.substring(query.indexOf("SELECT")));
			metGetVeryStaleProducts.start();
			resultsRS = prodStmt.executeQuery();
			metGetVeryStaleProducts.end();
			logger.info(metGetVeryStaleProducts.currentStats());
			int failureCount=0;
			while(resultsRS.next()){
				count++;
				Integer productId = resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID);
				java.util.Date lastDownloadTime = resultsRS.getTimestamp(ProductSummary.Columns.LAST_DOWNLOAD_TIME);
				try{
					String[] tables = new String[]{"PRODUCT_PRICES_HISTORY", "PRODUCT_REVIEWS_HISTORY", "PRODUCT_SELL_RANKS_HISTORY", "PRODUCT_SUMMARY"};
					logger.info("purge "+productId+", lastDownload="+DateTimeUtils.currentDateYYYYMMDD(lastDownloadTime));
					for(int i=0;i<tables.length;i++){
						//copy to stale table, insert into select * from product_price_history, reviews_history, sellrank_history
						query = "INSERT INTO "+tables[i]+"_STALE SELECT * FROM "+tables[i]+" WHERE PRODUCT_ID=?";
						stmt = sqlConn.prepareStatement(query);
						stmt.setInt(1, productId);
						stmt.executeUpdate();
						closeStmt(stmt);

						//remove from main table
						query = "DELETE FROM "+tables[i]+" WHERE PRODUCT_ID=?";
						stmt = sqlConn.prepareStatement(query);
						stmt.setInt(1, productId);
						stmt.executeUpdate();
						closeStmt(stmt);
					}//for(int i=0;i<tables.length;i++) ends...
					
					//remove product_category entry, no need archive this since it will be restored automatically by migration
					//remove DAILY_PRICE_MOVEMENT_SUMMARY entry, it will reinsert when product is back in product-summary
					tables = new String[]{"PRODUCT_CATEGORY", "DAILY_PRICE_MOVEMENT_SUMMARY"};
					for(int i=0;i<tables.length;i++){		
						query = "DELETE FROM "+tables[i]+" WHERE PRODUCT_ID=?";
						stmt = sqlConn.prepareStatement(query);
						stmt.setInt(1, productId);
						stmt.executeUpdate();
						closeStmt(stmt);
					}//for(int i=0;i<tables.length;i++) ends...
					//TODO: create structure of table -> action, action = ARCHIEVE or DELETE. ARCHIVE will copy it to stale table, DELETE will remove it completely
				}catch(Exception ex){
					logger.error("Error in purging productId"+productId);
					ex.printStackTrace();
					failureCount++;
					generateAlert(productId, ex.getMessage(), failureCount);
				}
				if(failureCount>MAX_FAILURE){
					logger.error("Too many errors, exceeded threshold "+MAX_FAILURE+ " exiting");
					System.exit(1);
				}
			}//while(resultsRS.next()){ ends...
			logger.info("Archived "+count+" products");
		} catch (SQLException e) {
			e.printStackTrace();
			generateAlert(e.getMessage());
		} finally {		
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
		return count;
	}
	
	/*
	 * send email alert with error
	 */
	private void generateAlert(Integer productId, String error, int failureCount){
		String html = "";
		html = "Failure in purging product "+productId+"	<br>";
		html += "Exception:"+error+"<br>";
		html += "Failure Count:"+failureCount+"	<br>";
		if(failureCount>MAX_FAILURE)
			html += "program will stop now after exceeding max error threshold " + MAX_FAILURE;
		generateAlert(html);
		
	}
	private void generateAlert(String error){
		java.util.Date runDate = new java.util.Date();
		String subject = DateTimeUtils.currentDateYYYYMMDD(runDate) + "- Product Cleanup Alert";
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append(error);
		html.append("</body></html>");
		Emailer.getInstance().sendHTML(subject, html.toString());
	}



}

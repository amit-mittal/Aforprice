package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.Queries;
import entities.PriceAlert;
import entities.PriceAlertType;
import thrift.genereated.pricealert.PRICE_ALERT_ERROR_CODE;
import thrift.genereated.pricealert.PriceAlertException;
import thrift.genereated.pricealert.PriceAlertThrift;
import thrift.genereated.pricealert.PriceAlertTypeThrift;
import util.DateTimeUtils;
import util.ValErr;
import util.build.PriceAlertBuilder;

public class PriceAlertDAO extends DataAccessObject{

	private static final Logger logger = Logger.getLogger(PriceAlertDAO.class);
	ReentrantLock lock = new ReentrantLock();
	
	public int getUniqueAlertId() throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		AtomicInteger alertId = null;

		try{
			lock.lock();
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_MAX_ALERT_ID);
			alertRS = alertStmt.executeQuery();
			while(alertRS.next())
				alertId = new AtomicInteger(alertRS.getInt(1));
			
			if(alertId != null)
				return alertId.incrementAndGet();
			return 1;
		}
		finally{
			lock.unlock();
			closeStmt(alertStmt);
			closeRS(alertRS);
			pool.releaseConnection(conn);
		}
	}
	
	public int addPriceAlert(PriceAlert alert) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.INSERT_PRICE_ALERT);
			int alertId = getUniqueAlertId();
			
			int i=0;
			alertStmt.setInt(++i, alertId);
			alert.setAlertId(alertId);
			alertStmt.setString(++i, alert.getEmailId());
			alertStmt.setInt(++i, alert.getProductId());
			alertStmt.setDouble(++i, alert.getAlertPrice());
			alertStmt.setTimestamp(++i, new Timestamp(alert.getTimeModified().getTime()));
			alertStmt.setTimestamp(++i, new Timestamp(alert.getAlertStartTime().getTime()));
			alertStmt.setTimestamp(++i, new Timestamp(alert.getAlertEndTime().getTime()));
			alertStmt.setString(++i, alert.getAlertType().toString());
			alertStmt.setBoolean(++i, alert.isActive());
			alertStmt.executeUpdate();
			logger.info("addAlert: "+alert);
			
			return alertId;
		}
		finally{
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/**
	 * copy current alert to archive table
	 * update current alert
	 * @param alert
	 * @throws SQLException
	 * @throws PriceAlertException 
	 */
	public void updatePriceAlert(PriceAlert alert) throws SQLException, PriceAlertException{
		if(getPriceAlert(alert.getAlertId())==null)
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.INVALID_ALERTID, String.format(ValErr.ERR_ALERT_INVALID_ALERTID, alert.getAlertId()));

		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement insertHistoryStmt = null;
		PreparedStatement alertStmt = null;
		logger.info(alert);
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			//copy to archive
			insertHistoryStmt = sqlConn.prepareStatement(Queries.INSERT_PRICE_ALERT_ARCHIVE);
			insertHistoryStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			insertHistoryStmt.setInt(2, alert.getAlertId());
			insertHistoryStmt.executeUpdate();
			
			//update
			alertStmt = sqlConn.prepareStatement(Queries.UPDATE_PRICE_ALERT);
			int i=0;
			alertStmt.setString(++i, alert.getEmailId());
			alertStmt.setDouble(++i, alert.getAlertPrice());
			alertStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			alertStmt.setTimestamp(++i, new Timestamp(alert.getAlertEndTime().getTime()));
			alertStmt.setString(++i, alert.getAlertType().toString());
			alertStmt.setBoolean(++i, alert.isActive());
			alertStmt.setInt(++i, alert.getAlertId());
			alertStmt.executeUpdate();
			logger.info("updateAlert: "+alert);
		}
		finally{
			closeStmt(insertHistoryStmt);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public boolean updatePriceAlertStatus(int alertId, boolean status) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.UPDATE_STATUS_PRICE_ALERT);
			int i=0;
			alertStmt.setBoolean(++i, status);
			alertStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			alertStmt.setInt(++i, alertId);			
			return alertStmt.executeUpdate()==1;
		}
		finally{
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public PriceAlert getPriceAlert(int alertId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_PRICE_ALERT);
			alertStmt.setInt(1, alertId);
			
			alertRS = alertStmt.executeQuery();
			if(alertRS.next()){
				PriceAlert alert = getPriceAlert(alertRS);
				return alert;
			}
			return null;
		}
		finally{
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public PriceAlert getActivePriceAlert(String emailId, int productId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_PRICE_ALERT_FROM_USER_PRODUCT_ID);
			alertStmt.setString(1, emailId);
			alertStmt.setInt(2, productId);
			
			alertRS = alertStmt.executeQuery();
			if(alertRS.next()){//TODO: here assuming user can setup only unique active alert for a product
				PriceAlert alert = getPriceAlert(alertRS);
				return alert;
			}
			return null;
		}
		finally{
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public List<PriceAlertThrift> getAllActivePriceAlertsOfUser(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		List<PriceAlertThrift> alerts = new ArrayList<PriceAlertThrift>();		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_ALL_PRICE_ALERTS_OF_USER);
			alertStmt.setString(1, emailId);
			
			alertRS = alertStmt.executeQuery();
			while(alertRS.next()){
				PriceAlertThrift alert = getPriceAlertThrift(alertRS);
				alert.emailId = emailId;
				alerts.add(alert);
			}
			return alerts;
		}
		finally{
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	private PriceAlertThrift getPriceAlertThrift(ResultSet result) throws SQLException{		
		PriceAlertThrift priceAlertThrift = new PriceAlertThrift();
		priceAlertThrift.alertId= result.getInt(PriceAlert.Columns.ALERT_ID);
		priceAlertThrift.productId = result.getInt(PriceAlert.Columns.PRODUCT_ID);
		priceAlertThrift.alertPrice = result.getDouble(PriceAlert.Columns.ALERT_PRICE);
		priceAlertThrift.timeModified = DateTimeUtils.getTimeYYYYMMDDHHMMSS(result.getTimestamp(PriceAlert.Columns.TIME_MODIFIED));
		priceAlertThrift.alertCreationDate = DateTimeUtils.currentDateYYYYMMDD(result.getTimestamp(PriceAlert.Columns.ALERT_START_TIME));
		priceAlertThrift.purchaseDate = priceAlertThrift.alertCreationDate;
		priceAlertThrift.alertExpirationDate = DateTimeUtils.currentDateYYYYMMDD(result.getTimestamp(PriceAlert.Columns.ALERT_END_TIME));
		String alertType = result.getString(PriceAlert.Columns.ALERT_TYPE);
		if(alertType.equals(PriceAlertType.ALERT_WHEN_AT_PRICE.toString()))
			priceAlertThrift.alertType = PriceAlertTypeThrift.ALERT_WHEN_AT_PRICE;
		else if(alertType.equals(PriceAlertType.ALERT_WHEN_PRICE_DROPS.toString()))
			priceAlertThrift.alertType = PriceAlertTypeThrift.ALERT_WHEN_PRICE_DROPS;
		priceAlertThrift.alertActive = result.getBoolean(PriceAlert.Columns.ACTIVE);
		priceAlertThrift.retailer = result.getString(PriceAlert.Columns.RETAILER_ID);
		priceAlertThrift.name = result.getString(PriceAlert.Columns.PRODUCT_NAME);
		priceAlertThrift.url = result.getString(PriceAlert.Columns.URL);
		priceAlertThrift.imageUrl = result.getString(PriceAlert.Columns.IMAGE_URL);
		priceAlertThrift.currPrice = result.getDouble(PriceAlert.Columns.PRICE);
		//priceAlertThrift.purchasePrice = result.getDouble(PriceAlert.Columns.PRICE);
		return priceAlertThrift;		
		
	}
	public PriceAlert getPriceAlert(ResultSet result) throws SQLException{
		PriceAlertBuilder b = new PriceAlertBuilder();
		b.alertId = result.getInt(PriceAlert.Columns.ALERT_ID);
		b.emailId = result.getString(PriceAlert.Columns.EMAIL_ID);
		b.productId = result.getInt(PriceAlert.Columns.PRODUCT_ID);
		b.alertPrice = result.getDouble(PriceAlert.Columns.ALERT_PRICE);
		b.timeModified = result.getTimestamp(PriceAlert.Columns.TIME_MODIFIED);
		b.alertStartTime = result.getTimestamp(PriceAlert.Columns.ALERT_START_TIME);
		b.alertEndTime = result.getTimestamp(PriceAlert.Columns.ALERT_END_TIME);
		String alertType = result.getString(PriceAlert.Columns.ALERT_TYPE);
		if(alertType.equals(PriceAlertType.ALERT_WHEN_AT_PRICE.toString()))
			b.alertType = PriceAlertType.ALERT_WHEN_AT_PRICE;
		else if(alertType.equals(PriceAlertType.ALERT_WHEN_PRICE_DROPS.toString()))
			b.alertType = PriceAlertType.ALERT_WHEN_PRICE_DROPS;
		b.active = result.getBoolean(PriceAlert.Columns.ACTIVE);
		PriceAlert alert = b.build();
		return alert;
	}
	
	/*
	 * Returns all the active alerts
	 */
	public List<PriceAlert> getAllActiveAlerts() throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		List<PriceAlert> activeAlerts = new ArrayList<PriceAlert>();
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_ALL_ACTIVE_PRICE_ALERTS);
			alertRS = alertStmt.executeQuery();
			
			PriceAlert alert;
			while(alertRS.next()){
				alert = getPriceAlert(alertRS);
				activeAlerts.add(alert);
			}			
			return activeAlerts;
		}
		catch(SQLException sqe)
		{
			logger.error(sqe.getMessage());
			throw new DAOException("Error when trying to get Active alerts");
		}
		finally {
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * Returns all the active products for which alerts has been setup
	 */
/*	public List<Integer> getAllActiveProductIds() throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null;
		ResultSet alertRS = null;
		List<Integer> activeProducts = new ArrayList<Integer>();
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertStmt = sqlConn.prepareStatement(Queries.GET_DISTINCT_ACTIVE_PRODUCT_IDS);
			alertRS = alertStmt.executeQuery();
			
			int prodId;
			while(alertRS.next()){
				prodId = alertRS.getInt(1);
				activeProducts.add(prodId);
			}			
			return activeProducts;
		} finally {
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
*/	
	/*
	 * Returns alerts corresponding to the product ids
	 */
/*	public List<PriceAlert> getPriceAlertsFromProductIds(int productId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertStmt = null, countStmt = null;
		ResultSet alertRS = null, countRS = null;
		List<PriceAlert> alerts = new ArrayList<PriceAlert>();
		int count = 0;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			countStmt = sqlConn.prepareStatement(Queries.GET_COUNT_ACTIVE_PRICE_ALERTS_FROM_PRODUCT_ID);
			countStmt.setInt(1, productId);
			countRS = countStmt.executeQuery();
			
			if(countRS.next())
				count = countRS.getInt(1);
			
			int fetchedAlerts = 0;
			if(count>0){
				alertStmt = sqlConn.prepareStatement(Queries.GET_ACTIVE_PRICE_ALERTS_FROM_PRODUCT_ID);
				alertStmt.setInt(1, productId);
				alertRS = alertStmt.executeQuery();
				
				while(alertRS.next()){
					++fetchedAlerts;
					PriceAlert alert = getPriceAlert(alertRS);
					alerts.add(alert);
				}
				logger.info("Fetched: "+fetchedAlerts+"/"+count+" alerts for productId: "+productId);
			}
			return alerts;
		}
		finally{
			closeRS(alertRS);
			closeRS(countRS);
			closeStmt(countStmt);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
*/	
	/*
	 * Returns alerts which have been updated after a given time
	 * @param modifiedTime
	 */
	public List<PriceAlert> getUpdatedPriceAlerts(Timestamp modifiedTime) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		ResultSet alertRS = null;
		PreparedStatement alertStmt = null;
		List<PriceAlert> alerts = new ArrayList<PriceAlert>();
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
	
			int fetchedAlerts = 0;
			
			alertStmt = sqlConn.prepareStatement(Queries.GET_UPDATED_PRICE_ALERTS);
			alertStmt.setString(1, modifiedTime.toString());
			alertRS = alertStmt.executeQuery();
			
			while(alertRS.next()){
				++fetchedAlerts;
				PriceAlert alert = getPriceAlert(alertRS);
				alerts.add(alert);
			}
			logger.info("Fetched "+fetchedAlerts + " updated alerts");
			
			return alerts;
		} finally {
			closeRS(alertRS);
			closeStmt(alertStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * method to clean up the tables before testing
	 */
/*	public void regTestRemoveAlerts(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement remUserStmt = null;
		PreparedStatement remPriceAlertStmt = null;
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			remUserStmt = sqlConn.prepareStatement(Queries.REMOVE_USER);
			remPriceAlertStmt = sqlConn.prepareStatement(Queries.REMOVE_PRICE_ALERTS);
			remUserStmt.setString(1, emailId);
			remPriceAlertStmt.setString(1, emailId);
			
			int rowsDeleted = remUserStmt.executeUpdate();
			logger.info("Removed "+rowsDeleted+" user rows");
			rowsDeleted = remPriceAlertStmt.executeUpdate();
			logger.info("Removed "+rowsDeleted+" price alert rows");
		}
		catch(SQLException e){
			logger.error(e.toString(), e);
			throw e;
		}
		finally{
			closeStmt(remUserStmt);
			closeStmt(remPriceAlertStmt);
			pool.releaseConnection(conn);
		}
	}*/
}

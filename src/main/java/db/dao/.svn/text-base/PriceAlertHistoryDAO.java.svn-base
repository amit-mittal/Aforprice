package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.Queries;
import entities.PriceAlertHistory;
import util.build.PriceAlertHistoryBuilder;

public class PriceAlertHistoryDAO extends DataAccessObject{

	private static final Logger logger = Logger.getLogger(PriceAlertHistoryDAO.class);
	
/*	public int getUniqueAlertHistId() throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertHistStmt = null;
		ResultSet alertHistRS = null;
		AtomicInteger alertHistId = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertHistStmt = sqlConn.prepareStatement(Queries.GET_MAX_ALERT_HISTORY_ID);
			alertHistRS = alertHistStmt.executeQuery();
			while(alertHistRS.next())
				alertHistId = new AtomicInteger(alertHistRS.getInt(1));
			
			if(alertHistId != null)
				return alertHistId.incrementAndGet();
			return 1;
		}
		finally{
			closeStmt(alertHistStmt);
			closeRS(alertHistRS);
			pool.releaseConnection(conn);
		}
	}
*/	
	public void addPriceAlertHistory(PriceAlertHistory alertHist) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertHistStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertHistStmt = sqlConn.prepareStatement(Queries.INSERT_PRICE_ALERT_HISTORY);
			int i=0;
			alertHistStmt.setInt(++i, alertHist.getAlertId());
			alertHistStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			alertHistStmt.setDouble(++i, alertHist.getNotificationPrice());
			alertHistStmt.executeUpdate();
			logger.info("addAlertHistory: "+alertHist);
		}
		finally{
			closeStmt(alertHistStmt);
			pool.releaseConnection(conn);
		}
	}
	
/*	public void updatePriceAlertHistory(PriceAlertHistory alertHist) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertHistStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertHistStmt = sqlConn.prepareStatement(Queries.UPDATE_PRICE_ALERT_HISTORY);
			int i=0;
			alertHistStmt.setInt(++i, alertHist.getAlertId());
			alertHistStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			alertHistStmt.setDouble(++i, alertHist.getNotificationPrice());
			alertHistStmt.setInt(++i, alertHist.getAlertHistoryId());
			alertHistStmt.executeUpdate();
			logger.info("updateAlertHistory: "+alertHist);
		}
		finally{
			closeStmt(alertHistStmt);
			pool.releaseConnection(conn);
		}
	}
*/	
	public List<PriceAlertHistory> getPriceAlertHistory(int alertId) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertHistStmt = null;
		ResultSet alertHistRS = null;
		List<PriceAlertHistory> alerts = new ArrayList<PriceAlertHistory>();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertHistStmt = sqlConn.prepareStatement(Queries.GET_PRICE_ALERT_HISTORY);
			alertHistStmt.setInt(1, alertId);
			
			alertHistRS = alertHistStmt.executeQuery();
			while(alertHistRS.next()){
				PriceAlertHistory alertHist = getPriceAlertHistory(alertHistRS);
				alerts.add(alertHist);
			}
			return alerts;
		}
		catch(SQLException sq)
		{
			logger.error(sq.getMessage());
			throw new DAOException("Issues when trying to read Price Alert History");
		}
		finally{
			closeRS(alertHistRS);
			closeStmt(alertHistStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public PriceAlertHistory getPriceAlertHistory(ResultSet result) throws SQLException{
		PriceAlertHistoryBuilder b = new PriceAlertHistoryBuilder();
		b.alertHistoryId = result.getInt(PriceAlertHistory.Columns.ALERT_HISTORY_ID);
		b.alertId = result.getInt(PriceAlertHistory.Columns.ALERT_ID);
		b.timeSent = result.getTimestamp(PriceAlertHistory.Columns.TIME_SENT);
		b.notificationPrice = result.getDouble(PriceAlertHistory.Columns.NOTIFICATION_PRICE);
		PriceAlertHistory alertHist = b.build();
		return alertHist;
	}
	
	/*
	 * Returns all the updates in the history table after a particular time
	 */
/*	public List<PriceAlertHistory> getUpdatedPriceAlertHistory(Timestamp modifiedTime) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement alertHistStmt = null;
		ResultSet alertHistRS = null;
		List<PriceAlertHistory> alertHistList = new ArrayList<PriceAlertHistory>();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			alertHistStmt = sqlConn.prepareStatement(Queries.GET_LATEST_FROM_PRICE_ALERT_HISTORY);
			alertHistStmt.setString(1, modifiedTime.toString());
			
			alertHistRS = alertHistStmt.executeQuery();
			while(alertHistRS.next()){
				PriceAlertHistory alertHist = getPriceAlertHistory(alertHistRS);
				alertHistList.add(alertHist);
			}
			return alertHistList;
		}
		finally{
			closeRS(alertHistRS);
			closeStmt(alertHistStmt);
			pool.releaseConnection(conn);
		}
	}*/
}

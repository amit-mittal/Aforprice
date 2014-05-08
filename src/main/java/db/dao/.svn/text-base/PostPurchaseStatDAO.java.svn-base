package db.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import adhoc.PostPurchasePricingAnalytics.PostPurchaseStat;
import db.DbConnection;
import db.DbConnectionPool;

public class PostPurchaseStatDAO
{

	private final Logger logger = Logger.getLogger(PostPurchaseStatDAO.class);
	private final static DbConnectionPool pool = DbConnectionPool.get();
	private final static PostPurchaseStatDAO instance = new PostPurchaseStatDAO();
	public static final String INSERT_POST_PURCHASE_STAT = "INSERT INTO PRODUCTS.POST_PURCHASE_STAT "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";	
	
	private PostPurchaseStatDAO(){	
	}
	
	public static PostPurchaseStatDAO getInstance(){
		return instance;		
	}
	
	public boolean store(PostPurchaseStat stat ) throws DAOException{		
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(INSERT_POST_PURCHASE_STAT);
			int i=1;
			//NAME, PERIOD, TOTAL_PROCESS_TIME, TOTAL_PROCESS_COUNT, 
			stmt.setString(i++,  stat.retailer );
			stmt.setDate(i++, new java.sql.Date( stat.statDate.getTime() ) );
			stmt.setLong(i++,  stat.categoryid );
			stmt.setLong(i++, stat.totalProducts );
			stmt.setLong(i++, stat.dropIn7Days);
			stmt.setLong(i++, stat.dropIn15Days);
			stmt.setLong(i++, stat.dropIn30Days);
			stmt.setLong(i++, stat.dropIn90Days);
			stmt.setDouble(i++, stat.dropAmt);
			stmt.setString(i++, stat.addenda1);
			stmt.setString(i++, stat.addenda2);
			stmt.setString(i++, stat.addenda3);
			
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}	
		finally{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
	}
	
	private void closeStmt(Statement stmt){
		try{
			if(stmt != null)
				stmt.close();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}

}

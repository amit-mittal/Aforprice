package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import util.Utils;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;

public class CategoryParseErrorRecorder {
	public enum ERRTYPE {
		NO_PRODS,
		CYCLE,
		UNPARSEABLE,
		PARSEERR,
		TIMEOUT,
		IS_SORETED_BS,
		UNKNOWN
	}
	
	private final static Logger logger = Logger.getLogger(CategoryParseErrorRecorder.class);
	private final static CategoryParseErrorRecorder instance = new CategoryParseErrorRecorder();
	private final static DbConnectionPool pool = DbConnectionPool.get();
	
	private CategoryParseErrorRecorder(){		
	}
	
	public static CategoryParseErrorRecorder getInstance(){
		return instance;
	}
	
	
	/**
	 * @param retailer
	 * @param categoryId
	 * @param categoryName
	 * @param type
	 * @param text
	 * @return
	 * @throws DAOException
	 */
	public void record(String retailer, int categoryId, String categoryName, ERRTYPE type, String text){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement insertStmt = null;		
		try {					
			insertStmt = sqlConn.prepareStatement(Queries.INSERT_CAT_PARSE_ERR);
			int i=1;
			insertStmt.setString(i++, retailer);
			insertStmt.setInt(i++, categoryId);
			insertStmt.setString(i++, categoryName);
			insertStmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
			insertStmt.setString(i++, type.toString());
			insertStmt.setString(i++, text);			
			insertStmt.execute();
		} catch (SQLException e) {						
			logger.error(retailer + "|" + categoryId + "|" + categoryName + "|" + type.toString() + "|" + text, e);
		}
		finally{
			pool.releaseConnection(conn);
			Utils.closeStmt(insertStmt);
		}

	}

}
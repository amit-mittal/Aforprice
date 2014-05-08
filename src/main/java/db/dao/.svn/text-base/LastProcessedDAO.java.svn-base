package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.Queries;

public class LastProcessedDAO extends DataAccessObject{
		
	private static Logger LOGGER = Logger.getLogger(LastProcessedDAO.class);
	
	public boolean setLast(String processName, Long value){
		return setLast(processName, String.valueOf(value));
	}
	
	public boolean setLast(String processName, String value){
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;	
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.UPDATE_LAST_PROCESSED);
			stmt.setString(1, value);
			stmt.setString(2, processName);
			return stmt.executeUpdate() == 1;			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);			
		}
		finally{
			pool.releaseConnection(conn);
			closeStmt(stmt);		
		}
		return false;
	}
	
	public String getLastProcessed(String processName){
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement getStmt = null;
		ResultSet result = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			getStmt = sqlConn.prepareStatement(Queries.GET_LAST_PROCESSED);
			getStmt.setString(1, processName);
			result = getStmt.executeQuery();
			if(result.next()){
				return result.getString(1);
			}
			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);			
		}
		finally{
			pool.releaseConnection(conn);
			closeStmt(getStmt);
			closeRS(result);			
		}
		return null;
	}
	
	public static void main(String[] args){
		String processName = null;
		Long lastValue = -1L;
		
		/*
		 * Format of Program Arguments
		 * process_name=? last_value=?
		 * This will change last_value of process_name
		 * to the value as specified
		 */
		if(args.length>=1){
			for(int i=0; i<args.length; i++){
				LOGGER.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2)
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				String key = keyVal[0];
				if(key.equalsIgnoreCase("process_name")){
					processName = keyVal[1];
				}
				else if(key.equalsIgnoreCase("last_value")){
					lastValue = Long.parseLong(keyVal[1]);
				}
				else
					LOGGER.warn("Ignoring arg "+keyVal[0]);;
			}
			if(processName==null)
				throw new IllegalArgumentException("missing argument process_name");
			if(lastValue==-1)
				throw new IllegalArgumentException("missing argument last_value");
		}
		
		LOGGER.info("Changing last_value of "+processName+" to "+lastValue);
		new LastProcessedDAO().setLast(processName, lastValue);
	}
}
package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.DbConnectionPool;

public class DataAccessObject {
	protected static final DbConnectionPool pool = DbConnectionPool.get();
	private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
	
	//For managing the inserts
	protected Lock insertLock = new ReentrantLock();
	protected DbConnection insertConn;
	protected Connection insertSqlConn;
	private PreparedStatement insertStmt = null;
		
	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	protected void startInsertBatch(String insertQry) throws SQLException{
		insertConn = pool.getConnection();
		insertSqlConn = insertConn.getConnection();
		startInsertBatch(insertSqlConn, insertQry);
	}

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	protected void startInsertBatch(Connection sqlConn, String insertQry) throws SQLException{
		try{
			insertLock.lock();
			insertStmt = sqlConn.prepareStatement(insertQry);
		}finally{
			insertLock.unlock();
		}
	}
	
	protected Lock getInsertLock(){
		return insertLock;
	}
	
	protected PreparedStatement getInsertStmt(){
		return insertStmt;
	}
	
	/**
	 * This needs to be called once all inserts have been processed
	 * @throws SQLException 
	 */
	public void endInsertBatch() throws SQLException{
		try{
			insertLock.lock();
			insertStmt.executeBatch();
			closeStmt(insertStmt);
			insertStmt = null;
			if(insertConn != null){
				pool.releaseConnection(insertConn);
				insertConn = null;
			}
			insertSqlConn = null;
		}finally{
			insertLock.unlock();
		}
	}

	protected static void closeStmt(Statement stmt){
		try{
			if(stmt != null)
				stmt.close();
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
	}

	protected static void closeRS(ResultSet result){
		try{
			if(result != null)
				result.close();
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Executes a query. 
	 * Note: This function causes memory leak as statement is not closed
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet execute(String query) throws SQLException{
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		Statement stmt = null;
		try{
			stmt = sqlConn.createStatement();
			return stmt.executeQuery(query);
		}finally{
			pool.releaseConnection(conn);
		}
	}
	
	public static int executeUpdate(String query) throws SQLException{
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		Statement stmt = null;
		try{
			stmt = sqlConn.createStatement();
			return stmt.executeUpdate(query);
		}finally{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}	
	}
}

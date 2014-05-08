package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.retailerConstants;
import util.RetailerTable;
import util.build.ProductAttributeHistoryBaseBuilder;
import db.DbConnection;
import entities.ProductAttributeHistoryBase;
import entities.ProductPricesHistory;
import global.exceptions.Bhagte2BandBajGaya;

public abstract class ProductAttributeHistoryBaseDAO extends DataAccessObject implements IProductAttributeHistoryDAO{

	private static final Logger LOGGER = Logger.getLogger(ProductAttributeHistoryBaseDAO.class);
	
	//insert statement for each retailer. value will be redundant in this map for retailers who are in same table
	private Map<String,PreparedStatement> retailerToInsertStmtMap = new HashMap<String, PreparedStatement>();
	//table to insert statement map
	private Map<String,PreparedStatement> historyTableToInsertStmtMap = new HashMap<String, PreparedStatement>();
	
	protected void startInsertBatch(Connection sqlConn, String insertQry) throws SQLException{
		try{
			insertLock.lock();
			for(String retailer : retailerConstants.RETAILERS.keySet()){
				String table = getHistoryTable(retailer);
				if(!historyTableToInsertStmtMap.containsKey(table)){
					PreparedStatement insertStmt = sqlConn.prepareStatement(String.format(insertQry, getHistoryTable(retailer)));
					historyTableToInsertStmtMap.put(table, insertStmt);
				}
				
				retailerToInsertStmtMap.put(retailer, historyTableToInsertStmtMap.get(table));
			}//for(String retailer  ends...
		}finally{
			insertLock.unlock();
		}		
	}
	
	private String getHistoryTable(String retailer){
		if(this instanceof ProductPricesHistoryDAO)
			return RetailerTable.getPricesHistoryTable(retailer);
		else if(this instanceof ProductReviewsHistoryDAO)
			return RetailerTable.getReviewsHistoryTable(retailer);
		else 
			throw new Bhagte2BandBajGaya("getHistoryTable is not implemented for "+this.getClass().getCanonicalName());
	}
	
	/*
	 * sanity check that retailer exists
	 */
	private static void checkRetailer(String retailer){
		if(!retailerConstants.RETAILERS.containsKey(retailer))
			throw new Bhagte2BandBajGaya("Invalid retailer "+retailer+", not found in retailerConstants.RETAILERS");
	}
	
	protected PreparedStatement getInsertStmt(String retailer){
		checkRetailer(retailer);
		return retailerToInsertStmtMap.get(retailer);
	}
	
	/**
	 * This needs to be called once all inserts have been processed
	 * @throws SQLException 
	 */
	public void endInsertBatch() throws SQLException{
		try{
			insertLock.lock();
			for(PreparedStatement insertStmt : historyTableToInsertStmtMap.values()){
				insertStmt.executeBatch();
				closeStmt(insertStmt);
			}
			historyTableToInsertStmtMap.clear();
			if(insertConn != null){
				pool.releaseConnection(insertConn);
				insertConn = null;
			}
			insertSqlConn = null;
		}finally{
			insertLock.unlock();
		}
	}

	/**
	 * Last product review before the time specified.
	 * @param time
	 * @return
	 */
	protected ProductAttributeHistoryBase getLatestBefore(int prodId, Date time, String lastBeforeQry){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = sqlConn.prepareStatement(lastBeforeQry);
			stmt.setInt(1, prodId);
			stmt.setTimestamp(2, new Timestamp(time.getTime()));
			rs = stmt.executeQuery();
			if(rs.next()){
				return get(rs);
			}
		}
		catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		finally{
			pool.releaseConnection(conn);
			closeRS(rs);
			closeStmt(stmt);
		}
		return null;
	}
	
	/**
	 * ProductAttributeHistoryBaseBuilder from the result set of one of the history tables 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected ProductAttributeHistoryBaseBuilder getBuilder(ResultSet rs) throws SQLException{
		ProductAttributeHistoryBaseBuilder b = new ProductAttributeHistoryBaseBuilder();
		b.productId = rs.getInt(ProductPricesHistory.Columns.PRODUCT_ID);
		b.time = rs.getTimestamp(ProductPricesHistory.Columns.TIME);
		b.timeModified = rs.getTimestamp(ProductPricesHistory.Columns.TIME_MODIFIED);
		return b;
	}

	public void finalize(){
		try{
			endInsertBatch();
		}
		catch(Throwable t){
		}
	}

}

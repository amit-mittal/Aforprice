package db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.ProductAttributeHistoryBase;
import entities.ProductSummary;

public interface IProductAttributeHistoryDAO {
	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	public void startInsertBatch() throws SQLException;

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	public void startInsertBatch(Connection sqlConn) throws SQLException;

	/**
	 * This needs to be called once all inserts have been processed
	 */
	public void endInsertBatch() throws SQLException;
	
	/**
	 * Inserts the attribute in attribute history table according to the following rule<p>
	 * 1. If no existing product exist, insert whatever current attribute is<p>
	 * 2. If existing product exists, then insert if attribute has changed
	 * @param prod Product
	 * @param existing Existing product
	 * @param flush Commit to database
	 * @throws SQLException
	 */
	public void insertBatch( 
			ProductSummary prod, 
			ProductSummary existing,
			boolean flush) throws SQLException;
		
	/**
	 * ProductReviewHistory from the result set of one of the history table 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public ProductAttributeHistoryBase get(ResultSet rs) throws SQLException;
	
	/**
	 * Cleanup
	 */
	public void finalize();

}

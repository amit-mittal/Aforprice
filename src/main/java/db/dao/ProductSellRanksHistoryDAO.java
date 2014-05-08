package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.Constants;
import util.DateTimeUtils;
import util.build.ProductAttributeHistoryBaseBuilder;
import util.build.ProductSellRanksHistoryBuilder;
import db.DbConnection;
import db.Queries;
import entities.ProductSellRanksHistory;
import entities.ProductSummary;

public class ProductSellRanksHistoryDAO extends ProductAttributeHistoryBaseDAO {
	
	private static final Logger logger = Logger.getLogger(ProductSellRanksHistoryDAO.class);
	
	public ProductSellRanksHistoryDAO() {
	}
		

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	public void startInsertBatch() throws SQLException{
		super.startInsertBatch(Queries.INSERT_PRODUCT_SELL_RANKS_HISTORY);
	}

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	public void startInsertBatch(Connection sqlConn) throws SQLException{
		super.startInsertBatch(sqlConn, Queries.INSERT_PRODUCT_SELL_RANKS_HISTORY);
	}
	
	/**
	 * Inserts the sell ranks according to the following rule<p>
	 * 1. If no existing product exist, insert whatever current rank is<p>
	 * 2. If existing product exists, then insert if sell rank changes
	 * @param prod Product
	 * @param existing Existing product
	 * @param flush Commit to database
	 * @throws SQLException
	 */
	@Override
	public void insertBatch( 
			ProductSummary prod, 
			ProductSummary existing,
			boolean flush) throws SQLException{
		if(prod == null)
			return;
		ProductSellRanksHistoryBuilder b = new ProductSellRanksHistoryBuilder();
		b.productId = prod.getId();
		b.rank = prod.getSalesRank();
		b.time = prod.getDownloadTime();
		b.timeModified = new Date();
		if(existing != null){
			//If prod is newer than existing or same time
			if(prod.getDownloadTime().after(existing.getDownloadTime()) || 
					Math.abs(DateTimeUtils.diff(prod.getDownloadTime(), existing.getDownloadTime(), TimeUnit.MINUTES)) <= Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN){				
				//If nothing changed, no need to save store
				if(prod.getSalesRank() == existing.getSalesRank()){
					return;
				}
				b.rank = prod.getSalesRank();
			}
		}
		//Either new product, or review rating or number of reviews have changed
		ProductSellRanksHistory p = b.build();
		int i = 0;
		try{
			getInsertLock().lock();
			PreparedStatement insertStmt = getInsertStmt(prod.getRetailerId()); 
			insertStmt.setInt(++i, p.getProductId());
			insertStmt.setTimestamp(++i, new Timestamp(p.getTime().getTime()));
			insertStmt.setDouble(++i, p.getRank());
			insertStmt.setTimestamp(++i, new Timestamp(p.getTimeModified().getTime()));
			insertStmt.addBatch();
			if(flush){
				insertStmt.executeBatch();
			}
		}finally{
			getInsertLock().unlock();
		}
	}
	
	/**
	 * get product history for relevant data like sell rank, price history or review history
	 * @param productId
	 * @param query
	 * @return
	 */
	public List<ProductSellRanksHistory> getHistory(int prodId){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = sqlConn.prepareStatement(Queries.PRODUCT_SELL_RANKS_HISTORY_QUERY);
			stmt.setInt(1, prodId);
			rs = stmt.executeQuery();
			List<ProductSellRanksHistory> history = new ArrayList<ProductSellRanksHistory>();
			while(rs.next()){
				ProductSellRanksHistory entry = get(rs);
				history.add(entry);
			}
			return history;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		finally{
			pool.releaseConnection(conn);
			closeRS(rs);
			closeStmt(stmt);
		}
		return null;
	}
	
	/**
	 * ProductReviewHistory from the result set of <tt>PRODUCT_SELL_RANKS_HISTORY</tt> table 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public ProductSellRanksHistory get(ResultSet rs) throws SQLException{
		ProductAttributeHistoryBaseBuilder bb = super.getBuilder(rs);
		ProductSellRanksHistoryBuilder b = new ProductSellRanksHistoryBuilder(bb);
		b.rank = rs.getInt(ProductSellRanksHistory.Columns.RANK);
		return b.build();
	}
}
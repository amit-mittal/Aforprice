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
import util.RetailerTable;
import util.build.ProductAttributeHistoryBaseBuilder;
import util.build.ProductReviewsHistoryBuilder;
import db.DbConnection;
import db.Queries;
import entities.ProductReviewsHistory;
import entities.ProductSummary;

public class ProductReviewsHistoryDAO extends ProductAttributeHistoryBaseDAO {

	private static final Logger logger = Logger.getLogger(ProductReviewsHistoryDAO.class);
	
	public ProductReviewsHistoryDAO() {
	}
		

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	public void startInsertBatch() throws SQLException{
		super.startInsertBatch(Queries.INSERT_PRODUCT_REVIEWS_HISTORY);		
	}

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	public void startInsertBatch(Connection sqlConn) throws SQLException{
		super.startInsertBatch(sqlConn, Queries.INSERT_PRODUCT_REVIEWS_HISTORY);	}
		
	/**
	 * Inserts the product reviews according to the following rule<p>
	 * 1. If no existing product exist, insert whatever current review rating/ number of reviews<p>
	 * 2. If existing product exists, then insert if any of review rating or number of reviews has changed
	 * @param prod Product
	 * @param existing Existing product
	 * @param flush Commit to database
	 * @throws SQLException
	 */
	public void insertBatch( 
			ProductSummary prod, 
			ProductSummary existing,
			boolean flush) throws SQLException{
		if(prod == null)
			return;
		ProductReviewsHistoryBuilder b = new ProductReviewsHistoryBuilder();
		b.productId = prod.getId();
		b.rating = prod.getReviewRating();
		b.numReviews = prod.getNumReviews();
		b.time = prod.getDownloadTime();
		b.timeModified = new Date();
		if(existing != null){
			//If prod is newer than existing or same time
			if(prod.getDownloadTime().after(existing.getDownloadTime()) || 
					Math.abs(DateTimeUtils.diff(prod.getDownloadTime(), existing.getDownloadTime(), TimeUnit.MINUTES)) <= Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN){
				//If nothing changed, no need to save store
				if(prod.getReviewRating() == existing.getReviewRating() && prod.getNumReviews() == existing.getNumReviews()){
					return;
				}
				b.rating = prod.getReviewRating();
				b.numReviews = prod.getNumReviews();
			}
		}
		//Either new product, or review rating or number of reviews have changed
		ProductReviewsHistory p = b.build();
		int i = 0;
		try{
			getInsertLock().lock();
			PreparedStatement stmt = getInsertStmt(prod.getRetailerId());
			stmt.setInt(++i, p.getProductId());
			stmt.setTimestamp(++i, new Timestamp(p.getTime().getTime()));
			stmt.setDouble(++i, p.getRating());
			stmt.setInt(++i, p.getNumReviews());
			stmt.setTimestamp(++i, new Timestamp(p.getTimeModified().getTime()));
			stmt.addBatch();
			if(flush){
				stmt.executeBatch();
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
	public List<ProductReviewsHistory> getHistory(int prodId, String retailer){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = sqlConn.prepareStatement(String.format(Queries.PRODUCT_REVIEWS_HISTORY_QUERY, RetailerTable.getReviewsHistoryTable(retailer)));
			stmt.setInt(1, prodId);
			rs = stmt.executeQuery();
			List<ProductReviewsHistory> history = new ArrayList<ProductReviewsHistory>();
			while(rs.next()){
				ProductReviewsHistory entry = get(rs);
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
	 * ProductReviewHistory from the result set of <tt>PRODUCT_REVIEW_HISTORY</tt> table 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public ProductReviewsHistory get(ResultSet rs) throws SQLException{
		ProductAttributeHistoryBaseBuilder bb = super.getBuilder(rs);
		ProductReviewsHistoryBuilder b = new ProductReviewsHistoryBuilder(bb);
		b.numReviews = rs.getInt(ProductReviewsHistory.Columns.NUM_REVIEWS);
		b.rating = rs.getDouble(ProductReviewsHistory.Columns.RATING);
		return b.build();
		
	}
	
}

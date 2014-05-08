package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import parsers.util.PriceTypes;
import util.ConfigParms;
import util.Constants;
import util.DateTimeUtils;
import util.Metric;
import util.RetailerTable;
import util.build.ProductAttributeHistoryBaseBuilder;
import util.build.ProductPricesHistoryBuilder;
import db.DbConnection;
import db.Queries;
import entities.ProductPricesHistory;
import entities.ProductSummary;

public class ProductPricesHistoryDAO extends ProductAttributeHistoryBaseDAO {

	private static final Logger logger = Logger.getLogger(ProductPricesHistoryDAO.class);

	
	public ProductPricesHistoryDAO() {
	}
		

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	@Override
	public void startInsertBatch() throws SQLException{
		super.startInsertBatch(Queries.INSERT_PRODUCT_PRICES_HISTORY);
	}

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	@Override
	public void startInsertBatch(Connection sqlConn) throws SQLException{
		super.startInsertBatch(sqlConn, Queries.INSERT_PRODUCT_PRICES_HISTORY);
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
		ProductPricesHistoryBuilder b = new ProductPricesHistoryBuilder().productId(prod.getId()).price(prod.getPrice()).time(prod.getDownloadTime());
		if(ConfigParms.isUnitTestMode() && prod.getCreationTime()!=null)
			b.timeModified = prod.getCreationTime();
		else
			b.timeModified = new Date();
		if(existing != null){
			//If prod is newer than existing or same time!=n
			if(prod.getDownloadTime().after(existing.getDownloadTime()) || 
					Math.abs(DateTimeUtils.diff(prod.getDownloadTime(), existing.getDownloadTime(), TimeUnit.MINUTES)) <= Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN){
				//If nothing changed, no need to save store
				if(Math.abs(prod.getPrice() - existing.getPrice()) <= Constants.MIN_PRICE_DELTA){
					return;
				}
				b.price = prod.getPrice();
			}
		}
		//Either new product, or review rating or number of reviews have changed
		ProductPricesHistory p = b.build();
		int i = 0;
		try{
			getInsertLock().lock();
			PreparedStatement stmt = getInsertStmt(prod.getRetailerId());
			stmt.setInt(++i, p.getProductId());
			stmt.setTimestamp(++i, new Timestamp(p.getTime().getTime()));
			stmt.setDouble(++i, p.getPrice());
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
	 * ProductReviewHistory from the result set of <tt>PRODUCT_SELL_RANKS_HISTORY</tt> table 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ProductPricesHistory get(ResultSet rs) throws SQLException{
		ProductAttributeHistoryBaseBuilder bb = super.getBuilder(rs);
		ProductPricesHistoryBuilder b = new ProductPricesHistoryBuilder(bb);		
		b.price = rs.getDouble(ProductPricesHistory.Columns.PRICE);
		return b.build();
	}
	
	public enum QUERYTYPE{
		PRICE_TIME,
		ROW_MODIFIED_TIME,
		PRODUCT_ID
	}
	
	/**
	 * get product history for relevant data like sell rank, price history or review history
	 * @param productId
	 * @param query
	 * @return
	 */
	public List<ProductPricesHistory> getHistory(int prodId, String retailer){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = sqlConn.prepareStatement(String.format(Queries.PRODUCT_PRICES_HISTORY_QUERY, RetailerTable.getPricesHistoryTable(retailer)));
			stmt.setInt(1, prodId);
			rs = stmt.executeQuery();
			List<ProductPricesHistory> history = new ArrayList<ProductPricesHistory>();
			while(rs.next()){
				ProductPricesHistory entry = get(rs);
				history.add(entry);
			}
			return history;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		finally{
			closeRS(rs);
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return null;
	}

	public Map<Integer, List<ProductPricesHistory>> getPriceHistoryForProductsWithPriceChange( Integer productId, Date startTime, Date endTime, QUERYTYPE queryType, String retailerId ) throws SQLException
	{
		logger.debug("BEGIN getPriceHistoryForProductsWithPriceChange, productId " + productId + 
				 " startTime " + startTime + " endTime " + endTime + " queryType " + queryType );
		
		Map<Integer, List<ProductPricesHistory>> priceHistoryByProduct = new HashMap<>();
		DbConnection conn = null;		
		Connection sqlConn = null;
		PreparedStatement priceHistoryStmt = null;
		ResultSet priceHistoryResults = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			if(queryType.equals(QUERYTYPE.PRICE_TIME) || queryType.equals(QUERYTYPE.ROW_MODIFIED_TIME))
			{	
				if(queryType.equals(QUERYTYPE.PRICE_TIME))
					priceHistoryStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_PRICES_HISTORY_FOR_SPAN2, RetailerTable.getPricesHistoryTable(retailerId), RetailerTable.getPricesHistoryTable(retailerId)));
				else
					throw new UnsupportedOperationException();//don't see any code reaching here
				priceHistoryStmt.setTimestamp( 1, new Timestamp(startTime.getTime()));
				priceHistoryStmt.setTimestamp( 2, new Timestamp(endTime.getTime()));
				priceHistoryStmt.setTimestamp( 3, new Timestamp(endTime.getTime()));
			}
			else
			{
				priceHistoryStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_PRICES_HISTORY, RetailerTable.getPricesHistoryTable(retailerId)));
				priceHistoryStmt.setInt( 1, productId );
			}
			
			priceHistoryResults = priceHistoryStmt.executeQuery();
			priceHistoryByProduct = getPriceHistoryFromResultSet( priceHistoryResults );
			return priceHistoryByProduct;
		}
		finally{
			closeRS(priceHistoryResults);
			closeStmt(priceHistoryStmt);
			pool.releaseConnection(conn);
			logger.debug("END getPriceHistoryForProductsWithPriceChange" );
		}
	
	}


	private Map<Integer, List<ProductPricesHistory>> getPriceHistoryFromResultSet(ResultSet priceHistoryResults) throws SQLException
	{
		Map<Integer, List<ProductPricesHistory>> priceHistoryByProduct = new HashMap<>();
		while( priceHistoryResults.next())
		{	
			ProductPricesHistory priceHistory = new ProductPricesHistoryBuilder()
				.price(priceHistoryResults.getDouble(ProductSummary.Columns.PRICE))
				.productId(priceHistoryResults.getInt(ProductSummary.Columns.PRODUCT_ID))
				.time(priceHistoryResults.getTimestamp(ProductSummary.Columns.TIME))
				.timeModified(priceHistoryResults.getTimestamp(ProductSummary.Columns.TIME_MODIFIED)).build(); 

			if( PriceTypes.isIgnorablePrice(priceHistory.getPrice())) 
				continue;
			
			if( priceHistoryByProduct.containsKey(priceHistory.getProductId()))
			{
				priceHistoryByProduct.get(priceHistory.getProductId()).add(priceHistory);	
			}
			else
			{
				List<ProductPricesHistory> priceHistoryList = new ArrayList<>();
				priceHistoryList.add(priceHistory);
				priceHistoryByProduct.put(priceHistory.getProductId(), priceHistoryList);
			}
		}
		return priceHistoryByProduct;
	}
	
	/*
	 * Returns the new prices which have been added after the parameter modified time
	 */
	public Map<Integer, List<ProductPricesHistory>> getLatestProductPricesHistory(Timestamp modifiedTime, String retailerId) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodHistStmt = null;
		ResultSet resultRS = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			
			prodHistStmt = sqlConn.prepareStatement(String.format(Queries.GET_LATEST_FROM_PRODUCT_PRICES_HISTORY, RetailerTable.getPricesHistoryTable(retailerId)));
			prodHistStmt.setString(1, modifiedTime.toString());
			resultRS = prodHistStmt.executeQuery();
			
			return getPriceHistoryFromResultSet(resultRS);
		}
		catch(SQLException sqe)
		{
			sqe.printStackTrace();
			logger.error(sqe.getMessage());
			throw new DAOException("Error while extracting product prices history");
		}
		finally{
			closeStmt(prodHistStmt);
			closeRS(resultRS);
			pool.releaseConnection(conn);
		}		
	}

/*	pending implementation of retailer based table here
 * public List<ProductPricesHistory> getProductPricesHistoryForSpan(Timestamp fromTime, Timestamp toTime) 
			throws DAOException
	{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodHistStmt = null;
		ResultSet resultRS = null;
		List<ProductPricesHistory> productsWithPriceChange = new ArrayList<>();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			
			prodHistStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_PRICES_HISTORY_FOR_SPAN);
			prodHistStmt.setString(1, fromTime.toString());
			prodHistStmt.setString(2, toTime.toString());
			resultRS = prodHistStmt.executeQuery();
			
			while( resultRS.next())
			{	
				ProductPricesHistoryBuilder pricesHistoryBuilder = new ProductPricesHistoryBuilder();
				pricesHistoryBuilder.price = resultRS.getDouble(ProductSummary.Columns.PRICE);
				if( PriceTypes.isIgnorablePrice(pricesHistoryBuilder.price)) continue;
				pricesHistoryBuilder.productId = resultRS.getInt(ProductSummary.Columns.PRODUCT_ID);
				pricesHistoryBuilder.time = resultRS.getTimestamp(ProductSummary.Columns.TIME); 
				pricesHistoryBuilder.timeModified = resultRS.getTimestamp(ProductSummary.Columns.TIME_MODIFIED); 
				ProductPricesHistory priceHistory = pricesHistoryBuilder.build();
				
				productsWithPriceChange.add(priceHistory);	
			}
		}
		catch(SQLException sqe)
		{
			logger.error(sqe.getMessage());
			throw new DAOException("Error while extracting product prices history");
		}
		finally{
			closeStmt(prodHistStmt);
			closeRS(resultRS);
			pool.releaseConnection(conn);
		}		
		return productsWithPriceChange;
	}		
	*/
		
}
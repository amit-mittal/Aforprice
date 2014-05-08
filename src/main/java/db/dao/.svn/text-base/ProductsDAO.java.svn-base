package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import parsers.util.ProductUID;
import products.migration.MigrationFilters;
import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import thrift.servers.ProductData;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.Constants;
import util.DateTimeUtils;
import util.Emailer;
import util.Metric;
import util.ProductUtils;
import util.RetailerTable;
import util.build.ProductSummaryBuilder;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import db.DbConnection;
import db.Queries;
import db.QueryBuilder;
import db.types.ISQLType;
import db.types.SQLBoolean;
import db.types.SQLDouble;
import db.types.SQLInteger;
import db.types.SQLString;
import db.types.SQLTimestamp;
import db.util.UniqIdGenerator;
import entities.ProductCategory;
import entities.ProductReviewsHistory;
import entities.ProductSummary;
import global.exceptions.Bhagte2BandBajGaya;
/*
 * Test Files:
 * ProductsDAOTest: insertUpdateProductSummary
 * ProductsDAOTest2: getPriceHistory, ReveiewRating and SellRankHistory
 */
public class ProductsDAO extends DataAccessObject{	

	private final static Logger logger = Logger.getLogger(ProductsDAO.class);
	//used in insertUpdateProductSummary
	Metric metProductIns = new Metric("ProductInsert");
	Metric metProductInsUidQry = new Metric("metProductInsUidQry");
	Metric metProductUpd = new Metric("ProductUpdate");
	Metric metProductCaching = new Metric("ProductCaching");
	Metric metHistIns = new Metric("HistIns");
	Metric metProductCatUpd = new Metric("ProductCatUpd");
	Metric metMarkProcessed = new Metric("MarkProcessed");
	
	//used in getProudctsThrift2
	Metric metPriceQuery = new Metric("PriceQuery");
	Metric metPriceCaching = new Metric("PriceCaching");
	int realUpdates=0, dummyUpdates=0;
	ArrayList<ProductSummary> emptyProductArray = new ArrayList<>();
	Map<Integer, List<Integer>> emptyProductCategories = new HashMap<Integer, List<Integer>>();
	public static enum HISTORY_QUERY_TYPE{
		PRICE,
		REVIEW
	};
	
	/**
	 * Get the downloaded products on a particular date
	 * 
	 * @param date
	 *            Date for which the products need to be fetched
	 * @param retailerId
	 * @param categoryId -1 for all products
	 * @return Map of product name and product
	 * @throws SQLException
	 */
	public Map<String, Set<ProductSummary>> getDownloadedProductsOnDate(Date date, String retailerId, int categoryId, ConfigParms.DOWNLOAD_MODE mode) throws SQLException {
		Calendar midNight = DateTimeUtils.getMidNight(date);
		Calendar nextMidNight = DateTimeUtils.getNextMidNight(date);
		return getDownloadedProductsTimeRange(midNight.getTime(), nextMidNight.getTime(), retailerId, categoryId, mode);
	}
	
	
	/**
	 * Get the downloaded products for previous week excluding input 'date'
	 * 
	 * @param date
	 * @param retailerId 
	 * @param categoryId
	 * @return Map of product name and product
	 * @throws SQLException
	 */
	public Map<String, Set<ProductSummary>> getDownloadedProductsPreviousNDays(Date date, String retailerId, int categoryId, int n, ConfigParms.DOWNLOAD_MODE mode) throws SQLException {
		Calendar midNight = DateTimeUtils.getMidNight(date);
		Calendar prevWeekMidNight = DateTimeUtils.getPrevNDaysMidNight(date, n);
		return getDownloadedProductsTimeRange(prevWeekMidNight.getTime(), midNight.getTime(), retailerId, categoryId, mode);
	}


	/**
	 * Get the downloaded products for previous week excluding input 'date'
	 * 
	 * @param date
	 * @param retailerId
	 * @return Map of product name and product
	 * @throws SQLException
	 */
	public Map<String, ProductSummary> get100DownloadedProductsPreviousWeek(Date date, String retailerId) throws SQLException {
		Calendar midNight = DateTimeUtils.getMidNight(date);
		Calendar prevWeekMidNight = DateTimeUtils.getPrevWeekMidNight(date);
		return get100DownloadedProductsTimeRange(prevWeekMidNight.getTime(), midNight.getTime(), retailerId);
	}
	
	

	/**
	 * Get the a map of downloaded products for the given range date, the key being the product uid, and
	 * value being list of products corresponding to the same uid.
	 * @param startTime
	 * @param endTime        
	 * @param retailerId
	 * @param categoryId
	 * @return Map of product name and product
	 * @throws SQLException
	 */
	public Map<String, Set<ProductSummary>> getDownloadedProductsTimeRange(Date startTime, Date endTime, String retailerId, int categoryId, ConfigParms.DOWNLOAD_MODE mode) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodDownStmt = null;
		PreparedStatement prodDownCountStmt = null;
		ResultSet countRS = null;
		ResultSet results = null;
		int count = 0;
		try {
			logger.debug("getDownloadedProductsTimeRange : " + startTime + "->" + endTime);
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			if(categoryId == -1){
				prodDownStmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS_FOR_RETAILER, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				prodDownCountStmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS_FOR_RETAILER_COUNT);
			}
			else{
				prodDownStmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS);
				prodDownCountStmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS_COUNT);
			}
			int i = 0;
			prodDownStmt.setString(++i, retailerId);
			prodDownCountStmt.setString(i, retailerId);
			if(categoryId != -1){
				prodDownStmt.setInt(++i, categoryId);
				prodDownCountStmt.setInt(i, categoryId);
			}
			prodDownStmt.setTimestamp(++i, new Timestamp(startTime.getTime()));
			prodDownCountStmt.setTimestamp(i, new Timestamp(startTime.getTime()));
			prodDownStmt.setTimestamp(++i, new Timestamp(endTime.getTime()));
			prodDownCountStmt.setTimestamp(i, new Timestamp(endTime.getTime()));
			prodDownStmt.setString(++i, mode.getDbValue());
			prodDownCountStmt.setString(i, mode.getDbValue());
			logger.debug("Running Query: "+prodDownCountStmt);
			countRS = prodDownCountStmt.executeQuery();
			if(countRS.next()){
				count = countRS.getInt(1);
			}
			if(count > 2e6){//if more than 2m product
				prodDownStmt.setFetchSize(Integer.MIN_VALUE);
			}
			results = prodDownStmt.executeQuery();
			Map<String, Set<ProductSummary>> products = new HashMap<String, Set<ProductSummary>>(count);
			while (results.next()) {
				ProductSummary prod = getDownloadedProduct(results);
				String key = prod.getNameLower();
				Set<ProductSummary> set = products.get(key);
				if(set == null){
					set = new HashSet<ProductSummary>();
					products.put(key, set);
				}
				set.add(prod);
			}
			return products;
		} finally {
			closeRS(results);
			closeRS(countRS);
			closeStmt(prodDownStmt);
			closeStmt(prodDownCountStmt);			
			pool.releaseConnection(conn);			
		}
	}

	public HashMap<ProductSummary, ProductSummary> getDownloadedProductsTimeRange(Date startTime, Date endTime, boolean allProdInfo) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodDownStmt = null;
		PreparedStatement prodDownCountStmt = null;
		ResultSet countRS = null;
		ResultSet results = null;
		int count = 0;
		try {
			logger.info("getDownloadedProductsTimeRange : " + startTime + "->" + endTime);
			conn = pool.getConnection();
			sqlConn = conn.getConnection();

			if(allProdInfo)
				prodDownStmt = sqlConn.prepareStatement(Queries.GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER);
			else
				prodDownStmt = sqlConn.prepareStatement(Queries.GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER_AVLBL_INFO);
			prodDownCountStmt = sqlConn.prepareStatement(Queries.GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER_COUNT);
			
			prodDownStmt.setTimestamp(3, new Timestamp(startTime.getTime()));
			prodDownStmt.setTimestamp(4, new Timestamp(endTime.getTime()));
			prodDownCountStmt.setTimestamp(1, new Timestamp(startTime.getTime()));
			prodDownCountStmt.setTimestamp(2, new Timestamp(endTime.getTime()));		
		
			countRS = prodDownCountStmt.executeQuery();
			if(countRS.next()){
				count = countRS.getInt(1);
			}
			long start = getFirstId(startTime, endTime, sqlConn);
			long end = start - 1;
			long last =  getLastId(startTime, endTime, sqlConn);
			int fetchedProds = 0;
			HashMap<ProductSummary, ProductSummary> products = new HashMap<ProductSummary, ProductSummary>(count);
			while (true) {
				start = end + 1;
				end = Math.min(last, start + Constants.MAX_FETCH_SIZE_PROD_SUMMARY);
				
				prodDownStmt.setLong(1, start);
				prodDownStmt.setLong(2, end);
				results = prodDownStmt.executeQuery();
				while(results.next()){
					ProductSummary prod = getDownloadedProduct(results);
					ProductSummary existing = products.get(prod);
					if(existing != null){
						if(!ProductUtils.isProductValid(existing, prod)){
							products.put(prod, prod);
						}
					}
					else
						products.put(prod, prod);
					++fetchedProds;
				}
				logger.info("Fetched " + fetchedProds + "/" + count + " downloaded products between " + startTime + " and " + endTime);
				closeRS(results);
				if(end >= last)
					break;
			}
			logger.info("Returning " + products.size() + " number of unique products");
			return products;
		} finally {
			closeRS(countRS);
			closeStmt(prodDownStmt);
			closeStmt(prodDownCountStmt);			
			pool.releaseConnection(conn);			
		}
	}
	
	private long getFirstId(Date start, Date end, Connection conn) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet r = null;
		try{
			stmt = conn.prepareStatement(Queries.GET_MIN_ID_TIMERANGE);
			stmt.setTimestamp(1, new Timestamp(start.getTime()));
			stmt.setTimestamp(2, new Timestamp(end.getTime()));
			r = stmt.executeQuery();
			if(r.next()){
				return r.getLong(1);
			}
			return 0L;
		}finally{
			closeRS(r);
			closeStmt(stmt);
		}
	}
	
	private long getLastId(Date start, Date end, Connection conn) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet r = null;
		try{
			stmt = conn.prepareStatement(Queries.GET_MAX_ID_TIMERANGE);
			stmt.setTimestamp(1, new Timestamp(start.getTime()));
			stmt.setTimestamp(2, new Timestamp(end.getTime()));
			r = stmt.executeQuery();
			if(r.next()){
				return r.getLong(1);
			}
			return Long.MAX_VALUE;
		}finally{
			closeRS(r);
			closeStmt(stmt);
		}
	}
	
	public HashMap<ProductSummary, ProductSummary> getProducts() throws SQLException {
		return getProducts(null, false);
	}

	/**
	 * @param retailers the set of retailers for which the products need to be included or excluded based on include flag. if null or empty
	 *                  all the products are included
	 * @param include true to include only those retailers which are in the retailers set and false to exclude those. 
	 * @return
	 * @throws SQLException
	 */
	public HashMap<ProductSummary, ProductSummary> getProducts(Set<String> retailers, boolean include) throws SQLException {
		logger.info("getProducts called");
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, prodMaxIdStmt = null;
		ResultSet resultsRS = null, prodMaxIdRS = null;
		int start = 0, end = 0, maxId = 0;		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodMaxIdStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_SUMMARY_MAX_ID);
			prodMaxIdRS = prodMaxIdStmt.executeQuery();
			if(prodMaxIdRS.next())
				maxId = prodMaxIdRS.getInt(1);
			HashMap<ProductSummary, ProductSummary>products = new HashMap<>(maxId);
			int fetchedProds = 0;
			while(true){
				start = end;
				end = start + Constants.MAX_FETCH_SIZE_PROD_SUMMARY;
				prodStmt = sqlConn.prepareStatement(Queries.GET_ALL_ACTIVE_PRODS_AVLBL_INFO);
				prodStmt.setInt(1, start);
				prodStmt.setInt(2, end);
				resultsRS = prodStmt.executeQuery();
				while (resultsRS.next()) {
					ProductSummary product = getRequiredProductSummary(resultsRS);
					if(retailers != null && retailers.size() > 0){
						if((!include && retailers.contains(product.getRetailerId())) || (include && !retailers.contains(product.getRetailerId()))){
							continue;
						} 
					}
					fetchedProds++;
					products.put(product, product);
				}
				if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST)
					logger.info("Fetched " + fetchedProds + " active products");
				closeRS(resultsRS);
				closeStmt(prodStmt);
				if(end >= maxId)
					break;
			}
			logger.info("Returning " + fetchedProds + " number of active products");
			return products;
		} finally {		
			closeRS(prodMaxIdRS);
			closeStmt(prodMaxIdStmt);
			pool.releaseConnection(conn);
		}
	}

	/*
	 * returns all the active products for a retailer
	 */
	public List<Product> getProductsThriftForRetailer(String retailerId, Integer numProducts) throws SQLException {
		logger.info("getActiveProductsForRetailer(" + retailerId + ")");
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, prodMaxCountStmt = null;
		ResultSet resultsRS = null, countRS = null;
		int maxProdId = 0;
		boolean check = numProducts==-1 ? false : true;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_PRODUCTS_FOR_RETAILER);
			prodStmt.setString(1, retailerId);
			prodMaxCountStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_SUMMARY_MAX_ID, RetailerTable.getPricesHistoryTable(retailerId)));
			countRS = prodMaxCountStmt.executeQuery();
			if(countRS.next())
				maxProdId = countRS.getInt(1) + 1;
			int start = 0;
			int end = 0;
			List<Product> products = new ArrayList<Product>();
			int fetchedProds = 0;
			while(end<maxProdId){
				start = end;
				end = Math.min(maxProdId, start+Constants.MAX_FETCH_SIZE_PROD_SUMMARY);
				prodStmt.setInt(2, start);
				prodStmt.setInt(3, end);
				resultsRS = prodStmt.executeQuery();
				while (resultsRS.next()) {
					++fetchedProds;
					List<Tick> priceTicks = new ArrayList<Tick>();
					PriceHistory priceHistory = new PriceHistory();
					priceHistory.setPriceTicks(priceTicks);
					Product product = new Product(
							resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID),
							resultsRS.getString(ProductSummary.Columns.PRODUCT_NAME), 
							resultsRS.getString(ProductSummary.Columns.MODEL_NO),
							resultsRS.getString(ProductSummary.Columns.IMAGE_URL), 
							resultsRS.getString(ProductSummary.Columns.URL),
							priceHistory,
							new ArrayList<Tick>(),
							new ArrayList<Review>());
					products.add(product);
					if(check){
						if(products.size()>=numProducts)
							break;
					}
				}
				if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST)
					logger.info("Fetched " + fetchedProds +  " active products");
				closeRS(resultsRS);
				if(check){
					if(products.size()>=numProducts)
						break;
				}
			}
			logger.info("Returning " + fetchedProds + " number of active products");
			return products;
		} finally {
			System.gc();
			closeRS(countRS);
			closeStmt(prodStmt);
			closeStmt(prodMaxCountStmt);
			pool.releaseConnection(conn);
		}
	}
	
	
	/*
	 * returns all the products for a retailer 
	 */
	public List<Product> getAsOfProducts(String retailerId, Integer numProducts, Date asOfDate) throws SQLException {
		logger.info("getActiveProductsForRetailer(" + retailerId + ")");
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, prodMaxCountStmt = null;
		ResultSet resultsRS = null, countRS = null;
		int maxProdId = 0;
		boolean check = numProducts==-1 ? false : true;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_ALL_PRODUCTS_FOR_RETAILER);
			prodStmt.setString(1, retailerId);
			prodStmt.setDate(2, new java.sql.Date(asOfDate.getTime()));

			prodMaxCountStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_SUMMARY_MAX_ID, RetailerTable.getPricesHistoryTable(retailerId)));
			countRS = prodMaxCountStmt.executeQuery();
			if(countRS.next())
				maxProdId = countRS.getInt(1) + 1;
			int start = 0;
			int end = 0;
			List<Product> products = new ArrayList<Product>();
			int fetchedProds = 0;
			while(end<maxProdId){
				start = end;
				end = Math.min(maxProdId, start+Constants.MAX_FETCH_SIZE_PROD_SUMMARY);
				prodStmt.setInt(3, start);
				prodStmt.setInt(4, end);
				resultsRS = prodStmt.executeQuery();
				while (resultsRS.next()) {
					++fetchedProds;
					List<Tick> priceTicks = new ArrayList<Tick>();
					PriceHistory priceHistory = new PriceHistory();
					priceHistory.setPriceTicks(priceTicks);
					Product product = new Product(
							resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID),
							resultsRS.getString(ProductSummary.Columns.PRODUCT_NAME), 
							resultsRS.getString(ProductSummary.Columns.MODEL_NO),
							resultsRS.getString(ProductSummary.Columns.IMAGE_URL), 
							resultsRS.getString(ProductSummary.Columns.URL),
							priceHistory,
							new ArrayList<Tick>(),
							new ArrayList<Review>());
					products.add(product);
					if(check){
						if(products.size()>=numProducts)
							break;
					}
				}
				if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST)
					logger.info("Fetched " + fetchedProds +  "  products");
				closeRS(resultsRS);
				if(check){
					if(products.size()>=numProducts)
						break;
				}
			}
			logger.info("Returning " + fetchedProds + " number of  products");
			return products;
		} finally {
			System.gc();
			closeRS(countRS);
			closeStmt(prodStmt);
			closeStmt(prodMaxCountStmt);
			pool.releaseConnection(conn);
		}
	}
	
	
	/*
	 * get products for given category, no price history
	 */
	//TODO test is pending
	public List<Integer> getProductIdsForCategory(int categoryId) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null;
		ResultSet resultsRS = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			//metProductQuery.start();
			prodStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_IDS_FOR_CATEGORY);
			prodStmt.setInt(1, categoryId);
			resultsRS = prodStmt.executeQuery();
			List<Integer> productIds = new ArrayList<>();
			while (resultsRS.next()) {
				int prodId = resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID);
				productIds.add(prodId);
			}
			
			//logger.info(categoryId + ": " + productIds.size()+ " products");
			//metProductQuery.end();
			//logger.info(metProductQuery.currentStats());
			return productIds;
		} finally {		
			closeRS(resultsRS);
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
	}
	
	private int getPriceTickFromResultSet(ResultSet priceHistoryResults) throws SQLException{
		int fetchedPrices = 0;
		while(priceHistoryResults.next()){
			Integer productId = priceHistoryResults.getInt(ProductSummary.Columns.PRODUCT_ID);
			Product product = ProductData.getInstance().getProduct(productId);
			if(product==null)//means this product doesn't belong to any retailer in this cache
				continue;
			List<Tick> priceTicks = product.getPriceHistory().getPriceTicks();
			Timestamp priceTime = priceHistoryResults.getTimestamp(ProductSummary.Columns.TIME); 
			Tick tick = new Tick(priceTime.getTime(), priceHistoryResults.getDouble(ProductSummary.Columns.PRICE));
			priceTicks.add(tick);
			fetchedPrices++;
			if(fetchedPrices%100e3==0)
				logger.info("Fetched "+fetchedPrices+" entries");
		}
		return fetchedPrices;
	}
	
	private int getReviewFromResultSet(ResultSet reviewHistoryResults) throws SQLException{
		int fetchedReviews = 0;
		while(reviewHistoryResults.next()){
			Integer productId = reviewHistoryResults.getInt(ProductReviewsHistory.Columns.PRODUCT_ID);
			Product product = ProductData.getInstance().getProduct(productId);
			if(product==null)//means this product doesn't belong to any retailer in this cache
				continue;
			List<Review> reviewHistory = product.getReviewHistory();
			Timestamp priceTime = reviewHistoryResults.getTimestamp(ProductReviewsHistory.Columns.TIME);
			double reviewRating = reviewHistoryResults.getDouble(ProductReviewsHistory.Columns.RATING);
			int numReviews = reviewHistoryResults.getInt(ProductReviewsHistory.Columns.NUM_REVIEWS);
			Review review = new Review(priceTime.getTime(), reviewRating, numReviews);
			reviewHistory.add(review);
			fetchedReviews++;
		}
		return fetchedReviews;
	}
	
	Set<String> gotDataFromTables = new HashSet<String>();
	public void resetGotDataFromTablesUnitTest(){
		gotDataFromTables.clear();
	}
	
	/**
	 * Get price history/sell rank history/review history for given products by retailerId
	 * @param type
	 * 		0: price history
	 *  	1: review history
	 *  	2: sell rank history
	 */
	public void getDataForProductsByRetailer(String retailerId, HISTORY_QUERY_TYPE type) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null;
		ResultSet results = null;
		Metric metQuery = new Metric("metHistoryQuery");
		try {
			String table;
			if(type == HISTORY_QUERY_TYPE.PRICE)
				table = RetailerTable.getPricesHistoryTable(retailerId);
			else
				table = RetailerTable.getReviewsHistoryTable(retailerId);
			if(gotDataFromTables.contains(table)){
				logger.info("Already got data from table "+table);
				return;
			}
			gotDataFromTables.add(table);
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			metQuery.start();
			if(type == HISTORY_QUERY_TYPE.PRICE)
				prodStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_PRICES_HISTORY_BY_RETAILER, RetailerTable.getPricesHistoryTable(retailerId)),ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			else
				prodStmt = sqlConn.prepareStatement(String.format(Queries.GET_PRODUCT_REVIEWS_HISTORY_BY_RETAILER, RetailerTable.getReviewsHistoryTable(retailerId)), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			prodStmt.setFetchSize(Integer.MIN_VALUE);
			String query = prodStmt.toString();			
			logger.info("Executing query: "+query.substring(query.indexOf("SELECT")));
			results = prodStmt.executeQuery();
			if(type == HISTORY_QUERY_TYPE.PRICE)
				getPriceTickFromResultSet(results);
			else
				getReviewFromResultSet(results);
			metQuery.end();
			logger.info(metQuery.currentStats());
		} finally {		
			closeRS(results);
			closeStmt(prodStmt);
			System.gc();
			pool.releaseConnection(conn);
		}
	}
	
	/**
	 * Get the downloaded products for the given range date for given retailer
	 * 
	 * @param startTime
	 * @param endTime        
	 * @param retailerId
	 * @return Map of product name and product
	 * @throws SQLException
	 */
	private Map<String, ProductSummary> get100DownloadedProductsTimeRange(Date startTime, Date endTime, String retailerId) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodDownStmt = null;
		try {
			logger.debug("getDownloadedProductsTimeRange : " + startTime + "->" + endTime);
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodDownStmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS_FOR_RETAILER_100);
			int i = 0;
			prodDownStmt.setString(++i, retailerId);
			prodDownStmt.setTimestamp(++i, new Timestamp(startTime.getTime()));
			prodDownStmt.setTimestamp(++i, new Timestamp(endTime.getTime()));
			Map<String, ProductSummary> products = new HashMap<String, ProductSummary>();
			ResultSet results = prodDownStmt.executeQuery();
			while (results.next()) {
				ProductSummary prod = new ProductSummary(
						results.getString(ProductSummary.Columns.RETAILER_ID),
						results.getInt(ProductSummary.Columns.CATEGORY_ID), "",
						results.getString(ProductSummary.Columns.PRODUCT_NAME),
						results.getDouble(ProductSummary.Columns.PRICE),
						results.getString(ProductSummary.Columns.URL),
						results.getString(ProductSummary.Columns.IMAGE_URL),
						results.getString(ProductSummary.Columns.DESCRIPTION),
						results.getString(ProductSummary.Columns.MODEL_NO));
				prod.setId(results.getInt(ProductSummary.Columns.ID));
				products.put(results.getString(ProductSummary.Columns.PRODUCT_NAME),
						prod);
			}
			return products;
		} finally {
			closeStmt(prodDownStmt);
			pool.releaseConnection(conn);			
		}
	}
	/**
	 * @param startId
	 * @param endId
	 * @param retailers the set of retailers for which the downloaded products need to be included or excluded based on include flag. if null or empty
	 *                  all the products are included
	 * @param include true to include only those retailers which are in the retailers set and false to exclude those. 
	 * @return downloaded products
	 * @throws SQLException
	 */
	public List<ProductSummary> getDownloadedProductsToProcess(long startId, long endId, Set<String> retailers, boolean include) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		List<ProductSummary> products = new ArrayList<ProductSummary>((int) (endId - startId + 1));
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.GET_DOWNLOADED_PRODS_TO_PROCESS);
			stmt.setLong(1, startId);
			stmt.setLong(2, endId);
			results = stmt.executeQuery();
			while (results.next()) {
				ProductSummary prod = getDownloadedProduct(results);
				if(retailers != null && retailers.size() > 0){
					if((!include && retailers.contains(prod.getRetailerId())) || (include && !retailers.contains(prod.getRetailerId()))){
						continue;
					} 
				}
				products.add(prod);
			}
			return products;
		} finally {
			closeRS(results);
			closeStmt(stmt);			
			pool.releaseConnection(conn);
		}
	}
	
	public ProductSummary getDownloadedProduct(ResultSet results) throws SQLException{
		//The following three fields could be null as these were not being captured from day of data collection
		Double reviewRating = results.getDouble(ProductSummary.Columns.REVIEW_RATING); 
		Integer numReviews = results.getInt(ProductSummary.Columns.NUM_REVIEWS);
		Integer salesRank = results.getInt(ProductSummary.Columns.BEST_SELLER_RANK);
		
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.downloadId = results.getLong(ProductSummary.Columns.ID);
		b.retailerId = results.getString(ProductSummary.Columns.RETAILER_ID);
		b.categoryId = results.getInt(ProductSummary.Columns.CATEGORY_ID);
		b.name = results.getString(ProductSummary.Columns.PRODUCT_NAME);
		b.price = results.getDouble(ProductSummary.Columns.PRICE);
		b.url = results.getString(ProductSummary.Columns.URL);
		b.imageUrl = results.getString(ProductSummary.Columns.IMAGE_URL);
		b.model = results.getString(ProductSummary.Columns.MODEL_NO);
		b.reviewRating = reviewRating == null?-1: reviewRating;
		b.numReviews = numReviews == null?-1: numReviews;
		b.salesRank = salesRank == null?-1: salesRank;
		b.downloadTime = results.getTimestamp(ProductSummary.Columns.DOWNLOAD_TIME);
		ProductSummary prod = b.build();
		return prod;

	}

	//smarter product construction which skips some attributes for non-active product to keep memory of product migration optimized
	public ProductSummary getRequiredProductSummary(ResultSet productRow) throws SQLException{
		if(productRow.getBoolean(ProductSummary.Columns.ACTIVE))
			return getProductSummary(productRow);
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.prodId = productRow.getInt(ProductSummary.Columns.PRODUCT_ID);
		b.retailerId = productRow.getString(ProductSummary.Columns.RETAILER_ID);
		b.url = productRow.getString(ProductSummary.Columns.URL);
		b.active = false;
		return b.build();
	}
	
	//convert sql result into product object
	public ProductSummary getProductSummary(ResultSet productRow) throws SQLException{
		//The following three fields could be null as these were not being captured from day of data collection
		Double reviewRating = productRow.getDouble(ProductSummary.Columns.REVIEW_RATING); 
		Integer numReviews = productRow.getInt(ProductSummary.Columns.NUM_REVIEWS);
		Integer salesRank = productRow.getInt(ProductSummary.Columns.BEST_SELLER_RANK);
		
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.prodId = productRow.getInt(ProductSummary.Columns.PRODUCT_ID);
		b.retailerId = productRow.getString(ProductSummary.Columns.RETAILER_ID);
		b.name = productRow.getString(ProductSummary.Columns.PRODUCT_NAME);
		b.price = productRow.getDouble(ProductSummary.Columns.PRICE);
		b.url = productRow.getString(ProductSummary.Columns.URL);
		b.imageUrl = productRow.getString(ProductSummary.Columns.IMAGE_URL);
		//b.desc = results.getString(ProductSummary.Columns.DESCRIPTION);
		b.model = productRow.getString(ProductSummary.Columns.MODEL_NO);
		b.reviewRating = reviewRating == null?-1: reviewRating;
		b.numReviews = numReviews == null?-1: numReviews;
		b.salesRank = salesRank == null?-1: salesRank;
		b.downloadTime = productRow.getTimestamp(ProductSummary.Columns.LAST_DOWNLOAD_TIME);
		b.creationTime = productRow.getTimestamp(ProductSummary.Columns.START_DATE);
		b.active = productRow.getBoolean(ProductSummary.Columns.ACTIVE);
		return b.build();
	}

	public long getMaxDownloadId() throws SQLException{
		return downloadId(Queries.MAX_DOWNLOAD_ID);
	}
	
	public long getMinDownloadId() throws SQLException{
		return downloadId(Queries.MIN_DOWNLOAD_ID);
	}
	
	private long downloadId(String query) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				return rs.getLong(1);
		}
		finally{
			closeRS(rs);
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return -1;
	}
	
	
	public void setProcessed(List<ProductSummary> products, List<Constants.PROCESS_RESULT> results) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.MARK_PROCESSED);
			int i = 0;
			for(ProductSummary prod: products){
				stmt.setString(1, results.get(i).getDbValue());
				stmt.setLong(2, prod.getDownloadId());
				stmt.addBatch();
				i++;
			}
			stmt.executeBatch();
			
		} finally {
			closeStmt(stmt);			
			pool.releaseConnection(conn);
		}
	}
	
	/**
	 * @param prodName Product Name
	 * @param asOf Price history is latest as of asOf. If there is a price after asOf, that price 
	 *             will not be fetched.
	 * @return All products with prodName
	 * @throws SQLException
	 */
	public Set<ProductSummary> getProductSummary(String prodName, Date asOf) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, catStmt = null;
		ResultSet results = null, categoryResults = null;
		Set<ProductSummary> products = new HashSet<ProductSummary>();		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT);
			prodStmt.setString(1, prodName);
			results = prodStmt.executeQuery();
			while (results.next()) {
				ProductSummary product = getProductSummary(results);
				if(!ConfigParms.getInstance().isProduction()){//for unit test
					catStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_CATEGORIES);
					catStmt.setInt(1, product.getId());
					categoryResults = catStmt.executeQuery();
					while(categoryResults.next())
						product.addCategory(categoryResults.getInt(ProductCategory.Columns.CATEGORY_ID));
					closeRS(categoryResults);
					closeStmt(catStmt);
				}//if(!ConfigParms.getInstance().isProduction() ends...
				products.add(product);
			}
		} finally {		
			closeRS(results);
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
		return products;
	}
		
	/**
	 * @param prodId Product Id
	 * @return Product with prodId
	 * @throws SQLException
	 */
	public ProductSummary getProductSummaryByProductId(int prodId) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, catStmt = null;
		ResultSet results = null, categoryResults = null;
		ProductSummary product = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_BY_PRODUCT_ID);
			prodStmt.setInt(1, prodId);
			results = prodStmt.executeQuery();
			if (results.next()) {
				product = getProductSummary(results);
				if(!ConfigParms.getInstance().isProduction()){//for unit test
					catStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_CATEGORIES);
					catStmt.setInt(1, product.getId());
					categoryResults = catStmt.executeQuery();
					while(categoryResults.next())
						product.addCategory(categoryResults.getInt(ProductCategory.Columns.CATEGORY_ID));
					closeRS(categoryResults);
					closeStmt(catStmt);
				}//if(!ConfigParms.getInstance().isProduction() ends...
			}
		} finally {		
			closeRS(results);
			closeStmt(catStmt);
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
		return product;
	}

	/**
	 * @param prodId UID
	 * @return Product with prodId
	 * @throws SQLException
	 */
	public ProductSummary getProductSummaryByUID(String UID, String retailer) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null;
		ResultSet results = null;
		ProductSummary product = null;
		try {
			metProductInsUidQry.start();
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_BY_UID_AND_RETAILER);
			prodStmt.setString(1, UID);
			prodStmt.setString(2, retailer);
			results = prodStmt.executeQuery();
			if (results.next()) {
				product = getProductSummary(results);
				logger.info("revived inactive product " + product.getId());
			}
			metProductInsUidQry.end();
		} finally {		
			closeRS(results);
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
		return product;
	}
	/**
	 * backward compatibility
	 * @param products
	 * @param existingProds
	 * @throws SQLException
	 */
	public void insertUpdateProductSummary(List<ProductSummary> products, HashMap<ProductSummary, ProductSummary> existingProds) throws SQLException{
		insertUpdateProductSummary(products, existingProds, emptyProductCategories);
	}
	/**
	 * Inserts product summary along with price history and product categories if it is a new product. If the product 
	 * exists, then product url, image url are updated and the new entry is created in the price history table if 
	 * the price has changed, and new category is inserted in the product category table if this product does not have
	 * an entry for that category. 
	 * @param prodSummary
	 * @throws SQLException 
	 */
	public void insertUpdateProductSummary(List<ProductSummary> products, HashMap<ProductSummary, ProductSummary> existingProds,
			Map<Integer, List<Integer>> existingProductCategories) throws SQLException{
		PreparedStatement prodStmt = null;
		Statement prodStmtUpdate = null;
		DbConnection conn = pool.getConnection();
		ProductCompositeAttributeHistoryDAO histDAO = new ProductCompositeAttributeHistoryDAO();
		ProductCategoryDAO prodCatDAO = new ProductCategoryDAO();
		List<Constants.PROCESS_RESULT> processResults = new ArrayList<Constants.PROCESS_RESULT>(products.size());
	
		try {
			Connection sqlConn = conn.getConnection();
			histDAO.startInsertBatch(sqlConn);
			prodCatDAO.startInsertBatch(sqlConn);			
			prodStmt = sqlConn.prepareStatement(Queries.INSERT_PRODUCT_SUMM);
			prodStmtUpdate = sqlConn.createStatement();
			
			for(ProductSummary prod : products){
				if(MigrationFilters.filter(prod)){
					logger.info("skipping due to filter: " + prod.getRetailerId() + ":" + prod.getName());
					processResults.add(Constants.PROCESS_RESULT.FILTERED);
					continue;
				}
				
				if(!ProductUtils.isProductMigratable(prod)){
					logger.info("skipping as nonmigratable: newprod=" + prod);
					processResults.add(Constants.PROCESS_RESULT.UNMIGRATABLE);
					continue;
				}

				ProductSummary existing = ProductUtils.findMatch(prod, existingProds);
				//try to recover if this product is part of inactive pool
				if(existing == null)
					existing = getProductSummaryByUID(ProductUID.get(prod.getRetailerId(), prod.getUrl()), prod.getRetailerId());
				
				if(existing == null){//New product
					metProductIns.start();
					insertProduct(prodStmt, prod);
					metProductIns.end();
				}else{
					metProductUpd.start();
//					//note: this logic won't kick in since we don't cache inactive products now
//					if(!existing.isActive()){
//						logger.info("reviving inactive product "+existing.getId());
//						existing = getProductSummaryByProductId(existing.getId());
//					}
//					//note: end
					updateProduct(prodStmtUpdate, prod, existing);
					metProductUpd.end();
				}//else of 'if existing==null'
				updateProductCategory(prodCatDAO, prod, existing);
				existingProds.put(prod, prod);//update our cache
				metHistIns.start();
				histDAO.insertBatch(prod, existing, false);
				metHistIns.end();
				processResults.add(Constants.PROCESS_RESULT.MIGRATED);
			}//for(ProductSummary prod : products){

			metProductUpd.start();
			prodStmtUpdate.executeBatch();
			metProductUpd.end();
			metProductCatUpd.start();
			prodCatDAO.endInsertBatch();
			metProductCatUpd.end();
			metHistIns.start();
			histDAO.endInsertBatch();
			metHistIns.end();
			metMarkProcessed.start();
			setProcessed(products, processResults);
			metMarkProcessed.end(products.size());

			if(!ConfigParms.isUnitTestMode()){
				logger.info("\t" + metProductIns.currentStats());
				if(metProductInsUidQry.average(TimeUnit.MILLISECONDS)>0)
					logger.info("\t" + metProductInsUidQry.currentStats());
				logger.info("\t" + metProductUpd.currentStats());
				logger.info("\t" + "PERF:\t real:"+ realUpdates+ ", dummy: " + dummyUpdates);
				logger.info("\t" + metProductCatUpd.currentStats());
				logger.info("\t" + metHistIns.currentStats());
				if(metMarkProcessed.average(TimeUnit.MILLISECONDS)>0)
					logger.info("\t" + metMarkProcessed.currentStats());
			}
			
		}
		catch(SQLException e ){			
			logger.error(e.toString(), e);
			throw e;
		}
		finally{
			closeStmt(prodStmt);
			closeStmt(prodStmtUpdate);
			pool.releaseConnection(conn);
		}
	}


	/**
	 * @param prodCatDAO
	 * @param prod
	 * @param existing
	 * @throws SQLException
	 */
	private void updateProductCategory(ProductCategoryDAO prodCatDAO,
			ProductSummary prod, ProductSummary existing) throws SQLException {
		if(existing==null || !existing.getCategories().contains(prod.getCategoryId())){
			metProductCatUpd.start();
			prodCatDAO.insertBatch(prod, false);
			metProductCatUpd.end();
		}
		if(existing!=null){//copy over categories to new product entry
			if(existing.getCategories().contains(prod.getCategoryId()))
				prod.setCategories(existing.getCategories());
			else
				prod.addCategories(existing.getCategories());
		}
	}


	/**
	 * @param prodStmt
	 * @param prod
	 * @throws SQLException
	 * internal use only - for insertUpdateProductSummary
	 */
	private void insertProduct(PreparedStatement prodStmt, ProductSummary prod)
			throws SQLException {
		int retryCount=1;
		boolean success=false;
		while(retryCount++<=3){
			try{
				int i=0;
				int productId = UniqIdGenerator.get().getNextProdId();
				prod.setId(productId);
				prodStmt.setInt(++i, productId);
				prodStmt.setString(++i, ProductUID.get(prod.getRetailerId(), prod.getUrl()));
				prodStmt.setString(++i,  prod.getRetailerId());
				prodStmt.setString(++i, prod.getName() != null?prod.getName():"");
				prodStmt.setDouble(++i, prod.getPrice());
				prodStmt.setDouble(++i, prod.getReviewRating());
				prodStmt.setInt(++i, prod.getNumReviews());
				prodStmt.setInt(++i, prod.getSalesRank());
				prodStmt.setString(++i, prod.getModel() != null?prod.getModel():"");
				prodStmt.setString(++i, prod.getImageUrl() != null?prod.getImageUrl():"");
				prodStmt.setTimestamp(++i, new Timestamp(prod.getDownloadTime().getTime()));
				prodStmt.setString(++i, prod.getUrl());//Url will never be null
				prodStmt.setBoolean(++i, prod.isActive());
				prodStmt.setTimestamp(++i, new Timestamp(prod.getDownloadTime().getTime()));
				prodStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
				prodStmt.execute();
				success=true;
				break;
			}catch(MySQLIntegrityConstraintViolationException e){
				logger.warn("try again to get new productId, handing exception "+e.toString());
				try {
					logger.info("sleep for 5 seconds");
					Thread.sleep(5*1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}//while ends
		if(!success){
			logger.info("Retry count exceeded, stopping process");
			Emailer.getInstance().sendText("Product insert failed, bringing down process", 
					"retry count exceeded as well for inserts");
			System.exit(1);
		}
	}

	/*
	 * internal use only method for insertUpdateProductSummary
	 */
	private void updateProduct(	Statement prodStmtUpdate, ProductSummary prod, ProductSummary existing) throws SQLException {
		prod.setId(existing.getId());
		/*If product exists, update the dynamic attributes i.e. the attributes which change frequently.
		  TODO: generate the query to update the dynamic product attributes using a
		  configurable set of product attributes*/
		//Update if its a newer product
		if(prod.getDownloadTime().after(existing.getDownloadTime())){
			boolean isModified = false;
			TreeMap<String, ISQLType> updateCols = new TreeMap<>();
			TreeMap<String, ISQLType> whereCols = new TreeMap<>();
			String prev = "";
			String now = "";
			now = prod.getName() == null || prod.getName().trim().length() == 0?"":prod.getName();
			prev = existing.getName() == null || existing.getName().trim().length() == 0?"":existing.getName();
			if(!now.equalsIgnoreCase(prev)){
				isModified = true;
				updateCols.put(ProductSummary.Columns.PRODUCT_NAME, new SQLString(now));
			}
			now = prod.getUrl() == null || prod.getUrl().trim().length() == 0?"":prod.getUrl();
			prev = existing.getUrl() == null || existing.getUrl().trim().length() == 0?"":existing.getUrl();
			if(!now.equalsIgnoreCase(prev)){
				isModified = true;
				updateCols.put(ProductSummary.Columns.URL, new SQLString(now));
			}
			if(prod.getPrice() != existing.getPrice()){
				isModified = true;
				updateCols.put(ProductSummary.Columns.PRICE, new SQLDouble(prod.getPrice()));
			}
			if(prod.getReviewRating() != existing.getReviewRating()){
				isModified = true;
				updateCols.put(ProductSummary.Columns.REVIEW_RATING,new SQLDouble(prod.getReviewRating()));
			}
			if(prod.getNumReviews() != existing.getNumReviews()){
				isModified = true;
				updateCols.put(ProductSummary.Columns.NUM_REVIEWS, new SQLInteger(prod.getNumReviews()));
			}
			if(prod.getSalesRank() != existing.getSalesRank()){
				//isModified = true; //no longer critical update and unnecessary creates load on ProductUpdater thread 
				updateCols.put(ProductSummary.Columns.BEST_SELLER_RANK, new SQLInteger(prod.getSalesRank()));
			}
			now = prod.getModel() == null || prod.getModel().trim().length() == 0?"":prod.getModel();
			prev = existing.getModel() == null || existing.getModel().trim().length() == 0?"":existing.getModel();
			if(!now.equalsIgnoreCase(prev)){
				isModified = true;
				updateCols.put(ProductSummary.Columns.MODEL_NO, new SQLString(now));
			}
			now = prod.getImageUrl() == null || prod.getImageUrl().trim().length() == 0?"":prod.getImageUrl();
			prev = existing.getImageUrl() == null || existing.getImageUrl().trim().length() == 0?"":existing.getImageUrl();
			if(!now.equalsIgnoreCase(prev)){
				isModified = true;
				updateCols.put(ProductSummary.Columns.IMAGE_URL, new SQLString(now));
			}
			if(existing.isActive()!=prod.isActive()){
				isModified = true;
				updateCols.put(ProductSummary.Columns.ACTIVE, new SQLBoolean(prod.isActive()));
			}
			if(isModified){
				realUpdates++;
				updateCols.put(ProductSummary.Columns.TIME_MODIFIED, new SQLTimestamp(new Date()));
			}else
				dummyUpdates++;
			//always update the last downloaded time
			updateCols.put(ProductSummary.Columns.LAST_DOWNLOAD_TIME, new SQLTimestamp(prod.getDownloadTime()));
			whereCols.put(ProductSummary.Columns.PRODUCT_ID, new SQLInteger(prod.getId()));
			prodStmtUpdate.addBatch(QueryBuilder.updateQuery("PRODUCTS", "PRODUCT_SUMMARY", updateCols, whereCols));
		}
	}
	
	/*
	 * RegTest Cleanup Method
	 * Remove products for given retailer
	 */
	public void regTestRemoveProducts(String retailerId) throws SQLException{
		//safey check
		if(!retailerId.toUpperCase().contains("TEST"))
			throw new Bhagte2BandBajGaya("can't remove rows for non-test retailers");
		DbConnection conn = pool.getConnection();
		Metric metRegTestRemove = new Metric("RegTestRemoveProducts");
		PreparedStatement removePriceStmt=null;
		PreparedStatement removeReviewStmt=null;
		//PreparedStatement removeSellRankStmt=null;
		PreparedStatement removeCategoryStmt=null;
		PreparedStatement removeProductCategoryStmt=null;
		PreparedStatement removeProdStmt=null;

		//PreparedStatement removePriceStaleStmt=null;
		//PreparedStatement removeReviewStaleStmt=null;
		//PreparedStatement removeSellRankStaleStmt=null;
		//PreparedStatement removeProdStaleStmt=null;
		try {
			metRegTestRemove.start();
			Connection sqlConn = conn.getConnection();
			removePriceStmt = sqlConn.prepareStatement(String.format(Queries.REMOVE_PRODUCT_PRICES_HISTORY, RetailerTable.getPricesHistoryTable(retailerId)));
			removeReviewStmt = sqlConn.prepareStatement(String.format(Queries.REMOVE_PRODUCT_REVIEWS_HISTORY, RetailerTable.getReviewsHistoryTable(retailerId)));
			//removeSellRankStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_SELL_RANKS_HISTORY);
			removeCategoryStmt = sqlConn.prepareStatement(Queries.REMOVE_CATEGORY);
			removeProductCategoryStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_CATEGORY);
			removeProdStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_SUMM);
			
			removePriceStmt.setString(1, retailerId);
			removeReviewStmt.setString(1, retailerId);
			//removeSellRankStmt.setString(1, retailerId);
			removeCategoryStmt.setString(1, retailerId);
			removeProductCategoryStmt.setString(1, retailerId);
			removeProdStmt.setString(1, retailerId);
			int rowsDeleted = removePriceStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " price rows");
			//rowsDeleted = removeSellRankStmt.executeUpdate();
			//logger.info("Deleted "+rowsDeleted+ " sell rank rows");
			rowsDeleted = removeReviewStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " reviews rows");
			rowsDeleted = removeProductCategoryStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " product category rows");
			rowsDeleted = removeCategoryStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " category rows");
			rowsDeleted = removeProdStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " product rows");
			metRegTestRemove.end();
			logger.info("\t" + metRegTestRemove.currentStats());

			//remove stale entries
			/*
			 * note: if we decide to enable below then we will need to add table split logic here
			removePriceStaleStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_PRICES_HISTORY_STALE);
			removeReviewStaleStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_REVIEWS_HISTORY_STALE);
			//removeSellRankStaleStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_SELL_RANKS_HISTORY_STALE);
			removeProdStaleStmt = sqlConn.prepareStatement(Queries.REMOVE_PRODUCT_SUMMARY_STALE);
			removePriceStaleStmt.setString(1, retailerId);
			removeReviewStaleStmt.setString(1, retailerId);
			//removeSellRankStaleStmt.setString(1, retailerId);
			removeProdStaleStmt.setString(1, retailerId);
			*/
			/*
			rowsDeleted = removePriceStaleStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " stale price rows");
			rowsDeleted = removeSellRankStaleStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " stale sell rank rows");
			rowsDeleted = removeReviewStaleStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " stale reviews rows");
			rowsDeleted = removeProdStaleStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " stale product rows");
			*/
		}
		catch(SQLException e ){			
			logger.error(e.toString(), e);
			throw e;
		}
		finally{
			closeStmt(removePriceStmt);
			closeStmt(removeCategoryStmt);
			closeStmt(removeProdStmt);
			pool.releaseConnection(conn);
		}
		
	}
	
	public void updateProductRecStatus(long id, boolean status, String text) throws SQLException{
		List<Long> ids = new ArrayList<>(1);
		ids.add(id);
		List<Boolean> statuses = new ArrayList<>(1);
		statuses.add(status);
		List<String> texts = new ArrayList<>(1);
		texts.add(text);
		updateProductRecStatus(ids, statuses, texts);
	}
	
	public void updateProductRecStatus(List<Long> ids, List<Boolean> statuses, List<String> texts)
			throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.UPDATE_PROD_REC_STATUS);
			for(int idx = 0; idx < ids.size(); idx++){
				int i = 0;
				stmt.setBoolean(++i, statuses.get(idx));
				stmt.setString(++i, texts.get(idx));
				stmt.setLong(++i, ids.get(idx));
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
		finally {			
			pool.releaseConnection(conn);
			closeStmt(stmt);				
		}
	}
	

	/*
	 * Return products which are updated after given time
	 * @param retailerId- if null returns updates of all retailers
	 * @param modifiedTime
	 */
	public List<ProductSummary> getUpdatedProducts(String retailerId, Timestamp modifiedTime) throws SQLException {
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null;
		ResultSet resultsRS = null;
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			ArrayList<ProductSummary>products = new ArrayList<>();
			
			if(retailerId==null)
				prodStmt = sqlConn.prepareStatement(Queries.GET_UPDATED_PRODUCTS);
			else{
				prodStmt = sqlConn.prepareStatement(Queries.GET_UPDATED_PRODUCTS_BY_RETAILER);
				prodStmt.setString(2, retailerId);
			}
			
			prodStmt.setString(1, modifiedTime.toString());
			resultsRS = prodStmt.executeQuery();

			while (resultsRS.next()) {
				Double reviewRating = resultsRS.getDouble(ProductSummary.Columns.REVIEW_RATING); 
				Integer numReviews = resultsRS.getInt(ProductSummary.Columns.NUM_REVIEWS);
				Integer salesRank = resultsRS.getInt(ProductSummary.Columns.BEST_SELLER_RANK);
				ProductSummaryBuilder b = new ProductSummaryBuilder();
				b.retailerId = resultsRS.getString(ProductSummary.Columns.RETAILER_ID);
				b.name = resultsRS.getString(ProductSummary.Columns.PRODUCT_NAME);
				b.price = resultsRS.getDouble(ProductSummary.Columns.PRICE);
				b.url = resultsRS.getString(ProductSummary.Columns.URL);
				b.imageUrl = resultsRS.getString(ProductSummary.Columns.IMAGE_URL);
				//b.desc = resultsRS.getString(ProductSummary.Columns.DESCRIPTION);
				b.model = resultsRS.getString(ProductSummary.Columns.MODEL_NO);
				b.prodId = resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID);
				b.reviewRating = reviewRating == null?-1: reviewRating;
				b.numReviews = numReviews == null?-1: numReviews;
				b.salesRank = salesRank == null?-1: salesRank;
				b.downloadTime = resultsRS.getTimestamp(ProductSummary.Columns.LAST_DOWNLOAD_TIME);
				b.categoryId = resultsRS.getInt(ProductSummary.Columns.CATEGORY_ID);
				b.active = resultsRS.getBoolean(ProductSummary.Columns.ACTIVE);

				ProductSummary product = b.build();
				products.add(product);
			}
			return products;
		}
		finally {		
			closeStmt(prodStmt);
			closeRS(resultsRS);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * Return max time_modified from product_summary table
	 */
	public Timestamp getProductMaxModifiedTime() throws SQLException{
		DbConnection conn = null;
		PreparedStatement prodStmt = null;
		ResultSet resultsRS = null;
		Timestamp modifiedTime =null;
		try {
			conn = pool.getConnection();
			Connection sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_MAX_TIME_MODIFIED_PRODUCTS);
			resultsRS = prodStmt.executeQuery();
			
			while (resultsRS.next()) {
				String time = resultsRS.getString(1);
				modifiedTime = Timestamp.valueOf(time);
			}
			return modifiedTime;
		}
		finally {
			closeRS(resultsRS);
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
	}
}
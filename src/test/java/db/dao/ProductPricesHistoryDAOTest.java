package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import util.ConfigParms;
import util.Constants;
import util.DateTimeUtils;
import util.RetailerTable;
import util.build.ProductSummaryBuilder;
import db.DbConnection;
import db.DbConnectionPool;
import db.dao.ProductPricesHistoryDAO.QUERYTYPE;
import entities.ProductPricesHistory;
import entities.ProductSummary;
import entities.Retailer;

public class ProductPricesHistoryDAOTest extends AbstractTest
{
	private static final Logger logger = Logger.getLogger(ProductPricesHistoryDAOTest.class);
	private ProductPricesHistoryDAO dao = new ProductPricesHistoryDAO();
	private DbConnectionPool pool = null;
	private DbConnection conn = null;

	private static final long EPSILON_MS = 1000;

	@Before
	public void setUp() throws Exception
	{
		ConfigParms.enableUnitTestMode();
		pool = DbConnectionPool.get();
		conn = pool.getConnection();
		dao.startInsertBatch(conn.getConnection());
	}

	@After
	public void tearDown() throws Exception
	{
		pool.releaseConnection(conn);
		dao.endInsertBatch();
		if(RetailerTable.isTableSplitEnabled()){
			DataAccessObject.executeUpdate("DELETE FROM PRODUCT_PRICES_HISTORY_1 limit 100");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCT_PRICES_HISTORY_2 limit 100");
		}else{
			DataAccessObject.executeUpdate("DELETE FROM PRODUCT_PRICES_HISTORY limit 100");
		}
	}

	@Test
	public void testInsertBatchNewProducts()
	{
		int prodNew1Id = Math.abs((int)System.currentTimeMillis());
		int prodNew2Id = prodNew1Id + 1;
		int prodNew3Id = prodNew1Id + 2;

		ProductSummaryBuilder builder = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product1", Retailer.TESTRETAILER.getLink() + prodNew1Id,
				"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(100.1).reviewRating(2.0).numReviews(100).
				salesRank(100).downloadTime(new Date()).productId(prodNew1Id);
		ProductSummary prodNew1 = builder.build();
		builder.url(Retailer.TESTRETAILER.getLink() + prodNew2Id).productId(prodNew2Id).price(100.2);
		ProductSummary prodNew2 = builder.build();

		//insert product for different retailer which should go to different price history table
		builder.retailer(Retailer.TESTRETAILER2.getId()).url(Retailer.TESTRETAILER2.getLink() + prodNew3Id).productId(prodNew3Id).price(100.3);
		ProductSummary prodNew3 = builder.build();
		
		try
		{
			// Should not insert as flush is false
			dao.insertBatch(prodNew1, null, false);
			//NOTE: use hardcoded table names in this test because this test verifies product data is going in right tables
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodNew1Id, null);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodNew2Id, null);

			// Should insert all products
			dao.insertBatch(prodNew2, null, true);
			dao.insertBatch(prodNew3, null, true);
			
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodNew1Id, prodNew1);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodNew2Id, prodNew2);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_2 WHERE PRODUCT_ID=" + prodNew3Id, prodNew3);

		} catch (SQLException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testInsertBatchOlderThanExisting()
	{
		int prodId = Math.abs((int)System.currentTimeMillis());
		try
		{
			Date oldest = new Date();
			Date older = DateTimeUtils.add(oldest, Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);
			Date current = DateTimeUtils.add(oldest, 2*Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);			

			ProductSummaryBuilder builder = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product1", Retailer.TESTRETAILER.getLink() + prodId,
					"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(100.1).reviewRating(2.0).numReviews(100).
					salesRank(100).downloadTime(current).productId(prodId);
			ProductSummary prodCurrent = builder.build();
			
			builder.downloadTime = oldest;
			ProductSummary prodOldest = builder.build();
			// Insert the curent product
			dao.insertBatch(prodCurrent, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId, prodCurrent);
			// Now we get old product, everything else is the same. Should
			// insert the price again as it will compare against something
			// older than this one and wont find anything
			dao.insertBatch(prodOldest, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='"
					+ DateTimeUtils.getTimeYYYYMMDDHHMMSS(oldest) + "'", prodOldest);
			builder.downloadTime = older;
			ProductSummary prodOlder = builder.build();
			// Should not insert as it will compare against the oldest product
			// and will find no difference.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='"
					+ DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", null);
			builder.price = builder.price + 1;
			prodOlder = builder.build();
			// Should insert as it is different from prodOldest now.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='"
					+ DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", prodOlder);

		} catch (Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testInsertBatchNewerThanExisting()
	{
		int prodId = Math.abs((int)System.currentTimeMillis());
		try
		{
			Date existingTime = new Date();
			Date newTime = DateTimeUtils.add(existingTime, 1000, TimeUnit.MILLISECONDS, 0);
			ProductSummaryBuilder builder = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product1", Retailer.TESTRETAILER.getLink() + prodId,
					"http://www.images.com", "Model 1").categoryId(1000).categoryName("TestCategory").price(100.1).reviewRating(2.0).numReviews(100).
					salesRank(100).downloadTime(existingTime).productId(prodId);
			ProductSummary prodExisting = builder.build();
			// Insert existing product
			dao.insertBatch(prodExisting, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId, prodExisting);
			builder.downloadTime = newTime;
			ProductSummary prodExistingNew = builder.build();
			// Should not insert anything as the new is the same as existing
			// product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='"
					+ DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", null);
			builder.price = 100.2;
			prodExistingNew = builder.build();
			// Should insert as the price changed in the new product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='"
					+ DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", prodExistingNew);
		} catch (Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void assertExpected(String qryActual, ProductSummary expected) throws SQLException
	{
		ResultSet rs = null;
		rs = DataAccessObject.execute(qryActual);
		if (expected == null)
		{
			_assertTrue(!rs.next());
			return;
		}
		_assertTrue(rs.next());
		ProductPricesHistory rh = dao.get(rs);
		_assertTrue(rh.getPrice() == expected.getPrice());
		_assertTrue(Math.abs(rh.getTime().getTime() - expected.getDownloadTime().getTime()) <= EPSILON_MS);
		_assertTrue(!rs.next());
		rs.close();
		DataAccessObject.closeRS(rs);
	}

	@Test
	public void testgetPriceHistoryForProductsWithPriceChange()
	{
		int prodId = Math.abs((int)System.currentTimeMillis());
		try
		{
			Date oldest = new Date();
			Date older = DateTimeUtils.add(oldest, 60, TimeUnit.MINUTES, 0);
			Date current = DateTimeUtils.add(oldest, 120, TimeUnit.MINUTES, 0);
			ProductSummaryBuilder builder = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product1", Retailer.TESTRETAILER.getLink() + prodId,
					"http://www.images.com", "Model 1").categoryId(1000).categoryName("TestCategory").price(100.1).reviewRating(2.0).numReviews(100).
					salesRank(100).downloadTime(current).productId(prodId);
			ProductSummary prodCurrent = builder.build();
			builder.downloadTime = oldest;
			builder.price = 200;
			ProductSummary prodOldest = builder.build();

			builder.downloadTime = older;
			ProductSummary prodOlder = builder.build();
			builder.price = builder.price + 1;
			prodOlder = builder.build();

			builder.prodId = prodId + 1;
			builder.downloadTime = DateTimeUtils.add(current, 5, TimeUnit.MINUTES, 0);
			ProductSummary diffProduct = builder.build();
			dao.insertBatch(prodOldest, null, true);
			dao.insertBatch(prodOlder, null, true);
			dao.insertBatch(prodCurrent, null, true);
			dao.insertBatch(diffProduct, null, true);

			// Query by download time
			// 1 products with 3 price history objects
			Map<Integer, List<ProductPricesHistory>> priceHistoryMap = dao.getPriceHistoryForProductsWithPriceChange(
					prodId, DateTimeUtils.add(oldest, 1, TimeUnit.MINUTES, 0),
					DateTimeUtils.add(older, 1, TimeUnit.MINUTES, 0), QUERYTYPE.PRICE_TIME, prodCurrent.getRetailerId());
			_assertTrue(priceHistoryMap.size() == 1);
			_assertTrue(priceHistoryMap.get(Integer.valueOf(prodId)).size() == 2);
			_assertTrue(priceHistoryMap.get(Integer.valueOf(diffProduct.getId())) == null);
			

			
			// Query by download time
			// No Product should be returned as there is no history for diffProduct
			priceHistoryMap = dao.getPriceHistoryForProductsWithPriceChange(prodId,
					DateTimeUtils.add(current, 4, TimeUnit.MINUTES, 0),
					DateTimeUtils.add(current, 10, TimeUnit.MINUTES, 0), QUERYTYPE.PRICE_TIME, prodCurrent.getRetailerId());
			_assertEquals(1, priceHistoryMap.size());
			
			//TODO: Fix test for Query by modified time
			/*
			 * 			
			 * Date timeModified1 = productPriceHistory.get(0).getTimeModified();
			 * Date timeModified2 = productPriceHistory.get(1).getTimeModified();
			List<ProductPricesHistory> productPriceHistory = priceHistoryMap.get(Integer.valueOf(prodId));
			priceHistoryMap = dao.getPriceHistoryForProductsWithPriceChange(prodId,
					DateTimeUtils.add(timeModified1, -1, TimeUnit.SECONDS, 0),
					DateTimeUtils.add(timeModified1, 1, TimeUnit.SECONDS, 0), QUERYTYPE.ROW_MODIFIED_TIME);
			assertTrue(priceHistoryMap.size() == 2);
			*/
		}catch (Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetLatestProductPricesHistory(){
		int prodId = Math.abs((int)System.currentTimeMillis());
		try
		{
			Date current = new Date();
			Date older = DateTimeUtils.add(current, -5, TimeUnit.SECONDS);
			Date oldest = DateTimeUtils.add(current, -10, TimeUnit.SECONDS);
			logger.info(current);
			logger.info(older);
			logger.info(oldest);
			
			logger.info("============ MAKING CURRENT PRODUCT ============");
			ProductSummaryBuilder b = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product1", Retailer.TESTRETAILER.getLink() + prodId, 
					"http://www.images.com", "Model 1").categoryId(1000).categoryName("TestCategory").price(100.1).
					downloadTime(current).productId(prodId);
			ProductSummary prodCurrent = b.build();

			logger.info("============ MAKING OLDER PRODUCT - SAME PRODUCT ID============");
			b.downloadTime = b.creationTime = older;
			b.price = b.price + 1;
			ProductSummary prodOlder = b.build();

			logger.info("============ MAKING OLDEST PRODUCT - SAME PRODUCT ID ============");
			b.downloadTime = b.creationTime = oldest;
			b.price = 200;
			ProductSummary prodOldest = b.build();
			
			logger.info("============ MAKING DIFFERENT PRODUCT - DIFFERENT PRODUCT ID ============");
			b.prodId = prodId + 1;
			b.downloadTime = b.creationTime = DateTimeUtils.add(current, 10, TimeUnit.SECONDS, 0);
			ProductSummary diffProduct = b.build();
			
			logger.info("============ INSERTING INTO THE DATABASE ============");
			dao.insertBatch(prodOldest, null, false);
			//TODO: doing this so that this doesn't come in updated but this may fail build if time taken is more or less
			//TODO: and insert operation automatically takes current time for inserting so, here time set by us is of no use as such
			dao.insertBatch(prodOlder, null, false);
			dao.insertBatch(prodCurrent, null, false);
			dao.insertBatch(diffProduct, null, true);

			logger.info("============ GETTING ALL THE LATEST PRODUCT PRICES ============");
			Map<Integer, List<ProductPricesHistory>> latestProdPrices = new HashMap<Integer, List<ProductPricesHistory>>();
			latestProdPrices = dao.getLatestProductPricesHistory(new Timestamp(older.getTime()-1000), prodCurrent.getRetailerId());//-1sec to take care of rounding
			
			logger.info("============ MAKING ALL THE ASSERTIONS ============");
			_assertEquals(2, latestProdPrices.size());
			_assertEquals(2, latestProdPrices.get(prodId).size());
			_assertEquals(1, latestProdPrices.get(prodId+1).size());		
			assertProductPriceHistory(prodOlder, latestProdPrices);
			assertProductPriceHistory(prodCurrent, latestProdPrices);
			assertProductPriceHistory(diffProduct, latestProdPrices);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void assertProductPriceHistory(ProductSummary expected, Map<Integer, List<ProductPricesHistory>> actual){
		assertTrue(actual.get(expected.getId())!=null);
		
		List<ProductPricesHistory> prodPrices = actual.get(expected.getId());
		boolean found = false;
		for(ProductPricesHistory prodPrice : prodPrices){
			if(prodPrice.getPrice() == expected.getPrice()){
				found = true;
				assertTrue(prodPrice.getProductId()==expected.getId());
				assertTrue(DateTimeUtils.diff(prodPrice.getTime(), expected.getDownloadTime(), TimeUnit.MILLISECONDS)<=1);
			}
		}
		assertTrue(found==true);
	}
}

package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.Constants;
import util.DateTimeUtils;
import util.build.ProductSummaryBuilder;
import db.DbConnection;
import db.DbConnectionPool;
import entities.ProductSellRanksHistory;
import entities.ProductSummary;

public class ProductSellRanksHistoryDAOTest {

	private ProductSellRanksHistoryDAO dao =  new ProductSellRanksHistoryDAO();
	private DbConnectionPool pool = null;
	private DbConnection conn = null;
	
	private static final long EPSILON_MS = 1000;

	private enum TEST_RETAILER{
		RETAILER1("walmart", "http://www.walmart.com/ip/test-product/"),
		RETAILER2("target", "http://m.target.com/p/carson-space-saver-leaning-bookcase-cherry/-/A-");

		private final String name;
		private final String baseUrl;
		TEST_RETAILER(String name, String baseUrl){
			this.name = name;
			this.baseUrl = baseUrl;
		}
	}

	@Before
	public void setUp() throws Exception {
		pool = DbConnectionPool.get();
		conn = pool.getConnection();
		//dao.startInsertBatch(conn.getConnection());
	}

	@After
	public void tearDown() throws Exception {
		pool.releaseConnection(conn);
		//dao.endInsertBatch();
	}
	@Test
	public void dummyTest(){
		
	}
/*	DISABLE TESTS UNTIL SELL RANK IS REIMPLEMENTED
	@Test
	public void testInsertBatchNewProducts() {
		int prodNew1Id = Math.abs((int)System.currentTimeMillis());
		int prodNew2Id = prodNew1Id + 1;
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.retailerId = TEST_RETAILER.RETAILER1.name;
		b.categoryId = 1000;
		b.categoryName = "TestCategory";
		b.name = "Product1";
		b.price = 100.1;
		b.url = TEST_RETAILER.RETAILER1.baseUrl + prodNew1Id;
		b.imageUrl = "http://www.images.com";
		b.desc = "Description 1";
		b.model = "Model 1";
		b.reviewRating = 2.0;
		b.numReviews = 100;
		b.salesRank = 100;
		b.downloadTime = new Date();
		b.prodId = prodNew1Id;
		ProductSummary prodNew1 = b.build();
		b.url = TEST_RETAILER.RETAILER1.baseUrl + prodNew2Id;
		b.prodId = prodNew2Id;
		ProductSummary prodNew2 = b.build();
		try {
			//Should not insert as flush is false
			dao.insertBatch(prodNew1, null, false);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew1Id, null);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew2Id, null);

			//Should insert prodNew1 and prodNew2
			dao.insertBatch(prodNew2, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew1Id, prodNew1);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew2Id, prodNew2);
						
			DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew1Id);
			DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodNew2Id);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInsertBatchOlderThanExisting(){
		int prodId = Math.abs((int)System.currentTimeMillis());
		try{
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			Date oldest = new Date();
			Date older = DateTimeUtils.add(oldest, Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);
			Date current = DateTimeUtils.add(oldest, 2*Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);
			b.retailerId = TEST_RETAILER.RETAILER1.name;
			b.categoryId = 1000;
			b.categoryName = "TestCategory";
			b.name = "Product1";
			b.price = 100.1;
			b.url = TEST_RETAILER.RETAILER1.baseUrl + prodId;
			b.imageUrl = "http://www.images.com";
			b.desc = "Description 1";
			b.model = "Model 1";
			b.reviewRating = 2.0;
			b.numReviews = 100;
			b.salesRank = 100;
			
			b.downloadTime = current;
			b.prodId = prodId;
			ProductSummary prodCurrent = b.build();
			b.downloadTime = oldest;
			ProductSummary prodOldest = b.build();
			//Insert the curent product
			dao.insertBatch(prodCurrent, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId, prodCurrent);
			//Now we get old product, everything else is the same. Should insert the reviews again as it will compare against something
			//older than this one and wont find anything
			dao.insertBatch(prodOldest, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(oldest) + "'", prodOldest);
			b.downloadTime = older;
			ProductSummary prodOlder = b.build();
			//Should not insert as it will compare against the oldest product and will find no difference.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", null);
			b.salesRank = b.salesRank + 1;
			prodOlder = b.build();
			//Should insert as it is different from prodOldest now.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", prodOlder);
			
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			try {
				DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	@Test
	public void testInsertBatchNewerThanExisting(){
		int prodId = Math.abs((int)System.currentTimeMillis());
		try{
			Date existingTime = new Date();
			Date newTime = DateTimeUtils.add(existingTime, 1000, TimeUnit.MILLISECONDS, 0);
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = TEST_RETAILER.RETAILER1.name;
			b.categoryId = 1000;
			b.categoryName = "TestCategory";
			b.name = "Product1";
			b.price = 100.1;
			b.url = TEST_RETAILER.RETAILER1.baseUrl + prodId;
			b.imageUrl = "http://www.images.com";
			b.desc = "Description 1";
			b.model = "Model 1";
			b.reviewRating = 2.0;
			b.numReviews = 100;
			b.salesRank = 100;
			b.downloadTime = existingTime;
			b.prodId = prodId;
			ProductSummary prodExisting = b.build();
			//Insert existing product
			dao.insertBatch(prodExisting, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId, prodExisting);
			b.downloadTime = newTime;
			ProductSummary prodExistingNew = b.build();
			//Should not insert anything as the new is the same as existing product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", null);
			b.salesRank = 50;
			prodExistingNew = b.build();
			//Should insert as the number of reviews changed in the new product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", prodExistingNew);
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			try {
				DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=" + prodId);
			} catch (SQLException e) {
			}
		}
		
	}
	*/
	private void assertExpected(String qryActual, ProductSummary expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			ProductSellRanksHistory rh = dao.get(rs);
			assertTrue(rh.getRank() == expected.getSalesRank());
			assertTrue(Math.abs(rh.getTime().getTime() - expected.getDownloadTime().getTime())<= EPSILON_MS);
			assertTrue(!rs.next());
			rs.close();
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}

}

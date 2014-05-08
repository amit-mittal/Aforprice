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
import entities.ProductReviewsHistory;
import entities.ProductSummary;

public class ProductReviewsHistoryDAOTest {

	private ProductReviewsHistoryDAO dao =  new ProductReviewsHistoryDAO();
	private DbConnectionPool pool = null;
	private DbConnection conn = null;
	
	private static final long EPSILON_MS = 1000;

	private enum TEST_RETAILER{
		RETAILER1("testretailer", "http://www.testretailer.com/"), 
		RETAILER2("testretailer2", "http://www.testretailer2.com/");

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
		dao.startInsertBatch(conn.getConnection());
	}

	@After
	public void tearDown() throws Exception {
		pool.releaseConnection(conn);
		dao.endInsertBatch();
		DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 LIMIT 10");
		DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_2 LIMIT 10");
	}

	@Test
	public void testInsertBatchNewProducts() {
		int prodNew1Id = Math.abs((int)System.currentTimeMillis());
		int prodNew2Id = prodNew1Id + 1;
		int prodNew3Id = prodNew1Id + 2;

		ProductSummaryBuilder b = new ProductSummaryBuilder(TEST_RETAILER.RETAILER1.name, "Product1", TEST_RETAILER.RETAILER1.baseUrl + prodNew1Id,
				"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(100.1).desc("Desc 1").reviewRating(2.0).numReviews(100).
				salesRank(100).downloadTime(new Date()).productId(prodNew1Id);
		ProductSummary prodNew1 = b.build();
		b.url = TEST_RETAILER.RETAILER1.baseUrl + prodNew2Id;
		b.prodId = prodNew2Id;
		ProductSummary prodNew2 = b.build();

		//insert product for different retailer which should go to different price history table
		b.retailer(TEST_RETAILER.RETAILER2.name).url(TEST_RETAILER.RETAILER2.baseUrl + prodNew3Id).productId(prodNew3Id).reviewRating(3.0).numReviews(99);
		ProductSummary prodNew3 = b.build();

		try {
			//Should not insert as flush is false
			dao.insertBatch(prodNew1, null, false);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodNew1Id, null);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodNew2Id, null);

			//Should insert all products with flush true
			dao.insertBatch(prodNew2, null, true);
			dao.insertBatch(prodNew3, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodNew1Id, prodNew1);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodNew2Id, prodNew2);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_2 WHERE PRODUCT_ID=" + prodNew3Id, prodNew3);
						
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInsertBatchOlderThanExisting(){
		int prodId = Math.abs((int)System.currentTimeMillis());
		try{
			Date oldest = new Date();
			Date older = DateTimeUtils.add(oldest, Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);
			Date current = DateTimeUtils.add(oldest, 2*Constants.PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN + 1, TimeUnit.MINUTES, 0);			
			ProductSummaryBuilder b = new ProductSummaryBuilder(TEST_RETAILER.RETAILER1.name, "Product1", TEST_RETAILER.RETAILER1.baseUrl + prodId,
					"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(100.1).desc("Desc 1").reviewRating(2.0).numReviews(100).
					salesRank(100).downloadTime(current).productId(prodId);
			ProductSummary prodCurrent = b.build();
			b.downloadTime = oldest;
			ProductSummary prodOldest = b.build();
			//Insert the curent product
			dao.insertBatch(prodCurrent, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId, prodCurrent);
			//Now we get old product, everything else is the same. Should insert the reviews again as it will compare against something
			//older than this one and wont find anything
			dao.insertBatch(prodOldest, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(oldest) + "'", prodOldest);
			b.downloadTime = older;
			ProductSummary prodOlder = b.build();
			//Should not insert as it will compare against the oldest product and will find no difference.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", null);
			b.reviewRating = b.reviewRating + 1;
			prodOlder = b.build();
			//Should insert as it is different from prodOldest now.
			dao.insertBatch(prodOlder, prodCurrent, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(older) + "'", prodOlder);
			
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
		
	@Test
	public void testInsertBatchNewerThanExisting(){
		int prodId = Math.abs((int)System.currentTimeMillis());
		try{
			Date existingTime = new Date();
			Date newTime = DateTimeUtils.add(existingTime, 1000, TimeUnit.MILLISECONDS, 0);
			ProductSummaryBuilder b = new ProductSummaryBuilder(TEST_RETAILER.RETAILER1.name, "Product1", TEST_RETAILER.RETAILER1.baseUrl + prodId,
					"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(100.1).desc("Desc 1").reviewRating(2.0).numReviews(100).
					salesRank(100).downloadTime(existingTime).productId(prodId);
			ProductSummary prodExisting = b.build();
			//Insert existing product
			dao.insertBatch(prodExisting, null, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId, prodExisting);
			b.downloadTime = newTime;
			ProductSummary prodExistingNew = b.build();
			//Should not insert anything as the new is the same as existing product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", null);
			b.numReviews = -1;
			prodExistingNew = b.build();
			//Should insert as the number of reviews changed in the new product
			dao.insertBatch(prodExistingNew, prodExisting, true);
			assertExpected("SELECT * FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY_1 WHERE PRODUCT_ID=" + prodId + " AND TIME='" + DateTimeUtils.getTimeYYYYMMDDHHMMSS(newTime) + "'", prodExistingNew);
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private void assertExpected(String qryActual, ProductSummary expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			ProductReviewsHistory rh = dao.get(rs);
			assertTrue(rh.getNumReviews() == expected.getNumReviews());
			assertTrue(rh.getRating() == expected.getReviewRating());
			assertTrue(Math.abs(rh.getTime().getTime() - expected.getDownloadTime().getTime())<= EPSILON_MS);
			assertTrue(!rs.next());
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}

}

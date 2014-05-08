package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.DateTimeUtils;
import util.build.ProductSummaryBuilder;
import db.DbConnection;
import db.DbConnectionPool;
import entities.ProductCategory;
import entities.ProductSummary;
import entities.Retailer;

public class ProductCategoryDAOTest {

	private ProductCategoryDAO dao = new ProductCategoryDAO();
	private DbConnectionPool pool = null;
	private DbConnection conn = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
	}

	@Test
	public void test_insertBatch() {
		int prodId = Math.abs((int)System.currentTimeMillis());
		try{
			ProductSummaryBuilder prod1TestRetailerB = new ProductSummaryBuilder();
			prod1TestRetailerB.prodId = prodId;
			prod1TestRetailerB.retailerId = Retailer.TESTRETAILER.getId();
			prod1TestRetailerB.categoryId = 1;
			prod1TestRetailerB.categoryName = "mycategory";
			prod1TestRetailerB.name = "myproduct";
			prod1TestRetailerB.price = 10.0;
			prod1TestRetailerB.url = Retailer.TESTRETAILER.getLink();
			prod1TestRetailerB.imageUrl = "http://www.walmart.com/image.jpg";
			//prod1WalmartB.desc = "desc1";
			prod1TestRetailerB.model = "model1";
			prod1TestRetailerB.reviewRating = 1;
			prod1TestRetailerB.numReviews = 100;
			prod1TestRetailerB.salesRank = 100;
			prod1TestRetailerB.prodId = prodId;
			prod1TestRetailerB.downloadTime = new Date();
			ProductSummary prod = prod1TestRetailerB.build();
			assertExpected("SELECT * FROM PRODUCT_CATEGORY WHERE PRODUCT_ID=" + prod.getId() + " AND CATEGORY_ID=" + prod.getCategoryId(),  dao.insertBatch(prod, true));
			prod1TestRetailerB.categoryId = 2;
			prod = prod1TestRetailerB.build();
			dao.insertBatch(prod, true);
			assertExpected("SELECT * FROM PRODUCT_CATEGORY WHERE PRODUCT_ID=" + prod.getId() + " AND CATEGORY_ID=" + prod.getCategoryId(),  dao.insertBatch(prod, true));
			Thread.sleep(5000);
			prod1TestRetailerB.categoryId = 1;
			dao.insertBatch(prod, true);
			assertExpected("SELECT * FROM PRODUCT_CATEGORY WHERE PRODUCT_ID=" + prod.getId() + " AND CATEGORY_ID=" + prod.getCategoryId(),  dao.insertBatch(prod, true));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			try {
				DataAccessObject.executeUpdate("DELETE FROM PRODUCT_CATEGORY WHERE PRODUCT_ID=" + prodId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void assertExpected(String qryActual, ProductCategory expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			ProductCategory pc = dao.get(rs);
			assertTrue(pc.getCategoryId() == expected.getCategoryId());
			assertTrue(pc.getProductId() == expected.getProductId());
			assertTrue(Math.abs(DateTimeUtils.diff(pc.getTimeModified(), expected.getTimeModified(), TimeUnit.SECONDS)) <= 1);
			rs.close();
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
}

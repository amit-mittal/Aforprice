package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.dao.ProductsDAO.HISTORY_QUERY_TYPE;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import thrift.servers.ProductData;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.DateTimeUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class ProductsDAOTest2 extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductsDAOTest2.class);
	private ProductsDAO dao = new ProductsDAO();
	private static final double EPSILON = 0.000001;
	private static final long TIME_DIFF = 1000000L;

	@Before
	public void setUp() throws Exception {
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
	}

	@After
	public void tearDown() throws Exception {
		dao.regTestRemoveProducts(Retailer.TESTRETAILER.getId());
	}

	private int getProductId(long uniqId){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME LIKE '%" + uniqId + "%'");
			rs.next();
			ProductSummary p = dao.getProductSummary(rs);
			int prodId = p.getId();
			return prodId;
		} catch(Exception e){
			logger.error(e.getStackTrace());
			logger.error("Error in getProductId: " + e.getMessage());
		} finally{
			DataAccessObject.closeRS(rs);
		}
		return -1;
	}
	
	private Product getProductFromProductSummary(ProductSummary product){
		Product prod = new Product();
		prod.setProductId(product.getId());
		prod.setName(product.getName());
		prod.setModelNo(product.getModel());
		prod.setImageUrl(product.getImageUrl());
		prod.setUrl(product.getUrl());
		PriceHistory priceHist = new PriceHistory();
		priceHist.priceTicks = new ArrayList<Tick>();
		prod.setPriceHistory(priceHist);
		List<Tick> sellRankHist = new ArrayList<Tick>();
		List<Review> reviewHist = new ArrayList<Review>();
		prod.setSellRankHistory(sellRankHist);
		prod.setReviewHistory(reviewHist);		
		return prod;
	}

	@Test
	public void test_makeProduct() {
		logger.info("======= TESTING getPriceHistoryForProducts =======");
		logger.info("======= TESTING getReviewHistoryForProducts =======");
		logger.info("======= TESTING getSellRankHistoryForProducts =======");
		HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
		List<ProductSummary> products = new ArrayList<>();
		long uniqId = System.currentTimeMillis();
		try {
			logger.info("======= INSERTING FIRST PRODUCT INTO THE DATABASE =======");
			Date t0 = new Date();
			ProductSummary prod_t0 = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "product"+uniqId, Retailer.TESTRETAILER.getLink()+uniqId, "http://image.jpg", "model1").
					categoryName("mycategory").categoryId(1).price(10.0).reviewRating(1).numReviews(100).salesRank(100).downloadTime(t0).build();
			products.add(prod_t0);
			dao.insertUpdateProductSummary(products, existingProds);
			
			assertProductSummary("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t0.getName() + "'", prod_t0);
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t0).getDownloadTime(), existingProds.get(prod_t0).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			int prodId_t0 = getProductId(uniqId);//get productid of insert product
			ProductData.getInstance().addProduct(Retailer.TESTRETAILER, getProductFromProductSummary(prod_t0));
			dao.getDataForProductsByRetailer(Retailer.TESTRETAILER.getId(), HISTORY_QUERY_TYPE.PRICE);
			dao.getDataForProductsByRetailer(Retailer.TESTRETAILER.getId(), HISTORY_QUERY_TYPE.REVIEW);
			
			Product prod_t0Expected = new Product(getProductFromProductSummary(prod_t0));
			prod_t0Expected.getPriceHistory().getPriceTicks().add(new Tick(prod_t0.getDownloadTime().getTime(), prod_t0.getPrice()));
			prod_t0Expected.getReviewHistory().add(new Review(prod_t0.getDownloadTime().getTime(), prod_t0.getReviewRating(), prod_t0.getNumReviews()));
			
			assertProduct(ProductData.getInstance().getProduct(prodId_t0), prod_t0Expected);
			
			logger.info("======= INSERTING NEW PRICE FOR EXISTING PRODUCT =======");
			Date t1 = new Date(t0.getTime() + TIME_DIFF);
			ProductSummary prod_t1= new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "product"+uniqId, Retailer.TESTRETAILER.getLink()+uniqId, "http://image.jpg", "model1").
					categoryName("mycategory").categoryId(1).price(13.0).reviewRating(3).numReviews(100).salesRank(20).downloadTime(t1).build();
			products.clear();
			products.add(prod_t1);
			dao.insertUpdateProductSummary(products, existingProds);
			
			assertProductSummary("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1.getName() + "'", prod_t1);
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t1).getDownloadTime(), existingProds.get(prod_t1).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
		
			ProductData.getInstance().addProduct(Retailer.TESTRETAILER, getProductFromProductSummary(prod_t1));
			dao.resetGotDataFromTablesUnitTest();
			dao.getDataForProductsByRetailer(Retailer.TESTRETAILER.getId(), HISTORY_QUERY_TYPE.PRICE);
			dao.getDataForProductsByRetailer(Retailer.TESTRETAILER.getId(), HISTORY_QUERY_TYPE.REVIEW);
			
			prod_t0Expected.getPriceHistory().getPriceTicks().add(new Tick(prod_t1.getDownloadTime().getTime(), prod_t1.getPrice()));
			prod_t0Expected.getReviewHistory().add(new Review(prod_t1.getDownloadTime().getTime(), prod_t1.getReviewRating(), prod_t1.getNumReviews()));
			assertProduct(ProductData.getInstance().getProduct(prodId_t0), prod_t0Expected);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private void assertList(List<Tick> actual, List<Tick> expected){
		_assertEquals(expected.size(), actual.size());
		for(Tick actualTick : actual){
			boolean found = false;
			for(Tick expectedTick : expected){
				//TODO asserting within 1 second
				if(Math.abs(actualTick.getTime() - expectedTick.getTime()) <= 1000){
					found = true;
					assertTrue(Math.abs(actualTick.getValue()-expectedTick.getValue())<=EPSILON);
				}
			}
			assertTrue(found);
		}
	}
	
	private void assertReviewList(List<Review> actual, List<Review> expected){
		assertTrue(actual.size() == expected.size());
		for(Review actualReview : actual){
			boolean found = false;
			for(Review expectedReview : expected){
				//TODO asserting within 1 second
				if(Math.abs(actualReview.getTime() - expectedReview.getTime()) <= 1000){
					found = true;
					assertTrue(Math.abs(actualReview.getReviewRating()-expectedReview.getReviewRating())<=EPSILON);
				}
			}
			assertTrue(found);
		}
	}
	
	private void assertProduct(Product actual, Product expected){
		assertTrue(actual.getName().equals(expected.getName()));
		assertTrue(actual.getModelNo().equals(expected.getModelNo()));
		assertTrue(actual.getImageUrl().equals(expected.getImageUrl()));
		assertTrue(actual.getUrl().equals(expected.getUrl()));
		assertList(actual.getPriceHistory().getPriceTicks(), expected.getPriceHistory().getPriceTicks());
		assertReviewList(actual.getReviewHistory(), expected.getReviewHistory());
		//assertList(actual.getSellRankHistory(), expected.getSellRankHistory());
	}
	
	private void assertProductSummary(String qryActual, ProductSummary expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			ProductSummary ps = dao.getProductSummary(rs);
			assertTrue(ps.getRetailerId().equals(expected.getRetailerId()));
			assertTrue(ps.getName().equals(expected.getName()));
			assertTrue(ps.getUrl().equals(expected.getUrl()));
			assertTrue(ps.getImageUrl().equals(expected.getImageUrl()));
			assertTrue(ps.getModel().equals(expected.getModel()));
			assertTrue(Math.abs(DateTimeUtils.diff(ps.getDownloadTime(), expected.getDownloadTime(), TimeUnit.SECONDS)) <= 1);
			assertTrue(ps.getPrice() == expected.getPrice());
			assertTrue(ps.getSalesRank() == expected.getSalesRank());
			assertTrue(ps.getReviewRating() == expected.getReviewRating());
			assertTrue(ps.getNumReviews() == expected.getNumReviews());
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

package db.dao;

import static entities.Retailer.TESTRETAILER;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.DateTimeUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductPricesHistory;
import entities.ProductReviewsHistory;
import entities.ProductSellRanksHistory;
import entities.ProductSummary;
import entities.Retailer;

public class ProductsCleanupDAOTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductsCleanupDAOTest.class);
	ProductsCleanupDAO cleanupDAO = new ProductsCleanupDAO();
	ProductsDAO productDAO = new ProductsDAO();
	HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
	List<ProductSummary> products = new ArrayList<>();
	private static long productIdCounter = System.currentTimeMillis();
	
	@Before
	public void setUp() throws Exception {
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		productDAO.regTestRemoveProducts(TESTRETAILER.getId());
	}

	//@Test - getting this error com.mysql.jdbc.MysqlDataTruncation: Data truncation: Truncated incorrect DOUBLE value: 'Y'
	public final void testMarkStaleProductsInactive() {
		logger.info("====== testMarkStaleProductsInactive======================================================");
		logger.info("add two products, one stale and one active, confirm that stale product is removed and active is intact");
		String productName = "TestActiveProduct-" + ++productIdCounter;
		Calendar downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 2); //downloaded 2 days ago
		ProductSummary activeProduct= new ProductSummaryBuilder(TESTRETAILER.getId(), productName, "http://www.testretailer.com/"+productIdCounter, "www.image1.com", "model1").
				categoryId(10000).price(1.0).downloadTime(downloadTime.getTime()).build();
		products.add(activeProduct);
		
		//anything older than month is marked inactive in test
		productName = "TestStaleProduct-" + ++productIdCounter;
		downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 35); //downloaded 35 days ago
		ProductSummary staleProduct= new ProductSummaryBuilder(TESTRETAILER.getId(), productName, "http://www.testretailer.com/"+productIdCounter, "www.image1.com", "model1").
				categoryId(10000).price(1.0).downloadTime(downloadTime.getTime()).build();
		products.add(staleProduct);
		try {
			productDAO.insertUpdateProductSummary(products, existingProds);
		
		cleanupDAO.markStaleProductsInactive();
		
		ProductSummary dbActiveProduct = productDAO.getProductSummaryByProductId(activeProduct.getId());
		_assertTrue(dbActiveProduct.isActive());
		ProductSummary dbStaleProduct = productDAO.getProductSummaryByProductId(staleProduct.getId());
		_assertTrue(!dbStaleProduct.isActive());
		
		logger.info("====== testMarkStaleProductsInactive ends======================================================");
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	//@Test pending change for retailer based table
	public final void testMoveVeryStaleProductsToStaleTables() {
		logger.info("====== testMoveVeryStaleProductsToStaleTables==========================================================");
		logger.info("add two products, one stale inactive, one stale active and one active, confirm that only stale inactive product is removed and active is intact");
		String productName = "TestActiveProduct-" + ++productIdCounter;
		Calendar downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 2); //downloaded 2 days ago
		ProductSummary activeProduct= new ProductSummaryBuilder(TESTRETAILER.getId(), productName, "http://www.testretailer.com/"+productIdCounter, "www.image1.com", "model1").
				categoryId(10000).price(1.2).downloadTime(downloadTime.getTime()).build();
		products.add(activeProduct);
		
		//anything older than 12 months is removed in test
		productName = "TestStaleInactiveProduct-" + ++productIdCounter;
		downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 390); //downloaded 190 days ago
		ProductSummary staleInactiveProduct= new ProductSummaryBuilder(TESTRETAILER.getId(), productName, "http://www.testretailer.com/"+productIdCounter, "www.image1.com", "model1").
				categoryId(10000).price(1.3).downloadTime(downloadTime.getTime()).active(false).build();
		products.add(staleInactiveProduct);
		
		//if product is still marked active even though stale, then it won't be removed
		productName = "TestStaleActiveProduct-" + ++productIdCounter;
		downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 390); //downloaded 190 days ago
		ProductSummary staleActiveProduct= new ProductSummaryBuilder(TESTRETAILER.getId(), productName, "http://www.testretailer.com/"+productIdCounter, "www.image1.com", "model1").
				categoryId(10000).price(1.4).downloadTime(downloadTime.getTime()).active(true).build();

		products.add(staleActiveProduct);
		try {
			productDAO.insertUpdateProductSummary(products, existingProds);
			cleanupDAO.moveVeryStaleProductsToStaleTables();
			ProductSummary dbActiveProduct = productDAO.getProductSummaryByProductId(activeProduct.getId());
			_assertTrue(dbActiveProduct.isActive());

			//confirm its removed from all tables
			ProductSummary dbStaleInactiveProduct = productDAO.getProductSummaryByProductId(staleInactiveProduct.getId());
			_assertTrue(dbStaleInactiveProduct==null);
			/*
			ProductSellRanksHistoryDAO rankDao = new ProductSellRanksHistoryDAO();
			List<ProductSellRanksHistory> rankHistory = rankDao.getHistory(staleInactiveProduct.getId());
			_assertTrue(rankHistory.size()==0);
			*/
			//TODO: this asssertion needs to be changed for multiple history tables
			ProductReviewsHistoryDAO reviewDao = new ProductReviewsHistoryDAO();
			List<ProductReviewsHistory> reviewHistory = reviewDao.getHistory(staleInactiveProduct.getId(), Retailer.WALMART.getId());
			_assertTrue(reviewHistory.size()==0);
			ProductPricesHistoryDAO priceHistoryDao = new ProductPricesHistoryDAO();
			List<ProductPricesHistory> priceHistory = priceHistoryDao.getHistory(staleInactiveProduct.getId(), Retailer.WALMART.getId());
			_assertTrue(priceHistory.size()==0);
			
			//confirm stale but active product is not removed.
			ProductSummary dbStaleActiveProduct = productDAO.getProductSummaryByProductId(staleActiveProduct.getId());
			_assertTrue(dbStaleActiveProduct.isActive());
			/*rankHistory = rankDao.getHistory(staleActiveProduct.getId());
			_assertTrue(rankHistory.size()==1);*/
			logger.info("====== testMoveVeryStaleProductsToStaleTables	ENDS====================================================");
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}

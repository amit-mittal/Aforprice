/**
 * 
 */
package products;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import products.ProductTest.ProductBuilder;
import thrift.genereated.retailer.Review;
import uploader.util.CategoryType;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.ProductSummaryBuilder;
import db.dao.CategoryDAO;
import db.dao.ProductCategoryDAO;
import db.dao.ProductPricesHistoryDAO;
import db.dao.ProductReviewsHistoryDAO;
import db.dao.ProductsDAO;
import db.dao.ProductsDownloadDAO;
import db.dao.ReconilationDAO;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.ProductPricesHistory;
import entities.ProductReviewsHistory;
import entities.ProductSummary;

/**
 * @author Ashish
 *
 */
@SuppressWarnings("deprecation")
public class ProductsMigrationTest extends AbstractTest{
	private static Logger logger;	
	private ProductsDAO prodDao;
	private ProductPricesHistoryDAO priceDao;
	private ProductReviewsHistoryDAO reviewDao;
	private static long currentTime = System.currentTimeMillis();
	private static long productIdCounter = System.currentTimeMillis();
	private CategoryDAO categoryDao;
	static{
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
	}
	
	private enum TEST_RETAILER{
		RETAILER1("testretailer", "http://www.testretailer.com/ip/test-product/"),
		RETAILER2("testretailer2", "http://www.testretailer2.com/ip/test-product/");

		private final String name;
		private final String baseUrl;
		TEST_RETAILER(String name, String baseUrl){
			this.name = name;
			this.baseUrl = baseUrl;
		}
	}
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("ENVIRONMENT", "QA");
		if(ConfigParms.getInstance().isAshishPC())
			System.setProperty("log4j.configuration", "file:D:\\Ashish\\Eclipse\\workspace\\crawler\\src\\main\\config\\log4j\\log4j.xml");
		logger = Logger.getLogger(ProductsMigrationTest.class);
		prodDao = new ProductsDAO();
		categoryDao = new CategoryDAO();
		priceDao = new ProductPricesHistoryDAO();
		reviewDao = new ProductReviewsHistoryDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		prodDao.regTestRemoveProducts(TEST_RETAILER.RETAILER1.name);
		prodDao.regTestRemoveProducts(TEST_RETAILER.RETAILER2.name);
	}

	@Test
	public void testOneRetailerOneCategoryTwoProducts(){
		try {
			logger.info("==========================OneRetailerOneCategoryTwoProducts================================");
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			long downloadStartTime = currentTime;
			Category category1 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory1", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419431").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category1);
			String productName = "TestProduct1-" + productIdCounter++;
			ProductSummary testProduct1 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category1.getCategoryId(), category1.getName(),productName, 100.2, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct1image.com", "product 1 desc", "product 1 model", new Date(currentTime), new Date(currentTime));
			testProduct1.setReviewRating(4.5);testProduct1.setNumReviews(50);
			currentTime++;
			productName = "TestProduct2-" + productIdCounter++;
			ProductSummary testProduct2 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category1.getCategoryId(), category1.getName(),productName, 101.3, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct2image.com", "product 2 desc", "product 2 model", new Date(currentTime), new Date(currentTime));
			testProduct2.setReviewRating(4.6);testProduct2.setNumReviews(51);
			
			ProductBuilder builder = new ProductBuilder(testProduct1.getRetailerId(), testProduct1.getCategoryId(), testProduct1.getName(), testProduct1.getUrl(), 
					testProduct1.getImageUrl(), testProduct1.getDesc(), testProduct1.getModel(), testProduct1.getCreationTime(), testProduct1.getDownloadTime());
			builder.addPrice(testProduct1.getDownloadTime(), testProduct1.getPrice());
			builder.addReview(testProduct1.getDownloadTime(), new Review(testProduct1.getDownloadTime().getTime(), testProduct1.getReviewRating(), testProduct1.getNumReviews()));
			ProductTest expectedProduct1 = builder.build();
			builder = new ProductBuilder(testProduct2.getRetailerId(), testProduct2.getCategoryId(), testProduct2.getName(), testProduct2.getUrl(), 
					testProduct2.getImageUrl(), testProduct2.getDesc(), testProduct2.getModel(), testProduct2.getCreationTime(), testProduct2.getDownloadTime());
			builder.addPrice(testProduct2.getDownloadTime(), testProduct2.getPrice());
			builder.addReview(testProduct2.getDownloadTime(), new Review(testProduct2.getDownloadTime().getTime(), testProduct2.getReviewRating(), testProduct2.getNumReviews()));
			ProductTest expectedProduct2 = builder.build();
			
			//*******RECORD PRODUCTS
			products.add(testProduct1);
			products.add(testProduct2);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			//*******VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct1.getRetailerId(), testProduct1.getCategoryId(), products);			
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct1.getDownloadId(), true, "");
			prodDao.updateProductRecStatus(testProduct2.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct1);
			verifyProductMigrated(expectedProduct2);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void testTwoRetailerTwoCategoryTwoProducts_SameProductDifferentPrices(){
		try {
			logger.info("==========================TwoRetailerTwoCategoryTwoProducts_SameProductDifferentPrices================================");
			currentTime = currentTime+1000;
			long downloadStartTime = currentTime;
			Date downloadTime = new Date(currentTime);
			Category category3 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory3", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419433").catType(CategoryType.TERMINAL).build();
			Category category4 = new CategoryBuilder(TEST_RETAILER.RETAILER2.name, 1, "testparent", "TestCategory4", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419434").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category3);
			categoryDao.recordCategory(category4);
			String productName = "TestProduct3-" + productIdCounter++;
			ProductSummary testProduct3 = new ProductSummaryBuilder(TEST_RETAILER.RETAILER1.name, productName, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter, 
						"http://www.testproduct3image.com", "product 3 model").categoryId(category3.getCategoryId()).categoryName(category3.getName()).
						price(100.2).desc("product 3 desc").creationTime(downloadTime).downloadTime(downloadTime).reviewRating(3.5).numReviews(20).build();
			downloadTime = new Date(++currentTime);
			productName = "TestProduct4-" + productIdCounter++;
			ProductSummary testProduct4 = new ProductSummaryBuilder(TEST_RETAILER.RETAILER2.name, productName, TEST_RETAILER.RETAILER2.baseUrl + productIdCounter, 
					"http://www.testproduct4image.com", "product 4 model").categoryId(category4.getCategoryId()).categoryName(category4.getName()).
					price(101.3).desc("product 4 desc").creationTime(downloadTime).downloadTime(downloadTime).reviewRating(3.7).numReviews(15).build();

			ProductBuilder builder = new ProductBuilder(testProduct3.getRetailerId(), testProduct3.getCategoryId(), testProduct3.getName(), testProduct3.getUrl(), 
					testProduct3.getImageUrl(), testProduct3.getDesc(), testProduct3.getModel(), testProduct3.getCreationTime(), testProduct3.getDownloadTime());
			builder.addPrice(testProduct3.getDownloadTime(), testProduct3.getPrice());
			builder.addReview(testProduct3.getDownloadTime(), new Review(testProduct3.getDownloadTime().getTime(), testProduct3.getReviewRating(), testProduct3.getNumReviews()));
			ProductTest expectedProduct3 = builder.build();
			builder = new ProductBuilder(testProduct4.getRetailerId(), testProduct4.getCategoryId(), testProduct4.getName(), testProduct4.getUrl(), 
					testProduct4.getImageUrl(), testProduct4.getDesc(), testProduct4.getModel(), testProduct4.getCreationTime(), testProduct4.getDownloadTime());
			builder.addPrice(testProduct4.getDownloadTime(), testProduct4.getPrice());
			builder.addReview(testProduct4.getDownloadTime(), new Review(testProduct4.getDownloadTime().getTime(), testProduct4.getReviewRating(), testProduct4.getNumReviews()));
			ProductTest expectedProduct4 = builder.build();
			
			//*******RECORD PRODUCTS
			List<ProductSummary> products = new ArrayList<ProductSummary>();
			products.add(testProduct3);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			//*******VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct3.getRetailerId(), testProduct3.getCategoryId(), products);
			//second retailer
			products = new ArrayList<ProductSummary>();
			products.add(testProduct4);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct4.getRetailerId(), testProduct4.getCategoryId(), products);
			
			//after little bit, we got testproduct3 with different price
			currentTime = currentTime+1000;
			downloadTime = new Date(currentTime);
			downloadStartTime = currentTime;
			//todo: need to change last arg with download_time when anurag adds it. since right now its creation time which should be same as testProduct3
			ProductSummary testProduct3NewPrice = new ProductSummaryBuilder(testProduct3.getRetailerId(), testProduct3.getName(), testProduct3.getUrl(), 
					testProduct3.getImageUrl(), testProduct3.getModel()).categoryId(testProduct3.getCategoryId()).categoryName(testProduct3.getCategoryName()).
					price(100.3).desc(testProduct3.getDesc()).creationTime(testProduct3.getCreationTime()).downloadTime(downloadTime).reviewRating(3.7).numReviews(22).build();

			builder = new ProductBuilder(testProduct3NewPrice.getRetailerId(), testProduct3NewPrice.getCategoryId(), testProduct3NewPrice.getName(), testProduct3NewPrice.getUrl(), 
					testProduct3NewPrice.getImageUrl(), testProduct3NewPrice.getDesc(), testProduct3NewPrice.getModel(), testProduct3NewPrice.getCreationTime(), testProduct3NewPrice.getDownloadTime());
			builder.addPrice(testProduct3.getDownloadTime(), testProduct3.getPrice());
			builder.addPrice(testProduct3NewPrice.getDownloadTime(), testProduct3NewPrice.getPrice());
			builder.addReview(testProduct3.getDownloadTime(), new Review(testProduct3.getDownloadTime().getTime(), testProduct3.getReviewRating(), testProduct3.getNumReviews()));
			builder.addReview(testProduct3NewPrice.getDownloadTime(), new Review(testProduct3NewPrice.getDownloadTime().getTime(), testProduct3NewPrice.getReviewRating(), testProduct3NewPrice.getNumReviews()));			
			ProductTest expectedProduct3NewPrice = builder.build();

			expectedProduct3.addPrice(testProduct3NewPrice.getDownloadTime(), testProduct3NewPrice.getPrice());//since its price history will have the new price
			expectedProduct3.addReview(testProduct3NewPrice.getDownloadTime(), new Review(testProduct3NewPrice.getDownloadTime().getTime(), testProduct3NewPrice.getReviewRating(), testProduct3NewPrice.getNumReviews()));
			//*******RECORD PRODUCTS - updated price
			products.clear();
			products.add(testProduct3NewPrice);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			//*******VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct3NewPrice.getRetailerId(), testProduct3NewPrice.getCategoryId(), products);

			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct3.getDownloadId(), true, "");
			prodDao.updateProductRecStatus(testProduct4.getDownloadId(), true, "");
			prodDao.updateProductRecStatus(testProduct3NewPrice.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct3);
			verifyProductMigrated(expectedProduct4);
			verifyProductMigrated(expectedProduct3NewPrice);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	/*
	 * one product is downloaded under two different categories, same price
	 * price history should have timestamp of first download
	 * product_category has two entries
	 */
	
	@Test
	public void testOneProductDownloadedUnderTwoCategory(){
		try {
			logger.info("==========================testOneProductDownloadedUnderTwoCategory================================");
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			currentTime=currentTime+1000;
			long downloadStartTime = currentTime;
			Category category51 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory51", 
					TEST_RETAILER.RETAILER1.baseUrl+"164194351").catType(CategoryType.TERMINAL).build();
			Category category52 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory52", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419452").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category51);
			categoryDao.recordCategory(category52);
			String productName = "TestProduct5-" + productIdCounter++;
			ProductSummary testProduct5 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category51.getCategoryId(), category51.getName(),productName, 100.2, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct5image.com", "product 5 desc", "product 5 model", new Date(currentTime), new Date(currentTime));
			testProduct5.setReviewRating(4.7);testProduct5.setNumReviews(52);
			currentTime=currentTime+1000;
			ProductSummary testProduct5_DiffCategory = new ProductSummary(testProduct5.getRetailerId(), category52.getCategoryId(), category52.getName(), testProduct5.getName(), 100.2, testProduct5.getUrl(),
					testProduct5.getImageUrl(), testProduct5.getDesc(), testProduct5.getModel(), testProduct5.getCreationTime(), new Date(currentTime));
			testProduct5_DiffCategory.setReviewRating(4.7);testProduct5_DiffCategory.setNumReviews(52);//same review, only category is different
			
			ProductBuilder builder = new ProductBuilder(testProduct5.getRetailerId(), testProduct5.getCategoryId(), testProduct5.getName(), testProduct5.getUrl(), 
					testProduct5.getImageUrl(), testProduct5.getDesc(), testProduct5.getModel(), testProduct5.getCreationTime(), testProduct5.getDownloadTime());
			builder.addCategory(testProduct5_DiffCategory.getCategoryId());
			builder.addPrice(testProduct5.getDownloadTime(), testProduct5.getPrice());
			builder.addReview(testProduct5.getDownloadTime(), new Review(testProduct5.getDownloadTime().getTime(), testProduct5.getReviewRating(), testProduct5.getNumReviews()));
			ProductTest expectedProduct5 = builder.build();
			builder = new ProductBuilder(testProduct5_DiffCategory.getRetailerId(), testProduct5_DiffCategory.getCategoryId(), testProduct5_DiffCategory.getName(), testProduct5_DiffCategory.getUrl(), 
					testProduct5_DiffCategory.getImageUrl(), testProduct5_DiffCategory.getDesc(), testProduct5_DiffCategory.getModel(), testProduct5_DiffCategory.getCreationTime(), testProduct5_DiffCategory.getDownloadTime());
			builder.addPrice(testProduct5.getDownloadTime(), testProduct5_DiffCategory.getPrice());//price will have timestamp of above download
			builder.addCategory(testProduct5.getCategoryId());
			builder.addReview(testProduct5.getDownloadTime(), new Review(testProduct5.getDownloadTime().getTime(), testProduct5.getReviewRating(), testProduct5.getNumReviews()));
			ProductTest expectedProduct5_DiffCategory = builder.build();
			
			//*******RECORD PRODUCTS
			//*******VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			products.add(testProduct5);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct5.getRetailerId(), testProduct5.getCategoryId(), products);
			products.clear();
			products.add(testProduct5_DiffCategory);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct5_DiffCategory.getRetailerId(), testProduct5_DiffCategory.getCategoryId(), products);			
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct5.getDownloadId(), true, "");
			prodDao.updateProductRecStatus(testProduct5_DiffCategory.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct5);
			verifyProductMigrated(expectedProduct5_DiffCategory);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void testProductNotReconciledNotMigrated(){
		try {
			logger.info("==========================testProductNotReconciledNotMigrated================================");
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			currentTime=currentTime+1000;
			long downloadStartTime = currentTime;
			Date startTime = new Date(currentTime);
			Category category6 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory6", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419436").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category6);
			String productName = "TestProduct6-" + productIdCounter++;
			ProductSummary testProduct6 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category6.getCategoryId(), category6.getName(),productName, 100.6, "http://www.testproduct6.com",
					"http://www.testproduct6image.com", "product 6 desc", "product 6 model", startTime);
			ProductBuilder builder = new ProductBuilder(testProduct6.getRetailerId(), testProduct6.getCategoryId(), testProduct6.getName(), testProduct6.getUrl(), 
					testProduct6.getImageUrl(), testProduct6.getDesc(), testProduct6.getModel(), testProduct6.getCreationTime(), testProduct6.getDownloadTime());
			builder.addCategory(6000062);
			builder.addPrice(testProduct6.getDownloadTime(), testProduct6.getPrice());
			ProductTest expectedProduct6 = builder.build();
			
			//*******RECORD PRODUCTS
			//*******VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			products.add(testProduct6);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct6.getRetailerId(), testProduct6.getCategoryId(), products);
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct6.getDownloadId(), false, ReconilationDAO.TYPE.NOPRICENEW.name());
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductNotMigrated(expectedProduct6);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/*
	 * same product has two entries in product_download table but they are not in order of download_time
	 * newer entry has older timestamp, it should migrated to product history table in correct time order
	 */
	@Test
	public void testProductPriceOutOfOrderInTime(){
		try {
			logger.info("==========================testProductPriceOutOfOrderInTime================================");
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			currentTime=currentTime+5000;
			long downloadStartTime = currentTime;
			Category category7 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory7", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419437").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category7);
			String productName = "TestProduct7-" + productIdCounter++;
			ProductSummary testProduct7_2 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category7.getCategoryId(), category7.getName(), productName, 100.82, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct7image.com", "product 7 desc", "product 7 model", new Date(currentTime), new Date(currentTime));
			testProduct7_2.setReviewRating(4.9);testProduct7_2.setNumReviews(54);
			ProductBuilder builder = new ProductBuilder(testProduct7_2.getRetailerId(), testProduct7_2.getCategoryId(), testProduct7_2.getName(), testProduct7_2.getUrl(), 
					testProduct7_2.getImageUrl(), testProduct7_2.getDesc(), testProduct7_2.getModel(), testProduct7_2.getCreationTime(), testProduct7_2.getDownloadTime());
			builder.addPrice(testProduct7_2.getDownloadTime(), testProduct7_2.getPrice());
			builder.addReview(testProduct7_2.getDownloadTime(), new Review(testProduct7_2.getDownloadTime().getTime(), testProduct7_2.getReviewRating(), testProduct7_2.getNumReviews()));
			ProductTest expectedProduct7_2 = builder.build();
			//*******RECORD PRODUCTS & VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			products.add(testProduct7_2);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct7_2.getRetailerId(), testProduct7_2.getCategoryId(), products);
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct7_2.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct7_2);
			products.clear();
			
			downloadStartTime=currentTime=currentTime-2000;
			ProductSummary testProduct7_1 = new ProductSummary(TEST_RETAILER.RETAILER1.name, testProduct7_2.getCategoryId(), testProduct7_2.getCategoryName(), productName, 100.71, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					testProduct7_2.getImageUrl(), testProduct7_2.getDesc(), testProduct7_2.getModel(), testProduct7_2.getCreationTime(), new Date(currentTime));
			testProduct7_1.setReviewRating(4.8);testProduct7_1.setNumReviews(53);
			builder = new ProductBuilder(testProduct7_1.getRetailerId(), testProduct7_1.getCategoryId(), testProduct7_1.getName(), testProduct7_1.getUrl(), 
					testProduct7_1.getImageUrl(), testProduct7_1.getDesc(), testProduct7_1.getModel(), testProduct7_2.getCreationTime(), testProduct7_2.getDownloadTime());//product_summary table will have first product entry's creation and latest download time
			builder.addPrice(testProduct7_1.getDownloadTime(), testProduct7_1.getPrice());
			builder.addPrice(testProduct7_2.getDownloadTime(), testProduct7_2.getPrice());
			builder.addReview(testProduct7_1.getDownloadTime(), new Review(testProduct7_1.getDownloadTime().getTime(), testProduct7_1.getReviewRating(), testProduct7_1.getNumReviews()));
			builder.addReview(testProduct7_2.getDownloadTime(), new Review(testProduct7_2.getDownloadTime().getTime(), testProduct7_2.getReviewRating(), testProduct7_2.getNumReviews()));
			ProductTest expectedProduct7_1 = builder.build();
			
			products.add(testProduct7_1);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct7_1.getRetailerId(), testProduct7_1.getCategoryId(), products);			

			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct7_1.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct7_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/*
	 * product's url and desc changes next day. price remains same. keep name and model same so product can match with previous entry
	 */
	@Test
	public void oneProductSamePriceOnTwoDays(){
		try {
			logger.info("==========================testProductPriceOutOfOrderInTime================================");
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			currentTime=currentTime+5000;
			long downloadStartTime = currentTime;
			String productName = "TestProduct8-" + productIdCounter++;
			CategoryDAO categoryDao = new CategoryDAO();
			Category category8 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory8", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419438").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category8);
			ProductSummary testProduct8_1 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category8.getCategoryId(), category8.getName(), productName, 100.8, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct8image.com", "product 8 desc", "product 8 model", new Date(currentTime), new Date(currentTime));
			testProduct8_1.setReviewRating(5.0);testProduct8_1.setNumReviews(55);
			ProductBuilder builder = new ProductBuilder(testProduct8_1.getRetailerId(), testProduct8_1.getCategoryId(), testProduct8_1.getName(), testProduct8_1.getUrl(), 
					testProduct8_1.getImageUrl(), testProduct8_1.getDesc(), testProduct8_1.getModel(), testProduct8_1.getCreationTime(), testProduct8_1.getDownloadTime());
			builder.addPrice(testProduct8_1.getDownloadTime(), testProduct8_1.getPrice());
			builder.addReview(testProduct8_1.getDownloadTime(), new Review(testProduct8_1.getDownloadTime().getTime(), testProduct8_1.getReviewRating(), testProduct8_1.getNumReviews()));
			ProductTest expectedProduct8_1 = builder.build();
			
			products.add(testProduct8_1);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct8_1.getRetailerId(), testProduct8_1.getCategoryId(), products);			
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct8_1.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct8_1);

			downloadStartTime=currentTime=currentTime+2000;
			ProductSummary testProduct8_2 = new ProductSummary(testProduct8_1.getRetailerId(), testProduct8_1.getCategoryId(), testProduct8_1.getCategoryName(), productName, 100.8, testProduct8_1.getUrl(),
					testProduct8_1.getImageUrl(), testProduct8_1.getDesc(), testProduct8_1.getModel(), testProduct8_1.getCreationTime(), new Date(currentTime));
			testProduct8_2.setReviewRating(testProduct8_1.getReviewRating());testProduct8_2.setNumReviews(testProduct8_1.getNumReviews());
			builder = new ProductBuilder(testProduct8_2.getRetailerId(), testProduct8_2.getCategoryId(), testProduct8_2.getName(), testProduct8_2.getUrl(), 
					testProduct8_2.getImageUrl(), testProduct8_2.getDesc(), testProduct8_2.getModel(), testProduct8_1.getCreationTime(), testProduct8_2.getDownloadTime());
			builder.addPrice(testProduct8_1.getDownloadTime(), testProduct8_2.getPrice());//price will have timestamp of above download
			builder.addReview(testProduct8_1.getDownloadTime(), new Review(testProduct8_1.getDownloadTime().getTime(), testProduct8_2.getReviewRating(), testProduct8_2.getNumReviews()));//review will have download time of first entry since it hasn't changed
			ProductTest expectedProduct8_2 = builder.build();
			//*******RECORD PRODUCTS & VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			products.clear();
			products.add(testProduct8_2);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct8_2.getRetailerId(), testProduct8_2.getCategoryId(), products);
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct8_2.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			//expectedProduct8_2.setCreationTime(expectedProduct8_1.getLastDownloadTime());
			verifyProductMigrated(expectedProduct8_2);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	/*
	 * if an exisiting inactive product becomes active then it should reuse exisiting productid
	 */
	@Test
	public void inActiveProductBecomesActive(){
		try {
			logger.info("==========================inActiveProductBecomesActive================================");
			/*
			 * insert a product
			 * inactive it
			 * download again and confirm it reactivated it
			 */
			List<ProductSummary> products = new ArrayList<ProductSummary>(2);
			currentTime=currentTime+5000;
			long downloadStartTime = currentTime;
			String productName = "TestProduct9-" + productIdCounter++;
			CategoryDAO categoryDao = new CategoryDAO();
			Category category9 = new CategoryBuilder(TEST_RETAILER.RETAILER1.name, 1, "testparent", "TestCategory9", 
					TEST_RETAILER.RETAILER1.baseUrl+"16419438").catType(CategoryType.TERMINAL).build();
			categoryDao.recordCategory(category9);
			ProductSummary testProduct9_1 = new ProductSummary(TEST_RETAILER.RETAILER1.name, category9.getCategoryId(), category9.getName(), productName, 200.5, TEST_RETAILER.RETAILER1.baseUrl + productIdCounter,
					"http://www.testproduct9image.com", "product 9 desc", "product 9 model", new Date(currentTime), new Date(currentTime));
			testProduct9_1.setReviewRating(5.0);testProduct9_1.setNumReviews(55);
			ProductBuilder builder = new ProductBuilder(testProduct9_1.getRetailerId(), testProduct9_1.getCategoryId(), testProduct9_1.getName(), testProduct9_1.getUrl(), 
					testProduct9_1.getImageUrl(), testProduct9_1.getDesc(), testProduct9_1.getModel(), testProduct9_1.getCreationTime(), testProduct9_1.getDownloadTime());
			builder.addPrice(testProduct9_1.getDownloadTime(), testProduct9_1.getPrice());
			builder.addReview(testProduct9_1.getDownloadTime(), new Review(testProduct9_1.getDownloadTime().getTime(), testProduct9_1.getReviewRating(), testProduct9_1.getNumReviews()));
			ProductTest expectedProduct9_1 = builder.build();			
			products.add(testProduct9_1);			
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct9_1.getRetailerId(), testProduct9_1.getCategoryId(), products);			
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct9_1.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct9_1);

			//now mark product inactive
			testProduct9_1.setActive(false);
			HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
			existingProds = prodDao.getProducts();
			ProductCategoryDAO prodCatDAO = new ProductCategoryDAO();
			prodCatDAO.populateProductCategories(existingProds);
			prodDao.insertUpdateProductSummary(products, existingProds);
			ProductSummary dbStaleProduct = prodDao.getProductSummaryByProductId(testProduct9_1.getId());
			_assertTrue(!dbStaleProduct.isActive());
		
			//make product active again with some price change
			downloadStartTime=currentTime=currentTime+2000;
			ProductSummary testProduct9_2 = new ProductSummary(testProduct9_1.getRetailerId(), testProduct9_1.getCategoryId(), testProduct9_1.getCategoryName(), productName, 210, testProduct9_1.getUrl(),
					testProduct9_1.getImageUrl(), testProduct9_1.getDesc(), testProduct9_1.getModel(), testProduct9_1.getCreationTime(), new Date(currentTime));
			testProduct9_2.setReviewRating(testProduct9_1.getReviewRating());testProduct9_2.setNumReviews(testProduct9_1.getNumReviews());
			builder = new ProductBuilder(testProduct9_2.getRetailerId(), testProduct9_2.getCategoryId(), testProduct9_2.getName(), testProduct9_2.getUrl(), 
					testProduct9_2.getImageUrl(), testProduct9_2.getDesc(), testProduct9_2.getModel(), testProduct9_1.getCreationTime(), testProduct9_2.getDownloadTime());
			builder.addPrice(testProduct9_1.getDownloadTime(), testProduct9_1.getPrice());
			builder.addPrice(testProduct9_2.getDownloadTime(), testProduct9_2.getPrice());
			builder.addReview(testProduct9_1.getDownloadTime(), new Review(testProduct9_1.getDownloadTime().getTime(), testProduct9_2.getReviewRating(), testProduct9_2.getNumReviews()));//review will have download time of first entry since it hasn't changed
			ProductTest expectedProduct9_2 = builder.build();
			//*******RECORD PRODUCTS & VERIFY PRODUCT_DOWNLOAD TABLE AND GET PRODUCT_DOWNLOAD ID
			products.clear();
			products.add(testProduct9_2);
			_assertEquals(0, ProductsDownloadDAO.get().recordProducts(products).size());
			verifyProductDownloadTable(downloadStartTime, currentTime, testProduct9_2.getRetailerId(), testProduct9_2.getCategoryId(), products);
			//*******MARK RECONCILED
			prodDao.updateProductRecStatus(testProduct9_2.getDownloadId(), true, "");
			//*******MIGRATE
			ProductsMigration.start();
			//*******VERIFY MIGRATION
			verifyProductMigrated(expectedProduct9_2);
			ProductSummary dbActiveProduct = prodDao.getProductSummaryByProductId(testProduct9_1.getId());
			_assertTrue(dbActiveProduct.isActive());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	/**
	 * @param startTime
	 * @param retailerId
	 * @param categoryId
	 * @param testProduct1
	 * @param endTime
	 * @param testProduct2
	 * @param expectedDownloadedProductCount
	 * @throws SQLException
	 */
	private void verifyProductDownloadTable(long startTime, long endTime, String retailerId, int categoryId, List<ProductSummary> products)
			throws SQLException {
		Map<String, ProductSummary> expectedDbProducts = new HashMap<String, ProductSummary>();
		for(ProductSummary product : products)
			expectedDbProducts.put(product.getName().toLowerCase(),product);
		Map<String, Set<ProductSummary>> dbProducts = prodDao.getDownloadedProductsTimeRange(new Date(startTime), new Date(endTime), retailerId, categoryId, ConfigParms.getDownloadMode());
		_assertEquals(expectedDbProducts.size(), dbProducts.size());
		for(Map.Entry<String, Set<ProductSummary>> entry: dbProducts.entrySet()){
			String dbProductName = entry.getKey();
			Set<ProductSummary> dbProductSet = entry.getValue();
			_assertEquals(1, dbProductSet.size());
			ProductSummary dbProduct = (ProductSummary)(dbProductSet.toArray()[0]);
			ProductSummary exectedProduct = expectedDbProducts.get(dbProductName);
			_assertNotNull(exectedProduct, dbProductName + " found in product_download, but not in expected list");
			exectedProduct.setDownloadId(dbProduct.getDownloadId());
		}
		//todo: check that product_download table should say processed='Y'
	}

	/**
	 * @param testProduct
	 * @throws SQLException
	 */
	private void verifyProductMigrated(ProductTest expectedProduct)
			throws SQLException {
		//-find out all the tables that are updated, category-product map, product, product price history
		//product is marked processed in the product_download table
		//product_summary		
		Set<ProductSummary> migratedProd1FromDbSet = prodDao.getProductSummary(expectedProduct.getName(), expectedProduct.getLastDownloadTime());
		_assertEquals(1, migratedProd1FromDbSet.size());
		ProductSummary migratedProd1FromDb = (ProductSummary) migratedProd1FromDbSet.toArray()[0];
		_assertEquals(expectedProduct.getName(), migratedProd1FromDb.getName());
		_assertEquals(null, migratedProd1FromDb.getDesc());
		//verify price history
		_assertEquals(expectedProduct.getLatestPrice(), migratedProd1FromDb.getPrice());
		TreeMap<Date, Double> priceHistory = getPriceHistory(migratedProd1FromDb.getId(), migratedProd1FromDb.getRetailerId());
		_assertEquals(expectedProduct.getPriceHistory().size(), priceHistory.size());
		Iterator<Map.Entry<Date, Double>> expectedPriceHistoryIterator = expectedProduct.getPriceHistory().entrySet().iterator();
		Iterator<Map.Entry<Date, Double>> actualPriceHistoryIterator = priceHistory.entrySet().iterator();
		while(expectedPriceHistoryIterator.hasNext() && actualPriceHistoryIterator.hasNext()){
			Map.Entry<Date, Double> expected = expectedPriceHistoryIterator.next();
			Map.Entry<Date, Double> actual =actualPriceHistoryIterator.next();
			_assertTrue(Math.abs(expected.getKey().getTime() - actual.getKey().getTime())<1000,
					"expectedCreationTime="+expected.getKey()+",actualCreationTime="+actual.getKey());
			_assertEquals(expected.getValue(), actual.getValue());
		}
		//verify review history
		_assertEquals(expectedProduct.getLatestRating(), migratedProd1FromDb.getReviewRating());
		_assertEquals(expectedProduct.getLatestNumReviews(), migratedProd1FromDb.getNumReviews());
		TreeMap<Date, Review> reviewHistory = getReviewHistory(migratedProd1FromDb.getId(), migratedProd1FromDb.getRetailerId());
		_assertEquals(expectedProduct.getReviewHistory().size(), reviewHistory.size());
		Iterator<Map.Entry<Date, Review>> expectedReviewHistoryIterator = expectedProduct.getReviewHistory().entrySet().iterator();
		Iterator<Map.Entry<Date, Review>> actualReviewHistoryIterator = reviewHistory.entrySet().iterator();
		while(expectedReviewHistoryIterator.hasNext() && actualReviewHistoryIterator.hasNext()){
			Map.Entry<Date, Review> expected = expectedReviewHistoryIterator.next();
			Map.Entry<Date, Review> actual =actualReviewHistoryIterator.next();
			_assertTrue(Math.abs(expected.getKey().getTime() - actual.getKey().getTime())<1000,
					"expectedCreationTime="+expected.getKey()+",actualCreationTime="+actual.getKey());
			_assertEquals(expected.getValue().getReviewRating(), actual.getValue().getReviewRating());
			_assertEquals(expected.getValue().getNumReviews(), actual.getValue().getNumReviews());
		}
		
		assertEquals(expectedProduct.getUrl(), migratedProd1FromDb.getUrl());
		assertEquals(expectedProduct.getCategoryIds().size(), migratedProd1FromDb.getCategories().size());
		HashSet<Integer> catIdsInDb = new HashSet<Integer>();
		for(Integer categoryId : migratedProd1FromDb.getCategories()){
			_assertContains(expectedProduct.getCategoryIds(), categoryId);
			_assertTrue(expectedProduct.getCategoryIds().contains(categoryId));
			catIdsInDb.add(categoryId);			
		}
		for(Integer categoryId : expectedProduct.getCategoryIds())
			_assertTrue(catIdsInDb.contains(categoryId));
		_assertEquals(expectedProduct.getRetailerId(), migratedProd1FromDb.getRetailerId());
		_assertEquals(expectedProduct.getImageUrl(), migratedProd1FromDb.getImageUrl());
		_assertEquals(expectedProduct.getModel(), migratedProd1FromDb.getModel());
		_assertTrue(Math.abs(expectedProduct.getCreationTime().getTime() -  migratedProd1FromDb.getCreationTime().getTime())<1000, 
				"expectedCreationTime="+expectedProduct.getCreationTime()+",actualCreationTime="+migratedProd1FromDb.getCreationTime());
		_assertTrue(Math.abs(expectedProduct.getLastDownloadTime().getTime() -  migratedProd1FromDb.getDownloadTime().getTime())<1000, 
				"expectedLastDownloadTime="+expectedProduct.getLastDownloadTime()+",actualLastDownloadTime="+migratedProd1FromDb.getDownloadTime());
		_assertEquals(expectedProduct.isActive(), migratedProd1FromDb.isActive());
	}
	
	/**
	 * @param prodId
	 * @param retailer
	 * @throws SQLException
	 */
	private TreeMap<Date, Double> getPriceHistory(int prodId, String retailer) throws SQLException {
		List<ProductPricesHistory> tempHistory = priceDao.getHistory(prodId, retailer);
		TreeMap<Date, Double> priceHistory = new TreeMap<Date, Double>();	
		for(ProductPricesHistory history : tempHistory)
			priceHistory.put(history.getTime(), history.getPrice());
		return priceHistory;
	}

	/**
	 * @param prodId
	 * @param retailer
	 * @throws SQLException
	 */
	private TreeMap<Date, Review> getReviewHistory(int prodId, String retailer) throws SQLException {
		List<ProductReviewsHistory> tempHistory = reviewDao.getHistory(prodId, retailer);
		TreeMap<Date, Review> priceHistory = new TreeMap<Date, Review>();	
		for(ProductReviewsHistory history : tempHistory)
			priceHistory.put(history.getTime(), new Review(history.getTime().getTime(), history.getRating(), history.getNumReviews()));
		return priceHistory;
	}
	
	/**
	 * @param testProduct
	 * @throws SQLException
	 */
	private void verifyProductNotMigrated(ProductTest expectedProduct)
			throws SQLException {
		Set<ProductSummary> migratedProd1FromDbSet = prodDao.getProductSummary(expectedProduct.getName(), expectedProduct.getCreationTime());
		_assertEquals(0, migratedProd1FromDbSet.size());
	}

}

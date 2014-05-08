package thrift.servers;

import static entities.Retailer.TESTRETAILER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.ProductSummaryBuilder;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import entities.Retailer;

/**
 * @author Amit
 *
 */

public class ProductCacheImplTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductCacheImplTest.class);
	private static long currentTime = System.currentTimeMillis();
	private static List<Retailer> myRetailers;
	ProductCacheImpl cache;
	ProductsDAO dao = new ProductsDAO();
	int expNoOfProductsInRetailer, expNoOfProductsInCategory, expNoOfCategoriesForProduct, expPriceTicksSize;
	private static final long TIME_DIFF = 60*60*1000L;
	
	//TODO Can I make all the maps and products, etc. in the setup, this way I will be able to use them in both methods
	//TODO Currently testing after adding each product in testCategoryIdProductMap() method then have to test it finally only
	public void setUp(){
		try {
			ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
			dao.regTestRemoveProducts(TESTRETAILER.getId());
			myRetailers = new ArrayList<Retailer>();
			myRetailers.add(TESTRETAILER);
			setupMock();
			ProductData.getInstance().resetUnitTest();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setupMock(){
	}
	
	@Test
	public void testCategoryIdProductMap(){
		//TODO Here have not assert anything about the retailer as init method has not been called
		cache = new ProductCacheImpl(myRetailers);
		/*
		 * Making products by setting PriceHistory, etc.
		 * And then adding them to all the categoryProduct Maps
		 * calling Process methods to sort the tick lists
		 * calling sortMap method to sort all the category maps
		 */
		logger.info("======= CHECKING IF LISTS OF PRODUCT ARE GETTING SORTED =======");
		logger.info("======= MAKING PRODUCT1 =======");
//		String productName = "TestProduct3-" + productIdCounter++;
//		currentTime = currentTime+1000;
//		long downloadStartTime = currentTime;
//		Date downloadTime = new Date(currentTime);
//
//		ProductSummary testProduct1 = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), productName, Retailer.TESTRETAILER.getLink() + productIdCounter, 
//					"http://www.testproduct3image.com", "product 3 model").categoryId(category3.getCategoryId()).categoryName(category3.getName()).
//					price(100.2).desc("product 3 desc").creationTime(downloadTime).downloadTime(downloadTime).reviewRating(3.5).numReviews(20).build();
//
		Product prod1 = new Product(1, "prod1", "model1", "http://image1.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks1 = new ArrayList<Tick>();
		Tick priceTick1_1 = new Tick(currentTime, 11.67);
		Tick priceTick1_2 = new Tick(currentTime-TIME_DIFF, 42.33);
		Tick priceTick1_3 = new Tick(currentTime+TIME_DIFF, 15.39);
		Tick priceTick1_4 = new Tick(currentTime+TIME_DIFF/2, 100.39);
		Tick priceTick1_5 = new Tick(currentTime+TIME_DIFF/3, -100.39);
		priceTicks1.add(priceTick1_1);
		priceTicks1.add(priceTick1_2);
		priceTicks1.add(priceTick1_3);
		priceTicks1.add(priceTick1_4);
		priceTicks1.add(priceTick1_5);
		prod1.getPriceHistory().setPriceTicks(priceTicks1);
		
		Review review1_1 = new Review(currentTime, 3, 100);
		Review review1_2 = new Review(currentTime-TIME_DIFF, 4, 203);
		prod1.getReviewHistory().add(review1_1);
		prod1.getReviewHistory().add(review1_2);
		
		/*Tick sellRankTick1_1 = new Tick(currentTime, 100);
		Tick sellRankTick1_2 = new Tick(currentTime-TIME_DIFF, 50);
		Tick sellRankTick1_3 = new Tick(currentTime+TIME_DIFF, 200);
		prod1.getSellRankHistory().add(sellRankTick1_1);
		prod1.getSellRankHistory().add(sellRankTick1_2);
		prod1.getSellRankHistory().add(sellRankTick1_3);*/
		
		ProductData.getInstance().addProduct(TESTRETAILER, prod1);
		List<Product> categoryList1 = new ArrayList<Product>();
		categoryList1.add(prod1);
		ProductData.getInstance().getCategoryProductsMap().put(1, categoryList1);
		ProductData.getInstance().getCategoryProductsByPriceMap().put(1, new ArrayList<>(categoryList1));
		ProductData.getInstance().getCategoryProductsByReviewMap().put(1, new ArrayList<>(categoryList1));
		
		cache.processProductHistoryData();
		
		//TODO not tested if curr price is min as then current time is set in to which cant be asserted
		PriceHistory expectedPriceHist1 = new PriceHistory();
		expectedPriceHist1.setCurrPrice(priceTick1_3.getValue());
		expectedPriceHist1.setMaxPrice(priceTick1_4.getValue());
		expectedPriceHist1.setMinPrice(priceTick1_1.getValue());
		expectedPriceHist1.setCurrPriceFromTime(priceTick1_3.getTime());
		expectedPriceHist1.setMaxPriceFromTime(priceTick1_4.getTime());
		expectedPriceHist1.setMaxPriceToTime(priceTick1_3.getTime());
		expectedPriceHist1.setMinPriceFromTime(priceTick1_1.getTime());
		expectedPriceHist1.setMinPriceToTime(priceTick1_4.getTime());
		
		assertProduct(1, expectedPriceHist1);
		
		ProductData.getInstance().sortAllCategoryProductsMap();
		assertProductList(1);
		
		logger.info("======= MAKING PRODUCT2 =======");
		Product prod2 = new Product(2, "prod2", "model2", "http://image2.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks2 = new ArrayList<Tick>();
		Tick priceTick2_1 = new Tick(currentTime, 11.39);
		Tick priceTick2_2 = new Tick(currentTime-TIME_DIFF, 45.33);
		Tick priceTick2_3 = new Tick(currentTime+TIME_DIFF, 15.39);
		Tick priceTick2_4 = new Tick(currentTime+TIME_DIFF/2, 11.39);
		priceTicks2.add(priceTick2_1);
		priceTicks2.add(priceTick2_2);
		priceTicks2.add(priceTick2_3);
		priceTicks2.add(priceTick2_4);
		prod2.getPriceHistory().setPriceTicks(priceTicks2);
		
		Review review2_1 = new Review(currentTime, 3.9, 30);
		Review review2_2 = new Review(currentTime-TIME_DIFF, 3.3, 45);
		prod2.getReviewHistory().add(review2_1);
		prod2.getReviewHistory().add(review2_2);
		
		/*Tick sellRankTick2_1 = new Tick(currentTime, 20);
		Tick sellRankTick2_2 = new Tick(currentTime-TIME_DIFF, 150);
		Tick sellRankTick2_3 = new Tick(currentTime+TIME_DIFF, 100);
		prod2.getSellRankHistory().add(sellRankTick2_1);
		prod2.getSellRankHistory().add(sellRankTick2_2);
		prod2.getSellRankHistory().add(sellRankTick2_3);*/
		
		ProductData.getInstance().addProduct(TESTRETAILER, prod2);
		categoryList1.add(prod2);
		ProductData.getInstance().getCategoryProductsByPriceMap().get(1).add(prod2);
		ProductData.getInstance().getCategoryProductsByReviewMap().get(1).add(prod2);
	
		cache.processProductHistoryData();
		
		PriceHistory expectedPriceHist2 = new PriceHistory();
		expectedPriceHist2.setCurrPrice(priceTick2_3.getValue());
		expectedPriceHist2.setMaxPrice(priceTick2_2.getValue());
		expectedPriceHist2.setMinPrice(priceTick2_4.getValue());
		expectedPriceHist2.setCurrPriceFromTime(priceTick2_3.getTime());
		expectedPriceHist2.setMaxPriceFromTime(priceTick2_2.getTime());
		expectedPriceHist2.setMaxPriceToTime(priceTick2_1.getTime());
		expectedPriceHist2.setMinPriceFromTime(priceTick2_4.getTime());
		expectedPriceHist2.setMinPriceToTime(priceTick2_3.getTime());
		
		assertProduct(1, expectedPriceHist1);
		assertProduct(2, expectedPriceHist2);
		
		ProductData.getInstance().sortAllCategoryProductsMap();
		assertProductList(1);
		
		logger.info("======= MAKING PRODUCT3 =======");
		Product prod3 = new Product(3, "prod3", "model3", "http://image3.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks3 = new ArrayList<Tick>();
		Tick priceTick3_1 = new Tick(currentTime, 131.39);
		Tick priceTick3_2 = new Tick(currentTime-TIME_DIFF, 155.33);
		Tick priceTick3_3 = new Tick(currentTime+TIME_DIFF, 150.39);
		priceTicks3.add(priceTick3_1);
		priceTicks3.add(priceTick3_2);
		priceTicks3.add(priceTick3_3);
		prod3.getPriceHistory().setPriceTicks(priceTicks3);
		
		//Tick sellRankTick3_1 = new Tick(currentTime, 42);
		//prod3.getSellRankHistory().add(sellRankTick3_1);
		
		ProductData.getInstance().addProduct(TESTRETAILER, prod3);
		categoryList1.add(prod3);
		ProductData.getInstance().getCategoryProductsByPriceMap().get(1).add(prod3);
		ProductData.getInstance().getCategoryProductsByReviewMap().get(1).add(prod3);
		
		cache.processProductHistoryData();
		
		PriceHistory expectedPriceHist3 = new PriceHistory();
		expectedPriceHist3.setCurrPrice(priceTick3_3.getValue());
		expectedPriceHist3.setMaxPrice(priceTick3_2.getValue());
		expectedPriceHist3.setMinPrice(priceTick3_1.getValue());
		expectedPriceHist3.setCurrPriceFromTime(priceTick3_3.getTime());
		expectedPriceHist3.setMaxPriceFromTime(priceTick3_2.getTime());
		expectedPriceHist3.setMaxPriceToTime(priceTick3_1.getTime());
		expectedPriceHist3.setMinPriceFromTime(priceTick3_1.getTime());
		expectedPriceHist3.setMinPriceToTime(priceTick3_3.getTime());
		
		assertProduct(1, expectedPriceHist1);
		assertProduct(2, expectedPriceHist2);
		assertProduct(3, expectedPriceHist3);
		
		ProductData.getInstance().sortAllCategoryProductsMap();
		assertProductList(1);
	}
	
	public void checkIfTickListSorted(List<Tick> ticks){
		logger.info("Asserting if the tick list is sorted.");
		long prevTime = 0;
		for(Tick tick : ticks){
			assertTrue(tick.getTime()>=prevTime);
			prevTime = tick.getTime();
		}
	}
	
	public void checkIfReviewListSorted(List<Review> reviews){
		logger.info("Asserting if the review list is sorted.");
		long prevTime = 0;
		for(Review review : reviews){
			assertTrue(review.getTime()>=prevTime);
			prevTime = review.getTime();
		}
	}
	
	public void checkPriceHistory(PriceHistory expected, PriceHistory actual){
		logger.info("Asserting if the Price History is correct");
		assertEquals(expected.getCurrPrice(), actual.getCurrPrice(), epsilon);
		assertEquals(expected.getMaxPrice(), actual.getMaxPrice(), epsilon);
		assertEquals(expected.getMinPrice(), actual.getMinPrice(), epsilon);
		assertEquals(expected.getCurrPriceFromTime(), actual.getCurrPriceFromTime(), epsilon);
		assertEquals(expected.getMaxPriceFromTime(), actual.getMaxPriceFromTime(), epsilon);
		assertEquals(expected.getMaxPriceToTime(), actual.getMaxPriceToTime(), epsilon);
		assertEquals(expected.getMinPriceFromTime(), actual.getMinPriceFromTime(), epsilon);
		assertEquals(expected.getMinPriceToTime(), actual.getMinPriceToTime(), epsilon);
	}
	
	//checks if all tick lists are sorted and PriceHistory's parameters are correct or not
	public void assertProduct(int prodId, PriceHistory expected){
		checkIfTickListSorted(ProductData.getInstance().getProduct(prodId).getPriceHistory().getPriceTicks());
		checkIfReviewListSorted(ProductData.getInstance().getProduct(prodId).getReviewHistory());
		//checkIfTickListSorted(cache.getProductIdProductMap().get(prodId).getSellRankHistory());
		checkPriceHistory(expected, ProductData.getInstance().getProduct(prodId).getPriceHistory());
		//TODO Here not asserting if products of other maps are correct because product in other maps is just a reference
	}
	
	public void checkIfProductListSortedByPrice(List<Product> products){
		logger.info("Asserting if the product list is sorted by price.");
		double prevPrice = 0.00;
		for(Product prod : products){
			assertTrue(!(prod.getPriceHistory().getCurrPrice()<prevPrice));
			prevPrice = prod.getPriceHistory().getCurrPrice();
		}
	}
	
	public void checkIfProductListSortedByReview(List<Product> products){
		logger.info("Asserting if the product list is sorted by reviews.");
		double prevReview = 1000000.00;//Very large value here
		for(Product prod : products){
			List<Review> reviewHistory = prod.getReviewHistory();
			if(reviewHistory.size()>0){
				assertTrue(!(reviewHistory.get(reviewHistory.size()-1).getReviewRating()>prevReview));
				prevReview = reviewHistory.get(reviewHistory.size()-1).getReviewRating();
			}
		}
	}
	/*
	public void checkIfProductListSortedBySellRank(List<Product> products){
		logger.info("Asserting if the product list is sorted by sell rank.");
		double prevSellRank = 0;
		for(Product prod : products){
			List<Tick> sellRankHistory = prod.getSellRankHistory();
			if(sellRankHistory.size()>0){
				assertTrue(!(sellRankHistory.get(sellRankHistory.size()-1).value<prevSellRank));
				prevSellRank = sellRankHistory.get(sellRankHistory.size()-1).value;
			}
		}
	}
	*/
	//checks if all categoryProduct maps are sorted or not
	public void assertProductList(int categoryId){
		checkIfProductListSortedByPrice(ProductData.getInstance().getCategoryProductsByPriceMap().get(categoryId));
		checkIfProductListSortedByReview(ProductData.getInstance().getCategoryProductsByReviewMap().get(categoryId));
	}
	
	@Test
	public void testRemoveTerminalCategory(){
		logger.info("========= TESTING REMOVE TERMINAL CATEGORY FUNCTION =========");
		cache = new ProductCacheImpl(myRetailers);
		
		List<ProductSummary> updates = new ArrayList<ProductSummary>();
		logger.info("======= MAKING ALL THE PRODUCTS =======");
		int categoryid1 = 1;
		ProductSummary prod1= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(categoryid1).productId(1).price(23.23).downloadTime(new Date(currentTime)).build();
		ProductSummary prod2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product2", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(categoryid1).productId(2).price(11.67).downloadTime(new Date(currentTime)).build();
		ProductSummary prod3_1= new ProductSummaryBuilder(TESTRETAILER.getId(), "product3", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(categoryid1).productId(3).price(253.98).downloadTime(new Date(currentTime)).build();
		int categoryid2 = 2;
		ProductSummary prod3_2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product3", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(categoryid2).productId(3).price(253.98).downloadTime(new Date(currentTime)).build();
		
		updates.add(prod1);
		updates.add(prod2);
		updates.add(prod3_1);
		updates.add(prod3_2);
		cache.updateProducts(updates);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=3; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCategoryInCache(prod1, true);
		assertCategoryInCache(prod2, true);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=3; expNoOfCategoriesForProduct=2;expPriceTicksSize=1;
		assertCategoryInCache(prod3_1, true);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=2;expPriceTicksSize=1;
		assertCategoryInCache(prod3_2, true);
		
		ProductData.getInstance().removeTerminalCategory(categoryid1);
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=0; expNoOfCategoriesForProduct=0;expPriceTicksSize=0;
		assertCategoryInCache(prod1, false);
		assertCategoryInCache(prod2, false);
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=0; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
//		assertCache(prod3_1, true);//TODO: write test for such issue prodid-active, categoryid-inactive
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCategoryInCache(prod3_2, true);
	}
	
	public void assertCategoryInCache(ProductSummary product, boolean isCategoryActive) {
		if(expNoOfProductsInCategory>0){
			assertEquals(expNoOfProductsInCategory, ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId()).size());
			assertEquals(expNoOfProductsInCategory, ProductData.getInstance().getCategoryProductsByPriceMap().get(product.getCategoryId()).size());
//			assertEquals(expNoOfProductsInCategory, ProductData.getInstance().getCategoryProductsByReviewMap().get(product.getCategoryId()).size());
		} else {
			assertTrue(ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId())==null);
			assertTrue(ProductData.getInstance().getCategoryProductsByPriceMap().get(product.getCategoryId())==null);
//			assertTrue(ProductData.getInstance().getCategoryProductsByReviewMap().get(product.getCategoryId())==null);
		}
		
		if(expNoOfCategoriesForProduct>0)
			assertEquals(expNoOfCategoriesForProduct, ProductData.getInstance().getCategoriesOfProduct(product.getId()).size());
		
		if(isCategoryActive){			
			assertTrue(ProductData.getInstance().getProduct(product.getId())!=null);
			assertTrue(ProductData.getInstance().getProductIdCategoryMap().get(product.getId()).contains(product.getCategoryId()));
			
			Product actualProduct = ProductData.getInstance().getProduct(product.getId());
			//couple of sanity checks
			assertEquals(actualProduct.getUrl(), product.getUrl());
			assertEquals(actualProduct.getName(), product.getName());
			List<Tick> actualPriceTicks = actualProduct.getPriceHistory().getPriceTicks();
			assertEquals(expPriceTicksSize, actualPriceTicks.size());
			_assertTime(product.getDownloadTime().getTime(), actualPriceTicks.get(expPriceTicksSize-1).getTime());
			assertEquals(product.getPrice(), actualPriceTicks.get(expPriceTicksSize-1).getValue(), epsilon);
		}
		else{
			assertTrue(ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId())==null);
			assertTrue(ProductData.getInstance().getCategoryProductsByPriceMap().get(product.getCategoryId())==null);
			assertTrue(ProductData.getInstance().getCategoryProductsByReviewMap().get(product.getCategoryId())==null);
		}
	}
}

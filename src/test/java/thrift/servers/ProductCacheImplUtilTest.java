package thrift.servers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;

/**
 * @author Amit
 * Test of class ProductCacheImplUtil
 */

public class ProductCacheImplUtilTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductCacheImplUtilTest.class);
	private static long currentTime = System.currentTimeMillis();
	ProductCacheImplUtil util = new ProductCacheImplUtil();
	private static final long TIME_DIFF = 60*60*1000L;
	
	public void setUp(){
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
	}
	
	/*
	 * Adding negative prices and then checking if they are getting
	 * removed or not
	 */
	@Test
	public void testCleanUpPriceHistory(){
		logger.info("========== TESTING CLEANUP PRICE HISTORY ==========");
		PriceHistory actualPriceHistory = new PriceHistory();
		PriceHistory expectedPriceHistory = new PriceHistory();
		List<Tick> actualPriceTicks = new ArrayList<Tick>();
		List<Tick> expectedPriceTicks = new ArrayList<Tick>();
		actualPriceHistory.setPriceTicks(actualPriceTicks);
		expectedPriceHistory.setPriceTicks(expectedPriceTicks);
		Tick p1 = new Tick(currentTime-TIME_DIFF, 22.10);
		Tick p2 = new Tick(currentTime, -1.22);
		Tick p3 = new Tick(currentTime+TIME_DIFF, 3.19);
		Tick p4 = new Tick(currentTime, 0.00);
		actualPriceTicks.add(p1);
		actualPriceTicks.add(p2);
		actualPriceTicks.add(p3);
		actualPriceTicks.add(p4);
		expectedPriceTicks.add(p1);
		expectedPriceTicks.add(p3);
		
		util.cleanUpTicks(actualPriceTicks);
		assertPriceHistory(expectedPriceHistory, actualPriceHistory, true);
	}
	
	/*
	 * Testing if reviewRating is not positive or numReviews is not positive
	 */
	@Test
	public void testCleanUpReviewHistory(){
		logger.info("========== TESTING CLEANUP REVIEW HISTORY - CaseI ==========");
		List<Review> expectedReviewHistory = new ArrayList<Review>();
		List<Review> actualReviewHistory = new ArrayList<Review>();
		Review ar1 = new Review(currentTime-TIME_DIFF, 2.0, 33);
		Review ar2 = new Review(currentTime, -1, 36);
		Review ar3 = new Review(currentTime+TIME_DIFF, 2.3, -5);
		Review ar4 = new Review(currentTime+(2*TIME_DIFF), 3.0, 22);
		actualReviewHistory.add(ar1);
		actualReviewHistory.add(ar2);
		actualReviewHistory.add(ar3);
		actualReviewHistory.add(ar4);
		Review er1 = new Review(currentTime-TIME_DIFF, 2.0, 33);
		Review er2 = new Review(currentTime+TIME_DIFF, 2.3, 33);
		Review er3 = new Review(currentTime+(2*TIME_DIFF), 3.0, 22);
		expectedReviewHistory.add(er1);
		expectedReviewHistory.add(er2);
		expectedReviewHistory.add(er3);
		
		util.cleanUpReviewHistory(actualReviewHistory);
		assertReviewHistory(expectedReviewHistory, actualReviewHistory);
		
		logger.info("========== TESTING CLEANUP REVIEW HISTORY - CaseII ==========");
		expectedReviewHistory.clear();
		actualReviewHistory.clear();
		Review ar5 = new Review(currentTime-TIME_DIFF, -2.0, -33);
		Review ar6 = new Review(currentTime, 2.5, -36);
		Review ar7 = new Review(currentTime+TIME_DIFF, 0.0, -22);
		Review ar8 = new Review(currentTime+(2*TIME_DIFF), 2.3, 3);
		Review ar9 = new Review(currentTime+(3*TIME_DIFF), 1.0, -3);
		actualReviewHistory.add(ar5);
		actualReviewHistory.add(ar6);
		actualReviewHistory.add(ar7);
		actualReviewHistory.add(ar8);
		actualReviewHistory.add(ar9);
		Review er4 = new Review(currentTime, 2.5, 0);
		Review er5 = new Review(currentTime+(2*TIME_DIFF), 2.3, 3);
		Review er6 = new Review(currentTime+(3*TIME_DIFF), 1.0, 3);
		expectedReviewHistory.add(er4);
		expectedReviewHistory.add(er5);
		expectedReviewHistory.add(er6);
		
		util.cleanUpReviewHistory(actualReviewHistory);
		assertReviewHistory(expectedReviewHistory, actualReviewHistory);
	}
	
	/*
	 * Testing if negative prices are removed and if other parameters of
	 * PriceHistory are rightly set or not
	 */
	@Test
	public void testProcessPriceHistory(){
		logger.info("========== TESTING PROCESS PRICE HISTORY ==========");
		PriceHistory actualPriceHistory = new PriceHistory();
		PriceHistory expectedPriceHistory = new PriceHistory();
		List<Tick> actualPriceTicks = new ArrayList<Tick>();
		List<Tick> expectedPriceTicks = new ArrayList<Tick>();
		actualPriceHistory.setPriceTicks(actualPriceTicks);
		expectedPriceHistory.setPriceTicks(expectedPriceTicks);
		
		Tick p1 = new Tick(currentTime-TIME_DIFF, 22.10);
		Tick p2 = new Tick(currentTime, 1.22);
		Tick p3 = new Tick(currentTime+TIME_DIFF, 23.19);
		Tick p4 = new Tick(currentTime+(2*TIME_DIFF), -3.24);
		actualPriceTicks.add(p1);
		actualPriceTicks.add(p2);
		actualPriceTicks.add(p3);
		actualPriceTicks.add(p4);
		util.processPriceHistory(actualPriceHistory);
		expectedPriceTicks.add(p1);
		expectedPriceTicks.add(p2);
		expectedPriceTicks.add(p3);
		expectedPriceHistory.setCurrPrice(p3.getValue());
		expectedPriceHistory.setCurrPriceFromTime(p3.getTime());
		expectedPriceHistory.setMaxPrice(p3.getValue());
		expectedPriceHistory.setMaxPriceFromTime(p3.getTime());
		expectedPriceHistory.setMaxPriceToTime(-1);
		expectedPriceHistory.setMinPrice(p2.getValue());
		expectedPriceHistory.setMinPriceFromTime(p2.getTime());
		expectedPriceHistory.setMinPriceToTime(p3.getTime());
		assertPriceHistory(expectedPriceHistory, actualPriceHistory, false);
	}
	
	/*
	 * Testing if extra sell ranks, they are getting removed or not
	 */
	@Test
	public void testProcessSellRankHistory(){
		logger.info("========== TESTING PROCESS SELL RANK HISTORY ==========");
		List<Tick> actualSellRankHistory = new ArrayList<Tick>();
		List<Tick> expectedSellRankHistory = new ArrayList<Tick>();
		
		logger.info("======= IF SIZE OF SELL RANK HISTORY IS EQUAL TO MAX =======");
		util.overrideMaxSellRankHistorySize(2);
		Tick sr1 = new Tick(currentTime-TIME_DIFF, 22);
		Tick sr2 = new Tick(currentTime-(TIME_DIFF/2), 12);
		actualSellRankHistory.add(sr1);
		actualSellRankHistory.add(sr2);
		util.processSellRankHistory(actualSellRankHistory);
		expectedSellRankHistory.add(sr1);
		expectedSellRankHistory.add(sr2);
		assertSellRankHistory(expectedSellRankHistory, actualSellRankHistory);
		
		logger.info("======= IF SIZE OF SELL RANK HISTORY IS GREATER THAN MAX =======");
		Tick sr3 = new Tick(currentTime+TIME_DIFF, 13);
		actualSellRankHistory.add(sr3);
		expectedSellRankHistory.remove(sr1);
		expectedSellRankHistory.add(sr3);
		util.processSellRankHistory(actualSellRankHistory);
		assertSellRankHistory(expectedSellRankHistory, actualSellRankHistory);
		
		logger.info("======= IF ONE OF THE INTERVAL HAS NO SELL RANK =======");
		util.overrideMaxSellRankHistorySize(3);
		Tick sr4 = new Tick(currentTime+(8*TIME_DIFF), 19);
		actualSellRankHistory.clear();
		actualSellRankHistory.add(sr1);
		actualSellRankHistory.add(sr2);
		actualSellRankHistory.add(sr3);
		actualSellRankHistory.add(sr4);
		expectedSellRankHistory.clear();
		expectedSellRankHistory.add(sr3);
		expectedSellRankHistory.add(sr4);
		util.processSellRankHistory(actualSellRankHistory);
		assertSellRankHistory(expectedSellRankHistory, actualSellRankHistory);
	}
	
	/*
	 * Testing if extra reviews, they are getting removed or not
	 */
	@Test
	public void testProcessReviewHistory(){
		logger.info("========== TESTING PROCESS SELL RANK HISTORY ==========");
		List<Review> actualReviewHistory = new ArrayList<Review>();
		List<Review> expectedReviewHistory = new ArrayList<Review>();
		
		logger.info("======= IF SIZE OF REVIEW HISTORY IS EQUAL TO MAX =======");
		util.overrideMaxReviewHistorySize(2);
		Review r1 = new Review(currentTime-TIME_DIFF, 3.0, 22);
		Review r2 = new Review(currentTime-(TIME_DIFF/2), 2.5, 122);
		actualReviewHistory.add(r1);
		actualReviewHistory.add(r2);
		util.processReviewHistory(actualReviewHistory);
		expectedReviewHistory.add(r1);
		expectedReviewHistory.add(r2);
		assertReviewHistory(expectedReviewHistory, actualReviewHistory);
		
		logger.info("======= IF SIZE OF REVIEW HISTORY IS GREATER THAN MAX =======");
		Review r3 = new Review(currentTime+TIME_DIFF, 3.5, 196);
		actualReviewHistory.add(r3);
		expectedReviewHistory.remove(r1);
		expectedReviewHistory.add(r3);
		util.processReviewHistory(actualReviewHistory);
		assertReviewHistory(expectedReviewHistory, actualReviewHistory);
		
		logger.info("======= IF ONE OF THE INTERVAL HAS NO REVIEW =======");
		util.overrideMaxReviewHistorySize(3);
		Review r4 = new Review(currentTime+(8*TIME_DIFF), 3.8, 247);
		actualReviewHistory.clear();
		actualReviewHistory.add(r1);
		actualReviewHistory.add(r2);
		actualReviewHistory.add(r3);
		actualReviewHistory.add(r4);
		expectedReviewHistory.clear();
		expectedReviewHistory.add(r3);
		expectedReviewHistory.add(r4);
		util.processReviewHistory(actualReviewHistory);
		assertReviewHistory(expectedReviewHistory, actualReviewHistory);
	}
	
	@Test
	public void testProcessNewProduct(){
		PriceHistory actualPriceHistory = new PriceHistory();
		actualPriceHistory.setPriceTicks(new ArrayList<Tick>());
		List<Review> actualReviewHistory = new ArrayList<Review>();
		List<Tick> actualSellRankHistory = new ArrayList<Tick>();
		
		PriceHistory expectedPriceHistory = new PriceHistory();
		expectedPriceHistory.setPriceTicks(new ArrayList<Tick>());
		List<Review> expectedReviewHistory = new ArrayList<Review>();
		List<Tick> expectedSellRankHistory = new ArrayList<Tick>();
		
		Product actualProd = new Product(1, "product",
				"model", "http://imageURL", "http://productURL", 
				actualPriceHistory, actualSellRankHistory, actualReviewHistory);
		Product expectedProd = new Product(1, "product",
				"model", "http://imageURL", "http://productURL", 
				expectedPriceHistory, expectedSellRankHistory, expectedReviewHistory);
		
		logger.info("======= A NEW PRODUCT IS ADDED - HISTORY DATA IS NEGATIVE =======");
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime, -12.34));
		actualReviewHistory.add(new Review(currentTime, -3.0, 22));
		actualSellRankHistory.add(new Tick(currentTime, -4));
		util.processNewProduct(actualProd);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= A NEW PRODUCT IS ADDED - PRICE DATA IS POSITIVE =======");
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime, 12.34));
		actualReviewHistory.add(new Review(currentTime, -3.0, 22));
		actualSellRankHistory.add(new Tick(currentTime, -4));
		util.processNewProduct(actualProd);
		logger.info(actualPriceHistory);
		expectedPriceHistory.getPriceTicks().add(new Tick(currentTime, 12.34));
		expectedPriceHistory.setCurrPrice(12.34);
		expectedPriceHistory.setCurrPriceFromTime(currentTime);
		expectedPriceHistory.setMinPrice(12.34);
		expectedPriceHistory.setMinPriceFromTime(currentTime);
		expectedPriceHistory.setMinPriceToTime(-1);
		expectedPriceHistory.setMaxPrice(12.34);
		expectedPriceHistory.setMaxPriceFromTime(currentTime);
		expectedPriceHistory.setMaxPriceToTime(-1);
		assertProduct(expectedProd, actualProd);
	}
	
	@Test
	public void testProcessUpdatedProduct(){
		PriceHistory actualPriceHistory = new PriceHistory();
		actualPriceHistory.setPriceTicks(new ArrayList<Tick>());
		List<Review> actualReviewHistory = new ArrayList<Review>();
		List<Tick> actualSellRankHistory = new ArrayList<Tick>();
		
		PriceHistory expectedPriceHistory = new PriceHistory();
		expectedPriceHistory.setPriceTicks(new ArrayList<Tick>());
		List<Review> expectedReviewHistory = new ArrayList<Review>();
		List<Tick> expectedSellRankHistory = new ArrayList<Tick>();
		
		Product actualProd = new Product(1, "product",
				"model", "http://imageURL", "http://productURL", 
				actualPriceHistory, actualSellRankHistory, actualReviewHistory);
		Product expectedProd = new Product(1, "product",
				"model", "http://imageURL", "http://productURL", 
				expectedPriceHistory, expectedSellRankHistory, expectedReviewHistory);
		
		logger.info("======= SELL RANK HISTORY IS UPDATED - NEW ONE IS POSITIVE =======");
		actualSellRankHistory.add(new Tick(currentTime, 4));
		util.processUpdatedProduct(actualProd);
		expectedSellRankHistory.add(new Tick(currentTime, 4));
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= SELL RANK HISTORY IS UPDATED - NEW ONE IS NEGATIVE =======");
		actualSellRankHistory.add(new Tick(currentTime, -2));
		util.processUpdatedProduct(actualProd);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= REVIEW HISTORY IS UPDATED - NEW ONE IS POSITIVE =======");
		actualReviewHistory.add(new Review(currentTime, 4.2, 102));
		util.processUpdatedProduct(actualProd);
		expectedReviewHistory.add(new Review(currentTime, 4.2, 102));
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= REVIEW HISTORY IS UPDATED - NEGATIVE RATING =======");
		actualReviewHistory.add(new Review(currentTime, -1.9, 12));
		util.processUpdatedProduct(actualProd);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= REVIEW HISTORY IS UPDATED - POSITIVE RATING, NEGATIVE NUMREVIEWS =======");
		actualReviewHistory.add(new Review(currentTime, 2.3, -11));
		util.processUpdatedProduct(actualProd);
		expectedReviewHistory.add(new Review(currentTime, 2.3, 102));
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= PRICE HISTORY IS UPDATED - NEW ONE IS POSITIVE =======");
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime, 42.99));
		util.processUpdatedProduct(actualProd);
		logger.info(actualPriceHistory);
		expectedPriceHistory.getPriceTicks().add(new Tick(currentTime, 42.99));
		expectedPriceHistory.setCurrPrice(42.99);
		expectedPriceHistory.setCurrPriceFromTime(currentTime);
		expectedPriceHistory.setMinPrice(42.99);
		expectedPriceHistory.setMinPriceFromTime(currentTime);
		expectedPriceHistory.setMinPriceToTime(-1);
		expectedPriceHistory.setMaxPrice(42.99);
		expectedPriceHistory.setMaxPriceFromTime(currentTime);
		expectedPriceHistory.setMaxPriceToTime(-1);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= PRICE HISTORY IS UPDATED - NEW ONE IS NEGATIVE =======");
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime, -52.99));
		util.processUpdatedProduct(actualProd);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= PRICE HISTORY IS UPDATED - NEW PRICE IS MINIMUM =======");
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime+TIME_DIFF, 23.99));
		util.processUpdatedProduct(actualProd);
		expectedPriceHistory.getPriceTicks().add(new Tick(currentTime+TIME_DIFF, 23.99));
		expectedPriceHistory.setCurrPrice(23.99);
		expectedPriceHistory.setCurrPriceFromTime(currentTime+TIME_DIFF);
		expectedPriceHistory.setMinPrice(23.99);
		expectedPriceHistory.setMinPriceFromTime(currentTime+TIME_DIFF);
		expectedPriceHistory.setMinPriceToTime(-1);
		expectedPriceHistory.setMaxPrice(42.99);
		expectedPriceHistory.setMaxPriceFromTime(currentTime);
		expectedPriceHistory.setMaxPriceToTime(currentTime+TIME_DIFF);
		assertProduct(expectedProd, actualProd);
		
		logger.info("======= PRICE HISTORY IS UPDATED - WHOLE HISTORY GETS UPDATED =======");
		actualSellRankHistory.add(new Tick(currentTime+TIME_DIFF, -2));
		actualReviewHistory.add(new Review(currentTime+TIME_DIFF, 4.1, 122));
		actualPriceHistory.getPriceTicks().add(new Tick(currentTime+(2*TIME_DIFF), 62.99));
		util.processUpdatedProduct(actualProd);
		expectedReviewHistory.add(new Review(currentTime+TIME_DIFF, 4.1, 122));
		expectedPriceHistory.getPriceTicks().add(new Tick(currentTime+(2*TIME_DIFF), 62.99));
		expectedPriceHistory.setCurrPrice(62.99);
		expectedPriceHistory.setCurrPriceFromTime(currentTime+(2*TIME_DIFF));
		expectedPriceHistory.setMinPrice(23.99);
		expectedPriceHistory.setMinPriceFromTime(currentTime+TIME_DIFF);
		expectedPriceHistory.setMinPriceToTime(currentTime+(2*TIME_DIFF));
		expectedPriceHistory.setMaxPrice(62.99);
		expectedPriceHistory.setMaxPriceFromTime(currentTime+(2*TIME_DIFF));
		expectedPriceHistory.setMaxPriceToTime(-1);
		assertProduct(expectedProd, actualProd);
	}
	
	private void assertPriceHistory(PriceHistory expectedPriceHistory, PriceHistory actualPriceHistory, boolean checkOnlyTicks){
		List<Tick> expectedPriceTicks = expectedPriceHistory.getPriceTicks();
		List<Tick> actualPriceTicks = actualPriceHistory.getPriceTicks();
		_assertEquals(expectedPriceTicks.size(), actualPriceTicks.size());
		
		logger.info("Asserting if the price ticks history is correct.");
		for(int index = 0;index<actualPriceTicks.size();++index){
			Tick actual = actualPriceTicks.get(index);
			Tick expected = expectedPriceTicks.get(index);
			_assertEquals(expected.getTime(), actual.getTime());
			_assertEquals(expected.getValue(), actual.getValue());
		}
		
		if(!checkOnlyTicks){
			_assertEquals(expectedPriceHistory.getCurrPrice(), actualPriceHistory.getCurrPrice());
			_assertEquals(expectedPriceHistory.getMaxPrice(), actualPriceHistory.getMaxPrice());
			_assertEquals(expectedPriceHistory.getMinPrice(), actualPriceHistory.getMinPrice());
			_assertEquals(expectedPriceHistory.getCurrPriceFromTime(), actualPriceHistory.getCurrPriceFromTime());
			_assertEquals(expectedPriceHistory.getMaxPriceFromTime(), actualPriceHistory.getMaxPriceFromTime());
			_assertEquals(expectedPriceHistory.getMinPriceFromTime(), actualPriceHistory.getMinPriceFromTime());
			_assertEquals(expectedPriceHistory.getMaxPriceToTime(), actualPriceHistory.getMaxPriceToTime());
			_assertEquals(expectedPriceHistory.getMinPriceToTime(), actualPriceHistory.getMinPriceToTime());
		}
	}
	
	private void assertSellRankHistory(List<Tick> expectedSellRankHistory, List<Tick> actualSellRankHistory){
		_assertEquals(expectedSellRankHistory.size(), actualSellRankHistory.size());
		
		logger.info("Asserting if the sell rank history is correct.");
		for(int index = 0;index<actualSellRankHistory.size();++index){
			Tick actual = actualSellRankHistory.get(index);
			Tick expected = expectedSellRankHistory.get(index);
			_assertEquals(expected.getTime(), actual.getTime());
			_assertEquals(expected.getValue(), actual.getValue());
		}
	}
	
	private void assertReviewHistory(List<Review> expectedReviewHistory, List<Review> actualReviewHistory){
		_assertEquals(expectedReviewHistory.size(), actualReviewHistory.size());
		
		logger.info("Asserting if the review history is correct.");
		for(int index = 0;index<actualReviewHistory.size();++index){
			Review actual = actualReviewHistory.get(index);
			Review expected = expectedReviewHistory.get(index);
			_assertEquals(expected.getTime(), actual.getTime());
			_assertEquals(expected.getReviewRating(), actual.getReviewRating());
			_assertEquals(expected.getNumReviews(), actual.getNumReviews());
		}
	}
	
	private void assertProduct(Product expected, Product actual){
		assertPriceHistory(expected.getPriceHistory(), actual.getPriceHistory(), false);
		//assertSellRankHistory(expected.getSellRankHistory(), actual.getSellRankHistory());
		assertReviewHistory(expected.getReviewHistory(), actual.getReviewHistory());
	}
}

package adhoc;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;
import thrift.servers.ProductData;
import adhoc.PostPurchasePricingAnalytics.PostPurchaseStat;
import categories.CategoryTreeNode;
import db.dao.DAOException;
import db.dao.DataAccessObject;
import db.dao.PostPurchaseStatDAO;
import entities.Retailer;

public class PostPurchasePricingAnalyticsTest
{
	@Before
	public void setup()
	{
		ProductData.getInstance().resetUnitTest();
	}
	@Test
	public void testGenerateStats() throws java.text.ParseException 
	{
		Product p1 = new Product();
		p1.productId = 123;
		p1.url = "www.testretailer.com/123";
		PriceHistory priceHistory = new PriceHistory();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
		
		List<Tick> priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 90 ));
		priceHistory.setPriceTicks(priceTicks);
		p1.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p1);
		
		PostPurchasePricingAnalytics postPurchasePricingAnalytics = new PostPurchasePricingAnalytics();
		Date startDate = sdf.parse("2013-Jan-01");
		Date endDate = sdf.parse("2013-JAN-02");
		Map<Date, Map<Integer, PostPurchaseStat>> postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 0.1, -1);
		
		PostPurchaseStat stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 1);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);		

		startDate = sdf.parse("2013-Jan-10");
		endDate = sdf.parse("2013-JAN-11");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-10")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 1);		

		
		startDate = sdf.parse("2013-Jan-12");
		endDate = sdf.parse("2013-JAN-13");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats( startDate, endDate,false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-12")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);		

		Product p2 = new Product();
		p2.productId = 456;
		p2.url = "www.testretailer.com/456";
		priceHistory = new PriceHistory();
		priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-11").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-09").getTime(), 90 ));

		priceTicks.add(new Tick( sdf.parse("2012-Dec-25").getTime(), 140 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-03").getTime(), 120 ));
		priceHistory.setPriceTicks(priceTicks);
		p2.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p2);

		startDate = sdf.parse("2012-Dec-24");
		endDate = sdf.parse("2013-Dec-25");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats( startDate, endDate, false, 0.1, -1);

		
		stat1 = postPurchaseStats.get( sdf.parse("2012-DEC-24")).get(-1);
		assertEquals(stat1.totalProducts, 0);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 0);		

		
		startDate = sdf.parse("2012-Dec-26");
		endDate = sdf.parse("2013-Dec-27");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, false, 0.1, -1);

		stat1 = postPurchaseStats.get( sdf.parse("2012-DEC-26")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 1);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);		

		startDate = sdf.parse("2013-Jan-01");
		endDate = sdf.parse("2013-Jan-02");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 1);		
		assertEquals(stat1.dropIn15Days, 2);		
		assertEquals(stat1.dropIn30Days, 2);		
		assertEquals(stat1.dropIn90Days, 2);		

		
		startDate = sdf.parse("2013-Jan-10");
		endDate = sdf.parse("2013-Jan-11");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-10")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 2);		

		startDate = sdf.parse("2013-Jan-12");
		endDate = sdf.parse("2013-Jan-13");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-12")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 2);		
		assertEquals(stat1.dropIn90Days, 2);		
	}

	@Test
	public void testSaveStats() throws DAOException, SQLException, ParseException  
	{
		DataAccessObject.executeUpdate("DELETE FROM POST_PURCHASE_STAT where retailer_id = 'Test'");
		
		PostPurchasePricingAnalytics test = new PostPurchasePricingAnalytics();
		PostPurchaseStat postPurchaseStat = test.new PostPurchaseStat();
		postPurchaseStat.retailer = "Test";
		postPurchaseStat.statDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-04");
		postPurchaseStat.totalProducts = 5;
		postPurchaseStat.dropIn7Days = 7;
		postPurchaseStat.dropIn15Days = 15;
		postPurchaseStat.dropIn30Days = 30;
		postPurchaseStat.dropIn90Days = 90;
		postPurchaseStat.dropAmt = 5;
		postPurchaseStat.categoryid = 12345;
		postPurchaseStat.addenda1 = "comment1";
		
		PostPurchaseStatDAO postPostPurchaseDAO = PostPurchaseStatDAO.getInstance();
		postPostPurchaseDAO.store(postPurchaseStat);
		
		ResultSet rs = DataAccessObject.execute("SELECT RETAILER_ID, STATDATE, TOTAL, CATEGORY_ID, DROPPED7DAY, DROPPED15DAY, DROPPED30DAY, DROPPED90DAY, DROPAMT, ADDENDA1, ADDENDA2, ADDENDA3 " +
				" FROM POST_PURCHASE_STAT where retailer_id = 'Test'");
		while(rs.next())
		{
			assertEquals( rs.getString("RETAILER_ID"), "Test" );
			assertEquals( rs.getInt("TOTAL"), 5 );
			assertEquals( rs.getDate("STATDATE"), new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-04").getTime()));
			assertEquals( rs.getInt("DROPPED7DAY"), 7 );
			assertEquals( rs.getInt("DROPPED15DAY"), 15 );
			assertEquals( rs.getInt("DROPPED30DAY"), 30 );
			assertEquals( rs.getInt("DROPPED90DAY"), 90 );
			assertEquals(rs.getDouble("DROPAMT"), 5 , 0.1);
			assertEquals( rs.getInt("CATEGORY_ID"), 12345 );
			assertEquals(  rs.getString("ADDENDA1"), "comment1" );
		}

		DataAccessObject.executeUpdate("DELETE FROM POST_PURCHASE_STAT where retailer_id = 'Test'");
	}
	
	@Test
	public void testGenerateStatsByDropAmt() throws java.text.ParseException 
	{
		Product p1 = new Product();
		p1.productId = 123;
		p1.url = "www.testretailer.com/123";	
		PriceHistory priceHistory = new PriceHistory();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
		
		List<Tick> priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 80 ));
		priceHistory.setPriceTicks(priceTicks);
		p1.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p1);

		PostPurchasePricingAnalytics postPurchasePricingAnalytics = new PostPurchasePricingAnalytics();
		Date startDate = sdf.parse("2013-Jan-01");
		Date endDate = sdf.parse("2013-JAN-14");
		Map<Date, Map<Integer,PostPurchaseStat>> postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);
		
		
		PostPurchaseStat stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 1);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);			
		
		startDate = sdf.parse("2013-Jan-10");
		endDate = sdf.parse("2013-JAN-11");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);

		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-10")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 1);			
	
		startDate = sdf.parse("2013-Jan-12");
		endDate = sdf.parse("2013-JAN-13");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-12")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);			

	
		Product p2 = new Product();
		p2.productId = 456;
		p2.url = "www.testretailer.com/456";
		priceHistory = new PriceHistory();
		priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-11").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-09").getTime(), 90 ));

		priceTicks.add(new Tick( sdf.parse("2012-Dec-25").getTime(), 140 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-03").getTime(), 120 ));
		priceHistory.setPriceTicks(priceTicks);
		p2.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p2);


		startDate = sdf.parse("2012-Dec-24");
		endDate = sdf.parse("2012-Dec-25");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, false, 0.1, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2012-DEC-24")).get(-1);
		assertEquals(stat1.totalProducts, 0);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 0);			
		
		startDate = sdf.parse("2012-Dec-26");
		endDate = sdf.parse("2012-Dec-27");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2012-DEC-26")).get(-1);
		assertEquals(stat1.totalProducts, 1);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 1);		
		assertEquals(stat1.dropIn30Days, 1);		
		assertEquals(stat1.dropIn90Days, 1);			
		
		startDate = sdf.parse("2013-Jan-01");
		endDate = sdf.parse("2013-Jan-02");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 1);		
		assertEquals(stat1.dropIn15Days, 2);		
		assertEquals(stat1.dropIn30Days, 2);		
		assertEquals(stat1.dropIn90Days, 2);			
		
		
		
		startDate = sdf.parse("2013-Jan-10");
		endDate = sdf.parse("2013-Jan-11");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);
		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-10")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 0);		
		assertEquals(stat1.dropIn90Days, 1);			

		
		
		startDate = sdf.parse("2013-Jan-12");
		endDate = sdf.parse("2013-Jan-13");
		postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 19, -1);

		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-12")).get(-1);
		assertEquals(stat1.totalProducts, 2);
		assertEquals(stat1.dropIn7Days, 0);		
		assertEquals(stat1.dropIn15Days, 0);		
		assertEquals(stat1.dropIn30Days, 2);		
		assertEquals(stat1.dropIn90Days, 2);			

	}
	
	@Test
	public void testGenerateStatsByCategory() throws java.text.ParseException 
	{
		Product p1 = new Product();
		p1.productId = 123;
		p1.url = "www.testretailer.com/123";
		PriceHistory priceHistory = new PriceHistory();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
		
		List<Tick> priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 90 ));
		priceHistory.setPriceTicks(priceTicks);
		p1.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p1);
		
		Product p2 = new Product();
		p2.productId = 456;
		p2.url = "www.testretailer.com/456";
		priceHistory = new PriceHistory();
		
		priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 90 ));
		priceHistory.setPriceTicks(priceTicks);
		p2.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p2);
		
		PostPurchasePricingAnalytics postPurchasePricingAnalytics = new PostPurchasePricingAnalytics();
		
		postPurchasePricingAnalytics.productToCategory = getProductToCategoryMap();
		CategoryTreeNode.terminalCategoryMap = getTerminalToTopCategoryMap();
		Date startDate = sdf.parse("2013-Jan-01");
		Date endDate = sdf.parse("2013-JAN-02");
		Map<Date, Map<Integer,PostPurchaseStat>> postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 0.1, 25);
		
		PostPurchaseStat stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(25);
		assertEquals(1, stat1.totalProducts);
		assertEquals(0, stat1.dropIn7Days);		
		assertEquals(1, stat1.dropIn15Days);		
		assertEquals(1, stat1.dropIn30Days);		
		assertEquals(1, stat1.dropIn90Days);		

		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(2, stat1.totalProducts);
		assertEquals(0, stat1.dropIn7Days);		
		assertEquals(2, stat1.dropIn15Days);		
		assertEquals(2, stat1.dropIn30Days);		
		assertEquals(2, stat1.dropIn90Days);		
	}
	
	@Test
	public void testGenerateStatsForTotalDropByCategory() throws java.text.ParseException 
	{
		Product p1 = new Product();
		p1.productId = 123;
		p1.url = "www.testretailer.com/123";
		PriceHistory priceHistory = new PriceHistory();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
		
		List<Tick> priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-11").getTime(), 80 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 90 ));
		priceHistory.setPriceTicks(priceTicks);
		p1.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p1);
		
		Product p2 = new Product();
		p2.productId = 456;
		p2.url = "www.testretailer.com/456";
		priceHistory = new PriceHistory();
		
		priceTicks = new ArrayList<>();
		priceTicks.add(new Tick( sdf.parse("2013-JAN-01").getTime(), 120 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-10").getTime(), 100 ));
		priceTicks.add(new Tick( sdf.parse("2013-JAN-12").getTime(), 120 ));
	    priceTicks.add(new Tick( sdf.parse("2013-Feb-10").getTime(), 90 ));
		priceHistory.setPriceTicks(priceTicks);
		p2.setPriceHistory(priceHistory);
		ProductData.getInstance().addProduct(Retailer.TESTRETAILER, p2);

		PostPurchasePricingAnalytics postPurchasePricingAnalytics = new PostPurchasePricingAnalytics();
		postPurchasePricingAnalytics.productToCategory = getProductToCategoryMap();
		CategoryTreeNode.terminalCategoryMap = getTerminalToTopCategoryMap();
		Date startDate = sdf.parse("2013-Jan-01");
		Date endDate = sdf.parse("2013-JAN-02");
		Map<Date, Map<Integer,PostPurchaseStat>> postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate,false, 0.1, 25);
		
		PostPurchaseStat stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(25);
		assertEquals(1, stat1.totalProducts);
		assertEquals(0, stat1.dropIn7Days);		
		assertEquals(1, stat1.dropIn15Days);		
		assertEquals(1, stat1.dropIn30Days);		
		assertEquals(1, stat1.dropIn90Days);
		assertEquals(0, Double.parseDouble(stat1.addenda1), 0.1);		
		assertEquals(40, Double.parseDouble(stat1.addenda2), 0.1);		
		assertEquals(40, Double.parseDouble(stat1.addenda3), 0.1);		


		stat1 = postPurchaseStats.get( sdf.parse("2013-Jan-01")).get(-1);
		assertEquals(2, stat1.totalProducts);
		assertEquals(0, stat1.dropIn7Days);		
		assertEquals(2, stat1.dropIn15Days);		
		assertEquals(2, stat1.dropIn30Days);		
		assertEquals(2, stat1.dropIn90Days);
		assertEquals(0, Double.parseDouble(stat1.addenda1), 0.1);		
		assertEquals(60, Double.parseDouble(stat1.addenda2), 0.1);		
		assertEquals(70, Double.parseDouble(stat1.addenda3), 0.1);		
	}
	
	private HashMap<Integer, Integer> getTerminalToTopCategoryMap()
	{
		 HashMap<Integer, Integer> terminalToTopCategoryMap = new HashMap<>();
		 terminalToTopCategoryMap.put(Integer.valueOf(111), Integer.valueOf(25)); 
		// terminalToTopCategoryMap.put(Integer.valueOf(333), Integer.valueOf(25)); 
		return terminalToTopCategoryMap;
		
	}
	private HashMap<Integer, List<Integer>> getProductToCategoryMap()
	{
		HashMap<Integer, List<Integer>> productToCategoryMap = new HashMap<>();
		
		List<Integer> categories = new ArrayList<>();
		categories.add(111);
		categories.add(222);
		productToCategoryMap.put(123, categories);
		
		List<Integer> categories1 = new ArrayList<>();
		categories1.add(333);
		productToCategoryMap.put(456, categories1);

		
		return productToCategoryMap;
	}


}

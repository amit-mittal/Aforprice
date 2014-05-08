package db.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import util.ConfigParms;
import db.DbConnection;
import db.DbConnectionPool;
import entities.PriceMovementSummary;

public class PriceMovementSummaryDAOTest extends AbstractTest
{
/*
	private PriceMovementSummaryDAO priceMovementSummaryDAO = PriceMovementSummaryDAO.getInstance();
	private DbConnectionPool pool = null;
	private DbConnection conn = null;
	private Date runDate;
	private Date runDateNext;

	
	@Before
	public void setUp() throws Exception
	{
		pool = DbConnectionPool.get();
		conn = pool.getConnection();
		DataAccessObject.executeUpdate("DELETE FROM DAILY_PRICE_MOVEMENT_SUMMARY where PRODUCT_ID = 8000001");
		DataAccessObject.executeUpdate("DELETE FROM DAILY_PRICE_MOVEMENT_SUMMARY where PRODUCT_ID = 8000002");
		DataAccessObject.executeUpdate("DELETE FROM PRODUCT_CATEGORY where PRODUCT_ID = 8000001");
		DataAccessObject.executeUpdate("DELETE FROM PRODUCT_CATEGORY where PRODUCT_ID = 8000002");
		DataAccessObject.executeUpdate("DELETE FROM PRODUCT_SUMMARY where PRODUCT_ID = 8000001");
		DataAccessObject.executeUpdate("DELETE FROM PRODUCT_SUMMARY where PRODUCT_ID = 8000002");

		DataAccessObject
				.executeUpdate("insert into PRODUCT_SUMMARY (PRODUCT_ID, RETAILER_ID,PRODUCT_NAME,MODEL_NO,IMAGE_URL," 
						+ "START_DATE,END_DATE,URL,ACTIVE,TIME_MODIFIED,"
						+ "LAST_DOWNLOAD_TIME,PRICE, REVIEW_RATING,NUM_REVIEWS,BEST_SELLER_RANK,UID,DESCRIPTION)"
						+ "values (8000001, '1', '', '', '', current_date(),"
						+ "current_date(), '', 'Y', current_date(), current_date(), 0, 0, 0, 0, '','')");

		DataAccessObject
				.executeUpdate("insert into PRODUCT_SUMMARY (PRODUCT_ID, RETAILER_ID,PRODUCT_NAME,MODEL_NO,IMAGE_URL,START_DATE," 
						+ "END_DATE,URL,ACTIVE,TIME_MODIFIED,"
						+ "LAST_DOWNLOAD_TIME,PRICE, REVIEW_RATING,NUM_REVIEWS,BEST_SELLER_RANK,UID,DESCRIPTION)"
						+ "values (8000002, '1', '', '', '', current_date(), current_date(), "  
						+ "'', 'Y', current_date(), current_date(), 0, 0, 0, 0, '','')");

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DATE);
		cal.clear();
		cal.set(year, month, day);
		runDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		runDateNext = cal.getTime();
	}

	@Test
	public void testInsertPriceMovementSummary() throws SQLException
	{
		List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();

		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setProductId(8000001);
		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms1.setMaxPrice(500);
		pms1.setLastPriceChangeTime(runDate);
		pms1.setLastPrice(300);
		pms1.setLatestPrice(400);
		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms1.setAveragePrice(280);
		priceMovementSummaryInsertList.add(pms1);

		PriceMovementSummary pms2 = new PriceMovementSummary();
		pms2.setProductId(8000002);
		pms2.setMaxPriceTime(DateUtils.addDays(runDate, -20));
		pms2.setMaxPrice(700);
		pms2.setLastPriceChangeTime(new Date(System.currentTimeMillis()));
		pms2.setLastPrice(200);
		pms2.setLatestPrice(600);
		pms2.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms2.setAveragePrice(480);
		priceMovementSummaryInsertList.add(pms2);

		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);
		DataAccessObject.executeUpdate("INSERT INTO PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED) values ( 8000001, 1001111, current_date() )");
		DataAccessObject.executeUpdate("INSERT INTO PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED) values ( 8000002, 1002222, current_date() )");
		List<PriceMovementSummary> priceMovementSummaryList = priceMovementSummaryDAO
				.getPriceMovementSummaryByDate(new Date(System.currentTimeMillis()));

		boolean notFound = true;

		for (PriceMovementSummary pm : priceMovementSummaryList)
		{
			if (pm.getProductId().equals(pms1.getProductId()))
			{
				assertTrue("Last price should be 300", pm.getLastPrice() == 300);
				assertTrue("category Id doesn't match", pm.getCategoryIdList().get(0) == 1001111);
				assertTrue("category Id doesn't match", pm.getCategoryIdList().get(0) == 1001111);

				notFound = false;
			}
			if (pm.getProductId().equals(pms2.getProductId()))
			{
				assertTrue("Last price should be 200", pm.getLastPrice() == 200);
				notFound = false;
			}

		}

		assertFalse("Expected product couldn't be found", notFound);
	}

	@Test
	public void testUpdatePriceMovementSummary() throws SQLException
	{
		List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();
		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setProductId(8000001);
		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms1.setMaxPrice(500);
		pms1.setLastPriceChangeTime(runDate);
		pms1.setLastPrice(300);
		pms1.setLatestPrice(400);
		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms1.setAveragePrice(280);
		priceMovementSummaryInsertList.add(pms1);
		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);

		List<PriceMovementSummary> priceMovementSummaryUpdateList = new ArrayList<>();
		pms1.setMaxPrice(500 + 100);
		pms1.setLastPrice(300 + 100);
		pms1.setLatestPrice(400 + 100);
		pms1.setAveragePrice(280 + 100);
		priceMovementSummaryUpdateList.add(pms1);
		priceMovementSummaryDAO.updatePriceMovementSummary(priceMovementSummaryUpdateList);
		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000001, 1001111, current_date() )");
		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000002, 1002222, current_date() )");

		List<PriceMovementSummary> priceMovementSummaryList = priceMovementSummaryDAO
				.getPriceMovementSummaryByDate(new Date(System.currentTimeMillis()));
		boolean found = false;
		for (PriceMovementSummary pm : priceMovementSummaryList)
		{
			if (pm.getProductId().equals(pms1.getProductId()))
			{
				assertTrue("Last price doesn't match expected", pm.getLastPrice() == 400);
				assertTrue("Latest price doesn't match expected", pm.getLatestPrice() == 500);
				assertTrue("Average price doesn't match expected", pm.getAveragePrice() == 380);
				found = true;
			}

		}

		assertTrue("didn't find expected product", found);
		assertTrue("expected size doesn't match", priceMovementSummaryList.size() == 1);
	}

	@Test
	public void testGetPriceMovementSummaryForToday() throws SQLException
	{
		List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();

		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setProductId(8000001);
		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms1.setMaxPrice(500);
		pms1.setLastPriceChangeTime(runDate);
		pms1.setLastPrice(300);
		pms1.setLatestPrice(400);
		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms1.setAveragePrice(280);
		priceMovementSummaryInsertList.add(pms1);

		PriceMovementSummary pms2 = new PriceMovementSummary();
		pms2.setProductId(8000002);
		pms2.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms2.setMaxPrice(700);
		pms2.setLastPriceChangeTime(new Date(System.currentTimeMillis()));
		pms2.setLastPrice(200);
		pms2.setLatestPrice(600);
		if(ConfigParms.isWindows())
			pms2.setLatestPriceTime(new Date(DateUtils.addDays(new Date(System.currentTimeMillis()), PriceMovementSummaryDAO.LOOKBACK_SPAN_WINDOWS *2).getTime()));
		else
			pms2.setLatestPriceTime(new Date(DateUtils.addDays(new Date(System.currentTimeMillis()), PriceMovementSummaryDAO.LOOKBACK_SPAN * 2).getTime()));
		pms2.setAveragePrice(480);
		priceMovementSummaryInsertList.add(pms2);

		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);
		DataAccessObject.executeUpdate("INSERT INTO PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000001, 1001111, current_date() )");
		DataAccessObject.executeUpdate("INSERT INTO PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000002, 1002222, current_date() )");
		DataAccessObject.executeUpdate("INSERT INTO PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000001, 1002223, current_date() )");

		List<PriceMovementSummary> priceMovementSummaryList = priceMovementSummaryDAO.getPriceMovementSummaryForToday();
		boolean notFound = true;

		for (PriceMovementSummary pm : priceMovementSummaryList)
		{
			if (pm.getProductId().equals(pms1.getProductId()))
			{
				assertTrue("Last price should be 300", pm.getLastPrice() == 300);
				assertTrue("category Id doesn't match", pm.getCategoryIdList().contains(1001111));
				assertTrue("category Id doesn't match", pm.getCategoryIdList().contains(1002223));
				notFound = false;
			}
			if (pm.getProductId().equals(pms2.getProductId()))
				assertFalse("Didn't expect pms2", true);

		}

		assertFalse("Expected product couldn't be found", notFound);
	
	}

	@Test
	public void testGetPriceMovementSummaryByCategory() throws SQLException
	{
		List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();

		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setCalenderDay(runDate);
		pms1.setProductId(8000001);
		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms1.setMaxPrice(500);
		pms1.setLastPriceChangeTime(runDate);
		pms1.setLastPrice(300);
		pms1.setLatestPrice(400);
		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms1.setAveragePrice(280);
		priceMovementSummaryInsertList.add(pms1);

		PriceMovementSummary pms2 = new PriceMovementSummary();
		pms2.setCalenderDay(runDate);
		pms2.setProductId(8000002);
		pms2.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms2.setMaxPrice(700);
		pms2.setLastPriceChangeTime(runDate);
		pms2.setLastPrice(200);
		pms2.setLatestPrice(600);
		pms2.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms2.setAveragePrice(480);
		priceMovementSummaryInsertList.add(pms2);

		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);

		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000001, 1001111, current_date() )");
		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED)  values ( 8000002, 1002222, current_date() )");
		
		List<PriceMovementSummary> priceMovementSummaryList = priceMovementSummaryDAO
				.getPriceMovementSummaryByCategory(1001111);

		boolean found = false;
		for (PriceMovementSummary pm : priceMovementSummaryList)
		{
			if (pm.getProductId().equals(pms1.getProductId()))
			{
				assertTrue("Last Price should be 300", pm.getLastPrice() == 300);
				found = true;
			}
			if (pm.getProductId().equals(pms2.getProductId()))
				assertFalse("didn't expect pms2", true);
		}
		assertTrue("Unable to find the product in question", found);

	}
	
	@Test
	public void testgetPriceMovementUpdates() throws SQLException, InterruptedException
	{
		List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();
		long currentTime = System.currentTimeMillis();
		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setProductId(8000001);
		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms1.setMaxPrice(500);
		pms1.setLastPriceChangeTime(runDate);
		pms1.setLastPrice(300);
		pms1.setLatestPrice(400);
		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms1.setAveragePrice(280);
		priceMovementSummaryInsertList.add(pms1);
		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);
		Thread.sleep(3000);
		long nextTime = System.currentTimeMillis();

		priceMovementSummaryInsertList.clear();
		PriceMovementSummary pms2 = new PriceMovementSummary();
		pms2.setCalenderDay(runDate);
		pms2.setProductId(8000002);
		pms2.setMaxPriceTime(new Date(System.currentTimeMillis()));
		pms2.setMaxPrice(700);
		pms2.setLastPriceChangeTime(DateUtils.addDays(runDate, -20));
		pms2.setLastPrice(200);
		pms2.setLatestPrice(600);
		pms2.setLatestPriceTime(new Date(System.currentTimeMillis()));
		pms2.setAveragePrice(480);
		priceMovementSummaryInsertList.add(pms2);
		priceMovementSummaryDAO.insertPriceMovementSummary(priceMovementSummaryInsertList);

		
		
		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED) values ( 8000001, 1001111, current_date() )");
		DataAccessObject.executeUpdate("insert into PRODUCT_CATEGORY(PRODUCT_ID, CATEGORY_ID, TIME_MODIFIED) values ( 8000002, 1002222, current_date() )");
		priceMovementSummaryDAO.setPriceMovementRecorderDate(runDate);
		List<PriceMovementSummary> priceMovementSummaryList = priceMovementSummaryDAO
				.getPriceMovementUpdates(null, new Timestamp(nextTime-1000));

		boolean found = false;
		for (PriceMovementSummary pm : priceMovementSummaryList)
		{
			if (pm.getProductId().equals(pms2.getProductId()))
			{
				assertTrue("Last Price should be 200", pm.getLastPrice() == 200);
				found = true;
			}
			if (pm.getProductId().equals(pms1.getProductId()))
				assertFalse("didn't expect pms1", true);
		}
		assertTrue("Unable to find the product in question", found);
	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			DataAccessObject.executeUpdate("DELETE FROM DAILY_PRICE_MOVEMENT_SUMMARY where PRODUCT_ID = 8000001");
			DataAccessObject.executeUpdate("DELETE FROM DAILY_PRICE_MOVEMENT_SUMMARY where PRODUCT_ID = 8000002");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCT_CATEGORY where PRODUCT_ID = 8000001");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCT_CATEGORY where PRODUCT_ID = 8000002");
			DataAccessObject.executeUpdate("DELETE FROM CATEGORY where CATEGORY_ID = 1001111");
			DataAccessObject.executeUpdate("DELETE FROM CATEGORY where CATEGORY_ID = 1002222");

			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		pool.releaseConnection(conn);
	}
*/
}

package db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import entities.DailyRecon;
import entities.ProductCountRecon;

public class ReconcilerRecorderTest extends AbstractTest
{
	ReconilationDAO reconcilerRecorder;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		System.setProperty("ENVIRONMENT", "QA");
		System.setProperty("log4j.configuration", "file:/home/batchprod/dist/cur/config/log4j/log4j.xml");
		reconcilerRecorder = ReconilationDAO.getInstance();
	}

	@Test
	public void testReconcilderRecordInsert() throws DAOException
	{
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		boolean result = reconcilerRecorder.storeRec("homedepot:walloven", ts, ReconilationDAO.NAME.COUNTER,
				ReconilationDAO.TYPE.PRODUCTSPERCATEGORY, "90", Integer.parseInt("99"), ts);
		System.out.println(result);
		Timestamp fromTs = new Timestamp(ts.getTime());
		Timestamp toTs = new Timestamp(ts.getTime());
		toTs.setMinutes(ts.getMinutes() + 1);

		List<DailyRecon> dailyRecons = reconcilerRecorder.getProductCountMismatches("homedepot",
				fromTs, toTs);
		assertTrue(dailyRecons.size() != 0);
		assertEquals(dailyRecons.get(0).getActualProductCount(), 99);
		ProductCountRecon p = new ProductCountRecon(dailyRecons);
		assertTrue(p.getCatMismatch() == 1);
		assertTrue(p.getActual() == 99);
		assertTrue(p.getExpected() == 90);
	}

}

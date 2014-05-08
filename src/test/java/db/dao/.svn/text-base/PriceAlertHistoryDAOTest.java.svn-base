package db.dao;

import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import util.DateTimeUtils;
import util.build.PriceAlertHistoryBuilder;
import entities.PriceAlertHistory;

public class PriceAlertHistoryDAOTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(PriceAlertHistoryDAOTest.class);
	PriceAlertHistoryDAO dao = new PriceAlertHistoryDAO();
	PriceAlertDAO alertDao = new PriceAlertDAO();
	private static final double EPSILON_PRICE = 0.000001;
	private List<PriceAlertHistory> alerts = new ArrayList<PriceAlertHistory>(1);
	
	@Before
	public void setUp() throws Exception{	
	}
	
	@Test
	public void testPriceAlertHistory() throws SQLException{
		String currTimeStr = String.valueOf(System.currentTimeMillis());
		String uniqIdStr = currTimeStr.substring(currTimeStr.length() - 5, currTimeStr.length());
		int uniqId = Integer.parseInt(uniqIdStr);
		try{
			logger.info("===============ADDING ROW TO THE PRICE ALERT HISTORY TABLE===============");
			Date t1 = new Date();
			PriceAlertHistoryBuilder alertHistBuilder1=new PriceAlertHistoryBuilder();
			alertHistBuilder1.alertId=uniqId;
			alertHistBuilder1.timeSent=t1;
			alertHistBuilder1.notificationPrice=100;
			PriceAlertHistory alertHist1=alertHistBuilder1.build();
			dao.addPriceAlertHistory(alertHist1);
			alerts.add(alertHist1);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID="+alertHistBuilder1.alertId, alerts);
	
			logger.info("===============ADDING SECOND ROW TO THE PRICE ALERT HISTORY TABLE===============");
			Date t2 = new Date();
			PriceAlertHistoryBuilder alertHistBuilder2=new PriceAlertHistoryBuilder();
			alertHistBuilder2.alertId=uniqId;
			alertHistBuilder2.timeSent=t2;
			alertHistBuilder2.notificationPrice=110.62;
			PriceAlertHistory alertHist2=alertHistBuilder2.build();
			dao.addPriceAlertHistory(alertHist2);
			alerts.add(alertHist2);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID="+alertHistBuilder2.alertId, alerts);
			
			logger.info("===============ADDING THIRD ROW TO THE PRICE ALERT HISTORY TABLE===============");
			Date t3 = new Date();
			PriceAlertHistoryBuilder alertHistBuilder3=new PriceAlertHistoryBuilder();
			alertHistBuilder3.alertId=uniqId;
			alertHistBuilder3.timeSent=t3;
			alertHistBuilder3.notificationPrice=72.27;
			PriceAlertHistory alertHist3=alertHistBuilder3.build();
			dao.addPriceAlertHistory(alertHist3);
			alerts.add(alertHist3);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID="+alertHistBuilder3.alertId, alerts);
			
			logger.info("===============ADDING DIFFERENT ALERT ID ROW TO THE PRICE ALERT HISTORY TABLE===============");
			alerts.clear();
			Date t4 = new Date();
			PriceAlertHistoryBuilder alertHistBuilder4=new PriceAlertHistoryBuilder();
			alertHistBuilder4.alertId=uniqId+1;
			alertHistBuilder4.timeSent=t4;
			alertHistBuilder4.notificationPrice=22.22;
			PriceAlertHistory alertHist4=alertHistBuilder4.build();
			dao.addPriceAlertHistory(alertHist4);
			alerts.add(alertHist4);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID="+alertHistBuilder4.alertId, alerts);
		
			logger.info("===============GETTING RESULT SET FROM THE PRICE ALERT HISTORY TABLE===============");
			alerts=dao.getPriceAlertHistory(uniqId);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID="+uniqId, alerts);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			cleanUp(uniqId);
			cleanUp(uniqId+1);
		}
	}
	
	private void assertExpected(String qryActual, List<PriceAlertHistory> expected){
		ResultSet rs=null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				_assertTrue(!rs.next());
				return;
			}
			
			List<PriceAlertHistory> alerts = new ArrayList<PriceAlertHistory>();
			for(int index=0 ; index<expected.size(); ++index){
				_assertTrue(rs.next());
				PriceAlertHistory alertHist = dao.getPriceAlertHistory(rs);
				alerts.add(alertHist);
			}
	
			_assertEquals(expected.size(), alerts.size());
			for(PriceAlertHistory alertHist : alerts){
				boolean found = false;
				for(PriceAlertHistory expectedAlertHist : expected){
					_assertTrue(alertHist.getAlertId() == expectedAlertHist.getAlertId());
					if(Math.abs(alertHist.getNotificationPrice() - expectedAlertHist.getNotificationPrice())<=EPSILON_PRICE){
						if(Math.abs(DateTimeUtils.diff(alertHist.getTimeSent(), expectedAlertHist.getTimeSent(), TimeUnit.SECONDS)) <= 1){
							found = true;
						}
					}
				}
				_assertTrue(found);
			}
			
			_assertTrue(!rs.next());
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
	
	private void cleanUp(int alertId){
		ResultSet rs=null;
		try{
			logger.info("===============CLEANING UP THE DATABASE===============");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID=" + alertId);
		}
		catch(Exception e){
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
}

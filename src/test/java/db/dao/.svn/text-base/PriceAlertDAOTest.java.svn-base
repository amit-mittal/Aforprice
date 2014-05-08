package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import thrift.genereated.pricealert.PriceAlertThrift;
import util.AbstractTest;
import util.build.PriceAlertBuilder;
import entities.PriceAlert;
import entities.PriceAlertType;

public class PriceAlertDAOTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(PriceAlertDAOTest.class);
	PriceAlertDAO dao = new PriceAlertDAO();
	
	@Before
	public void setUp() throws Exception{
	}
	
	//@Test
	public void testPriceAlert() throws SQLException{
		long uniqId = System.currentTimeMillis();
		try{
			logger.info("===============ADDING PRICE ALERT TO THE DATABASE===============");
			Date t0 = new Date();
			PriceAlertBuilder alertBuilder=new PriceAlertBuilder();
			alertBuilder.alertId=dao.getUniqueAlertId();
			alertBuilder.emailId="test1@gmail.com"+uniqId;
			alertBuilder.productId=1001;
			alertBuilder.alertPrice=99.05;
			alertBuilder.timeModified=t0;
			alertBuilder.alertStartTime=t0;
			alertBuilder.alertEndTime=t0;
			alertBuilder.alertType=PriceAlertType.ALERT_WHEN_AT_PRICE;
			alertBuilder.active=true;
			PriceAlert alert1=alertBuilder.build();
			dao.addPriceAlert(alert1);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder.alertId, alert1);
			
			logger.info("===============UPDATING EXISTING PRICE ALERT IN THE DATABASE===============");
			alertBuilder.alertPrice=110;
			alertBuilder.alertType=PriceAlertType.ALERT_WHEN_AT_PRICE;
			alert1=alertBuilder.build();
			dao.updatePriceAlert(alert1);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder.alertId, alert1);
			
			logger.info("===============GETTING PRICE ALERT FROM THE DATABASE===============");
			PriceAlert alert3 = dao.getPriceAlert(alertBuilder.alertId);
			assertTrue(alert3.equals(alert1));
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder.alertId, alert3);
			
			logger.info("===============REMOVING PRICE ALERT FROM THE DATABASE===============");
			boolean removed = dao.updatePriceAlertStatus(alertBuilder.alertId, false);
			alertBuilder.active=false;
			alert1=alertBuilder.build();
			_assertTrue(removed);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder.alertId, alert1);
			
			logger.info("===============ADDING ANOTHER PRICE ALERT TO THE DATABASE===============");
			Date t2 = new Date();
			PriceAlertBuilder alertBuilder2=new PriceAlertBuilder();
			alertBuilder2.alertId=dao.getUniqueAlertId();
			alertBuilder2.emailId="test2@gmail.com"+uniqId;
			alertBuilder2.productId=1601;
			alertBuilder2.alertPrice=1990.05;
			alertBuilder2.timeModified=t2;
			alertBuilder2.alertStartTime=t2;
			alertBuilder2.alertEndTime=t2;
			alertBuilder2.alertType=PriceAlertType.ALERT_WHEN_AT_PRICE;
			alertBuilder2.active=false;
			PriceAlert alert2=alertBuilder2.build();
			dao.addPriceAlert(alert2);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder2.alertId, alert2);
			
			logger.info("===============ADDING ANOTHER PRICE ALERT TO THE DATABASE===============");
			Date t4 = new Date(System.currentTimeMillis() + 60*60*1000);
			PriceAlertBuilder alertBuilder4=new PriceAlertBuilder();
			alertBuilder4.alertId=dao.getUniqueAlertId();
			alertBuilder4.emailId="test3@gmail.com"+uniqId;
			alertBuilder4.productId=601;
			alertBuilder4.alertPrice=90.05;
			alertBuilder4.timeModified=t4;
			alertBuilder4.alertStartTime=t4;
			alertBuilder4.alertEndTime=t4;
			alertBuilder4.alertType=PriceAlertType.ALERT_WHEN_PRICE_DROPS;
			alertBuilder4.active=true;
			PriceAlert alert4=alertBuilder4.build();
			dao.addPriceAlert(alert4);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder4.alertId, alert4);
						
			logger.info("===============GETTING ALL ACTIVE ALERTS FROM THE DATABASE===============");
			List<PriceAlert> activeAlerts = dao.getAllActiveAlerts();
			_assertEquals(1, activeAlerts.size());
			assertAlerts(alert4, activeAlerts.get(0));
			
			logger.info("===============GETTING UPDATED ALERTS FROM THE DATABASE===============");
			List<PriceAlert> updatedAlerts = dao.getUpdatedPriceAlerts(new Timestamp(System.currentTimeMillis() + 60*1000));
			_assertEquals(1, updatedAlerts.size());
			assertAlerts(alert4, updatedAlerts.get(0));
			
			logger.info("===============GETTING ACTIVE ALERTS FROM THE DATABASE FROM EMAIL ID AND PRODUCT ID===============");
			PriceAlert alert5 = dao.getActivePriceAlert("test3@gmail.com"+uniqId, 601);
			assertAlerts(alert4, alert5);
			
			logger.info("===============ADDING ANOTHER PRICE ALERT TO THE DATABASE===============");
			Date t5 = new Date(System.currentTimeMillis() + 60*60*1000);
			PriceAlertBuilder alertBuilder5=new PriceAlertBuilder();
			alertBuilder5.alertId=dao.getUniqueAlertId();
			alertBuilder5.emailId="test3@gmail.com"+uniqId;
			alertBuilder5.productId=1601;
			alertBuilder5.alertPrice=160.05;
			alertBuilder5.timeModified=t5;
			alertBuilder5.alertStartTime=t5;
			alertBuilder5.alertEndTime=t5;
			alertBuilder5.alertType=PriceAlertType.ALERT_WHEN_PRICE_DROPS;
			alertBuilder5.active=true;
			PriceAlert alert6=alertBuilder5.build();
			dao.addPriceAlert(alert6);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alertBuilder5.alertId, alert6);
			
			logger.info("===============GETTING ALL ACTIVE ALERTS FROM THE DATABASE FROM EMAIL ID===============");
			List<PriceAlertThrift> alertList = dao.getAllActivePriceAlertsOfUser("test3@gmail.com"+uniqId);
			_assertEquals(2, alertList.size());
			assertAlertsList(alert4, alertList);
			assertAlertsList(alert6, alertList);
			
			logger.info("===============VERIFYING PRICE ALERT EXISTING IN THE DATABASE===============");
			boolean verified = dao.updatePriceAlertStatus(alert1.getAlertId(), true);
			alert1.setActive(true);
			_assertTrue(verified);
			assertExpected("SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID="+alert1.getAlertId(), alert1);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			cleanUp(uniqId);
		}
	}
	
	private void assertExpected(String qryActual, PriceAlert expected){
		ResultSet rs=null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				_assertTrue(!rs.next());
				return;
			}
			_assertTrue(rs.next());
			PriceAlert alert = dao.getPriceAlert(rs);
			assertAlerts(expected, alert);
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
	
	private void assertAlerts(PriceAlert expected, PriceAlert alert){
		_assertEquals(expected.getAlertId(), alert.getAlertId());
		_assertEquals(expected.getEmailId(), alert.getEmailId());
		_assertEquals(expected.getProductId(), alert.getProductId());
		_assertEquals(expected.getAlertPrice(), alert.getAlertPrice());
//		_assertTime(expected.getTimeModified().getTime(), alert.getTimeModified().getTime());
//		_assertTime(expected.getAlertStartTime().getTime(), alert.getAlertStartTime().getTime());
//		_assertTime(expected.getAlertEndTime().getTime(), alert.getAlertEndTime().getTime());
		_assertEquals(expected.getAlertType().toString(), alert.getAlertType().toString());
		_assertTrue(expected.isActive() == alert.isActive());
	}
	
	private void assertAlerts(PriceAlert expected, PriceAlertThrift alert){
		_assertEquals(expected.getAlertId(), alert.getAlertId());
		_assertEquals(expected.getEmailId(), alert.getEmailId());
		_assertEquals(expected.getProductId(), alert.getProductId());
		_assertEquals(expected.getAlertPrice(), alert.getAlertPrice());
//		_assertTime(expected.getTimeModified().getTime(), alert.getTimeModified().getTime());
//		_assertTime(expected.getAlertStartTime().getTime(), alert.getAlertStartTime().getTime());
//		_assertTime(expected.getAlertEndTime().getTime(), alert.getAlertEndTime().getTime());
		_assertEquals(expected.getAlertType().toString(), alert.getAlertType().toString());
		_assertTrue(expected.isActive() == alert.isAlertActive());
	}
	
	private void assertAlertsList(PriceAlert expected, List<PriceAlertThrift> actualList){
		boolean flag = false;
		for(PriceAlertThrift alert : actualList){
			if(expected.getAlertId() == alert.getAlertId()){
				flag = true;
				assertAlerts(expected, alert);
			}
		}
		_assertTrue(flag);
	}
	
	private void cleanUp(long uniqId){
		ResultSet rs=null;
		try{
			logger.info("===============CLEANING UP THE DATABASE AFTER TEST===============");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.PRICE_ALERT WHERE EMAIL_ID LIKE '%" + uniqId + "%'");
		}
		catch(Exception e){
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
}

package thrift.servers;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.mockito.Matchers.*;

import thrift.genereated.pricealert.PriceAlertException;
import thrift.genereated.pricealert.PriceAlertThrift;
import thrift.genereated.pricealert.PriceAlertTypeThrift;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.PriceAlertBuilder;
import util.build.UserBuilder;
import db.dao.DAOException;
import db.dao.PriceAlertDAO;
import db.dao.PriceAlertServerFactory;
import db.dao.UserDAO;
import entities.PriceAlert;
import entities.PriceAlertType;
import entities.User;

public class PriceAlertManagerTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(PriceAlertManager.class);
	private PriceAlertDAO mockAlertDAO = mock(PriceAlertDAO.class);
	private UserDAO mockUserDAO = mock(UserDAO.class);
	private PriceAlertManager alertManager = new PriceAlertManager();
	
	@Before
	public void setUp(){
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
		setupMock();
	}
	
	public void setupMock(){
		PriceAlertServerFactory.getInstance().setPriceAlertDAO(mockAlertDAO);
		PriceAlertServerFactory.getInstance().setUserDAO(mockUserDAO);
//		when(mockUserDAO.geany(String.class))).then
	}
	
	@Test
	public void testAddPriceAlertThrift1() throws SQLException, DAOException, TException{
		try{
			logger.info("======== MAKING THRIFT PRICE ALERT OBJECT ========");
			PriceAlertThrift alert1 = new PriceAlertThrift();
			alert1.emailId = "test1@price_alert_manager.com";
			alert1.alertId = 1239;
			alert1.alertPrice = 100.84;
			alert1.alertType = PriceAlertTypeThrift.ALERT_WHEN_AT_PRICE;
			when(mockAlertDAO.getActivePriceAlert(alert1.getEmailId(), alert1.getProductId())).thenReturn(null);
			
			logger.info("======== ADDDING PRICE ALERT - USER NOT EXISTING NOT REGISTERED NOT ACTIVE ========");
			User usr1 = new UserBuilder().emailId(alert1.getEmailId()).active(false).newsletter(false).registered(false).build();
			
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(false);
			when(mockUserDAO.getUser(alert1.getEmailId())).thenReturn(usr1);
			when(mockAlertDAO.addPriceAlert(any(PriceAlert.class))).thenReturn(1);
			
			boolean added1 = alertManager.addPriceAlertThrift(alert1);
			PriceAlert actual1 = alertManager.convertPriceAlertThriftToPriceAlert(alert1, usr1);
			actual1.setAlertId(mockAlertDAO.addPriceAlert(actual1));
//			verify(mockUserDAO, times(1)).addUser(any(User.class));
			
			PriceAlert expected1 = new PriceAlertBuilder().alertId(1).emailId(alert1.getEmailId()).productId(alert1.getProductId()).
					alertPrice(alert1.getAlertPrice()).alertType(PriceAlertType.ALERT_WHEN_AT_PRICE).active(true).build();
			
			_assertTrue(added1);
			assertAlert(expected1, actual1);
			
			logger.info("======== ADDDING PRICE ALERT - USER EXISTING NOT REGISTERED NOT ACTIVE ========");
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(true);
			when(mockAlertDAO.addPriceAlert(any(PriceAlert.class))).thenReturn(2);
			
			boolean added2 = alertManager.addPriceAlertThrift(alert1);
			PriceAlert actual2 = alertManager.convertPriceAlertThriftToPriceAlert(alert1, usr1);
			actual2.setAlertId(mockAlertDAO.addPriceAlert(actual2));
			
			PriceAlert expected2 = new PriceAlertBuilder().alertId(2).emailId(alert1.getEmailId()).productId(alert1.getProductId()).
					alertPrice(alert1.getAlertPrice()).alertType(PriceAlertType.ALERT_WHEN_AT_PRICE).active(true).build();
			
			_assertTrue(added2);
//			verify(mockUserDAO, times(1)).addUser(any(User.class));
			assertAlert(expected2, actual2);
			
			logger.info("======== ADDDING PRICE ALERT - USER EXISTING REGISTERED ACTIVE ========");
			User usr3 = new UserBuilder().emailId(alert1.getEmailId()).active(true).newsletter(false).registered(true).build();
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(true);
			when(mockAlertDAO.addPriceAlert(any(PriceAlert.class))).thenReturn(3);
			when(mockUserDAO.getUser(alert1.getEmailId())).thenReturn(usr3);
			
			boolean added4 = alertManager.addPriceAlertThrift(alert1);
			PriceAlert actual3 = alertManager.convertPriceAlertThriftToPriceAlert(alert1, usr3);
			actual3.setAlertId(mockAlertDAO.addPriceAlert(actual3));
			
			PriceAlert expected3 = new PriceAlertBuilder().alertId(3).emailId(alert1.getEmailId()).productId(alert1.getProductId()).
					alertPrice(alert1.getAlertPrice()).alertType(PriceAlertType.ALERT_WHEN_AT_PRICE).active(true).build();
			
			_assertTrue(added4);
//			verify(mockUserDAO, times(1)).addUser(any(User.class));
			assertAlert(expected3, actual3);
		
			/* we don't check these things now
			logger.info("======== ADDDING PRICE ALERT - USER EXISTING REGISTERED NOT ACTIVE ========");
			User usr2 = new UserBuilder().emailId(alert1.getEmailId()).active(false).newsletter(false).registered(true).build();
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(true);
			when(mockUserDAO.getUser(alert1.getEmailId())).thenReturn(usr2);
			
			boolean added3 = alertManager.addPriceAlertThrift(alert1);
			fail("Expecting PriceAlertException as no alert has been added");
			*/
		} catch(PriceAlertException e){
			logger.info("Got PriceAlertException: "+e.error_msg);
		}
	}
	
	@Test
	public void testAddPriceAlertThrift2() throws SQLException, DAOException, TException{
		try{
			logger.info("======== MAKING THRIFT PRICE ALERT OBJECT ========");
			PriceAlertThrift alert1 = new PriceAlertThrift();
			alert1.emailId = "test1@price_alert_manager.com";
			alert1.alertId = 1239;
			alert1.alertPrice = 100.84;
			alert1.alertType = PriceAlertTypeThrift.ALERT_WHEN_AT_PRICE;
			
			logger.info("======== ADDDING PRICE ALERT - USER EXISTING NOT REGISTERED NOT ACTIVE ========");
			User usr1 = new UserBuilder().emailId(alert1.getEmailId()).active(false).newsletter(false).registered(false).build();
			
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(true);
			when(mockUserDAO.getUser(alert1.getEmailId())).thenReturn(usr1);
			when(mockAlertDAO.addPriceAlert(any(PriceAlert.class))).thenReturn(1);
			when(mockAlertDAO.getActivePriceAlert(alert1.getEmailId(), alert1.getProductId())).thenReturn(null);
			
			boolean added1 = alertManager.addPriceAlertThrift(alert1);
			PriceAlert actual1 = alertManager.convertPriceAlertThriftToPriceAlert(alert1, usr1);
			actual1.setAlertId(mockAlertDAO.addPriceAlert(actual1));
			
			PriceAlert expected1 = new PriceAlertBuilder().alertId(1).emailId(alert1.getEmailId()).productId(alert1.getProductId()).
					alertPrice(alert1.getAlertPrice()).alertType(PriceAlertType.ALERT_WHEN_AT_PRICE).active(true).build();
			
			_assertTrue(added1);
			assertAlert(expected1, actual1);
			
			logger.info("======== ADDDING PRICE ALERT FOR SAME PRODUCT ID - USER EXISTING NOT REGISTERED NOT ACTIVE ========");
			when(mockUserDAO.isEmailIdExisting(alert1.getEmailId())).thenReturn(true);
			when(mockAlertDAO.addPriceAlert(any(PriceAlert.class))).thenReturn(2);
			actual1.setActive(true);
			when(mockAlertDAO.getActivePriceAlert(alert1.getEmailId(), alert1.getProductId())).thenReturn(actual1);
			
			boolean added2 = alertManager.addPriceAlertThrift(alert1);
			fail("Expecting PriceAlertException as no alert has been added");
		} catch(PriceAlertException e){
			logger.info("Got PriceAlertException: "+e.error_msg);
		}
	}
	
	public void assertAlert(PriceAlert expected, PriceAlert actual){
		_assertEquals(expected.getAlertId(), actual.getAlertId());
		_assertEquals(expected.getEmailId(), actual.getEmailId());
		_assertEquals(expected.getProductId(), actual.getProductId());
		_assertEquals(expected.getAlertPrice(), actual.getAlertPrice());
		_assertEquals(expected.getAlertType().toString(), actual.getAlertType().toString());
		_assertTrue(expected.isActive() == actual.isActive());
	}
}

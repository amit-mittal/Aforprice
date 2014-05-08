package products;

import static entities.Retailer.TESTRETAILER;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.PriceAlertBuilder;
import util.build.ProductSummaryBuilder;
import util.build.UserBuilder;
import db.dao.DAOException;
import db.dao.PriceAlertDAO;
import db.dao.PriceAlertHistoryDAO;
import db.dao.PriceAlertServerFactory;
import db.dao.ProductsDAO;
import db.dao.UserDAO;
import entities.PriceAlert;
import entities.PriceAlertHistory;
import entities.PriceAlertType;
import entities.ProductSummary;
import entities.Retailer;
import entities.User;


public class PriceAlertServerTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(PriceAlertServerTest.class);
	private List<PriceAlert> updates = new ArrayList<PriceAlert>();
	private PriceAlertServer alertServer;
	private PriceAlertDAO mockAlertDAO = mock(PriceAlertDAO.class);
	private PriceAlertHistoryDAO mockAlertHistDAO = mock(PriceAlertHistoryDAO.class);
	private ProductsDAO mockProdDAO = mock(ProductsDAO.class);
	private UserDAO mockUserDAO = mock(UserDAO.class);
	int expNoOfAlertsInProductMap;
	private static final Long TIMEDIFF = 30*24*60*60*1000L;//1 month
	
	@Before
	public void setUp(){
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
		setupMock();
		alertServer = new PriceAlertServer(); //create this after mock
	}
	
	public void setupMock(){
		PriceAlertServerFactory.getInstance().setPriceAlertDAO(mockAlertDAO);
		PriceAlertServerFactory.getInstance().setPriceAlertHistDAO(mockAlertHistDAO);
		PriceAlertServerFactory.getInstance().setProdDAO(mockProdDAO);
		PriceAlertServerFactory.getInstance().setUserDAO(mockUserDAO);
	}
	
	@Test
	public void testUpdateAlerts() throws SQLException{
		logger.info("======== ADD PRICE ALERT - ADDING VERY FIRST PRICE ALERT ========");
		Date t1 = new Date();
		PriceAlert alert1=new PriceAlertBuilder(1, "test1@gmail.com", 1001, 99.05, t1, t1, t1, PriceAlertType.ALERT_WHEN_AT_PRICE, true).build();
		updates.add(alert1);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 1;
		assertAlert(alert1);
		
		logger.info("======== ADDING SECOND PRICE ALERT - SAME PRODUCT ID ========");
		Date t2 = new Date();
		PriceAlert alert2=new PriceAlertBuilder(2, "test2@gmail.com", 1001, 199.05, t2, t2, t2, PriceAlertType.ALERT_WHEN_PRICE_DROPS, true).build();
		updates.clear();
		updates.add(alert2);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 2;
		assertAlert(alert2);
		
		logger.info("======== ADDING SECOND PRICE ALERT - DIFFERENCT PRODUCT ID ========");
		Date t3 = new Date();
		PriceAlert alert3=new PriceAlertBuilder(3, "test3@gmail.com", 1201, 1299.05, t3, t3, t3, PriceAlertType.ALERT_WHEN_PRICE_DROPS, true).build();
		updates.clear();
		updates.add(alert3);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 1;
		assertAlert(alert3);
		
		logger.info("======== UPDATE PRICE ALERT - ALERT PRICE IS CHANGED ========");
		alert3.setAlertPrice(2001.34);
		updates.clear();
		updates.add(alert3);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 1;
		assertAlert(alert3);
		
		logger.info("======== PRICE ALERT BECOMES INACTIVE - ALERT_ID_PRODUCT MAP BECOMES EMPTY ========");
		alert3.setActive(false);
		updates.clear();
		updates.add(alert3);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 0;
		assertAlert(alert3);
		
		logger.info("======== PRICE ALERT BECOMES INACTIVE - ALERT_ID_PRODUCT MAP IS NON EMPTY ========");
		alert2.setActive(false);
		updates.clear();
		updates.add(alert2);
		alertServer.updateAlerts(updates);
		expNoOfAlertsInProductMap = 1;
		assertAlert(alert2);
	}
	
	@Test
	public void testPriceAlertServer() throws DAOException, SQLException, ParseException{
		logger.info("======== MOCK TESTING - CREATING USERS ========");
		User user1 = new UserBuilder().userId(1).emailId("test1@gmail.com").name("user1").password("pass1").country("country1").
				phone("011").lastLoggedIn(new Date()).active(true).newsletter(false).registered(true).build();
		User user2 = new UserBuilder().userId(2).emailId("test2@gmail.com").name("user2").password("pass2").country("country2").
				phone("011").lastLoggedIn(new Date()).active(false).newsletter(false).registered(false).build();
		User user3 = new UserBuilder().userId(3).emailId("test3@gmail.com").name("user3").password("pass3").country("country3").
				phone("011").lastLoggedIn(new Date()).active(true).newsletter(false).registered(false).build();
		User user4 = new UserBuilder().userId(4).emailId("test4@gmail.com").name("user4").password("pass4").country("country4").
				phone("011").lastLoggedIn(new Date()).active(false).newsletter(false).registered(false).build();
		User user5 = new UserBuilder().userId(5).emailId("test5@gmail.com").name("user5").password("pass5").country("country5").
				phone("011").lastLoggedIn(new Date()).active(false).newsletter(false).registered(true).build();
		
		when(mockUserDAO.getUser("test1@gmail.com")).thenReturn(user1);
		when(mockUserDAO.getUser("test2@gmail.com")).thenReturn(user2);
		when(mockUserDAO.getUser("test3@gmail.com")).thenReturn(user3);
		when(mockUserDAO.getUser("test4@gmail.com")).thenReturn(user4);
		when(mockUserDAO.getUser("test5@gmail.com")).thenReturn(user5);
		
		logger.info("======== MOCK TESTING - CREATING PRODUCTS ========");
		ProductSummary prod1= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(10000).productId(1001).price(1.0).downloadTime(new Date()).build();
		ProductSummary prod2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product2", "http://www.testretailer.com/16419432", "www.image2.com", "model2").
				categoryId(10000).productId(1201).price(1.0).downloadTime(new Date()).build();
		ProductSummary prod3= new ProductSummaryBuilder(TESTRETAILER.getId(), "product3", "http://www.testretailer.com/16419432", "www.image3.com", "model3").
				categoryId(10000).productId(1301).price(1.0).downloadTime(new Date()).build();
		ProductSummary prod4= new ProductSummaryBuilder(TESTRETAILER.getId(), "product4", "http://www.testretailer.com/16419432", "www.image4.com", "model4").
				categoryId(10000).productId(1401).price(1.0).downloadTime(new Date()).build();
		
		when(mockProdDAO.getProductSummaryByProductId(1001)).thenReturn(prod1);
		when(mockProdDAO.getProductSummaryByProductId(1201)).thenReturn(prod2);
		when(mockProdDAO.getProductSummaryByProductId(1301)).thenReturn(prod3);
		when(mockProdDAO.getProductSummaryByProductId(1401)).thenReturn(prod4);
		when(mockProdDAO.getProductMaxModifiedTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
		logger.info("======== MOCK TESTING - CREATING HASHMAPS ========");
		List<PriceAlert> activeAlerts = new ArrayList<PriceAlert>();
		
		Date t0 = new Date(System.currentTimeMillis()-TIMEDIFF);//before 1 month time
		PriceAlert alert1 = new PriceAlertBuilder(1, "test1@gmail.com", 1001, 99.05, t0, t0, t0, PriceAlertType.ALERT_WHEN_AT_PRICE, true).build();
		PriceAlert alert2 = new PriceAlertBuilder(2, "test2@gmail.com", 1001, 199.05, t0, t0, t0, PriceAlertType.ALERT_WHEN_PRICE_DROPS, true).build();
		PriceAlert alert3 = new PriceAlertBuilder(3, "test3@gmail.com", 1201, 1299.05, t0, t0, t0, PriceAlertType.ALERT_WHEN_PRICE_DROPS, true).build();
		PriceAlert alert4 = new PriceAlertBuilder(4, "test4@gmail.com", 1301, 999.05, t0, t0, t0, PriceAlertType.ALERT_WHEN_AT_PRICE, true).build();
		PriceAlert alert5 = new PriceAlertBuilder(5, "test5@gmail.com", 1401, 100, t0, t0, t0, PriceAlertType.ALERT_WHEN_AT_PRICE, true).build();
		
		activeAlerts.add(alert1);
		activeAlerts.add(alert2);
		activeAlerts.add(alert3);
		activeAlerts.add(alert4);
		activeAlerts.add(alert5);
		
		when(mockAlertDAO.getAllActiveAlerts()).thenReturn(activeAlerts);
		
		PriceAlertHistory alertHist1_1 = new PriceAlertHistory(1, 1, new Date(System.currentTimeMillis() - TIMEDIFF/2), 93.04);
		PriceAlertHistory alertHist1_2 = new PriceAlertHistory(2, 1, new Date(System.currentTimeMillis() - TIMEDIFF/3), 94.09);
		List<PriceAlertHistory> alertHistList1 = new ArrayList<PriceAlertHistory>();
		alertHistList1.add(alertHist1_1);
		alertHistList1.add(alertHist1_2);
		when(mockAlertHistDAO.getPriceAlertHistory(1)).thenReturn(alertHistList1);
		
		PriceAlertHistory alertHist2_1 = new PriceAlertHistory(3, 2, new Date(System.currentTimeMillis() - TIMEDIFF/2), 193.04);
		List<PriceAlertHistory> alertHistList2 = new ArrayList<PriceAlertHistory>();
		alertHistList2.add(alertHist2_1);
		when(mockAlertHistDAO.getPriceAlertHistory(2)).thenReturn(alertHistList2);
		
		PriceAlertHistory alertHist3_1 = new PriceAlertHistory(4, 3, new Date(System.currentTimeMillis() - TIMEDIFF/3), 1193.04);
		List<PriceAlertHistory> alertHistList3 = new ArrayList<PriceAlertHistory>();
		alertHistList3.add(alertHist3_1);
		when(mockAlertHistDAO.getPriceAlertHistory(3)).thenReturn(alertHistList3);
		
		List<PriceAlertHistory> alertHistList4 = new ArrayList<PriceAlertHistory>();
		when(mockAlertHistDAO.getPriceAlertHistory(4)).thenReturn(alertHistList4);
		
		PriceAlertHistory alertHist5_1 = new PriceAlertHistory(5, 5, new Date(System.currentTimeMillis() - TIMEDIFF/3), 110.04);
		List<PriceAlertHistory> alertHistList5 = new ArrayList<PriceAlertHistory>();
		alertHistList5.add(alertHist5_1);
		when(mockAlertHistDAO.getPriceAlertHistory(5)).thenReturn(alertHistList5);
		
		alertServer.getAlertConfigurationAndHistory();
		
		assertAlertHistory(1, alertHist1_2);
		assertAlertHistory(2, alertHist2_1);
		assertAlertHistory(3, alertHist3_1);
		assertAlertHistory(4, null);
		assertAlertHistory(5, alertHist5_1);
		
		logger.info("======== MOCK TESTING - FIRST EMAIL WILL BE SENT TO A NEW ALERT ========");
		List<ProductSummary> updatedProducts = new ArrayList<ProductSummary>();
		when(mockProdDAO.getUpdatedProducts(Matchers.anyString(), Matchers.any(Timestamp.class))).thenReturn(updatedProducts);
		
		ProductSummary update1 = createProductUpdate(1301, 900.06);
		updatedProducts.add(update1);
		
		
		List<ProductSummary> updates = alertServer.getProductUpdates();
		alertServer.processProductUpdate(updates);

		assertSendEmail(4, update1);
		verify(mockAlertHistDAO, times(1)).addPriceAlertHistory(any(PriceAlertHistory.class));
		
		logger.info("======== MOCK TESTING - ADDING PRODUCT PRICE UPDATE FOR NON-EXISTING PRODUCT ID ALERT ========");
		updatedProducts.clear();
		ProductSummary update2 = createProductUpdate(1234, 191.06);
		updatedProducts.add(update2);
		updates = alertServer.getProductUpdates();
		alertServer.processProductUpdate(updates);
		_assertTrue(alertServer.getProductIdAlertMap().get(1234)==null);
		verify(mockAlertHistDAO, times(1)).addPriceAlertHistory(any(PriceAlertHistory.class));
		
		logger.info("======== MOCK TESTING - SEND EMAIL TO BOTH ALERTS ========");
		updatedProducts.clear();
		ProductSummary update3 = createProductUpdate(1001, 91.06);
		updatedProducts.add(update3);
		updates = alertServer.getProductUpdates();
		alertServer.processProductUpdate(updates);
		assertSendEmail(1, update3);
		assertSendEmail(2, update3);
		verify(mockAlertHistDAO, times(3)).addPriceAlertHistory(any(PriceAlertHistory.class));
		
		logger.info("======== MOCK TESTING - MULTIPLE PRODUCTS UPDATES ========");
		updatedProducts.clear();
		ProductSummary update4 = createProductUpdate(1001, 93.46);
		updatedProducts.add(update4);
		
		ProductSummary update5 = createProductUpdate(1201, 1300.46);
		updatedProducts.add(update5);
		
		updates = alertServer.getProductUpdates();
		alertServer.processProductUpdate(updates);
		assertSendEmail(1, update4);
		assertSendEmail(2, update3);
		assertSendEmail(3, null);
		verify(mockAlertHistDAO, times(4)).addPriceAlertHistory(any(PriceAlertHistory.class));
		
		logger.info("======== MOCK TESTING - NOT SENDING EMAIL IF USER IS REGISTERED AND INACTIVE ========");
		updatedProducts.clear();
		ProductSummary update6 = createProductUpdate(1401, 91.06);
		updatedProducts.add(update6);
		
		updates = alertServer.getProductUpdates();
		alertServer.processProductUpdate(updates);
		assertSendEmail(5, null);
		verify(mockAlertHistDAO, times(4)).addPriceAlertHistory(any(PriceAlertHistory.class));
		
		logger.info("======== MOCK TESTING - FINAL ASSERTIONS ========");
		assertSendEmail(1, update4);
		assertSendEmail(2, update3);
		assertAlertHistory(3, alertHist3_1);
		assertSendEmail(4, update1);
		assertSendEmail(5, null);
	}
	
	private ProductSummary createProductUpdate(int productId, double price){
		ProductSummaryBuilder b = new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "Product"+productId, Retailer.TESTRETAILER.getLink() + productId,
				"http://www.images.com", "Model 1").categoryId(100).categoryName("TestCategory").price(price).desc("Desc 1").reviewRating(2.0).numReviews(100).
				salesRank(100).downloadTime(new Date(System.currentTimeMillis() + TIMEDIFF)).productId(productId);
		return b.build();
	}

	public void assertAlert(PriceAlert expected){
		List<PriceAlert> prodAlerts = alertServer.getProductIdAlertMap().get(expected.getProductId());
		
		_assertEquals(expNoOfAlertsInProductMap, prodAlerts.size());
		if(expected.isActive()){
			_assertTrue(alertServer.getAlertIdAlertMap().get(expected.getAlertId())!=null);
			_assertTrue(alertServer.getProductIdAlertMap().get(expected.getProductId())!=null);
			
			PriceAlert actual = alertServer.getAlertIdAlertMap().get(expected.getAlertId());
			_assertEquals(expected.getAlertId(), actual.getAlertId());
			_assertEquals(expected.getEmailId(), actual.getEmailId());
			_assertEquals(expected.getProductId(), actual.getProductId());
			_assertEquals(expected.getAlertPrice(), actual.getAlertPrice());
			_assertTime(expected.getTimeModified().getTime(), actual.getTimeModified().getTime());
			_assertTime(expected.getAlertStartTime().getTime(), actual.getAlertStartTime().getTime());
			_assertTime(expected.getAlertEndTime().getTime(), actual.getAlertEndTime().getTime());
			_assertEquals(expected.getAlertType().toString(), actual.getAlertType().toString());
			_assertTrue(expected.isActive() == actual.isActive());
		}
		else{
			_assertTrue(alertServer.getAlertIdAlertMap().get(expected.getAlertId())==null);
			_assertTrue(alertServer.getAlertIdAlertHistMap().get(expected.getAlertId())==null);
			for(PriceAlert alert : prodAlerts){
				_assertTrue(expected.getAlertId() != alert.getAlertId());
			}
		}
	}
	
	public void assertAlertHistory(int alertId, PriceAlertHistory expected){
		_assertTrue(alertServer.getAlertIdAlertMap().get(alertId)!=null);
		
		if(expected!=null){
			_assertTrue(alertServer.getAlertIdAlertHistMap().get(expected.getAlertId())!=null);
			_assertEquals(expected.getAlertId(), alertId);
			
			PriceAlertHistory actual = alertServer.getAlertIdAlertHistMap().get(expected.getAlertId());
			_assertEquals(expected.getAlertHistoryId(), actual.getAlertHistoryId());
			_assertEquals(expected.getAlertId(), actual.getAlertId());
			_assertEquals(expected.getNotificationPrice(), actual.getNotificationPrice());
			_assertTime(expected.getTimeSent().getTime(), actual.getTimeSent().getTime());
		} else{
			_assertTrue(alertServer.getAlertIdAlertHistMap().get(alertId)==null);
		}
	}
	
	private void assertSendEmail(int alertId, ProductSummary expected){
		_assertTrue(alertServer.getAlertIdAlertMap().get(alertId)!=null);
		if(expected!=null){
			_assertTrue(alertServer.getAlertIdAlertHistMap().get(alertId)!=null);			
			PriceAlertHistory actual = alertServer.getAlertIdAlertHistMap().get(alertId);
			_assertEquals(alertId, actual.getAlertId());
			_assertEquals(expected.getPrice(), actual.getNotificationPrice());
		}
	}
}

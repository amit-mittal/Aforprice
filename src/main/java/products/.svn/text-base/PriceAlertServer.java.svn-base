package products;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.Emailer;
import util.concurrent.Executors;
import db.dao.DAOException;
import db.dao.LastProcessedDAO;
import db.dao.PriceAlertDAO;
import db.dao.PriceAlertHistoryDAO;
import db.dao.PriceAlertServerFactory;
import db.dao.ProductsDAO;
import db.dao.UserDAO;
import entities.PriceAlert;
import entities.PriceAlertHistory;
import entities.PriceAlertType;
import entities.ProductSummary;
import entities.User;

public class PriceAlertServer {
	private static final Logger logger = Logger
			.getLogger(PriceAlertServer.class);
	private PriceAlertDAO alertDao;
	private PriceAlertHistoryDAO alertHistDao;
	private LastProcessedDAO lastProcDao;
	private UserDAO userDao;
	private ProductsDAO prodDao;
	Map<Integer, List<PriceAlert>> _productIdAlertMap = new HashMap<Integer, List<PriceAlert>>();
	Map<Integer, PriceAlertHistory> _alertIdAlertHistMap = new HashMap<Integer, PriceAlertHistory>();
	Map<Integer, PriceAlert> _alertIdAlertMap = new HashMap<Integer, PriceAlert>();
	public static int PRICE_ALERT_TIMER_INTERVAL = 5 * 60 * 1000;// 5 mins
	public static final String PROCESS_NAME = "PRICE_ALERT";
	public static final double EPSILON = 0.000001;
	private final SimpleDateFormat dateFormatterYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date lastProcessedTime;
	public PriceAlertServer() {
		// set it here so we use mock objects in unit test mode
		alertDao = PriceAlertServerFactory.getInstance().getPriceAlertDAO();
		alertHistDao = PriceAlertServerFactory.getInstance()
				.getPriceAlertHistDAO();
		lastProcDao = PriceAlertServerFactory.getInstance().getLastProcDAO();
		userDao = PriceAlertServerFactory.getInstance().getUserDAO();
		prodDao = PriceAlertServerFactory.getInstance().getProdDAO();

	}

	public void addAlert(PriceAlert newAlert) {
		int prodId = newAlert.getProductId();
		int alertId = newAlert.getAlertId();

		_alertIdAlertMap.put(alertId, newAlert);

		if (_productIdAlertMap.containsKey(prodId)) {
			_productIdAlertMap.get(prodId).add(newAlert);
		} else {
			List<PriceAlert> alerts = new ArrayList<PriceAlert>(1);
			alerts.add(newAlert);
			_productIdAlertMap.put(prodId, alerts);
		}

		_alertIdAlertHistMap.put(alertId, null);
	}

	public void updateAlert(PriceAlert updatedAlert) {
		PriceAlert curAlert = _alertIdAlertMap.get(updatedAlert.getAlertId());
		curAlert.setAlertStartTime(updatedAlert.getAlertStartTime());
		curAlert.setAlertEndTime(updatedAlert.getAlertEndTime());
		curAlert.setAlertPrice(updatedAlert.getAlertPrice());
		curAlert.setActive(updatedAlert.isActive());
		curAlert.setTimeModified(updatedAlert.getTimeModified());
	}

	public void removeAlert(PriceAlert deletedAlert) {
		_alertIdAlertHistMap.remove(deletedAlert.getAlertId());
		PriceAlert remAlert = _alertIdAlertMap
				.remove(deletedAlert.getAlertId());

		if (remAlert == null)
			logger.error("Non existant alert is being removed");

		if (_productIdAlertMap.get(deletedAlert.getProductId()) != null) {
			_productIdAlertMap.get(deletedAlert.getProductId())
					.remove(remAlert);
		} else
			logger.error("Price alert is present in non-existant product id");
	}

	public void updateAlerts(List<PriceAlert> alerts) {
		for (PriceAlert alertInfo : alerts) {
			if (alertInfo.isActive()) {
				if (_alertIdAlertHistMap.containsKey(alertInfo.getAlertId()))
					updateAlert(alertInfo);
				else
					addAlert(alertInfo);
			} else {
				removeAlert(alertInfo);
			}
		}
	}

	public String makeHTMLEmail(PriceAlert alert,
			PriceAlertHistory latestAlert, ProductSummary product) {
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");
		html.append("<h2>").append("Price Alert").append("</h2>");
		html.append("<table border=\"1\">");

		html.append("<tr>");
		html.append("<th>").append("Alert Id").append("</th>");
		html.append("<th>").append(alert.getAlertId()).append("</th>");
		html.append("</tr>");

		html.append("<tr>");
		html.append("<td>").append("Product Id").append("</td>");
		html.append("<td>").append(alert.getProductId()).append("</td>");
		html.append("</tr>");

		html.append("<tr>");
		html.append("<td>").append("Product Name").append("</td>");
		html.append("<td>").append(product.getName()).append("</td>");
		html.append("</tr>");

		html.append("<tr>");
		html.append("<td>").append("Product URL").append("</td>");
		html.append("<td>").append(product.getUrl()).append("</td>");
		html.append("</tr>");

		if (alert.getAlertType() == PriceAlertType.ALERT_WHEN_AT_PRICE) {
			html.append("<tr>");
			html.append("<td>").append("Set Price").append("</td>");
			html.append("<td>").append(alert.getAlertPrice()).append("</td>");
			html.append("</tr>");
		}

		html.append("<tr>");
		html.append("<td>").append("Current Price").append("</td>");
		html.append("<td>").append(latestAlert.getNotificationPrice())
				.append("</td>");
		html.append("</tr>");

		html.append("</table>").append("</body>").append("</html>");
		return html.toString();
	}

	public void sendAlert(PriceAlert alert, double price) throws SQLException {

		logger.info("Sending the email alert to alertId: " + alert.getAlertId());

		PriceAlertHistory latestAlert = new PriceAlertHistory(-1,
				alert.getAlertId(), new Date(), price);
		ProductSummary product = prodDao.getProductSummaryByProductId(alert
				.getProductId());

		String subject = "Price Alert for " + product.getName() + ", price is "
				+ price;
		String emailTemplate = makeHTMLEmail(alert, latestAlert, product);
		List<String> to = new ArrayList<String>();
		to.add(alert.getEmailId());
		// TODO: CHECK BELOW IMPLEMENTATION
		if (ConfigParms.getInstance().isProduction())
			Emailer.getInstance().send(to, null, null, subject, emailTemplate,
					true);

		_alertIdAlertHistMap.remove(latestAlert.getAlertId());
		_alertIdAlertHistMap.put(latestAlert.getAlertId(), latestAlert);
		alertHistDao.addPriceAlertHistory(latestAlert);
	}

	public boolean checkUser(PriceAlert alert) throws SQLException {
		User user = userDao.getUser(alert.getEmailId());
		if (user == null)
			return false;

		if (user.isActive())
			return true;
		else {
			if (user.isRegistered())
				return false;
			else
				return true;
		}
	}

	private void checkAlert(PriceAlert alert, double price) throws SQLException {
		if (alert.getAlertType().equals(PriceAlertType.ALERT_WHEN_AT_PRICE)) {// notification
																				// if
																				// product
																				// price
																				// just
																				// falls
																				// below
																				// his
																				// alert
																				// price
			if (alert.getAlertPrice() - price >= EPSILON) {
				sendAlert(alert, price);
			}
		} else if (alert.getAlertType().equals(
				PriceAlertType.ALERT_WHEN_PRICE_DROPS)) {
			if (_alertIdAlertHistMap.get(alert.getAlertId()) != null) {
				if (_alertIdAlertHistMap.get(alert.getAlertId())
						.getNotificationPrice() - price >= EPSILON) {
					sendAlert(alert, price);
				}
			} else {
				if (alert.getAlertPrice() - price >= EPSILON) {
					sendAlert(alert, price);
				}
			}
		}
	}

	protected void processProductUpdate(List<ProductSummary> updatedProducts)
			throws SQLException {
		for (ProductSummary update : updatedProducts) {
			if (!_productIdAlertMap.containsKey(update.getId()))
				continue;
			List<PriceAlert> alerts = _productIdAlertMap.get(update.getId());
			for (PriceAlert alert : alerts) {
				if (checkUser(alert) == true)
					checkAlert(alert, update.getPrice());
			}
		}
		logger.info("Processed updates");
	}

	public Map<Integer, List<PriceAlert>> getProductIdAlertMap() {
		return _productIdAlertMap;
	}

	public Map<Integer, PriceAlert> getAlertIdAlertMap() {
		return _alertIdAlertMap;
	}

	public Map<Integer, PriceAlertHistory> getAlertIdAlertHistMap() {
		return _alertIdAlertHistMap;
	}

	protected void getAlertConfigurationAndHistory() throws DAOException {
		List<PriceAlert> activeAlerts = alertDao.getAllActiveAlerts();
		logger.info("Number of Active Alerts: " + activeAlerts.size());
		for (PriceAlert alert : activeAlerts) {
			logger.info("Alert: " + alert);
			_alertIdAlertMap.put(alert.getAlertId(), alert);

			// Identify & set the latest alert from alert history
			List<PriceAlertHistory> alertHistoryList = alertHistDao
					.getPriceAlertHistory(alert.getAlertId());
			logger.info("Alert History");
			PriceAlertHistory latestAlert = null;
			for (PriceAlertHistory alertHistoryItem : alertHistoryList) {
				logger.info(alertHistoryItem);
				if (latestAlert == null)
					latestAlert = alertHistoryItem;
				else if (latestAlert.getTimeSent().before(
						alertHistoryItem.getTimeSent()))
					latestAlert = alertHistoryItem;
			}
			if (latestAlert != null)
				_alertIdAlertHistMap.put(alert.getAlertId(), latestAlert);

			if (_productIdAlertMap.containsKey(alert.getProductId()))
				_productIdAlertMap.get(alert.getProductId()).add(alert);
			else {
				List<PriceAlert> prodAlerts = new ArrayList<PriceAlert>(1);
				prodAlerts.add(alert);
				_productIdAlertMap.put(alert.getProductId(), prodAlerts);
			}
		}
	}

	private List<ProductSummary> emptyProductSummary = new ArrayList<ProductSummary>();

	protected List<ProductSummary> getProductUpdates()
			throws DAOException, SQLException, ParseException {
		Timestamp newModifiedTime = prodDao.getProductMaxModifiedTime();
		String lastProcessed = lastProcDao.getLastProcessed(PROCESS_NAME);			
		lastProcessedTime = dateFormatterYYMMDDHHMMSS.parse(lastProcessed);
		if(newModifiedTime.before(lastProcessedTime)){//possible if we are not getting any products
			logger.info("no new products after "+lastProcessedTime);
			return emptyProductSummary;
		}
		logger.info("Getting products updated after "+lastProcessedTime);
		List<ProductSummary> products = prodDao.getUpdatedProducts(null, new Timestamp(lastProcessedTime.getTime()));
		logger.info("Got "+products.size()+" updated products");

		//if new time is same as old time, then add 1 sec so we don't keep getting same products over and over
		if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST && newModifiedTime.equals(lastProcessedTime))//no optimization in test mode, it messes it up 
			//since time_modified field may not be different for two different rows for one product inserted very quickly
			lastProcessedTime = new Timestamp(newModifiedTime.getTime()+1000);
		else
			lastProcessedTime = newModifiedTime;
		return products;
	}

	public void start() throws DAOException, SQLException, ParseException {
		// read alert configuration & history details
		Timestamp lastGetTime = new Timestamp(System.currentTimeMillis());
		getAlertConfigurationAndHistory();

		// a background thread to update alert configurations
		ScheduledExecutorService scheduler = Executors
				.newScheduledDaemonThreadPool(1);
		AlertConfigUpdator alertConfigUpdator = new AlertConfigUpdator(this,
				lastGetTime);
		// TODO: Uncomment this code and remove line below it
		// scheduler.scheduleWithFixedDelay(alertConfigUpdator, 10, 10,
		// TimeUnit.MINUTES );
		scheduler.scheduleWithFixedDelay(alertConfigUpdator, 5, 5,
				TimeUnit.HOURS);

		// monitor product prices history & send alerts
		//set initial value
		while (true) {
			List<ProductSummary> updatedProducts = getProductUpdates();
			processProductUpdate(updatedProducts);
			lastProcDao.setLast(PROCESS_NAME, dateFormatterYYMMDDHHMMSS.format(lastProcessedTime));
			try {
				Thread.sleep(PRICE_ALERT_TIMER_INTERVAL);
			} catch (InterruptedException ie) {
				logger.error(ie.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		logger.info("Starting the PriceAlertServer");
		try {
			//this dummy socket helps preventing dupe process
			@SuppressWarnings({ "resource", "unused" })
			ServerSocket socket = new ServerSocket(29999);
			new PriceAlertServer().start();
		} catch (BindException be){
			logger.error(be.getMessage(), be);
			logger.error("Dupe process, going down");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}

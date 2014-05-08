package products;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import db.dao.PriceAlertDAO;
import entities.PriceAlert;

public class AlertConfigUpdator implements Runnable{

	private static final Logger logger = Logger.getLogger(AlertConfigUpdator.class);
	private PriceAlertServer alertServer;
	private static final PriceAlertDAO alertDao = new PriceAlertDAO();
	Timestamp m_lastGetTime;

	public AlertConfigUpdator(PriceAlertServer alertServer, Timestamp lastGetTime) {
		this.alertServer = alertServer;
		m_lastGetTime = lastGetTime;
	}

	@Override
	public void run() {
		try {
			List<PriceAlert> alerts = alertDao.getUpdatedPriceAlerts(m_lastGetTime);
			alertServer.updateAlerts(alerts);

			/**
			 * Who can update alert history? Only the price alert server? Right
			 * now we have only one price alert server. So we will update in
			 * memory alert history before saving to the database. We always
			 * know latest state of alert histories before database knows. No
			 * need to poll this data back from database
			 */
			// List<PriceAlertHistory> alertHistList =
			// pollDbForAlertHistoryUpdates();
			// alertServer.updateAlertHistoryList(alertHistList);

		} catch (SQLException e) {
			logger.error("Error updating alert configuration");
			logger.error(e.getMessage());
		}
	}

	/*
	 * public List<PriceAlertHistory> pollDbForAlertHistoryUpdates() throws
	 * SQLException{ List<PriceAlertHistory> alertHistList = new
	 * ArrayList<PriceAlertHistory
	 * >(alertHistDao.getUpdatedPriceAlertHistory(latestModifiedTime)); return
	 * alertHistList; }
	 */
}

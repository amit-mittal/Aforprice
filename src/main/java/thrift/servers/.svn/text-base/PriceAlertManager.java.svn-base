package thrift.servers;

import static thrift.genereated.pricealert.price_alertConstants.PRICE_ALERT_SERVER_PORT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.genereated.pricealert.PRICE_ALERT_ERROR_CODE;
import thrift.genereated.pricealert.PriceAlertException;
import thrift.genereated.pricealert.PriceAlertService;
import thrift.genereated.pricealert.PriceAlertThrift;
import util.ConfigParms;
import util.DateTimeUtils;
import util.Emailer;
import util.ValErr;
import util.build.PriceAlertBuilder;
import util.build.UserBuilder;
import db.dao.DAOException;
import db.dao.PriceAlertDAO;
import db.dao.PriceAlertServerFactory;
import db.dao.UserDAO;
import entities.PriceAlert;
import entities.PriceAlertType;
import entities.User;

public class PriceAlertManager implements PriceAlertService.Iface{
	private static final Logger logger = Logger.getLogger(PriceAlertManager.class);
	private static PriceAlertDAO dao = new PriceAlertDAO();
	private static UserDAO userDAO = new UserDAO();
	public static final long TIME_INT = 30*24*60*60*1000L;//1 month
	private Map<String, User> emailToUserMap = new HashMap<String, User>();
	
	//TODO: Have to implement methods such that able to transfer exceptions from JAVA to PHP
	
	public String makeHTMLEmail(User user, PriceAlert alert){
		//TODO this link should be encrypted before sending the again should be decrypted before verification, this way user can't change emailId in the url
		StringBuilder link = new StringBuilder();
		link.append("http://69.136.251.135/aforprice/Testing/testThriftPriceAlertClient.php?action=verify&");
		link.append("alertId="+alert.getAlertId());
		
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");		
		html.append("<h2>").append("Price Alert Details").append("</h2>");
		html.append("<table border=\"1\">");
		
		html.append("<tr>");
			html.append("<td>").append("Alert Id").append("</td>");
			html.append("<td>").append(alert.getAlertId()).append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
			html.append("<td>").append("Product Id").append("</td>");
			html.append("<td>").append(alert.getProductId()).append("</td>");
		html.append("</tr>");

		if(alert.getAlertType()==PriceAlertType.ALERT_WHEN_AT_PRICE){
			html.append("<tr>");
				html.append("<td>").append("Set Price").append("</td>");
				html.append("<td>").append(alert.getAlertPrice()).append("</td>");
			html.append("</tr>");
		}
		
		html.append("<tr>");
			html.append("<td>").append("Email Id").append("</td>");
			html.append("<td>").append(user.getEmailId()).append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
			html.append("<td>").append("Price Alert Verification").append("</td>");
			html.append("<td>").append("<a href='"+link.toString()+"'>LINK</a>").append("</td>");
		html.append("</tr>");
		
		if(user.isRegistered()){
			StringBuilder userLink = new StringBuilder();
			userLink.append("http://69.136.251.135/aforprice/Testing/testThriftUserClient.php?action=verify&");
			userLink.append("emailId="+user.getEmailId());
			
			html.append("<tr>");
				html.append("<td>").append("User Verification").append("</td>");
				html.append("<td>").append("<a href='"+userLink.toString()+"'>LINK</a>").append("</td>");
			html.append("</tr>");
		} else{
			StringBuilder userLink = new StringBuilder();
			userLink.append("http://69.136.251.135/aforprice/services/");
			
			html.append("<tr>");
				html.append("<td>").append("User Registration").append("</td>");
				html.append("<td>").append("<a href='"+userLink.toString()+"'>LINK</a>").append("</td>");
			html.append("</tr>");
		}
		
		html.append("</table>").append("</body>").append("</html>");
		return html.toString();
	}

	public void sendPriceAlertVerification(User user, PriceAlert alert) throws SQLException{
		logger.info("Sending the email alert to EmailId: "+user.getEmailId());
		
		String subject = "PriceAlert Verification from Pricerite";
		String emailTemplate = makeHTMLEmail(user, alert);
		List<String> to = new ArrayList<String>();
		to.add(user.getEmailId());
		
		if(ConfigParms.getInstance().isProduction())
			Emailer.getInstance().send(to, null, null, subject, emailTemplate, true);
	}
	
	public PriceAlert convertPriceAlertThriftToPriceAlert(PriceAlertThrift alert, User user){
		PriceAlertBuilder b = new PriceAlertBuilder();
		b.emailId = alert.getEmailId();
		b.productId = alert.getProductId();
		b.alertPrice = alert.getAlertPrice();
		b.timeModified = new Date();
		b.alertStartTime = new Date();
		b.alertEndTime = new Date(System.currentTimeMillis() + TIME_INT);
		if(alert.alertType.getValue()==0)
			b.alertType = PriceAlertType.ALERT_WHEN_AT_PRICE;
		else
			b.alertType = PriceAlertType.ALERT_WHEN_PRICE_DROPS;
		b.active = true;
		PriceAlert newAlert = b.build();
		
		return newAlert;
	}
	
	@Override
	public boolean addPriceAlertThrift(PriceAlertThrift alert) throws TException {
		try{
			userDAO = PriceAlertServerFactory.getInstance().getUserDAO();
			dao = PriceAlertServerFactory.getInstance().getPriceAlertDAO();
			
			if(dao.getActivePriceAlert(alert.getEmailId(), alert.getProductId())!=null){
				String error = String.format(ValErr.ERR_ALERT_DUPLICATE_ALERT_ON_PRODUCTID, alert.getProductId());
				logger.error(error);
				throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.DUPLICATE_PRICE_ALERT_ON_PRODUCTID, 
						error);
			}
			User user = getOrAddUser(alert.getEmailId());
			PriceAlert newAlert = convertPriceAlertThriftToPriceAlert(alert, user);
			newAlert.setAlertId(dao.addPriceAlert(newAlert));
			/*Remove such email for now.. we will add back if we get any complains of false alert setup
			if(!user.isActive()){
				sendPriceAlertVerification(user, newAlert);
			}
			*/			
			return true;
		} catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		} catch(DAOException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		}
	}

	/**
	 * Get user for given emailId, add to DB if it doesn't exist
	 * @param alert
	 * @return
	 * @throws SQLException
	 * @throws DAOException
	 */
	private User getOrAddUser(String emailId) throws SQLException,
			DAOException {
		//get it from Cache
		User user =  emailToUserMap.get(emailId);
		if(user==null){
			//get it from DB
			user = userDAO.getUser(emailId);
			if(user==null){
				//create and add to DB
				User newUser = new UserBuilder().emailId(emailId).active(true).newsletter(false).registered(false).build();
				userDAO.addUser(newUser);
			}
			//add to cache
			emailToUserMap.put(emailId, user);
		}
		return user;
	}

	@Override
	public boolean updatePriceAlertThrift(PriceAlertThrift alert) throws TException {
		try{
			PriceAlertType alertType;
			if(alert.alertType.getValue()==0)
				alertType = PriceAlertType.ALERT_WHEN_AT_PRICE;
			else
				alertType = PriceAlertType.ALERT_WHEN_PRICE_DROPS;
			PriceAlert updatedAlert = new PriceAlertBuilder().alertId(alert.getAlertId()).emailId(alert.getEmailId()).
					productId(alert.getProductId()).alertPrice(alert.getAlertPrice()).timeModified(new Date()).
					alertEndTime(DateTimeUtils.dateFromyyyyMMdd(alert.alertExpirationDate)).
							active(alert.isAlertActive()).alertType(alertType).build();
			dao.updatePriceAlert(updatedAlert);
			return true;
		} catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		}
	}

	@Override
	public boolean removePriceAlertThrift(int alertId) throws TException {
		try {
			boolean removed = dao.updatePriceAlertStatus(alertId, false);
			return removed;
		} catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		}
	}
	
	@Override
	public boolean verifyPriceAlertThrift(int alertId) throws TException {
		try{
			boolean verified = dao.updatePriceAlertStatus(alertId, true);
			return verified;
		} catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		}
	}
	
	@Override
	public PriceAlertThrift getPriceAlertThrift(String emailId, int productId) throws TException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<PriceAlertThrift> getPriceAlerts(String emailId) throws TException {
		try{
			//TODO: replace database look up with in-memory lookup
			logger.info("getPriceAlerts("+emailId+")");			
			List<PriceAlertThrift> priceAlerts = dao.getAllActivePriceAlertsOfUser(emailId);
			logger.info("Returning "+priceAlerts.size()+" alerts");
			return priceAlerts;
		} catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new PriceAlertException(PRICE_ALERT_ERROR_CODE.SYSTEM_ERROR, ValErr.ERR_ALERT_SYSTEM_ERROR);
		}
	}
	
	public static void main(String[] args){
		Integer port = -1;
		Integer instance = -1;
		if(args.length>=1){
			//args format key=value.. serverId=1 instance=1
			for(int i=0; i<args.length; i++){
				logger.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2)
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				String key = keyVal[0];
				if(key.equalsIgnoreCase("instance")){
					instance = Integer.parseInt(keyVal[1]);
					if( (port=PRICE_ALERT_SERVER_PORT.get(instance-1)) == null)
						throw new IllegalArgumentException("instance "+instance+" is missing in PRICE_ALERT_SERVER_PORT list");
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}//for(int i=0; i<args.length; i++){ ends...
			if( instance==-1)
				throw new IllegalArgumentException("missing argument instance");
			logger.info("Port="+port);
		}//if(args.length>=1) ends...
		
		try {
			TServerSocket serverTransport = new TServerSocket(port);
			PriceAlertService.Processor processor = new PriceAlertService.Processor(new PriceAlertManager());
	        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
	        System.out.println("Starting server on port: "+port);
	        server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}

package entities;

import java.util.Date;

import util.Constants;
import util.ToStringBuilder;
import util.build.PriceAlertBuilder;

public class PriceAlert implements ToStringBuilder{

	private int alertId=-1;
	private final String emailId;
	private int productId;
	private double alertPrice;
	private Date timeModified;
	private Date alertStartTime;
	private Date alertEndTime;
	private final PriceAlertType alertType;
	private boolean active;
	
	public PriceAlert(PriceAlertBuilder builder){
		this.alertId=builder.alertId;
		this.emailId=builder.emailId;
		this.productId=builder.productId;
		this.alertPrice=builder.alertPrice;
		this.timeModified=builder.timeModified;
		this.alertStartTime=builder.alertStartTime;
		this.alertEndTime=builder.alertEndTime;
		this.alertType=builder.alertType;
		this.active=builder.active;
	}
	
	public PriceAlert(int alertId, String emailId,
					  int productId, double alertPrice,
					  Date timeModified, Date alertStartTime,
					  Date alertEndTime, PriceAlertType alertType,
					  boolean active){
		this.alertId=alertId;
		this.emailId=emailId;
		this.productId=productId;
		this.alertPrice=alertPrice;
		this.timeModified=timeModified;
		this.alertStartTime=alertStartTime;
		this.alertEndTime=alertEndTime;
		this.alertType=alertType;
		this.active=active;
	}

	public int getAlertId() {
		return alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public double getAlertPrice() {
		return alertPrice;
	}

	public void setAlertPrice(double alertPrice) {
		this.alertPrice = alertPrice;
	}

	public Date getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(Date timeModified) {
		this.timeModified = timeModified;
	}

	public Date getAlertStartTime() {
		return alertStartTime;
	}

	public void setAlertStartTime(Date alertStartTime) {
		this.alertStartTime = alertStartTime;
	}

	public Date getAlertEndTime() {
		return alertEndTime;
	}

	public void setAlertEndTime(Date alertEndTime) {
		this.alertEndTime = alertEndTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmailId() {
		return emailId;
	}

	public PriceAlertType getAlertType() {
		return alertType;
	}
	
	@Override
	public boolean equals(Object obj){
		try{
			PriceAlert that = (PriceAlert)obj;
			if(this==null||that==null)
				return false;
			
			if(this.alertId==that.alertId)
				return true;
			else
				return false;
		}
		catch(Exception e){
			return false;
		}
	}
	
	@Override
	public String toString(){
		return toString(new StringBuilder());
	}
	
	@Override
	public String toString(StringBuilder sb) {
		sb.append(alertId).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(emailId).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(productId).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(alertPrice).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(timeModified).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(alertStartTime).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(alertEndTime).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(alertType).append(Constants.PRICE_ALERT_ATTR_COL_SEP)
		.append(active);
		return sb.toString();
	}
	
	public static final class Columns{
		public static final String ALERT_ID = "ALERT_ID";
		public static final String EMAIL_ID = "EMAIL_ID";
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String ALERT_PRICE = "ALERT_PRICE";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
		public static final String ALERT_START_TIME = "ALERT_START_TIME";
		public static final String ALERT_END_TIME = "ALERT_END_TIME";
		public static final String ALERT_TYPE = "ALERT_TYPE";
		public static final String ACTIVE = "ACTIVE";
		public static final String RETAILER_ID = "RETAILER_ID";
		public static final String PRODUCT_NAME = "PRODUCT_NAME";
		public static final String URL = "URL";
		public static final String IMAGE_URL = "IMAGE_URL";
		public static final String PRICE = "PRICE";
	}
}

package entities;

import java.util.Date;

import util.Constants;
import util.ToStringBuilder;
import util.build.PriceAlertHistoryBuilder;

public class PriceAlertHistory implements ToStringBuilder{

	private int alertHistoryId=-1;
	private int alertId;
	private Date timeSent;
	private double notificationPrice;
	
	public PriceAlertHistory(PriceAlertHistoryBuilder builder){
		this.alertHistoryId=builder.alertHistoryId;
		this.alertId=builder.alertId;
		this.timeSent=builder.timeSent;
		this.notificationPrice=builder.notificationPrice;
	}
	
	public PriceAlertHistory(int alertHistoryId, int alertId, Date timeSent, double notificationPrice){
		this.alertHistoryId=alertHistoryId;
		this.alertId=alertId;
		this.timeSent=timeSent;
		this.notificationPrice=notificationPrice;
	}
	
	public int getAlertHistoryId() {
		return alertHistoryId;
	}

	public void setAlertHistoryId(int alertHistoryId) {
		this.alertHistoryId = alertHistoryId;
	}

	public int getAlertId() {
		return alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public double getNotificationPrice() {
		return notificationPrice;
	}

	public void setNotificationPrice(double notificationPrice) {
		this.notificationPrice = notificationPrice;
	}

	@Override
	public boolean equals(Object obj){
		try{
			PriceAlertHistory that = (PriceAlertHistory)obj;
			if(this==null||that==null)
				return false;
			
			if(this.alertHistoryId==that.alertHistoryId)
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
		sb.append(alertHistoryId).append(Constants.PRICE_ALERT_HISTORY_ATTR_COL_SEP)
		.append(alertId).append(Constants.PRICE_ALERT_HISTORY_ATTR_COL_SEP)
		.append(timeSent).append(Constants.PRICE_ALERT_HISTORY_ATTR_COL_SEP)
		.append(notificationPrice);
		return sb.toString();
	}
	
	public static final class Columns{
		public static final String ALERT_HISTORY_ID = "ALERT_HISTORY_ID";
		public static final String ALERT_ID = "ALERT_ID";
		public static final String TIME_SENT = "TIME_SENT";
		public static final String NOTIFICATION_PRICE = "NOTIFICATION_PRICE";
	}
}

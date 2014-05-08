package util.build;

import java.util.Date;

import entities.PriceAlert;
import entities.PriceAlertType;

public class PriceAlertBuilder extends ObjectBuilder<PriceAlert>{
	
	public int alertId=-1;
	public String emailId;
	public int productId;
	public double alertPrice;
	public Date timeModified;
	public Date alertStartTime;
	public Date alertEndTime;
	public PriceAlertType alertType;
	public boolean active;
	
	public PriceAlertBuilder(){
	}

	@Override
	public PriceAlert build() {
		return new PriceAlert(this);
	}
	
	public PriceAlertBuilder(int alertId, String emailId,
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
	
	public PriceAlertBuilder alertId(int alertId) {
		this.alertId = alertId;
		return this;
	}
	
	public PriceAlertBuilder emailId(String emailId) {
		this.emailId = emailId;
		return this;
	}
	
	public PriceAlertBuilder productId(int productId) {
		this.productId = productId;
		return this;
	}
	
	public PriceAlertBuilder alertPrice(double alertPrice) {
		this.alertPrice = alertPrice;
		return this;
	}
	
	public PriceAlertBuilder timeModified(Date timeModified) {
		this.timeModified = timeModified;
		return this;
	}
	
	public PriceAlertBuilder alertStartTime(Date alertStartTime) {
		this.alertStartTime = alertStartTime;
		return this;
	}
	
	public PriceAlertBuilder alertEndTime(Date alertEndTime) {
		this.alertEndTime = alertEndTime;
		return this;
	}
	
	public PriceAlertBuilder alertType(PriceAlertType alertType) {
		this.alertType = alertType;
		return this;
	}
	
	public PriceAlertBuilder active(boolean active) {
		this.active = active;
		return this;
	}
}

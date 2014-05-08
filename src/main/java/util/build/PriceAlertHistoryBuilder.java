package util.build;

import java.util.Date;

import entities.PriceAlertHistory;

public class PriceAlertHistoryBuilder extends ObjectBuilder<PriceAlertHistory>{
	public int alertHistoryId=-1;
	public int alertId;
	public Date timeSent;
	public double notificationPrice;
	
	public PriceAlertHistoryBuilder(){
	}
	
	@Override
	public PriceAlertHistory build() {
		return new PriceAlertHistory(this);
	}

	public PriceAlertHistoryBuilder(int alertHistoryId, int alertId, Date timeSent, double notificationPrice){
		this.alertHistoryId=alertHistoryId;
		this.alertId=alertId;
		this.timeSent=timeSent;
		this.notificationPrice=notificationPrice;
	} 
}

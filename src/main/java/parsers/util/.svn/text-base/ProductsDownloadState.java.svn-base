package parsers.util;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import entities.Retailer;

public class ProductsDownloadState {

	private final Retailer retailer;
	private final Date startTime;
	//This supports state for multiple retailers even though the parsers are run per retailer
	private static ConcurrentHashMap<Retailer, ProductsDownloadState> map = new ConcurrentHashMap<>();
	
	private ProductsDownloadState(Retailer retailer) {
		this.retailer = retailer;
		this.startTime = new Date();
	}
	
	private ProductsDownloadState(String retailer) {
		this(Retailer.getRetailer(retailer));
	}
	
	public static ProductsDownloadState get(Retailer retailer){
		return map.get(retailer);
	}
	
	public static ProductsDownloadState get(String retailer){
		return map.get(Retailer.getRetailer(retailer));
	}
	
	public Retailer getRetailer(){
		return retailer;
	}
	
	public Date getStartTime(){
		return startTime;
	}
}
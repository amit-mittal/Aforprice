package work.workers.specs;

import entities.Retailer;


public class ScheduledWorkerSpecs {
	
	private final int poolSize;
	public ScheduledWorkerSpecs(){
		poolSize = 8; //Default pool size
	}
	
	public ScheduledWorkerSpecs(String retailer){
		if(Retailer.WALMART.getId().equalsIgnoreCase(retailer) || 
				Retailer.TARGET_MOBILE.getId().equalsIgnoreCase(retailer) ||
				Retailer.AWSAMAZON.getId().equalsIgnoreCase(retailer) ||
				Retailer.AMAZONBESTSELLER.getId().equalsIgnoreCase(retailer)
				){
			poolSize = 6;
		}
		else 
			poolSize = 2;		
	}
	public int threadPoolSize(){
		return poolSize;
	}
	
	public int frequencyInSeconds(){
		return 60*60*24;
	}
	
	public int startHrs(){
		return 1;
	}
	
	public int startMin(){
		return 0;
	}
	
	public int startSec(){
		return 0;
	}
	
	public boolean onlyOnce(){
		return true;
	}
}

package servers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import stores.ProductStore;
import util.ConfigParms;
import util.Constants;
import util.DateTimeUtils;
import util.Emailer;
import util.MetricCollector;
import work.managers.ProductsParseWorkManager;
import work.managers.WorkManager;
import work.workers.ScheduledWorker;
import work.workers.specs.ScheduledWorkerSpecs;
import entities.Retailer;

public class ProductsParsingServer {
	private static final Logger logger = Logger.getLogger(ProductsParsingServer.class);
	public static void main(String[] args){
		Thread metricCollectorShuDownRunner = new Thread(MetricCollector.SHUTDOWN_RUNNER);
		Runtime.getRuntime().addShutdownHook(metricCollectorShuDownRunner);
		String retailerOverride = "";
		int id = 0;
		if(args.length>=1){
			//args format key=value.. retailer=walmart mode=popular
			for(int i=0; i<args.length; i++){
				logger.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2){
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				}				
				String key = keyVal[0];
				String val = keyVal[1];
				if(key.equalsIgnoreCase("retailer")){
					logger.info("retailer arg is specified, will download product for "+val);
					retailerOverride = Retailer.getRetailer(val).getId();
				}else if(key.equals("mode")){
					if(val.equalsIgnoreCase(ConfigParms.DOWNLOAD_MODE.POPULAR.name()))
						ConfigParms.setDownloadMode(ConfigParms.DOWNLOAD_MODE.POPULAR);
				}	
				else if(key.equals("id")){
					try{
						id = Integer.parseInt(val);
					}catch(Exception e){
						logger.warn("expects id to be an integer. defaulting to " + id);
					}
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}
		}
		Date runDate = new Date();
		WorkManager workManager = new ProductsParseWorkManager(runDate, retailerOverride, id);
		ScheduledWorker worker = new ScheduledWorker(retailerOverride == null?new ScheduledWorkerSpecs(): new ScheduledWorkerSpecs(retailerOverride), workManager);
		worker.scheduleNow();
		ProductStore store = ProductStore.Factory.get();
		while(!store.allProcessed()){
			logger.info("All records not processed. Sleeping for 30 seconds");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
			}
		}
		store.close();
		Date finshTime = new Date();
		long diff = DateTimeUtils.diff(finshTime, runDate, TimeUnit.HOURS); 
		if( diff > 24){
			String defAlertTxt = "Products parser for " + retailerOverride + " took " + diff + " hrs. Please investigate.";
			try{
				String owner = Retailer.retailerToOwnerMap.get(retailerOverride);
				Emailer.getInstance().sendText(owner + "-" + defAlertTxt, defAlertTxt);
			}catch(Exception e){
				Emailer.getInstance().sendText(defAlertTxt, defAlertTxt);
			}
			
		}		
		System.exit(0);
	}
}
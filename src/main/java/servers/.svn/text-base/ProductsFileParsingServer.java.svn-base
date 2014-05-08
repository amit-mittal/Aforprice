package servers;

import org.apache.log4j.Logger;

import products.ProductsReconciler;

import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.DateTimeUtils;
import work.managers.ProductsFileParseWorkManager;
import work.managers.WorkManager;
import work.workers.ScheduledWorker;
import work.workers.specs.ScheduledWorkerSpecs;

public class ProductsFileParsingServer {
	private static final Logger logger = Logger.getLogger(ProductsFileParsingServer.class);
	public static void main(String[] args){
		if(args.length<2){
			logger.error("Usage " + ProductsFileParsingServer.class.getCanonicalName() +" <retailer> <rundate>");
			System.exit(-1);
		}
		String retailer=args[0];
		String runDate=args[1];
		logger.info("Parse downloaded files for retailer=" + retailer + ",rundate=" + runDate);
		WorkManager workManager = new ProductsFileParseWorkManager(retailer, runDate);
		ConfigParms.getInstance().setMode(RUNTIME_MODE.FILEMODE);
		ConfigParms.getInstance().setFileRunDate(DateTimeUtils.dateFromyyyyMMdd(runDate));
		ScheduledWorker worker = new ScheduledWorker(new ScheduledWorkerSpecs(), workManager);
		worker.scheduleNow();
		//Done with parsing, now wait for all tasks to be finished
		synchronized(worker){
			try {
				logger.info(ProductsFileParsingServer.class.getCanonicalName() + " before ScheduledWorker.wait" );
				worker.wait();
				logger.info(ProductsFileParsingServer.class.getCanonicalName() + " after ScheduledWorker.wait" );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//now reconcile
//		ProductsReconciler reconciler = new ProductsReconciler();
//		reconciler.reconcile(DateTimeUtils.dateFromyyyyMMdd(runDate), retailer, 7);
	}
}
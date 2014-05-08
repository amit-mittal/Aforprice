package products;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.MemoryUsage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.Constants;
import util.Emailer;
import util.Metric;
import util.MetricCollector;
import util.MyMemoryUsage;
import util.RawMetric;
import util.Utils;
import db.dao.LastProcessedDAO;
import db.dao.ProductCategoryDAO;
import db.dao.ProductsDAO;
import entities.ProductSummary;

public class ProductsMigration {
	private static final ProductsDAO prodsDAO = new ProductsDAO();
	private static final ProductCategoryDAO prodCatDAO = new ProductCategoryDAO();
	private static final LastProcessedDAO lastDAO = new LastProcessedDAO();
	private static final Logger logger = Logger.getLogger(ProductsMigration.class);
	//These paramters can be overridden by providing input parameters
	private static int batchSize = Constants.MAX_DOWNLOADED_PRODS_TO_PROCESS_PER_LOOP;
	private static Set<String> retailersToProcess = new HashSet<String>();
	private static boolean include = true;
	private static String processName = "ProductsMigration";
	private static Metric metLastCounter = new Metric("LastCounter");
	public static void start(){
		String startValue = lastDAO.getLastProcessed(processName);
		long start = 0, end = 0;
		try {
			HashMap<ProductSummary, ProductSummary> existing = prodsDAO.getProducts(retailersToProcess, include);
			prodCatDAO.populateProductCategories(existing);
			boolean keepRunning = false;//disabling this feature as it occupies too much memory on the box
			while(true){
				startValue = lastDAO.getLastProcessed(processName);
				start = startValue == null? prodsDAO.getMinDownloadId(): Long.parseLong(startValue);
				long maxId = prodsDAO.getMaxDownloadId();
				end = Math.min(start + batchSize, maxId);
				while(start <= maxId){				
					migrate(start, end, existing);
					start += batchSize;
					end = Math.min(end + batchSize, maxId);
					//check memory usage is under control
					checkMemoryUsage();
				}//while(start <= maxId) ends...
				if(!keepRunning)
					break;
				if(ConfigParms.isUnitTestMode())
					break;
				//sleep for 1 hr and process new records
				logger.info("Sleep for 1 hrs and than process new records");
				Thread.sleep(60*60*1000);
			}//while(keepRunning) ends...
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			StringWriter sw = new StringWriter();
			PrintWriter p = new PrintWriter(sw);
			e.printStackTrace(p);
			if(!ConfigParms.isUnitTestMode())
				Emailer.getInstance().sendText("Failed to migrate all products for which id > " + start, sw.toString());
		}		
	}

	/**
	 * check memory usage and restart if memory usage is more than 
	 * @throws InterruptedException 
	 */
	private static void checkMemoryUsage() throws InterruptedException{
		int memoryCheckThreshold = 85;
		int memoryRestartThreshold = 80;//keep it bit lower than above threshold to make sure we don't get in a loop
		logger.info(MyMemoryUsage.getInstance().getMemoryInfo(true));
		if(MyMemoryUsage.getInstance().usedMemoryInPercentage()<=memoryCheckThreshold)
			return;
		logger.info("Memory usage exceeded "+memoryCheckThreshold+" percent, trying to free up memory");
		System.gc();
		logger.info("sleep for 20 seconds");
		Thread.sleep(20*1000);		
		//check again
		logger.info(MyMemoryUsage.getInstance().getMemoryInfo(true));
		if(MyMemoryUsage.getInstance().usedMemoryInPercentage()<=memoryRestartThreshold)
			return;
		logger.info("Memory usage exceeded "+memoryRestartThreshold+" percent, restarting process");
		Emailer.getInstance().sendText("Memory usage exceeded "+memoryRestartThreshold+" percent, restarting process", MyMemoryUsage.getInstance().getMemoryInfo(false));
		System.exit(Constants.RESTARTABLE_ERROR);
	}
	
	/**
	 * @param start
	 * @param end
	 * @param metLastCounter
	 * @param startTimeSeconds
	 * @throws SQLException
	 */
	private static void migrate(long start, long end, HashMap<ProductSummary, ProductSummary> existing) throws SQLException {
		List<ProductSummary> prods;
		long startTimeSeconds = System.currentTimeMillis();
		prods = prodsDAO.getDownloadedProductsToProcess(start, end, retailersToProcess, include);
		long endTimeSeconds = System.currentTimeMillis();
		MetricCollector.RawMetricQueue.add(new RawMetric("ProductMigration:GetDownloadedProductToProcess", startTimeSeconds, endTimeSeconds ));				
		logger.info(prods.size() + " products to migrate between " + start + " and " + end + " ids");
		if(prods.size() > 0)
			prodsDAO.insertUpdateProductSummary(prods, existing);
		metLastCounter.start();
		lastDAO.setLast(processName, end);
		metLastCounter.end();
		if(metLastCounter.average(TimeUnit.MILLISECONDS)>0)
			logger.info(metLastCounter.currentStats());
	}
	
	
	public static void main(String[] args){
		parseArgs(args);
		start();
		
		
		Thread metricCollector = new Thread(MetricCollector.INSTANCE);
		Runtime.getRuntime().addShutdownHook(metricCollector);
	}
	
	private static void parseArgs(String[] args){
		if(args.length>=1){
			//args format key=value.. retailers=walmart,target,kohls include=true/false
			for(int i=0; i<args.length; i++){
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2){
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				}				
				String key = keyVal[0];
				String val = keyVal[1];
				if(key.equalsIgnoreCase("batchSize")){
					logger.info("batchSize arg is specified");
					batchSize = Integer.parseInt(val);
				}
				else if(key.equalsIgnoreCase("retailers")){
					String[] rets = val.split(",");
					for(String ret: rets){
						retailersToProcess.add(ret);
					}
				}
				else if(key.equalsIgnoreCase("include")){
					try{
						include = Boolean.parseBoolean(val);
					}catch(Exception e){	
					}
				}
				else if(key.equalsIgnoreCase("processName")){
					try{
						processName = val;
					}catch(Exception e){	
					}
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}//for(int i=0; i<args.length; i++){ ends...
			logger.info("processName="+processName+" batchSize="+ batchSize + " retailers=" + (retailersToProcess.size() == 0?"all": retailersToProcess) + " include=" + include);
		}

	}
}
package util;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.dao.ReconilationDAO;

import util.ConfigParms.DOWNLOAD_MODE;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public class Constants {
	public static final String MYSQL = "MYSQL";
	public final static String MYDB = "MYSQL";
	
	public final static String USER_AGENT = "aforpricebot";
	
	public final static int PROSUPLOAD_DB_TPOOL_SZ = 1;
	public final static int PROD_UP_TO = 1;//30 seconds
	public final static String PRODUCT_DIR_PATH = System.getenv("TMP") + File.separator + "products";
	public final static int PRODUCTS_PER_TASK = 100;
	public final static int PRODUCTS_BATCH_UPLOAD_SZ = 100;
	public final static char PRODUCTS_ATTR_COL_SEP = '\u00a6';
	public final static char PRODUCTS_SEP = '\u00ac';
	public final static String PRODUCTS_ERR_KEY = "products.err";
	
	public final static int MAX_RECONNECT_RETRIES = 10;
	public final static int SLEEP_BTW_RETRIES = 2*1000;//2 seconds
	public final static int PROD_PARSER_CONN_TIME_OUT = 2*60*1000;//2 minutes
	
	public final static int MAX_PRODUCTS_TO_CACHE = 10000;
	public final static String UNPARSEABLE_PRODS_FILE = "unparseable_products";
	public final static String UNRECORDED_PRODS_FILE = "unloadable_products";
	
	public final static double MIN_PRICE_DELTA = 0.001; //1/10th of cent
	public final static int PRODUCT_DOWNLOAD_TIME_THRESHOLD_MIN = 10; 
	
	//If more than 5 products are found again in the same category, then it is considered a cycle
	public final static int MIN_PRODS_CYCLE = 5;
	
	public final static boolean CACHE_PRODS_PROD_STORE = false;
	
	public final static String SMTP_HOST = "192.168.1.120"; 
	public final static String LOWES_STORE = "Lowe's Of W Jordan## UT|1613|2|84084|no|Y|7456 South Plaza Center Dr.|West Jordan|M-Sa 6 Am - 10 Pm## Su 8 Am - 8 Pm|(801) 260-0500|(801) 260-0514|IJ";
	public final static int MAX_DOWNLOADED_PRODS_TO_PROCESS_PER_LOOP = 1000;
	public final static int MAX_FETCH_SIZE_PROD_SUMMARY = 100000;
	public final static int NUM_GROUPS_PROD_SUMMARY = 10;
	
	public final static char USERS_ATTR_COL_SEP = '\u00a6';
	
	public final static char PRICE_ALERT_ATTR_COL_SEP = '|';
	public final static char PRICE_ALERT_HISTORY_ATTR_COL_SEP = '|';
	public final static int RESTARTABLE_ERROR = 95;
	
	public final static class DB_POOL_SIZE{
		//Db pool
		private static final int MAX_POOL_SIZE = 10;
		private static final int INITIAL_POOL_SIZE = 2; 
		private static final int MAX_POOL_SIZE_REGT = 10;
		private static final int INITIAL_POOL_SIZE_REGTG = 2;
		public final static int maxSize(){
			if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.UNITTEST)
				return MAX_POOL_SIZE_REGT;
			return MAX_POOL_SIZE;
		}
		
		public final static int initialSize(){
			if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.UNITTEST)
				return INITIAL_POOL_SIZE_REGTG;
			return INITIAL_POOL_SIZE;
		}
	}

	
	public enum PROCESS_RESULT{
		FILTERED("F"),
		UNMIGRATABLE("U"),
		MIGRATED("M");
		
		private String value;
		private PROCESS_RESULT(String dbValue){
			this.value = dbValue;
		}
		
		public String getDbValue(){
			return value;
		}
	}

	/**
	 * Class to calculate how much time we need to wait
	 * between consecutive http requests to a retailer.
	 * TODO: Make changes to honour the robots.txt directive
	 * for waiting between consecutive requests. 
	 * @author Anurag
	 */
	public static final class WAIT_CALC{ 
		
		private final static Map<DateTimeUtils.TIME_OF_DAY, Map<String, Integer>> WAIT_MAP = new HashMap<DateTimeUtils.TIME_OF_DAY, Map<String, Integer>>();
		static{
			Map<String, Integer> nightWaitMap = new HashMap<String, Integer>();
			Map<String, Integer> dayWaitMap = new HashMap<String, Integer>();
			Retailer[] retailers = Retailer.values();
			for(Retailer retailer: retailers){
				nightWaitMap.put(retailer.getId(), retailer.nightThrottle()); 
				dayWaitMap.put(retailer.getId(), retailer.dayThrottle());
			}
			WAIT_MAP.put(DateTimeUtils.TIME_OF_DAY.NIGHT, nightWaitMap);
			WAIT_MAP.put(DateTimeUtils.TIME_OF_DAY.DAY, dayWaitMap);		
		}
		public final static long MIN_WAIT_ACCESS(String retailer){
			DateTimeUtils.TIME_OF_DAY timeOfDay = DateTimeUtils.getTimeOfDay();
			Integer waitTime = WAIT_MAP.get(timeOfDay).get(retailer);
			if(waitTime == null)
				waitTime = 1000;
			return waitTime;
		}		
	}
	
	public static final class NUM_PRODS_TO_DOWNLOAD{
		private static final Map<String, Integer> retailerNumMap = new HashMap<String, Integer>();
		static{
			retailerNumMap.put(Retailer.WALMART.getId(), 200);
			retailerNumMap.put(Retailer.TARGET_MOBILE.getId(), 200);
			retailerNumMap.put(Retailer.AWSAMAZON.getId(), 200);
		}
		
		public static int get(ConfigParms.DOWNLOAD_MODE mode, String retailer){
			if(mode.equals(DOWNLOAD_MODE.POPULAR)){
				if(retailerNumMap.containsKey(retailer)){
					return retailerNumMap.get(retailer);
				}				
			}
			return Integer.MAX_VALUE;
				
		}
	}
	
	public static final class FILTER_DATE{
		public static Date category(String retailer){
			Calendar cal = DateTimeUtils.getPrevNDaysMidNight(new Date(), 7);
			return new Date(cal.getTimeInMillis());
		}
	}
	
	public static final boolean isWindows() {		 
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}
	
	public static int staleProdsThreshold(String retailer){
		return 100;
	}
	public static String getHostname(){
		try{
			return InetAddress.getLocalHost().getHostName();
		}catch(UnknownHostException e){
			throw new Bhagte2BandBajGaya(e);
		}
	}
	
	public static final class RECON{
		public static final int maxReportCatMismatch(){
			return 1000;
		}
		public static final Date statsFrom(Date to, ReconilationDAO.NAME name){
			return new Date( DateTimeUtils.getPrevNDaysMidNight(to, 30).getTimeInMillis());
		}
		public static final String cellColor(RECON_FIELDS field, int value, ReconStat stat, boolean isErrField){
			if(stat == null)
				return "";
			int avg = stat.getAvg();
			if(avg == 0)
				return "";
			double diffPer = (value - avg)*100/avg;
			if(isErrField && diffPer >= 1 || !isErrField && diffPer <= -1)
				return "red";
			return "";
		}
		public static final String statSummary(ReconStat stat){
			if(stat == null)
				return "";
			return "<b>(</b>" + stat.getMin() + "<b>:</b>" + stat.getAvg() + "<b>:</b>" + stat.getMax() + "<b>)</b>";
		}
		
	}
	
	public final static class PRODUCT_FILES{		
		public final static String DOWN_INTERMEDIATE = ".down.tmp";
		public final static String DOWN_DONE = ".down.done";
		public final static String UP_DONE = ".up.done";
		public final static String UP_DBFAIL = ".up.dbfail";
		public final static String UP_PARSE_ERR = ".up.parseerror";
		public final static String UP_STATE = ".up.state";
		
		public final static String PRODUCT_DIR_PATH(){
			StringBuilder path = new StringBuilder();
			if(isWindows()){
				if(getHostname().equals("ashish-pc"))
					path.append("C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\product_summary_download_failures");
				else
					path.append("C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\product_summary_download_failures");
			}
			else
				path.append(System.getenv("HOME") + "/crawler/product_summary_download_failures");
			path.append(File.separator);
			if(ConfigParms.getInstance().isProduction())
				path.append("prod");				
			else
				path.append("qa");
			
			return path.toString();
		}
	}
	public final static class ALERTS{
		public final static List<String> TO = new ArrayList<String>();
		static{
			if(ConfigParms.getInstance().isProduction() 
					|| ConfigParms.getInstance().isQAReplica()){//qa replication alert
				TO.add("aforprice-alerts@googlegroups.com");
				}
				else {
					if(ConfigParms.getInstance().isAshishPC())
						TO.add("shahashish@gmail.com");
					else
						TO.add("alertanurag@gmail.com");
				}
			}
	}
	/*
	 * product files which are used to get prices
	 */
	
	public final static class PRODUCT_SUMMARY_HTML_FILES{
		public static String path(String retailer, Date date, boolean isErr){
			return rootPath().append(File.separator).append(DateTimeUtils.currentDateYYYYMMDD(date)).append(File.separator)
					.append(retailer).append(isErr?File.separator + "error" + File.separator:"").toString();
		}
		public static String path(String retailer, String catName, Date date, boolean isErr){
			return rootPath().append(File.separator).append(DateTimeUtils.currentDateYYYYMMDD(date))
					   .append(File.separator).append(retailer).append(isErr?File.separator + "error" + File.separator:"")
					   .append(File.separator).append(catName).toString();				 
		}
		/**
		 * @return
		 */
		private static StringBuilder rootPath() {
			StringBuilder path = new StringBuilder();
			if(isWindows()) {				
				String saveLocation = System.getProperty("FILE_SAVE_LOCATION_BASE", "C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\product_summary_html_files\\");
				path.append(saveLocation);
			}
			else
				path.append(System.getenv("HOME") + "/crawler/product_summary_html_files/");
			if(ConfigParms.getInstance().isProduction())
				path.append(File.separator).append("prod");
			else
				path.append(File.separator).append("qa");
			return path;
		}		
	}
	/*
	 * category download files which are used to get categories
	 */
	public final static class CATEGORY_HTML_FILES{
		public static String path(String retailer, Date date){
//			String urlWithoutRoot = url.substring(url.indexOf(".com")+4);
//			urlWithoutRoot.replace("/", File.separator);
			return rootPath().append(File.separator).append(DateTimeUtils.currentDateYYYYMMDD(date)).append(File.separator)
					.append(retailer).append(File.separator).toString();
			
		}
		private static StringBuilder rootPath() {
			StringBuilder path = new StringBuilder();
			if(isWindows()) {
				String saveLocationCat = System.getProperty("FILE_SAVE_LOCATION_BASE_CAT", "C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\category_html_files\\");
				path.append(saveLocationCat);
			} else
				path.append(System.getenv("HOME") + "/crawler/category_html_files/");
			path.append(ConfigParms.getInstance().getEnvironment());
			return path;
		}		
	}
	
	/*
	 * sitemap files and robots.txt files which are used to collect URL's
	 */
	public final static class SITEMAP_ROBOTS_FILES {
		private static StringBuilder rootPath() {
			StringBuilder path = new StringBuilder();
			if(isWindows())
				path.append("C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\sitemap_robots_files\\");
			else
				path.append(System.getenv("HOME") + "/crawler/sitemap_robots_files/");
			path.append(ConfigParms.getInstance().getEnvironment());
			return path;
		}
		
		public static String path(String retailer, Date date) {
			return rootPath().append(File.separator)
					.append(DateTimeUtils.currentDateYYYYMMDD(date))
					.append(File.separator)
					.append(retailer).append(File.separator).toString();
		}
	}
	
	//Constants related to product details downloader.
	public static final int MAX_THREADS_DOWNLOAD(String retailer){
		return 2;
	}
}
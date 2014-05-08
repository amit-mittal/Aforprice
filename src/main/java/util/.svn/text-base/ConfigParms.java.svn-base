/**
 * 
 */
package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.log4j.Logger;

import entities.DbCredentials;
import global.exceptions.Bhagte2BandBajGaya;

/**
 * @author Ashish
 *
 */
public class ConfigParms {
	private final static Logger logger = Logger.getLogger(ConfigParms.class);	
	private Environment environment=Environment.QA;
	private static ConfigParms instance = new ConfigParms();
	public static RUNTIME_MODE MODE = RUNTIME_MODE.REALTIME;
	public static Date FILE_RUNDATE; //Date when files were downloaded 
	private static DOWNLOAD_MODE downloadMode = DOWNLOAD_MODE.ALL;
	private static String os = null;
	private CacheMode cacheMode = CacheMode.WEBSITE;
	public static enum RUNTIME_MODE{
		UNITTEST,
		REALTIME,
		FILEMODE,
		ONETIME
	}
	
	public static enum DOWNLOAD_MODE{
		ALL("A"),
		POPULAR("P");
		private final String dbValue;
		private DOWNLOAD_MODE(String dbValue){
			this.dbValue = dbValue;
		}
		public String getDbValue(){
			return dbValue;
		}
	}
	
	public static enum Environment{
		PRODUCTION,
		QA,
		DEV,
		PRODUCTION_REPLICA,
		QA_REPLICA,
		DEV_REPLICA
	}
	
	public static enum CacheMode{
		WEBSITE,
		PLUGIN,
		IPHONE
	}
	
	public static DOWNLOAD_MODE getDownloadMode(){
		return ConfigParms.downloadMode;
	}
	public static void setDownloadMode(DOWNLOAD_MODE downloadMode){
		ConfigParms.downloadMode = downloadMode;
	}
	public void setMode(RUNTIME_MODE mode){
		MODE = mode;
	}
	
	public void setFileRunDate(Date date){
		FILE_RUNDATE = date;
	}
	public Date getFileRunDate(){
		return FILE_RUNDATE;
	}
	
	private ConfigParms(){
		String environment = System.getProperty("ENVIRONMENT");
		if(environment!=null){
			if(environment.equalsIgnoreCase("PRODUCTION"))
				this.environment = Environment.PRODUCTION;
			else if(environment.equalsIgnoreCase("PRODUCTION_REPLICA"))
				this.environment = Environment.PRODUCTION_REPLICA;
			else if(environment.equalsIgnoreCase("QA"))
				this.environment = Environment.QA;
			else if(environment.equalsIgnoreCase("QA_REPLICA"))
				this.environment = Environment.QA_REPLICA;
			else
				this.environment = Environment.DEV;
		}
		else
			logger.info("ENVIRONMENT property is not set, use default QA");
		logger.info("Environment=" + this.environment);	
		
		String cacheMode = System.getProperty("CACHE_MODE");
		if(cacheMode!=null){
			logger.info("Cache Mode input " + cacheMode);
			if("WEBSITE".equalsIgnoreCase(cacheMode))
				this.cacheMode = CacheMode.WEBSITE;
			else if("PLUGIN".equalsIgnoreCase(cacheMode))
				this.cacheMode = CacheMode.PLUGIN;
			else if("IPHONE".equalsIgnoreCase(cacheMode))
				this.cacheMode = CacheMode.IPHONE;
			else
				throw new RuntimeException("Invalid Input for CACHE_MODE, expecting CACHE_MODE=WEBSITE/PLUGIN/IPHONE, found CACHE_MODE="+cacheMode);
		}
		logger.info("CacheMode=" + this.cacheMode);
	}
	
	public static ConfigParms getInstance(){
		return instance;
	}
	/*
	 * only use it in regtest, used to test different instance of configparms created under differnt environment variable
	 */
	public static void regtestResetInstance(){
		instance=new ConfigParms();
	}
	public boolean isProduction(){
		return getEnvironment()==Environment.PRODUCTION || getEnvironment()==Environment.PRODUCTION_REPLICA;
	}
	public boolean isQAReplica(){
		return getEnvironment()==Environment.QA_REPLICA;
	}
	
	public Environment getEnvironment(){
		return this.environment;
	}
	
	public DbCredentials getDbCredentials(){
		return getDbCredentials(getEnvironment());
	}
	
	public DbCredentials getDbCredentials(Environment environment){
		if(environment == Environment.PRODUCTION){
			if(isLocalNetwork())
				return new DbCredentials("192.168.1.120", "mysqlprod", "mysqlprodPa2s", "PRODUCTS");
			else
				return new DbCredentials("73.33.4.220", "mysqlprod", "mysqlprodPa2s", "PRODUCTS");
		}
		if(environment == Environment.PRODUCTION_REPLICA)
			return new DbCredentials("192.168.1.90", "mysqlprod2", "mysqlprod2Pa2s", "PRODUCTS");
		if(environment == Environment.QA){
			if(isAshishLaptop())
				//return new DbCredentials("69.136.251.135", "mysqlro", "mysqlroPa2s", "PRODUCTS");
				return new DbCredentials("localhost:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS");
			else if(Constants.isWindows())
				return new DbCredentials("192.168.1.3", "mysqlqa", "mysqlPa2s", "PRODUCTS");
			else
				return new DbCredentials("192.168.1.120:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS");
		}
		if(environment == Environment.QA_REPLICA)
			return new DbCredentials("192.168.1.90:3307", "mysqlqa2", "mysql2Pa2s", "PRODUCTS");
		if(environment == Environment.DEV)
			return new DbCredentials("localhost", "mysqlqa", "mysqlPa2s", "PRODUCTS");
		if(environment == Environment.DEV_REPLICA)
			return new DbCredentials("localhost", "mysqlqa", "mysqlPa2s", "PRODUCTS");
		throw new Bhagte2BandBajGaya("Database not configured for env=" + environment);
	}
	
	public boolean isAshishPC(){
		try {
			return InetAddress.getLocalHost().getHostName().toLowerCase().contains("ashish") ;
		} catch (UnknownHostException e) {
			return false;
		}
	}

	public boolean isAshishLaptop(){
		try {
			return InetAddress.getLocalHost().getHostName().toLowerCase().contains("ashish-laptop") ;
		} catch (UnknownHostException e) {
			return false;
		}
	}
	
	/**
	 * @return
	 * True for machines in Anurag's home,
	 * False otherwise
	 */
	public boolean isLocalNetwork(){
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName().toLowerCase();
			return hostname.contains("ocean");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return true;//default		
	}
	
	public static boolean isUnitTestMode(){
		return MODE==RUNTIME_MODE.UNITTEST;
	}
	
	public static void enableUnitTestMode(){
		MODE=RUNTIME_MODE.UNITTEST;
	}

	public static String getOsName()
	{
		if(os == null) 
			os = System.getProperty("os.name");
		return os;
	}
	public static boolean isWindows()
	{
		return getOsName().startsWith("Windows");
	}
	
	public CacheMode getCacheMode(){
		return this.cacheMode;
	}
	
	public boolean isWebsiteMode(){
		return this.cacheMode==CacheMode.WEBSITE;
	}
}

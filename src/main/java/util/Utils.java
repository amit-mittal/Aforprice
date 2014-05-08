package util;

import global.exceptions.Bhagte2BandBajGaya;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import parsers.robotsProtocol.CrawlerProtocol;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Utils {
	
	public static final Logger logger = Logger.getLogger(Utils.class);
	//time to fetch url
	public static long totalTimeToFetchUrl=0;
	public static int urlCount=0;
	
	private static Map<String, Long> lastAccess = new ConcurrentHashMap<String, Long>();
	
	public static String arrayAsString(Object[] arr){
		StringBuilder s = new StringBuilder("[ ");
		for(Object o: arr){
			s.append(o).append(" ");
		}
		s.append("]");
		return s.toString();
	}

    public static Map<String, Object> flatten(Map<String, Object> map){
        return map;
    }

    public static Map<String, Object> unFlatten(Map<String, Object> map){
        return map;
    }


    /**
	 * productFile is of the format yyyyMMdd_retailer.<suffix>
	 * @param productFile
	 * @return
	 */
	public static String getRetailer(String productFile){		
		String[] parts = productFile.split(".");
		return parts[0].split("_")[1];
	}
	
	public static Date getDate(String productFile){
		String yyyyMMdd  = productFile.split("_")[0];
		return DateTimeUtils.dateFromyyyyMMdd(yyyyMMdd);
	}
	
	public static boolean renameFile(File file, String newName){		
		if(!file.isFile())
			throw new Bhagte2BandBajGaya("Expecting file, got directory: " + file.getName());
		File dest = new File(file.getParentFile(), newName);
		if(dest.exists())
			throw new Bhagte2BandBajGaya(dest + " already exists");
		file.renameTo(dest);		
		return dest.exists();
	}
	
	public static boolean isUrlAllowed(String url, String id) {
		return CrawlerProtocol.isUrlAllowed(id, url);
	}
	
	public static void printUrlGetTimings(){
		logger.info("PERF Total url get time " + (totalTimeToFetchUrl/1e6) + " ms" );
		logger.info("PERF Average url get time " + (totalTimeToFetchUrl/(urlCount * 1e6)) + " ms" );
	}
	
	/**
	 * @param url
	 * @param cookies accept cookies also to send to webserver
	 * @param id This is used to keep track of rate of access to a host. All connect requests
	 *           with same id will get throttled to not exceed a configured rate of requests 
	 * @return
	 */
	public static Document connect(String url, Map<String, String> cookies, String id){
		/* Checking if the URL is Crawlable/scrapable or not */
		if(!isUrlAllowed(url, id)) {
			logger.warn(url + ", not allowed by " + id + " in its robots.txt");
		}
		
		Document doc = null;
		Long lastAccessTime = lastAccess.get(id.toLowerCase());
		if(lastAccessTime != null){
			synchronized(lastAccessTime){						
				long since = System.currentTimeMillis() - lastAccessTime.longValue();
				if(since < CrawlerProtocol.getCrawlDelay(id)){
					try {
						logger.info("Throttle for " + (CrawlerProtocol.getCrawlDelay(id) - since) + "ms before accessing " + url);
						//Add an entry to make sure if there are multiple threads, all of them do not access the url at the same time
						lastAccess.put(id.toLowerCase(), System.currentTimeMillis() + CrawlerProtocol.getCrawlDelay(id) - since);
						lastAccessTime.wait(CrawlerProtocol.getCrawlDelay(id) - since);
					} catch (InterruptedException e) {
					}
				}				
			}
		}
		lastAccess.put(id.toLowerCase(), System.currentTimeMillis());
		for(int retryCount = 1; retryCount <= Constants.MAX_RECONNECT_RETRIES; retryCount++){
			boolean retry = true;
			Throwable connectError = null;
			try{
				long startTime = System.nanoTime();
				long startTimeSeconds = System.currentTimeMillis();
				doc = Jsoup.connect(URLUtils.fixURL(url, id)).userAgent(Constants.USER_AGENT).cookies(cookies).timeout(Constants.PROD_PARSER_CONN_TIME_OUT).get();
				urlCount++;
				long endTime = System.nanoTime();
				long endTimeSeconds = System.currentTimeMillis();
				MetricCollector.RawMetricQueue.add(new RawMetric(id + ":util.Utils.connect", startTimeSeconds, endTimeSeconds ));				
				long timeTaken = endTime-startTime;
				totalTimeToFetchUrl += timeTaken;
				
				break;
			}catch(MalformedURLException e){
				retry=false;
				connectError = e;
			}
			catch(Exception e){
				connectError = e;
			}			
			if(connectError != null && retry){				
				if(retryCount < Constants.MAX_RECONNECT_RETRIES){
					logger.error("Will retry, Caught Exception. Sleep for " + Constants.SLEEP_BTW_RETRIES*retryCount, connectError);
					try {
						long sleepInterval = (long)(Constants.SLEEP_BTW_RETRIES*retryCount); 
						Thread.sleep(sleepInterval);
					} catch (InterruptedException e1) {
					}
				}
				else
					logger.error("Exceeded retry count, Caught Exception" + connectError.toString());
			}//if(connectError != null && retry) ends...	
			else if(connectError != null && !retry){
				logger.error("Retry is set as false, retry won't fix exception" + connectError.toString());
				break;
			}
		}
		if(doc == null) //Unable to fetch url contents
			throw new Bhagte2BandBajGaya("Unable to fetch contents of " + url);
		return doc;		
	}
	
	public static String getHostName(){
		try{
			InetAddress add = InetAddress.getLocalHost();
			return add.getCanonicalHostName();
		}catch(Exception e){
			return "Unknown Host";
		}		
	}
	
	public static void closeStmt(Statement stmt){
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {				
				logger.error(e.getMessage(), e);
			}
		}
	}	
	public static String removeNonFolderChars( String folderName) {
		return folderName.replaceAll("[/\\s'\" ]*", "");
	}
	
	public static boolean isEmpty(String str){
		return str == null || str.trim().equals("");
	}
	
	public static String stripTrailing(String str, String trail){
		while(str.endsWith(trail)){
			str = str.substring(0, str.length() - trail.length());
		}
		return str;
	}
	
	public static Elements getFirstMatching(Document root, String[] classes){		
		Elements elm = null;
		for(String clazz: classes){
			elm = root.getElementsByClass(clazz);
			if(elm != null && elm.size() > 0)
				break;
		}
		return elm;
	}
	
	public static String getTabs(int num){
		StringBuilder tabs = new StringBuilder();
		for(int i = 0; i < num; i++){
			tabs.append("\t");
		}
		return tabs.toString();
	}
	
	public static String getStackTrace(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < stack.length; i++){
			if(i <= 1)
				continue;
			StackTraceElement elm = stack[i];
			sb.append(elm.toString()).append("\n");
		}
		return sb.toString();
	}

    public static void move(Path from, Path to) throws IOException {
        Files.createDirectories(from.getParent());
        Files.createDirectories(to.getParent());
        Files.move(from, to, REPLACE_EXISTING);
    }
	
	public static void main(String[] ars){
		System.out.println(getStackTrace());
	}
}
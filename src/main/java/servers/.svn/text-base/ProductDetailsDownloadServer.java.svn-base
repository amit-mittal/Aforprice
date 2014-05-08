package servers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import util.Constants;
import util.DateTimeUtils;
import util.Metric;
import util.Metrics;
import util.concurrent.Executors;
import work.items.ProductDetailsParseTask;
import entities.ProductSummary;
import global.exceptions.BandBajGaya;

public class ProductDetailsDownloadServer{
	
	private static final Logger LOGGER = Logger.getLogger(ProductDetailsDownloadServer.class);
	private static final ProductDetailsDownloadServer instance = new ProductDetailsDownloadServer();
		
	private Map<String, ThreadPoolExecutor> processorMap = new HashMap<String, ThreadPoolExecutor>();
	
	private ProductDetailsDownloadServer(){
	}
	
	/**
	 * Singleton implementation
	 * @return
	 */
	public static final ProductDetailsDownloadServer get(){
		return instance;
	}
	/**
	 * Downloads the product details associated with this <tt>url</tt>. Retailer is used
	 * to determine the product details parser associated with the <tt>url</tt>. 
	 * 
	 * <p>The retailer is also used to determine the thread which is used to download
	 * as there are dedicated threads for each retailer.
	 * 
	 * <p>This method returns immediately after adding the url to a <tt>Deque</tt> associated
	 * with this retailer.
	 * @param retailer the retailer associated with the url
	 * @param url the url to download
	 */
	public void download(String retailer, String url, ProductSummary prod){
		if(retailer == null){
			LOGGER.warn("skipping download as retailer is null");
			return;
		}		
		if(url == null){
			LOGGER.warn("skipping download as url is null");
			return;
		}			
		if(!processorMap.containsKey(retailer)){			
			processorMap.put(retailer, Executors.newFixedThreadPoolExecutor(Constants.MAX_THREADS_DOWNLOAD(retailer)));
			Metric m = Metrics.getMetric(Metrics.getProductDetailsParserMetricKey(retailer));
			m.setStartTime(System.currentTimeMillis());
		}		
		try {
			Metrics.getMetric(Metrics.getProductDetailsParserMetricKey(retailer)).add(1);
			processorMap.get(retailer).execute(new ProductDetailsParseTask(url, retailer, prod));
		} catch (BandBajGaya e) {			
			LOGGER.error("skipping download as due to " + e.getMessage());				
		}
		//TODO: what is this doing here?
		synchronized(this){
			notifyAll();
		}
	}
	
	/**
	 * Extract the <tt>retailer</tt> and <tt>url</tt> from the products and invoke {@link #download(String, String)}
	 * on each of them  
	 * @param products products for which the details need to be downloaded.
	 */
	public void download(Set<ProductSummary> products){
		for(ProductSummary prod: products){
			if(prod == null){
				LOGGER.warn("skipping download because product summary is null");
				continue;
			}
			String retailer = prod.getRetailerId();
			String url = prod.getUrl();
			download(url, retailer, prod);
		}
	}
	
	/**
	 * Gives the outstanding product detail downloads remaining at the 
	 * instance of invocation of this method. It is possible that more
	 * downloads may be added at a later point of time.
	 * @param retailer
	 * @return
	 */
	public int downloadsRemaining(String retailer){
		ThreadPoolExecutor exec = processorMap.get(retailer);
		if(exec == null)
			return 0;
		return exec.getQueue().size();
	}
	
	/**
	 * Gives all the outstanding product detail downloads remaining for 
	 * all retailers.
	 * @return
	 */
	public int downloadsRemaining(){
		Set<String> retailers = processorMap.keySet();
		int remaining = 0;
		for(String retailer: retailers){
			remaining += downloadsRemaining(retailer);
		}
		return remaining;
	}
	
	public void publish(String[] retailers){
		while(true){
			if(downloadsRemaining() == 0)
				break;
			try {
				Thread.sleep(10*60*1000);
			} catch (InterruptedException e) {				
			}
		}
		Set<String> metricsKeys = new HashSet<>();
		for(String retailer: retailers)
			metricsKeys.add(Metrics.getProductDetailsParserMetricKey(retailer));
		Metrics.publish(DateTimeUtils.currentDateYYYYMMDD() + "-Product Details Download Metrics", metricsKeys);
	}
	
	public static void main(String[] args){
		//get().download("walmart", "http://www.walmart.com/ip/Asus-Nexus-7-ASUS-1B16-with-WiFi-7-Touchscreen-Tablet-PC-Featuring-Android-4.1-Jelly-Bean-Operating-System/21150249");
	}

}
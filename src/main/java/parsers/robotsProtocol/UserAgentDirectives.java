/*
 * @author: Chirag Maheshwari
 */
package parsers.robotsProtocol;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentSkipListSet;

import util.Constants;
import util.Metrics;
import util.Metric;

public class UserAgentDirectives {
	
	private ConcurrentSkipListSet<String> DisallowedDirectives;
	private ConcurrentSkipListSet<String> AllowedDirectives;
	
	private long crawlDelay;
	private String retailerId;
	
	private boolean isEmpty = true;
	
	private int allowedUrlCount = 0,
				disallowedUrlCount = 0;
	
	/*
	 * Metrics to keep track of the time taken to check URL's
	 */
	private Metric metric = Metrics.getMetric("RobotsTxt Exclusion Directives");
	
	public UserAgentDirectives(String retailerId) {
		DisallowedDirectives = new ConcurrentSkipListSet<String>();
		AllowedDirectives = new ConcurrentSkipListSet<String>();
		
		crawlDelay = -1;
		this.retailerId = retailerId;
	}
	
	protected void addDisallow(String path) {
		if((path = path.trim()).length() == 0) {
			/*
			 * Empty disallow means allow all.
			 */
			return;
		}
		DisallowedDirectives.add(path);
		isEmpty = false;
	}
	
	protected void addAllow(String path) {
		if((path = path.trim()).length() == 0)
			return;
		AllowedDirectives.add(path);
		isEmpty = false;
	}
	
	protected void setCrawlDelay(float delay) {
		crawlDelay = (long)(delay * 1000);
		isEmpty = false;
	}
	
	protected void setEmpty(boolean empty) {
		isEmpty = empty;
	}
	
	public long getCrawlDelay() {
		return Math.max(Constants.WAIT_CALC.MIN_WAIT_ACCESS(retailerId), crawlDelay);
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	private int matchUrlDisallowed(String path) {
		String matchedDirective = DisallowedDirectives.floor(path);
		if(matchedDirective != null && path.startsWith(matchedDirective)) {
			return matchedDirective.length();
		}
		else
			return -1;
	}
	
	private int matchUrlAllowed(String path) {
		String matchedDirective = AllowedDirectives.floor(path);
		if(matchedDirective != null && path.startsWith(matchedDirective))
			return matchedDirective.length();
		else
			return -1;
	}
	
	public boolean isUrlAllowed(String urlString) throws MalformedURLException{
		try {
			URL url = new URL(urlString);
			return isUrlAllowed(url);
		} catch(MalformedURLException ex) {
			throw ex;
		}
	}
	
	public boolean isUrlAllowed(URL url) {
		metric.start();
			if(matchUrlDisallowed(url.getPath()) > matchUrlAllowed(url.getPath())) {
				metric.end();
				disallowedUrlCount++;
				
				return false;
			}
			else {
				metric.end();
				allowedUrlCount++;
				
				return true;	
			}
	}
	
	public int getDisallowedCount() {
		return disallowedUrlCount;
	}
	
	public int getAllowedCount() {
		return allowedUrlCount;
	}
	
	public String getPerformance() {
		return metric.currentStats();
	}
}
package parsers.robotsProtocol;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.Constants;

//TODO: Initialize before using as initialization for a retailer takes some time for downloading and parsing
public class CrawlerProtocol {
	private static final Logger logger = Logger.getLogger(CrawlerProtocol.class);
	
	private static Map<String, RobotsTxt> robotsTxtParsers = new HashMap<>();
	private static Map<String, UserAgentDirectives> directivesMap = new HashMap<>();
	
	public static RobotsTxt getRobotsExclusion(String retailerId){
		if(robotsTxtParsers.containsKey(retailerId)){
			return robotsTxtParsers.get(retailerId);
		}
		
		robotsTxtParsers.put(retailerId, new RobotsTxt(retailerId));
		return robotsTxtParsers.get(retailerId);
	}
	
	public static UserAgentDirectives getUserAgentDirectives(String retailerId) {
		UserAgentDirectives directive = directivesMap.get(retailerId);
		
		if(directive != null) {
			return directive;
		} else {
			directivesMap.put(retailerId, getRobotsExclusion(retailerId).getDirectivesForUserAgent(Constants.USER_AGENT));
			return directivesMap.get(retailerId);
		}
	}
	
	public static void initializeDirectives(String retailerId) {
		getUserAgentDirectives(retailerId);
	}
	
	public static boolean isUrlAllowed(String retailerId, String url) {
		try {
			return getUserAgentDirectives(retailerId).isUrlAllowed(url);
		} catch (MalformedURLException ex) {
			logger.warn(url + "is malformed. Url not allowed.");
			return false;
		}
	}
	
	public static long getCrawlDelay(String retailerId) {
		return getUserAgentDirectives(retailerId).getCrawlDelay();
	}
	
	public static void publishStatics(String retailerId) {
		int allowedUrl = getUserAgentDirectives(retailerId).getAllowedCount(),
			disallowedUrl = getUserAgentDirectives(retailerId).getDisallowedCount();
		
		logger.info(retailerId + "\n Total Url's filtered: " + (allowedUrl + disallowedUrl) + 
				"\t(Disallowed: " + disallowedUrl + ", allowed: " + allowedUrl + ")" + 
				"\n" + getUserAgentDirectives(retailerId).getPerformance());
	}
}
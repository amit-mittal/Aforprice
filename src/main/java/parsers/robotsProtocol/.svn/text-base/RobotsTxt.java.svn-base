/*
 * @author Chirag Maheshwari
 */
package parsers.robotsProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import entities.Retailer;

import util.Constants;

public class RobotsTxt {
	private String robotsTxtUrl;
	private Retailer retailer;
	
	private static final Logger logger = Logger.getLogger(RobotsTxt.class);
	
	private static final String COMMENT_LINE = "#";
	private static final Pattern HTML_TAGS_PATTERN = Pattern.compile("<[^>]+>");
	private static final Pattern USER_AGENT_PATTERN = Pattern.compile("(?i)^User-agent:.*");
	private static final Pattern DISALLOW_DIRECTIVE_PATTERN = Pattern.compile("(?i)Disallow:.*");
	private static final Pattern ALLOW_DIRECTIVE_PATTERN = Pattern.compile("(?i)Allow:.*");
	private static final Pattern CRAWL_DELAY_DIRECTIVE_PATTERN = Pattern.compile("(?i)Crawl-delay:.*");
	private static final Pattern SITEMAP_PATTERN = Pattern.compile("(?i)Sitemap:.*");
	
	private static final String CRLF = "\r\n|\r|\n";
	
	protected static final long MAX_SIZE = 500*1024;
	
	private List<String> baseSitemapIndexes;
	private List<String> UserAgents;
	private Map<String, UserAgentDirectives> userAgentToDirectives;
	private UserAgentDirectives wildCardDirective;
	
	private boolean hasErrors = false;
	
	public RobotsTxt(String retailerId) throws IllegalArgumentException {
		retailer = Retailer.getRetailer(retailerId);
		
		if(retailer != null)
			this.robotsTxtUrl = extractRobotsTxtPath(retailer.getLink());
		else {
			logger.error("No retailer exists with ID - " + retailerId);
			throw new IllegalArgumentException("No retailer exists with ID - " + retailerId);
		}
		
		if(this.robotsTxtUrl.length() != 0)
			initializeFromUrl();
		
		if(wildCardDirective == null) {
			/*
			 * This is the default directives to fall-back to when 
			 * directives specific to our user-agent is not found.
			 * 
			 * If wildCardDirectives are null, it just means empty directives for remaining user-agents.
			 */
			wildCardDirective = new UserAgentDirectives(retailer.getId());
		}
	}
	
	private String extractRobotsTxtPath(String siteUrl) {
		try {
			URL urlObj = new URL(siteUrl);
			return urlObj.getProtocol() + "://" + urlObj.getHost() + (urlObj.getPort() != -1? ":" + urlObj.getPort() : "") + "/robots.txt";
		} catch (MalformedURLException ex) {
			logger.error(siteUrl + " is a malformed Url. Cannot get robots.txt file.");
			return "";
		}
	}
	
	private void initializeFromUrl() {
		try {
			logger.info("Getting robots.txt file for " + retailer.getId());
			String robotsFile = new String(Jsoup.connect(this.robotsTxtUrl).userAgent(Constants.USER_AGENT).timeout(Constants.PROD_PARSER_CONN_TIME_OUT).execute().bodyAsBytes(), "UTF-8");
			logger.info("Got robots.txt file for " + retailer.getId());
			
			parseRobotsTxt(robotsFile);
		} catch (Exception ex) {
			logger.error("Couldnt fetch robots.txt file for " + retailer.getId() + ". all Urls allowed");
		}
	}
	
	private void parseRobotsTxt(String robotsTxtStr) {
		logger.info("Started parsing of robots.txt for " + retailer.getId());
		
		String lines[] = robotsTxtStr.split(CRLF);
		long charCount = 0;
		
		baseSitemapIndexes = new ArrayList<String>();
		userAgentToDirectives = new HashMap<String, UserAgentDirectives>();
		UserAgents = new ArrayList<String>();
		
		String CurrentUserAgent = null;
		UserAgentDirectives currentDirectives = null;
		String path;
		
		for(int i=0; i<lines.length; i++) {
			if(charCount >= MAX_SIZE) {
				logger.warn("Already processed " + charCount + "characters, ignoring the rest as given in HER-1990");
				break;
			} else {
				lines[i] = lines[i].trim();
				if(lines[i].length() == 0)
					continue;
				charCount += lines[i].length();
				
				/*
				 * Removing the comment/HTML markup part of the line (if any exists); 
				 */
				lines[i] = HTML_TAGS_PATTERN.matcher(lines[i]).replaceAll("");
				int commentIndex = lines[i].indexOf(COMMENT_LINE);
				if(commentIndex > -1) {
					lines[i] = lines[i].substring(0, commentIndex).trim();
				}
				/*
				 * If the whole line was a comment or contains any unnecessary html markup,
				 * now the line should be empty as the comment has been removed.
				 */
				if(lines[i].length() == 0)
					continue;
				
				/* If the line is a user agent line(the upcoming lines must be directives) */
				if(USER_AGENT_PATTERN.matcher(lines[i]).matches()) {
					CurrentUserAgent = lines[i].substring(11).trim().toLowerCase();
					
					if(currentDirectives == null || !currentDirectives.isEmpty()) {
						currentDirectives = new UserAgentDirectives(retailer.getId());
					}
					
					UserAgents.add(CurrentUserAgent);
					
					if(CurrentUserAgent.equals("*")) {
						wildCardDirective = currentDirectives;
					} else {
						userAgentToDirectives.put(CurrentUserAgent, currentDirectives);
					}
					
				} else if(DISALLOW_DIRECTIVE_PATTERN.matcher(lines[i]).matches()) {
					if(CurrentUserAgent == null) {
						/*
						 * robots.txt is not properly formatted.
						 * Ignoring and continuing.
						 */
						hasErrors = true;
						continue;
					}
					
					path = lines[i].substring(9).trim();
					if(path.endsWith("*"))
						path = path.substring(0, path.length() - 1);
					if(path.indexOf('*') != -1 || path.indexOf('$') != -1) {
						/*
						 * Contains some wildcard characters
						 * not supported, thus ignoring.
						 */
//						TODO: Support for wildCard characters in paths
						logger.warn("Ignoring disallowed path in robots.txt: " + path);
						continue;
					}
					
					currentDirectives.addDisallow(path);
				} else if(ALLOW_DIRECTIVE_PATTERN.matcher(lines[i]).matches()) {
					if(CurrentUserAgent == null) {
						/*
						 * robots.txt is not properly formatted.
						 * Ignoring and continuing.
						 */
						hasErrors = true;
						continue;
					}
					
					path = lines[i].substring(6).trim();
					if(path.endsWith("*"))
						path = path.substring(0, path.length() - 1);
					if(path.indexOf('*') != -1 || path.indexOf('$') != -1) {
						/*
						 * Contains some wildcard characters
						 * not supported, thus ignoring.
						 */
						logger.warn("Ignoring allowed path in robots.txt: " + path);
//						TODO: Support for wildCard characters in paths
						continue;
					}
					
					currentDirectives.addAllow(path);
				} else if(SITEMAP_PATTERN.matcher(lines[i]).matches()) {
					
					path = lines[i].substring(8).trim();
					baseSitemapIndexes.add(path);

				} else if(CRAWL_DELAY_DIRECTIVE_PATTERN.matcher(lines[i]).matches()) {
					if(CurrentUserAgent == null) {
						/*
						 * robots.txt is not properly formatted.
						 * Ignoring and continuing.
						 */
						hasErrors = true;
						continue;
					}
					
					currentDirectives.setEmpty(false);
					path = lines[i].substring(12).trim();
					try {
						path = path.split("[^\\d\\.]+")[0];
						currentDirectives.setCrawlDelay(Float.parseFloat(path));
					} catch(Exception ex) {
						/*
						 * Ignoring the error as it means nothing.
						 * Just faulty robots.txt thus default crawl delay must be used.
						 */
						hasErrors = true;
					}
				}
			}
		}
		
		if(hasErrors)
			logger.warn("Finished parsing of robots.txt for " + retailer.getId() + ". robots.txt is not properly formatted.");
		else
			logger.info("Finished parsing of robots.txt for " + retailer.getId());
 	}
	
	public List<String> getBaseSitemapIndexes() {
		return baseSitemapIndexes;
	}
	
	public UserAgentDirectives getDirectivesForUserAgent(String userAgent) {
		UserAgentDirectives toReturn = null;
		if(userAgentToDirectives != null)
			toReturn = userAgentToDirectives.get(userAgent);
		
		if(toReturn == null)
			return wildCardDirective;
		else
			return toReturn;
	}
	
	public List<String> getAllUSerAgents() {
		return UserAgents;
	}
	
	/*public static void main(String args[]) throws Exception{
		RobotsTxt parser = new RobotsTxt("amazonbs");
		UserAgentDirectives directives = parser.getDirectivesForUserAgent(Constants.USER_AGENT);
		directives.isUrlAllowed(new URL("http://www.amazon.com/"));
	}*/
}
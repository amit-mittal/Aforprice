/*
 * @author Chirag Maheshwari
 */
package sitemap.entities;

import java.sql.Timestamp;

public class ProductURL {
	
	public enum ChangeFrequency {
		ALWAYS,
		HOURLY,
		DAILY,		// default
		WEEKLY,
		MONTHLY,
		YEARLY,
		NEVER
	}
	
	private int urlId;
	private Timestamp creationTime;
	
	private final String retailerId;
	private String url;
	private Timestamp lastModified;
	private ChangeFrequency changeFreq;
	private float priority;
	
	private StringBuilder toStringBuilder = new StringBuilder();
	
	public ProductURL(String retailerID, String url, String lastModified, String changeFreq, String priority) {
		this.retailerId = retailerID;
		this.url = url;
	}
	
	public ProductURL(String retailerID, String url, String lastModified, String changeFreq) {
		this.retailerId = retailerID;
		this.url = url;
	}
	
	public ProductURL(String retailerID, String url, String lastModified) {
		this.retailerId = retailerID;
		this.url = url;
		
		this.changeFreq = ChangeFrequency.DAILY;
	}
	
	public ProductURL(String retailerID, String url) {
		this.retailerId = retailerID;
		this.url = url;
		
		this.changeFreq = ChangeFrequency.DAILY;
	}
	
	public static final class columns {
		public static final String ID = "ID";
		public static final String RETAILER = "RETAILER_ID";
		public static final String URL = "URL";
		public static final String LASTMOD = "LAST_MODIFIED";
		public static final String LASTUPDATED = "LAST_UPDATED";
		public static final String CHANGE = "CHANGE_FREQUENCY";
		public static final String PRIORITY = "PRIORITY";
	}
}

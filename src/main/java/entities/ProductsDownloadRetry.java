package entities;

import java.util.Date;

import util.YesNo;
import util.build.ProductsDownloadRetryBuilder;

public class ProductsDownloadRetry {

	private final int id;
	private final String retailerId;
	private final int categoryId;
	private final String categoryName;
	private final String startUrl;
	private final Date createTime;
	private final String retryReason;
	private final YesNo processed;
	
	public ProductsDownloadRetry(ProductsDownloadRetryBuilder b) {
		this.id = b.id;
		this.retailerId = b.retailerId;
		this.categoryId = b.categoryId;
		this.categoryName = b.categoryName;
		this.startUrl = b.startUrl;
		this.createTime = b.createTime;
		this.retryReason = b.retryReason;
		this.processed = b.processd;
	}
	
	public int getId() {
		return id;
	}

	public String getRetailerId() {
		return retailerId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getRetryReason() {
		return retryReason;
	}
	
	public YesNo getProcessed(){
		return processed;
	}

	public static final class Columns{
		public static final String ID = "ID";
		public static final String RETAILER_ID = "RETAILER_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String CATEGORY_NAME = "CATEGORY_NAME";
		public static final String START_URL = "START_URL";
		public static final String CREATE_TIME = "CREATE_TIME";
		public static final String PROCESSED = "PROCESSED";
		public static final String RETRY_REASON = "RETRY_REASON";
	}
}
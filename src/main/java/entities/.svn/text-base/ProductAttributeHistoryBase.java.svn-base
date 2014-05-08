package entities;

import java.util.Date;

import util.build.ProductAttributeHistoryBaseBuilder;

public class ProductAttributeHistoryBase {
	
	private int productId;
	private Date time;
	private Date  timeModified;

	public ProductAttributeHistoryBase(ProductAttributeHistoryBaseBuilder b) {
		this.productId = b.productId;
		this.time = b.time;
		this.timeModified = b.timeModified;
	}

	public int getProductId() {
		return productId;
	}

	public Date getTime() {
		return time;
	}

	public Date getTimeModified() {
		return timeModified;
	}
	
	public static class Columns{
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String TIME = "TIME";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
		public static final String RATING = "RATING";
		public static final String NUM_REVIEWS = "NUM_REVIEWS";
		public static final String PRICE = "PRICE";
		public static final String RANK = "RANK";

	}


}

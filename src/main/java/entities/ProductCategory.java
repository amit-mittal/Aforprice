package entities;

import java.sql.Timestamp;

import util.build.ProductCategoryBuilder;

public class ProductCategory {
	private final int prodId;
	private final int categoryId;
	private final Timestamp timeModified;
	
	public ProductCategory(ProductCategoryBuilder b){
		this.prodId = b.prodId;
		this.categoryId = b.categoryId;
		this.timeModified = b.timeModified;
	}

	public int getProductId() {
		return prodId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public Timestamp getTimeModified() {
		return timeModified;
	}
	
	public static class Columns{
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
	}
}
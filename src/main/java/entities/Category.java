package entities;

import java.util.Date;

import uploader.util.CategoryType;

public class Category {
	
	private int categoryId;
	private String categoryName;
	private String url;
	private String retailerId;
	private int parentCategoryId;
	private String parentName;
	private int genericCategoryId;
	private String genericCategoryName;
	private Boolean isActive;
	private CategoryType catType;
	private String uniqueId;	
	private String retailerCategoryId;
	private Date timeModified;
	
	public Category(CategoryBuilder b){
		this.categoryId = b.categoryId;
		this.categoryName = b.name;
		this.url = b.url;
		this.retailerId = b.retailerId;
		this.parentCategoryId = b.parentCategoryId;
		this.parentName = b.parentName;
		this.genericCategoryId = b.genericCategoryId;
		this.genericCategoryName = b.genericName;
		this.isActive = b.isActive;
		this.catType = b.catType;
		this.retailerCategoryId = b.retailerCategoryId;
		initUniqueId();
	}
	
	public Category(util.build.CategoryBuilder b){
		this.categoryId = b.categoryId;
		this.categoryName = b.name;
		this.url = b.url;
		this.retailerId = b.retailerId;
		this.parentCategoryId = b.parentCategoryId;
		this.parentName = b.parentName;
		this.genericCategoryId = b.genericCategoryId;
		this.genericCategoryName = b.genericName;
		this.isActive = b.isActive;
		this.catType = b.catType;
		this.timeModified = b.timeModified;
		this.retailerCategoryId = b.retailerCategoryId;
		initUniqueId();
	}
	
	public Category(int catId, String retailerId, String name, String url){
		this.categoryId = catId;
		this.categoryName = name;
		this.url = url;
		this.retailerId = retailerId;
	}
	
	private void initUniqueId(){
		this.uniqueId = this.getParentName() + "{"+this.getParentCategoryId()+"}->" + this.getName();
	}
	
	public int getCategoryId(){
		return categoryId;
	}
	public void setCategoryId(int id){
		this.categoryId = id;
	}
	public int getParentCategoryId() {
		return parentCategoryId;
	}

	public boolean isRootCategory(){
		return this.parentCategoryId == 0;
	}
	public void setParentCategoryId(int parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getGenericName() {			
		return genericCategoryName;
	}
	public String getName() {
		return categoryName;
	}
	public void setName(String name) {
		this.categoryName=name;
	}
	public String getParentName() {
		return parentName;
	}

	/**
	 * @return the genericCategoryId
	 */
	public int getGenericCategoryId() {
		return genericCategoryId;
	}

	/**
	 * @param genericCategoryId the genericCategoryId to set
	 */
	public void setGenericCategoryId(int genericCategoryId) {
		this.genericCategoryId = genericCategoryId;
	}

	public String getUrl() {
		return url;
	}
	public String getRetailerId() {
		return retailerId;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public CategoryType getType(){
		return catType;
	}

	public void setType(CategoryType catType){
		this.catType = catType;	
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUniqueId(){
		return this.uniqueId;
	}	
	
	@Deprecated
	public String getRetailerCategoryId() {
		return retailerCategoryId;
	}
	
	public Date getTimeModified(){
		return timeModified;
	}

	public String toString(){
		return categoryId + "\t" + categoryName + "\t" + url + "\t" + (catType);
	}
	
	public boolean equals(Object obj){
		Category compareTo = (Category) obj;
		boolean result;
		if(this.getParentName()!=null && compareTo.getParentName()!=null)
			result = this.getParentName().equals(compareTo.getParentName());
		else
			result = this.getParentName() == null && compareTo.getParentName()==null;
		 result =   result &&
				this.getName().equals(compareTo.getName()) &&
//				this.getGenericName().equals(compareTo.getGenericName()) &&
//				this.getUrl().equals(compareTo.getUrl()) &&
				this.getRetailerId().equals(compareTo.getRetailerId()) &&
				this.isActive() == compareTo.isActive() && 
				this.getType().equals(compareTo.getType());
		return result;
	}
	
	public static final class Columns{
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String CATEGORY_NAME = "CATEGORY_NAME";
		public static final String GENERIC_CATEGORY_ID = "GENERIC_CATEGORY_ID";
		public static final String GENERIC_CATEGORY_NAME = "GENERIC_CATEGORY_NAME";
		public static final String PARENT_CATEGORY_ID = "PARENT_CATEGORY_ID";
		public static final String PARENT_CATEGORY_NAME = "PARENT_CATEGORY_NAME";
		public static final String RETAILER_ID = "RETAILER_ID";
		public static final String NAME = "CATEGORY_NAME";
		public static final String URL = "URL";
		public static final String ACTIVE = "ACTIVE";
		public static final String PARENT = "PARENT";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String EXT_ID = "EXT_ID";

	}
	
	@Deprecated
	public static class CategoryBuilder{
		private int categoryId;
		private String name;
		private String url;
		private String retailerId;
		private int parentCategoryId;
		private String parentName;
		private int genericCategoryId;
		private String genericName;
		private Boolean isActive = true;
		private CategoryType catType;
		private String retailerCategoryId; //used for amazon who has internal id for each category
		public CategoryBuilder(String retailerId, Category parent, String name, String url){
			this.retailerId = retailerId;
			if(parent==null){//root category
				this.parentCategoryId = 0;
				this.parentName="";
			}else{
				this.parentCategoryId = parent.getCategoryId();
				this.parentName = parent.getName();
			}
			this.name = name;
			this.url = url;
		}

		public CategoryBuilder(String retailerId, int parentCategoryId, String parentName, String name, String url){
			this.retailerId = retailerId;
			this.parentCategoryId = parentCategoryId;
			this.parentName=parentName;
			this.name = name;
			if(url!=null)
				this.url = url;
			else
				this.url = "";
		}

		public CategoryBuilder categoryId(int categoryId){
			this.categoryId = categoryId;
			return this;
		}
		public CategoryBuilder parentCategoryId(int parentCategoryId){
			this.parentCategoryId = parentCategoryId;
			return this;
		}
		public CategoryBuilder parentName(String parentName){
			this.parentName = parentName;
			return this;
		}
		public CategoryBuilder genericCategoryId(int genericCategoryId){
			this.genericCategoryId = genericCategoryId;
			return this;
		}
		public CategoryBuilder genericName(String genericName){
			this.genericName = genericName;
			return this;
		}
		public CategoryBuilder isActive(Boolean isActive){
			this.isActive = isActive;
			return this;
		}
		public CategoryBuilder catType(CategoryType catType){
			this.catType = catType;
			return this;
		}
		public CategoryBuilder retailerCategoryId(String retailerCategoryId){
			this.retailerCategoryId = retailerCategoryId;
			return this;
		}
		public Category build(){
			return new Category(this);
		}
	}
}
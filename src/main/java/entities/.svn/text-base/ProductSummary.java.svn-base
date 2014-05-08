package entities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parsers.util.PriceTypes;
import parsers.util.ProductUID;
import util.Constants;
import util.ProductUtils;
import util.ToStringBuilder;
import util.URL;
import util.build.ProductSummaryBuilder;

public class ProductSummary implements ToStringBuilder{	
		
	private int prodId = -1;
	private long downloadId = -1;
	private final Date creationTime;
	private final String retailerId;
	private int categoryId;
	private String name;	
	private final String url;
	private String imageUrl;
	//private final String desc;
	private String model;
	private Date downloadTime;
	private URL urlObj; //Gets set lazily
	private double price;
	private Date timeOfPrice;
	private List<Integer> categories = new ArrayList<Integer>();
	private String categoryName;
	private int salesRank;
	private double reviewRating;
	private int numReviews;
	public boolean active=true;

	public ProductSummary(ProductSummaryBuilder builder){
		this.prodId = builder.prodId;
		this.downloadId = builder.downloadId;
		this.retailerId = builder.retailerId;
		this.categoryId = builder.categoryId;
		if(this.categoryId!=0)//we get 0 when we read product from product_summary table
			this.addCategory(this.categoryId);
		this.name = builder.name;
		this.price = builder.price;
		this.url = builder.url;
		this.imageUrl = builder.imageUrl;
		//this.desc = builder.desc;
		this.model = builder.model;
		this.salesRank = builder.salesRank;
		this.reviewRating = builder.reviewRating;
		this.numReviews = builder.numReviews;
		this.creationTime = builder.creationTime;
		this.downloadTime = builder.downloadTime;
		this.active = builder.active;
	}
	
	public ProductSummary(int prodId, String retailerId, 
							int categoryId, String categoryName, 
							String name, double price, String url, 
							String imageUrl, String desc, 
							String model, Date creationTime, Date lastDownloadTime){
		this.prodId = prodId;
		this.retailerId = retailerId;
		this.categoryId = categoryId;
		this.name = name;
		this.price = price;
		this.url = url;
		this.imageUrl = imageUrl;
		//this.desc = desc;
		this.model = model;
		this.creationTime = creationTime;
		this.downloadTime = lastDownloadTime;
	}

	public ProductSummary(int prodId, String retailerId, int categoryId, String categoryName, String name, double price, String url, String imageUrl, String desc, String model, Date lastDownloadTime){
		this(-1, retailerId, categoryId, categoryName, name, price, url, imageUrl, desc, model, lastDownloadTime, lastDownloadTime);
	}
	public ProductSummary(String retailerId, int categoryId, String categoryName, String name, double price, String url, String imageUrl, String desc, String model){
		this(-1, retailerId, categoryId, categoryName, name, price, url, imageUrl, desc, model, new Date());
	}

	public ProductSummary(String retailerId, int categoryId, String categoryName, String name, double price, String url, String imageUrl, String desc, String model, Date creationTime, Date lastDownloadTime){
		this(-1, retailerId, categoryId, categoryName, name, price, url, imageUrl, desc, model, creationTime, lastDownloadTime);
	}

	public ProductSummary(String retailerId, int categoryId, String categoryName, String name, double price, String url, String imageUrl, String desc, String model, Date lastDownloadTime){
		this(-1, retailerId, categoryId, categoryName, name, price, url, imageUrl, desc, model, lastDownloadTime);
	}
	
	public ProductSummary(String retailerId, String name, double price, String url, String imageUrl, String desc, String model){
		this(-1, retailerId, -1, "", name, price, url, imageUrl, desc, model, new Date());
	}
	
	public ProductSummary(String retailerId, String name, String url, String imageUrl, String desc, String model, Date creationTime, Date lastDownloadTime){
		this(-1, retailerId, -1, "", name, PriceTypes.UNKNOWN.getValue(), url, imageUrl, desc, model, creationTime, lastDownloadTime);
	}

	
	public static ProductSummary getDummyProductQA(String retailerId, int categoryId, String categoryName){
		return new ProductSummary(retailerId, categoryId, categoryName, "", PriceTypes.UN_PARSEABLE.getValue(), "", "", null, "");
	}	
			
	public void setId(int id){
		this.prodId = id;
	}
	
	public int getId(){
		return prodId;
	}
	
	public long getDownloadId(){
		return downloadId;
	}
	
	public void setDownloadId(long id){
		this.downloadId = id;
	}
	
	public Date getCreationTime(){
		return this.creationTime;
	}

	public Date getDownloadTime(){
		return this.downloadTime;
	}
	
	public String getRetailerId(){
		return retailerId;
	}
	
	public void setCategoryId(int categoryId){
		this.categoryId=categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}
	
	public String getCategoryName(){
		return categoryName;
	}
	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public String getName() {
		return name!=null?name:"";
	}
	public String getNameLower() {
		return getName().toLowerCase();
	}

	public String getDesc() {
		return null;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {	
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public URL getURL(){
		if(urlObj == null)
			try {
				urlObj = new URL(url, null);
			} catch (MalformedURLException e) {				
			}
		return urlObj;
	}
	
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setModel(String model){
		this.model = model;
	}
	public String getModel() {
		return model!=null?model:"";
	}
	public String getModelLower(){
		return getModel().toLowerCase();
	}
	
	public Date getTimeOfPrice(){
		return timeOfPrice;
	}
	public void setTimeOfPrice(Date ts){
		this.timeOfPrice = ts;
	}
	
	public void addCategory(Integer categoryId){
		this.categories.add(categoryId);
	}
	public void setCategories(List<Integer> categories){
		this.categories = categories;
	}
	public void addCategories(List<Integer> categories){
		this.categories.addAll(categories);
	}
	public List<Integer> getCategories(){
		return categories;
	}

	public int getSalesRank(){
		return salesRank;
	}

	public void setReviewRating(double reviewRating){
		this.reviewRating = reviewRating;
	}
	public double getReviewRating() {
		return reviewRating;
	}

	public void setNumReviews(int numReviews){
		this.numReviews = numReviews;
	}
	public int getNumReviews() {
		return numReviews;
	}

	public void setActive(boolean active){
		this.active = active;
	}
	
	public boolean isActive(){
		return active;
	}
	public void setDownloadTime(Date downloadTime){
		this.downloadTime=downloadTime;
	}
	
	@Override
	public String toString(){
		return toString(new StringBuilder());
	}
	
	@Override
	public boolean equals(Object obj){
		try{
			ProductSummary that = (ProductSummary)obj;
			return ProductUtils.areEqual(this, that);
		}
		catch(Exception e){
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		String uid = ProductUID.get(retailerId, url);
		if(uid.equals(ProductUID.UNKNOWN))
			return super.hashCode();//If the uid is unknown, let the hash-code be unique
		return uid.hashCode();
	}
	
	@Override
	public String toString(StringBuilder sb){
		sb.append(downloadTime).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(prodId).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(retailerId).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(categoryId).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(name).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(price).append(Constants.PRODUCTS_ATTR_COL_SEP) 
		.append(url).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(imageUrl).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(salesRank).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(reviewRating).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(numReviews).append(Constants.PRODUCTS_ATTR_COL_SEP)
		.append(model);
		return sb.toString();
	}

	public static final class Columns{
		public static final String ID = "ID";
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String RETAILER_ID = "RETAILER_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String PRODUCT_NAME = "PRODUCT_NAME";
		public static final String MODEL_NO = "MODEL_NO";
		public static final String DESCRIPTION = "DESCRIPTION";
		public static final String IMAGE_URL = "IMAGE_URL";		
		public static final String URL = "URL";		
		public static final String PRICE = "PRICE";
		public static final String TIME = "TIME";
		public static final String DOWNLOAD_TIME = "DOWNLOAD_TIME";
		public static final String LAST_DOWNLOAD_TIME = "LAST_DOWNLOAD_TIME";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
		public static final String START_DATE = "START_DATE";	
		public static final String REVIEW_RATING = "REVIEW_RATING";
		public static final String NUM_REVIEWS = "NUM_REVIEWS";
		public static final String BEST_SELLER_RANK = "BEST_SELLER_RANK";
		public static final String ACTIVE = "ACTIVE";
	}
}
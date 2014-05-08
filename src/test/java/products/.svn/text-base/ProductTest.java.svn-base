package products;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import thrift.genereated.retailer.Review;
import util.URL;
import entities.ProductCategory;
import global.exceptions.BandBajGaya;

/**
 * 
 */

/**
 * @author Ashish
 *
 */
public class ProductTest {
	private int prodId;
	private Date creationTime;	
	private final String retailerId;
	private List<Integer> categoryIds;
	private final String name;	
	private Timestamp timeOfPrice;
	private TreeMap<Date, Double> priceHistory;
	private TreeMap<Date, Review> reviewHistory;
	private String url;
	private URL urlObj; //Gets set lazily
	private final String imageUrl;
	private List<ProductCategory> prodCategories;
	private String desc;
	private String model = "";
	private Date lastDownloadTime;
	private boolean active=true;
	
	public ProductTest(ProductBuilder builder){
		this.retailerId = builder.retailerId;
		this.categoryIds = builder.categoryIds;
		this.name = builder.name;
		this.url = builder.url;
		this.imageUrl = builder.imageUrl;
		this.desc = builder.desc;		
		this.creationTime = builder.creationTime;
		this.model = builder.model;
		this.priceHistory = builder.priceHistory;
		this.reviewHistory = builder.reviewHistory;
		this.lastDownloadTime = builder.lastDownloadTime;
		this.active = builder.active;
	}
	
	public static class ProductBuilder{
		private Date creationTime;	
		private final String retailerId;
		private List<Integer> categoryIds = new ArrayList<Integer>();
		private final String name;	
		private TreeMap<Date, Double> priceHistory;
		private TreeMap<Date, Review> reviewHistory;
		private String url;
		private final String imageUrl;
		private String desc;
		private String model = "";
		private Date lastDownloadTime;
		private boolean active=true;
		
		public ProductBuilder(String retailerId, int categoryId, String name, String url, String imageUrl, String desc, String model, Date creationTime, Date lastDownloadTime){
			this.retailerId = retailerId;
			this.categoryIds.add(categoryId);
			this.name = name;
			this.url = url;
			this.imageUrl = imageUrl;
			this.desc = desc;		
			this.creationTime = creationTime;
			this.model = model;
			this.lastDownloadTime = lastDownloadTime;
			priceHistory = new TreeMap<Date, Double>();
			reviewHistory = new TreeMap<Date, Review>();
		}
		
		public ProductBuilder addCategory(Integer categoryId){
			this.categoryIds.add(categoryId);
			return this;
		}
		public ProductBuilder addPrice(Date time, Double price){
			this.priceHistory.put(time, price);
			return this;
		}
		public ProductBuilder addReview(Date time, Review review){
			this.reviewHistory.put(time, review);
			return this;
		}
		public ProductTest build(){
			return new ProductTest(this);
		}
	}
	
	/**
	 * Unique id for a product built using product model and product name/hash combination. If model is absent, only product name/hash
	 * is used. TODO: We should use product unique identifiers like UPC, SBIN etc. 
	 * @return UID
	 * @throws BandBajGaya 
	 */
	public String getUId() throws BandBajGaya{
		StringBuilder u = new StringBuilder();		
		if(model == null) 
			throw new BandBajGaya("UID does not exists for " + name);
		u.append(model.trim().toLowerCase() + "-");
		u.append(name != null? name.substring(0, Math.min(5, name.length())).trim().toLowerCase() + "-":"");
		u.append(name != null? name.hashCode():"");
		return u.toString();		
	}
		
	public void setId(int id){
		this.prodId = id;
	}
	
	public int getId(){
		return prodId;
	}
	
	public Date getCreationTime(){
		return this.creationTime;
	}
	public void setCreationTime(Date ts){
		this.creationTime = ts;
	}
	
	public String getRetailerId(){
		return retailerId;
	}
	
	public void setCategoryIds(List<Integer> categoryIds){
		this.categoryIds=categoryIds;
	}

	public List<Integer> getCategoryIds(){
		return categoryIds;
	}
	
	public String getName() {
		return name!=null?name:"";
	}
	public String getNameLower() {
		return getName().toLowerCase();
	}

	public String getDesc() {
		return desc;
	}
	public TreeMap<Date, Double> getPriceHistory() {
		return priceHistory;
	}
	public void setPriceHistory(TreeMap<Date, Double> priceHistory) {	
		this.priceHistory = priceHistory;
	}
	public double getLatestRating(){
		return this.reviewHistory.lastEntry().getValue().getReviewRating();
	}
	public int getLatestNumReviews(){
		return this.reviewHistory.lastEntry().getValue().getNumReviews();
	}
	public TreeMap<Date, Review> getReviewHistory() {
		return reviewHistory;
	}
	public void setReviewHistory(TreeMap<Date, Review> reviewHistory) {	
		this.reviewHistory = reviewHistory;
	}
	public void addPrice(Date time, Double price){
		this.priceHistory.put(time, price);
	}
	public double getLatestPrice(){
		return this.priceHistory.lastEntry().getValue();
	}
	public void addReview(Date time, Review review){
		this.reviewHistory.put(time, review);
	}
	public void setUrl(String url){
		this.url = url;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public String getModel() {
		return model!=null?model:"";
	}
	public String getModelLower(){
		return getModel().toLowerCase();
	}
	
	public Timestamp getTimeOfPrice(){
		return timeOfPrice;
	}
	public void setTimeOfPrice(Timestamp ts){
		this.timeOfPrice = ts;
	}
	
	public void setCategories(List<ProductCategory> categories){
		this.prodCategories = categories;
	}
	public List<ProductCategory> getCategories(){
		return prodCategories;
	}

	public Date getLastDownloadTime() {
		return lastDownloadTime;
	}

	public void setLastDownloadTime(Date lastDownloadTime) {
		this.lastDownloadTime = lastDownloadTime;
	}
	
	public boolean isActive(){
		return active;
	}

	@Override
	public int hashCode(){
		return name != null?name.length()%100:0;
	}
	
}

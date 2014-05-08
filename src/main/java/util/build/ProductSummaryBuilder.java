package util.build;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import entities.ProductCategory;
import entities.ProductSummary;

public class ProductSummaryBuilder extends ObjectBuilder<ProductSummary>{

	public int prodId;
	public long downloadId;
	public Date creationTime;
	public String retailerId;
	public int categoryId;
	public String name;	
	public String url;
	public String imageUrl;
	public String desc;
	public String model;
	public Date downloadTime;
	public double price;
	public Date timeOfPrice;
	public TreeMap<Date, Double> priceHistory;
	public List<ProductCategory> prodCategories;
	public String categoryName;
	public int salesRank;
	public double reviewRating;
	public int numReviews;
	public boolean active=true;

	public ProductSummaryBuilder(){
		
	}
	@Override	
	public ProductSummary build() {
		return new ProductSummary(this);
	}
	
	public ProductSummaryBuilder(String retailerId, String name, String url, String imageUrl, String model){
		this.retailerId = retailerId;
		this.name = name;
		this.url = url;
		this.imageUrl = imageUrl;
		this.model = model;
	}
	public ProductSummaryBuilder retailer(String retailer){
		this.retailerId = retailer;
		return this;
	}
	public ProductSummaryBuilder url(String url){
		this.url = url;
		return this;
	}
	public ProductSummaryBuilder categoryId(int categoryId){
		this.categoryId = categoryId;
		return this;
	}

	public ProductSummaryBuilder categoryName(String name){
		this.categoryName = name;
		return this;
	}
	public ProductSummaryBuilder productId(int productId){
		this.prodId = productId;
		return this;
	}
	
	public ProductSummaryBuilder price(double price){
		this.price = price;
		return this;
	}
	
	@Deprecated
	public ProductSummaryBuilder desc(String desc){
		//this.desc=null;
		return this;
	}
	
	public ProductSummaryBuilder timeOfPrice(Date timeOfPrice){
		this.timeOfPrice = timeOfPrice;
		return this;
	}

	public ProductSummaryBuilder downloadTime(Date downloadTime){
		this.downloadTime = downloadTime;
		return this;
	}
	
	public ProductSummaryBuilder creationTime(Date creationTime){
		this.creationTime = creationTime;
		return this;
	}

	public ProductSummaryBuilder priceHistory(TreeMap<Date, Double> priceHistory){
		this.priceHistory = priceHistory;
		return this;
	}
	public ProductSummaryBuilder reviewRating(double reviewRating){
		this.reviewRating = reviewRating;
		return this;
	}	
	public ProductSummaryBuilder numReviews(int numReviews){
		this.numReviews = numReviews;
		return this;
	}	
	public ProductSummaryBuilder salesRank(int salesRank){
		this.salesRank = salesRank;
		return this;
	}
	public ProductSummaryBuilder active(boolean active) {
		this.active = active;
		return this;
	}	
}

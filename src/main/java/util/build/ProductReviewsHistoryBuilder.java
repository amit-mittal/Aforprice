package util.build;

import entities.ProductReviewsHistory;

public class ProductReviewsHistoryBuilder extends ProductAttributeHistoryBaseBuilder {

	public double rating;
	public int numReviews;
	
	public ProductReviewsHistoryBuilder(ProductAttributeHistoryBaseBuilder base){
		this.productId = base.productId;
		this.time = base.time;
		this.timeModified = base.timeModified;
	}
	
	public ProductReviewsHistoryBuilder(){		
	}

	@Override
	public ProductReviewsHistory build() {
		return new ProductReviewsHistory(this);
	}

}

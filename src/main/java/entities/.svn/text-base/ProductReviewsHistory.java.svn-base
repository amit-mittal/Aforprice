package entities;

import util.build.ProductReviewsHistoryBuilder;

public class ProductReviewsHistory extends ProductAttributeHistoryBase{

	private double rating;
	private int numReviews;

	public ProductReviewsHistory(ProductReviewsHistoryBuilder builder) {
		super(builder);
		rating = builder.rating;
		numReviews = builder.numReviews;
	}

	public double getRating() {
		return rating;
	}

	public int getNumReviews() {
		return numReviews;
	}
	
	public static final class Columns{
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String TIME = "TIME";
		public static final String RATING = "RATING";
		public static final String NUM_REVIEWS = "NUM_REVIEWS";
		public static final String END_TIME = "END_TIME";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
	}
}

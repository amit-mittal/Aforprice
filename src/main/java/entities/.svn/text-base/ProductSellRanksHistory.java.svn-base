package entities;

import util.build.ProductSellRanksHistoryBuilder;

public class ProductSellRanksHistory extends ProductAttributeHistoryBase{

	private int rank;

	public ProductSellRanksHistory(ProductSellRanksHistoryBuilder b) {
		super(b);
		this.rank = b.rank;
	}

	public int getRank() {
		return rank;
	}
	
	public static final class Columns{
		public static final String PRODUCT_ID = "PRODUCT_ID";
		public static final String TIME = "TIME";
		public static final String RANK = "RANK";
		public static final String END_TIME = "END_TIME";
		public static final String TIME_MODIFIED = "TIME_MODIFIED";
	}
}
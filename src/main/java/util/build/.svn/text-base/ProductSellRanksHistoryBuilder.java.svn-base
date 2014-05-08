package util.build;

import entities.ProductSellRanksHistory;

public class ProductSellRanksHistoryBuilder extends ProductAttributeHistoryBaseBuilder {
	
	
	public int rank;
	
	public ProductSellRanksHistoryBuilder(ProductAttributeHistoryBaseBuilder base){
		this.productId = base.productId;
		this.time = base.time;
		this.timeModified = base.timeModified;
	}
	
	public ProductSellRanksHistoryBuilder(){
		
	}
		@Override
	public ProductSellRanksHistory build() {
		return new ProductSellRanksHistory(this);
	}

}

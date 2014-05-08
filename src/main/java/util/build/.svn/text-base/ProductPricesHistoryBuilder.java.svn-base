package util.build;

import java.util.Date;

import entities.ProductPricesHistory;

public class ProductPricesHistoryBuilder extends ProductAttributeHistoryBaseBuilder {

	public double price;
	
	public ProductPricesHistoryBuilder(){		
	}
	
	public ProductPricesHistoryBuilder(ProductAttributeHistoryBaseBuilder base){
		this.productId = base.productId;
		this.time = base.time;
		this.timeModified = base.timeModified;
	}
	
	@Override
	public ProductPricesHistory build() {
		return new ProductPricesHistory(this);
	}
	public ProductPricesHistoryBuilder productId(int productId){
		this.productId=productId;
		return this;
	}
	public ProductPricesHistoryBuilder time(Date time){
		this.time=time;
		return this;
	}
	public ProductPricesHistoryBuilder timeModified(Date timeModified){
		this.timeModified=timeModified;
		return this;
	}
	public ProductPricesHistoryBuilder price(double price){
		this.price=price;
		return this;
	}

}

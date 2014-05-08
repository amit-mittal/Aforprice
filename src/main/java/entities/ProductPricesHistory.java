package entities;

import util.build.ProductPricesHistoryBuilder;

public class ProductPricesHistory extends ProductAttributeHistoryBase{

	private double price;
	
	public ProductPricesHistory(ProductPricesHistoryBuilder b) {
		super(b);
		this.price = b.price;
	}

	public double getPrice() {
		return price;
	}
}

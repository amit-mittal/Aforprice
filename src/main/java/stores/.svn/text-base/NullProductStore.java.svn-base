package stores;

import java.util.List;

import entities.ProductSummary;

public class NullProductStore implements ProductStore {

	@Override
	public boolean save(List<ProductSummary> products) {
		for(ProductSummary product: products){
			System.out.println(product);
		}
		return true;
	}

	@Override
	public boolean save(ProductSummary product) {
		System.out.println(product);
		return true;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean allProcessed() {
		// TODO Auto-generated method stub
		return false;
	}

}

package stores;

import java.util.List;

import entities.ProductSummary;

public interface ProductStore {
	
	boolean save(List<ProductSummary> products);
	boolean save(ProductSummary product);
	boolean allProcessed();
	void close();
	
	public class Factory{	
		public static ProductStore get(){
			return ProductStoreDbAsync.get();
		}
		public static ProductStore getProductDetailsStore(){
			return ProductDetailsStoreDbAsync.get();			
		}
	}
}
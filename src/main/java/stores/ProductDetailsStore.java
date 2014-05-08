package stores;

import entities.ProductSummary;

public interface ProductDetailsStore extends ProductStore
{
	public boolean save(ProductSummary product, ProductSummary existingProd);

}

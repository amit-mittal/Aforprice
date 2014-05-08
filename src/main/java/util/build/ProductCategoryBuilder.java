package util.build;

import java.sql.Timestamp;

import entities.ProductCategory;

public class ProductCategoryBuilder extends ObjectBuilder<ProductCategory> {

	public int prodId;
	public int categoryId;
	public Timestamp timeModified;
	@Override
	public ProductCategory build() {
		return new ProductCategory(this);
	}

}

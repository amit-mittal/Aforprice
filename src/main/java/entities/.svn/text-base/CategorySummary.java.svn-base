package entities;

import uploader.util.CategoryType;

public class CategorySummary {
	public final String retailerId;
	public final int categoryId;
	public final boolean active;
	public final CategoryType catType;
	
	public CategorySummary(String retailerId, int categoryId, CategoryType type, boolean active){
		this.retailerId = retailerId;
		this.categoryId = categoryId;
		this.catType = type;
		this.active = active;
	}
	
	@Override
	public String toString(){
		return retailerId + ":" + categoryId + ":" + catType + ":" + active;
	}
}
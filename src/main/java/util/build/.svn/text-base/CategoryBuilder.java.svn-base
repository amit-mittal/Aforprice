package util.build;

import java.util.Date;

import uploader.util.CategoryType;
import entities.Category;

public class CategoryBuilder extends ObjectBuilder<Category> {

	public int categoryId;
	public String name;
	public String url;
	public String retailerId;
	public int parentCategoryId;
	public String parentName = "";
	public int genericCategoryId;
	public String genericName;
	//by default, category is active
	public Boolean isActive = true;
	public CategoryType catType;
	public Date timeModified;
	public String retailerCategoryId;
	
	public CategoryBuilder(){
		
	}
	public CategoryBuilder(String retailerId, int parentCategoryId, String parentName, String name,String url) {
		this.retailerId = retailerId;
		this.parentCategoryId = parentCategoryId;
		this.parentName = parentName;
		this.name = name;
		this.url = url;
	}

	public CategoryBuilder categoryId(int categoryId){
		this.categoryId = categoryId;
		return this;
	}
	public CategoryBuilder catType(CategoryType catType){
		this.catType = catType;
		return this;
	}
	@Override
	public Category build() {
		return new Category(this);
	}

}

package util.build;

import java.util.Date;

import entities.ProductAttributeHistoryBase;

public class ProductAttributeHistoryBaseBuilder extends ObjectBuilder<ProductAttributeHistoryBase> {

	public int productId;
	public Date time;
	public Date timeModified;
	
	public ProductAttributeHistoryBaseBuilder(){
	}
	
	public ProductAttributeHistoryBaseBuilder(ProductAttributeHistoryBaseBuilder base){
		this.productId = base.productId;
		this.time = base.time;
		this.timeModified = base.timeModified;
	}
	
	@Override
	public ProductAttributeHistoryBase build(){
		return new ProductAttributeHistoryBase(this);
	}
	
	public ProductAttributeHistoryBaseBuilder productId(int productId){
		this.productId=productId;
		return this;
	}
	public ProductAttributeHistoryBaseBuilder time(Date time){
		this.time=time;
		return this;
	}
	public ProductAttributeHistoryBaseBuilder timeModified(Date timeModified){
		this.timeModified=timeModified;
		return this;
	}

}

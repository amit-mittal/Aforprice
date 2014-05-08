package entities;

import java.util.HashMap;
import java.util.Map;

public class Product extends ProductSummary {

	private Map<String, Object> attributes = new HashMap<>();
	public Product(String retailerId, String name, 
					double price, String url,
					String imageUrl, String desc, String model) {
		super(retailerId, name, price, url, imageUrl, desc, model);
	}

	public void setProductAttributes(Map<String, Object> attributes){
		this.attributes = attributes;
	}
	
	public void addProductAttributes(Map<String, Object> attributes){
		if(this.attributes != null)
			this.attributes.putAll(attributes);
		else
			this.attributes = attributes;
	}
	
	public Map<String, Object> getAttributes(){
		return attributes;
	}
}
package util.build;

import java.util.Date;

import util.YesNo;

import entities.ProductsDownloadRetry;

public class ProductsDownloadRetryBuilder extends ObjectBuilder<ProductsDownloadRetry> {
	public int id;
	public String retailerId;
	public int categoryId;
	public String categoryName;
	public String startUrl;
	public Date createTime;
	public String retryReason;
	public YesNo processd;
	
	@Override
	public ProductsDownloadRetry build() {
		return new ProductsDownloadRetry(this);
	}

}

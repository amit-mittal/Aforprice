package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import parsers.util.PriceTypes;
import parsers.util.ProductUID;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;

public class ProductUtils {
	
	private static final Logger LOGGER = Logger.getLogger(ProductUtils.class);		

	/**
	 * This function is used to match a product with other products with the same name. 
	 * TODO: If it is not able to determine the match, then it should get the details from the retailer 
	 * website for the products and try to match them based on the contents of the page.
	 * @param product
	 * @param products
	 * @return
	 */
	public static ProductSummary findMatch(ProductSummary product, HashMap<ProductSummary, ProductSummary> products){
		if(product == null || products == null || products.size() == 0)
			return null;		
		return products.get(product);
	}
	
	public static boolean areEqual(ProductSummary prod1, ProductSummary prod2){
		if(prod1 == null || prod2 == null)
			return false;
		if(!prod1.getRetailerId().equalsIgnoreCase(prod2.getRetailerId())){
			return false;
		}
		String uid1 = ProductUID.get(prod1.getRetailerId(), prod1.getUrl());
		if(uid1.equals(ProductUID.UNKNOWN))
			return false;
		String uid2 = ProductUID.get(prod2.getRetailerId(), prod2.getUrl());
		if(uid2.equals(ProductUID.UNKNOWN))
			return false;
		return uid1.equals(uid2);
	}
	
	public static Set<ProductSummary> uniqueProducts(List<ProductSummary> products){
		HashSet<ProductSummary> uProds = new HashSet<ProductSummary>();
		uProds.addAll(products);
		return uProds;
	}
	
	/**
	 * Utility function to check if all the relevant fields of a product are getting downloaded.
	 * @param prod
	 * @return
	 */
	public static boolean isProductValid(ProductSummary prod){
		String uid = ProductUID.get(prod.getRetailerId(), prod.getUrl());
		boolean isValid = (prod != null &&				
				!PriceTypes.isInvalidType(prod.getPrice())
				&& uid != null && !uid.equals(ProductUID.UNKNOWN) && uid.trim().length() != 0
				&& prod.getName() != null && prod.getName().trim().length() != 0
				&& prod.getImageUrl() != null && prod.getImageUrl().trim().length() != 0);
		return isValid;
	}
	
	/**
	 * Is product valid with respect to the existing one
	 * @param prod
	 * @param base
	 * @return
	 */
	public static boolean isProductValid(ProductSummary prod, ProductSummary base){
		if(prod == null)
			return false;
		String uid = ProductUID.get(prod.getRetailerId(), prod.getUrl());
		if(uid == null || uid.trim().length() == 0 || uid.equals(ProductUID.UNKNOWN))
			return false;
		if(PriceTypes.isInvalidType(prod.getPrice()))
			return false;
		/*if(base != null){
			String existingUid = ProductUID.get(base.getRetailerId(), base.getUrl());
			if(existingUid.equals(uid)){//verify that the existing actually matches
				/if(base.getReviewRating() > 0 && prod.getReviewRating() <= 0)
					return false;
				if(base.getSalesRank() > 0 && prod.getSalesRank() <= 0)
					return false;
				if(base.getNumReviews() > 0 && prod.getNumReviews() <= 0)
					return false;
			}
		}*/
		return true;
	}
	
	/**
	 * If a product can be migrated from PRODUCT_DOWLOAD table to PRODUCT_SUMMARY table. 
	 * Product is eligible for migration if 
	 * 1. Product uid is valid 
	 * 2. Price is valid
	 * @param prod
	 * @return
	 */
	public static boolean isProductMigratable(ProductSummary prod){
		if(prod == null)
			return false;
		String uid = ProductUID.get(prod.getRetailerId(), prod.getUrl());
		if(uid == null || uid.trim().length() == 0 || uid.equals(ProductUID.UNKNOWN))
			return false;
		if(PriceTypes.isInvalidType(prod.getPrice()))
			return false;
		return true;
	}
	
	public static boolean isProductMigratable(ProductSummary prod, HashMap<ProductSummary, ProductSummary> existingProds){
		return isProductMigratable(prod);
	}
	
    /*
     * This method creates a clone of existing product summary
     */
	public static ProductSummary cloneProductSummary(ProductSummary prodToClone)
	{
		ProductSummaryBuilder builder = new ProductSummaryBuilder();
		builder.name = prodToClone.getName();
		builder.url = prodToClone.getUrl();
		builder.prodId = prodToClone.getId();
		builder.numReviews = prodToClone.getNumReviews();
		builder.salesRank = prodToClone.getSalesRank();
		builder.reviewRating = prodToClone.getReviewRating();
		builder.imageUrl = prodToClone.getImageUrl();
		builder.model = prodToClone.getModel();
		builder.retailerId = prodToClone.getRetailerId();
		builder.downloadTime = prodToClone.getDownloadTime();
		ProductSummary productSummary = builder.build();
		return productSummary;
	}
}
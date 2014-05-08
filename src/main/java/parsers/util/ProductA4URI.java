package parsers.util;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Product;
import util.Utils;
import entities.Retailer;
/*
 * A4URI stands for A4 Unique Receipt Identifier 
 * Given Product (and possibly details) we identify unique receipt identifier which is shown on receipt 
 * 
 */
public class ProductA4URI
{
	public static final String UNKNOWN = "unknownpid";
	private static final Logger LOGGER = Logger.getLogger(ProductA4URI.class);

	
	private ProductA4URI() {
	}
	
	public static String get(String retailer, Product product){
		Retailer r = Retailer.getRetailer(retailer);
		if(r == null)
		{
			LOGGER.warn("unable to find retailer for id=" + retailer);
			LOGGER.debug(Utils.getStackTrace());
		}
		if(Retailer.WALMART.getId().equals(retailer)){
			return getWalmartA4URI(product);
		}
		return UNKNOWN;
	}
	
	public static String getWalmartA4URI(Product product){
		String uid = null;
		try
		{
			String imageURL = product.getImageUrl();
			int indexOflastSlash = imageURL.lastIndexOf("/");
			String imageName = imageURL.substring(indexOflastSlash +1, imageURL.length() ); 
			uid = imageName.substring(0,imageName.indexOf("_"));
		}
		catch( Exception e){
			LOGGER.info("Failure trying to lookup receipt identifier for product " + product);
			e.printStackTrace();
		}
		return uid == null?UNKNOWN: uid;
	}	
}

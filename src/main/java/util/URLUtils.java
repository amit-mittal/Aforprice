package util;

import entities.Retailer;

public class URLUtils {

	public static String fixURL(String url, String retailer){
		if(Retailer.WALMART.getId().equals(retailer)){
			return url.replaceAll(" ", "%20");
		}
		return url;
	}

}

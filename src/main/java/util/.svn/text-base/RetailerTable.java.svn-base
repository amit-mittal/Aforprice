/**
 * 
 */
package util;

import org.apache.log4j.Logger;
import org.omg.CosNaming.IstringHelper;

import global.exceptions.Bhagte2BandBajGaya;
import thrift.genereated.retailer.retailerConstants;

/**
 * @author Ashish
 * Give table names for retailer
 */
public class RetailerTable {
	private final static Logger logger = Logger.getLogger(RetailerTable.class);
	private static final String PRODUCT_PRICES_HISTORY = "PRODUCT_PRICES_HISTORY";
	private static final String PRODUCT_REVIEWS_HISTORY = "PRODUCT_REVIEWS_HISTORY";
	private static final String PRODUCT_SELL_RANKS_HISTORY = "PRODUCT_SELL_RANKS_HISTORY";

	public static String getPricesHistoryTable(String retailer){
		return PRODUCT_PRICES_HISTORY + tableSuffix(retailer);
	}
	
	public static String getReviewsHistoryTable(String retailer){
		return PRODUCT_REVIEWS_HISTORY + tableSuffix(retailer);
	}
	
	public static String getSellRanksHistoryTable(String retailer){
		return PRODUCT_SELL_RANKS_HISTORY + tableSuffix(retailer);
	}
	
	/*
	 * sanity check that retailer exists
	 */
	private static void checkRetailer(String retailer){
		if(!retailerConstants.RETAILERS.containsKey(retailer))
			throw new Bhagte2BandBajGaya("Invalid retailer "+retailer+", not found in retailerConstants.RETAILERS");
	}

	/*
	 * turn it off on windows and production
	 */
	public static boolean isTableSplitEnabled(){
		return true;
	}
	
	/*
	 * return retailer specific table number if enabled else empty
	 */
	private static String tableSuffix(String retailer){
		if(isTableSplitEnabled()){
			checkRetailer(retailer);
			return "_"+retailerConstants.RETAILERS.get(retailer);
		}else{
			return "";
		}		
	}	
	
}

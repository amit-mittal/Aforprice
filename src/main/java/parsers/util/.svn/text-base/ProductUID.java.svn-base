package parsers.util;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import util.URL;
import util.Utils;
import entities.Retailer;

/**
 * Class responsible for getting the unique id for a product of a retailer. Each retailer should be identifying 
 * every product based on the url used to access that product. We make intelligent guesses to find out what that
 * unique id would be.
 * @author anurag
 */
public class ProductUID {

	public static final String UNKNOWN = "unknownpid";
	
	private static final Logger LOGGER = Logger.getLogger(ProductUID.class);
	
	private static final Pattern gapUrlPatt = Pattern.compile("http://www.gap.com/products/.*-P(\\d+).jsp.*");
	private static final Pattern bananaRepPatt = Pattern.compile("http://www.bananarepublic.com/products/.*-P(\\d+).jsp.*");
	private static final Pattern oldNavyUrlPatt = Pattern.compile("http://www.oldnavy.com/products/.*-P(\\d+).jsp.*");
		
	private ProductUID() {
	}
	
	public static String get(String retailer, String url){
		Retailer r = Retailer.getRetailer(retailer);
		if(r != null){
			if(!url.contains(r.getDomain())){
				LOGGER.warn("url does not have the correct domain for retailer=" +retailer + ", url=" + url);
				LOGGER.debug(Utils.getStackTrace());
			}
		}
		else{
			LOGGER.warn("unable to find retailer for id=" + retailer);
			LOGGER.debug(Utils.getStackTrace());
		}
		if(Retailer.AWSAMAZON.getId().equals(retailer)){
			return getAmazonUID(url);
		}
		if(Retailer.AMAZONBESTSELLER.getId().equals(retailer)){
			return getAmazonBSUID(url);
		}
		if(Retailer.BABYSRUS.getId().equals(retailer)){
			return getToysrusUID(url);
		}
		if(Retailer.BANANAREPUBLIC.getId().equals(retailer)){
			return getBananaRepublicUid(url);
		}
		if(Retailer.BESTBUY.getId().equals(retailer)){
			return getBestBuyUid(url);
		}
		if(Retailer.BJS.getId().equals(retailer)){
			return getBjsUID(url);
		}
		if(Retailer.COSTCO.getId().equals(retailer)){
			return getCostcoUID(url);
		}
		if(Retailer.CVS.getId().equals(retailer)){
			return getCVSUid(url);
		}
		if(Retailer.GAP.getId().equals(retailer)){
			return getGapUid(url);
		}
		if(Retailer.HOMEDEPOT.getId().equals(retailer)){
			return getHomedepotUID(url);
		}
		if(Retailer.JCPENNY.getId().equals(retailer)){
			return getJCPUID(url);
		}
		if(Retailer.KOHLS.getId().equals(retailer)){
			return getKohlsUID(url);
		}
		if(Retailer.LOWES.getId().equals(retailer)){
			return getLowesUID(url);
		}
		if(Retailer.MACYS.getId().equals(retailer)){
			return getMacysUID(url);
		}
		if(Retailer.OLDNAVY.getId().equals(retailer)){
			return getOldNavyUid(url);
		}
		if(Retailer.RITEAID.getId().equals(retailer)){
			return getRiteaidUID(url);
		}
		if(Retailer.SAMSCLUB.getId().equals(retailer)){
			return getSamsclubUID(url);
		}
		if(Retailer.STAPLES.getId().equals(retailer)){
			return getStaplesUID(url);
		}
		if(Retailer.TARGET_MOBILE.getId().equals(retailer)){
			return getTargetUID(url);
		}
		if(Retailer.TOYSRUS.getId().equals(retailer)){
			return getToysrusUID(url);
		}
		if(Retailer.WALGREENS.getId().equals(retailer)){
			return getWalgreensUID(url);
		}
		if(Retailer.WALMART.getId().equals(retailer)){
			return getWalmartUID(url);
		}
		if(Retailer.TESTRETAILER.getId().equals(retailer) || Retailer.TESTRETAILER2.getId().equals(retailer)){
			return getTestRetailerUID(url);
		}
		return UNKNOWN;
	}
	
	/**
	 * http://www.amazon.com/Olympic-Sleeve-Conversion-Kit-Chrome/dp/B000KOBGNG%3FSubscriptionId%3DAKIAJWXLVHIMPKIZ346Q%26tag%3Dravede-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D165953%26creativeASIN%3DB000KOBGNG
	 * uid = B000KOBGNG
	 * @param url
	 * @return
	 */
	public static String getAmazonUID(String url){
		try{
			url = URLDecoder.decode(url, "UTF-8");
			String uid = URL.getSubPathElement(url, "/dp/");
			return uid == null?UNKNOWN: uid;
		}catch(Exception e){
		}
		return UNKNOWN;
	}

	
	/**
	 * The amazon product detail urls are of the form
	 * http://www.amazon.com/Memory-Light-Wheel-Time-Hardcover/dp/0765325950/ref=zg_bs_books_1
	 * where ASIN (unique identifier) follows /dp/.
	 * This function extracts the ASIN from the product url 
	 * Some urls are of the following format
	 * http://www.amazon.com/gp/product/B001EDB1GY/ref=zg_bs_324544011_26/181-8355871-8255017
	 * @param url amazon product detail url
	 * @return amazon product ASIN
	 */
	public static String getAmazonBSUID(String url){
		String uid = URL.getSubPathElement(url, "/dp/");
		if(uid == null){
			uid = URL.getSubPathElement(url, "/gp/product/");
		}
		return uid == null?UNKNOWN: uid;
	}	

	/**
	 * http://www.bananarepublic.com/products/straight-fit-charcoal-plaid-cotton-trouser-P323302.jsp
	 * @param url
	 * @return
	 */
	public static String getBananaRepublicUid(String url){
		Matcher matcher = bananaRepPatt.matcher(url);
		if(matcher.matches()){
			return matcher.group(1);
		}
		return UNKNOWN;
	}
	
	/**
	 * http://www.bestbuy.com/site/100+Year+Anniversary+of+Fenway+Park+-+Various+-+CD/5314941.p?id=2447467&skuId=5314941&cmp=RMX&ky=2efes1Xc0ecwp0jt2NhRyhj6y4Cj1P56J
	 * uid = 2447467
	 * @param url
	 * @return
	 */
	public static String getBestBuyUid(String url){
		String uid = URL.getParam(url, "id");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * Bjs url are of the form
	 * http://www.bjs.com/lg-32-led-hdtv-720p-60hz.product.215905?dimId=2000835
	 * the portion after product. is the product id
	 * @param url
	 * @return
	 */
	public static String getBjsUID(String url){
		String uid = URL.getSubPathElement(url, ".product.");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://www.costco.com/Samsung-55%22-Class-1080p-120Hz-3D-Smart-LED-HDTV-Bundle-UN55ES6580F.product.11756412.html
	 * Old Urls
	 * http://www.costco.com/Browse/Product.aspx?Prodid=11657751&whse=BC&Ne=4000000&eCat=BC|50126&N=4045521&Mo=5&No=0&Nr=P_CatalogName:BC&cat=49266&Ns=P_Price
	 * @param url
	 * @return
	 */
	public static String getCostcoUID(String url){
		String uid = URL.getSubPathElement(url, ".product.");
		if(uid != null)
			uid = uid.replace(".html", "");
		if(uid==null)
			uid = URL.getParam(url, "Prodid");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://www.cvs.com/shop/product-detail/CVS-Diapers-Size-1-8-14-lbs?skuId=890259
	 * Returns the skuId
	 * @param url cvs url
	 * @return skuId
	 */
	public static String getCVSUid(String url){
		String uid = URL.getParam(url, "skuId");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://www.gap.com/products/cotton-cashmere-crewneck-P247857.jsp
	 * @param url
	 * @return
	 */
	public static String getGapUid(String url){
		Matcher matcher = gapUrlPatt.matcher(url);
		if(matcher.matches()){
			return matcher.group(1);
		}
		return UNKNOWN;
	}
	
	/**
	 * http://www.homedepot.com/Appliances-Refrigeration-Refrigerators-French-Door-Refrigerators/h_d1/N-5yc1vZc3oo/R-202847444/h_d2/ProductDisplay?catalogId=10053&langId=-1&storeId=10051
	 * <19May2013> - homedepot url format has been changed
	 * http://www.homedepot.com/p/LG-Electronics-30-5-cu-ft-French-Door-Refrigerator-in-Stainless-Steel-Door-In-Door-Design-LFX31945ST/203295339
	 * 
	 * @param url
	 * @return 
	 * Returns the internet id which is a path element of the format R- in the url
	 * <19May2013> - Returns the internet id which is the end path in the url
	 */
	public static String getHomedepotUID(String url){
		String uid = URL.getSubPathElement(url, "/R-");
		if(uid == null || uid.trim().equals("")){
			try{
				java.net.URL u = new java.net.URL(url);
				String path = u.getPath();
				path = Utils.stripTrailing(path, "/");
				uid = URL.getSubPathElement(path, "/", -1);
			}catch(Exception e){
			}
		}
		return uid == null?UNKNOWN: uid;		
	}
	
	/**
	 * For the below url
	 * http://www.jcpenney.com/dotcom//for-the-home/brands/royal-velvet/categories/bedding/royal-velvet-versailles-7-pc-comforter-set-accessories/prod.jump?ppId=ens6002370003&catId=cat100250063&deptId=dept20000012&dimCombo=null&dimComboVal=null
	 * the function returns ens6002370003
	 * <p>Also, for jcp, if we specify only ppId, the product is uniquely identified. So, the following url returns the same result as the previous url
	 * http://www.jcpenney.com/dotcom/prod.jump?ppId=ens6002370003
	 * @param url
	 * @return
	 */
	public static String getJCPUID(String url){
		String uid = URL.getParam(url, "ppId");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * New url
	 * <p>http://www.kohls.com/product/prd-912267/nike-attack-razor-training-tee.jsp
	 * <p>Old url
	 * <p>http://www.kohls.com/kohlsStore/costumes/teens/PRD~724279/SpongeBob+Squarepants+Costume.jsp
	 * Returns the uid which is the subpath following prd- or PRD~
	 * @param url
	 * @return
	 */
	public static String getKohlsUID(String url){
		String uid = URL.getSubPathElement(url, "/prd-");
		if(uid == null){
			uid = URL.getSubPathElement(url, "/PRD~");
		}
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://www.lowes.com/pd_391479-80668-86579_4294701412__?productId=3827415&Ns=p_product_qty_sales_dollar|1&pl=1&currentURL=%3Fpage%3D5%26Ns%3Dp_product_qty_sales_dollar%7C1&facetInfo=
	 * @param url
	 * @return
	 */
	public static String getLowesUID(String url){
		String uid = URL.getParam(url, "productId");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * 
	 * http://www1.macys.com/shop/product/bianca-bathrobes-kids-bubble-size-6-8-bathrobe?ID=757349
	 * @param url
	 * @return
	 */
	public static String getMacysUID(String url){
		String uid = URL.getParam(url, "ID");
		return uid == null?UNKNOWN: uid;
	}

	/**
	 * http://www.oldnavy.com/products/womens-color-blocked-jersey-tanks-P387076.jsp
	 * @param url
	 * @return
	 */
	public static String getOldNavyUid(String url){
		Matcher matcher = oldNavyUrlPatt.matcher(url);
		if(matcher.matches()){
			return matcher.group(1);
		}
		return UNKNOWN;
	}

	/**
	 * http://www.riteaidonlinestore.com/Huggies-Pull-Ups-Learning-Training-Disney-Pixar/dp/B002CA8I3M?field_availability=-1&field_browse=3070378011&field_product_site_launch_date_utc=-1y&id=Huggies+Pull-Ups+Learning+Training+Disney-Pixar&ie=UTF8&refinementHistory=brandtextbin%2Csubjectbin%2Ccolor_map%2Cprice%2Csize_name&searchNodeID=3070378011&searchPage=1&searchRank=salesrank&searchSize=12
	 * The uid is present after /dp/ path.
	 * @param url
	 * @return
	 */
	public static String getRiteaidUID(String url){
		String uid = URL.getSubPathElement(url, "/dp/");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://www.samsclub.com/sams/pampers-baby-dry-size-5-27-lbs-172-ct/prod2900050.ip
	 * <p>For the above url, it will return 2900050.ip
	 * http://www.samsclub.com/sams/serta-concierge-suite-pillow-top-mattress-set-twin-xl-2-pk/119250.ip?navAction=push
	 * <p>For the above url, it will return 119250.ip
	 * @param url
	 * @return
	 */
	public static String getSamsclubUID(String url){
		try{
			java.net.URL u = new java.net.URL(url);
			String path = u.getPath();
			path = Utils.stripTrailing(path, "/");
			String uid = URL.getSubPathElement(path, "/", -1);
			return uid == null?UNKNOWN: uid;		
		}catch(Exception e){
		}
		return UNKNOWN;
	}
	
	/**
	 * http://www.staples.com/Scanners-PCs-Drives-Accessories/cat_CL158948/Perfection-B11B178061-4800-6400-dpi-USB-Flatbed-Color-Image-Scanner/product_IM1J27772
	 * <p>For the above url, it will return IM1J27772
	 * Old url formats
	 * http://www.staples.com/Small-Office-Home-Office/cat_CL158904/Bush-Somerset-71-inch-L-Desk-Hansen-Cherry-Finish/product-nr_BI28421
	 * http://www.staples.com/Expanding-File-Pockets/cat_CL140619/Smead-Secure-Pockets/StaplesProductDisplay?catalogId=10051&langId=-1&storeId=10001&partNumber=SS1049696
	 * @param url
	 * @return
	 */
	public static String getStaplesUID(String url){
		String uid = URL.getSubPathElement(url, "/product_");
		if(uid == null){
			uid = URL.getSubPathElement(url, "/product-nr_");
		}
		if(uid == null)
			uid = URL.getParam(url, "partNumber");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * http://m.target.com/p/carson-space-saver-leaning-bookcase-cherry/-/A-14029258
	 * <p>For the above url, it returns A-14029258
	 * @param url
	 * @return
	 */
	public static String getTargetUID(String url){
		String uid = URL.getSubPathElement(url, "/-/");
		if(uid != null)
			uid = uid.replace("A-", "");
		if(uid == null)
			uid = URL.getParam(url, "tcin");
		return uid == null?UNKNOWN: uid;
	}
	
	/**
	 * toysrus/babysrus url are of the form
	 * http://www.toysrus.com/product/index.jsp?productId=13014875
	 * This method extracts the productId from the url.
	 * @param url toysrus or babysrus product detail.
	 * @return product id 
	 */
	public static String getToysrusUID(String url){
		String prodId = URL.getParam(url, "productId");
		return prodId == null?UNKNOWN: prodId;
	}
	
	/**
	 * http://www.walgreens.com/store/c/colgate-cavity-protection-fluoride-toothpaste/ID=prod1635-product
	 * @param url
	 * @return
	 */
	public static String getWalgreensUID(String url){
		String uid = URL.getSubPathElement(url, "/ID=");
		return uid == null?UNKNOWN: uid;		
	}
	
	/**
	 * http://www.walmart.com/ip/V7-Vantage-Webcam-300-with-Built-In-Microphone/16419432
	 * @param url
	 * @return
	 */
	public static String getWalmartUID(String url){
		try{
			java.net.URL u = new java.net.URL(url);
			String path = u.getPath();
			path = Utils.stripTrailing(path, "/");
			String uid = URL.getSubPathElement(path, "/", -1);
			return uid == null?UNKNOWN: uid;
		}catch(Exception e){
			return UNKNOWN;
		}
	}

	/**
	 * http://www.testretailer.com/abc/16419432
	 * @param url
	 * @return
	 */
	public static String getTestRetailerUID(String url){
		try{
			java.net.URL u = new java.net.URL(url);
			String path = u.getPath();
			path = Utils.stripTrailing(path, "/");
			String uid = URL.getSubPathElement(path, "/", -1);
			return uid == null?UNKNOWN: uid;
		}catch(Exception e){
			return UNKNOWN;
		}
	}

	public static void main(String[] args){
		System.out.println(get("lowes", "http://www.lowe.com/pd_445601-48373-CAN-BLUE_4294770550__?productId=4368351&Ns=p_product_qty_sales_dollar|1&pl=1&currentURL=\\%3FNs\\%3Dp_product_qty_sales_dollar\\%7C1\\%26page\\%3D106&facetInfo="));
	}
	
}
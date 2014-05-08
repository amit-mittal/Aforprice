package entities;

import global.exceptions.Bhagte2BandBajGaya;

import java.util.HashMap;
import java.util.Map;

public enum Retailer {	
	//Superset of all the supported retailers. Please note that the retailers
	//are in alphabetical order. Please add new retailers in the same order.
	
	//RETAILER("name", "url for the start point for getting the categories", nightThrottle, dayThrottle)
	AWSAMAZON("amazon", null, 500, 2000, false),
	AMAZONBESTSELLER("amazonbs", "amazon", "http://www.amazon.com/Best-Sellers/zgbs/ref=zg_bs_unv_e_0_e_1", 500, 2000, true, true, true, true),
	BABYSRUS("babysrus", "toysrus", "http://www.toysrus.com/category/index.jsp?categoryId=2273443&ab=BRU_Header:Utility3:All-Categories:Home-Page", 1000, 5000, true, true, true, false),
	BANANAREPUBLIC("bananarepublic", "http://www.bananarepublic.com/products/index.jsp", 500, 1000, true, false, false, false),
	BJS("bjs", "http://www.bjs.com", 500, 2000, true, false, true, true),
	BESTBUY("bestbuy", "http://www.bestbuy.com", 500, 2000, true),
	COSTCO("costco", "http://www.costco.com/", 1000, 1000, true, false, true, true),
	CVS("cvs", "http://www.cvs.com/shop/", 1000, 5000, true, false, true, true),
	GAP("gap", "http://www.gap.com/products/index.jsp", 500, 1000, true, false, false, false),
	HOMEDEPOT("homedepot", "http://www.homedepot.com", 500, 1500, true, true, true, true),
	JCPENNY("jcpenny", "jcpenney", "http://www.jcpenney.com/dotcom/index.jsp", 1000, 5000, true, false, true, true),
	KOHLS("kohls", "http://www.kohls.com", 1000, 5000, true, true, true, true),
	LOWES("lowes", "http://www.lowes.com", 1000, 5000, true, true, true, true),
	MACYS("macys", "http://www.macys.com", 1000, 5000, true, false, false, false),
	NORDSTROM("nordstrom", "http://shop.nordstrom.com/", 500, 1000, false),
	OLDNAVY("oldnavy", "http://www.oldnavy.com/products/index.jsp", 500, 1000, true, false, false, false),
	RITEAID("riteaid", "riteaidonlinestore", "http://www.riteaidonlinestore.com", 1000, 5000, true, true, false, true),
	SEARS("sears", "http://www.sears.com", 1000, 5000, false),
	SAMSCLUB("samsclub", "http://www.samsclub.com/sams/shop/common/samsProducts_seeAll.jsp", 1000, 5000, true, true, false, false),
	STAPLES("staples", "http://www.staples.com/office/supplies/sitemap-html", 1000, 5000, true, false, true, true),
	TARGET("target_defunct", "target", "http://www.target.com/np/see-more/-/N-5xsxf#?lnk=nav_t_spc_13_0", 500, 2000, false, false, false, false),
	TARGET_MOBILE("target", "http://m.target.com/mcategories", 500, 1000, true, true, true, true),
	TOYSRUS("toysrus", "http://www.toysrus.com/category/index.jsp?categoryId=2273442&ab=TRU_Header:Category:", 1000, 5000, true, true, true, false),
	WALGREENS("walgreens", "http://www.walgreens.com/store/catalog/shopLanding?tab=view_all", 5000, 10000, true, true, true, true),
	WALMART("walmart", "http://www.walmart.com", 500, 2000, true, true, true, true),
	GENERIC("generic", "blah", 500, 500, false),
	TESTRETAILER("testretailer", "http://www.testretailer.com/", 500, 500, false, true, true, true),
	TESTRETAILER2("testretailer2", "testretailer1", "http://www.testretailer1.com/", 500, 500, false, false, false, false);
	
	private final String id;
	private final String name;
	private final String link;
	private final int dayThrottle;
	private final int nightThrottle;
	private final boolean isActive;
	private final boolean isSortedBySellRank;
	private final boolean hasReviews;
	private final boolean hasNumReviewers;
	
	public static Map<String, String> retailerToOwnerMap = new HashMap<String, String>();
	static{
		retailerToOwnerMap.put(COSTCO.id,"Arpan");
		retailerToOwnerMap.put(BJS.id,"Ashish");
		retailerToOwnerMap.put(RITEAID.id,"Chirag");
		retailerToOwnerMap.put(STAPLES.id,"Anurag");

		retailerToOwnerMap.put(AMAZONBESTSELLER.id,"Anurag");
		retailerToOwnerMap.put(LOWES.id,"Anurag");
		retailerToOwnerMap.put(WALMART.id,"Anurag");
		retailerToOwnerMap.put(KOHLS.id,"Anurag");
		
		retailerToOwnerMap.put(BANANAREPUBLIC.id,"Arpan");
		retailerToOwnerMap.put(BESTBUY.id,"Arpan");
		retailerToOwnerMap.put(GAP.id,"Arpan");
		retailerToOwnerMap.put(HOMEDEPOT.id,"Arpan");
		retailerToOwnerMap.put(JCPENNY.id,"Arpan");
		retailerToOwnerMap.put(OLDNAVY.id,"Arpan");
		
		retailerToOwnerMap.put(BABYSRUS.id,"Ashish");
		retailerToOwnerMap.put(TARGET.id,"Ashish");
		retailerToOwnerMap.put(TOYSRUS.id,"Ashish");
		retailerToOwnerMap.put(MACYS.id,"Ashish");

		retailerToOwnerMap.put(CVS.id,"Chirag");
		retailerToOwnerMap.put(SAMSCLUB.id,"Chirag");
		retailerToOwnerMap.put(WALGREENS.id,"Chirag");		
	}
	

	
	private static Map<String, Retailer> idToRetailerMap = new HashMap<String, Retailer>(Retailer.values().length);
	static{
		for(Retailer retailer:Retailer.values()){
			if(idToRetailerMap.containsKey(retailer.getId().toLowerCase()))
				throw new Bhagte2BandBajGaya("Duplicate retailer id = " + retailer.getId());
			idToRetailerMap.put(retailer.getId().toLowerCase(), retailer);
		}
	}
	/**
	 * @param id
	 * @param link
	 * @param nightThrottle milliseconds to throttle between consecutive requests during night time
	 * @param dayThrottle milliseconds to throttle between consecutive requests during day time
	 */
	private Retailer(String id, String link, int nightThrottle, int dayThrottle, boolean active){
		this(id, id, link, nightThrottle, dayThrottle, active, false, false, false);
	}

	/**
	 * @param id
	 * @param link
	 * @param nightThrottle milliseconds to throttle between consecutive requests during night time
	 * @param dayThrottle milliseconds to throttle between consecutive requests during day time
	 */
	private Retailer(String id, String link, int nightThrottle, int dayThrottle, boolean isActive, boolean isSortedBySellRank, boolean hasReviews, boolean hasNumReviewers){
		this(id, id, link, nightThrottle, dayThrottle, isActive, isSortedBySellRank, hasReviews, hasNumReviewers);
	}

	/**
	 * @param id
	 * @param link
	 * @param nightThrottle milliseconds to throttle between consecutive requests during night time
	 * @param dayThrottle milliseconds to throttle between consecutive requests during day time
	 */
	private Retailer(String id, String name, String link, int nightThrottle, int dayThrottle, boolean isActive, boolean isSortedBySellRank, boolean hasReviews, boolean hasNumReviewers){
		this.id = id;
		this.name = name;
		this.link = link;
		this.dayThrottle = dayThrottle;
		this.nightThrottle = nightThrottle;
		this.isSortedBySellRank = isSortedBySellRank;
		this.isActive = isActive;
		this.hasReviews = hasReviews;
		this.hasNumReviewers = hasNumReviewers;
	}

	public String getId(){
		return this.id;
	}
	public String getDomain(){
		return name + ".com";
	}
	public String getLink(){
		return this.link;
	}
	public int dayThrottle(){
		return this.dayThrottle;
	}
	public int nightThrottle(){
		return this.nightThrottle;
	}
	public boolean isActive(){
		return this.isActive;
	}
	public static Retailer getRetailer(String id){
		return idToRetailerMap.get(id.toLowerCase());
	}

	public boolean isSortedBySellRank() {
		return isSortedBySellRank;
	}

	public boolean hasReviews() {
		return hasReviews;
	}

	public boolean hasNumReviewers() {
		return hasNumReviewers;
	}

	//todo:replace below ID/LINK usage with enum
	public static class ID{
		public static final String AMAZON = "amazon";
		public static final String AWS_AMAZON = "AWSamazon";
		public static final String BJS = "bjs";
		public static final String BESTBUY = "bestbuy";
		public static final String COSTCO = "costco";
		public static final String JCPENNY = "jcpenny";
		public static final String KOHLS = "kohls";
		public static final String LOWES = "lowes";
		public static final String MACYS = Retailer.MACYS.getId();
		public static final String SEARS = "sears";
		public static final String TOYSRUS = "toysrus";
		public static final String WALMART = "walmart";
		public static final String TARGET = "target";
		public static final String SAMSCLUB = "samsclub";
		public static final String STAPLES = "staples";
		public static final String HOMEDEPOT = "homedepot";
		public static final String GAP = "gap";
		public static final String NORDSTROM = "nordstrom";
		public static final String OLDNAVY = "oldnavy";
		public static final String BANANAREPUBLIC = "bananarepublic";
	}
}
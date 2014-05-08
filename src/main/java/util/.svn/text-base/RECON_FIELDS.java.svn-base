package util;

import java.util.Comparator;
import java.util.TreeSet;

import db.dao.ReconilationDAO;

public enum RECON_FIELDS {
	//General fields 
	PRODUCTSPERCATEGORY(0, ReconilationDAO.TYPE.PRODUCTSPERCATEGORY, ""),
	//ProductsRecon fields
	TOTAL(1, ReconilationDAO.TYPE.TOTAL, "Unique", true, false),
	EXPECTED(2, ReconilationDAO.TYPE.EXPECTED, "Exp"),
	RECEIVED(3, ReconilationDAO.TYPE.RECEIVED, "Rcvd"),
	CATMISMATCH(4, ReconilationDAO.TYPE.CATMISMATCH, "CatMismatch"),
	COUNTMISMATCH(5, ReconilationDAO.TYPE.COUNTMISMATCH, "Mismatch"),	
	FAILED(6, ReconilationDAO.TYPE.FAILED, "Failed", true, true),
	NOUID(7, ReconilationDAO.TYPE.NOUID, "NoUid", true, true),
	NOURL(8, ReconilationDAO.TYPE.NOURL, "NoUrl"),
	NOPRICE(9, ReconilationDAO.TYPE.NOPRICE, "NoPrice"),
	PRICEUP(10, ReconilationDAO.TYPE.PRICEUP, "PriceUp", true, true),
	PRICEDN(11, ReconilationDAO.TYPE.PRICEDN, "PriceDn", true, false),
	PRICEUP5ORMORE(12, ReconilationDAO.TYPE.PRICEUP5ORMORE, "PriceUp5", true, true),
	PRICEDN5ORMORE(13, ReconilationDAO.TYPE.PRICEDN5ORMORE, "PriceDn5", true, true),
	NORANK(14, ReconilationDAO.TYPE.NORANK, "NoRank"),
	NORANKNEW(15, ReconilationDAO.TYPE.NORANKNEW, "NoRank*"),
	NOREVIEWSNEW(16, ReconilationDAO.TYPE.NOREVIEWSNEW, "NoReviews*"),
	NONUMREVIEWERSNEW(17, ReconilationDAO.TYPE.NONUMREVIEWERSNEW, "NoReviewers*"),
	HASREVIEWS(18, ReconilationDAO.TYPE.HASREVIEWS, "Reviews", true, false),
	HASREVIEWERS(19, ReconilationDAO.TYPE.HASREVIEWERS, "Reviewers", true, false),
	NOPRICENEW(20, ReconilationDAO.TYPE.NOPRICENEW, "NoPrice*"),	
	NONAME(21, ReconilationDAO.TYPE.NONAME, "NoName"),
	NONAMENEW(22, ReconilationDAO.TYPE.NONAMENEW, "NoName*"),
	NOIMGURL(23, ReconilationDAO.TYPE.NOIMGURL, "NoImgUrl"),
	NOIMGURLNEW(24, ReconilationDAO.TYPE.NOIMGURLNEW, "NoImgUrl*"),
	NOMODELNEW(25, ReconilationDAO.TYPE.NOMODELNEW, "NoModel*"),
	STALE(26, ReconilationDAO.TYPE.STALE, "Stale"),
	NEWCOUNT(27, ReconilationDAO.TYPE.NEWCOUNT, "New"),
	//Category recon fields
	ACTIVECATSCOUNT(1, ReconilationDAO.TYPE.ACTIVECATSCOUNT, "Active"),
	CATSTALE_CHILDRENNOTSTALE(2, ReconilationDAO.TYPE.CATSTALE_CHILDRENNOTSTALE, "Parent Stale"),
	CATNOTSTALE_ALLCHILDRENSTALE(3, ReconilationDAO.TYPE.CATNOTSTALE_ALLCHILDRENSTALE, "Children Stale"),
	CATSTALE(4, ReconilationDAO.TYPE.CATSTALE, "Stale");
	
	
	private final int order;
	private final String display;
	private final ReconilationDAO.TYPE type;
	private final boolean showStats;
	private final boolean isErrField;//This field is relevant only when showStats is true
	RECON_FIELDS(int order, ReconilationDAO.TYPE type, String display, boolean showStats, boolean isErrField){
		this.order = order;
		this.display = display;
		this.type = type;
		this.showStats = showStats;
		this.isErrField = isErrField;
	}
	
	RECON_FIELDS(int order, ReconilationDAO.TYPE type, String display){
		this(order, type, display, false, false);
	}

	public static RECON_FIELDS[] categoryFields() {
		return new RECON_FIELDS[]{ACTIVECATSCOUNT, CATSTALE_CHILDRENNOTSTALE, CATNOTSTALE_ALLCHILDRENSTALE, CATSTALE};
	}
	
	public static RECON_FIELDS[] productFields() {
		return new RECON_FIELDS[]{
				TOTAL,
				EXPECTED,
				RECEIVED,
				COUNTMISMATCH,
				CATMISMATCH,
				FAILED,
				NOUID,
				NOURL,
				NOPRICE,
				NORANK,
				NORANKNEW,
				NOREVIEWSNEW,
				NONUMREVIEWERSNEW,
				HASREVIEWS,
				HASREVIEWERS,
				NOPRICENEW,	
				NONAME,
				NONAMENEW,
				NOIMGURL,
				NOIMGURLNEW,
				NOMODELNEW,
				STALE,
				NEWCOUNT,
				PRICEUP,
				PRICEDN,
				PRICEUP5ORMORE,
				PRICEDN5ORMORE
				
		};
	}
	public String getDisplayName(){
		return display;
	}

	public ReconilationDAO.TYPE getType(){
		return type;
	}

	public boolean showStats() {
		return showStats;
	}
	
	public boolean isErrField(){
		return isErrField;
	}

	public static TreeSet<RECON_FIELDS> orderedProductReconFields(){
		RECON_FIELDS[] fields = RECON_FIELDS.productFields();
		TreeSet<RECON_FIELDS> orderd = new TreeSet<RECON_FIELDS>(comparator());
		for(RECON_FIELDS field: fields){
			orderd.add(field);
		}
		return orderd;
	}
	
	public static TreeSet<RECON_FIELDS> orderedCategoryReconFields(){
		RECON_FIELDS[] fields = RECON_FIELDS.categoryFields();
		TreeSet<RECON_FIELDS> orderd = new TreeSet<RECON_FIELDS>(comparator());
		for(RECON_FIELDS field: fields){
			orderd.add(field);
		}
		return orderd;
	}
	
	public static Comparator<RECON_FIELDS> comparator(){
		return new Comparator<RECON_FIELDS>() {
			@Override
			public int compare(RECON_FIELDS o1, RECON_FIELDS o2) {
				return Integer.compare(o1.order, o2.order);
			}
			
		};
	}
}
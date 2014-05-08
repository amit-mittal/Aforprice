package products;

//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.log4j.Logger;
//
//import parsers.util.PriceTypes;
//import util.ConfigParms;
//import util.Constants;
//import util.DateTimeUtils;
//import util.EmailAlerts;
//import util.MutableInt;
//import util.ProductUtils;
//import util.UtilityLogger;
//import db.dao.DAOException;
//import db.dao.ProductsDAO;
//import db.dao.ReconcilationRecorder;
//import entities.ProductSummary;
//import entities.Retailer;
//import global.exceptions.Bhagte2BandBajGaya;

public class ProductsReconciler {
			  
//	private static final Logger logger = Logger.getLogger(ProductsReconciler.class);
//	private static final ProductsDAO prodDao = new ProductsDAO();
//	
//	private enum RECON_FIELDS {
//		NONAME, 
//		NONAMENEW, 
//		NOPRICE, 
//		NOPRICENEW,
//		NOURL,
//		NOURLNEW,
//		NOMODELNEW,
//		NODESCNEW,
//		NOIMGURL,
//		NOIMGURLNEW,
//		NEWCOUNT,
//		TOTAL,
//		FAILED
//	};
//	
//	/**
//	 * Reconcile the products downloaded on reconDate by comparing it to prev week's products download.
//	 * @param reconDate Date for carrying out reconciliation
//	 * @param daysAgainst reconcile against previous daysAgainst days of products
//	 * @return true, if reconciliation is successful, false otherwise
//	 */
//	public boolean reconcile(Date reconDate, int daysAgainst){
//		for(String retailer: Retailer.RECON_RETAILERS)
//			reconcile(reconDate, retailer, daysAgainst);
//		return true;
//	}
//	
//	/**
//	 * Reconcile the products downloaded on reconDate for given retailer by comparing it to prev week's products download.
//	 * @param reconDate Date for carrying out reconciliation
//	 * @param daysAgainst reconcile against daysAgainst days of products
//	 * @return true, if reconciliation is successful, false otherwise
//	 */
//	public boolean reconcile(Date reconDate, String retailer, int daysAgainst){
//		try{
//			logger.info("Running reconcilation for " + retailer);			
//			Map<String, Map<String, Integer>> reconResults = new HashMap<String, Map<String, Integer>>();
//			int totalProds = 0;			
//			Map<String, Set<ProductSummary>> products = prodDao.getDownloadedProductsOnDate(reconDate, retailer, -1, ConfigParms.getDownloadMode());
//			totalProds += totalProds(products);
//			Map<String, Map<String, Set<ProductSummary>>> productsPerCat = getCategorizedProducts(products);
//			Map<String, Set<ProductSummary>> prodsPrevWeek = prodDao.getDownloadedProductsPreviousNDays(reconDate, retailer, -1, daysAgainst, ConfigParms.getDownloadMode());
//			Map<String, Map<String, Set<ProductSummary>>> prodsPrevWeekPerCat = getCategorizedProducts(prodsPrevWeek);
//			for(Map.Entry<String, Map<String, Set<ProductSummary>>> entry: productsPerCat.entrySet()){
//				Map<String, Integer> reconResult = reconcile(entry.getValue(), prodsPrevWeekPerCat.get(entry.getKey()), reconDate, entry.getKey());
//				if(reconResult != null && reconResult.size() > 0){
//					reconResults.put(entry.getKey(), reconResult);
//				}				
//			}
//			EmailAlerts.reconAlert(reconDate, retailer, reconResults, totalProds);
//		}catch(Exception e){
//			UtilityLogger.logError(e);
//			throw new Bhagte2BandBajGaya(e);
//		}
//		return true;
//	}
//	
//	/**
//	 * @param start
//	 * @param step
//	 */
//	public void reconcileContinous(Date start, int step){
//		try{
//			Calendar midNight = DateTimeUtils.getPrevMidNight(start);
//			Calendar prevWeekMidNight = DateTimeUtils.getPrevNDaysMidNight(start, 7);
//			
//			final Map<String, Set<ProductSummary>> allProds = prodDao.getDownloadedProductsTimeRange(prevWeekMidNight.getTime(), midNight.getTime());
//			final Map<String, Date> dateStore = new HashMap<String, Date>(1);
//			dateStore.put("lastrun", start);
//			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
//				
//				@Override
//				public void run() {					
//					 try {
//						 Date to = new Date();						 
//						 Map<String, Set<ProductSummary>> prods = prodDao.getDownloadedProductsTimeRange(dateStore.get("lastrun"), to);
//						 dateStore.put("lastrun", to);
//					} catch (SQLException e) {
//					}
//				}
//			}, step, step, TimeUnit.HOURS);
//		}catch(Exception e){
//			
//		}
//
//	}
//	
//	private Map<String, Map<String, Set<ProductSummary>>> getCategorizedProducts(Map<String, Set<ProductSummary>> prods){
//		Map<String, Map<String, Set<ProductSummary>>> categorizedProds = new HashMap<String, Map<String, Set<ProductSummary>>>(prods.size());
//		for(Map.Entry<String, Set<ProductSummary>> entry: prods.entrySet()){
//			Set<ProductSummary> products = entry.getValue();
//			for(ProductSummary product: products){
//				String category = String.valueOf(product.getCategoryId());
//				if(!categorizedProds.containsKey(category)){
//					categorizedProds.put(category, new HashMap<String, Set<ProductSummary>>());
//				}
//				if(!categorizedProds.get(category).containsKey(product.getNameLower())){
//					categorizedProds.get(category).put(product.getNameLower(), new HashSet<ProductSummary>());
//				}
//				categorizedProds.get(category).get(product.getNameLower()).add(product);
//			}
//		}
//		return categorizedProds;
//	}
//	private int totalProds(Map<String, Set<ProductSummary>> products){
//		int total = 0;
//		for(Map.Entry<String, Set<ProductSummary>> entry: products.entrySet()){
//			total += entry.getValue().size();
//		}
//		return total;
//	}
//	
//
//	
//	private Map<String, Integer> reconcile(Map<String, Set<ProductSummary>> prods, Map<String, Set<ProductSummary>> prodsPrevWeek, Date reconDate, String categoryId){		
//		int newCount = 0;
//		int staleCount = 0;
//		int noName = 0;
//		int noNameNew = 0;
//		int noPrice = 0;
//		int noPriceNew = 0;
//		int noUrl = 0;
//		int noUrlNew = 0;
//		int noImgUrl = 0;
//		int noImgUrlNew = 0;
//		int noModelNew = 0;
//		int noDescNew = 0;
//		int prodFails = 0;
//		Map<String, Integer> recon = new TreeMap<String, Integer>();	
//		prodsPrevWeek = (prodsPrevWeek == null?new HashMap<String, Set<ProductSummary>>():prodsPrevWeek);
//		
//		for(Map.Entry<String, Set<ProductSummary>> entry: prods.entrySet()){					
//			StringBuilder conflictTxt = new StringBuilder();			
//			String prodKey = entry.getKey();
//			Set<ProductSummary> prodList = entry.getValue();			
//			for(ProductSummary prod: prodList){
//				boolean prodRecStatus = true;
//				StringBuilder prodRecTxt = new StringBuilder(conflictTxt);
//				if(prod.getName() == null || prod.getName().trim().isEmpty()){
//					prodRecStatus = false;
//					prodRecTxt.append(ReconcilationRecorder.TYPE.NONAME);
//					noName++;
//				}
//				if(PriceTypes.isInvalidType(prod.getPrice())){
//					prodRecStatus = false;
//					prodRecTxt.append(ReconcilationRecorder.TYPE.NOPRICE);
//					noPrice++;				
//				}
//				if(prod.getUrl() == null || prod.getUrl().trim().length() == 0){
//					prodRecStatus = false;
//					prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NOURL);
//					noUrl++;
//				}
//				ProductSummary prodPrev = null;
//				Set<ProductSummary> prodPrevList = prodsPrevWeek.get(prodKey);
//				if(prodPrevList != null){
//					prodPrev = ProductUtils.findMatch(prod, prodPrevList);
//				}					
//				if(prodPrev == null){	
//					newCount++;
//					if(prod.getImageUrl() == null || prod.getImageUrl().trim().isEmpty()){
//						prodRecStatus = false;
//						prodRecTxt.append(ReconcilationRecorder.TYPE.NOIMGURL);
//						noImgUrl++;
//					}					
//				}
//				else{
//					if(!PriceTypes.isInvalidType(prodPrev.getPrice()) && PriceTypes.isInvalidType(prod.getPrice())){
//						prodRecStatus = false;
//						prodRecTxt.append(ReconcilationRecorder.TYPE.NOPRICENEW);
//						noPriceNew++;					
//					}
//					String prev = prodPrev.getName()==null?"":prodPrev.getName().trim();
//					String cur = prod.getName()==null?"":prod.getName().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NONAMENEW);
//						noNameNew++;
//					}
//					prev = prodPrev.getUrl()==null?"":prodPrev.getUrl().trim();
//					cur = prod.getUrl()==null?"":prod.getUrl().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NOURLNEW);
//						noUrlNew++;
//					}
//					prev = prodPrev.getImageUrl()==null?"":prodPrev.getImageUrl().trim();
//					cur = prod.getImageUrl()==null?"":prod.getImageUrl().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NOIMGURLNEW);
//						noImgUrlNew++;
//					}
//					prev = prodPrev.getModel()==null?"":prodPrev.getModel().trim();
//					cur = prod.getModel()==null?"":prod.getModel().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NOMODELNEW);
//						noModelNew++;
//					}
//					prev = prodPrev.getDesc()==null?"":prodPrev.getDesc().trim();
//					cur = prod.getDesc()==null?"":prod.getDesc().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + ReconcilationRecorder.TYPE.NODESCNEW);
//						noDescNew++;
//					}					
//				}			
//				try{
//					if(!prodRecStatus)
//						prodFails++;
//					prodDao.updateProductRecStatus(prod.getId(), prodRecStatus, prodRecTxt.toString());
//				}
//				catch(Exception e){
//					logger.error(e.getMessage(), e);
//					logger.error("error updating prodrec status for id=" + prod.getId()+": status="+prodRecStatus +",text=" + prodRecTxt);
//				}
//			}
//			//remove all the products which were received today from the previous days product map.
//			if(prodsPrevWeek.containsKey(prodKey)){
//				prodsPrevWeek.get(prodKey).removeAll(prodList);
//				if(prodsPrevWeek.get(prodKey).size() == 0)
//					prodsPrevWeek.remove(prodKey);
//			}
//		}
//		for(Map.Entry<String, Set<ProductSummary>> entry: prodsPrevWeek.entrySet()){
//			staleCount += entry.getValue().size();			
//		}//for(Map.Entry<String, ProductSummary> entry: prods.entrySet()){	ends...
//		Timestamp reconTs = new Timestamp(reconDate.getTime());
//		Timestamp ts = new Timestamp(System.currentTimeMillis());		
//		try {
//			recon.put(ReconcilationRecorder.TYPE.NONAME.toString(), noName);
//			if(noName > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NONAME, "", noName, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NONAMENEW.toString(), noNameNew);
//			if(noNameNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NONAMENEW, "", noNameNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOPRICE.toString(), noPrice);
//			if(noPrice > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOPRICE, "", noPrice, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOPRICENEW.toString(), noPriceNew);
//			if(noPriceNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOPRICENEW, "", noPriceNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOURL.toString(), noUrl);
//			if(noUrl > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOURL, "", noUrl, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOURLNEW.toString(), noUrlNew);
//			if(noUrlNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOURLNEW, "", noUrlNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOMODELNEW.toString(),noModelNew);
//			if(noModelNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOMODELNEW, "", noModelNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NODESCNEW.toString(), noDescNew);
//			if(noDescNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NODESCNEW, "", noDescNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOIMGURL.toString(), noImgUrlNew);
//			if(noImgUrl > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOIMGURL, "", noImgUrl, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NOIMGURLNEW.toString(), noImgUrlNew);
//			if(noImgUrlNew > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NOIMGURLNEW, "", noImgUrlNew, ts);				
//			}
//			recon.put(ReconcilationRecorder.TYPE.NEWCOUNT.toString(), newCount);
//			if(newCount > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.NEWCOUNT, "", newCount, ts);
//			}
//			recon.put(ReconcilationRecorder.TYPE.STALECOUNT.toString(), staleCount);
//			if(staleCount > 0){
//				ReconcilationRecorder.getInstance().storeRec(categoryId, reconTs, ReconcilationRecorder.NAME.PRODUCT, ReconcilationRecorder.TYPE.STALECOUNT, "", staleCount, ts);									
//			}			
//			recon.put(Constants.PRODS_TOT, totalProds(prods));
//			recon.put(Constants.PRODS_RECON_FAIL, prodFails);			
//		} catch (DAOException e) {	
//			logger.error(e.getMessage(), e);			
//		}
//		return recon;		
//	}
//	
//	private Map<String, TreeMap<String, Integer>> reconcile(Map<String, Set<ProductSummary>> prods, Map<String, Set<ProductSummary>> prodsPrevWeek){
//		Map<RECON_FIELDS, Map<String, MutableInt>> counts = new HashMap<>();
//		for(RECON_FIELDS t: RECON_FIELDS.values()){
//			counts.put(t, new HashMap<String, MutableInt>());
//		}
//		Map<String, TreeMap<String, Integer>> recon = new HashMap<String, TreeMap<String, Integer>>();		
//		Set<String> retailers = new HashSet<>();
//		
//		prodsPrevWeek = (prodsPrevWeek == null?new HashMap<String, Set<ProductSummary>>():prodsPrevWeek);
//		
//		for(Map.Entry<String, Set<ProductSummary>> entry: prods.entrySet()){
//			String prodKey = entry.getKey();
//			Set<ProductSummary> prodList = entry.getValue();			
//			for(ProductSummary prod: prodList){
//				retailers.add(prod.getRetailerId());
//				boolean prodRecStatus = true;
//				StringBuilder prodRecTxt = new StringBuilder();
//				addRecon(prod, RECON_FIELDS.TOTAL, counts, null);
//				if(prod.getName() == null || prod.getName().trim().isEmpty()){
//					prodRecStatus = false;
//					addRecon(prod, RECON_FIELDS.NONAME, counts, prodRecTxt);
//				}
//				if(PriceTypes.isInvalidType(prod.getPrice())){
//					prodRecStatus = false;	
//					addRecon(prod, RECON_FIELDS.NOPRICE, counts, prodRecTxt);
//				}
//				if(prod.getUrl() == null || prod.getUrl().trim().length() == 0){
//					prodRecStatus = false;
//					addRecon(prod, RECON_FIELDS.NOURL, counts, prodRecTxt);					
//				}
//				ProductSummary prodPrev = null;
//				Set<ProductSummary> prodPrevList = prodsPrevWeek.get(prodKey);
//				if(prodPrevList != null){
//					prodPrev = ProductUtils.findMatch(prod, prodPrevList);
//				}					
//				if(prodPrev == null){	
//					addRecon(prod, RECON_FIELDS.NEWCOUNT, counts, prodRecTxt);
//					if(prod.getImageUrl() == null || prod.getImageUrl().trim().isEmpty()){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NOIMGURL, counts, prodRecTxt);
//					}					
//				}
//				else{
//					if(!PriceTypes.isInvalidType(prodPrev.getPrice()) && PriceTypes.isInvalidType(prod.getPrice())){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NOPRICENEW, counts, prodRecTxt);				
//					}
//					String prev = prodPrev.getName()==null?"":prodPrev.getName().trim();
//					String cur = prod.getName()==null?"":prod.getName().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NONAMENEW, counts, prodRecTxt);
//					}
//					prev = prodPrev.getUrl()==null?"":prodPrev.getUrl().trim();
//					cur = prod.getUrl()==null?"":prod.getUrl().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NOURLNEW, counts, prodRecTxt);
//					}
//					prev = prodPrev.getImageUrl()==null?"":prodPrev.getImageUrl().trim();
//					cur = prod.getImageUrl()==null?"":prod.getImageUrl().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NOIMGURLNEW, counts, prodRecTxt);
//					}
//					prev = prodPrev.getModel()==null?"":prodPrev.getModel().trim();
//					cur = prod.getModel()==null?"":prod.getModel().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NOMODELNEW, counts, prodRecTxt);
//					}
//					prev = prodPrev.getDesc()==null?"":prodPrev.getDesc().trim();
//					cur = prod.getDesc()==null?"":prod.getDesc().trim();
//					if(cur.length() == 0 && prev.length() != 0){
//						prodRecStatus = false;
//						addRecon(prod, RECON_FIELDS.NODESCNEW, counts, prodRecTxt);
//					}					
//				}			
//				try{
//					if(!prodRecStatus)
//						addRecon(prod, RECON_FIELDS.FAILED, counts, null);
//					prodDao.updateProductRecStatus(prod.getId(), prodRecStatus, prodRecTxt.toString());
//				}
//				catch(Exception e){
//					logger.error(e.getMessage(), e);
//					logger.error("error updating prodrec status for id=" + prod.getId()+": status="+prodRecStatus +",text=" + prodRecTxt);
//				}
//			}
//			//remove all the products which were received today from the previous days product map.
//			if(prodsPrevWeek.containsKey(prodKey)){
//				prodsPrevWeek.get(prodKey).removeAll(prodList);
//				if(prodsPrevWeek.get(prodKey).size() == 0)
//					prodsPrevWeek.remove(prodKey);
//			}
//		}		
//		for(String retailer: retailers){
//			recon.put(retailer, new TreeMap<String, Integer>());
//			for(RECON_FIELDS t: RECON_FIELDS.values()){
//				recon.get(retailer).put(t.toString(), 
//						counts.get(t).get(t.toString()) == null?0:counts.get(t).get(t.toString()).getValue());	
//			}			
//		}			
//		return recon;		
//	}
//	
//	private void addRecon(
//				ProductSummary prod, 
//				RECON_FIELDS t, 
//				Map<RECON_FIELDS, Map<String, MutableInt>> counts, 
//				StringBuilder prodRecTxt){
//		if(!t.equals(RECON_FIELDS.TOTAL)&&!t.equals(RECON_FIELDS.FAILED))			
//			prodRecTxt.append(prodRecTxt.length() !=0?"|":"" + t);
//		add(prod.getRetailerId(), counts.get(t), 1);
//	}
//	
//	private void add(String retailer, Map<String, MutableInt> container, int amount){
//		if(!container.containsKey(retailer)){
//			container.put(retailer, new MutableInt(amount));
//			return;
//		}
//		container.get(retailer).add(amount);		
//	}
//	
//			
//	public static void main(String[] args){
//		if(args.length < 2)
//			throw new IllegalArgumentException("Needs the start date and end date to run the reconcilation");
//
//		ConfigParms.getInstance().setMode(ConfigParms.RUNTIME_MODE.ONETIME);
//		ProductsReconciler recon = new ProductsReconciler();
//		String startDateIn = null, endDateIn = null, retailerOverride = null;
//		Calendar startDate = null, endDate = null;
//		int daysAgainst = 7;
//		
//		//args format key=value.. retailer=walmart mode=popular
//		for(int i=0; i<args.length; i++){
//			logger.info("processing arg: " + args[i]);
//			String[] keyVal = args[i].split("=");
//			if(keyVal.length!=2){
//				throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
//			}				
//			String key = keyVal[0];
//			String val = keyVal[1];
//			if(key.equalsIgnoreCase("start")){
//				logger.info("startdate " + val);
//				startDateIn = val;
//			}else if(key.equalsIgnoreCase("end")){
//				logger.info("enddate " + val);
//				endDateIn = val;				
//			}				
//			else if(key.equalsIgnoreCase("days")){				
//				try{
//					daysAgainst = Integer.parseInt(val);
//				}
//				catch(Exception e){
//					logger.warn("Will reconcile against previous " + daysAgainst +" days");
//				}
//			}
//			else if(key.equalsIgnoreCase("retailer")){
//				logger.info("reconcile for " + val);
//				retailerOverride = val;
//			}
//			else if(key.equalsIgnoreCase("mode")){
//				if(val.equalsIgnoreCase(ConfigParms.DOWNLOAD_MODE.POPULAR.name()))
//					ConfigParms.setDownloadMode(ConfigParms.DOWNLOAD_MODE.POPULAR);
//			}
//			else
//				logger.warn("Ignoring arg "+ keyVal[0]);
//		}
//		if(startDateIn == null || endDateIn == null)
//			throw new IllegalArgumentException("start date or end date not specified");
//		startDate = DateTimeUtils.getMidNight(DateTimeUtils.dateFromyyyyMMdd(startDateIn));		
//		endDate = DateTimeUtils.getMidNight(DateTimeUtils.dateFromyyyyMMdd(endDateIn));
//		logger.info("Reconciling products downloaded between " + startDateIn + " and " + endDateIn);
//				
//		for(Calendar date = startDate; 
//				Integer.parseInt(DateTimeUtils.currentDateYYYYMMDD(date.getTime())) <= Integer.parseInt(DateTimeUtils.currentDateYYYYMMDD(endDate.getTime())); 
//				date = DateTimeUtils.getNextMidNight(date.getTime())){
//			if(retailerOverride != null)
//				recon.reconcile(date.getTime(), retailerOverride, daysAgainst);
//			else
//				recon.reconcile(date.getTime(), daysAgainst);
//			System.gc();
//			try {
//				Thread.sleep(30*1000);//to give time to system gc
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
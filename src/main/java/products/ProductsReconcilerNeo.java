package products;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import parsers.util.PriceTypes;
import parsers.util.ProductUID;
import util.ConfigParms;
import util.DateTimeUtils;
import util.EmailAlerts;
import util.Emailer;
import util.MutableInt;
import util.ProductUtils;
import util.RECON_FIELDS;
import db.dao.DAOException;
import db.dao.ProductsDAO;
import db.dao.ReconilationDAO;
import entities.ProductCountRecon;
import entities.ProductSummary;
import entities.Retailer;

public class ProductsReconcilerNeo {

	private static final Logger LOGGER = Logger.getLogger(ProductsReconcilerNeo.class);
	private static final ProductsDAO prodDao = new ProductsDAO();
	private static final ReconilationDAO reconDao = ReconilationDAO.getInstance();
	private static final int BATCH_SIZE = 1000;
			
	/**
	 * @param start
	 * @param end Time till which we need to catcup. If null, it runs as a server and keeps reconciling every step hours
	 * @param step
	 */
	public static void reconcile(final Date start, final Date end, final int step){
		try{
			//TODO: Start getting active products after migration is over.
			final HashMap<ProductSummary, ProductSummary> allProds = prodDao.getProducts();
			final Map<String, Date> dateStore = new HashMap<String, Date>(3);
			final TreeMap<String, TreeMap<RECON_FIELDS, MutableInt>> resultsTotal = new TreeMap<>();//Sum total of all runs
			final Lock lock = new ReentrantLock(); 
			
			LOGGER.info("Running catchup..");
			//1. Running catchup
			//For the catchup, the alert gets sent out for every 24 hours of catchup starting from  
			dateStore.put("lastalert", start);
			boolean first = true;
			Date to = runRecon(start, end, allProds, resultsTotal, step, lock);
			dateStore.put("lastrun", to);
			boolean isCaughtUp = false;
			while(true){
				Date catchUpDate = (end == null?new Date(): end);
				isCaughtUp = TimeUnit.HOURS.convert(to.getTime() - catchUpDate.getTime(), TimeUnit.MILLISECONDS) >= 0;
				if(isCaughtUp)
					break;
				LOGGER.info("Reconciliaton not caught up yet..");
				to = runRecon(to, end, allProds, resultsTotal, step, lock);
				dateStore.put("lastrun", to);//last time when the reconciler finished
				//Send alert if we have reconciled beyond the alert time and the last alert was sent for recons older than 24 hours.
				//For the first time, we dont want to wait for 24 hours and want to send the alert as soon as we are past the alert send time period
				if(((DateTimeUtils.diff(to, DateTimeUtils.getReconAlertTime(to), TimeUnit.SECONDS) >= -1) && ( first || DateTimeUtils.diff(to, dateStore.get("lastalert"), TimeUnit.HOURS) >= 24))){
					doStuffSendEmailAndReset(lock, dateStore, allProds, resultsTotal);
					first = false;
				}
			}
			LOGGER.info("Reconciliaton is fully caught upto " + to);
			//If end time is specified send alert now for any product which has not been notified for.
			if(end != null){
				if( resultsTotal.size() > 0)
					doStuffSendEmailAndReset(lock, dateStore, allProds, resultsTotal);
				return;
			}

			//2. Setting up alerter to run every 24 hours @ recon alert time
			long alertDelay = DateTimeUtils.getReconAlertTime().getTime() - new Date().getTime();
			long oneDayInMs = TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS); 
			if(alertDelay < 0){
				//We are running after the recon alert time for this day, so we want to run @ alert send time next day 
				alertDelay =  oneDayInMs  + alertDelay;
			}
			LOGGER.info("Next reconclier alert will be sent at " + DateTimeUtils.add(new Date(), alertDelay, TimeUnit.MILLISECONDS, 0));
			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
						doStuffSendEmailAndReset(lock, dateStore, allProds, resultsTotal);
				}
			}, alertDelay, oneDayInMs, TimeUnit.MILLISECONDS); //run every 24 hours

			//3. Setting up real time reconciler
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {				
				@Override
				public void run() {					
					 try {
						 LOGGER.info("Begin Reconciliation");
						 Date to = runRecon(dateStore.get("lastrun"), end, allProds, resultsTotal, step, lock);
						 dateStore.put("lastrun", to);
					} catch (SQLException e) {
						LOGGER.error(e.getMessage(), e);
					}
					 
				}
			}, 0, step, TimeUnit.HOURS);			
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private static void doStuffSendEmailAndReset(
			Lock lock, Map<String, Date> dateStore, 
			HashMap<ProductSummary, ProductSummary> allProducts, 
			TreeMap<String, TreeMap<RECON_FIELDS, MutableInt>> resultsTotal){
		try{
			if(resultsTotal == null || resultsTotal.size() == 0){
				LOGGER.info("doStuffSendEmailAndReset - Nothing to process.");
			}
			lock.lock();
			Date from = dateStore.get("lastalert");
			Date to = dateStore.get("lastrun");
			Map<String, MutableInt> stale = getStaleProducts(from, allProducts);
			
			for(Map.Entry<String, MutableInt> entry: stale.entrySet()){
				String retailer = entry.getKey();
				if(!resultsTotal.containsKey(retailer)){
					 //Initialize counts for each field for this missing retailer. Everything will be reported as 0 except for stale count
					resultsTotal.put(retailer, new TreeMap<RECON_FIELDS, MutableInt>());
					for(RECON_FIELDS t: RECON_FIELDS.productFields()){
						resultsTotal.get(retailer).put(t, new MutableInt(0));
					}
				}
				resultsTotal.get(retailer).get(RECON_FIELDS.STALE).add(entry.getValue().getValue());
			}
			for(String retailer: resultsTotal.keySet()){
				ProductCountRecon recs = null;
				try {
					recs = new ProductCountRecon(reconDao.getProductCountMismatches(retailer, from, to));
				} catch (DAOException e) {
					LOGGER.warn("Error getting product count mismatch", e);
					e.printStackTrace();
				}
				if(recs != null){
					resultsTotal.get(retailer).get(RECON_FIELDS.EXPECTED).add(recs.getExpected());
					resultsTotal.get(retailer).get(RECON_FIELDS.RECEIVED).add(recs.getActual());
					resultsTotal.get(retailer).get(RECON_FIELDS.CATMISMATCH).add(recs.getCatMismatch());
					resultsTotal.get(retailer).get(RECON_FIELDS.COUNTMISMATCH).add(recs.getCountMismatch());
				}
			}
			dateStore.put("lastalert", to);
			EmailAlerts.productsReconAlert(from, to, resultsTotal);
			//Store these reconciliation data
			Timestamp ts = new Timestamp(to == null?System.currentTimeMillis(): to.getTime());
			for(Map.Entry<String, TreeMap<RECON_FIELDS, MutableInt>> entry: resultsTotal.entrySet()){
				for(Map.Entry<RECON_FIELDS, MutableInt> subEntry: entry.getValue().entrySet()){
					try{
						ReconilationDAO.getInstance().storeRec(entry.getKey(), ts, ReconilationDAO.NAME.PRODUCT, subEntry.getKey().getType(), "", subEntry.getValue().getValue(), ts);
					} catch (DAOException e) {
						LOGGER.error("Error saving reconciliation data", e);
					}
				}
			}
			resultsTotal.clear();
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Gets the stale products which are determined by whether the last download time of a product is  
	 * after startTime
	 * @param startTime
	 * @param products
	 * @return
	 */
	private static Map<String, MutableInt> getStaleProducts(Date startTime, HashMap<ProductSummary, ProductSummary> products){
		Map<String, MutableInt> stale = new HashMap<>();
		for(ProductSummary prod: products.keySet()){
			Date lastDownTime = prod.getDownloadTime();
			//If lastdownloadtime is unknown, consider stale
			if(lastDownTime == null || lastDownTime.before(startTime)){
				if(lastDownTime == null)
					LOGGER.warn("lastDownloadTime is null for " + prod);
				String retailer = prod.getRetailerId();
				if(!stale.containsKey(retailer)){
					stale.put(retailer, new MutableInt(0));
				}
				stale.get(retailer).add(1);
			}
		}
		return stale;
	}
	
	private static Date runRecon(
			final Date lastRun, 
			final Date endTime,
			final HashMap<ProductSummary, ProductSummary> allProds, 
			final Map<String, TreeMap<RECON_FIELDS, MutableInt>> resultsTotal, 
			final int step,
			final Lock lock) throws SQLException{
		try{
			lock.lock();
			Date to = new Date();
			if(endTime != null && to.after(endTime))
				to = endTime;
			Date lastRunPlusStep = DateTimeUtils.add(lastRun, step*2, TimeUnit.HOURS, 0);
			if(lastRunPlusStep.before(to)){
				to = lastRunPlusStep;
			}
			LOGGER.info("Reconciling products between " + lastRun + " and " + to);
			HashMap<ProductSummary, ProductSummary> prods = prodDao.getDownloadedProductsTimeRange(lastRun, to, false);
			long start = System.currentTimeMillis();
			Map<String, TreeMap<RECON_FIELDS, MutableInt>> result = reconcile(prods, allProds, prodDao);
			LOGGER.info("Took " + (System.currentTimeMillis() - start)/1000 + " seconds to reconcile");
			merge(resultsTotal, result);
			mergeLatestProducts(allProds, prods);
			return to;
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Merges result1 and result2 into result1
	 * @param result1
	 * @param result2
	 */
	private static void merge(Map<String, TreeMap<RECON_FIELDS, MutableInt>> result1, Map<String, TreeMap<RECON_FIELDS, MutableInt>> result2){
		for(Map.Entry<String, TreeMap<RECON_FIELDS, MutableInt>> entry:result2.entrySet()){
			if(!result1.containsKey(entry.getKey())){
				result1.put(entry.getKey(), entry.getValue());
				continue;
			}
			for(Map.Entry<RECON_FIELDS, MutableInt> subEntry: entry.getValue().entrySet()){
				if(result1.get(entry.getKey()).containsKey(subEntry.getKey())){
					result1.get(entry.getKey()).get(subEntry.getKey()).add(subEntry.getValue().getValue());
				}
				else{
					result1.get(entry.getKey()).put(subEntry.getKey(), subEntry.getValue());
				}
				
			}
		}
	}
	
	private static void mergeLatestProducts(HashMap<ProductSummary, ProductSummary> prods, HashMap<ProductSummary, ProductSummary> newProds){
		for(ProductSummary newProd: newProds.keySet()){
			//If the existing list of products does not contain the newProd, then add it.
			if(!prods.containsKey(newProd)){
				prods.put(newProd, newProd);
				continue;
			}
			ProductSummary existing = ProductUtils.findMatch(newProd, prods);
			//Add newProd only if it is valid.
			if(ProductUtils.isProductValid(newProd, existing)){
				prods.remove(existing);
				prods.put(newProd, newProd);
			}
		}
	}
	
	private static Map<String, TreeMap<RECON_FIELDS, MutableInt>> reconcile(
				HashMap<ProductSummary, ProductSummary> prods, 
				HashMap<ProductSummary, ProductSummary> allProds,
				ProductsDAO prodDao){
		Map<RECON_FIELDS, Map<String, MutableInt>> counts = new HashMap<>(); //Field, Map<RetailerId, Count for Field>
		 //Initialize counts for each field. The map for each field will be updated with the count of that field for each retailer
		for(RECON_FIELDS t: RECON_FIELDS.productFields()){
			counts.put(t, new HashMap<String, MutableInt>());
		}
		Map<String, TreeMap<RECON_FIELDS, MutableInt>> result = new TreeMap<String, TreeMap<RECON_FIELDS, MutableInt>>();		
		Set<String> retailers = new HashSet<>();
		
		allProds = (allProds == null?new HashMap<ProductSummary, ProductSummary>():allProds);
		long start = System.currentTimeMillis();
		int counter = 0;		
		List<Long> ids = new ArrayList<>(BATCH_SIZE);
		List<Boolean> statuses = new ArrayList<>(BATCH_SIZE);
		List<String> texts = new ArrayList<>(BATCH_SIZE);
		for(ProductSummary prod: prods.keySet()){
			++counter;
			Retailer retailer = Retailer.getRetailer(prod.getRetailerId());
			retailers.add(prod.getRetailerId());
			StringBuilder prodRecTxt = new StringBuilder();
			ProductSummary prodPrev = ProductUtils.findMatch(prod, allProds);
			boolean prodRecStatus = ProductUtils.isProductValid(prod, prodPrev);

			addRecon(prod, RECON_FIELDS.TOTAL, counts, null);
			//For a product, not having name, url, price or uid
			if(prod.getName() == null || prod.getName().trim().isEmpty()){
				addRecon(prod, RECON_FIELDS.NONAME, counts, prodRecTxt);
			}
			if(PriceTypes.isInvalidType(prod.getPrice())){	
				addRecon(prod, RECON_FIELDS.NOPRICE, counts, prodRecTxt);
			}
			if(prod.getUrl() == null || prod.getUrl().trim().length() == 0){
				addRecon(prod, RECON_FIELDS.NOURL, counts, prodRecTxt);					
			}
			if(ProductUID.get(prod.getRetailerId(), prod.getUrl()).equals(ProductUID.UNKNOWN)){
				addRecon(prod, RECON_FIELDS.NOUID, counts, prodRecTxt);
			}
			if(prod.getImageUrl() == null || prod.getImageUrl().trim().length() == 0){
				addRecon(prod, RECON_FIELDS.NOIMGURL, counts, prodRecTxt);					
			}
			if(retailer.hasReviews()){
				if(prod.getReviewRating() > 0){
					addRecon(prod, RECON_FIELDS.HASREVIEWS, counts, prodRecTxt);
				}
			}
			else{
				set(prod.getRetailerId(), counts.get(RECON_FIELDS.HASREVIEWS), -1);
			}
			if(retailer.hasNumReviewers()){
				if(prod.getNumReviews() > 0){
					addRecon(prod, RECON_FIELDS.HASREVIEWERS, counts, prodRecTxt);
				}
			}
			else{
				set(prod.getRetailerId(), counts.get(RECON_FIELDS.HASREVIEWERS), -1);
			}
			if(retailer.isSortedBySellRank()){
				if(prod.getSalesRank() <= 0){
					addRecon(prod, RECON_FIELDS.NORANK, counts, prodRecTxt);
				}
			}
			else{
				set(prod.getRetailerId(), counts.get(RECON_FIELDS.NORANK), -1);
			}

			if(prodPrev == null){
				if(prodRecStatus)
					addRecon(prod, RECON_FIELDS.NEWCOUNT, counts, prodRecTxt);					
			}
			else{
				if(!PriceTypes.isInvalidType(prodPrev.getPrice()) && PriceTypes.isInvalidType(prod.getPrice())){
					addRecon(prod, RECON_FIELDS.NOPRICENEW, counts, prodRecTxt);				
				}
				if(!PriceTypes.isInvalidType(prodPrev.getPrice())&&!PriceTypes.isInvalidType(prod.getPrice())){
					if(prod.getPrice() > prodPrev.getPrice()){
						//price went up
						addRecon(prod, RECON_FIELDS.PRICEUP, counts, prodRecTxt);
					}
					if(prod.getPrice() < prodPrev.getPrice()){
						//price went up
						addRecon(prod, RECON_FIELDS.PRICEDN, counts, prodRecTxt);
					}
					if(prod.getPrice() - prodPrev.getPrice() >= 5){
						//price went down by at least a $5
						addRecon(prod, RECON_FIELDS.PRICEUP5ORMORE, counts, prodRecTxt);
					}
					
					if(prod.getPrice() - prodPrev.getPrice() <= -5){
						//price went down by at least a $5
						addRecon(prod, RECON_FIELDS.PRICEDN5ORMORE, counts, prodRecTxt);
					}
				}
				String prev = prodPrev.getName()==null?"":prodPrev.getName().trim();
				String cur = prod.getName()==null?"":prod.getName().trim();
				if(cur.length() == 0 && prev.length() != 0){
					addRecon(prod, RECON_FIELDS.NONAMENEW, counts, prodRecTxt);
				}
				prev = prodPrev.getModel()==null?"":prodPrev.getModel().trim();
				cur = prod.getModel()==null?"":prod.getModel().trim();
				if(cur.length() == 0 && prev.length() != 0){
					addRecon(prod, RECON_FIELDS.NOMODELNEW, counts, prodRecTxt);
				}
				prev = prodPrev.getImageUrl()==null?"":prodPrev.getImageUrl().trim();
				cur = prod.getImageUrl()==null?"":prod.getImageUrl().trim();
				if(cur.length() == 0 && prev.length() != 0){
					addRecon(prod, RECON_FIELDS.NOIMGURLNEW, counts, prodRecTxt);
				}
				if(retailer.hasReviews()){
					if(prodPrev.getReviewRating() > 0 && prod.getReviewRating() <= 0){
						addRecon(prod, RECON_FIELDS.NOREVIEWSNEW, counts, prodRecTxt);
					}
				}
				else{
					set(prod.getRetailerId(), counts.get(RECON_FIELDS.NOREVIEWSNEW), -1);
				}
				if(retailer.hasNumReviewers()){
					if(prodPrev.getNumReviews() > 0 && prod.getNumReviews() <= 0){
						addRecon(prod, RECON_FIELDS.NONUMREVIEWERSNEW, counts, prodRecTxt);
					}
				}
				else{
					set(prod.getRetailerId(), counts.get(RECON_FIELDS.NONUMREVIEWERSNEW), -1);
				}
				if(retailer.isSortedBySellRank()){
					if(prodPrev.getSalesRank() > 0 && prod.getSalesRank() <= 0){
						addRecon(prod, RECON_FIELDS.NORANKNEW, counts, prodRecTxt);
					}
				}
				else{
					set(prod.getRetailerId(), counts.get(RECON_FIELDS.NORANKNEW), -1);
				}
			}
			if(!prodRecStatus)
				addRecon(prod, RECON_FIELDS.FAILED, counts, null);
			ids.add(prod.getDownloadId());
			statuses.add(prodRecStatus);
			texts.add(prodRecTxt.toString());
			if(counter%BATCH_SIZE == 0 ){
				long t = System.currentTimeMillis();
				update(ids, statuses, texts);
				LOGGER.info("Took " + (System.currentTimeMillis() - t) + " ms to update "+ BATCH_SIZE + " products recon status");
			}
			if(counter%BATCH_SIZE == 0 ){
				LOGGER.info("Took " + (System.currentTimeMillis() - start) + " ms to reconcile " + BATCH_SIZE + " products");
				start = System.currentTimeMillis();
			}
		
		}
		
		update(ids, statuses, texts);
		for(String retailer: retailers){
			result.put(retailer, new TreeMap<RECON_FIELDS, MutableInt>(RECON_FIELDS.comparator()));
			for(RECON_FIELDS t: RECON_FIELDS.productFields()){
				result.get(retailer).put(t, 
						counts.get(t).get(retailer) == null?new MutableInt(0):counts.get(t).get(retailer));	
			}			
		}			
		return result;		
	}
	
	private static void update(List<Long> ids, List<Boolean> statuses, List<String> texts){
		boolean isErr = false;
		try {
			prodDao.updateProductRecStatus(ids, statuses, texts);
			ids.clear();
			statuses.clear();
			texts.clear();
		}
		catch(BatchUpdateException e){
			LOGGER.error(e.getMessage(), e);
			StringBuffer errs = new StringBuffer();
			for(int i: e.getUpdateCounts()){
				errs.append(i + " ");
			}
			LOGGER.error("error updating prodrec status for the productids at " + errs + " indices in " + ids);
			isErr = true;
		} catch (Exception e) {
			isErr = true;
			LOGGER.error(e.getMessage(), e);
		}
		
		if(isErr){
			Emailer.getInstance().sendText("Error updating rec status for following products.", "Ids:" + ids + "\nStatuses:" + statuses + "\nTexts:" + texts);
		}
		
	}
	
	private static void addRecon(
				ProductSummary prod, 
				RECON_FIELDS t, 
				Map<RECON_FIELDS, Map<String, MutableInt>> counts, 
				StringBuilder prodRecTxt){
		if(!t.equals(RECON_FIELDS.TOTAL)&&!t.equals(RECON_FIELDS.FAILED)&&!t.equals(RECON_FIELDS.HASREVIEWS)&&!t.equals(RECON_FIELDS.HASREVIEWERS))			
			prodRecTxt.append((prodRecTxt.length() !=0?"|":"") + t);
		add(prod.getRetailerId(), counts.get(t), 1);
	}
	
	private static void add(String retailer, Map<String, MutableInt> container, int amount){
		if(!container.containsKey(retailer)){
			container.put(retailer, new MutableInt(amount));
			return;
		}
		container.get(retailer).add(amount);		
	}
	
	private static void set(String retailer, Map<String, MutableInt> container, int value){
		if(!container.containsKey(retailer)){
			container.put(retailer, new MutableInt(value));
			return;
		}
		container.get(retailer).setValue(value);
	}
	
			
	public static void main(String[] args){
		String startTimeIn = null, endTimeIn = null;
		Date startTime = DateTimeUtils.getPrevMidNight(new Date()).getTime();
		Date endTime = null;
		int stepInHr = 1;
		
		//args format key=value.. start=2012-10-01T00:00:00 step=1 mode=popular
		for(int i=0; i<args.length; i++){
			LOGGER.info("processing arg: " + args[i]);
			String[] keyVal = args[i].split("=");
			if(keyVal.length!=2){
				throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
			}				
			String key = keyVal[0];
			String val = keyVal[1];
			//yyyy-MM-ddTHH:mm:ss
			if(key.equalsIgnoreCase("start")){
				startTimeIn = val;
				LOGGER.info("start=" + startTimeIn);
			}
			else if(key.equals("end")){
				endTimeIn = val;
				LOGGER.info("end=" + endTimeIn);
			}
			else if(key.equalsIgnoreCase("step")){				
				try{
					stepInHr = Integer.parseInt(val);
				}
				catch(Exception e){
					LOGGER.error("Unable to parse step in hours for reconciliation", e);
					throw new IllegalArgumentException("Unable to parse step in hours for reconciliation", e);
				}
			}
			else if(key.equalsIgnoreCase("mode")){
				if(val.equalsIgnoreCase(ConfigParms.DOWNLOAD_MODE.POPULAR.name()))
					ConfigParms.setDownloadMode(ConfigParms.DOWNLOAD_MODE.POPULAR);
			}
			else
				LOGGER.warn("Ignoring arg "+ keyVal[0]);
		}
		if(startTimeIn != null){
			startTimeIn = startTimeIn.replace('T', ' ');
			startTime = DateTimeUtils.getTime(startTimeIn);
		}
		if(endTimeIn != null){
			endTimeIn = endTimeIn.replace('T', ' ');
			endTime = DateTimeUtils.getTime(endTimeIn);
		}
		LOGGER.info("Reconciling products downloaded after " + startTime + " every " + stepInHr + " hour." + (endTime != null?"Reconciliation will reconcile till " + endTime: ""));
		reconcile(startTime, endTime, stepInHr);
	}
}

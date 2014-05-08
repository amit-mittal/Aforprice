package parsers;

import global.errorhandling.ErrorCodes;
import global.exceptions.Bhagte2BandBajGaya;
import helper.Validate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.robotsProtocol.CrawlerProtocol;
import stores.ProductStore;
import util.ConfigParms;
import util.Constants;
import util.MetricCollector;
import util.RawMetric;
import util.Utils;
import db.dao.CategoryParseErrorRecorder;
import db.dao.CategoryParseErrorRecorder.ERRTYPE;
import db.dao.DAOException;
import db.dao.ProductSummaryPageDAO;
import db.dao.ReconilationDAO;
import entities.ProductSummary;
import entities.Retailer;


public abstract class ProductsParser {
		
	protected static final int UNIMPLEMENTED = -1;
	private final String retailerId;
	private static final Logger logger = Logger.getLogger(ProductsParser.class);
	
	private static final CategoryParseErrorRecorder errRecorder = CategoryParseErrorRecorder.getInstance();
	@SuppressWarnings("unused")
	private static final ProductSummaryPageDAO summPgDAO = new ProductSummaryPageDAO();
	
	
	//For keeping the hash of all the orders in a page along with ordering information
	private Map<String, String> pageHashes = new HashMap<String, String>();
	protected Map<String, String> visitedURLs = null;
	//<Category, <Product Name, Null>>
	private Map<String, Map<String, String>> parsedProdsCache = new HashMap<String, Map<String,String>>();
	protected Map<String, String> cookies = new HashMap<String, String>();
	private int expectedProductCount;
	
	public ProductsParser(String retailerId){
		this.retailerId = retailerId;
	}
	
	public String getRetailerId(){
		return retailerId;
	}
		
	public void parseAndSave(int categoryId, String categoryName, String categoryURL, ProductStore productStore) throws IOException, DAOException{
		try{
			if(skipCategory(categoryName))
				return;
			int total = 0;
			int MAX = Constants.NUM_PRODS_TO_DOWNLOAD.get(ConfigParms.getDownloadMode(), getRetailerId());
			Validate.notNull(productStore, ErrorCodes.INVALID_INPUT);
			Validate.notNull(categoryURL, ErrorCodes.INVALID_INPUT);
			visitedURLs = new HashMap<String, String>();	
			categoryURL = transformUrl(categoryURL);
			
			/* Initializing the robots.txt Exclusion directives before downloading any URL */
			CrawlerProtocol.initializeDirectives(retailerId);
			
			Document doc = Utils.connect(categoryURL, cookies, retailerId);			
			int pgNo = 1;
			int startRank = -1;
			//Initialize startRank to 1
			try{
				if(isSortedByBestSeller(doc, categoryId)){
					startRank = 1;
				}
			}catch(Exception e){
				logger.warn(e.getMessage(), e);
				errRecorder.record(retailerId, categoryId, categoryName, ERRTYPE.IS_SORETED_BS, e.getMessage());
			}
			List<ProductSummary> products = parseAndSaveHtml(categoryURL, doc, categoryId, categoryName, startRank, pgNo);
			total += products.size();
			saveProducts(productStore, products, categoryURL, categoryId, categoryName);
			String currentURL = categoryURL;
			String nextURL = null;
			while((nextURL = getNextURL(doc, currentURL, categoryURL, pgNo)) != null){
				if(cycleExists(nextURL)){
					errRecorder.record(retailerId, categoryId, categoryName, ERRTYPE.CYCLE, nextURL);
					throw new Bhagte2BandBajGaya("Found a cycle for " + nextURL);
				}
				++pgNo;
				currentURL = nextURL;
				doc = Utils.connect(nextURL, cookies, retailerId);
				//Check for best seller sorting. If any page is not sorted by best sellers ranking
				//we stop populating the best selling rank.
				try{
					if(startRank != -1 && isSortedByBestSeller(doc, categoryId)){
						startRank += products.size();
					}
					else
						startRank = -1;
				}catch(Exception e){
					startRank = -1;
					logger.warn(e.getMessage(), e);
					errRecorder.record(retailerId, categoryId, categoryName, ERRTYPE.IS_SORETED_BS, e.getMessage());
				}
				products = parseAndSaveHtml(nextURL, doc, categoryId, categoryName, startRank, pgNo);
				if(products == null || products.size() == 0)
					break;
				total += products.size();
				saveProducts(productStore, products, nextURL, categoryId, categoryName);
				if(total >= MAX){
					logger.info("Downloaded " + total + " products. Required to download " + MAX);
					break;
				}
			}
			printAndSaveDownloadSummary(categoryName, total, categoryId);
		}finally{
			clearCache(categoryName);//Remove the cache for this category, if it exists
			System.gc();//Clean up memory
		}
	}

	long totalTimeInSavingCountMismatches = 0;
	protected void printAndSaveDownloadSummary(String categoryName, int total, int categoryId)
	{		
		try{
			// Total product count should match expected, if we are not running in POPULAR mode
			int maxCount = Constants.NUM_PRODS_TO_DOWNLOAD.get(ConfigParms.getDownloadMode(), getRetailerId());
			if(expectedProductCount != UNIMPLEMENTED)
			{	
				expectedProductCount = Math.min(expectedProductCount, maxCount);
				if(total != expectedProductCount)
					logger.warn("Category:" + categoryName + "(" + categoryId +"),Total product downloaded:" + total + ",Expected count: " + expectedProductCount);
				else
					logger.info("Category:" + categoryName + "(" + categoryId +"),Total product downloaded:" + total + ",Expected count: " + expectedProductCount);
				
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				ReconilationDAO reconilationDAO = ReconilationDAO.getInstance();
				reconilationDAO.storeRec(retailerId + ":"+String.valueOf(categoryId), ts, 
					ReconilationDAO.NAME.COUNTER, 
					ReconilationDAO.TYPE.PRODUCTSPERCATEGORY, String.valueOf(expectedProductCount),
					Integer.valueOf(total), ts);
				totalTimeInSavingCountMismatches += System.currentTimeMillis() - ts.getTime();
				MetricCollector.RawMetricQueue.add(new RawMetric(this.retailerId + ":reconcilationDAO:storeRec", ts.getTime(), System.currentTimeMillis() ));				
			};
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		

	}
	
	protected String getNextURL(Document doc, String currentURL, String categoryURL)
	{
		return getNextURL(doc);
	}
	
	protected String getNextURL(Document doc, String currentURL, String categoryURL, int currPage){
		return getNextURL(doc, currentURL, categoryURL);
	}

	/*
 * used in file based run 
 */
	public void parseFileAndSave(int categoryId, String categoryName, String filepath, ProductStore productStore) throws IOException{
		try{
			if(skipCategory(categoryName))
				return;
			Validate.notNull(productStore, ErrorCodes.INVALID_INPUT);
			Validate.notNull(filepath, ErrorCodes.INVALID_INPUT);
			Validate.notNull(Retailer.getRetailer(getRetailerId()), "Can't find retailer for retailerid"+getRetailerId());
			visitedURLs = new HashMap<String, String>();
			Document doc = Jsoup.parse(new File(filepath), null, Retailer.getRetailer(getRetailerId()).getLink());
			//TODO: Implement ranking
			List<ProductSummary> products = parse(filepath, doc, categoryId, categoryName, -1, -1);
			saveProducts(productStore, products, filepath, categoryId, categoryName);
		}finally{
			clearCache(categoryName);//Remove the cache for this category, if it exists
			System.gc();//Clean up memory
		}
	}

	public void parseAndSave(ProductStore productStore) throws Exception{
		throw new UnsupportedOperationException();
	}
		
	protected String transformUrl(String url){
		return url;
	}
		
	protected Element getPriceElement(Element root){
		return getMatching(root, getClassNames().price());
	}
	
	protected Element getDecimalPriceElement(Element root){
		return getMatching(root, getClassNames().decimalPrice());		
	}
	
	protected boolean skipCategory(String categoryName){
		if(Utils.isEmpty(categoryName))
				return true;
		String name = categoryName.trim().toLowerCase();
		if(name.startsWith("see all" ) ||
				name.toLowerCase().startsWith("all") || 
				name.startsWith("shop all") ||
				name.startsWith("view all") ||
				name.startsWith("show all")){
			logger.info("Skipping category: " + categoryName);
			return true;
		}
			
		return false;
	}
	
	protected abstract boolean isSortedByBestSeller(Document doc, int categoryId);
	
	protected abstract double getReviewRating(Element product);
	
	protected abstract int getNumReviews(Element product);

	protected void addProductToCache(String categoryName, String prodName){
		String catKey = categoryName.toLowerCase();
		if(!parsedProdsCache.containsKey(catKey)){
			parsedProdsCache.put(catKey, new HashMap<String, String>());
			
		}
		parsedProdsCache.get(catKey).put(prodName.toLowerCase(), null);
		
	}
	protected void addProductsToCache(String categoryName, Map<String, String> products){
		String catKey = categoryName.toLowerCase();
		if(!parsedProdsCache.containsKey(catKey)){
			parsedProdsCache.put(catKey, new HashMap<String, String>());
			
		}
		parsedProdsCache.get(catKey).putAll(products);
	}
	protected void clearCache(String categoryName){
		parsedProdsCache.remove(categoryName.toLowerCase());
		pageHashes.clear();
	}
	protected boolean existsInCache(String categoryName, String prodName){
		String catKey = categoryName.toLowerCase();		
		if(!parsedProdsCache.containsKey(catKey)){
			return false;
		}
		return parsedProdsCache.get(catKey).containsKey(prodName.toLowerCase());
	}

	public abstract List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max);
	protected abstract String getNextURL(Document doc);
	protected abstract ClassNames getClassNames();

	private boolean cycleExists(String url){		
		return(visitedURLs.containsKey(url));
	}
	
	protected List<ProductSummary> parseAndSaveHtml(String url, Document doc, int catId, String catName, int startRank, int pgNo){
		List<ProductSummary> products = null;
		try{
			if(pgNo == 1){
				//Get product count
				 expectedProductCount = getProductCountForCategory(doc);
			}
			products = parse(url, doc, catId, catName, startRank, -1);	
			logger.info(getRetailerId() + ":" + catId + ":Got " + (products != null?products.size(): 0) + " products");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			errRecorder.record(getRetailerId(), catId, catName, ERRTYPE.PARSEERR, url);
		}
		saveHtml(products, catId, catName, url, doc.html(), pgNo);
		return products;
	}

	//If you override, this please return 0 when you don't know the expected productcount.
	//If you return -1 it would mean unimplemented.
	protected int getProductCountForCategory(Document doc)
	{
		return UNIMPLEMENTED;
	}

	/**
	 * Validates the products and then saves them if all products valid.
	 * @param productStore
	 * @param products
	 * @param category
	 * @param url
	 * @return
	 */
	protected boolean saveProducts(ProductStore productStore, List<ProductSummary> products, String url, int categoryId, String category){
		visitedURLs.put(url, null);
		if(products == null || products.size() == 0){
			return false;
		}		
		List<ProductSummary> productsToSave = new ArrayList<ProductSummary>(products.size());
		Map<String, String> prodsToCache = new HashMap<String, String>();		
		StringBuilder hashBuilder = new StringBuilder();
		for(ProductSummary prod: products){
			hashBuilder.append(prod.getName().toLowerCase());				
			productsToSave.add(prod);			
			prodsToCache.put(prod.getName().toLowerCase(), null);										
		}
		if(hashBuilder.length() > 0){
			String hash = products.get(0).getName().toLowerCase() + hashBuilder.toString().hashCode() + products.get(products.size()-1).getName().toLowerCase();
			if(pageHashes.containsKey(hash)){
				//cycle
				errRecorder.record(retailerId, categoryId, category, ERRTYPE.CYCLE, pageHashes.get(hash) + "|" + url);
				logger.error("Cycle found: prev url = " + pageHashes.get(hash) + ": new url" + url);
				throw new Bhagte2BandBajGaya("Cycle found as the pageHashes already has the hash for " + url);
			}
			pageHashes.put(hash, url);
		}			
		addProductsToCache(category, prodsToCache);		
		return productStore.save(productsToSave);	
	}
	
	private void saveHtml(List<ProductSummary> products, int catId, String cat, String url, String html, int pageNo){
		try{
			String pathName = Constants.PRODUCT_SUMMARY_HTML_FILES.path(getRetailerId(), cat, new Date(), products == null||products.size() == 0);
			File path = new File(pathName);
			if(!path.exists()){
				path.mkdirs();
			}
			String fileName = catId + "_" + pageNo + ".html";
			File htmlFilePath = new File(path, fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFilePath));
			writer.write(html);		
			writer.close();
		}catch(Exception e){
			logger.error("Error saving the following html", e);
			logger.error(html);
		}
	}

	protected Element getMatching(Element root, String[] priceClasses){		
		Element elm = null;
		for(String priceClass: priceClasses){
			elm = root.getElementsByClass(priceClass).first();
			if(elm != null)
				break;
		}
		return elm;
	}

	protected Elements getFirstMatching(Element root, String[] priceClasses){		
		Elements elm = null;
		for(String priceClass: priceClasses){
			elm = root.getElementsByClass(priceClass);
			if(elm != null && elm.size() > 0)
				break;
		}
		return elm;
	}
}
package uploader.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import parsers.ProductsParser;
import parsers.ProductsParserFactory;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.Constants;
import util.MetricCollector;
import util.Utils;
import db.DbConnection;
import db.DbConnectionPool;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import entities.Category;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public abstract class NeoCategoryUploader {
	private static final Logger logger = Logger.getLogger(NeoCategoryUploader.class);
	private DbConnection _dbConnection;
	private CategoryDAO _categoryDAO;
	public static StringBuilder failedParsingURLs = new StringBuilder();
	protected String retailerId;
	protected String retailerLink;
	private HashMap<String, Set<String>> haveSeenUrls = new HashMap<>();
	public static StringBuilder failedToStoreCategories = new StringBuilder();
	//category stats
	private int categoryInserted = 0, categoryUpdated = 0, categoryUnchanged = 0, categoryFailed=0;
	private DbConnectionPool connectionPool = DbConnectionPool.get();
	protected List<Category> allCategories = new ArrayList<Category>();
	private String debuCategory;
	private boolean debug;
	private List<Category> existingCategories;
	protected Map<String, String> cookies = new HashMap<String, String>();
	
	public NeoCategoryUploader(String retailerId, String retailerLink) throws UploaderException{
		this.retailerId = retailerId;
		this.retailerLink = retailerLink;
		if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.REALTIME){
			_dbConnection = getDbConnection();
			try{
				_categoryDAO = new CategoryDAO();
			}catch(Exception e){
				throw new UploaderException(e.getMessage());
			}
		}
	}
	
	public NeoCategoryUploader(Retailer retailer) throws UploaderException{
		this(retailer.getId(), retailer.getLink());
	}

	private DbConnection getDbConnection(){		
		return connectionPool.getConnection();
	}
	
	/**
	 * Does a depth first traversal of the category tree and stores the categories in the database one full path at a time.
	 * Also does the following sanity checks on the categories
	 * 1. Check against the change in the number of categories obtained with respect to the previous run. 
	 */
	public void walkAndStore() {
		/*
		 * Filemode is backward compatible, it will go to website if can't find the page locally
		 */
		ConfigParms.getInstance().setMode(RUNTIME_MODE.FILEMODE);
		ConfigParms.getInstance().setFileRunDate(new Date());
		long startTime = System.currentTimeMillis();
		try{
			//lets store existing categories first in the cache for rec
			if(this.isDebug())
				setExistingCategories(_categoryDAO.getAllChildCategoriesForCategory(this.getDebuCategory(), this.retailerId));
			else
				setExistingCategories(_categoryDAO.getActiveCategoriesForRetailer(this.retailerId));
			List<Category> rootCategories = getRootCategories();
			if(rootCategories == null || rootCategories.size() == 0){
				throw new Bhagte2BandBajGaya("No root categories found for " + retailerId);
			}
			String rootFolder = Constants.CATEGORY_HTML_FILES.path(getRetailerId(), new Date());
			for(Category rootCat: rootCategories){
				if(this.isDebug() && !this.getDebuCategory().equals(rootCat.getName())){
					logger.info("Debug mode, skipping category " + rootCat.getName());
					continue;
				}
				if(rootCat.getUrl() == "")
					continue;
				String categoryFolder = rootFolder + Utils.removeNonFolderChars(rootCat.getName()) + File.separator;
				Document rootCatDoc = getDocument(rootCat.getUrl(), categoryFolder);
				if(rootCatDoc == null)
					continue;
				CategoryType type = getCategoryType(rootCat.getUrl(), rootCatDoc, rootCat.getName());
				rootCat.setType(type);
				if(!type.equals(CategoryType.PARENT)){
					allCategories.add(rootCat);
					storeCategory(rootCat);
					continue;
				}
				getAllSubCategories(1, rootCat, rootFolder);//we will be getting this rootCat again that's why pass parent folder which is rootFolder
				printCategoryStats(startTime, false);
			}//for(Category roo ends...
			//run test to verify new and existing data
			printCategoryStats(startTime, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally{
			shutDown();
			if(failedParsingURLs.length()>0)
				logger.error("Failed Parsing Below URLs\n" + failedParsingURLs.toString());
			if(failedToStoreCategories.length()>0)
				logger.error("Failed To Store Below Categories in DB\n" + failedToStoreCategories.toString());
			printCategoryStats(startTime, true);
			Utils.printUrlGetTimings();
			Thread metricCollectorShuDownRunner = new Thread(MetricCollector.SHUTDOWN_RUNNER);
			Runtime.getRuntime().addShutdownHook(metricCollectorShuDownRunner);
			
		}
	}
	
	protected void printCategoryStats(long startTime, boolean complete){
		logger.info("categoryInserted=" +  categoryInserted + ", categoryUpdated=" + categoryUpdated +
				", categoryUnchanged=" + categoryUnchanged + ", categoryFailed=" + categoryFailed);		
		long runTime = System.currentTimeMillis() - startTime;
		int min = (int) (runTime/60e3);
		int sec = (int) (runTime%60e3)/1000;
		int millis = (int) runTime%1000;
		logger.info((complete?"Total ":"") + "run time:" + min + " min, " + sec + " sec," + millis + " millis");
	}
	
	/**
	 * Recursively traverses all the categories.
	 * @param category
	 * @param parentFolder - folder of parent category where files are stored
	 * @return
	 */
	private void getAllSubCategories(int level, Category category, String parentCategoryFolder){
		logger.info(getLevelInfoString(level) + "Get SubCategory for Category - " + category.getName() + ", " + category.getUrl());
		
		if(getHaveSeenUrls().containsKey(category.getUrl())){
			Set<String> catIds = getHaveSeenUrls().get(category.getUrl());
			//Ignore this if it we have already seen this category url and same category unique id. 
			//Not enough to ignore on the basis of only category url, as same category may be present
			//under different parent categories and we want to capture that structure, so that our 
			//categories look similar to 
			if(catIds.contains(category.getUniqueId())){
				logger.info("Skipping category " + category.getUniqueId() + " as " + catIds + " have been processed");
				return;
			}
		}
		else{
			getHaveSeenUrls().put(category.getUrl(), new HashSet<String>());
		}
		getHaveSeenUrls().get(category.getUrl()).add(category.getUniqueId());
		String categoryFolder = parentCategoryFolder + Utils.removeNonFolderChars(category.getName()) + File.separator; 
		allCategories.add(category);
		Document parentCatDoc = getDocument(category.getUrl(), categoryFolder);
		if(parentCatDoc == null){
			category.setType(CategoryType.UNKNOWN);
			storeCategory(category);
			return;
		}
		CategoryType catType = getCategoryType(category.getUrl(), parentCatDoc, category.getName());
		category.setType(catType);
		storeCategory(category);//required to get category_id from db before getting child categories which need this id
		if(!catType.equals(CategoryType.PARENT)){
			return;
		}
		List<Category> subCats = getSubCategories(level+1, category, parentCatDoc);
		for(Category prodCat: subCats){
			getAllSubCategories(level+1, prodCat, categoryFolder);
		}
		//return allCats;
	}
		
	protected void shutDown(){
		if(_dbConnection!=null){
			connectionPool.releaseConnection(_dbConnection);
		}
	}

	protected void terminate(){
		logger.info("calling system exit");
		System.exit(0);
	}

	protected void storeCategories(List<Category> categories) throws DAOException{
		logger.warn("Storing categories in the database");
		for(Category category : categories){
			allCategories.add(category);
			storeCategory(category);				
		}		
	}

	protected void storeCategory(Category category) {
		try{
			CategoryDAO.DB_RESULT result = _categoryDAO.recordCategory(category);
			if(result== CategoryDAO.DB_RESULT.UNCHANGED)
				categoryUnchanged++;
			/*log("Category already exists, skipped - " + category.getName());*/
			if(result== CategoryDAO.DB_RESULT.UPDATED){
				categoryUpdated++;
				logger.info("Category updated - " + category.getName());
			}
			else if (result == CategoryDAO.DB_RESULT.INSERTED){
				categoryInserted++;
				logger.info("Category inserted - " + category.getName());

			}
			else if(result== CategoryDAO.DB_RESULT.FAILURE){
				categoryFailed++;
				logger.error("Category insert failed, skipped - " + category.getName());
				failedToStoreCategories.append("DB UNKNOWN FAILURE" + " " + category.getName() + " " + category.getUrl()).append("\n");
			}
			logger.info(category.getParentName() + ":" + category.getName() + ":" + category.getUrl());
		}
		catch(DAOException e){
			categoryFailed++;
			e.printStackTrace();
			failedToStoreCategories.append(e.toString() + " " + category.getName() + " " + category.getUrl()).append("\n");
		}
	}

	/*
	 * get document for given cat's url
	 * @param categoryFolder: store downloaded file under this folder
	 *     
	 */
	protected Document getDocument(String url, String categoryFolder){
		try{
			if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.FILEMODE && !ConfigParms.getInstance().isProduction() && Constants.isWindows()){ //we don't need to store files in prod or on linux since this is just for development
//				categoryFolder = Utils.removeNonFolderChars(categoryFolder);
				File f = new File(categoryFolder+"index.html");
				if(f.exists())
					return Jsoup.parse(f, null, Retailer.getRetailer(getRetailerId()).getLink());
				logger.debug("Downloading from web, can't find file " + f.getAbsolutePath());
			}
			Document doc = Utils.connect(url, cookies, retailerId);
			if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.FILEMODE && !ConfigParms.getInstance().isProduction() && Constants.isWindows())
				this.saveHtml(url, doc.html(), categoryFolder);
			return doc;
		}catch(Bhagte2BandBajGaya e){
			failedParsingURLs.append(url);
			logger.error("Skipping url " + url, e);
		}catch(IOException e){
			throw new Bhagte2BandBajGaya(e.toString());
		}
		return null;
	}
	/*
	 * called by subclasses to get root document
	 */
	protected Document getDocument(String url) {
		return getDocument(url, Constants.CATEGORY_HTML_FILES.path(getRetailerId(), new Date()));
	}
	/*
	 * save html of category mainly for local debugging and development
	 * turned off in production and on linux box
	 */
	private void saveHtml(String url, String html, String categoryFolder){
		try{
	//		categoryFolder = Utils.removeNonFolderChars(categoryFolder);
			File path = new File(categoryFolder);
			if(!path.exists()){
				path.mkdirs();
			}
			File htmlFilePath = new File(path, "index.html");
			BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFilePath));
			writer.write(html);		
			writer.close();
		}catch(Exception e){
			logger.error("Error saving the following html", e);
			logger.error(html);
		}
	}

	protected String getRetailerId(){
		return retailerId;
	}
	
	protected String getRetailerLink() {
		return retailerLink;
	}

	protected CategoryType getCategoryType(String url, Document categoryDoc, String name){
		ProductsParser parser = ProductsParserFactory.get(retailerId);
		try{									
			List<ProductSummary> products = parser.parse(url, categoryDoc, 0, "NA",-1, 2);
			if(products != null && products.size() > 0){
				//This is the terminal category and does not have any sub-categories
				return CategoryType.TERMINAL;
			}
			//This category should be a parent unless we are not able to extract sub-categories out of it
			return CategoryType.PARENT;
		}catch(Exception e){
			e.printStackTrace();
			return CategoryType.UNKNOWN;
		}		
	}
	protected void logErrCategories(String url, String errTxt, Throwable err){
		//TODO: insert this categoryurl into database.
	}
			
	/**
	 * This function returns the list of top level categories for a web-site. From the top level categories, rest of the categories
	 * are obtained by recursively traveling the tree of categories down from the root categories upto the terminal categories i.e.
	 * the categories which do not have any children categories.
	 * The categories returned by this function do not parent category id set, and the category type is unknown.    
	 * @return List of the root categories
	 */
	protected abstract List<Category> getRootCategories();
	
	
	/**
	 * Get the list of categories under the parent category. This function is to be invoked recursively in depth first search manner 
	 * in order to traverse through all the categories.
	 * @param parent Parent category
	 * @param level Level is only for logging purpose
	 * @return List of categories for which the category type is not known.
	 */
	protected abstract List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc);

	protected String getLevelInfoString(int level){
		StringBuilder strLog = new StringBuilder();
		//add tabs
		for(int i=1;i<=level;i++){
			strLog.append("\t");
		}
		strLog.append("L").append(level).append(":");
		return strLog.toString();
	}

	public List<Category> getAllCategories(){
		return this.allCategories;
	}
	
	public String getDebuCategory() {
		return debuCategory;
	}

	public void setDebuCategory(String debuCategory) {
		this.debuCategory = debuCategory;
		this.setDebug(true);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public List<Category> getExistingCategories() {
		return existingCategories;
	}

	public void setExistingCategories(List<Category> existingCategories) {
		this.existingCategories = existingCategories;
	}

	public HashMap<String, Set<String>> getHaveSeenUrls() {
		return haveSeenUrls;
	}

}

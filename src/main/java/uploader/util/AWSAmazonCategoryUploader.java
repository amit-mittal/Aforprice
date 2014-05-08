package uploader.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.SignedRequestHandler;
import util.UtilityLogger;
import util.Utils;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public class AWSAmazonCategoryUploader extends NeoCategoryUploader {
	
	private static final String AWS_ACCESS_KEY_ID = "AKIAJWXLVHIMPKIZ346Q";
	private static final String AWS_SECRET_KEY = "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";
	private static final String AWS_ASSOCIATE_TAG = "ravede-20";
	
	/*
	 * Region wise endpoints:
	 * 		US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
	 */
	private static final String ENDPOINT = "ecs.amazonaws.com";
	
	private static final String BrowseNodeIdLink = "http://docs.amazonwebservices.com/AWSECommerceService/latest/DG/BrowseNodeIDs.html";
	
	public AWSAmazonCategoryUploader() throws UploaderException {
		super(Retailer.AWSAMAZON);
	}
	
	public String ConstructAwsQuery(String BrowseNodeId) {
		SignedRequestHandler helper;
        try {
            helper = SignedRequestHandler.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrl = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("Operation", "BrowseNodeLookup");
        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
        params.put("BrowseNodeId", BrowseNodeId);

        requestUrl = helper.sign(params);
        return requestUrl;
	}
	
	@Override
	public void walkAndStore() {
		long startTime = System.currentTimeMillis();
		try {
			if(this.isDebug())
				setExistingCategories(new CategoryDAO().getAllChildCategoriesForCategory(this.getDebuCategory(), this.retailerId));
			else
				setExistingCategories(new CategoryDAO().getActiveCategoriesForRetailer(this.retailerId));
			
			List<Category> rootCategories = getRootCategories();
			if(rootCategories == null || rootCategories.size() == 0)
				throw new Bhagte2BandBajGaya("No root categories found for " + retailerId);
			for(Category rootCat: rootCategories) {
				if(this.isDebug() && !this.getDebuCategory().equalsIgnoreCase(rootCat.getName())) {
					UtilityLogger.logInfo("Debug mode, skipping category " + rootCat.getName());
					continue;
				}
				
				String BrowseNodeId = rootCat.getGenericName();
				if(BrowseNodeId == ""){
					continue;
				}
				
				getAllSubCategories(1, rootCat);
				printCategoryStats(startTime, false);
				//run test to verify new and existing data
			}//for(Category rootCat: rootCategories) ends...
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			shutDown();
			if(failedParsingURLs.length() > 0)
				UtilityLogger.logError("Failed Parsing Below URLs\n" + failedParsingURLs.toString());
			if(failedToStoreCategories.length() > 0)
				UtilityLogger.logError("Failed To Store Below Categories in DB\n" + failedToStoreCategories.toString());
			printCategoryStats(startTime, true);
			Utils.printUrlGetTimings();
		}
	}
	
	public String getCategoryURL(String id) {
			SignedRequestHandler helper;
	        try {
	            helper = SignedRequestHandler.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {	
	            e.printStackTrace();
	            return null;
	        }
	        
	        String requestUrl = null;
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Version", "2011-08-01");
	        params.put("Operation", "ItemSearch");
	        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
	        params.put("BrowseNode", id);
	        params.put("ResponseGroup", "Small");
	        /*
	         * SearchIndex - Does not make any difference to the result set
	         * when the BrowseNode Id is specified. Learned from experimentation.
	         * But is necessary for the query.
	         */
	        params.put("SearchIndex", "Appliances");

	        requestUrl = helper.sign(params);
	        Document doc = getDocument(requestUrl);
	        
	        Element helperEle = doc.getElementsByTag("MoreSearchResultsUrl").first();
	        
	        return helperEle.ownText();
	}

	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		List<String> BrowsNodeIds = new ArrayList<String>();
		try {
			Document doc = getDocument(BrowseNodeIdLink);
			
			Element helper = doc.getElementsByClass("informaltable").first();
			Elements BrowseNodeIdElements = helper.getElementsByTag("tr");

			for(Element BrowseNodeIdEle: BrowseNodeIdElements) {
				helper = BrowseNodeIdEle.getElementsByTag("td").last();
				if(helper == null)
					continue;
				String temp = helper.ownText();
				
				if(temp == null || temp.length() == 1)
					continue;
				BrowsNodeIds.add(temp);
			}
			
			Category categoryObject = null;
			
			for(String BrowseNodeId: BrowsNodeIds) {
				String url = ConstructAwsQuery(BrowseNodeId);
				
				doc = getDocument(url);
				//System.out.println("url: "+url);
				
				String id, name, id2;
				
				helper = doc.getElementsByTag("Ancestors").first();
				if(helper != null) {
					helper = helper.getElementsByTag("BrowseNode").first();
					id = helper.getElementsByTag("BrowseNodeId").first().ownText();
					name = helper.getElementsByTag("Name").first().ownText();
					
					helper = doc.getElementsByTag("BrowseNode").first();
					id2 = helper.getElementsByTag("BrowseNodeId").first().ownText();
				} else {
					helper = doc.getElementsByTag("BrowseNode").first();
					if(helper == null)
						continue;
					id = helper.getElementsByTag("BrowseNodeId").first().ownText();
					name = helper.getElementsByTag("Name").first().ownText();
					
					helper = doc.getElementsByTag("Children").first();
					helper = helper.getElementsByTag("BrowseNode").first();
					id2 = helper.getElementsByTag("BrowseNodeId").first().ownText();
				}
				
				String categoryUrl = getCategoryURL(id);
				categoryObject = new CategoryBuilder(getRetailerId(), null, name, categoryUrl).retailerCategoryId(id2).build();
				UtilityLogger.logInfo("L0:\t" + name + " | " + id + " | "+categoryUrl);
				rootCats.add(categoryObject);
			}
			//System.out.println("\n\n Finally program finished!!!");
			//System.exit(0);
		} catch(Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		}
		return rootCats;
	}
	
	protected void getAllSubCategories(int level, Category parentCat) {
		UtilityLogger.logInfo(getLevelInfoString(level) + "Get SubCategory for Category - " + parentCat.getGenericName() + ", " + parentCat.getUrl());
		if(getHaveSeenUrls().containsKey(parentCat.getUrl())){
			Set<String> catIds = getHaveSeenUrls().get(parentCat.getUrl());
			if(catIds.contains(parentCat.getUniqueId()))
				return;
		}
		else{
			getHaveSeenUrls().put(parentCat.getUrl(), new HashSet<String>());
		}
		getHaveSeenUrls().get(parentCat.getUrl()).add(parentCat.getUniqueId());
		
		allCategories.add(parentCat);
		//allCats.add(parentCat);
		
		Document parentCatDoc = getDocument(ConstructAwsQuery(parentCat.getRetailerCategoryId()));
		if(parentCatDoc == null) {
			storeCategory(parentCat);
			return;
		}
		
		CategoryType catType = getCategoryType(parentCatDoc);
		parentCat.setType(catType);
		storeCategory(parentCat);//required to get category_id from db before getting child categories which need this id
		if(catType.equals(CategoryType.TERMINAL)) {
			return;
		}
		List<Category> subCats = getSubCategories(level+1, parentCat, parentCatDoc);
		for(Category prodCat: subCats) {
			getAllSubCategories(level+1, prodCat);
		}
		return;
	}
	
	protected CategoryType getCategoryType(Document categoryDoc) {
		try {
			Element helper = categoryDoc.getElementsByTag("BrowseNode").first();
			helper = helper.getElementsByTag("Children").first();
			
			if(helper != null)
				return CategoryType.PARENT;
			else
				return CategoryType.TERMINAL;
		} catch(Exception e) {
			e.printStackTrace();
			return CategoryType.UNKNOWN;
		}
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element helper = parentCatDoc.getElementsByTag("Children").first();
		Elements categoryElements = helper.getElementsByTag("BrowseNode");
		
		for(Element categoryElement: categoryElements) {
			String name = categoryElement.getElementsByTag("Name").first().ownText();
			String Id = categoryElement.getElementsByTag("BrowseNodeId").first().ownText();
			
			Category subCategoryObject = new CategoryBuilder(getRetailerId(), parentCat, name, getCategoryURL(Id)).retailerCategoryId(Id).build();
			UtilityLogger.logInfo(getLevelInfoString(level) + "\t" + name + "|" + Id );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
	
	public static void main(String[] args) {
		AWSAmazonCategoryUploader uploader = null;
		try {
			uploader = new AWSAmazonCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
		//System.out.println(uploader.ConstructAwsQuery("493964"));
		//System.out.println(uploader.getCategoryURL("493964"));
	}
}

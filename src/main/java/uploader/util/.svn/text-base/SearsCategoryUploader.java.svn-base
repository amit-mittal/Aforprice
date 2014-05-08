package uploader.util;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;


public class SearsCategoryUploader extends NeoCategoryUploader{
	public SearsCategoryUploader() throws UploaderException{
		super(Retailer.SEARS);
	}

	/*
	 * sample category page
	<div id="NavContainer">
	<ul class="clearfix" id="deptNav">
		<li><a rev="om" class="leftend" href="/shc/s/v_10153_12605_Appliances?adCell=WH" id="appliances_bar">Appliances</a></li>
		<li class="intShipHide"><a rev="om" href="/shc/s/v_10153_12605_Automotive?adCell=WH" id="auto_bar">Automotive &amp; Tires</a></li>
		<li><a rev="om" href="/shc/s/v_10153_12605_Baby?adCell=WH" id="baby_bar">Baby</a></li>
		<li><a rev="om" href="/shc/s/v_10153_12605_Beauty?adCell=WH" id="beauty_bar">Beauty</a></li>
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try{
			Document doc = getDocument(getRetailerLink());
			//Get navcontainer which has all categories
			Element navigationContainer = doc.getElementsByAttributeValue("id", "NavContainer").first();
			//Each category has 'href' attribute
			Elements catLinkElms = navigationContainer.getElementsByAttribute("href");
			for(Element category : catLinkElms){//Appliances, Automotive &amp; Tires
				String name = category.text().trim();
				String url = category.absUrl("href");
				Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
//				UtilityLogger.logInfo(category.text().trim() + "\t" + category.absUrl("href"));
				rootCats.add(prodCat);				
			}

		} catch (Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		} 
		return rootCats;
	}
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element categoryBlock =null;
		if(parentCatDoc.getElementsByClass("grid_col_1").size()!=0)
			categoryBlock = parentCatDoc.getElementsByClass("grid_col_1").first();
		else if(parentCatDoc.getElementsByClass("col_1").size()!=0)
			categoryBlock = parentCatDoc.getElementsByClass("col_1").first();
		else{
			UtilityLogger.logWarning("No subcategories on page " + parentCat.getUrl());
			return categories;
		}
		
		Elements childCategories = categoryBlock.getElementsByAttribute("href");
		for(Element childCategory : childCategories){
			String name = childCategory.text().trim();
			String url = childCategory.absUrl("href");
			if(name.contains("View All") || name.contains("See All")){ //ignore
				UtilityLogger.logWarning(getLevelInfoString(level) + "Skipping category to avoid loops" + name + "|\t" + url );			
				continue;
			}				
			Category subCategoryObject= new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			UtilityLogger.logInfo(getLevelInfoString(level) + name + "|\t" + url );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearsCategoryUploader uploader = null;
		try {
			uploader = new SearsCategoryUploader();
			//uploader.setDebuCategory("Sports Fan Shop");
		} catch (UploaderException e) {
			e.printStackTrace();
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}

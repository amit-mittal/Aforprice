package uploader.util;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;


public class WalmartCategoryUploader extends NeoCategoryUploader{
	public WalmartCategoryUploader() throws UploaderException{
		super(Retailer.WALMART);
	}

	/*
	 * sample category page
	<div id="com_wm_module_g0066_title_307474" class="mainSpriteHDR flyoutNavCategory ddMenuOff"><span class="arrow">Movies, Music & Books</span>
		<div class="InlineBlock">
			<a class="MainCategory"href="/cp/movies-tv/4096">Movies & TV</a>
			<a href="/cp/blu-ray/616859">Blu-ray Discs</a>
			<a href="http://see.walmart.com/moviecenter/">Moviecenter</a>
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try{
			String url, categoryName;
			Document doc = getDocument(getRetailerLink());
			//iterate over category
			Elements categories = doc.getElementsByClass("MainCategory");
			for(Element category : categories){
				//Movies & TV
				categoryName = category.childNodes().get(0).toString();
				url = category.attr("abs:href");
				UtilityLogger.logInfo("L1:" + categoryName + "|\t" + url );
				Category categoryObject = new CategoryBuilder(getRetailerId(), null, categoryName, url).build();
				rootCats.add(categoryObject);
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
		Elements childCategories = parentCatDoc.getElementsByClass("browseInOuter");
		for(Element childCategory : childCategories){
			String name = childCategory.children().first().html();
			String url = childCategory.children().first().attr("abs:href");
			Category subCategoryObject=new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			UtilityLogger.logInfo(getLevelInfoString(level) + name + "|\t" + url );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WalmartCategoryUploader uploader = null;
		try {
			uploader = new WalmartCategoryUploader();			
			//uploader.setDebuCategory("Sports Fan Shop");
		} catch (UploaderException e) {
			e.printStackTrace();
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}

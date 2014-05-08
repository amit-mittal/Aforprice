package uploader.util;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Constants;
import util.UtilityLogger;
import util.Utils;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;


public class LowesCategoryUploader extends NeoCategoryUploader{

	public LowesCategoryUploader() throws UploaderException{		
		super(Retailer.LOWES);
		//Lowes give prices based on your location, below cookie will set your local store and that is used to get price
		cookies.put("selectedStore1", Constants.LOWES_STORE);
	}

	/*
	 * sample category page
		<li class="main-level">
				<a class="nav-heading" name="MASTHEAD_Departments"><span>Departments</span> <span class="ui-icon ui-icon-carat-1-s"></span></a>
				<div id="nav-departments" class="sub-level">
					<ul class="categories">
						<li class="first category">
							<a class="category-name" name="MASTHEAD_Depts_Appliances" rel="/wcsstore/B2BDirectStorefrontAssetStore/MContent/Structured/MastheadArea/departments/appliances.html" href="/Appliances/_/N-1z13elr/pc">Appliances</a>
							<div class="category-panel"><!-- Content Area: Appliances --></div>
						</li>
						<li class="category"><a class="category-name" name="MASTHEAD_Depts_Bathroom" rel="/wcsstore/B2BDirectStorefrontAssetStore/MContent/Structured/MastheadArea/departments/bath.html" href="/Bathroom/_/N-1z0z4ih/pl">Bathroom</a>
							<div class="category-panel"><!-- Content Area: Bathroom --></div>
						</li>												
		...
		</li>
		-->below block is not required so wil be skipped by looking at id="nav-departments"
		<li class="main-level">
				<a class="nav-heading" name="MASTHEAD_Inspiration"><span>Inspiration</span> <span class="ui-icon ui-icon-carat-1-s"></span></a>
				<div id="nav-inspiration" class="sub-level">
					<ul class="categories">
						<li class="first category active"><a class="category-name" name="MASTHEAD_Inspiration_kitchen_gallery" href="/MyLowes/app/homeideas/browse?category=KITCHEN" rel="/wcsstore/B2BDirectStorefrontAssetStore/MContent/Structured/MastheadArea/inspiration/kitchen.html">Kitchen</a>
							<div class="category-panel"></div>
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try{
			String url, name;
			Document doc = getDocument(getRetailerLink());
			//iterate over category
			Elements mainLevels = doc.getElementsByClass("main-level");
			Element mainLevelWithCategory=null;
			//find the one with categories that we are interested in
			for(Element mainLevel:mainLevels){
				if(mainLevel.getElementsByAttributeValue("id", "nav-departments").size()!=0){
					mainLevelWithCategory = mainLevel;
					break;
				}
			}
			if(mainLevelWithCategory==null){
				UtilityLogger.logError("Can't find any catgories, missing main-level with nav-deparments id");
				return rootCats;
			}
			Elements categories = mainLevelWithCategory.getElementsByClass("category");
			for(Element category : categories){
				Elements catLinkElms = category.getElementsByAttribute("href");
				if(catLinkElms == null || catLinkElms.size() == 0){
					UtilityLogger.logWarning("No links found for " + category.html());
					continue;
				}else{
					name = catLinkElms.first().text().trim();
					url = catLinkElms.first().absUrl("href");
					UtilityLogger.logInfo("L1:" + name + "|\t" + url );
					Category categoryObject = new CategoryBuilder(getRetailerId(), null, name, url).build();
					rootCats.add(categoryObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		} 
		return rootCats;
	}
	/*
	 * <div class="cat_nav">		
				<h1>Bathroom</h1>
					<ul>					
					<li><a href="/Bathroom/Bathroom-Accessories-Decor/_/N-1z0z4ec/pl?Ns=p_product_qty_sales_dollar|1" title="Bathroom Accessories & Decor">Bathroom Accessories & Decor&nbsp;<span>(684)</span></a></li>					
					<li><a href="/Bathroom/Bathroom-Cabinets/_/N-1z0z4eq/pl?Ns=p_product_qty_sales_dollar|1" title="Bathroom Cabinets">Bathroom Cabinets&nbsp;<span>(3561)</span></a></li>
					(non-Javadoc)
	 * @see uploader.util.NeoCategoryUploader#getSubCategories(int, entities.Category, org.jsoup.nodes.Document)
	 */
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element categoryBlock = parentCatDoc.getElementsByClass("cat_nav").first();
		if(categoryBlock==null){
			UtilityLogger.logError("Empty Category Page marked as Parent " + parentCat.getUrl());
			return categories;
		}
		Elements childCategories = categoryBlock.getElementsByAttribute("href");
		for(Element childCategory : childCategories){
			String name = childCategory.attr("title").trim();
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
		LowesCategoryUploader uploader = null;
		try {
			uploader = new LowesCategoryUploader();
			uploader.walkAndStore();
			boolean debug = false;
			if(debug){
				uploader.getRootCategories();
				String url = "http://www.lowes.com/Appliances/_/N-1z13elr/pc";
				Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
				uploader.getSubCategories(1,null, doc);
				uploader.setDebuCategory("TV &amp; Video");				
			}
			Utils.printUrlGetTimings();
			uploader.terminate();
		} catch (Exception e) {
			UtilityLogger.logError(e);
		}
	
	}
}

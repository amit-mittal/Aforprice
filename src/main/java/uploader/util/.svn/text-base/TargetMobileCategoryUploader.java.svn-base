package uploader.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.URL;
import util.Utils;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public class TargetMobileCategoryUploader extends NeoCategoryUploader {
	
	private static final Logger LOGGER = Logger.getLogger(TargetMobileCategoryUploader.class);
	
	public TargetMobileCategoryUploader() throws UploaderException {
		super(Retailer.TARGET_MOBILE);
	}
		
	/**
	 * @return
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Document doc = getDocument(getRetailerLink());
			rootCats = getCategories(doc, null);	
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		}
		if(rootCats == null || rootCats.size() == 0)
			throw new Bhagte2BandBajGaya("No root categories found for " + getRetailerLink());
		return rootCats;
	}
	
	private List<Category> getCategories(Document doc, Category parentCat){
		List<Category> cats = new ArrayList<Category>();
		if(doc == null)
			return cats;
		Element rootElm = doc.getElementsByClass("product-categories").first();
		if(rootElm == null){
			LOGGER.warn("product-categories is absent");
			return cats;
		}
		Element categoriesElm = rootElm.getElementsByTag("ul").first();
		if(categoriesElm == null){
			LOGGER.warn("Element ul is absent");
			return cats;
		}
		Elements categories = categoriesElm.getElementsByTag("li");		
		for(Element categoryGroup: categories) {
			rootElm = categoryGroup.getElementsByTag("a").first();			
			String name = rootElm.ownText();			
			if(skipCategory(name))
				continue;
			String url = rootElm.absUrl("href");					
			if(name.equalsIgnoreCase("more") || name.equalsIgnoreCase("see more")) {
				LOGGER.info("adding more categories for " + parentCat);
				doc = Utils.connect(url, new HashMap<String, String>(), getRetailerId());
				cats.addAll(getCategories(doc, parentCat));
				continue;
			}
			Category prodCat = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			cats.add(prodCat);
		}
		return cats;
	}
	
	private boolean skipCategory(String name){
		name = name == null?"":name.trim().toLowerCase();
		//skip all clearance categories as the products in these categories will be available in other categorieds
		if(name.startsWith("clearance") ||				
				name.indexOf("ways to shop") != -1){
			LOGGER.warn("Skipping category " + name);
			return true;
		}
		return false;
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		return getCategories(parentCatDoc, parentCat);
	}
	
	@Override
	protected CategoryType getCategoryType(String url, Document categoryDoc, String name){	
		try{	
			if(isTerminal(url))
				return CategoryType.TERMINAL;
			List<Category> cats = getCategories(categoryDoc, null);
			//If there are more than one categories, then this is not a terminal category
			if(cats.size() > 1)
				return CategoryType.PARENT;
			else if(cats.size() == 1){
				//If there is only one category, then use that category to determine the category type. Don't change the name though.
				//E.g., if there is a category jewellery-->see all jewellery, then use the name jewellery, but use the url of see all jewellery.
				Category cat = cats.get(0);
				return getCategoryType(cat.getUrl(), getDocument(cat.getUrl()), name);
			}
			else
				return super.getCategoryType(url, categoryDoc, name);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return CategoryType.UNKNOWN;
		}		
	}
	
	private boolean isTerminal(String url){
		String type = URL.getParam(url, "type");
		if(type != null && type.equalsIgnoreCase("products"))
			return true;
		return false;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TargetMobileCategoryUploader uploader = null;
		try {
			uploader = new TargetMobileCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}

}

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

public class NordstromCategoryUploader extends NeoCategoryUploader{

	public NordstromCategoryUploader() throws UploaderException {
		super(Retailer.NORDSTROM);
	}
	
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Element helper;
			//get the document from the given link
			Document doc = getDocument(getRetailerLink());
			Elements categoryEles = doc.getElementsByClass("tab");
			for(Element categoryEle: categoryEles) {
				String name="", url="";
				helper = categoryEle.getElementsByTag("a").first();
				if(helper != null) {
					url = helper.absUrl("href");
					helper = helper.getElementsByTag("div").first();
					if(helper != null)
						name = helper.ownText();
				}
				
				UtilityLogger.logInfo("L0:" +name);
				Category categoryObject = new CategoryBuilder(getRetailerId(), null, name, url).build();
				rootCats.add(categoryObject);
			}
		} catch(Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		}
		return rootCats;
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element helper = parentCatDoc.getElementById("leftnav");
		if(helper != null) {
			Elements categoryEles = helper.getElementsByTag("li");
			for(Element categoryEle: categoryEles) {
				if(categoryEle.hasClass("header") || 
						categoryEle.hasClass("sidenav-style-watch") ||
						categoryEle.hasClass("hrule") ||
						categoryEle.hasClass("flyout-link-hovered")) {
					continue;
				}
				if(categoryEle.parents().hasClass("flyout-link-hovered") ||
						categoryEle.parents().hasClass("sidenav-style-watch")) {
					continue;
				}
				
				helper = categoryEle.getElementsByTag("a").first();
				String name = helper.ownText();
				String url = helper.absUrl("href");
				
				Category subCategoryObject = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
				UtilityLogger.logInfo(getLevelInfoString(level) + "\t" + name + "|" + url );			
				categories.add(subCategoryObject);
			}
		}
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NordstromCategoryUploader uploader = null;
		try {
			uploader = new NordstromCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}

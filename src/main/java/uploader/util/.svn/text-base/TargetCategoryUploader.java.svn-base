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

public class TargetCategoryUploader extends NeoCategoryUploader {

	public TargetCategoryUploader() throws UploaderException {
		super(Retailer.TARGET);
	}
	
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Document doc = getDocument(getRetailerLink());
			Element helper = doc.getElementsByClass("ul-wrapper").first();
			Elements categoryGrouping = helper.getElementsByTag("ul");
			
			for(Element categoryGroup: categoryGrouping) {
				if(categoryGroup.hasClass("innerCol"))
					continue;
				
				Elements categoryElements = categoryGroup.getElementsByTag("li");
				for(Element categoryElement: categoryElements) {
					if(categoryElement.getElementsByTag("ul").first() == null)
						continue;
					helper = categoryElement.getElementsByTag("a").first();
					
					String url = helper.absUrl("href");
					String name = helper.ownText();
					
					UtilityLogger.logInfo("L1:" + name + " | " + url);
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
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element categoryElement = parentCatDoc.getElementById("categoryList");
		Elements list = categoryElement.getElementsByTag("li");
		for(Element category: list) {
			categoryElement = category.getElementsByTag("a").first();
			
			String url = categoryElement.absUrl("href");
			String name = categoryElement.ownText();
			
			Category subCategoryObject = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			UtilityLogger.logInfo(getLevelInfoString(level) + "\t" + name + "|" + url );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TargetCategoryUploader uploader = null;
		try {
			uploader = new TargetCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}
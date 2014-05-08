/******************************************
** StaplesCategoryUploader.java Created @Aug 12, 2012 1:10:47 AM
** @author: Jayanta Hazra
**
******************************************/
package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class StaplesCategoryUploader extends NeoCategoryUploader {

	private static Logger LOGGER = Logger.getLogger(StaplesCategoryUploader.class);
	
	public StaplesCategoryUploader() throws UploaderException {
		super(Retailer.STAPLES);
	}
	
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Document doc = getDocument(getRetailerLink());
			Elements categoryGrouping = doc.getElementsByClass("c01");
			
			for(Element categoryGroup: categoryGrouping) {
				if( categoryGroup.getElementsByTag("h2").size() == 0 || categoryGroup.ownText().equals("Additional Important Links")){
					//Additional Importan Links has More Categories which has Copy Paper and Mulipurpose Paper as a category, but that category
					//is already covered under PAPER
					continue;
				}
				Element categoryElement = categoryGroup.getElementsByTag("a").first();
				String url = categoryElement.absUrl("href");
				String name = categoryElement.ownText();
				
				LOGGER.info("L1:" + name + " | " + url);
				util.build.CategoryBuilder b = new util.build.CategoryBuilder();
				b.isActive = true;
				b.name = name;
				b.retailerId = getRetailerId();
				b.url = url;
				Category categoryObject = b.build();
				rootCats.add(categoryObject);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		}
		return rootCats;
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		List<Category> categories = new ArrayList<Category>();
		Element categoryElement = parentCatDoc.getElementsByClass("sdbr").first();
		if( categoryElement == null)
			return categories;
		Elements list = categoryElement.getElementsByTag("li");
		for(Element category: list) {
			categoryElement = category.getElementsByTag("a").first();			
			String url = categoryElement.absUrl("href");
			String name = categoryElement.text();
			util.build.CategoryBuilder b = new util.build.CategoryBuilder();
			b.retailerId = getRetailerId();
			b.parentName = parentCat.getName();
			b.parentCategoryId = parentCat.getCategoryId();
			b.name = name;
			b.url = url;
			Category subCategoryObject = b.build();
			LOGGER.info(getLevelInfoString(level) + "\t" + name + "|" + url );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws UploaderException{
		StaplesCategoryUploader uploader = new StaplesCategoryUploader();
		uploader.walkAndStore();
		uploader.terminate();
	}
}
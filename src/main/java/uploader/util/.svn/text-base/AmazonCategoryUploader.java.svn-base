package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;

public class AmazonCategoryUploader extends NeoCategoryUploader {
	
	public AmazonCategoryUploader() throws UploaderException {
		super(Retailer.AWSAMAZON);
	}
	
	protected boolean refineCategory(String categoryName) {
		String reference[] = {"Unlimited Instant Videos", "MP3s &amp; Cloud Player", "Amazon Cloud Drive", "Kindle", "Appstore for Android", "Digital Games &amp; Software", "Audible Audiobooks", "Books", "Movies, Music &amp; Games"};
		for(String refer: reference) {
			if(categoryName.equalsIgnoreCase(refer))
				return false;
		}
		return true;
	}
	
	/* 
	 * sample category page
	 * <div class="popover-grouping">
	 * 		<div class="popover-category-name">
	 * 			<h2 style="margin: 0px; font-size: 13px;">Category Heading/Grouping</h2>
	 * 		</div>
	 * 		<div>
	 * 			<a href="url">Category name-level 1</a>
	 * 		</div>
	 * 		<div>
	 * 			<a href="url">Category name-level 1</a>
	 * 		</div>
	 * 		<div>
	 * 			<a href="url">Category name-level 1</a>
	 * 		</div>
	 * </div>
	 */
	
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Element helper;
			//get the document from the given link
			Document doc = getDocument(getRetailerLink());
			Elements categoryGrouping = doc.getElementsByClass("popover-grouping");
			
			/*for(Element categoryGroup: categoryGrouping) {
				Elements categories = categoryGroup.getElementsByTag("a");
				
				for(Element category: categories) {
					String parentCategoryName = category.html();
					System.out.println("parentCategoryName: "+ parentCategoryName);
				}
			}
			System.exit(0); */
			for(Element categoryGroup: categoryGrouping) {
				Elements categories = categoryGroup.getElementsByTag("a");
				Element parentCategory = categoryGroup.getElementsByTag("h2").first();
				String parentCategoryName = parentCategory.html();
				
				if(!refineCategory(parentCategoryName))
					continue;
				
				UtilityLogger.logInfo("L0:" + parentCategoryName);
				Category parentCategoryObject = new CategoryBuilder(getRetailerId(), null, parentCategoryName, "").build();
				storeCategory(parentCategoryObject);//required to get category_id from db before getting child categories which need this id
				
				for(Element category: categories) {
					String categoryName, url;
					
					helper = category.getAllElements().first();
					categoryName = helper.html();
					url = helper.absUrl("href");
					
					Category categoryObject = new CategoryBuilder(getRetailerId(), parentCategoryObject, categoryName, url).build();
					UtilityLogger.logInfo("L1:\t" + categoryName + " | " + url );
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
		Elements categoryElements = parentCatDoc.getElementsByClass("unified_widget");
		Element categoryElement = null;
		for(Element category: categoryElements) {
			if(category.hasClass("pageBanner")) {
				categoryElement = category;
				break;
			}
		}
		categoryElements = categoryElement.getElementsByTag("a");
		for(Element childCategory: categoryElements) {
			String name = childCategory.html();
			String url = childCategory.absUrl("href");
			
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
		AmazonCategoryUploader uploader = null;
		try {
			uploader = new AmazonCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}
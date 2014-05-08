package uploader.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import parsers.details.ProductDetailsParser;
import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class KohlsCategoryUploader extends NeoCategoryUploader {
	private static Logger logger = Logger.getLogger(KohlsCategoryUploader.class);
	private HashSet<String> urlAddedToCategoryList = new HashSet<String>(100);

	public KohlsCategoryUploader() throws UploaderException {
		super(Retailer.KOHLS);
	}

	@Override
	public List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {

			Document doc = getDocument(retailerLink);
			// each entry below represents one item in the drop down
			Elements navList = doc.getElementsByClass("navigation-item-link");
			for (Element navItemList : navList) {
				System.out.println(navItemList);
				String name = navItemList.childNodes().get(0).toString();
				String url = navItemList.absUrl("href");
				Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
				logger.info(name + "|" + url);
				rootCats.add(prodCat);
			}
		} catch (Exception e) {
			UtilityLogger
					.logError("Ooops.. put some code to catch exception so we don't come here");
			e.printStackTrace();
		}
		return rootCats;
	}

	@Override
	protected List<Category> getSubCategories(int level, Category parentCat,
			Document parentDoc) {
		// Skip 'Best Sellers', 'Top Rated', 'Top Brands'

		List<Category> categories = new ArrayList<Category>();
/*		Elements catElements = parentDoc.getElementsByClass("left-nav");
		if (catElements.size() == 0) {// no urls found for the subcategories
			logErrCategories(parentCat.getUrl(),
					"No urls found for the subcategories", null);
			return categories;
		}*/
		Elements catElements = parentDoc.getElementsByClass("header-group");
		if (catElements == null) {// no urls found for the subcategories
			logErrCategories(parentCat.getUrl(),
					"No urls found for the subcategories", null);
			return categories;
		}

		for (Element catElement : catElements) {	
			// The first link is either heading (i.e. Dorm Essentials)
			Element firstChild = catElement.child(0);// this return h3 block
			// logger.info(firstChild);
			String catType = firstChild.text();
			if (catType.contains("Best Sellers")
					|| catType.contains("Top Rated")
					|| catType.contains("Top Brands")
					|| catType.contains("Online Exclusive")
					|| catType.contains("Featured Categories")
					|| catType.contains("You May Also Like")
					|| catType.contains("Featured Brands")
					|| catType.contains("CLEARANCE")
					)// online exclusive
															// items are already
															// under regular
															// category
				continue;
			Node temp = firstChild.childNodes().get(0);
			Category runningParentCategory = null; // each catElement has two
													// blocks - first block
													// (Dorm Essentials) is a
													// parent of second block
													// (Towels, Bath Rugs)
			if (temp instanceof Element
					&& !temp.attr("href").contains("javascript")
					&& temp.absUrl("href").length() != 0) {
				Element firstEntry = (Element) firstChild.childNodes().get(0);
				String name = firstEntry.text().trim();
				String url = firstEntry.absUrl("href");
				if (urlAddedToCategoryList.contains(url))
					continue;
				urlAddedToCategoryList.add(url);
				logger.info(name + "|" + url);
				runningParentCategory = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
				categories.add(runningParentCategory);
			}
			if (catElement.childNodes().size() <= 3) {
				// logger.info("No subcategory under category type "+catType);
				// //normal, we will get this subcategories on its own category
				// page retrieved from url got from above first child
				continue;
			}
			Element secondChild = catElement.child(1);
			Elements elements = secondChild.getElementsByAttribute("href");
			for (Element elem : elements) {
				String name = elem.text().trim();
				if (name.contains("more") || name.contains("Online Exclusive"))// online
																				// exclusive
																				// items
																				// are
																				// already
																				// under
																				// regular
																				// category)
					continue;
				if (elem.attr("href").contains("javascript:launch"))
					continue;
				String url = elem.absUrl("href");
				if (urlAddedToCategoryList.contains(url))
					continue;
				urlAddedToCategoryList.add(url);
				logger.info("\t" + name + "|" + url);
				if (runningParentCategory != null)
					categories.add(new CategoryBuilder(getRetailerId(), runningParentCategory, name, url).build());
				else
					categories.add(new CategoryBuilder(getRetailerId(), parentCat, name, url).build());
			}
		}// for(Element catElement : c ends...
		return categories;
	}

	public static void main(String[] args) throws UploaderException {
		KohlsCategoryUploader up = new KohlsCategoryUploader();
		// Document doc =
		// Utils.connect("http://www.kohls.com/upgrade/gift_registry/kohlsgrw_home.jsp",
		// up.cookies, up.retailerId);
		// up.getSubCategories(0, null, doc);
		// up.setDebuCategory("Party Supplies");
		up.walkAndStore();
		up.terminate();
	}
}

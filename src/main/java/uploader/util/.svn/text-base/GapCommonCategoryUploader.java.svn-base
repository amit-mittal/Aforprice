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

public class GapCommonCategoryUploader extends NeoCategoryUploader{
	
	public GapCommonCategoryUploader(Retailer retailer) throws UploaderException{
		super(retailer);
	}
	
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Element helper;
			//get the document from the given link
			Document doc = getDocument(getRetailerLink());
			Elements categoryGrouping = doc.getElementsByClass("idxH2Link");
			
			for(Element categoryEle: categoryGrouping) {
				helper = categoryEle.getElementsByTag("a").first();
				String url = helper.absUrl("href");
				String categoryName = helper.ownText();
				
				UtilityLogger.logInfo("L0:" + categoryName);
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
		Elements individualCategoryBlocks = parentCatDoc.getElementsByClass("categoryBlock");
        List<Category> categories = new ArrayList<>();
		for(Element categoryBlock : individualCategoryBlocks) {
			String name = categoryBlock.getElementsByClass("header4").first().getElementsByTag("a").first().ownText();
			name = name.replaceAll(":", "-");
			String url = categoryBlock.getElementsByClass("header4").first().getElementsByTag("a").first().absUrl("href");
			Category subCategoryObject = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			UtilityLogger.logInfo(getLevelInfoString(level) + "\t" + name + "|" + url );			
			categories.add(subCategoryObject);
		}
		return categories;
	}
}
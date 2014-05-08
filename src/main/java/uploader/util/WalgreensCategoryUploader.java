package uploader.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Utils;

import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;

public class WalgreensCategoryUploader extends NeoCategoryUploader{
	private static final Logger LOGGER= Logger.getLogger(WalgreensCategoryUploader.class);
	
	public WalgreensCategoryUploader() throws UploaderException {
		super(Retailer.WALGREENS);
	}

	/**
	 * @return
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Document doc = getDocument(getRetailerLink());
			return getCategories(doc, null);		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		}
		return rootCats;
	}
	
	private List<Category> getCategories(Document doc, Category parentCat){
		List<Category> cats = new ArrayList<Category>();
		if(doc == null)
			return cats;
		Element rootElm = doc.getElementsByClass("link-arrow-list").first();
		if(rootElm == null){
			LOGGER.warn("link-arrow-list is absent in " + doc);
			return cats;
		}		
		Elements categories = rootElm.getElementsByTag("li");		
		for(Element categoryGroup: categories) {
			rootElm = categoryGroup.getElementsByTag("a").first();			
			String name = rootElm.ownText();			
			if(skipCategory(name))
				continue;
			String url = rootElm.absUrl("href");					
			if(parentCat == null && name.startsWith("see more") ) {
				doc = Utils.connect(url, new HashMap<String, String>(), getRetailerId());
				cats.addAll(getCategories(doc, parentCat));
			}
			Category prodCat = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			cats.add(prodCat);
		}
		return cats;
	}
	
	private boolean skipCategory(String name){
		name = name == null?"":name.trim().toLowerCase();
		//skip all clearance categories as the products in these categories will be available in other categorieds
		if(name.startsWith("save") ||				
				name.startsWith("sale on")){
			LOGGER.warn("Skipping category " + name);
			return true;
		}
		return false;
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		return getCategories(parentCatDoc, parentCat);
	}
	
	public static void main(String[] args) throws UploaderException{
		NeoCategoryUploader u = new WalgreensCategoryUploader();
		u.walkAndStore();
		u.terminate();
//		List<Category> li = u.getRootCategories();
//		System.out.println(li);
	}

}

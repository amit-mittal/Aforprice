package uploader.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;

public class RiteaidCategoryUploader extends NeoCategoryUploader{

	private static final Logger LOGGER = Logger.getLogger(RiteaidCategoryUploader.class);
	//We need to keep track of root categories as all the root categories have all the products and have subcategories too.
	//The products in the root categories are not displayed though. If a category exists in this cache, it will be deemed as
	//parent without having to parse the product page.
	private Set<String> rootCatCache = new HashSet<String>();
	
	public RiteaidCategoryUploader() throws UploaderException{
		super(Retailer.RITEAID);
	}

	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {			
			Document doc = getDocument(retailerLink);
			
			Elements catElements = doc.getElementsByClass("navigationGroup");			
			for(Element catElement : catElements){
				//The first link is of the parent category.
				Elements catLinkElms = catElement.getElementsByAttribute("href");
				if(catLinkElms == null || catLinkElms.size() == 0){
					LOGGER.warn("No links found for " + catElement.html());
					continue;
				}
				else{
					Element catElm = catLinkElms.first();
					if(catElm == null){
						LOGGER.warn("No category element in " + catLinkElms.html());
						continue;
					}
					String name = catElm.text() != null? catElm.text().trim(): "";
					rootCatCache.add(name);
					String url = catElm.absUrl("href");
					if(url == null){
						LOGGER.error("No url found for category element " + catElm.html());
						continue;
					}					
					Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
					rootCats.add(prodCat);					
				}
			}
				
		} catch (Exception e) {
			LOGGER.error(e);			
		} 
		return rootCats;
	}
	
		
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentDoc){
		List<Category> categories = new ArrayList<Category>();
				
		Elements allCatElms = parentDoc.getElementsByClass("browseLadder");
		if(allCatElms == null || allCatElms.size() < 2)
			return categories;
		Element subCatContainer = allCatElms.get(1);
		Elements subCatElms = subCatContainer.getElementsByTag("a");
		for(Element elm: subCatElms){
			String url = elm.absUrl("href");
			String name = elm.text();
			if(name.equalsIgnoreCase("New Products")){
				LOGGER.info("new products creates issues with category tree, let us skip this");
				continue;
			}
			if(url == null){
				LOGGER.warn("url is null for category element: " + elm);
				continue;
			}				
			categories.add(new CategoryBuilder(getRetailerId(), parentCat, name, url).build());					
		}		
		return categories;
	}
	
	@Override
	protected CategoryType getCategoryType(String url, Document categoryDoc, String name){
		if(rootCatCache.contains(name))
			return CategoryType.PARENT;
		return super.getCategoryType(url, categoryDoc, name);		
	}

	public static void main(String[] args) throws UploaderException{
		RiteaidCategoryUploader up = new RiteaidCategoryUploader();
		up.walkAndStore();
		up.terminate();
	}
}

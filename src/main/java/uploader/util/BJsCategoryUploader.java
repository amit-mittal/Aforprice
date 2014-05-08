package uploader.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class BJsCategoryUploader extends NeoCategoryUploader{
	private static Logger logger = Logger.getLogger(BJsCategoryUploader.class);
	private HashSet<String> urlAddedToCategoryList = new HashSet<String>(100);
	public BJsCategoryUploader() throws UploaderException{
		super(Retailer.BJS);
	}

	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {
			
			Document doc = getDocument(retailerLink);
			Element menuCell = doc.getElementsByClass("newNavigationBar").first();
			
			for(Element navRowElement : menuCell.children()){
				Element firstChild = navRowElement.child(0);				
				String name = firstChild.text();				
				if(name.contains("View All") || name.contains("What's New"))
					continue;
				String url = firstChild.absUrl("href");
				Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
				rootCats.add(prodCat);					
			}
		} catch (Exception e) {
			UtilityLogger.logError("Ooops.. put some code to catch exception so we don't come here");
			e.printStackTrace();
		} 
		return rootCats;
	}
	
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentDoc){
		List<Category> categories = new ArrayList<Category>();
		Elements temp = parentDoc.getElementsByClass("leftmenu");
		if(temp.size()==0){
			logger.warn("No subcategory found on this page");
			return categories;
		}
			
		Element leftNavElement= temp.first();
		Elements catElements = leftNavElement.getElementsByAttribute("href");
		for(Element catElement : catElements){
			String name = catElement.text();
			int index = name.indexOf("(");//Restaurant (11) -> Restaurant
			if(index!=-1){
				//remove special character
				if(name.charAt(index-1)== 160)
					index--;
				name = name.substring(0, index).trim();
			}
			
			String url = catElement.absUrl("href");
			if(urlAddedToCategoryList.contains(url))
				continue;
			urlAddedToCategoryList.add(url);
			Category prodCat = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
			categories.add(prodCat);					

		}
		return categories;
	}

	public static void main(String[] args) throws UploaderException{
		BJsCategoryUploader up = new BJsCategoryUploader();
//		Document doc = Utils.connect("http://www.costco.com/upgrade/gift_registry/kohlsgrw_home.jsp", up.cookies, up.retailerId);
//		up.getSubCategories(0, null, doc);
//		up.setDebuCategory("Party Supplies");
		up.walkAndStore();
		up.terminate();
	}
}

package uploader.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


import util.UtilityLogger;
import util.Utils;
import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;

public class CostcoCategoryUploader extends NeoCategoryUploader{
	private static Logger logger = Logger.getLogger(CostcoCategoryUploader.class);
	private HashSet<String> urlAddedToCategoryList = new HashSet<String>(100);
	public CostcoCategoryUploader() throws UploaderException{
		super(Retailer.COSTCO);
	}

	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {
			
			Document doc = getDocument(retailerLink);
			Element menuCell = doc.getElementsByAttributeValue("id", "category-navigation-categories").first();
			Elements catElements = menuCell.getElementsByAttribute("href");
			for(Element catElement : catElements){
				String name = catElement.text();				
				if(name.contains("View All") || name.contains("What's New"))
					continue;
				String url = catElement.absUrl("href");
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
		Elements temp = parentDoc.getElementsByAttributeValue("id","left_nav");
		if(temp.size()==0){
			logger.warn("No subcategory found on this page");
			return categories;
		}
			
		Element leftNavElement= temp.first();
		boolean start=false;
		for(Node childNode:leftNavElement.childNodes()){
			if(!(childNode instanceof Element))
				continue;
			Element childElement = (Element) childNode;
			//start after seeing "Categories" title
			if(!start && childElement.text().toUpperCase().equals("SHOP BY CATEGORY")){
				start = true;
				continue;
			}
			//get subcategories
			Elements catElements = childElement.getElementsByAttribute("href");
			for(Element catElement : catElements){
				String name = catElement.text();
				int index = name.indexOf("(");//Restaurant (11) -> Restaurant
				if(index!=-1)
					name = name.substring(0, index).trim();
				String url = catElement.absUrl("href");
				if(urlAddedToCategoryList.contains(url))
					continue;
				urlAddedToCategoryList.add(url);
				Category prodCat = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
				categories.add(prodCat);					

			}
			//stop once you see "Brand"
			if(childElement.text().toUpperCase().equals("SHOP BY BRAND") || childElement.text().toUpperCase().equals("SHOP BY PRICE"))
				break;
			
		}
		return categories;
	}

	public static void main(String[] args) throws UploaderException{
		CostcoCategoryUploader up = new CostcoCategoryUploader();
//		Document doc = Utils.connect("http://www.costco.com/upgrade/gift_registry/kohlsgrw_home.jsp", up.cookies, up.retailerId);
//		up.getSubCategories(0, null, doc);
//		up.setDebuCategory("Party Supplies");
		up.walkAndStore();
		up.terminate();
	}
}

package uploader.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class SamsClubCategoryUploader extends NeoCategoryUploader{
	private static Logger logger = Logger.getLogger(SamsClubCategoryUploader.class);
	private HashSet<String> urlAddedToCategoryList = new HashSet<String>(100);
	public SamsClubCategoryUploader() throws UploaderException{
		super(Retailer.SAMSCLUB);
		cookies.put("myNeighboringClubs", "4730|6686|4718|6682");
		cookies.put("myPreferredClub", "6683");
	}

	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {
			
			Document doc = getDocument(retailerLink);
			
			Elements rooCatElements = doc.getElementsByClass("bottomBorderInfoList");
			if(rooCatElements == null){
				logger.error("Empty root categories");
				return rootCats;
			}
			for(Element rootCatElement: rooCatElements){
				Element urlElement = rootCatElement.getElementsByTag("a").first();
				if(urlElement == null)
					continue;
				String name = urlElement.text();
				String url = urlElement.absUrl("href");
				util.build.CategoryBuilder b = new util.build.CategoryBuilder();
				b.isActive = true;
				b.name = name;
				b.retailerId = getRetailerId();
				b.url = url;
				rootCats.add(b.build());
				logger.info("L1:" + name + "|\t" + url );
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} 
		return rootCats;
	}
	
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentDoc){
		List<Category> categories = new ArrayList<Category>();
		//id=pageFilterNavigation
		//get first child and confirm it contains text 'Category' else report error
		//get all href under first child
		
		Elements temp = parentDoc.getElementsByAttributeValue("id", "pageFilterNavigation" );
		
		if(temp.size()==0){
			logger.warn("No subcategory found on this page");
			return categories;
		}
		Element pageFilterNavigationFirstElement = temp.first();
		if(pageFilterNavigationFirstElement.children().size()==0){
			logger.warn("No child elements found under pageFilterNavigation id");
			return categories;			
		}
		Element elementCategory;
		try{	
		elementCategory= pageFilterNavigationFirstElement.child(0);
		}catch(IndexOutOfBoundsException e){
			int debug=0;
			return categories;
		}
		if(!elementCategory.text().contains("Category")){
			logger.warn("Can't find 'Category' text in url "+parentCat.getUrl());
			return categories;
		}
		Elements subCatElements = elementCategory.getElementsByAttribute("href");	
		for(Element subCatElement:subCatElements){
			String name = subCatElement.text();
			int index = name.indexOf("(");//Restaurant (11) -> Restaurant
			if(index!=-1)
				name = name.substring(0, index).trim();
			String url = subCatElement.absUrl("href");
			if(urlAddedToCategoryList.contains(url))
				continue;
			urlAddedToCategoryList.add(url);
			Category prodCat = new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
//			logger.info(name+"|"+url);
			categories.add(prodCat);					
		}
		return categories;
	}

	public static void main(String[] args) throws UploaderException{
		SamsClubCategoryUploader up = new SamsClubCategoryUploader();
		up.walkAndStore();
		up.terminate();
	}
}
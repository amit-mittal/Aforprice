package uploader.util;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.ProductsParser;
import parsers.ProductsParserFactory;

import entities.Category;
import entities.ProductSummary;
import entities.Retailer;
import entities.Category.CategoryBuilder;
import global.exceptions.Bhagte2BandBajGaya;


public class AmazonBestSellerCategoryUploader extends NeoCategoryUploader{
	private static final Logger LOGGER = Logger.getLogger(AmazonBestSellerCategoryUploader.class);
	public AmazonBestSellerCategoryUploader() throws UploaderException{
		super(Retailer.AMAZONBESTSELLER);
	}

	@Override
	protected List<Category> getRootCategories() {
		Document doc = getDocument(getRetailerLink());
		return getCategories(1, null, doc); 
	}
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc) {
		return getCategories(level, parentCat, parentCatDoc);
	}
	
	private List<Category> getCategories(int level, Category parentCat, Document doc){
		List<Category> rootCats = new ArrayList<Category>();
		try{
			String url, categoryName;
  			Element root = doc.getElementById("zg_browseRoot").getElementsByTag("ul").first();
			if(root == null)
				throw new Bhagte2BandBajGaya("No root element found");
			
			Elements categories = root.getElementsByTag("li");
			for(Element category : categories){
				//Movies & TV
				Element nameUrlElm = category.getElementsByTag("a").first(); 
				if(nameUrlElm == null)
					continue;
				if("zg_browseUp".equals(category.className()))
					continue;
				categoryName = nameUrlElm.text();
				url = nameUrlElm.absUrl("href");
				
				LOGGER.info(getLevelInfoString(level) + categoryName + "|\t" + url );
				Category categoryObject = new CategoryBuilder(getRetailerId(), parentCat, categoryName, url).build();
				rootCats.add(categoryObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
			throw new Bhagte2BandBajGaya(e.getMessage());
		} 
		return rootCats;
		
	}
	
	@Override
	protected CategoryType getCategoryType(String url, Document categoryDoc, String name){
		try{
  			Element root = categoryDoc.getElementById("zg_browseRoot").getElementsByTag("ul").first();
			if(root == null)
				return CategoryType.UNKNOWN;
			
			String tag = root.getElementsByClass("zg_selected").first().parent().parent().children().last().tag().toString();
			
			System.out.println( "tag  " + tag + " " + name);
			if( tag != null && tag.equals("ul")	)
			{
				return CategoryType.PARENT;
			}
			else
				return CategoryType.TERMINAL;
		} catch (Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error finding out catery type, category name is " + name, e);
			LOGGER.info("Can not identify category type for name:" + name + " , returning TERMINAL");
			return CategoryType.TERMINAL;
		}   	    
  	    
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AmazonBestSellerCategoryUploader uploader = null;
		try {
			uploader = new AmazonBestSellerCategoryUploader();
	//		uploader.setDebuCategory("Sports Fan Shop");
		} catch (UploaderException e) {
			e.printStackTrace();
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}

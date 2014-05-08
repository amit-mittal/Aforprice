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

public class ToysrusCategoryUploader extends NeoCategoryUploader{

	public ToysrusCategoryUploader() throws UploaderException{
		super(Retailer.TOYSRUS);
	}
	
	public ToysrusCategoryUploader(Retailer retailer) throws UploaderException{
		super(retailer);
		if(!retailer.equals(Retailer.TOYSRUS)&&!retailer.equals(Retailer.BABYSRUS)){
			throw new IllegalArgumentException("Invalid retailer - " + retailer);
		}
	}

	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {
			
			Document doc = getDocument(retailerLink);
			
			/*On the first page, there are Categories and Sub-categories. e.g. Clearance is the category and 
			and Action Figures etc are the sub-categories. Each Category when clicked leads to the sub-categories. So, 
			from the start page, we will all the categories and sub-categories, and each of the sub-category may lead to
			more categories.			
	<div class="subCatBlockTRU">
		<h2><a href="/category/index.jsp?categoryId=3999911"> Clearance  </a></h2> 
		<ul>
			<li><a href="/family/index.jsp?categoryId=3999912">Action Figures</a></li>
			<li><a href="/family/index.jsp?categoryId=3999915">Arts &amp; Crafts</a></li>
			<li><a href="/family/index.jsp?categoryId=4013146">Baby Toys</a></li>
		</ul>
	</div>
			 */
			Elements catElements = doc.getElementsByClass(getRootCatClass());			
			for(Element catElement : catElements){
				//The first link is of the parent category.
				Elements catLinkElms = catElement.getElementsByAttribute("href");
				if(catLinkElms == null || catLinkElms.size() == 0){
					UtilityLogger.logWarning("No links found for " + catElement.html());
					continue;
				}
				else{
					String name = catLinkElms.first().text().trim();
					String url = catLinkElms.first().absUrl("href");
					Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
					rootCats.add(prodCat);					
				}
			}
				
		} catch (Exception e) {
			UtilityLogger.logError("Ooops.. put some code to catch exception so we don't come here");
			e.printStackTrace();
		} 
		return rootCats;
	}
	
	protected String getRootCatClass(){
		return "subCatBlockTRU";
	}
	
		
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentDoc){
		List<Category> categories = new ArrayList<Category>();
				
		Element catElm = parentDoc.getElementById("module_Taxonomy1");
		if(catElm != null){			
			Elements catLinkElms = catElm.getElementsByAttribute("href");			
			if(catLinkElms != null && catLinkElms.size() > 0){						
				for(Element elm: catLinkElms){
					String url = elm.absUrl("href");
					String name = elm.ownText();
					categories.add(new CategoryBuilder(getRetailerId(), parentCat, name, url).build());					
				}
			}
			else{//no urls found for the subcategories
				logErrCategories(parentCat.getUrl(), "No urls found for the subcategories", null);
			}
		}
		else{//more than one classes with the class name module
			logErrCategories(parentCat.getUrl(), "No classe with id = module_Taxonomy1", null);
		}
		return categories;
	}
	
	public static void main(String[] args) throws UploaderException{
		ToysrusCategoryUploader up = new ToysrusCategoryUploader();
//		up.setDebuCategory("Party Supplies");
		up.walkAndStore();
		up.terminate();
		
	}
}

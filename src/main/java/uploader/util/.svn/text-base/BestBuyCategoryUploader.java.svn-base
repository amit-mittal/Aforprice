package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class BestBuyCategoryUploader extends NeoCategoryUploader{
	
	public BestBuyCategoryUploader() throws UploaderException{
		super(Retailer.BESTBUY);
	}

	/*
	 * sample category page
	 * search by 'nav-pro' class
	 * second child has all categories under it
	 * 	first child of category section is its name
	 * 	second child of category section has all sub-categories under it
	<li class="nav-pro">
		<a data-lid="ubr_prd" href="#" class="nav-pro">Products</a>
		<ul> //second child of nav-pro, all categories under this
			<li>//first category
				<h4><a data-lid="ubr_tvv" href="http://www.bestbuy.com/site/Electronics/TV-Video/abcat0100000.c?id=abcat0100000">TV &amp; Home Theater</a></h4>
				<ul>//second child of category section
					<li><a data-lid="ubr_tv_tvs" href="http://www.bestbuy.com/site/TV-Video/Televisions/abcat0101000.c?id=abcat0101000">TVs</a></li>
					<li><a data-lid="ubr_tv_tvo" href="http://www.bestbuy.com/site/TV-Video/Smart-TVs-Devices/abcat0103000.c?id=abcat0103000 ">Smart TVs &amp; Devices</a></li>
				</ul>
			</li>
			<li>//second category
				<h4><a data-lid="ubr_aud" href="http://www.bestbuy.com/site/Electronics/Audio/abcat0200000.c?id=abcat0200000">Audio &amp; MP3</a></h4>
				...
			</li>
	 */
	@Override
	public List<Category> getRootCategories(){
		List<Category> rootCats = new ArrayList<Category>();
		try {
			String url, departmentName;
			Document doc = getDocument(getRetailerLink());
			Elements parentNodesOfAllCategories = doc.getElementsByClass(("nav-pro"));
			//2nd child node is the parent of all categories
			Node categoryParent = parentNodesOfAllCategories.get(0).childNode(2);
			Category deptCategoryObject=null;
			//iterate over category
			for(Node childCategory : categoryParent.childNodes()){
				if(childCategory.childNodes().size()<=1)//skips not useful nodes
					continue;
				//first element is the category name
				//Electronics & Office
				Element firstElementNode = (Element) childCategory.childNode(0);
				departmentName = firstElementNode.children().first().html();
				url = firstElementNode.children().first().attr("abs:href");
				UtilityLogger.logInfo("L1:" + departmentName + "|\t" + url ); 
				deptCategoryObject = new CategoryBuilder(getRetailerId(), null, departmentName, url).build();
				rootCats.add(deptCategoryObject);
			}//for(Node childCategory .. ends
			//description
		} catch (Exception e) {
			e.printStackTrace();
			logErrCategories(getRetailerLink(), "Error in getting root categories", e);
		} 
		return rootCats;
	}
	
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, Document parentCatDoc){
		List<Category> categories = new ArrayList<Category>();
		//This is a parent category. with the parent being categoryObject being passed
		//All pages don't have narrow class/sub category defined, skip them. example: http://apps.bestbuy.com/
		if(parentCatDoc.getElementsByClass("narrow").first() == null){
			UtilityLogger.logWarning("Skipping " + parentCat.getUrl() + " no subcategory (narrow class) defined");
			return categories;
		}
		//narrow class
		Element childCategories = parentCatDoc.getElementsByClass("narrow").first().
				getElementsByClass("narrowcontent").first().
				getElementsByClass("search").first();
		for(Node childCategory : childCategories.childNodes()){
			if(childCategory instanceof Element){
				Element childCategoryElement = (Element) childCategory;
				if(childCategoryElement.children().size()==0)//skipping non-category stuff
					continue;
				String name = childCategoryElement.children().first().html().trim();
				//Exception:Sports Fan Shop <img src="http://images.bestbuy.com:80/BestBuy_US/en_US/images/global/footer/new_bug.gif" alt="New" />
				if(name.contains("img"))
					name = childCategoryElement.children().first().childNode(0).toString();
				String url = childCategoryElement.children().first().attr("abs:href");
				Category subCategoryObject=new CategoryBuilder(getRetailerId(), parentCat, name, url).build();
				UtilityLogger.logInfo(getLevelInfoString(level) + name + "|\t" + url );			
				categories.add(subCategoryObject);
			}//if(childCategory instanceof Element) ends...
		}//for(Node childCategory : childCategories.childNodes()) ends...
		return categories;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BestBuyCategoryUploader uploader = null;
		try {
			uploader = new BestBuyCategoryUploader();
			//uploader.setDebuCategory("TV &amp; Home Theater");
		} catch (UploaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uploader.walkAndStore();
		uploader.terminate();
	}

}

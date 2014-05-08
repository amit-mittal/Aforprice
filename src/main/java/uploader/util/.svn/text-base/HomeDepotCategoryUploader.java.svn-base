package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class HomeDepotCategoryUploader extends NeoCategoryUploader {

	public HomeDepotCategoryUploader() throws UploaderException {
		super(Retailer.HOMEDEPOT);
	}

	/**
	 * One of the category on the main page www.homedepot.com. We just take all
	 * the root categories, identified by class="item" and get their url. 
	 * <li class="item"><a href="http://www.homedepot.com/h_d1/N-5yc1vZbv1w/h_d2/Navigation?catalogId=10053&amp;langId=-1&amp;storeId=10051" title="Appliances" class="L1" onclick=
	 * "javascript:findCategoryUrl('http://www.homedepot.com/h_d1/N-5yc1vZbv1w/h_d2/Navigation?catalogId=10053&amp;langId=-1&amp;storeId=10051');s_objectID=url4;return this.s_oc?this.s_oc(e):true"
	 * >Appliances</a>
	 * </li>
	 * 
	 * @return
	 */
	@Override
	protected List<Category> getRootCategories() {
		List<Category> rootCats = new ArrayList<Category>();
		try {
			Document doc = getDocument(retailerLink);
			Elements catElements = doc.getElementsByClass("item");
			for (Element catElement : catElements) {
				// The first link is of the parent category.
				Elements catLinkElms = catElement
						.getElementsByAttribute("href");
				if (catLinkElms == null || catLinkElms.size() == 0) {
					UtilityLogger.logWarning("No links found for "
							+ catElement.html());
					continue;
				} else {
					String name = catLinkElms.first().text().trim();
					String url = catLinkElms.first().absUrl("href"); 
					Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build(); 
					rootCats.add(prodCat);
				}
			}

		} catch (Exception e) {
			UtilityLogger
					.logError("Ooops.. put some code to catch exception so we don't come here");
			e.printStackTrace();
		}
		return rootCats;
	}

	/**
	 * Sub categories html is as follows. To parse, look for activeLevel and then get list of all the sub categories (<li>)
	 * <ul class="activeLevel">
			
				<li><h3 class="title">Appliances</h3></li>
				
					<dd>
			    		<li><a href="/Appliances-Appliance-Parts-Filters-Accessories/h_d1/N-5yc1vZc35v/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Appliance Parts, Filters &amp; Accessories</a>&nbsp;(905)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Cooking/h_d1/N-5yc1vZc3pd/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Cooking</a>&nbsp;(1197)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Dishwashers-Disposers/h_d1/N-5yc1vZc3pc/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Dishwashers &amp; Disposers</a>&nbsp;(196)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Heating-Cooling-Air-Quality/h_d1/N-5yc1vZbv1t/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Heating, Cooling &amp; Air Quality</a>&nbsp;(744)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Kitchen-Appliances/h_d1/N-5yc1vZbv42/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Kitchen Appliances</a>&nbsp;(2180)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Laundry-Clothing-Care/h_d1/N-5yc1vZbv6m/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Laundry &amp; Clothing Care</a>&nbsp;(216)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Refrigeration/h_d1/N-5yc1vZc3o6/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Refrigeration</a>&nbsp;(787)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Small-Appliances/h_d1/N-5yc1vZbv48/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Small Appliances</a>&nbsp;(1452)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Vacuum-Cleaners-Floor-Care/h_d1/N-5yc1vZbv1z/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Vacuum Cleaners &amp; Floor Care</a>&nbsp;(485)</li>						
					</dd>
				
					<dd>
			    		<li><a href="/Appliances-Washers-Dryers/h_d1/N-5yc1vZc3ol/h_d2/Navigation?catalogId=10053&amp;Nu=P_PARENT_ID&amp;langId=-1&amp;storeId=10051&amp;searchNav=true">Washers &amp; Dryers</a>&nbsp;(216)</li>						
					</dd>
				
			</ul>
	 * @param level
	 * @param parentCat
	 * @param parentDoc
	 * @return
	 */
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat,
			Document parentDoc) {
		List<Category> subCategories = new ArrayList<Category>();
		
		Element subCategoriesElm = parentDoc.getElementsByClass("activeLevel").first();
		if(subCategoriesElm != null){			
			Elements catLinkElms = subCategoriesElm.getElementsByAttribute("href");			
			if(catLinkElms != null && catLinkElms.size() > 0){						
				for(Element elm: catLinkElms){
					String url = elm.absUrl("href");
					String name = elm.ownText();
					subCategories.add(new CategoryBuilder(getRetailerId(), parentCat, name, url).build());					
				}
			}
			else{//no urls found for the subcategories
				logErrCategories(parentCat.getUrl(), "No urls found for the subcategories", null);
			}
		}
		else{//more than one classes with the class name module
			logErrCategories(parentCat.getUrl(), "No class with id = activeLevel", null);
		}
		return subCategories;
	}
	
	public static void main(String[] args) throws UploaderException{
		HomeDepotCategoryUploader u = new HomeDepotCategoryUploader();
//		List<Category> li = u.getRootCategories();
		u.walkAndStore();
		u.terminate();
//		System.out.println(li);
	}
}

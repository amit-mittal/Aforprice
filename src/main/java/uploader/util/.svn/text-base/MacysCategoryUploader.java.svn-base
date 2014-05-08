package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class MacysCategoryUploader extends NeoCategoryUploader {

		private static final Logger LOGGER= Logger.getLogger(MacysCategoryUploader.class);
		public MacysCategoryUploader() throws UploaderException {
			super(Retailer.MACYS.getId(), Retailer.MACYS.getLink());
		}

		/**
		 * The Macys categories HTML<p>
<ul class="globalNavigationBar">
<li><a href="http://www1.macys.com/shop/for-the-home?id=22672&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-22672_for_the_home"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_01c_off.gif" border="0" alt="home furnishings"></a></li>
<li><a href="http://www1.macys.com/shop/bed-bath?id=7495&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-7495_bed_bath"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_02c_off.gif" border="0" alt="bed and bath"></a></li>
<li><a href="http://www1.macys.com/shop/womens?id=118&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-118_womens"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_03c_off.gif" border="0" alt="women"></a></li>
<li><a href="http://www1.macys.com/shop/mens?id=1&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-1_mens"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_04c_off.gif" border="0" alt="men"></a></li>
<li><a href="http://www1.macys.com/shop/juniors?id=16904&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-16904_juniors"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_05c_off.gif" border="0" alt="juniors sizes"></a></li>
<li><a href="http://www1.macys.com/shop/kids?id=5991&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-5991_kids"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_06c_off.gif" border="0" alt="kids clothing"></a></li>
<li><a href="http://www1.macys.com/shop/beauty?id=669&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-669_beauty"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_07c_off.gif" border="0" alt="beauty and cosmetics"></a></li>
<li><a href="http://www1.macys.com/shop/shoes?id=13247&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-13247_shoes"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_08c_off.gif" border="0" alt="shoes"></a></li>
<li><a href="http://www1.macys.com/shop/handbags-accessories?id=26846&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-26846_handbags_accessories"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_09c_off.gif" border="0" alt="handbags and accessories"></a></li>
<li><a href="http://www1.macys.com/shop/jewelry-watches?id=544&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-544_jewelry_watches"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_10c_off.gif" border="0" alt="jewelry and watches"></a></li>
<li><a href="http://www1.macys.com/shop/sale?id=3536&amp;edge=hybrid&amp;cm_sp=navigation-_-top_nav-_-3536_sale"><img src="http://www1.macys.com/web20/assets/img/nav/co_topnav_wide_11c_off.gif" border="0" alt="macys sale"></a></li>
</ul>
		 * 
		 * @return
		 */
		@Override
		protected List<Category> getRootCategories() {
			List<Category> rootCats = new ArrayList<Category>();
			try {
				Document doc = getDocument(retailerLink);
				Element root = doc.getElementById("globalMastheadCategoryMenu");
				if(root == null)
					return rootCats; //Empty categories
				Elements catElements = root.getElementsByTag("li");
				for (Element catElement : catElements) {					
					Element catLinkElm = catElement
							.getElementsByAttribute("href").first();
					if (catLinkElm == null) {
						LOGGER.warn("No link found for "
								+ catElement.html());
						continue;
					} else {
						String url = catLinkElm.absUrl("href");
						String name = catLinkElm.ownText();
						if(name == null){
							LOGGER.warn("category name is null for " + catLinkElm.html());
							continue;
						}
						Category prodCat = new CategoryBuilder(getRetailerId(), null, name, url).build();
						rootCats.add(prodCat);
					}
				}

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			return rootCats;
		}

		/**
		 *  
<li class="nav_cat_item_bold">
<span>handbags</span>
<ul class="nav_cat_sub_3">
<li class="first ">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/all-handbags?id=27686&amp;edge=hybrid">All Handbags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/backpacks-laptop-bags?id=51906&amp;edge=hybrid">Backpacks &amp; Laptop Bags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/clutches-evening-bags?id=27691&amp;edge=hybrid">Clutches &amp; Evening Bags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/crossbody-messenger-bags?id=46011&amp;edge=hybrid">Crossbody &amp; Messenger Bags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/hobo-bags?id=46012&amp;edge=hybrid">Hobo Bags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/satchels?id=46013&amp;edge=hybrid">Satchels</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/shoulder-bags?id=46014&amp;edge=hybrid">Shoulder Bags</a>
</li>
<li class="">
<a href="http://www1.macys.com/shop/handbags-accessories/handbags/tote-bags?id=46015&amp;edge=hybrid">Tote Bags</a>
</li>
</ul>
</li>
		 * @param level
		 * @param parentCat
		 * @param parentDoc
		 * @return
		 */
		@Override
		protected List<Category> getSubCategories(int level, Category parentCat,
				Document parentDoc) {
			List<Category> subCategories = new ArrayList<Category>();
			Elements subCatGrpElms = parentDoc.getElementsByClass("nav_cat_item_bold");			
			for(Element subCatGrpElm: subCatGrpElms){
				//Each of the subcategory has a parent category which does not have a url. The parent is specified in span
				Element newParentElm = subCatGrpElm.getElementsByTag("span").first();
				Category newParentCat = parentCat;
				if(newParentElm != null){
					util.build.CategoryBuilder b = new util.build.CategoryBuilder();
					b.retailerId = getRetailerId();
					b.parentName = parentCat.getName();
					b.parentCategoryId = parentCat.getCategoryId();
					b.name = newParentElm.text();
					b.catType = CategoryType.PARENT;
					newParentCat = b.build();
					//Parent category has to be stored before any child category can be stored as child categories
					//need parent category id. Since, this category does not have to be traversed, it should not be 
					//added in the list of categories being returned. This category is to only establish a link
					storeCategory(newParentCat);
				}
				Elements cats = subCatGrpElm.getElementsByTag("a");
				for(Element cat: cats){
					String name = cat.ownText();
					String url = cat.absUrl("href");
					if(url == null || url.trim().length() == 0){
						LOGGER.error("Invalid URL: " + url + " for element " + cat);
						continue;
					}
					
					util.build.CategoryBuilder b = new util.build.CategoryBuilder();
					b.retailerId = getRetailerId();
					b.parentName = newParentCat.getName();
					b.parentCategoryId = newParentCat.getCategoryId();
					b.name = name;
					b.url = url;
					subCategories.add(b.build());	
				}
			}			
			return subCategories;
		}
		
		@Override
		protected CategoryType getCategoryType(String url, Document categoryDoc, String name){
			try{
				//In all terminal categories, there is a product count element with the information of number of 
				//products displayed on that page.
				Element prodCountElm = categoryDoc.getElementsByClass("breadCrumb_productCount").first();
				if(prodCountElm != null){
					return CategoryType.TERMINAL;
				}
				return CategoryType.PARENT;
			}catch(Exception e){
				LOGGER.error(e.getMessage(),e);
				return CategoryType.UNKNOWN;
			}		
		}

		
		public static void main(String[] args) throws UploaderException{
			MacysCategoryUploader u = new MacysCategoryUploader();
			u.walkAndStore();
			u.terminate();
		}
	


}

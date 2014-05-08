package uploader.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.UtilityLogger;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public class JCPCategoryUploader extends NeoCategoryUploader 
{

		private static final String HTTP_WWW_JCPENNEY_COM = "http://www.jcpenney.com/";
		private static final List<String> Headings2Ignore = Arrays.asList(
																"SIZES", 
																"SHOP BY SIZE", 
																"BRANDS", 
																"FAIR & SQUARE PRICES",	
																"SERVICES",
																"SHOPS", 
																"SHOP SALE & CLEARANCE");
		
		private static final Logger LOGGER = Logger.getLogger(JCPCategoryUploader.class);
				
		public JCPCategoryUploader() throws UploaderException {
			super(Retailer.JCPENNY);
		}

		/**
		 * @return
		 */
		@Override
		protected List<Category> getRootCategories() {
			List<Category> rootCats = new ArrayList<Category>();
			try {
				Document doc = getDocument(retailerLink);
				Elements catElements = doc.getElementById("topmenu").children();
				for (Element catElement : catElements) {
					// The first link is of the parent category.
					Elements catLinkElms = catElement
							.getElementsByAttribute("href");
					if (catLinkElms == null || catLinkElms.size() == 0) {
						LOGGER.warn("No links found for "
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
				LOGGER.error("Ooops.. put some code to catch exception so we don't come here", e);
			}
			return rootCats;
		}

		/**
		 * @param level
		 * @param parentCat
		 * @param parentDoc
		 * @return
		 */
		@Override
		protected List<Category> getSubCategories(int level, Category parentCat, Document parentDoc) {
			List<Category> subCategories = new ArrayList<Category>();
			Element sideNavigation = parentDoc.getElementById("side_navigation");
			if(sideNavigation == null){
				LOGGER.warn("Ignoring category due to no Side navigation: " + parentCat.getName() + ":" + parentCat.getUrl());
				return subCategories;
			}
								
			Elements listElements = sideNavigation.getElementsByTag("li");
			boolean startReadingCategory = false;
			boolean isHeading = false;
			String currentHeading = "";
			Category newParentCat = null;
			for(Element listElement:listElements)
			{
				isHeading = false;
				if( listElement.getElementsByTag("h3").size() != 0 || listElement.getElementsByTag("h2").size() != 0)
					isHeading = true;
				String listText = listElement.text();				
				//Heading is like a parent category to the list of categories falling under that heading
				if(isHeading)
				{	
					//reset the new parent cat
					newParentCat = null;
					startReadingCategory = true;
					if(Headings2Ignore.contains(listText)){ 
						startReadingCategory = false;
						LOGGER.info("Ignoring category: " + listText);
						continue;
					}
					currentHeading = listText;
					util.build.CategoryBuilder b = new util.build.CategoryBuilder();
					b.retailerId = getRetailerId();
					b.parentName = parentCat.getName();
					b.parentCategoryId = parentCat.getCategoryId();
					b.name = currentHeading;
					b.catType = CategoryType.PARENT;
					newParentCat = b.build();
					//Parent category has to be stored before any child category can be stored as child categories
					//need parent category id. Since, this category does not have to be traversed, it should not be 
					//added in the list of categories being returned. This category is to only establish a link
					storeCategory(newParentCat);
					continue;
				}				
				if(listText.toLowerCase().indexOf("all") > 0 || 
						listText.toLowerCase().indexOf("clearance") > 0 || 
						listText.equalsIgnoreCase("sale") ||
						//sports fan shop is dealt differently
						listText.equalsIgnoreCase("sports fan shop") ||
						listText.equalsIgnoreCase("monogram shop")){
					LOGGER.info("Ignoring category: " + listText);
					continue;				
				}
				
				if(startReadingCategory)
				{
					util.build.CategoryBuilder b = new util.build.CategoryBuilder();
					b.retailerId = getRetailerId();
					b.parentName = newParentCat != null?newParentCat.getName(): parentCat.getName();
					b.parentCategoryId = newParentCat != null?newParentCat.getCategoryId(): newParentCat.getCategoryId();
					b.name = listText;
					b.url = listElement.children().last().absUrl("href");;
					subCategories.add(b.build());
				}
			}
			return subCategories;
		}
		
		public static void main(String[] args) throws UploaderException{
			JCPCategoryUploader u = new JCPCategoryUploader();
			u.walkAndStore();
		}

}

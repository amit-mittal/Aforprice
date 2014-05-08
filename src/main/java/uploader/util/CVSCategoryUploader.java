package uploader.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Category;
import entities.Retailer;
import entities.Category.CategoryBuilder;

public class CVSCategoryUploader extends NeoCategoryUploader{
	private static final Logger LOGGER= Logger.getLogger(MacysCategoryUploader.class);
	
	public CVSCategoryUploader() throws UploaderException {
		super(Retailer.CVS.getId(), Retailer.CVS.getLink());
	}

	/**
	 * 
<ul class="refineStyleCatalog">
<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/_/N-3rZe6tkZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine1">
Baby &amp; Child</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Beauty/_/N-3rZe7upZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine2">
Beauty</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Diet-&amp;-Nutrition/_/N-3rZe7elZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine3">
Diet &amp; Nutrition</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Home-Health/_/N-3rZe6z7Z2k?pt=DEPARTMENT">
<span class="pl40" for="refine4">
Home Health</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Household/_/N-3rZe7ejZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine5">
Household</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Medicine-Cabinet/_/N-3rZidZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine6">
Medicine Cabinet</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Personal-Care/_/N-3rZifZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine7">
Personal Care</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Sexual-Health/_/N-3rZe79tZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine8">
Sexual  Health</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Skin-Care/_/N-3rZe76oZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine9">
Skin Care</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Vitamins/_/N-3rZieZ2k?pt=DEPARTMENT">
<span class="pl40" for="refine10">
Vitamins</span>
</a></span></span></li>
</ul>
	 * 
	 * @return
	 */
	@Override
	protected List<Category> getRootCategories() {		
		try {
			Document doc = getDocument(retailerLink);
			return getCategories(doc, null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 *  
<ul class="refineStyleCatalog">

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Bath-&amp;-Skin-Care/_/N-3tZe6tmZ2k?pt=CATEGORY">
<span class="pl40" for="refine1">
Bath &amp; Skin Care</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Diapers-&amp;-Wipes/_/N-3tZe6tnZ2k?pt=CATEGORY">
<span class="pl40" for="refine2">
Diapers &amp; Wipes</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Feeding-&amp;-Nursing/_/N-3tZe6toZ2k?pt=CATEGORY">
<span class="pl40" for="refine3">
Feeding &amp; Nursing</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Food-&amp;-Formula/_/N-3tZe844Z2k?pt=CATEGORY">
<span class="pl40" for="refine4">
Food &amp; Formula</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Gifts-&amp;-Toys/_/N-3tZe7wxZ2k?pt=CATEGORY">
<span class="pl40" for="refine5">
Gifts &amp; Toys</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Health/_/N-3tZe6tlZ2k?pt=CATEGORY">
<span class="pl40" for="refine6">
Health</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Natural-&amp;-Organic/_/N-3tZ13hok2Z2k?pt=CATEGORY">
<span class="pl40" for="refine7">
Natural &amp; Organic</span>
</a></span></span></li>

<li><span class="refineDept"><span class="refineCatalog"><a class="nobackground_image" href="/shop/Baby-&amp;-Child/Nursery-&amp;-Home/_/N-3tZe7x1Z2k?pt=CATEGORY">
<span class="pl40" for="refine8">
Nursery &amp; Home</span>
</a></span></span></li>
</ul>
	 * @param level
	 * @param parentCat
	 * @param parentDoc
	 * @return
	 */
	@Override
	protected List<Category> getSubCategories(int level, Category parentCat,
			Document parentDoc) {
		return getCategories(parentDoc, parentCat);
	}
	
	private List<Category> getCategories(Document doc, Category parent){
		List<Category> rootCats = new ArrayList<Category>();
		try {		
			Elements catElements = doc.getElementsByClass("refineCatalog");
			for (Element catElement : catElements) {					
				Element catLinkElm = catElement
						.getElementsByAttribute("href").first();
				if (catLinkElm == null) {
					LOGGER.warn("No link found for "
							+ catElement.html());
					continue;
				} else {
					String url = catLinkElm.absUrl("href");
					if(url == null || url.trim().length() == 0){
						LOGGER.error("Invalid url: " + catLinkElm.html());
						continue;
					}
					Element p140 = catLinkElm.getElementsByClass("pl40").first();
					if(p140 == null){
						LOGGER.warn("No p140 element found for " + catLinkElm.html());
						continue;
					}
					String name = p140.text();
					if(name == null){
						LOGGER.warn("category name is null for " + p140.html());
						continue;
					}
					Category prodCat = new CategoryBuilder(getRetailerId(), parent, name, url).build();
					rootCats.add(prodCat);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return rootCats;
	}
	
	public static void main(String[] args) throws UploaderException{
		NeoCategoryUploader u = new CVSCategoryUploader();
		u.walkAndStore();
		u.terminate();
//		List<Category> li = u.getRootCategories();
//		System.out.println(li);
	}

}

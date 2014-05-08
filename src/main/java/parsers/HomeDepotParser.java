package parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.HomeDepotClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import db.dao.DAOException;
import entities.ProductSummary;
import entities.Retailer;

public class HomeDepotParser extends ProductsParser {

	private static final Logger logger = Logger.getLogger(HomeDepotParser.class);
	//All PRODUCTS (114)
	private static final Pattern prodCntPatt = Pattern.compile("All PRODUCTS \\((.*)\\)");
	private static final Pattern numReviewPatt = Pattern.compile("(\\d+)");

	
	private final ClassNames htlmClasses = new HomeDepotClassNames();

	public HomeDepotParser() {
		super(Retailer.ID.HOMEDEPOT);
	}
	
	/**
	 * It parses the following HTML page to get the products 
	 * 
productRows=============>	 
<div class="product pod grid_6 alpha" style="height: 570px; ">
     <div class="spad">
      <form name="comparisonform">
        <div class="productlist">
          	<label for="field">
            	<input type="checkbox" value="202677431" name="product" onclick="javascript:compareToSave();">Select to compare
          	</label>
          	
        </div>
        <div class="content dynamic">
        
         <div class="cell_section1">
productImage============>         
          <div class="content_image">
               <a href="http://www.homedepot.com/Appliances-Dishwashers-Disposers-Dishwashers-Built-In-Dishwashers/h_d1/N-5yc1vZc3nj/R-202677431/h_d2/ProductDisplay?catalogId=10053&amp;langId=-1&amp;storeId=10051">
                    <img src="http://www.homedepot.com/catalog/productImages/145/94/9484ac4d-332d-48a7-8831-a23807fd5398_145.jpg" data-original="http://www.homedepot.com/catalog/productImages/145/94/9484ac4d-332d-48a7-8831-a23807fd5398_145.jpg" alt="Built-In Dishwasher in White" height="145" width="145" class="lazyLoad" style="display: inline; ">
					<noscript>&lt;img src="http://www.homedepot.com/catalog/productImages/145/94/9484ac4d-332d-48a7-8831-a23807fd5398_145.jpg" alt="Built-In Dishwasher in White" height="145" width="145"/&gt;</noscript>
										
               </a>
    		<!--Modifed for performance issue -->
    		<a class="sskQuickViewPlp SSku_FB_Layover dynamic_btn grey_btn hide" productids="0" href="http://www.homedepot.com/webapp/wcs/stores/servlet/QuickViewService?storeId=10051&amp;langId=-1&amp;catalogId=10053&amp;R=202677431&amp;catEntryId=202677431">quick view</a>
          </div>
          <p class="more_options_container">
			
		  </p>
          </div>
          
         <div class="cell_section2">
price==================>		
               <div class="item_pricing_wrapper">
                    
                    <span class="xlarge item_price">$399.00</span>
                    <span class="normal item_stike_price" style="display:none;">WAS&nbsp;$399.00</span>
               </div>
productName/productDesc=>		
                    <a class="item_description" href="http://www.homedepot.com/Appliances-Dishwashers-Disposers-Dishwashers-Built-In-Dishwashers/h_d1/N-5yc1vZc3nj/R-202677431/h_d2/ProductDisplay?catalogId=10053&amp;langId=-1&amp;storeId=10051">
                        Danby Designer&nbsp;Built-In Dishwasher in White
                    </a>
               
          <p class="rebates_container">
	          
          </p>
model==================>		
          <p class="model_container">
	          
	               <span class="b">Model&nbsp;#</span>&nbsp;<span>DDW1809W</span>
	          
          </p>
          
          <div class="ratings">
	          
				<a class="reviews" href="http://www.homedepot.com/Appliances-Dishwashers-Disposers-Dishwashers-Built-In-Dishwashers/h_d1/N-5yc1vZc3nj/R-202677431/h_d2/ProductDisplay?catalogId=10053&amp;langId=-1&amp;storeId=10051#customer_reviews">
		 			<span class="stars" rel="2.5" style="width: 50.0%"></span>
				</a>
				
					<a class="b" href="http://www.homedepot.com/Appliances-Dishwashers-Disposers-Dishwashers-Built-In-Dishwashers/h_d1/N-5yc1vZc3nj/R-202677431/h_d2/ProductDisplay?catalogId=10053&amp;langId=-1&amp;storeId=10051#customer_reviews">(9)</a>
				
			</div>
		  </div>
		  
		  <div class="cell_section3">        
          
          <div class="shipping_options">
			
	
<!-- BEGIN IMPORT OF: /hdus/catalog/legacy/include/FreeShippingPromotionMessageInclude.jspf     IMPORTED BY: ProdCell.jspf -->
<!-- START - FreeShippingPromotionMessageInclude.jspf --><!-- END - FreeShippingPromotionMessageInclude.jspf -->
<!-- END IMPORT: /hdus/catalog/legacy/include/FreeShippingPromotionMessageInclude.jspf -->


	
          </div>
          
          	<div class="cart_options">
               							
                    <a class="dynamic_btn orange_btn c" href="http://www.homedepot.com/webapp/wcs/stores/servlet/OrderItemAdd?storeId=10051&amp;langId=-1&amp;catalogId=10053&amp;catEntryId=202677431&amp;quantity=1&amp;URL=OrderItemDisplay&amp;contractId=2081191">
                         <span class="plus">Add To Cart</span>
                    </a>
               	
			</div>
			<div class="bopis_options">
				
				    <p class="clear_btn titleCase">
	  					<span class="online_only short">Online Exclusive</span>
					</p>
				
	
<!-- BEGIN IMPORT OF: /hdus/catalog/legacy/include/CheckStoreAvailabilityInclude.jspf     IMPORTED BY: ProdCell.jspf -->

<a class="modal_overlay dynamic_btn split" href="http://www.homedepot.com/webapp/wcs/stores/servlet/Bopis2OverLay?storeId=10051&amp;langId=-1&amp;catalogId=10053&amp;R=202677431&amp;langId=-1&amp;catalogId=10053&amp;storeSkuNum=&amp;Overlay_Type=add_to_cart&amp;locStoreNum=989&amp;pageNum=0&amp;mode=localstore&amp;quantity=1&amp;basePage=PLP" style="display: none; visibility: visible; " rel="bopis">
	<span class="bopis_icon">PICK UP IN STORE</span>
</a>

<!-- END IMPORT: /hdus/catalog/legacy/include/CheckStoreAvailabilityInclude.jspf -->


	
	        </div>
         </div>
         
        </div>
      </form>
    </div>	
</div>
	 * @param doc
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId,
										String categoryName, int startRank, int max) {
		Element helper = null;
		List<ProductSummary> products = new ArrayList<ProductSummary>();		
	
		Element productsElement = doc.getElementById("products");
		//This ensures we don't return products from no terminal category
		if(productsElement == null) return products;
		
		Elements productRows = productsElement.getElementsByClass(htlmClasses.productRows()[0]);
		for(Element productRow: productRows){
			if(!productRow.hasClass("dynamic"))continue;
			String name = "";
			String desc = "";//Description is absent.
			double price = PriceTypes.UNKNOWN.getValue();
			String url = "";
			String imageUrl = "";
			String model = "";
				
			helper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
			if(helper != null){
				name = helper.html();
				url = helper.absUrl("href");
			}			
			helper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(helper != null){
				helper = helper.getElementsByTag("img").first();
				if(helper != null)
					imageUrl = helper.attr("data-original");
				//imageUrl = productRow.getElementsByClass(htlmClasses.productImage()).first().attr("abs:src");
			}
			helper = getPriceElement(productRow);
			if(helper != null){											
				price = PriceFormatter.formatDollarPrice(helper.html());
			}
			helper = productRow.getElementsByClass(htlmClasses.model()[0]).first();
			if(helper != null){
				Elements elms = helper.getElementsByTag("span");
				for(Element elm: elms){
					if(elm.html().contains("Model"))
						continue;
					model = elm.html();
					break;
				}
			}
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = price;
			b.url = url;
			b.imageUrl = imageUrl;
			b.desc = desc;
			b.model = model;
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();
			if(!ProductUtils.isProductValid(prod)){
				logger.warn("Invalid Product: " + prod);
			}

			products.add(prod);						
		}
		return products;
	}

	protected double getReviewRating(Element product){
		try{
			if(product == null)
				return -1;
			Element reviewElm = product.getElementsByClass("reviews").first();
			if(reviewElm != null){
				Element stars = reviewElm.getElementsByClass("stars").first();
				String ratingStr = stars.attr("rel");
				if(ratingStr != null)
					return Double.parseDouble(ratingStr.trim());
			}
		}catch(Exception e){
			logger.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		try{
			if(product == null)
				return -1;
			Element ratingsElm = product.getElementsByClass("ratings").first();
			if(ratingsElm != null){
				Element numReviewElm = ratingsElm.getElementsByClass("b").first();
				if(numReviewElm != null){
					//(24)
					String numReviewsStr = numReviewElm.text();
					Matcher m = numReviewPatt.matcher(numReviewsStr);
					if(m.find()){
						return Integer.parseInt(m.group(1).trim());		
					}
				}
			}
		}catch(Exception e){
			logger.error("Error getting number of reviews", e);
		}
		return -1;
	}
	/**
	 * 
	 * <div class="page-nav">
	 *   <span>1</span>
	 *   <a class="paginationNumberStyle" href="/b/Kitchen-Cleaning-Supplies-Cleaners/N-5yc1vZc093?Nao=96&amp;rpp=96"> 2</a>
	 *   <a class="paginationNumberStyle" href="/b/Kitchen-Cleaning-Supplies-Cleaners/N-5yc1vZc093?Nao=192&amp;rpp=96"> 3</a>
	 *   <a class="paginationNumberStyle" href="/b/Kitchen-Cleaning-Supplies-Cleaners/N-5yc1vZc093?Nao=288&amp;rpp=96"> 4</a>
	 *   <a class="paginationNumberStyle" href="/b/Kitchen-Cleaning-Supplies-Cleaners/N-5yc1vZc093?Nao=384&amp;rpp=96"> 5</a>
	 *   <a class="paginationNumberStyle page_arrows" href="/b/Kitchen-Cleaning-Supplies-Cleaners/N-5yc1vZc093?Nao=96&amp;rpp=96">
	 *   	<img alt="" src="/static/images/layout/triangle-green-right.gif" /></a>
	 * </div>
	 * @param doc
	 * @return
	 */
	@Override
	protected String getNextURL(Document doc) {
		Element nextUrlElm = doc.getElementsByClass(htlmClasses.nextUrl()).first();
		String url = null;
		if(nextUrlElm == null)
			return null;
		Element lastUrl = nextUrlElm.getElementsByClass("paginationNumberStyle").last();
		if(lastUrl == null)
			return null;
		Element imgElm = lastUrl.getElementsByTag("img").first();
		if(imgElm == null)
			return null;
		String imageName = imgElm.attr("src");
		if(imageName != null && imageName.contains("right.gif")){
			url = lastUrl.attr("abs:href");
			if(url != null){
				url = transformUrl(url);
			}
		}							
		return url;
	}

	@Override
	protected ClassNames getClassNames() {
		return htlmClasses;
	}
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = null;
		if(url.indexOf("?") == -1){
			urlNew = new StringBuilder(url).append("?rpp=96");
		}
		else {
			if(url.indexOf("rpp=") != -1){
				urlNew = new StringBuilder(url.replaceAll("rpp=\\d+", "rpp=96"));
			}
			else
				urlNew = new StringBuilder(url).append("&rpp=96");
		}		
		return urlNew.toString();		
	}
	
	@Override
	protected int getProductCountForCategory(Document doc)
	{
		int recordCount = 0;
		try
		{
			String recordCountStr = doc.getElementById("all_products").text();
			Matcher m = prodCntPatt.matcher(recordCountStr);
			if (m.find()) {
			    recordCount = Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e)
		{
			logger.warn("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		try{
			Element sortElm = doc.getElementById("sorting_Ns");
			if(sortElm != null){
				Element selected = sortElm.getElementsByAttribute("selected").first();
				if(selected != null){
					if(selected.text().equalsIgnoreCase("Top Sellers"))
						return true;					
				}
			}
		}catch(Exception e){
			logger.warn("Error while getting sort order", e);
		}
		logger.equals("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}
	
	public static void main(String[] args) throws IOException, DAOException{
		try{
			ProductsParser parser = new HomeDepotParser();
			//String url = "http://www.homedepot.com/Appliances-Dishwashers-Disposers-Dishwashers-Built-In-Dishwashers/h_d1/N-5yc1vZc3nj/h_d2/Navigation?catalogId=10053&Nu=P_PARENT_ID&langId=-1&storeId=10051&searchNav=true";
			String url = "http://www.homedepot.com/Storage-Organization-Garage-Storage-Workbenches/h_d1/N-5yc1vZbtub/h_d2/Navigation?catalogId=10053&Nu=P_PARENT_ID&langId=-1&storeId=10051&searchNav=true";
			url = "http://www.homedepot.com/Storage-Organization-Sheds-Garages-Outdoor-Storage/h_d1/N-5yc1vZbste/h_d2/Navigation?catalogId=10053&Nu=P_PARENT_ID&langId=-1&storeId=10051&searchNav=true";
			url = "http://www.homedepot.com/Storage-Organization-Sheds-Garages-Outdoor-Storage-Accessories/h_d1/N-5yc1vZbty6/h_d2/Navigation?catalogId=10053&Nu=P_PARENT_ID&langId=-1&storeId=10051&searchNav=true";
			url = "http://www.homedepot.com/h_d1/N-5yc1vZc3oo/h_d2/Navigation?catalogId=10053&langId=-1&storeId=10051";
			url= "http://www.homedepot.com/b/Appliances-Refrigeration-Refrigerators-French-Door-Refrigerators/N-5yc1vZc3oo";
			parser.parseAndSave(0, "Unknown", url, new NullProductStore());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}

package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.CVSClassNames;
import parsers.html.ClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class CVSParser extends ProductsParser {
	
	private static final Logger LOGGER = Logger.getLogger(CVSParser.class);	
	private static final ClassNames htlmClasses = new CVSClassNames();
	
	private static final Pattern prodCnt = Pattern.compile(".*Results.*of(.*)");
	private static final Pattern reviewPatt = Pattern.compile(".*rating_(.*)");
	private static final Pattern numReviewPatt = Pattern.compile("(\\d+)");

	public CVSParser() {
		super(Retailer.CVS.getId());
	}

	/**
	 * 
<div class="product">
<div class="innerBox">
<div class="productSection1">
<a class="resultImgLi" href="/shop/product-detail/Burts-Bees-Nourishing-Baby-Oil?skuId=113520" title="Burt's Bees Nourishing Baby Oil" onclick="dcsMultiTrack('DCSext.SearchandiseSource','1,113520,category')">
<img src="/bizcontent/merchandising/productimages/small/79285071299.jpg" alt="Burt's Bees Nourishing Baby Oil">
</a>
</div>
<div class="productSection2">
<h4 class="productBrand">
<a class="resultTitleLi" href="/shop/product-detail/Burts-Bees-Nourishing-Baby-Oil?skuId=113520" title="Burt's Bees Nourishing Baby Oil" onclick="dcsMultiTrack('DCSext.SearchandiseSource','1,113520,category')">
<span class="blockimg">
Burt's Bees Nourishing Baby Oil</span>
<span class="blockimg1">
Burt's Bees Nourishing Baby Oil</span>
</a>
</h4>
<div class="productResultsGrid productRate">
<a class="writeReview" href="//cvspharmacy.ugc.bazaarvoice.com/3006-en_us/113520/writereview.htm?user=6023facff601c3b8a128d8494b24615a646174653d323031322d30392d3035267573657269643d393133393035373934&amp;return=http%3A%2F%2Fwww.cvs.com%2Fshop%2FBaby-%26-Child%2FBath-%26-Skin-Care%2FBaby-Oil%2F_%2FN-3uZe6vwZ2k%3Fpt%3DSUBCATEGORY">
<div class="rating_0_0">
<div id="writeReview"><img src="/webcontent/images/common/spacer.png" alt="" title="">&nbsp;<span class="productResultsGrid productRate productRate_0">Write a review</span> </div>
</div>
</a>
</div>
<div class="productResultsGrid productSize font12">
<p>
<b>Size:</b> 4 OZ
<span class="prodWeight"><b>Weight:</b> 0.29&nbsp;LBS</span>
</p>
</div>
<ul class="productBullets">
<li class="productShipping">
<a href="#" title="Opens free standard shipping eligible details in a new window" data-url="/help/popups/shipping_info_details_popup.jsp" rel="#cvs-overlay" class="overlay"><b>FREE Shipping Eligible</b> </a>
</li>
</ul>
</div>
<div class="productSection3">
<div class="row">
<strong class="productPrice">$8.99</strong>
</div>
<div class="row">
<div class="Outofstock ml2">
Out of Stock
</div>
<!-- Shopping list starts -->
<div class="addToList">

<a class="productAddList arrow " href="/account/login.jsp?screenname=/shop/Baby-%26-Child/Bath-%26-Skin-Care/Baby-Oil/_/N-3uZe6vwZ2k%3Fpt%3DSUBCATEGORY%26listItem%3D113520">
<span class="left"></span>
<span class="center" style="padding: 1px;">
Add to shopping list
</span>
<span class="right"></span></a>
</div>
<!-- Shopping List Ends -->
</div>
</div>
</div>
</div>	 
	 * @param doc
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId,
										String categoryName, int startRank, int count) {
		Element helper = null;
		Elements productRows = doc.getElementsByClass(htlmClasses.productRows()[0]);
		List<ProductSummary> products = new ArrayList<ProductSummary>();		
		for(Element productRow: productRows){			
			String name = "";
			String desc = ""; //Description is not available
			double price = PriceTypes.UNKNOWN.getValue();
			String url = "";
			String imageUrl = "";
			String model = ""; //Model is not available
				
			helper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
			if(helper != null){
				Element spanClass = helper.getElementsByClass("blockimg").first();
				if(spanClass == null){
					spanClass = helper.getElementsByClass("blockimg1").first();
				}
				if(spanClass != null)
					name = helper.text();
				url = helper.absUrl("href");
			}	
			
			helper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(helper != null){
				helper = helper.getElementsByTag("img").first();
				if(helper != null)
					imageUrl = helper.attr("abs:src");				
			}
			helper = getPriceElement(productRow);
			if(helper != null){											
				price = PriceFormatter.formatDollarPrice(helper.html());
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
				LOGGER.warn("Invalid Product: " + prod);
			}
			products.add(prod);				
		}
		return products;
	}
	
	protected double getReviewRating(Element product){
		try{
			if(product == null)
				return -1;
			Element numReviewsElm = product.getElementsByClass("ratingNumber").first();
			if(numReviewsElm != null){
				Element reviews = numReviewsElm.parent();
				//rating_4_5
				String reviewStr = reviews.attr("class");
				Matcher m = reviewPatt.matcher(reviewStr);
				if(m.find()){
					double rating = Double.parseDouble(m.group(1).trim().replace("_", "."));
					if(rating > 5){
						LOGGER.warn("Review rating more than 5: " + reviewStr);
						return -1;
					}
					return rating;
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		try{
			if(product == null)
				return -1;
			Element numReviewsElm = product.getElementsByClass("ratingNumber").first();
			if(numReviewsElm != null){
				//(7082)
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					return Integer.parseInt(m.group(1).trim());		
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting number of reviews", e);
		}
		return -1;
	}

	
	/**
	 * Get all the products in one single query
	 * http://www.cvs.com/shop/Beauty/Makeup/Lipstick/_/N-3uZe7vlZ2k?pt=SUBCATEGORY&navNum=100000
	 * @param url
	 * @return
	 */
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?navNum=100");
		}
		else
			urlNew.append("&navNum=100");
		urlNew.append("&sortBy=mostReviewed&pageNum=1");
		return urlNew.toString();		
	}


	@Override
	protected String getNextURL(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getNextURL(Document doc, String currentURL, String categoryURL){
		try{
			String[] parts = currentURL.split("&pageNum=");
			if(parts == null || parts.length != 2)
				return null;
			int currPage = Integer.parseInt(parts[1]);
			return parts[0] + "&pageNum=" + ++currPage;
		}catch(Exception e){
			LOGGER.warn("Unable to get next url", e);
		}
		return null;
	}

	@Override
	protected ClassNames getClassNames() {
		return htlmClasses;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//Results 1-512 of 512
			Element form = doc.getElementById("topForm");
			Elements strongElms = form.getElementsByTag("strong");
			for(Element elm: strongElms){
				if(elm.text().contains("Results")){
					String recordCountStr = elm.text().trim();
					Matcher m = prodCnt.matcher(recordCountStr);
					if (m.find()) {
					    recordCount = Integer.parseInt(m.group(1).trim());
					}	
				}
			}
			
		}
		catch(Exception e){
			LOGGER.warn("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		//Sort options: top rated, most reviewed, price and name
		return false;
	}

	
	public static void main(String[] args) throws Exception{
		String url = "http://www.cvs.com/shop/Beauty/Makeup/Lipstick/_/N-3uZe7vlZ2k?pt=SUBCATEGORY";
		url = "http://www.cvs.com/shop/Baby-&-Child/Diapers-&-Wipes/Diapers/_/N-3uZe6u8Z2k?pt=SUBCATEGORY";
		url = "http://www.cvs.com/shop/Beauty/Lips/_/N-3tZ13l41iZ2k?pt=CATEGORY";
		ProductsParser parser = new CVSParser();
		parser.parseAndSave(0, "NA", url, new NullProductStore());
	}

}

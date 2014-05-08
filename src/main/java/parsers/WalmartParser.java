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

import parsers.html.ClassNames;
import parsers.html.WalmartClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import stores.ProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;


public class WalmartParser extends ProductsParser {
	
	private static final Logger LOGGER = Logger.getLogger(WalmartParser.class);
	private static final Pattern prodCntPatt = Pattern.compile("^.*of (.*) total.*");
	//4.1 out of 5 Stars
	private static final Pattern reviewPatt = Pattern.compile("(.*)out.*of(.*)Stars*");
	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)\\)");
	
	private ClassNames htlmClasses = new WalmartClassNames();

	public WalmartParser() {
		super(Retailer.WALMART.getId());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htlmClasses;
	}
	/**
	 * Walmart page is organized as 
	 * See All Departments-->Category-->Sub-Category-->Sub-Sub-Category ...-->List of products
	 * @param The Document corresponding to the sub(n)-category whose children are products
	 * @throws BandBajGaya 
	 */	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){		
		Elements productRows = doc.getElementsByClass(htlmClasses.productRows()[0]);
		List<ProductSummary> products = new ArrayList<ProductSummary>();		
		for(Element productRow: productRows){
			String name = "";
			String desc = "";
			double price = PriceTypes.UNKNOWN.getValue();
			String url = "";
			String imageUrl = "";
			String model = "";
				
			Element nameUrlhelper = productRow.getElementsByClass(htlmClasses.productName()[0]).first(); 
			if(nameUrlhelper != null){
				name = nameUrlhelper.html();			
				url = nameUrlhelper.absUrl("href");
			}
			Element imgHelper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(imgHelper != null){
				imageUrl = imgHelper.attr("abs:src");
			}
			Element priceHelper = getPriceElement(productRow);
			if(priceHelper != null){
				String t = priceHelper.text();
				if(t!= null){
					if(t.equalsIgnoreCase("Free")){
						price = 0d;
					}if(t.equalsIgnoreCase("Click here for price")){
						price = PriceTypes.CLICK_FOR_PRICE.getValue();
					}
					else{
						StringBuilder priceBuilder = new StringBuilder(t);
						priceHelper = getDecimalPriceElement(productRow);
						priceBuilder.append(priceHelper == null? "00": priceHelper.text());
						price = PriceFormatter.formatDollarPrice(priceBuilder.toString());
					}
				}
			}
			else{
				priceHelper = productRow.getElementsByClass("ShelfText").first();
				if(priceHelper != null){
					if(priceHelper.html() != null){
						if(priceHelper.html().equalsIgnoreCase("In stores only"))
							price = PriceTypes.NOT_AVAILABLE.getValue();
					}
				}
				if(PriceTypes.isInvalidType(price)){
					priceHelper = productRow.getElementsByClass("ShelfTextOOS").first();
					if(priceHelper != null){
						if(priceHelper.text() != null){
							if(priceHelper.text().equalsIgnoreCase("Out of stock online")){
								price = PriceTypes.OUT_OF_STOCK.getValue();
							}
						}
					}
				}
			}
			Element modelHelper = productRow.getElementsByClass(htlmClasses.model()[0]).first();
			if(modelHelper != null){
				model = modelHelper.html(); 
			}
			Element descHelper = productRow.getElementsByClass(htlmClasses.productDesc()).first();
			if(descHelper != null){
				desc = descHelper.html();
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
			Element reviewElm = product.getElementsByClass("wmStars").first();
			if(reviewElm != null){
				//4.1 out of 5 Stars
				String reviews = reviewElm.getElementsByAttribute("title").first().attr("title");
				Matcher m = reviewPatt.matcher(reviews);
				if(m.find()){
					int total = Integer.parseInt(m.group(2).trim());
					if(total != 5){
						LOGGER.warn("Invalid max review rating: " + total +", review string: " + reviews);
						return -1;
					}
					double reviewCount = Double.parseDouble(m.group(1).trim());
					return reviewCount;					
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
			Element numReviewsElm = product.getElementsByClass("NumOfReviews").first();
			if(numReviewsElm != null){
				//(24)
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					return Integer.parseInt(m.group(1).trim());		
				}
			}
			
			Element customerRatingStarsElm = product.getElementsByClass("CustomerRatingStars").first();
			if(customerRatingStarsElm != null){
				//(24)
				String numReviews = customerRatingStarsElm.text();
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
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?ic=60_0&showAll=true");
		}
		else if( url.indexOf("ic=") == -1 ){
			urlNew.append("&ic=60_0");
		}
		urlNew.append("&facet=retailer%3AWalmart.com&tab_value=All");
		
		return urlNew.toString().replaceFirst("ic=\\d\\d", "ic=60");		
	}
	
	


	@Override
	protected String getNextURL(Document doc, String currentURL, String categoryURL, int currPage){
		Element nextURLElement = doc.getElementsByClass(htlmClasses.nextUrl()).first();
		if(nextURLElement == null)
			return null;
		String url = nextURLElement.attr("abs:href");
		if(url == null || url.isEmpty())
			return null;
		if(currPage > 1 && currentURL.contains("browse-ng.do")){			
			//build the url as with walmart, sometimes the next url attribute for some of the urls is bad
			//eg. for the category url http://www.walmart.com/search/browse-ng.do?browsein=true&ic=48_0&ref=125873.154301
			//after traversing through some pages, next url returns the following url
			//http://www.walmart.com/browse/0/0/?tab_value=all&facet=retailer%3aWalmart.com&ss=false&ic=60_900
			String nextPageIdx = "60_" + (currPage*60);
			url = currentURL.replaceAll("ic=\\d+_\\d+", "ic=" + nextPageIdx);
		}
		System.out.println("next url=" + url);
		return url;
	}
	
	@Override
	protected String getNextURL(Document doc){
		throw new UnsupportedOperationException();
	}

	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			String recordCountStr = doc.getElementsByClass("SPRecordCount").text();
			Matcher m = prodCntPatt.matcher(recordCountStr);
			if (m.find()) {
			    recordCount = Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e){
			LOGGER.warn("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		try{
			if( doc.getElementsByClass("DropMenuHead").first() != null && 
					doc.getElementsByClass("DropMenuHead").first().text().equalsIgnoreCase("Best Sellers") )
				return true;
		}catch(Exception e){
			LOGGER.warn("Error while getting sort order", e);
		}
		LOGGER.info("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}

		
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new WalmartParser();
		String url = "http://www.walmart.com/browse/accessories/digital-photo-frames/3944_133277_132913_164092?browsein=true&catNavId=132913&ic=48_0";				
		url = "http://www.walmart.com/search/browse-ng.do?browsein=true&ic=48_0&ref=125873.154301";
		url = "http://www.walmart.com/browse/ipad-amp;-ereaders/apple-ipad-accessories/3944_1078524_1084930?browsein=true&_refineresult=true&catNavId=1078524&ic=48_0&povid=cat1086506-env414475-moduleA062911-lLinkLHN6iPadAccessories";
		url = "http://www.walmart.com/search/browse-ng.do?browsein=true&ic=48_0&ref=125873.154301";
		url = "http://www.walmart.com/search/browse-ng.do?browsein=true&ic=48_0&ref=125873.154511";
		url = "http://www.walmart.com/browse/sports-amp;-outdoors/sports-fan-shop/4125_1063984?browsein=true&_refineresult=true&ic=32_0&povid=cat1064345-env271033-moduleA050512-lLinkLHNCharlotte Bobcats&facet=league%3ANBA%7C%7Csports_team%3ACharlotte+Bobcats";
		ProductStore store = new NullProductStore(); 
		parser.parseAndSave(0, "Unknown", url, store);
	}
}

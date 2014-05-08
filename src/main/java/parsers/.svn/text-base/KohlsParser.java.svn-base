package parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.KohlsClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.Utils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class KohlsParser extends ProductsParser {
	private ClassNames htlmClasses = new KohlsClassNames();
	private static final Logger LOGGER = Logger.getLogger(KohlsParser.class);
	private static final String AVAILABLE_IN_CART = "For final price, Add to Bag.";
	private static final Pattern numReviewPatt = Pattern.compile("(.*)reviews");
	private static final Pattern reviewPat = Pattern.compile("Rating: (.*) of (.*)");
	private static final Pattern multi = Pattern.compile("(.*)–(.*)");
	
	public KohlsParser() {
		super(Retailer.ID.KOHLS);
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htlmClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		Elements productRows = doc.getElementsByClass("product");
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		int count = 0;
		for(Element productRow: productRows){
			String name = "", url = "", imgUrl = "";
			Map<String, String> nameUrlMap = getNameUrlMap(productRow);
			for(Map.Entry<String, String> entry: nameUrlMap.entrySet()){
				name = entry.getKey();
				url = entry.getValue();
			}
			imgUrl = getImgUrl(productRow);
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.url = url;
			b.price = getPrice(categoryId, name, url, productRow);
			b.desc = getDesc(productRow);
			b.imageUrl = imgUrl;
			b.model = getModel(productRow);
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();

			ProductSummary prod = b.build();
			if(!ProductUtils.isProductValid(prod))
				LOGGER.warn("Invalid Product:" + prod);
			products.add(prod);
			if(max > 0 && ++count >= max)
				break;
		}
		return products;
	}
	
	protected int getNumReviews(Element productRow)
	{
		// TODO Auto-generated method stub
		try{
			if(productRow == null)
				return -1;
			Element numReviewsElms = productRow.getElementsByClass("rating").first();
			if(numReviewsElms == null)
				return -1;
			Element numReviewElm = numReviewsElms.getElementsByTag("a").last();
			if(numReviewElm != null){
				//(24)
				String numReviews = numReviewElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					String numReviewStr = m.group(1).trim();
					return Integer.parseInt(numReviewStr);		
				}
			}
		}catch(Exception e){
			LOGGER.warn("Error getting number of reviews");
		}		
		return -1;
	}

	protected double getReviewRating(Element productRow)
	{
		// TODO Auto-generated method stub
		try{
			if(productRow == null)
				return -1;
			Element reviewRatings = productRow.getElementsByClass("rating").first();
			if(reviewRatings == null)
				return -1;
			Element reviewRatingElm = reviewRatings.getElementsByTag("a").first();
			if(reviewRatingElm != null){
				String reviews = reviewRatingElm.text();
				Matcher m = reviewPat.matcher(reviews);
				if(m.find()){
					double total = Double.parseDouble(m.group(2).trim());
					if(total != 5){
						LOGGER.warn("Invalid max review rating: " + total +", review string: " + reviews);
						return -1;
					}
					double reviewCount = Double.parseDouble(m.group(1).trim());
					return reviewCount;					
				}
			}
		}catch(Exception e){
			LOGGER.warn("Error getting number of reviews");
		}

		return -1;
	}
	

	private void parseIndProducts(ProductSummary base, List<ProductSummary> products){
		Document doc = Utils.connect(base.getUrl(), cookies, getRetailerId());
		Elements productRows = doc.getElementsByClass("product");
		for(Element productRow : productRows){
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = base.getCategoryId();
			b.categoryName = base.getCategoryName();
			b.name = getIndProductName(productRow);;
			b.price = getIndProductPrice(productRow);
			b.url = base.getUrl();
			b.desc = base.getDesc();
			b.imageUrl = base.getImageUrl();
			b.model = base.getModel();
			b.reviewRating = base.getReviewRating();
			b.numReviews = base.getNumReviews();
			b.salesRank = base.getSalesRank();
			b.downloadTime = new Date();
			ProductSummary prod = b.build();
			
			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product - Sub: " + prod);
			}			
			products.add(prod);				
		}
	}
	
	private boolean isMultiProductPage(String priceStr){
		if(priceStr == null)
			return false;
		//i.e. sale 29.99-$209.99
		Matcher match = multi.matcher(priceStr);
		if(match.matches())
			return true;
		return false;
	}
	
	private double getIndProductPrice(Element indProductRow){	
		try{
			Element priceBox = indProductRow.getElementsByClass("item_collection_right").first().getElementsByClass("pricebox").first();
			Element priceElm = getMatching(priceBox, new String[]{"sale", "regular"});
			String priceTxt = priceElm.text();
			if(priceTxt.equals(AVAILABLE_IN_CART))
				return PriceTypes.AVAILABLE_IN_CART.getValue();
			return PriceFormatter.formatDollarPrice(priceElm.text());
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return PriceTypes.UNKNOWN.getValue();
	}
	
	private String getIndProductName(Element indProductRow){
		try{
			return indProductRow.getElementsByClass("item_collection_left").first().text();
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}
	
	protected boolean isSortedByBestSeller(Document doc, int categoryId){
		try{
			Element sortElement = doc.getElementById("select-sort");
			Element selected = sortElement.getElementsByAttributeValue("selected", "selected").first();
			if(selected.text().trim().equalsIgnoreCase("Best Sellers"))
				return true;
		}catch(Exception e){
			LOGGER.error("Error getting best seller rank");
		}
		return false;
	}
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		urlNew.append("&S=3");
		return urlNew.toString();		
	}
	
	private Map<String, String> getNameUrlMap(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("product-info").first().getElementsByTag("a").first();
			String name = helper.text();
			String url = helper.absUrl("href");
			Map<String, String> map = new HashMap<>(1);
			map.put(name, url);
			return map;
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}
	
	private double getPrice(int categoryId, String name, String url, Element productRow){
		try{			
			Element prodInfo = productRow.getElementsByClass("product-info").first();
			if(prodInfo == null){
				return PriceTypes.UNKNOWN.getValue();
			}
			boolean isSalePrice = false;
			Element priceElm = prodInfo.getElementsByClass("sale_add").first();
			String priceTxt = "";
			if(priceElm != null){
				priceTxt = priceElm.text();
				if(priceTxt != null && priceTxt.trim().length() > 0){					
					isSalePrice = true;
				}
			}
			if(!isSalePrice){
				priceElm = prodInfo.getElementsByClass("price-original").first();
				if(priceElm != null){
					priceTxt = priceElm.text();
				}
			}
			if(isMultiProductPage(priceTxt)){
				//price for multiple products belonging to the same url is being recorded as IS_MULTI 
				//as a url identifies a product uniquely and multiple products belonging to the same url can't be
				//uniquely identified to determine the product histories.
				//TODO: Get prices for the multiple products and come up with methodology to identify all the products uniquely
				return PriceTypes.IS_MULTI.getValue();
			}
			if(priceTxt.equals(AVAILABLE_IN_CART))
				return PriceTypes.AVAILABLE_IN_CART.getValue();
			double price = PriceFormatter.formatDollarPrice(priceTxt);
			return price;
			//The following check leads us to hit the kohls website close to 65k times extra hence disabling
			/*if(isSalePrice)
				return price;
			LOGGER.info("(" + categoryId + ")" + name + ": fetching product url " + url + " to determine if displayed price is actual price");
			//If it is not sale price, we need to go to the product detail page to determine the price as some of the regular prices
			//are not selling price and the selling price is obtained by adding product to cart;
			Document doc = Utils.connect(url, cookies, getRetailerId());
			if(doc == null){
				LOGGER.warn("unable to parse the product url, so price is unknown");
				return PriceTypes.UNKNOWN.getValue();
			}
			Element e = doc.getElementsByClass("smsale").first();
			if(e != null){
				String txt = e.text();
				if(txt != null && txt.trim().equalsIgnoreCase("For final price, add this item to your shopping bag. Learn more."))
					return PriceTypes.AVAILABLE_IN_CART.getValue();
				return PriceTypes.UNKNOWN.getValue();
			}
			return price;*/
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return PriceTypes.UNKNOWN.getValue();
	}
	
	private String getImgUrl(Element productRow){
		try{
			return productRow.getElementsByTag("img").first().absUrl("src");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}
	private String getModel(Element elm){
		return "";
	}
	private String getDesc(Element elm){
		return "";
	}
		

	@Override
	protected String getNextURL(Document doc){
		try{
			Element linkElement = doc.getElementsByClass("view-indicator").first();
			Element next = linkElement.getElementsByAttributeValue("rel", "next").first();
			if(next == null){
				next = linkElement.getElementsByClass("next-set").first();
				if(next == null)
					return null;
			}
			return next.getElementsByTag("a").first().absUrl("href");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//TODO: take into consideration the extra products we get when price is shown as a range.
			//Viewing 196 of 145 products 1 2
			String recordCountStr = doc.getElementsByClass("view-indicator").first().text();
			Pattern p = Pattern.compile(".*of(.*)products");
			Matcher m = p.matcher(recordCountStr);
			if (m.find()) {
				String txt = m.group(1).trim().replaceAll(",", "");
			    recordCount = Integer.parseInt(txt);
			}
			
		}
		catch(Exception e){
			LOGGER.error("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new KohlsParser();
		String url = "http://www.kohls.com/kohlsStore/bedroom/comforters1/bedding_coordinates/contemporary.jsp?ENDECA_SEARCH_INPUT%3C%3ENe=&ENDECA_SEARCH_INPUT%3C%3ENo=0&ENDECA_SEARCH_INPUT%3C%3ENs=p_Product_Total_Inventory%7C1&ENDECA_SEARCH_INPUT%3C%3ENtt=&ENDECA_SEARCH_INPUT%3C%3Erecs=61&ENDECA_SEARCH_INPUT%3C%3EN=4294648829&ENDECA_SEARCH_INPUT%3C%3Etype=2&FOLDER%3C%3Efolder_id=2534374757579772&bmForm=guided_nav_search&bmFormID=1328686038320&bmUID=1328686038320";
		url = "http://www.kohls.com/kohlsStore/bedroom/comforters1/bedding_coordinates/contemporary.jsp?ENDECA_SEARCH_INPUT%3C%3ENe=&ENDECA_SEARCH_INPUT%3C%3ENo=0&ENDECA_SEARCH_INPUT%3C%3ENs=p_Product_Total_Inventory%7C1&ENDECA_SEARCH_INPUT%3C%3ENtt=&ENDECA_SEARCH_INPUT%3C%3Erecs=61&ENDECA_SEARCH_INPUT%3C%3EN=4294648829&ENDECA_SEARCH_INPUT%3C%3Etype=2&FOLDER%3C%3Efolder_id=2534374757579772&bmForm=guided_nav_search&bmFormID=1328686038320&bmUID=1328686038320";
		url = "http://www.kohls.com/catalog/for-the-home-furniture.jsp?CN=3000001558&N=3000001558+4294732267";
		url = "http://www.kohls.com/catalog/mens-jeans-pants-and-shorts-jeans-loose.jsp?CN=3000000906+4294736508";
		url = "http://www.kohls.com/catalog/kids-baby-nursery-art-and-decor.jsp?CN=3000000233";
		url = "http://www.kohls.com/catalog/toys-costumes-and-dress-up.jsp?CN=3000000825";
		url = "http://www.kohls.com/catalog/for-the-home-fine-dining.jsp?CN=3000001502";
		url = "http://www.kohls.com/catalog/home-decor-furniture-decor.jsp?CN=4294719786+4294719779";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());		
	}
}
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
import parsers.html.MacysClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class MacysParser extends ProductsParser {
	
	private ClassNames htlmClasses = new MacysClassNames();
	//Multi product 
	private static final Pattern multi = Pattern.compile("\\$(.*) - (.*)");
	//Sale $59.99 - 229.99
	private static final Logger LOGGER = Logger.getLogger(MacysParser.class);
	public MacysParser() {
		super(Retailer.ID.MACYS);		
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htlmClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int count){
		Element helper = null;
		Elements productRows = doc.getElementsByClass(htlmClasses.productDesc());
		
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		
		for(Element productRow: productRows){			
			String name = "";
			String desc = "";
			double price = PriceTypes.UNKNOWN.getValue();
			String url = "";
			String imageUrl = "";
			String model = "";
			
			helper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
			if(helper != null){
				helper = helper.getElementsByTag("a").first();
				name = helper.ownText();
				url = helper.attr("abs:href");
			}
			else
				LOGGER.error("No element found for class " + htlmClasses.productName()[0] + " in " + productRow.html() );
			helper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(helper != null)
				imageUrl = helper.attr("src");
			else
				LOGGER.error("No element found for class " + htlmClasses.productImage() + " in " + productRow.html());
			helper = productRow.getElementsByClass(htlmClasses.price()[0]).first();
			String temp = null;
			Element priceSaleElm = helper.getElementsByClass("priceSale").first();
			if(priceSaleElm == null){
				priceSaleElm = helper.getElementsByTag("span").first();
			}
			if(priceSaleElm != null){
				temp = priceSaleElm.html();
				if(temp != null){
					int index = temp.indexOf("$");
					if(index != -1){					
						temp = temp.substring(index);	
						Matcher match = multi.matcher(temp);
						if(match.matches()){
							//price for multiple products belonging to the same url is being recorded as IS_MULTI 
							//as a url identifies a product uniquely and multiple products belonging to the same url can't be
							//uniquely identified to determine the product histories.
							//TODO: Get prices for the multiple products and come up with methodology to identify all the products uniquely
							LOGGER.info("product with multiple prices - " + categoryId + ":" + url);
							price = PriceTypes.IS_MULTI.getValue();
						}
						else
							price = PriceFormatter.formatDollarPrice(temp);
					}
					else if(temp.equalsIgnoreCase("On Sale")){
						//products with price listed as "On Sale" also have multiple products associated with them
						//http://www1.macys.com/shop/womens-clothing/charter-club-womens-clothing?id=11427&edge=hybrid&_escaped_fragment_=true
						price = PriceTypes.IS_MULTI.getValue();
					}
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
				LOGGER.warn("Invalid Product: " + prod);
			}
			products.add(prod);						
		}
		return products;
	}
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?_escaped_fragment_=true");
		}
		else
			urlNew.append("&_escaped_fragment_=true");
		
		return urlNew.toString();		
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected double getReviewRating(Element product) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	protected int getNumReviews(Element product) {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * There is no next URL as we get a snapshot of all the products as mandated by "Google crawling of Ajax sites"
	 * @param doc
	 * @return
	 */
	@Override
	protected String getNextURL(Document doc){
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new MacysParser();
		String url = "http://www1.macys.com/shop/mattresses/mattresses-by-brand/stearns-foster?id=25946&edge=hybrid";
		url = "http://www1.macys.com/shop/womens-clothing/style-co-womens-clothing?id=29630&edge=hybrid&_escaped_fragment_=true";
				
		parser.parseAndSave(0, "NA", url, new NullProductStore());
	}


}
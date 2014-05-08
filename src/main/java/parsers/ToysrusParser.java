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
import parsers.html.ToysrusClassNames;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.UtilityLogger;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class ToysrusParser extends ProductsParser {
	
	private ClassNames htlmClasses = new ToysrusClassNames();
	private final static Logger LOGGER = Logger.getLogger(ToysrusParser.class);
	private static final Pattern prodCntPatt = Pattern.compile("Showing.*of (.*) results");
//	//4.1 out of 5 Stars
	private static final Pattern reviewPatt = Pattern.compile("px.*-(.*)px");
//	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)\\)");
	
	public ToysrusParser() {
		super(Retailer.TOYSRUS.getId());
	}
	
	public ToysrusParser(String retailer) {
		super(retailer);
		if(!retailer.equals(Retailer.TOYSRUS.getId()) && !retailer.equals(Retailer.BABYSRUS.getId())){
			throw new IllegalArgumentException("Invalid retailer - " + retailer);
		}
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htlmClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		Element helper = null;
		Elements productRows = doc.getElementsByClass(htlmClasses.productRows()[0]);
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		for(Element productRow: productRows){
			String name;
			String desc = null;
			double price = PriceTypes.UNKNOWN.getValue();
			String url;
			String imageUrl;
			String model = null;
				
			helper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
			name = helper.ownText();
			url = helper.absUrl("href");
			productRow.getElementsByClass("bubLyr2").remove();
			
			helper = productRow.getElementsByTag("img").first();
			imageUrl = helper.attr("abs:src");
			
			//some pages have products listed like 'terminal' page but has no prices.
			Elements priceElements = productRow.getElementsByClass(htlmClasses.price()[0]);
			if(priceElements.size()==0){
				UtilityLogger.logWarning(name + " has no price tag, we see few of these on toysrus");
				continue;
			}
			helper = priceElements.first();
			String temp = helper.ownText();
			int index = temp.indexOf("$");
			index++;
			temp = temp.substring(index);
			price = Double.parseDouble(temp);
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
			Element reviewElm = product.getElementsByClass("prStars").first();
			if(reviewElm != null){
				//5* 0px-180px and rest scale w.r.t 180 
				//"padding: 0pt; background-position: 0px -180px;background-image: url(/pwr/engine/images/stars_small.gif)
				String reviews = reviewElm.attr("style");
				Matcher m = reviewPatt.matcher(reviews);
				if(m.find()){
					double total = Integer.parseInt(m.group(1).trim());
					if(total > 180.0){
						LOGGER.warn("Invalid max review rating: " + total +", review string: " + reviews);
						return -1;
					}
					return total*5/180.0;					
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		return -1;
	}

	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?view=all");
		}
		else{
			urlNew.append("&view=all");
		}
		
		return urlNew.toString();		
	}

	@Override
	protected String getNextURL(Document doc){
		Element helper = doc.getElementsByClass(htlmClasses.nextUrl()).first();
		if(helper == null)
			return null;
		else
		{
			helper = helper.parent();
			return helper.absUrl("href");
		}
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			String recordCountStr = doc.getElementsByClass("showingText").first().text();
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
			Element sortForm = doc.getElementById("sortForm");
			if(sortForm == null)
				return false;
			Element selected = sortForm.getElementsByAttributeValue("selected", "selected").first();
			if(selected == null)
				return false;
			if("Best Selling".equalsIgnoreCase(selected.text() == null?"":selected.text().trim()))
					return true;
		}catch(Exception e){
			return false;
		}
		LOGGER.equals("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}

	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new ToysrusParser();
		parser.parseAndSave(0, "NA", "http://www.toysrus.com/family/index.jsp?categoryId=3123177", new NullProductStore());
	}
}
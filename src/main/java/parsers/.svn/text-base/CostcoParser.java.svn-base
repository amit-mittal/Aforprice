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
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

//TODO: For some categories, costco has only one product and currently this parser is not 
//able to handle that scenario as the html tags for that single product is different when
//compared with more than one products.
public class CostcoParser extends ProductsParser {
	private static final Logger logger = Logger.getLogger(CostcoParser.class);
	private static final Pattern productCountPattern = Pattern.compile("Showing results.*of (.*).");
	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)\\)");

	public CostcoParser() {
		super(Retailer.ID.COSTCO);
	}
	
	@Override
	protected ClassNames getClassNames(){
		return null;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Elements productRows = doc.getElementsByClass("product-tile");
		if(productRows.size()==0)
			return products;	
		for(Element productRow: productRows){		
			String imageUrl = getImageUrl(productRow);
			String name = getName(productRow);
			String url = getUrl(productRow);
			double price = getPrice(productRow, name);
			String model = getModel(productRow);
			
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = price;
			b.url = url;
			b.desc = null;
			b.imageUrl = imageUrl;
			b.model = model;
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();
			ProductSummary p = b.build();
			if(!ProductUtils.isProductValid(p)){
				logger.warn("Invalid Product: " + p);
			}			
			products.add(p);
		}//for(Element productRow: productRows){ ends...
		return products;
	}
	
	protected int getNumReviews(Element product)
	{
		try{
			if(product == null)
				return -1;
			Element numReviewsElm = product.getElementsByClass("product-review-count").first();
			if(numReviewsElm != null){
				//(24)
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					return Integer.parseInt(m.group(1).trim());		
				}
			}
		}catch(Exception e){
			logger.error("Error getting number of reviews", e);
		}
		return -1;
	}

	protected double getReviewRating(Element productRow)
	{
		return -1;
	}

	protected boolean isSortedByBestSeller(Document doc, int categoryId){
		return false;
	}
	
	private double getPrice(Element elm, String name) {
		try{
			Element helper = elm.getElementsByClass("currency").first();
			String temp = helper.text();
			if(temp != null && temp.trim().length() > 0)
				return PriceFormatter.formatDollarPrice(temp);
			//some names have price information e.g.
			//http://www.costco.com/exclusive-jewelry.html
			if(name.contains("-$")){
				temp = name.substring(name.indexOf("-$") - 1);
				double p = PriceFormatter.formatDollarPrice(temp);
				if(!PriceTypes.isInvalidType(p))
					return p;
			}
			Element form = elm.getElementsByAttributeValue("action", "ManageShoppingCartCmd").first();
			if(form != null){
				return getPriceSpecial(form, "submit");
			}
			form = elm.getElementsByAttributeValue("action", "ProductDisplay").first();
			if(form != null){
				return getPriceSpecial(form, "button");
			}

		}catch(Throwable e){
			logger.error("Unable to get price", e);
		}
		return PriceTypes.UNKNOWN.getValue(); 
	}
	
	private double getPriceSpecial(Element form, String buttonClass){
		if(form != null){
			Element button = form.getElementsByClass(buttonClass).first();
			if(button != null){
				String text = button.text();
				if(text != null){
					if(text.equals("More Details"))
						return PriceTypes.NOT_AVAILABLE.getValue();
					if(text.equals("Add for Details"))
						return PriceTypes.AVAILABLE_IN_CART.getValue();
				}
			}
		}
		return PriceTypes.UNKNOWN.getValue();
	}

	protected String getName(Element elm){
		Element helper = elm.getElementsByClass("product-tile-image-container").first();
		helper = helper.getElementsByAttribute("href").first();
		String name = helper.text();
		return name;
	}
	
	protected String getUrl(Element elm){
		Element helper = elm.getElementsByClass("product-tile-image-container").first();
		helper = helper.getElementsByAttribute("href").first();
		String url = helper.absUrl("href");
		return url;
	}
	
	protected String getDescription(Element elm) {
		throw new UnsupportedOperationException();
	}

	protected String getModel(Element elm) {//not available on product page	
		return null;
	}
	
	private String getImageUrl(Element elm){
		Element helper = elm.getElementsByClass("product-tile-image-container").first();
		helper = helper.getElementsByTag("img").first();
		String imageUrl = helper.attr("abs:src");
		return imageUrl;
	}


	@Override
	protected String getNextURL(Document doc){
		//only see all products on one page, couldn't find example where products are on more than one page
		return null;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			Elements spans = doc.getElementsByTag("span");
			for(Element span: spans){
				if(span.text().trim().startsWith("Showing results")){
					//Showing results 1 – 9 of 9.
					String recordCountStr = span.text();
					Matcher m = productCountPattern.matcher(recordCountStr);
					if (m.find()) {
					    recordCount = Integer.parseInt(m.group(1).trim());
					    return recordCount;
					}		
				}
			}
		}
		catch(Exception e){
			logger.warn("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new CostcoParser();
		String url = "http://www.costco.com/laundry-suites.html";
		url = "http://www.costco.com/computer-speakers.html";
		url = "http://www.costco.com/exclusive-jewelry.html";
		url = "http://www.costco.com/french-door-refrigerators.html";
		url = "http://www.costco.com/37-inch-tvs-through-50-inch-tvs.html";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());		
	}
}
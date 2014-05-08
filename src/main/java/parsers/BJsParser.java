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


public class BJsParser extends ProductsParser {
	private static final Logger logger = Logger.getLogger(BJsParser.class);
	private static final Pattern prodCnt = Pattern.compile("(.*)items.*");
	private static final Pattern reviewPatt = Pattern.compile(".*rating_(.*)");
	private static final Pattern numReviewPatt = Pattern.compile("(\\d+).*reviews.*\\)");
	public BJsParser() {
		super(Retailer.BJS.getId());
	}
	
	@Override
	protected ClassNames getClassNames(){
		return null;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Elements temp = doc.getElementsByAttributeValue("id", "products");
		if(temp.size()==0)
			return products;
		Element productParent = temp.first();
		Elements productRows = productParent.getElementsByClass("item");
		if(productRows.size()==0)
			return products;	
		for(Element productRow: productRows){		
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = getName(productRow);
			b.price = getPrice(productRow);;
			b.url = getUrl(productRow);
			b.imageUrl = getImageUrl(productRow);
			b.desc = "";
			b.model = getModel(productRow);
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
	
	private double getPrice(Element elm) {
		try{
			Element helper = elm.getElementsByClass("price").first();
			String temp = helper.text();
			double price = PriceFormatter.formatDollarPrice(temp);
			return price;
		}catch(Throwable e){
			logger.error("Unable to get price", e);
			return PriceTypes.UNKNOWN.getValue();
		}
	}

	protected String getName(Element elm){
		Element helper = elm.getElementsByClass("text").first();
		helper = helper.getElementsByAttribute("href").first();
		String name = helper.text();
		return name;
	}
	
	protected String getUrl(Element elm){
		Element helper = elm.getElementsByClass("text").first();
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
		Element helper = elm.getElementsByTag("img").first();
		String imageUrl = helper.attr("abs:src");
		return imageUrl;
	}
	
	protected double getReviewRating(Element product){
		try{
			if(product == null)
				return -1;
			Element reviewElm = product.getElementsByClass("pwrStarRating").first();
			if(reviewElm != null){
				//pwrStarRating rating_4_5
				String reviewStr = reviewElm.attr("class");
				Matcher m = reviewPatt.matcher(reviewStr);
				if(m.find()){
					double rating = Double.parseDouble(m.group(1).trim().replace("_", "."));
					if(rating > 5){
						logger.warn("Review rating more than 5: " + reviewStr);
						return -1;
					}
					return rating;
				}
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
			Element numReviewsElm = product.getElementsByClass("review").first();
			if(numReviewsElm != null){
				//(7 reviews)
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

	@Override
	protected String getNextURL(Document doc){
		//only see all products on one page, couldn't find example where products are on more than one page
		Element helper = doc.getElementsByClass("numnav").first();
		if(helper==null)
			return null;
		helper = helper.getElementsByClass("numeric").first();
		boolean nextEntryIsNextUrl = false;
		String url=null;
		for(Element numericChild : helper.children()){
			if(numericChild.hasClass("on")){
				nextEntryIsNextUrl=true;
				continue;
			}
			if(nextEntryIsNextUrl){
				if(numericChild.hasAttr("href")){
					url = numericChild.getElementsByAttribute("href").first().absUrl("href");
				}
				else
					logger.error("no href entry after 'on' class entry under 'numeric' class, weird, can't get nexturl");
				break;
			}
		}
		return url;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//153 items found for	" PC Games	 "
			String recordCountStr = doc.getElementsByClass("resultFound").first().text();
			Matcher m = prodCnt.matcher(recordCountStr);
			if (m.find()) {
			    recordCount = Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e){
			logger.warn("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		//Available sorting: Relevance, Price, Alphabetical
		return false;
	}


	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new BJsParser();
		String url = "http://www.bjs.com/televisions/29--under.category.34001.178.2000153.1";
		url="http://www.bjs.com/software/pc-games.category.359.752.2001251.1";
		url = "http://www.bjs.com/electronics/gps-navigation.category.198.160.2001020.1";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());		
	}
}
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
import parsers.html.RiteaidClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class RiteaidParser extends ProductsParser {
	
	private static final Logger LOGGER = Logger.getLogger(RiteaidParser.class);	
	private static final ClassNames htlmClasses = new RiteaidClassNames();
	//5 stars
	private static final Pattern reviewPatt = Pattern.compile("(\\d+).*stars");
	public RiteaidParser() {
		super(Retailer.RITEAID.getId());
	}

	/**
	 * 
	 * @param doc
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max) {	
		Elements productRows = doc.getElementsByClass(htlmClasses.productRows()[0]);
		List<ProductSummary> products = new ArrayList<ProductSummary>();		
		for(Element productRow: productRows){			
			String name = "";
			String desc = "";
			double price = PriceTypes.UNKNOWN.getValue();
			String url = "";
			String imageUrl = "";
			String model = ""; //Model is not available
				
			Element nameHelper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
			if(nameHelper != null){
				Element nameHref = nameHelper.getElementsByTag("a").first();				
				if(nameHref != null){
					name = nameHref.ownText();
					url = nameHref.absUrl("href");
				}
			}
			Elements priceElms = getFirstMatching(productRow, htlmClasses.price());
			if(priceElms != null && priceElms.size() >=2 ){
				Element priceHelper = priceElms.get(1);
				if(priceHelper != null){										
					String priceStr = priceHelper.text();
					if(priceStr != null)
						price = PriceFormatter.formatDollarPrice(priceStr.trim());
				}
			}
			Element imgHelper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(imgHelper != null){
				imgHelper = imgHelper.getElementsByTag("img").first();
				if(imgHelper != null)
					imageUrl = imgHelper.attr("abs:src");				
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
			Element reviewElm = product.getElementsByClass("reviewSummary").first();
			if(reviewElm != null){
				Element ratingElm = reviewElm.getElementsByClass("rating").first();
				if(ratingElm != null){
					String ratingStr = ratingElm.text().trim();
					Matcher m = reviewPatt.matcher(ratingStr);
					if(m.find()){
						double rating = Integer.parseInt(m.group(1).trim());
						if(rating > 5){
							LOGGER.warn("Invalid review rating: " + rating +", rating string: " + ratingStr);
							return -1;
						}
						return rating;					
					}
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		//Number of reivews not available
		return -1;
	}
	

	@Override
	protected String getNextURL(Document doc) {
		Element paginationURLElm = doc.getElementsByClass(htlmClasses.nextUrl()).first();
		if(paginationURLElm == null){
			LOGGER.warn("No next page element found in the document");
			return null;
		}
		Element urlElm = paginationURLElm.getElementsByTag("a").first();
		if(urlElm == null){
			LOGGER.warn("url element is null in " + paginationURLElm.html());
		}
		return urlElm.absUrl("href");
	}


	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?searchSize=72");
		}
		else if( url.indexOf("ic=") == -1 ){
			urlNew.append("&searchSize=72");
		}
		
		return urlNew.toString();		
	}

	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		try{
			Element searchRankElm = doc.getElementById("searchRank");
			if(searchRankElm != null){
				Element selected = searchRankElm.getElementsByAttribute("selected").first();
				String value = selected.attr("value");
				if(value != null && value.equalsIgnoreCase("salesRank"))
					return true;
			}
		}catch(Exception e){
			LOGGER.warn("Error while getting sort order", e);
		}
		LOGGER.equals("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}

	@Override
	protected ClassNames getClassNames() {
		return htlmClasses;
	}
	
	public static void main(String[] args) throws Exception{
		String url = "http://www.riteaidonlinestore.com/Medicine-Health/b/3003326011?ie=UTF8&title=Medicine+%26+Health";
		url = "http://www.riteaidonlinestore.com/Diapers-Baby-Mom/b/3070378011?ie=UTF8&title=Diapers";
		ProductsParser parser = new RiteaidParser();
		parser.parseAndSave(0, "NA", url, new NullProductStore());
	}

}

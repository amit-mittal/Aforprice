package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.NordstormClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class NordstormParser extends ProductsParser{

	private ClassNames htmlClasses = new NordstormClassNames();
	private static final Logger LOGGER = Logger.getLogger(NordstormParser.class);
	
	public NordstormParser() {
		super(Retailer.NORDSTROM.getId());
	}
	
	@Override
	protected ClassNames getClassNames() {
		return htmlClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int count){
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Elements productRows = doc.getElementsByClass(getClassNames().productRows()[0]);
		Element helper = null;
		
		for(Element product: productRows) {
			String name="", url="", imageUrl="", desc = "", model = "";
			double price = PriceTypes.UNKNOWN.getValue();
			
			helper = product.getElementsByClass(getClassNames().productName()[0]).first();
			if(helper != null) {
				name = helper.ownText();
				url = helper.absUrl("href");
			}
			
			helper = product.getElementsByClass(getClassNames().productImage()).first();
			if(helper != null) {
				helper = helper.getElementsByTag("img").first();
				if(helper != null)
					imageUrl = helper.absUrl("src");
			}
			
			helper = product.getElementsByClass(getClassNames().price()[0]).first();
			if(helper != null) {
				price = PriceFormatter.formatDollarPrice(helper.ownText().trim());
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
			b.reviewRating = getReviewRating(product);
			b.numReviews = getNumReviews(product);
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
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//1,149
			String recordCountStr = doc.getElementsByClass("count").first().text();
			recordCountStr = recordCountStr.replaceAll(",", "");
			recordCount = Integer.parseInt(recordCountStr);
		}
		catch(Exception e){
			LOGGER.warn("Can not find product count for category", e);
		}
		return recordCount;
	}

	
	@Override
	protected String getNextURL(Document doc){
		Element helper = doc.getElementsByClass(getClassNames().nextUrl()).first();
		if(helper != null) {
			if(helper.hasClass("disabled"))
				return null;
			else {
				helper = helper.getElementsByTag("a").first();
				if(helper != null)
					return helper.absUrl("href");
				else
					return null;
			}
		}
		return null;
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

	public static void main(String[] args) throws Exception{
		ProductsParser parser = new NordstormParser();
		String url = "http://shop.nordstrom.com/c/womens-pumps?origin=leftnav#category=b60139935&type=category&page=2&size=&width=&color=&price=&brand=&instoreavailability=false&lastfilter=&sizeFinderId=8&resultsmode=&segmentId=0&partial=1&pagesize=100";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());
	}
}

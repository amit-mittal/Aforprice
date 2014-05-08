package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.SearsClassNames;
import parsers.html.SearsClassNames2;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.UtilityLogger;
import util.Utils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class SearsParser extends ProductsParser {
	private static final Logger logger = Logger.getLogger(SearsParser.class);
	private ClassNames htmlClasses1 = new SearsClassNames();
	private ClassNames htmlClasses2 = new SearsClassNames2();
	private ClassNames curhtmlClasses;

	public SearsParser() {
		super(Retailer.SEARS.getId());
		cookies.put("zipCode", "84123");
		cookies.put("RSPULocInfo", "%7B%22zipCode%22%3A%2284123%22%2C%22userLocation%22%3A%2284123%22%2C%22latitude%22%3A%2240.658361%22%2C%22longitude%22%3A%22-111.91921%22%2C%22preferredStore%22%3A%22%22%2C%22deliveryCharge%22%3A69.99%7D");
//		RSPUZone, "1"
//		regionCookie, "770"
//		zipCode10153, 84123
	}
	
	@Override
	protected ClassNames getClassNames(){
		return curhtmlClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		Element helper = null;
		curhtmlClasses = htmlClasses1;
		Elements productRows = doc.getElementsByClass("mainInfo");
//		Elements productRows = doc.getElementsByAttributeValue("id", curhtmlClasses.productRows()[0]);
//		doc.get
		if(productRows.size()==0){//try with second tag group
			curhtmlClasses = htmlClasses2;
			productRows = doc.getElementsByClass(curhtmlClasses.productRows()[0]);
			productRows = doc.getElementsByClass("mainInfo");
		}
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		for(Element productRow: productRows){
			String name;
			String desc = null;
			double price = PriceTypes.NOT_AVAILABLE.getValue();
			String url;
			String imageUrl;
			String model = null;
			//todo: we are not able to get back products with id=ars-products, this bug needs to be fixed
			//url:http://www.sears.com/shc/s/dap_10153_12605_DAP_Hot+Appliance+Deals
			
			Elements temp1 = productRow.getElementsByClass(curhtmlClasses.productName()[0]);
			if(temp1.size() == 0){
				UtilityLogger.logError("Unable to parse products with tag " + curhtmlClasses.productRows() );
				return products;
			}
			helper = temp1.first().getElementsByAttribute("href").first();
			name = helper.ownText();
			url = helper.absUrl("href");
			
			helper = productRow.getElementsByTag("img").first();
			imageUrl = helper.attr("abs:src");
			
			//some pages have products listed like 'terminal' page but has no prices.
			Elements priceElements = productRow.getElementsByClass(curhtmlClasses.price()[0]);
			//if can't find price using first tag (sale price) then try second one (regular price)
			
			if(priceElements.size()==0 ){
				priceElements = productRow.getElementsByClass(curhtmlClasses.price()[0]);
				if(priceElements.size()==0 ){						
					UtilityLogger.logWarning(name + " has no price tag, we see few of these on sears");
					continue;
				}
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
				logger.warn("Invalid Product: " + prod);
			}
			products.add(prod);						
		}
		return products;
	}

	@Override
	protected String getNextURL(Document doc){
		Element helper = doc.getElementsByAttributeValue("id", htmlClasses1.nextUrl()).first();
		if(helper == null)
			return null;
		else
		{
			helper = helper.getElementsByAttribute("href").first();
			return helper.absUrl("href");
		}
	}
	
	public static void main(String[] args) throws Exception{
		logger.info("Running sears parser test");
		ProductsParser parser = new SearsParser();
		String url = "http://www.sears.com/shc/s/s_10153_12605_Automotive_Tires_View+All";
		//why does below url not work?
		url = "http://www.sears.com/automotive-tires/c-1020100?sName=View%20All&pageNum=2&viewItems=25&keywordSearch=false";
//		url = "http://www.sears.com/automotive-tires/c-1020100?sName=View%20All&pageNum=3&viewItems=25&keywordSearch=false";
		url = "http://www.sears.com/shc/s/dap_10153_12605_DAP_Hot+Appliance+Deals#";
//		url="http://www.sears.com/shc/s/dap_10153_12605_DAP_Real+Deals+Auto#"; //this has ars-product-prices but doesn't work
		url = "http://www.sears.com/automotive/v-1020005?sbf=Shop Your Way MAX";
		//todo: below url doens't parse without zipcode, need to figure out cookies
		url = "http://www.sears.com/appliances-refrigerators-freezerless-refrigerators&Kenmore/s-1020316?filter=Brand&viewItems=25&keywordSearch=false";
		url = "http://www.sears.com/sports-coverage-miami-hurricanes-drape-82-x-84/p-SPM3938497701";
		Document doc = Utils.connect(url, parser.cookies, parser.getRetailerId());
//		Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
//		url = parser.getNextURL(doc);
//		doc = Jsoup.connect(url).userAgent("Mozilla").get();
		parser.parseAndSave(1000, "NA", url, new NullProductStore());
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
}
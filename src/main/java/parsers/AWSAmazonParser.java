package parsers;

import global.errorhandling.ErrorCodes;
import helper.Validate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db.dao.DAOException;

import parsers.html.AWSAmazonClassNames;
import parsers.html.ClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import stores.ProductStore;
import util.ProductUtils;
import util.SignedRequestHandler;
import util.Utils;
import entities.ProductSummary;
import entities.Retailer;

public class AWSAmazonParser extends ProductsParser {
	
	private static final String AWS_ACCESS_KEY_ID = "AKIAJWXLVHIMPKIZ346Q";
	private static final String AWS_SECRET_KEY = "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";
	private static final String AWS_ASSOCIATE_TAG = "ravede-20";
	
	private static final Logger LOGGER = Logger.getLogger(AWSAmazonParser.class);
	/*
	 * Region wise endpoints:
	 * 		US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
	 */
	private static final String ENDPOINT = "ecs.amazonaws.com";
	
	private ClassNames htmlClasses = new AWSAmazonClassNames();
	
	public AWSAmazonParser() {
		super(Retailer.AWSAMAZON.getId());
	}
	
	@Override
	protected ClassNames getClassNames() {
		return htmlClasses;
	}
	
	public String ConstructAwsQuery(String BrowseNodeId, int ItemPageNo) {
		SignedRequestHandler helper;
        try {
            helper = SignedRequestHandler.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrl = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("Operation", "ItemSearch");
        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
        params.put("BrowseNode", BrowseNodeId);
        params.put("ResponseGroup", "Large");
        /*
         * SearchIndex - Does not make any difference to the result set
         * when the BrowseNode Id is specified. Learned from experimentation.
         * But is necessary for the query.
         */
        params.put("SearchIndex", "Appliances");
        params.put("ItemPage", Integer.toString(ItemPageNo));

        requestUrl = helper.sign(params);
        return requestUrl;
	}
	
	private int getMaxPages(Document doc) {
		Element helper = doc.getElementsByTag("TotalPages").first();
		return Integer.parseInt(helper.ownText());
	}
	
	@Override
	public void parseAndSave(int categoryId, String categoryName, String categoryURL, ProductStore productStore) throws IOException {
		try {
			if(skipCategory(categoryName))
				return;
			Validate.notNull(productStore, ErrorCodes.INVALID_INPUT);
			Validate.notNull(categoryURL, ErrorCodes.INVALID_INPUT);
			int pgNo = 1;
			visitedURLs = new HashMap<String, String>();
			String categoryRequest = ConstructAwsQuery(categoryName, pgNo);
			Document doc = Utils.connect(categoryRequest, cookies, getRetailerId());
			List<ProductSummary> products = parseAndSaveHtml(categoryURL, doc, categoryId, categoryName, -1, pgNo);
			saveProducts(productStore, products, categoryURL, categoryId, categoryName);
			int maximumPages = getMaxPages(doc);
			++pgNo;
			while(pgNo <=10 && pgNo <= maximumPages) {
				categoryRequest = ConstructAwsQuery(categoryName, pgNo);
				doc = Utils.connect(categoryRequest, cookies, getRetailerId());
				products = parseAndSaveHtml(categoryURL, doc, categoryId, categoryName, -1, pgNo);
				saveProducts(productStore, products, categoryURL, categoryId, categoryName);
				pgNo++;
			}
		}finally {
			clearCache(categoryName);
			System.gc();
		}
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int count){
		Element helper = null;
		Elements productRows = doc.getElementsByTag(htmlClasses.productRows()[0]);
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		
		for(Element product: productRows) {
			String name="", url="", imageUrl="", desc = "", model = "";
			double price = PriceTypes.UNKNOWN.getValue();
			
			//get name
			helper = product.getElementsByTag(htmlClasses.productName()[0]).first();
			if(helper != null)
				name = helper.ownText();
			
			//get URL
			helper = product.getElementsByTag(htmlClasses.productName()[1]).first();
			if(helper != null)
				url = helper.ownText();
			
			//get image URL
			helper = product.getElementsByTag(htmlClasses.productImage()).first();
			if(helper != null) {
				helper = helper.getElementsByTag("URL").first();
				if(helper != null)
					imageUrl = helper.ownText();
			}
			
			//get Description
			Elements descElements = product.getElementsByTag(htmlClasses.productDesc());
			desc = "<ul>";
			for(Element descriptionEle: descElements) {
				desc = desc + "<li>" + descriptionEle.ownText() +"</li>";
			}
			desc = desc+"</ul>";
			
			//get model
			helper = product.getElementsByTag(htmlClasses.model()[0]).first();
			if(helper != null)
				model = helper.ownText();
			
			//get Price
			helper = product.getElementsByTag(htmlClasses.price()[0]).first();
			if(helper != null) {
				helper = helper.getElementsByTag("FormattedPrice").first();
				if(helper != null)
					price = PriceFormatter.formatDollarPrice(helper.ownText());
			} else {
				helper = product.getElementsByTag(htmlClasses.price()[1]).first();
				if(helper != null) {
					helper = helper.getElementsByTag("FormattedPrice").first();
					if(helper != null)
						price = PriceFormatter.formatDollarPrice(helper.ownText());
				}
			}
			ProductSummary prod = new ProductSummary(getRetailerId(), categoryId, categoryName, name, price, url, imageUrl, desc, model);
			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product: " + prod);
			}
			products.add(prod);
		}
		return products;
	}
	
	/*
	 * No necessity of nextUrl as it is constructed from the page number itself.
	 */
	@Override
	protected String getNextURL(Document doc){
		return null;
	}
	
	public static void main(String[] args) throws DAOException {
		try {
		ProductsParser parser = new AWSAmazonParser();
		parser.parseAndSave(0, "5805441011", "unknown", new NullProductStore());
		} catch(IOException e) {
			e.getStackTrace();
		}
	}

	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected double getReviewRating(Element product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNumReviews(Element product) {
		// TODO Auto-generated method stub
		return 0;
	}
}

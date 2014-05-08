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
import parsers.html.StaplesClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class StaplesParser extends ProductsParser {
	private ClassNames htmlClasses = new StaplesClassNames();
	private static final Logger LOGGER = Logger.getLogger(StaplesParser.class);
	private static final Pattern prodCntPatt = Pattern.compile("^.*of(.*)items");
	private static final Pattern prodCntPatt1 = Pattern.compile("(\\d+) items");
	
	public StaplesParser() {
		super(Retailer.STAPLES.getId());
	}
	
	@Override
	protected ClassNames getClassNames() {
		return htmlClasses;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max) {
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Element prodCatalog = doc.getElementById("productDetail");
		if( prodCatalog == null) 
			return products;
		Elements productRows = prodCatalog.getElementsByClass("prd");
		Element helper = null;
		
		for(Element product: productRows) {
			String name = "", url = "", imageUrl = "", desc = "";
			double price = PriceTypes.UNKNOWN.getValue();
			
			//get imageUrl
			helper = product.getElementsByClass("pic").first();
			if(helper != null){
				helper = helper.getElementsByTag("img").first();
				if(helper != null)
					imageUrl = helper.absUrl("src");
			}
			
			//get name and url
			helper = product.getElementsByClass("name").first();
			if(helper != null){
				helper = helper.getElementsByClass("url").first();
				if(helper != null){
					url = helper.absUrl("href");
					name = helper.text();
				}
			}
			helper = product.getElementsByClass("pricenew").first();
			if(helper != null){
				helper = helper.getElementsByClass("theprice").first();
				if(helper != null){
					helper = helper.getElementsByClass("pis").first();
					if(helper != null){						
						price = PriceFormatter.formatDollarPrice(helper.text().trim());
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
			b.desc = desc;
			b.imageUrl = imageUrl;
			b.model = getModel(product);
			b.reviewRating = getReviewRating(product);
			b.numReviews = getNumReviews(product);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();
			if(!ProductUtils.isProductValid(prod))
				LOGGER.warn("Invalid Product:" + product);
			products.add(prod);
			
		}
		return products;
	}
	
	protected int getNumReviews(Element product)
	{
		try{
			Element numReviewsElm = product.getElementsByClass("votes").first();
			if(numReviewsElm!= null ){ 
				String numTxt = numReviewsElm.text();
				return Integer.parseInt(numTxt);
			}
		}
		catch(Exception e){
			LOGGER.error("Error getting number of reviews: " + e.getMessage());
		}		
		return -1;

	}
	
	private String getModel(Element prod){
		if(prod == null)
			return "";
		String model = prod.attr("mnum");
		return model == null?"":model;
	}

	protected double getReviewRating(Element product){
		try{
			Element reviewRatingElm = product.getElementsByClass("rating").first();
			if(reviewRatingElm!= null ){ 
				String reviewTxt = reviewRatingElm.text();
				return Double.parseDouble(reviewTxt);
			}
		}
		catch(Exception e){
			LOGGER.error("Error getting number of reviews: " + e.getMessage());
		}		
		return -1;
	}

	@Override
	protected String getNextURL(Document doc, String currentURL, String categoryURL, int currPage){
		Element productGrid = doc.getElementById("productgrid");
		if(productGrid == null)
			return null;
		Element paginationElm = productGrid.getElementsByClass("perpage").first();
		if(paginationElm == null)
			return null;
		Elements lists = paginationElm.getElementsByTag("li");
		int maxPage = -1;
		for(Element list: lists){
			String pgNoStr = list.text();
			try{
				int pgNo = Integer.parseInt(pgNoStr.trim());
				if(pgNo > maxPage)
					maxPage = pgNo;
			}
			catch(Exception e){				
			}			
		}
		if(currPage >= maxPage)
			return null;
		return categoryURL + "?pagenum=" + (currPage + 1);
	}
	
	@Override
	protected String getNextURL(Document doc){
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected boolean skipCategory(String name) {
		return false;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		try{
			Elements perPageElms = doc.getElementsByClass("perpage");
			for(Element perPage: perPageElms){
				Element note = perPage.getElementsByClass("note").first();
				if(note == null)
					continue;
				//1 - 24 of 29 items
				String recordCountStr = note.text();
				
				Matcher m = prodCntPatt.matcher(recordCountStr);
				if (m.find()) {
				    return Integer.parseInt(m.group(1).trim());
				}
				else{
					m = prodCntPatt1.matcher(recordCountStr);
					if(m.find()){
						return Integer.parseInt(m.group(1).trim());
					}
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Can not find product count for category", e);
		}
		return 0;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId){
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new StaplesParser();
		//parser.parseAndSave(1, "unknown", "http://www.staples.com/Paper-Towels-Plates-Cups-Cutlery/cat_CL141826", new stores.NullProductStore());
		String url = "http://www.staples.com/Scanners-PCs-Drives-Accessories/cat_CL158948";
		//url = "http://www.staples.com/Laptop-Sleeves/cat_CL164769";
		url = "http://www.staples.com/High-Intensity-Discharge-Light-Bulbs/cat_CL165714";
		url = "http://www.staples.com/Laptops/cat_CL167289";
		url = "http://www.staples.com/Laptops/cat_CL167289";
		parser.parseAndSave(1, "unknown", url, new stores.NullProductStore());		
	}
}

package parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.AmazonClassNames;
import parsers.html.ClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import stores.ProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class AmazonParser extends ProductsParser {
	
	private ClassNames htmlClasses = new AmazonClassNames();
	
	private Logger LOGGER = Logger.getLogger(AmazonParser.class);

	public AmazonParser() {
		super(Retailer.ID.AMAZON);		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htmlClasses;
	}
	
	/* <div class="product result">
	 * 		<div class="image">
	 * 			<a href="relative url">
	 * 				<img src="relative image url" alt="" />
	 * 			</a>
	 * 		</div>
	 * 		<h3 class="title">
	 * 			<a href="relative url">NAME OF PRODUCT</a>
	 * 		</h3>
	 * 		<span class="price">$<first/main price></span>
	 * 		<span class="price">$<price for limited new></span>
	 * 		<span class="price">$<price for limited used></span>
	 * 		<div class="sss">eligible for<span class="sssFree"> FREE </span><span class="sssLastLine">super shipping</span>
	 * </div>
	 */
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		int count = 0;
		Elements productRows = doc.getElementsByClass("product");
		if(productRows == null || productRows.size() == 0){
			LOGGER.info(pageUrl + " is other format");
			productRows = doc.getElementsByClass("prod");
			return parse2(pageUrl, productRows, categoryId, categoryName, max);
		}
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		for(Element productRow: productRows) {
			String name = "", url = "";
			Map<String, String> nameUrlMap = getNameUrlMap(productRow);
			for(Map.Entry<String, String> entry: nameUrlMap.entrySet()){
				name = entry.getKey();
				url = entry.getValue();
			}
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = getPrice(productRow);
			b.url = url;
			b.imageUrl = getImgUrl(productRow);
			b.desc = getDesc(productRow);
			b.model = getModel(productRow);
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();		

			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product("+pageUrl+"): " + prod);
			}
			products.add(prod);
			if(max > 0 && ++count >= max)
				break;
		}
		
		return products;
	}
	
	public List<ProductSummary> parse2(String pageUrl, Elements productRows, int categoryId, String categoryName, int max){
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		int count = 0;
		for(Element productRow: productRows) {
			String name = "", url = "";
			Map<String, String> nameUrlMap = getNameUrlMap2(productRow);
			for(Map.Entry<String, String> entry: nameUrlMap.entrySet()){
				name = entry.getKey();
				url = entry.getValue();
			}
			ProductSummary prod = new ProductSummary(getRetailerId(), categoryId, categoryName, name, getPrice2(productRow), url, getImgUrl2(productRow), getDesc(productRow), getModel(productRow));
			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product("+pageUrl+"): " + prod);
			}
			products.add(prod);
			if(max > 0 && ++count >= max)
				break;
		}
		
		return products;

	}
	
	
	private double getPrice(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("newPrice").first().getElementsByClass("price").first();			
			return PriceFormatter.formatDollarPrice(helper.ownText().trim());
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return PriceTypes.UNKNOWN.getValue();
	}

	private double getPrice2(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("rsltGridList").first().getElementsByClass("bld").first();			
			return PriceFormatter.formatDollarPrice(helper.ownText().trim());
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return PriceTypes.UNKNOWN.getValue();
	}

	private Map<String, String> getNameUrlMap(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("title").last();
			// get name and url
			String url = helper.absUrl("href");
			String name = helper.text();
			Map<String, String> map = new HashMap<>(1);
			map.put(name, url);
			return map;
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}
	
	private Map<String, String> getNameUrlMap2(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("newaps").first();
			helper = helper.getElementsByTag("a").first();
			// get name and url
			String url = helper.absUrl("href");
			String name = helper.text();
			Map<String, String> map = new HashMap<>(1);
			map.put(name, url);
			return map;
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}

	private String getImgUrl(Element productRow){
		try{
			Element helper = productRow.getElementsByClass("image").first();
			helper = helper.getElementsByTag("img").first();
			return helper.attr("abs:src");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	private String getImgUrl2(Element productRow){
		return getImgUrl(productRow);
	}

	private String getModel(Element productRow){
		return "";
	}
	
	private String getDesc(Element productRow){
		return "";
	}
	
	/*	activated next link!
	 * 	<span class="pagnNext">
	 * 		<a href="next page relative url" class="pagnNext" title="next">Next >></a>
	 * 	</span>
	 * deactivated link!
	 * 	<span class="pagnDisabled">Next >></span>
	 */
	@Override
	protected String getNextURL(Document doc){
		try{
			Element nextUrlElement = doc.getElementsByClass("pagnRA").first();
			if(nextUrlElement != null)
				return nextUrlElement.getElementsByClass("pagnNext").first().absUrl("href");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	protected boolean skipCategory(String name) {
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new AmazonParser();
		String url = "http://www.amazon.com/s/ref=sr_pg_1?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A541966%2Cn%3A300334%2Cp_85%3A2470955011&bbn=300334&ie=UTF8&qid=1353809093";
		url = "http://www.amazon.com/Projectors-Monitors-Computer-Add-Ons-Computers/b/ref=amb_link_360192002_19?ie=UTF8&node=300334&pf_rd_m=ATVPDKIKX0DER&pf_rd_s=left-2&pf_rd_r=01CHAKGQGN5KJRJEGYCG&pf_rd_t=101&pf_rd_p=1375375042&pf_rd_i=1266092011";
		url = "http://www.amazon.com/s/ref=lp_300334_pg_2?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A541966%2Cn%3A300334&page=2&ie=UTF8&qid=1353816265";
		url = "http://www.amazon.com/s/ref=sr_nr_p_n_condition-type_0?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A541966%2Cn%3A300334%2Cp_76%3A1249137011%2Cp_n_condition-type%3A2224371011&bbn=300334&ie=UTF8&qid=1353894698&rnid=2224369011";
		ProductStore store = new NullProductStore();
		parser.parseAndSave(0, "Unknown", url, store);
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
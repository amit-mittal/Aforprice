package parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.WalmartClassNames;
import parsers.util.PriceFormatter;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.ProductUtils;
import util.Utils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class AmazonMostSellingParser extends ProductsParser {
	
	private static final Logger LOGGER = Logger.getLogger(AmazonMostSellingParser.class);
	private static final Pattern reviewPatt = Pattern.compile("(.*)out.*of(.*)stars*");
	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)\\)");
	private static final Pattern prodCntPatt = Pattern.compile(".*-(.*)");
	
	private ClassNames htlmClasses = new WalmartClassNames();

	public AmazonMostSellingParser() {
		super(Retailer.AMAZONBESTSELLER.getId());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htlmClasses;
	}

	/**
	 * @param doc
	 * @param categoryId
	 * @param categoryName
	 * @param category
	 * @return
	 */
	@Override
	public List<ProductSummary> parse(String pageurl, Document doc, int categoryId, String categoryName, int startRank, int max){		
		Elements productRows = Utils.getFirstMatching(doc, new String[]{"zg_itemWrapper", "zg_itemRow"});
		List<ProductSummary> products = new ArrayList<ProductSummary>();		
		for(Element productRow: productRows){
			Element p = productRow.getElementsByClass("zg_item_compact").first(); 
			if( p!= null){
				//Some of the listings (amazon apps) have two products in one row, one which is free and 
				//the other which is paid. We are interested only in the paid ones.
				productRow = p; 
			}
			String name = "";
			String url = "";			
			Map<String, String> nameUrl = getNameUrlMap(productRow);
			for(Map.Entry<String, String> entry: nameUrl.entrySet()){
				name = entry.getKey();
				url = entry.getValue();
				break;
			}
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = getPrice(productRow);
			b.url = url;
			b.imageUrl = getImageUrl(productRow);
			b.desc = getDesc(productRow);
			b.model = getModel(productRow);
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
	
	private Map<String, String> getNameUrlMap(Element elm){
		try{			
			Element helper = elm.getElementsByClass("zg_title").first().getElementsByTag("a").first();
			String name = helper.ownText();
			String url = helper.absUrl("href");
			Map<String, String> data = new HashMap<>(1);
			data.put(name, url);
			return data;
		}catch(Exception e){
			LOGGER.error("Error getting name url map: " + e.getMessage());			
		}		
		return Collections.emptyMap();
	}
	
	private double getPrice(Element elm){
		try{
			Element helper = elm.getElementsByClass("price").first();
			if(helper != null)
				return PriceFormatter.formatDollarPrice(helper.text());
			helper = elm.getElementsByClass("zg_availability").first();
			if(helper != null && helper.text() != null && (helper.text().startsWith("Currently unavailable") || 
															helper.text().startsWith("Sign up to be notified when this item becomes available") || 
															helper.text().startsWith("Out of Print--Limited Availability")))
				return PriceTypes.NOT_AVAILABLE.getValue();
			//Get the full price block
			helper = elm.getElementsByClass("zg_itemPriceBlock_normal").first();
			if(helper != null){
				Element clickToSee = helper.getElementsContainingText("Click to see price").first();
				if(clickToSee != null)
					return PriceTypes.AVAILABLE_IN_CART.getValue();
			}
		}
		catch(Exception e){
			LOGGER.equals("Error getting price: " + e.getMessage());
		}
		return PriceTypes.UNKNOWN.getValue();
	}
	
	private String getImageUrl(Element elm){
		try{
			Element imgElm = elm.getElementsByClass("zg_image").first();
			Element image = imgElm.getElementsByTag("img").first();
			if(image != null)
				return image.absUrl("src");
		}catch(Exception e){
			LOGGER.error("Error getting image url: " + e.getMessage());
		}
		return "";
	}
	
	private String getDesc(Element elm){
		return "";
	}
	
	private String getModel(Element elm){
		return "";
	}
	
	protected double getReviewRating(Element product){
		try{
			if(product == null)
				return -1;
			Element reviewElm = product.getElementsByClass("zg_reviews").first();
			
			if(reviewElm != null){
				reviewElm = reviewElm.getElementsByClass("swSprite").first();
				if(reviewElm == null)
					return -1;
				//3.3 out of 5 stars
				String reviews = reviewElm.getElementsByClass("swSprite").first().getElementsByAttribute("title").first().attr("title");
				Matcher m = reviewPatt.matcher(reviews);
				if(m.find()){
					int total = Integer.parseInt(m.group(2).trim());
					if(total != 5){
						LOGGER.warn("Invalid max review rating: " + total +", review string: " + reviews);
						return -1;
					}
					double reviewCount = Double.parseDouble(m.group(1).trim());
					return reviewCount;					
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting review rating", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		try{
			if(product == null)
				return -1;
			Element numReviewsElm = product.getElementsByClass("crAvgStars").first();
			if(numReviewsElm != null){
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					String reviewTxt = m.group(1).trim().replaceAll(",", "");
					return Integer.parseInt(reviewTxt);		
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting number of reviews", e);
		}
		return -1;
	}

	
	@Override
	protected String getNextURL(Document doc){
		try{
			Element pagination = doc.getElementsByClass("zg_pagination").first();
			Element selected = pagination.getElementsByClass("zg_selected").first();
			String selectedId = selected.attr("id");
			int page = Integer.parseInt(selectedId.substring(selectedId.length() - 1));
			Element next = pagination.getElementById("zg_page" + (page + 1));
			if(next == null)
				return null;
			return next.getElementsByTag("a").first().absUrl("href");
		}catch(Exception e){
			LOGGER.error("Error getting next url: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		return true;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		try{
			Element paginationWrapper = doc.getElementById("zg_paginationWrapper");
			if(paginationWrapper == null)
				return UNIMPLEMENTED;
			Elements pages = paginationWrapper.getElementsByClass("zg_page");
			if(pages == null || pages.size() == 0)
				return UNIMPLEMENTED;
			Element lastPage = pages.last();
			String pageToFromTxt = lastPage.text();
			Matcher m = prodCntPatt.matcher(pageToFromTxt);
			if (m.find()) {
			    return Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e){
			LOGGER.warn("Can not find product count for category", e);
		}
		return UNIMPLEMENTED;	
	}
		
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new AmazonMostSellingParser();
		String url = "http://www.amazon.com/gp/bestsellers/videogames/402051011/ref=pd_zg_hrsr_vg_1_4_last";
		url = "http://www.amazon.com/Best-Sellers-Shoes-Boys-Football/zgbs/shoes/3420530011/ref=zg_bs_3420530011_pg_2/175-0740814-5123059";
		url = "http://www.amazon.com/Best-Sellers-Appstore-Android-City-Info-Apps/zgbs/mobile-apps/2478840011/ref=zg_bs_nav_mas_1_mas/175-0740814-5123059";
		url = "http://www.amazon.com/Best-Sellers-Toys-Games/zgbs/toys-and-games/ref=zg_bs_toys-and-games_home_all?pf_rd_p=1286242342&pf_rd_s=center-1&pf_rd_t=2101&pf_rd_i=home&pf_rd_m=ATVPDKIKX0DER&pf_rd_r=0G21N04K21TY703WAXWC";
		url = "http://www.amazon.com/Best-Sellers-Books-Future-Computing/zgbs/books/3571/ref=zg_bs_nav_b_4_69766/181-8355871-8255017";
		url = " http://www.amazon.com/Best-Sellers-Kindle-Store-Childrens-Superhero-Books/zgbs/digital-text/155750011/ref=zg_bs_nav_kstore_4_155217011/186-4381291-0756709";
		url = "http://www.amazon.com/Best-Sellers-Appstore-Android-Kids-Apps/zgbs/mobile-apps/2478846011/ref=zg_bs_nav_mas_1_mas/192-6761500-2378509";
		url = "http://www.amazon.com/Best-Sellers-Industrial-Scientific-Round-Spacers-Standoffs/zgbs/industrial/16413331/ref=zg_bs_nav_indust_3_16413321/178-5189742-3065966";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());
	}
	
}

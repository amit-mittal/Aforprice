package parsers;

import java.net.URL;
import java.net.URLDecoder;
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
import parsers.html.WalgreensClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class WalgreensParser extends ProductsParser {
	
	private static final Logger LOGGER = Logger.getLogger(WalgreensParser.class);	
	private static final ClassNames htlmClasses = new WalgreensClassNames();

	private static final Pattern prodCntPatt = Pattern.compile("(\\d*)");
	//4.1 out of 5 Stars
	private static final Pattern reviewPatt = Pattern.compile(".*rating-(.*)\\.gif.*");
	private static final Pattern numReviewPatt = Pattern.compile("(\\d+).*reviews");
	
	//2/$5.00 or 1/$2.59
	private static final Pattern priceMulti = Pattern.compile("2/\\$(.*) or 1/\\$(.*)");

	public WalgreensParser() {
		super(Retailer.WALGREENS.getId());
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
			Element imgHelper = productRow.getElementsByClass(htlmClasses.productImage()).first();
			if(imgHelper != null){
				imgHelper = imgHelper.getElementsByTag("img").first();
				if(imgHelper != null){
					imageUrl = imgHelper.attr("abs:src");
					if(imageUrl == null || imageUrl.trim().equals("")){
						imageUrl = imgHelper.attr("abs:sl_src");
					}
				}
			}			
			Element descHelper = productRow.getElementsByClass(htlmClasses.productDesc()).first();
			if(descHelper != null){
				desc = descHelper.text();
			}
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = getPrice(productRow);
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
	
	private double getPrice(Element product){
		double price = PriceTypes.UNKNOWN.getValue();
		try{
			Element priceHelper = getPriceElement(product);
			if(priceHelper != null){	
				String priceStr = priceHelper.text();
				if(priceStr != null){
					priceStr = priceStr.trim();
					if(priceStr.length() > 1){
						if(priceStr.equals("Priced per store")){
							return PriceTypes.IN_STORE_ONLY.getValue();
						}
						Matcher m = priceMulti.matcher(priceStr.trim());
						if(m.matches()){
							String price2 = m.group(1);
							try{
								price = Double.parseDouble(price2);
								if(!PriceTypes.isInvalidType(price))
									return price/2;
							}catch(Exception e){
							}
						}
						price = PriceFormatter.formatDollarPrice(priceHelper.html());
					}
				}
			}
		}catch(Exception e){
		}
		return price;
	}
	
	protected double getReviewRating(Element product){
		try{
			if(product == null)
				return -1;
			Element reviewElm = product.getElementsByClass("reviewSnippet").first();
			if(reviewElm != null){
				Element reviewImg = reviewElm.getElementsByTag("img").first();
				if(reviewImg != null){
					//http://www.walgreens.com/images/bvrating/rating-5_0.gif?Lo0P=e89c6b577df11d04fd3213bd54fdff60710
					String url = reviewImg.attr("sl_src");
					if(url != null){
						url = URLDecoder.decode(url, "UTF-8");
					}
					else{
						url = reviewImg.attr("src");
					}
					if(url != null){
						Matcher m = reviewPatt.matcher(url.trim());
						if(m.find()){
							String reviewStr = m.group(1).trim();
							double review = Double.parseDouble(reviewStr.replaceAll("_", "."));
							return review;
						}
					}
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected int getNumReviews(Element product){
		try{
			if(product == null)
				return -1;
			Element reviewElm = product.getElementsByClass("reviewSnippet").first();
			if(reviewElm != null){
				String numReviewsTxt = reviewElm.text();
				if(numReviewsTxt != null){
						Matcher m = numReviewPatt.matcher(numReviewsTxt.trim());
						if(m.find()){
							return Integer.parseInt(m.group(1).trim());
						}
					
				}
			}
		}catch(Exception e){
			LOGGER.error("Error getting number of reviews", e);
		}
		return -1;
	}


	@Override
	protected String getNextURL(Document doc) {
		Element paginationURLs = doc.getElementById("pagination");;
		if(paginationURLs == null){
			LOGGER.warn("No next page element found in the document");
			return null;
		}
		Element nextPageUrl = paginationURLs.getElementsByAttributeValue("title", "Next Page").first();
		if(nextPageUrl != null){
			return nextPageUrl.absUrl("href");
		}
		LOGGER.warn("No next page element found in " + paginationURLs.html());
		return null;
	}

	@Override
	protected ClassNames getClassNames() {
		return htlmClasses;
	}
	
	@Override	
	protected String transformUrl(String url){
		//TODO: Start getting 96 
		return url;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//405  Items
			String recordCountStr = doc.getElementsByClass("item-count").first().text().replaceAll(",", "");
			Matcher m = prodCntPatt.matcher(recordCountStr);
			if (m.find()) {
			    recordCount = Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e){
			LOGGER.error("Can not find product count for category", e);
		}
		return recordCount;
	}
	
	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		try{
			Element sortElm = doc.getElementById("sortSelectTop");
			if(sortElm != null){
				Element sortSelection = sortElm.getElementsByAttribute("selected").first();
				if(sortSelection != null){
					String text = sortSelection.text();
					if(text != null && text.trim().equalsIgnoreCase("Top Sellers"))
						return true;
				}
			}
		}catch(Exception e){
			LOGGER.error("Error while getting sort order", e);
		}
		LOGGER.equals("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		String url = "http://www.walgreens.com/store/store/category/productlist.jsp?Erp=96&N=362040&Eon=362040";
		url = "http://www.walgreens.com/store/c/perfume/ID=360439-tier3";
		ProductsParser parser = new WalgreensParser();
		parser.parseAndSave(0, "NA", url, new NullProductStore());
	}

}
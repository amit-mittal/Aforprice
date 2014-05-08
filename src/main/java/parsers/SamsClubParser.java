package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class SamsClubParser extends ProductsParser {
	private static final String SAMSCLUB_OFFSET_PARAM_NAME = "offset=";

	private static final int SAMSCLUB_OFFSET = 12;

	private static final Logger logger = Logger.getLogger(SamsClubParser.class);

	private static final Pattern prodCntPatt = Pattern.compile(".*of(.*)Page.*");

	public SamsClubParser() {
		super(Retailer.ID.SAMSCLUB);
		cookies.put("myNeighboringClubs", "4730|6686|4718|6682");
		cookies.put("myPreferredClub", "6683");
		cookies.put("noOfRecordsPerPage", "80");
	}
	
	@Override
	protected ClassNames getClassNames(){
		return null;
	}
	
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		//all products under first entry of 'brdrB' class
		//each product has class smMargT
			//image src is under 'imgCol' class
			//name and url under ContentCol class, look for href element
			//look at each child of contentCol and pick the one with Item text to get item (model) #
			//price is under orangePriceBox2 class -> price-side class
		
		if( doc.getElementsByClass("shelfItems").size() == 0 ) return products;
		
		Elements productRows = doc.getElementsByClass("shelfItems").first().children();

		for(Element productRow: productRows){
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = getName(productRow);
			b.price = getPrice(productRow);
			b.url = getUrl(productRow);
			b.imageUrl = getImageUrl(productRow);
			b.desc = getDescription(productRow);
			b.model = getModel(productRow);;
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
			String priceText = elm.getElementsByClass("moneyBoxBtn").first().getElementsByClass("leftVal").text();
			double price = PriceFormatter.formatDollarPrice(priceText);
			return price;
		}catch(Throwable e){
			logger.error("Unable to get price", e);
		}
		return PriceTypes.UNKNOWN.getValue();
	}

	protected String getName(Element elm){
		Element helper = elm.getElementsByClass("prodTitle").first();
		helper = helper.getElementsByAttribute("href").first();
		String name = helper.text();
		return name;
	}
	
	protected String getUrl(Element elm){
		String url = elm.getElementsByClass("prodTitle").first().getElementsByAttribute("href").first().absUrl("href");
		return url;
	}
	
	protected String getDescription(Element elm) {
		//Start getting description
		return "";
	}

	protected String getModel(Element elm) {
		//TODO: Fix as this is not returning the right model number.
		/*Element helper = elm.getElementsByClass("ContentCol").first();
		for(Node child : helper.childNodes()){
			if(child instanceof Element){
				if(((Element) child).text().contains("Item #")){
					Element modelElm = (Element)child;
					Elements ps = modelElm.getElementsByTag("p");
					for(Element p: ps){
						String t = p.ownText();
						if(t != null && t.contains("Item #")){
							return t;
						}
					}
					
				}
			}
		}*/
		return "";
	}
	
	private String getImageUrl(Element elm){
		String imageUrl = elm.getElementsByClass("shelfProdImg").first().attr("src");
		return imageUrl;
	}
	
	protected double getReviewRating(Element product){
		//reviewrating is available as image, so not getting that. 
		return -1;
	}
	
	protected int getNumReviews(Element product){
		//there is no review rating, hence skipping this.
		return -1;
	}


	@Override	
	protected String transformUrl(String url){
		if( !url.contains("?"))
			url = url + "?" + SAMSCLUB_OFFSET_PARAM_NAME + "1";
		else
			url = url + "&" + SAMSCLUB_OFFSET_PARAM_NAME + "1";
			
		return url;		
	}

	@Override
	protected String getNextURL(Document doc, String currentURL, String categoryURL)
	{
		int productCount = getProductCountForCategory(doc);
		
		String currentOffset = currentURL.substring(currentURL.indexOf(SAMSCLUB_OFFSET_PARAM_NAME)+7, currentURL.length() );
		if(currentOffset.contains("&"))
			currentOffset = currentOffset.substring(0, currentOffset.indexOf("&"));
		
		String newOffSet = String.valueOf(Integer.valueOf(currentOffset) + SAMSCLUB_OFFSET);
		
		String urlWithoutOffset = currentURL.substring(0, currentURL.indexOf(SAMSCLUB_OFFSET_PARAM_NAME));
		
		int beginoffset = currentURL.indexOf(SAMSCLUB_OFFSET_PARAM_NAME);
		int offsetend = currentURL.indexOf("&", beginoffset);
		if(offsetend == -1) offsetend = currentURL.length();
		
		String urlBegin = currentURL.substring(0, beginoffset);
		String urlEnd = "";
		if(offsetend != currentURL.length())
			urlEnd = currentURL.substring(offsetend, currentURL.length());
		
		if(Integer.parseInt(newOffSet) > productCount)
			return null;
		else
			return urlBegin + SAMSCLUB_OFFSET_PARAM_NAME + newOffSet + urlEnd;
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//1-22 of 22  items
			String recordCountStr = doc.getElementsByClass("shelfPaginate").first().text();
			//In the items string, the white spaces are not spaces.
			recordCountStr = recordCountStr.replaceAll(" ", " ");
			Matcher m = prodCntPatt.matcher(recordCountStr);
			if (m.find()) {
			    recordCount = Integer.parseInt(m.group(1).trim());
			}
		}
		catch(Exception e){
			logger.error("Can not find product count for category", e);
		}
		return recordCount;
	}

	@Override
	protected boolean isSortedByBestSeller(Document doc, int catId){
		try{
			Element sortElm = doc.getElementsByClass("shop-sortSystem").first();
			if(sortElm != null){
				Element sortedByElm = sortElm.getElementsByClass("active2").first();
				if(sortedByElm != null){
					String sortText = sortedByElm.text();
					if(sortText != null && sortText.trim().equalsIgnoreCase("Top Seller")){
						return true;
					}
				}
			}
		}catch(Exception e){
			logger.error("Error while getting sort order", e);
		}
		logger.warn("The products are not sorted in Best Sellers order for cat " + catId);
		return false;
	}

	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new SamsClubParser();
		String url = "http://www.samsclub.com/sams/scrapbooking/9198.cp?altQuery=&rootDimension=1009198&navAction=push&noOfRecordsPerPage=80";
		//url = "http://www.samsclub.com/sams/auto-fluids-auto-degreasers/1073.cp;jsessionid=AA5A54EE82E472FC287CC798B89BCE27.4109-ps5?altQuery=&rootDimension=1001073&navAction=push";
		url = "http://www.samsclub.com/sams/children-s-clothing/3010101.cp?altQuery=&rootDimension=4010101&navAction=push"; 
		//http://www.samsclub.com/sams/staples-stapler-supplies/1725.cp?altQuery=&rootDimension=1001725&navAction=push";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());

	}

	@Override
	protected String getNextURL(Document doc)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
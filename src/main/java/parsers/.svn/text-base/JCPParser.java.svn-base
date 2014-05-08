package parsers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.UtilityLogger;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class JCPParser extends ProductsParser
{
	private static final Logger logger = Logger.getLogger(JCPParser.class);
	private static final String HTTP_WWW_JCPENNEY_COM = "http://www.jcpenney.com";
	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)reviews\\)");
	private static final Pattern reviewPat = Pattern.compile("(.*)out of(.*)");

	public JCPParser()
	{
		super(Retailer.JCPENNY.getId());
	}
	
	@Override
	protected String getNextURL(Document doc, String currentURL, String categoryURL)
	{
		String[] tagVals = currentURL.split("&");
		int currentPageNumber = 1;
		for(String tagVal: tagVals)
		{
			if( tagVal.indexOf("pN=") != -1 )
			{
				currentPageNumber = Integer.parseInt(tagVal.split("=")[1]);
				break;
			}
		}
		Element topPagination = doc.getElementById("paginationIdTOP");
		if(topPagination == null)return null;
		Elements paginations = topPagination.getElementsByTag("li");
		boolean foundCurrent = false;
		for(Element pagination:paginations)
		{
			if(foundCurrent == true && pagination.text().trim().length() != 0)
			{
				int nextPage = ++currentPageNumber;
				return categoryURL + "&Nao=" + (nextPage-1) * 96 + "&pN=" + nextPage;
	    	}
			if(String.valueOf(currentPageNumber).equals(pagination.text().trim()))
				foundCurrent = true;
		}
		return null;
	}
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?pageSize=96");
		}
		else{
			urlNew.append("&pageSize=96");
		}				
		return urlNew.toString();		
	}

	
	
	protected String getNextURL(Document doc)
	{
		return null;
	}

	@Override
	public List<ProductSummary> parse(String pageUrl, Document pageDoc, int categoryId, String categoryName, int startRank, int count)
	{
		Elements productHolders = pageDoc.getElementsByClass("product_holder");
		List<ProductSummary> products = new LinkedList<>();
		for (Element productHolder : productHolders)
		{
     		try
	    	{
				String[] priceClasses = new String[] { "comparisonPrice", "gallery_page_price" };
				String price = "-1";

				for( String priceClass : priceClasses)
				{
					Elements priceElements = productHolder.getElementsByClass(priceClass);
					if(priceElements.size() != 0)
					{
						price = priceElements.first().getElementsByClass("def_cur").first().text();
						break;
					}
				}
				//price = price.replaceAll("\\$", "");
				double priceDouble =PriceFormatter.formatDollarPrice(price);
				
				String productName = productHolder.getElementsByClass("detail").first().text();
				String productURL = HTTP_WWW_JCPENNEY_COM
						+ productHolder.getElementsByClass("detail").first().childNodes().get(1).attr("href");
				String imageURL = productHolder.getElementsByClass("product_image").first().childNodes().get(1)
						.childNodes().get(1).attr("src");
				
				ProductSummaryBuilder b = new ProductSummaryBuilder();
				b.retailerId = getRetailerId();
				b.categoryId = categoryId;
				b.categoryName = categoryName;
				b.name = productName;
				b.price = priceDouble;
				b.url = productURL;
				b.desc = null;
				b.imageUrl = imageURL;
				b.model = null;
				b.reviewRating = getReviewRating(productHolder);
				b.numReviews = getNumReviews(productHolder);
				b.salesRank = startRank == -1?-1:startRank++;
				b.downloadTime = new Date();
				ProductSummary product = b.build();
				if(!ProductUtils.isProductValid(product))
					UtilityLogger.logWarning("Invalid Product:" + product);
				products.add(product);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				UtilityLogger.logInfo("Failed to Parse products for:" + categoryName);
			}
		}
		return products;
	}

	protected int getNumReviews(Element product)
	{
		try{
			if(product == null)
				return -1;
			Element numReviewsElm = product.getElementsByClass("gallery_rating").first();
			if(numReviewsElm != null){
				//(24)
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if(m.find()){
					String numReviewWithSpecialchar = m.group(1).trim();
					return Integer.parseInt(numReviewWithSpecialchar.substring(0, numReviewWithSpecialchar.length()-1));		
				}
			}
		}catch(Exception e){
			logger.error("Error getting number of reviews", e);
		}
		return -1;
		
	}

	protected double getReviewRating(Element productHolder)
	{
		try{
			if(productHolder == null)
				return -1;
			Element reviewElm = productHolder.getElementsByClass("gallery_rating").first();
			if(reviewElm != null){
				//4.1 out of 5 Stars
				Element reviewStrElm = reviewElm.getElementsByAttribute("alt").first();
				if(reviewStrElm == null)
					return -1;
				String reviews = reviewStrElm.attr("alt");
				Matcher m = reviewPat.matcher(reviews);
				if(m.find()){
					int total = Integer.parseInt(m.group(2).trim());
					if(total != 5){
						logger.warn("Invalid max review rating: " + total +", review string: " + reviews);
						return -1;
					}
					double reviewCount = Double.parseDouble(m.group(1).trim());
					return reviewCount;					
				}
			}
		}catch(Exception e){
			logger.error("Error getting review ratings", e);
		}
		return -1;
	}
	
	protected boolean isSortedByBestSeller(Document doc, int categoryId){
		return false;
	}	

	@Override
	protected ClassNames getClassNames()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	protected int getProductCountForCategory(Document doc)
	{
		int recordCount = 0;
		try
		{
			String recordCountStr = doc.getElementById("countvar").text();
			recordCount = Integer.parseInt(recordCountStr.trim());
		}
		catch(Exception e)
		{
			UtilityLogger.logWarning("Can not find product count for category");
			e.printStackTrace();
		}
		return recordCount;
	}	

	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new JCPParser();
		String url = "http://www.jcpenney.com//dotcom/for-the-home/categories/luggage-backpacks/cat.jump?id=cat100210003&deptId=dept20000011";
		url = "http://www.jcpenney.com/dotcom/men/clothing/big-tall/clothing/casual-shirts/cat.jump?id=cat100250140&subcatId=cat100250139&deptId=dept20000014";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());
	}

}
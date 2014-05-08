package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.TargetClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class TargetMobileParser extends ProductsParser
{
	private static final ClassNames htmlClasses = new TargetClassNames();
	private static final Logger LOGGER = Logger.getLogger(TargetMobileParser.class);
	private static final Pattern numReviewPatt = Pattern.compile("\\((.*)\\)");
	private static final Pattern reviewPat = Pattern.compile("stars-(.*)");

	public TargetMobileParser()
	{
		super(Retailer.TARGET_MOBILE.getId());
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassNames getClassNames()
	{
		return htmlClasses;
	}

	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max)
	{
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Elements productRows = doc.getElementsByClass(htmlClasses.productRows()[0]);
		Element helper = null;
		if (productRows.isEmpty() || productRows == null)
		{
			LOGGER.warn("No products: " + pageUrl);
			return products;
		}

		for (Element productRow : productRows)
		{
			String name = "", url = "", imageUrl = "", desc = "", model = "";
			double price = PriceTypes.UNKNOWN.getValue();
			try
			{
				helper = productRow.getElementsByTag("a").first();
				url = (helper == null ? "" : helper.absUrl("href"));

				helper = helper.getElementsByTag("img").first();
				imageUrl = helper == null ? "" : helper.absUrl("src");

				helper = productRow.getElementsByClass("price").first();
				if (helper != null)
				{
					String priceTxt = helper.ownText();
					if (priceTxt != null)
					{
						if (priceTxt.equalsIgnoreCase("out of stock") || priceTxt.equals("out of stock online"))
							price = PriceTypes.OUT_OF_STOCK.getValue();
						else if(priceTxt.equalsIgnoreCase("prices vary by store"))
							price = PriceTypes.IN_STORE_ONLY.getValue();
						else if(priceTxt.equalsIgnoreCase("price too low to display"))
							price = PriceTypes.AVAILABLE_IN_CART.getValue();
						else
							price = PriceFormatter.formatDollarPrice(helper.ownText());
					}
				}
				helper = productRow.getElementsByTag("h4").first();
				name = helper == null ? "" : helper.ownText();
			} catch (Exception e)
			{
				LOGGER.error(e.getMessage(), e);
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
			b.model = model;
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1 ? -1 : startRank++;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();
			if (!ProductUtils.isProductValid(prod))
				LOGGER.warn("Invalid Product:" + prod);
			products.add(prod);
		}
		return products;
	}
	

	protected int getNumReviews(Element productRow)
	{
		// TODO Auto-generated method stub
		try
		{
			if (productRow == null)
				return -1;
			Element numReviewsElm = productRow.getElementsByClass("review-num").first();

			if (numReviewsElm != null)
			{
				// (24)
				String numReviews = numReviewsElm.text();
				Matcher m = numReviewPatt.matcher(numReviews);
				if (m.find())
				{
					String numReviewStr = m.group(1).trim();
					return Integer.parseInt(numReviewStr);
				}
			}
		} catch (Exception e)
		{
			LOGGER.warn("Error getting number of reviews");
			return -1;
		}
		return -1;
	}

	protected double getReviewRating(Element productHolder)
	{
		try
		{
			if (productHolder == null)
				return -1;
			Element reviewElm = productHolder.getElementsByClass("rating").first();
			if (reviewElm != null)
			{
				Set<String> reviewElmClasses = reviewElm.classNames();
				for (String reviewElmClass : reviewElmClasses)
				{
					if (reviewElmClass.indexOf("stars") != -1)
					{
						Matcher m = reviewPat.matcher(reviewElmClass);
						if (m.find())
						{
							double reviewCount = Double.parseDouble(m.group(1).replace("-", ".").trim());
							return reviewCount;
						}
					}
				}
			}
		} catch (Exception e)
		{
			LOGGER.error("Error getting review ratings", e);
		}
		return -1;
	}

	@Override
	protected boolean skipCategory(String categoryName)
	{
		if (super.skipCategory(categoryName))
			return true;
		categoryName = categoryName.trim().toLowerCase();
		if (categoryName.indexOf("ways to") != -1 || categoryName.startsWith("clearance"))
		{
			LOGGER.info("Skipping category " + categoryName);
			return true;
		}
		return false;
	}
	
	@Override	
	protected String transformUrl(String url){
		StringBuilder urlNew = new StringBuilder(url);
		if(url.indexOf("?") == -1){
			urlNew.append("?type=products");
		}
		else{
			urlNew.append("&type=products");
		}
		
		return urlNew.toString();		
	}

	@Override
	protected String getNextURL(Document doc)
	{
		Element helper = doc.getElementsByClass("next-link").first();
		if (helper != null)
		{
			helper = helper.getElementsByTag("a").first();
			return helper.absUrl("href");
		} else
			return null;
	}

	@Override
	protected int getProductCountForCategory(Document doc)
	{
		int recordCount = 0;
		try
		{
			// showing 1-10 of 129 results
			String recordCountStr = doc.getElementsByClass("pagination-header").first().text();
			Pattern p = Pattern.compile("Showing.*of (.*) results");
			Matcher m = p.matcher(recordCountStr);
			if (m.find())
			{
				recordCount = Integer.parseInt(m.group(1).trim());
			}
		} catch (Exception e)
		{
			LOGGER.error("Can not find product count for category", e);
		}
		return recordCount;
	}

	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId)
	{
		try{
			Elements sortElements = doc.getElementsByClass("sort").first().getElementsByTag("a");
			for(Element sortElement:sortElements)
			{
				if(sortElement.classNames().contains("current"))
				{
					if(!sortElement.text().equalsIgnoreCase("best seller"))
						return false;
					else 
						return true;
				}
			}
		}catch(Exception e){
			LOGGER.error("can not find if sorted by best seller for categoryid=" + categoryId);
		}
		return false;
	}

	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new TargetMobileParser();
		String url = "http://m.target.com/c/conference-tables-home-office-furniture/-/N-5xtmw";
		url = "http://m.target.com/c/dresses-clothing-women/-/N-5xtcg";
		url = "http://m.target.com/c/office-chairs-home-furniture/-/N-5xtmt";
		url = "http://m.target.com/c/philosophy-see-more-genres-books/-/N-5csts";
		url = "http://m.target.com/c/frozen-breakfast-food-foods-grocery/-/N-5xsza";
		parser.parseAndSave(1, "unknown", url,
				new stores.NullProductStore());
	}
}

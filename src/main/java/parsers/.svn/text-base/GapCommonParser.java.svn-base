package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class GapCommonParser extends ProductsParser
{
	private static final Logger logger = Logger.getLogger("GapCommonParser");
	private static final Pattern nowPricePat = Pattern.compile(Pattern.quote("Now $") +"(.*)"); 



	public GapCommonParser(String retailer)
	{
		super(retailer);
	}

	@Override
	public ClassNames getClassNames()
	{
		return null;
	}

	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max)
	{
		int count = 0;
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		// check if individual category block exists. if yes, this is not a
		// products page.
		Elements individualCategoryBlocks = doc.getElementsByClass("categoryBlock");
		if (individualCategoryBlocks.size() != 0)
			return products;
		
		Elements productList = doc.getElementsByClass("productCatItem");
		for (Element product : productList)
		{
			try
			{
				String name = product.getElementsByClass("productItemName").first().text();
				Elements salePrice = product.getElementsByClass("priceDisplaySale");
				Elements salePriceAddenda = product.getElementsByClass("mupSupMessage");
				String priceStr = "";
				if (salePrice.size() != 0)
				{
					priceStr = salePrice.first().ownText();
				}
				else if( salePriceAddenda.size() != 0)
				{	
					String fullPriceStr = salePriceAddenda.first().ownText();
					Matcher m = nowPricePat.matcher(fullPriceStr);
					if(m.find())
						priceStr = m.group(1).trim();
				}
				else
					priceStr = product.getElementsByClass("priceDisplay").first().ownText();
					
				if(priceStr.trim().length() == 0 )
					priceStr = product.getElementsByClass("priceDisplay").text();
				double price = PriceFormatter.formatPrice(priceStr);
				String url = product.getElementsByClass("productItemName").first().absUrl("href");
				String imageUrl = product.getElementsByClass("imgDiv").first().getElementsByTag("img").first()
						.absUrl("src");
				ProductSummaryBuilder b = new ProductSummaryBuilder();
				b.retailerId = getRetailerId();
				b.categoryId = categoryId;
				b.categoryName = categoryName;
				b.name = name;
				b.price = price;
				b.url = url;
				b.imageUrl = imageUrl;
				b.desc = "";
				b.model = "";
				b.reviewRating = getReviewRating(product);
				b.numReviews = getNumReviews(product);
				b.salesRank = startRank == -1?-1:startRank++;
				b.downloadTime = new Date();
				ProductSummary prod = b.build();		

				if(!ProductUtils.isProductValid(prod))
					logger.warn(("Invalid Product: " + prod));
				
				products.add(prod);
				if(max > 0 && ++count >= max)
					break;
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.info("Failed to Parse products for:" + categoryName);
				logger.info("Elements with Issue" + product);
			}

		}
		return products;
	}

	@Override
	protected String getNextURL(Document doc)
	{
		return null;
	}

	@Override
	protected boolean skipCategory(String name)
	{
		return false;
	}

	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId){
		return false;
	}
	
	@Override
	protected double getReviewRating(Element product) {
		return -1;
	}

	@Override
	protected int getNumReviews(Element product) {
		return -1;
	}	
	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new GapCommonParser(Retailer.ID.GAP);
		String url = "http://www.gap.com/products/graphics-ts-tops-womens-C87974.jsp";
		parser.parseAndSave(1000, "NA", url, new NullProductStore());
	}


}

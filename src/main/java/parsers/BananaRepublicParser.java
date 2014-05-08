package parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.util.PriceFormatter;
import stores.NullProductStore;
import util.ProductUtils;
import entities.ProductSummary;
import entities.Retailer;

public class BananaRepublicParser extends GapCommonParser
{
	private static final Logger logger = Logger.getLogger("BananaRepublicParser");
	
	public BananaRepublicParser()
	{
		super(Retailer.ID.BANANAREPUBLIC);
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
		
		Elements productList = doc.getElementsByClass("productItem");
		for (Element product : productList)
		{
			try
			{
				String name = product.getElementsByClass("productItemName").first().text();
				Elements salePrice = product.getElementsByClass("priceDisplaySale");
				double price = -1;
				if (salePrice.size() != 0)
					price = PriceFormatter.formatPrice(salePrice.first().ownText());
				else
					price = PriceFormatter.formatPrice(product.getElementsByClass("priceDisplay").first().ownText());
				String url = product.getElementsByClass("productItemName").first().absUrl("href");
				String imageUrl = product.getElementsByClass("imgDiv").first().getElementsByTag("img").first()
						.absUrl("src");
				logger.info("Prodcut name:" + name + ", price:" + price + ", url:" + url);
				ProductSummary prod = new ProductSummary(getRetailerId(), categoryId, categoryName, name, price, url, imageUrl,
						"", ""); 				
				if(!ProductUtils.isProductValid(prod))
					logger.warn(("Invalid Product: " + prod));				
				products.add(prod);
				if(max > 0 && ++count >= max)
					break;

			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("Failed to Parse products for:" + categoryName);
				logger.error("Elements with Issue" + product);
			}

		}
		return products;
	}

	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new BananaRepublicParser();
		String url = "http://www.bananarepublic.com/products/suits-blazers-slim-fit-C67017.jsp";
		parser.parseAndSave(0, "Unknown", url, new NullProductStore());
	}
}


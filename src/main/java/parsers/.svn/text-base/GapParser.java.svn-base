package parsers;

import stores.NullProductStore;
import entities.Retailer;

public class GapParser extends GapCommonParser
{
	public GapParser()
	{
		super(Retailer.ID.GAP);
	}

	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new GapParser();
		String url = "http://www.gap.com/products/graphics-ts-tops-womens-C87974.jsp";
		parser.parseAndSave(1000, "NA", url, new NullProductStore());
	}
}

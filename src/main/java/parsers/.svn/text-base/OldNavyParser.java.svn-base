package parsers;

import stores.NullProductStore;
import entities.Retailer;

public class OldNavyParser extends GapCommonParser
{

	public OldNavyParser()
	{
		super(Retailer.ID.OLDNAVY);
	}

	public static void main(String[] args) throws Exception
	{
		ProductsParser parser = new OldNavyParser();
		String url = "http://www.oldnavy.com/products/sweaters-new-arrivals-women-C79757.jsp";
		parser.parseAndSave(1000, "NA", url, new NullProductStore());
	}
}


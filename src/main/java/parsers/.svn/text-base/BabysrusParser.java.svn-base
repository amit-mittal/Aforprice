package parsers;

import stores.NullProductStore;
import entities.Retailer;

public class BabysrusParser extends ToysrusParser {

	public BabysrusParser() {
		super(Retailer.BABYSRUS.getId());
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new BabysrusParser();
		String url = "http://www.toysrus.com/family/index.jsp?categoryId=2256159";
		parser.parseAndSave(0, "NA", url, new NullProductStore());
	}
}
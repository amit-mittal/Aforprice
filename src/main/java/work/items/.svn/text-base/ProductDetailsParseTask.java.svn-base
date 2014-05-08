package work.items;

import entities.ProductSummary;
import global.exceptions.BandBajGaya;
import global.exceptions.Bhagte2BandBajGaya;
import parsers.ProductsParserFactory;
import parsers.details.ProductDetailsParser;
import work.managers.ProductDetailsParseTaskNotifyee;


public class ProductDetailsParseTask extends Task {
	
	private final ProductDetailsParser parser;
	private final String url;
	private final String retailer;
	private final ProductSummary existingProd; 
	
	/**
	 * ProductDetailsParseTask
	 * @param parser
	 * @param url
	 * @throws BandBajGaya 
	 */
	public ProductDetailsParseTask(String retailer, String url, ProductSummary existingProd) throws BandBajGaya {		
		super(ProductDetailsParseTaskNotifyee.get(), 0, 1);		
		this.url = url;		
		this.retailer = retailer;
		this.existingProd = existingProd;
		this.parser = ProductsParserFactory.getDetailsParser(retailer);
		if(parser == null)
			throw new BandBajGaya("ProductDetailsParser not defined for " + retailer);
	}

	@Override
	public void doWork() {
		try{
			parser.parseSaveStore(url, existingProd);
		}catch(Exception e){
			throw new Bhagte2BandBajGaya(e);
		}
	}
	
	public String getURL(){
		return url;
	}
	
	public String getRetailer(){
		return retailer;
	}
}

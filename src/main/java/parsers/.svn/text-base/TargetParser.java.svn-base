/*package parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.TargetClassNames;
import parsers.html.ClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import util.ProductUtils;
import entities.ProductSummary;
import entities.Retailer;

public class TargetParser extends ProductsParser {
	private final ClassNames htmlClasses = new TargetClassNames();
	private final static Logger LOGGER = Logger.getLogger(TargetParser.class);
	
	public TargetParser() {
		super(Retailer.ID.TARGET);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ClassNames getClassNames() {
		return htmlClasses;
	}
	
	@Override
	public List<ProductSummary> parse(Document doc, int categoryId, String categoryName, boolean productsPageCheckOnly) {
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		Elements productRows = doc.getElementsByClass(htmlClasses.productRows()[0]);
		Element helper = null;
		
		for(Element product: productRows) {
			String name, url, imageUrl, desc = "", model = "";
			double price = PriceTypes.UNKNOWN.getValue();
			
			//get imageUrl
			helper = product.getElementsByClass(htmlClasses.productImage()).last();
			imageUrl = helper.attr("abs:src");
			
			//get name and url
			helper = product.getElementsByClass(htmlClasses.productName()[0]).first();
			helper = helper.getElementsByTag("a").first();
			url = helper.absUrl("href");
			name = helper.attr("title");
			
			//get price
			helper = product.getElementsByClass(htmlClasses.price()[0]).first();
			price = PriceFormatter.formatDollarPrice(helper.ownText().trim());
			
			ProductSummary prod = new ProductSummary(getRetailerId(), categoryId, categoryName, name, price, url, imageUrl, desc, model); 
			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product: " + prod);
			}
			products.add(prod);						

		}
		return products;
	}
	
	@Override
	protected String getNextURL(Document doc) {
		Element nextUrlElement = doc.getElementsByClass(htmlClasses.nextUrl()).first();
		nextUrlElement = nextUrlElement.getElementsByTag("a").first();
		if(nextUrlElement != null) {
			System.out.println(nextUrlElement.absUrl("href"));
			return nextUrlElement.absUrl("href");
		} else
			return null;
	}
	
	@Override
	protected boolean skipCategory(String name) {
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		ProductsParser parser = new TargetParser();
		parser.parseAndSave(1, "unknown", "http://m.target.com/c/boys-costumes-halloween/-/N-5tf5d", new stores.NullProductStore());
			
	}
}
*/
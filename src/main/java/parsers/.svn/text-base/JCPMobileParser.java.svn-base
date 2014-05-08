package parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.JCPennyMobileClassNames;
import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import stores.NullProductStore;
import entities.ProductSummary;
import entities.Retailer;

//public class JCPMobileParser extends ProductsParser {
//	
//	private static final Logger LOGGER = Logger.getLogger(JCPMobileParser.class);	
//	private static final ClassNames htlmClasses = new JCPennyMobileClassNames();
//
//	public JCPMobileParser() {
//		super(Retailer.JCPENNY_MOBILE.getId());
//	}
//	
//	/**
//	 * @param doc
//	 * @param categoryId
//	 * @param categoryName
//	 * @return
//	 */
//	@Override
//	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId,
//										String categoryName, int startRank, int max) {
//		Element helper = null;
//		Elements productRows = doc.getElementsByClass(htlmClasses.productRows()[0]);
//		List<ProductSummary> products = new ArrayList<ProductSummary>();		
//		for(Element productRow: productRows){			
//			String name = "";
//			String desc = ""; //Description is not available
//			double price = PriceTypes.NOT_AVAILABLE.getValue();
//			String url = "";
//			String imageUrl = "";
//			String model = ""; //Model is not available
//				
//			helper = productRow.getElementsByClass(htlmClasses.productName()[0]).first();
//			if(helper != null){
//				Element spanClass = helper.getElementsByClass("blockimg").first();
//				if(spanClass == null){
//					spanClass = helper.getElementsByClass("blockimg1").first();
//				}
//				if(spanClass != null)
//					name = helper.text();
//				url = helper.absUrl("href");
//			}	
//			
//			helper = productRow.getElementsByClass(htlmClasses.productImage()).first();
//			if(helper != null){
//				helper = helper.getElementsByTag("img").first();
//				if(helper != null)
//					imageUrl = helper.attr("abs:src");				
//			}
//			if(imageUrl.equals(""))				
//				LOGGER.warn("image missing for " + helper.html());
//			helper = getPriceElement(productRow);
//			if(helper != null){											
//				price = PriceFormatter.formatDollarPrice(helper.html());
//			}
//			boolean isDataMissing = (name == null || name.equals("") || 
//										url == null || url.equals("") || 
//										imageUrl == null || imageUrl.equals("") || 
//										PriceTypes.isInvalidType(price));
//			if(isDataMissing){
//				if(name == null || name.equals(""))
//					LOGGER.warn("name missing");
//				if(imageUrl == null || imageUrl.equals(""))
//					LOGGER.warn("image url is missing");
//				if(url == null || url.equals(""))
//					LOGGER.warn("url is missing");
//				if(PriceTypes.isInvalidType(price))
//					LOGGER.warn("price is missing");				
//				LOGGER.warn(productRow.html());				
//			}
//			products.add(new ProductSummary(getRetailerId(), categoryId, categoryName, name, price, url, imageUrl, desc, model));						
//		}
//		return products;
//	}
//
//
//	@Override
//	protected String getNextURL(Document doc) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected ClassNames getClassNames() {
//		return htlmClasses;
//	}
//	
//	public static void main(String[] args) throws Exception{
//		String url = "http://www.cvs.com/shop/Beauty/Makeup/Lipstick/_/N-3uZe7vlZ2k?pt=SUBCATEGORY";
//		ProductsParser parser = new JCPMobileParser();
//		parser.parseAndSave(0, "NA", url, new NullProductStore());
//	}
//
//}

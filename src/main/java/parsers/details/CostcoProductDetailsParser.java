package parsers.details;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.util.PriceTypes;
import parsers.util.PriceFormatter;
import uploader.util.MyAssert;
import entities.Product;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;

public class CostcoProductDetailsParser extends ProductDetailsParser{

	public CostcoProductDetailsParser() {
		super(Retailer.ID.COSTCO);
		logger = Logger.getLogger(CostcoProductDetailsParser.class);
	}

	/**
	 * Parses the product details page for walmart.com. The example HTML is located at the following location.
	 * 
	 * @param doc
	 * @return
	 */
	@Override
	public ProductSummary parseAndUpdateExisting(String url, Document doc, ProductSummary existingProd) throws BandBajGaya{				
		if(doc == null){
			throw new BandBajGaya("doc is null");			
		}
		Elements temp = doc.getElementsByClass("box-747");
		if(temp.size()==0){
			logger.error("couldn't find class 'box-747'");
			return null;
		}
				
		Element productElement = temp.first();
		String productName = getName(productElement);
		if(productName==null)
			return null;
		String imgUrl = getImageUrl(productElement);
		
		double price = getPrice(productElement);
		String desc = getDescription(productElement);
		String model = getModel(null);

		if(existingProd == null )
			existingProd = new Product(getRetailer(), productName, price, url, imgUrl, desc, model);
		
		existingProd.setName(productName);
		existingProd.setPrice(price);
		existingProd.setImageUrl(imgUrl);
		existingProd.setModel(model);
		
		return  existingProd;
	}

	/**
	 * @param productRow
	 * @return
	 */
	@Override
	protected double getPrice(Element productElm) throws BandBajGaya {
		try{
			Element helper = productElm.getElementsByClass("currency").first();
			String temp = helper.text();
			double price = PriceFormatter.formatDollarPrice(temp);
			return price;
		}catch(Throwable e){
			logger.error("Unable to get price", e);
			return PriceTypes.NOT_AVAILABLE.getValue();
		}
	}

	@Override
	protected String getName(Element elm) throws BandBajGaya {
		Elements helpers = elm.getElementsByClass("top_review_panel");
		if(helpers==null){
			logger.error("Couldn't get name due to missing class 'top_review_panel'");
			return null;
		}
		Element helper = helpers.first();
		Element child = helper.child(0);
		if(child==null){
			logger.error("Couldn't get name due to no child under 'top_review_panel'");
			return null;
		}
		String name = child.text();
		return name;
	}

	@Override
	protected String getDescription(Element elm) {
		Element helper = elm.getElementsByAttributeValue("id", "product-tab1").first();			
		String desc = helper.text();
		if(desc!=null && desc.length()>5000)
			desc = desc.substring(0,4999);//varchar(5000)
		return desc;
	}
	
	@Override
	protected String getModel(Element elm) {
		return null;//not available
	}
	
	@Override
	protected String getImageUrl(Element elm) {
		Elements helpers = elm.getElementsByClass("images");
		if(helpers.size()==0){
			logger.error("couldn't get image url due to mising class images");
			return null;
		}
		Element helper = helpers.first();
		helper = helper.getElementsByTag("img").first();
		String imageUrl = helper.attr("abs:src");
		return imageUrl;
			}
	
	@Override
	protected Map<String, Object> getAttributes(Element elm){
		throw new UnsupportedOperationException();
	}
	
	protected void assertProduct(Product actualProduct, ProductSummary expectedproduct, MyAssert myAssert){
		myAssert.assertEquals(actualProduct.getPrice(), "ExpectedPrice", actualProduct.getPrice(), "ActualPrice");
//		myAssert.assertEquals(actualProduct.getName(), "Name", actualProduct.getName(), "Name");
//		myAssert.assertEquals(actualProduct.getModel(), "Model", actualProduct.getModel(), "Model");
//		myAssert.assertEquals(actualProduct.getImageUrl(), "ImageUrl", actualProduct.getImageUrl(), "ImageUrl");
	}

	
	public static void main(String[] args) throws BandBajGaya{
		CostcoProductDetailsParser parser = new CostcoProductDetailsParser();
//		try {
//			parser.testParser();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String url;
		//output of below url - 2012-09-16¦costco¦Ellington 6-piece Cal King Bedroom Set¦6399.99¦http://images.costco.com/image/media/350-595408-847__1.jpg¦Ellington’s subtle textures and gentle curv
		url = "http://www.costco.com/Ellington-6-piece-Cal-King-Bedroom-Set.product.100003801.html";
		//2012-09-16¦costco¦DISH Tailgater® Portable HDTV System VQ2520¦499.99¦http://images.costco.com/image/media/350-674094-847__1.jpg¦The Tailgater is a portable,
		url = "http://www.costco.com/DISH-Tailgater%C2%AE-Portable-HDTV-System-VQ2520.product.11766857.html";
		ProductSummary product = parser.parseSaveStore(url);
		System.out.println(product);
	}
}

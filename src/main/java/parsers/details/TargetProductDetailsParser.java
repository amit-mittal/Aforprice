
package parsers.details;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parsers.util.PriceTypes;
import uploader.util.MyAssert;
import entities.Product;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;

public class TargetProductDetailsParser extends ProductDetailsParser{
	public TargetProductDetailsParser() {
		super(Retailer.ID.TARGET);
		logger = Logger.getLogger(ProductDetailsParser.class);
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
		Element productRow = doc.getElementById("ProductDetails");
		String imgUrl = getImageUrl(productRow);
		String productName = getName(productRow);		
		double price = getPrice(productRow);
		if( existingProd == null )
			existingProd = new Product(getRetailer(), productName, price, url, imgUrl, /*desc*/"", /*model*/"");
		else
		{
			existingProd.setName(productName);
			existingProd.setPrice(price);
			existingProd.setImageUrl(imgUrl);
		}
		return existingProd;
	}

	@Override
	protected double getPrice(Element pricingInfo) throws BandBajGaya {
 		pricingInfo = pricingInfo.getElementsByClass("offerPrice").first();
		String temp = pricingInfo.text();
		int index = temp.indexOf("$");
		double price;
		if(index==-1 && temp.contains("View Price in Cart"))
			price = PriceTypes.NOT_AVAILABLE.getValue();
		else{
			temp = temp.substring(++index).replace(",", "");
			index = temp.indexOf("/"); //i.e. $0.68 / Sq. Ft.
			if(index!=-1){
				index--;
				temp = temp.substring(0,index).trim();
			}
			temp = temp.split(" ")[0];//i.e. 10.98 New Lower Price
			price = Double.parseDouble(temp);
		}
		return price;
	}

	@Override
	protected String getName(Element elm) throws BandBajGaya {
		String name=elm.getElementsByClass("product-name").text();
		return name;		
	}

	@Override
	public String getDescription(Element elem){
		throw new UnsupportedOperationException();
	}
	
	protected String getDescription(Document doc) {
		return "";
		/*String desc=null;
		Element elm = doc.getElementsByAttributeValue("id","product_description").first();
		if(elm != null)
			desc = elm.text();
		return desc;*/
	}

	@Override
	protected String getModel(Element elm) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected String getImageUrl(Element elm) {
		String imageUrl = "";
		try
		{
			elm = elm.getElementById("heroImage");
			imageUrl = elm.getElementsByTag("img").first().attr("src");
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return imageUrl;
	}
	
	@Override
	protected Map<String, Object> getAttributes(Element elm){
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void assertProduct(Product actualProduct, ProductSummary expectedproduct, MyAssert myAssert){
		myAssert.assertEquals(expectedproduct.getPrice(), "ExpectedPrice", actualProduct.getPrice(), "ActualPrice");		
		myAssert.assertEquals(expectedproduct.getName().replace("&nbsp;", " "), "Name", actualProduct.getName(), "Name");
//		myAssert.assertEquals(expectedproduct.getModel(), "Model", actualProduct.getModel(), "Model");
//		myAssert.assertEquals(expectedproduct.getImageUrl(), "ImageUrl", actualProduct.getImageUrl(), "ImageUrl");
	}
	
	public static void main(String[] args) throws BandBajGaya, IOException{
		boolean fileBased = false;
		String fileOrUrl = "http://www.target.com/p/hello-kitty-toaster-kt5211/-/A-730762#prodSlot=medium_1_25";
		//String fileOrUrl = "http::/target.com";
		ProductDetailsParser parser = new TargetProductDetailsParser();
		ProductSummary product;
		if( fileBased )
		{
			Document doc = Jsoup.parse(new File(fileOrUrl), null, Retailer.getRetailer(Retailer.ID.TARGET).getLink());
			product = parser.parseAndUpdateExisting(fileOrUrl, doc, null);
		}
		else
		{
			 product = parser.parseSaveStore(fileOrUrl);
		}
		
		System.out.println(product);
	}
}

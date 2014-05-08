package parsers.details;

import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.util.PriceTypes;
import uploader.util.MyAssert;
import util.Constants;
import entities.Product;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;

public class LowesProductDetailsParser extends ProductDetailsParser{

	public LowesProductDetailsParser() {
		super(Retailer.ID.LOWES);
		cookies.put("selectedStore1", Constants.LOWES_STORE);
		logger = Logger.getLogger(LowesProductDetailsParser.class);
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
		Elements productRow = doc.getElementsByAttributeValue("id", "productLeft");
		Element helper = productRow.first();
		String imgUrl = getImageUrl(helper);
		String productName = getName(helper);		
		double price = getPrice(helper);
		String desc = getDescription(helper);
		String model = getModel(helper);
		if(existingProd == null)
			existingProd = new Product(getRetailer(), productName, price, url, imgUrl, desc, model);
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
		Elements tempPricingElements = pricingInfo.getElementsByAttributeValue("id", "pricing");
		if(tempPricingElements.size()==0)
			tempPricingElements = pricingInfo.getElementsByClass("pricing");
		if(tempPricingElements.size()==0){
			logger.error("Unable to find pricing element");
			return PriceTypes.NOT_AVAILABLE.getValue();
		}
		pricingInfo = pricingInfo.getElementsByAttributeValue("id", "pricing").first();
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
		String name=null;
		elm = elm.getElementsByAttributeValue("id","descCont").first();
		if(elm.childNodes().get(1) instanceof Element){//to be safe lets check this
			Element nameElem = (Element) elm.childNodes().get(1);
			name = nameElem.text();	
		}
		return name;		
	}

	@Override
	protected String getDescription(Element elm) {
		String desc=null;
		elm = elm.getElementsByAttributeValue("id","description-tab").first();
		if(elm != null)
			desc = elm.text();
		return desc;
	}

	@Override
	protected String getModel(Element elm) {
		String model=null;
		elm = elm.getElementsByClass("itemmodel").first();
		if(elm != null)
			model = elm.text(); 
		return model;
	}
	
	@Override
	protected String getImageUrl(Element elm) {
		elm = elm.getElementsByTag("img").first();
		String imageUrl = elm.attr("abs:src");
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
		LowesProductDetailsParser parser = new LowesProductDetailsParser();
		try {
			parser.testParser();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url;
		//output of below url - 2012-08-12¦lowes¦Samsung 3.9 cu ft Stackable Front-Load Washer (White) ENERGY STAR¦1079.1¦http://images.lowes.com/product/converted/036725/036725560352lg.jpg¦Description Got an update or addition to this product's details? Share it here. 3.9 cu ft Stackable Front-Load Washer (White) ENERGY STAR ENERGY STAR® qualified Vibration reduction technology (VRT) - makes for perfect 2nd floor installations Exclusive PowerFoam™ Technology creates a powerful, yet gentle foam that's ideal for washer large loads and bulky items PureCycle™ cleans your drum with the touch of a button to ensure a fresh, clean drum without the need for detergent Diamond DrumTM - for gentler washing¦Item #: 346646 |  Model #: WF431ABW
		url = "http://www.lowes.com/pd_346646-149-WF431ABW_4294857981__?productId=3355892&Ns=p_product_qty_sales_dollar|1&pl=1&currentURL=%3FNs%3Dp_product_qty_sales_dollar%7C1&facetInfo=";
		//2012-08-12¦lowes¦Electrolux 3 Installation Inlet Kit¦139.0¦http://images.lowes.com/product/converted/023169/023169129320lg.jpg¦Description Got an update or addition to this product's details? Share it here. 3 Installation Inlet Kit Three standard inlets and necessary materials to install in your home as a do it yourself project Contains 80 feet of pipe 6 nail guards¦Item #: 327356 |  Model #: 40368
		url = "http://www.lowes.com/pd_327356-73823-40368_4294810311__?productId=3274845&Ns=p_product_qty_sales_dollar&pl=1&currentURL=%3FNs%3Dp_product_qty_sales_dollar&facetInfo=";
		//url = "http://www.walmart.com/ip/HP-Black-Pavilion-p2-1123wb-Desktop-PC-Bundle-with-Intel-Celeron-G460-Processor-4GB-Memory-20-Monitor-1TB-Hard-Drive-and-Windows-7-Home-Premium/21081427";
		//url = "http://www.walmart.com/ip/Acer-Black-ZX4971G-UW20P-Desktop-PC-with-Intel-Core-i3-2120-Processor-6-Memory-21.5-HD-Monitor-1TB-Hard-Drive-and-Windows-7-Home-Premium/21097552";
		ProductSummary product = parser.parseSaveStore(url);
		System.out.println(product);
	}
}

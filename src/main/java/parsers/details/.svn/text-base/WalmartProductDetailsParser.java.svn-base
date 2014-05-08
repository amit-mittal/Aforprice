package parsers.details;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.util.PriceFormatter;
import uploader.util.MyAssert;
import entities.Product;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;

public class WalmartProductDetailsParser extends ProductDetailsParser{
	public WalmartProductDetailsParser() {
		super(Retailer.ID.WALMART);
		logger= Logger.getLogger(WalmartProductDetailsParser.class);
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
		//Image
		Element colOne = doc.getElementsByClass("columnOne").first();
		String imgUrl = getImageUrl(colOne);
		//Product description and price
		Element colTwo = doc.getElementsByClass("columnTwo").first();
		if(colTwo == null){
			throw new BandBajGaya("columnTwo class element is absent");
		}
		Element productNameElm = colTwo.getElementsByClass("productTitle").first();
		String productName = getName(productNameElm);
		
		Element pricingInfo = colTwo.getElementsByClass("PricingInfo").first();
		double price = getPrice(pricingInfo);
		
		Element specTableElm = colTwo.getElementsByClass("SpecTable").first();
		Map<String, Object> attributes = getAttributes(specTableElm);
		String model = (String)attributes.get("Model No.");
		
		if( existingProd == null )
			existingProd = new Product(getRetailer(), productName, price, url, imgUrl, "", model);
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
		if(pricingInfo == null)
			throw new BandBajGaya("PricingInfo class element is absent");
		Element priceElm = pricingInfo.getElementsByClass("bigPriceText1").first();
		Element decimalPriceElm = pricingInfo.getElementsByClass("smallPriceText1").first();
		if(priceElm == null)
			throw new BandBajGaya("bigPriceText1 class element is absent");
		StringBuilder priceText = new StringBuilder();
		priceText.append(priceElm.text());
		priceText.append(decimalPriceElm == null?"00":decimalPriceElm.text());
		
		return PriceFormatter.formatDollarPrice(priceText.toString());
	}

	@Override
	protected String getName(Element elm) throws BandBajGaya {
		if(elm != null)
			return elm.text();		
		logger.warn("productsTitle class element is absent");
		return "";
	}

	@Override
	protected String getDescription(Element elm) {
		//TODO: Implement getDescription
		return "";
	}

	@Override
	protected String getModel(Element elm) {
		throw new UnsupportedOperationException("The model is obtained from the product attributes");
	}

	@Override
	protected String getImageUrl(Element elm) {
		String url = "";
		if(elm == null){
			logger.warn("The element for the class columnOne is absent");
			return url;
		}
		Element imgElm = elm.getElementsByClass("LargeItemPhoto215").first();
		if(imgElm == null){
			logger.warn("The element for the class LargeItemPhoto215 is absent");
			return url;
		}
		Element imgHref = imgElm.getElementsByTag("a").first();
		if(imgHref == null){
			logger.warn("Unable to get image href in " + imgElm.html());
			return url;
		}
		return imgHref.attr("abs:href");
	}
	
	@Override
	protected Map<String, Object> getAttributes(Element elm){
		Map<String, Object> attributes = new HashMap<String, Object>();
		if(elm == null)
			return attributes;
		
		Elements rows = elm.getElementsByTag("tr");
		for(Element row: rows){
			Element keyElm = row.getElementsByClass("LightRowHead").first();
			if(keyElm == null){
				keyElm = row.getElementsByClass("DarkRowHead").first();
			}
			if(keyElm == null)
				continue;
			Element valueElm = row.getElementsByClass("LightRow").first();
			if(valueElm == null)
				valueElm = row.getElementsByClass("DarkRow").first();
			if(valueElm == null)
				continue;
			String key = keyElm.text();
			if(key != null){
				if(key.endsWith(":")){
					key = key.substring(0, key.length() - 1);
				}
				attributes.put(key, valueElm.text());
			}			
		}
		return attributes;
	}
	
	@Override
	protected void assertProduct(Product actualProduct, ProductSummary expectedproduct, MyAssert myAssert){
		myAssert.assertEquals(expectedproduct.getPrice(), "ExpectedPrice", actualProduct.getPrice(), "ActualPrice");		
	}
	
	public static void main(String[] args) throws BandBajGaya{
		ProductDetailsParser parser = new WalmartProductDetailsParser();
		//String url = "http://www.walmart.com/ip/Acer-Black-eMachines-EL1360G-UW12P-Desktop-PC-with-AMD-Dual-Core-E-300-Processor-2GB-Memory-20-Monitor-500GB-Hard-Drive-and-Windows-7-Home-Premium/19714391";
		//String url = "http://www.walmart.com/ip/Dell-Refurbished-Black-GX520-Desktop-PC-with-Intel-Pentium-4-Processor-160GB-Hard-Drive-and-Windows-7-Home-Premium/20678958";
		//String url = "http://www.walmart.com/ip/HP-Black-Pavilion-p2-1123wb-Desktop-PC-Bundle-with-Intel-Celeron-G460-Processor-4GB-Memory-20-Monitor-1TB-Hard-Drive-and-Windows-7-Home-Premium/21081427";
		String url = "http://www.walmart.com/ip/Dell-Silver-15.6-XPS-15-XPS15-1105sLV-Laptop-PC-with-Intel-Core-i7-3632QM-Processor-and-Windows-8-Operating-System/23574496";
		ProductSummary prod = parser.parseSaveStore(url);
		System.out.println(prod);
	}
}

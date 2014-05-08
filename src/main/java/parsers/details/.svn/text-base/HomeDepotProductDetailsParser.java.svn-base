
package parsers.details;

import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parsers.util.PriceTypes;
import uploader.util.MyAssert;
import entities.Product;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.BandBajGaya;

public class HomeDepotProductDetailsParser extends ProductDetailsParser{
	public HomeDepotProductDetailsParser() {
		super(Retailer.ID.HOMEDEPOT);
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
		Element productRow = doc.getElementById("productinfo_ctn");
		String imgUrl = getImageUrl(productRow);
		String productName = getName(productRow);		
		double price = getPrice(productRow);
		if(existingProd == null)
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
 		pricingInfo = pricingInfo.getElementsByClass("preg").first();
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
		String name=elm.getElementsByClass("product_title").text();
		return name;		
	}

	@Override
	public String getDescription(Element elem){
		throw new UnsupportedOperationException();
	}
	
	protected String getDescription(Document doc) {
		return "";
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
			elm = elm.getElementsByClass("product_mainimg").first();
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
	
	public static void main(String[] args) throws BandBajGaya{
		ProductDetailsParser parser = new HomeDepotProductDetailsParser();
		try {
			parser.testParser();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url;
		//output of below url - 2012-08-22¦homedepot¦Fasade 18 in. x 24 in. Waves Oil Rubbed Bronze Backsplash¦19.95¦http://www.homedepot.com/catalog/productImages/300/e1/e1d6044a-442a-467c-baef-38eaf2653f28_300.jpg¦PRODUCT DESCRIPTION Try the Fasade 17 in. x 25 in. Oil Rubbed Bronze Waves PVC Backsplash to create a bold and dramatic accent. This backsplash is made of PVC and is water and corrosion resistant for exceptional durability. The oil rubbed bronze finish makes a great addition to your kitchen decor. Made of PVC with an oil rubbed bronze finish for a dramatic accent Ideal for residential remodeling or new construction projects Panels are water and corrosion resistant for exceptional durability Use a recommended adhesive or high bond double sided tape for a quick installation (adhesive and tape not included) MFG Brand Name : Fasade MFG Model # : B65-26 MFG Part # : B65-26 Return To Top¦null
//		url = "http://www.homedepot.com/Kitchen-Backsplashes/h_d1/N-5yc1vZbcsz/R-202823730/h_d2/ProductDisplay?catalogId=10053&langId=-1&storeId=10051&superSkuId=202845927#.UDWpRMGuaSo";
		//2012-08-22¦homedepot¦GE 25.9 cu. ft. 35.75 in. Wide French Door Refrigerator in Stainless Steel¦1979.1¦http://www.homedepot.com/catalog/productImages/300/74/74813c88-bec0-4a05-a8b1-815079c75e6b_300.jpg¦PRODUCT DESCRIPTION This GE French door bottom freezer has fresh filtered water and crushed or cubed ice conveniently located through the dispenser. The ClimateKeeper system with dual evaporators maintains ideal humidity levels for your fresh foods by having separate environments for fresh and frozen food. Easily store all refrigerator items by using the full width adjustable temperature deli drawer, two adjustable humidity drawers, QuickSpace shelf, and flip up shelf. This refrigerator is also a product of ecomagination and Energy Star qualified. CLICK HERE to learn more about GE Refrigerators ENERGY STAR Qualified - meets or exceeds federal guidelines for energy efficiency, which means year-round energy and saves money Integrated dispenser - Enjoy your ice cubed or crushed, along with chilled, filtered water from a trimless, color-matched dispenser Factory-installed ice maker with water filtration system so your refrigerator comes ready to automatically create filtered ice ClimateKeeper system with dual evaporators create the ideal environments to maintain fresh and frozen foods Adjustable spill proof glass shelves with raised edges keep small spills from becoming big messes Freshness Center - Maintain the fresh tastes of fruits and vegetables with two humidity-controlled drawers Adjustable gallon-sized door bins allow you to free up shelf space and can be easily removed for transport Two-level Slide ‘n Store system provides instant access to frozen foods in large and extra large drawers BrightSpace interior with GE Reveal lighting makes it easy to find tasty treats under bright, clear interior lighting Frost-free defrost limits freezer burn and moisture build-up so food lasts longer Door alarm notifies you if any door has been left open for more than 3 minutes Click Here for details on the services included with Delivery & Basic Hook-up and Installation options for this product MFG Brand Name : GE MFG Model # : GFSS6KKYSS MFG Part # : GFSS6KKYSS Return To Top¦null
		url = "http://www.homedepot.com/p/3-4-in-x-4-ft-x-8-ft-HD-Maple-Plywood-263012/203600599?N=bu0m#.UX8klLWyCIU";
		ProductSummary product = parser.parseSaveStore(url);
		System.out.println(product);
		
		//String url = "http://www.walmart.com/ip/Acer-Black-eMachines-EL1360G-UW12P-Desktop-PC-with-AMD-Dual-Core-E-300-Processor-2GB-Memory-20-Monitor-500GB-Hard-Drive-and-Windows-7-Home-Premium/19714391";
		//String url = "http://www.walmart.com/ip/Dell-Refurbished-Black-GX520-Desktop-PC-with-Intel-Pentium-4-Processor-160GB-Hard-Drive-and-Windows-7-Home-Premium/20678958";
		//String url = "http://www.walmart.com/ip/HP-Black-Pavilion-p2-1123wb-Desktop-PC-Bundle-with-Intel-Celeron-G460-Processor-4GB-Memory-20-Monitor-1TB-Hard-Drive-and-Windows-7-Home-Premium/21081427";
		//String url = "http://www.walmart.com/ip/Dell-Silver-15.6-XPS-15-XPS15-1105sLV-Laptop-PC-with-Intel-Core-i7-3632QM-Processor-and-Windows-8-Operating-System/23574496";
		ProductSummary prod = parser.parseSaveStore(url);
		System.out.println(prod);
		
	}
}

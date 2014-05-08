package parsers.details;

import db.dao.ProductsDAO;
import entities.Product;
import entities.ProductSummary;
import global.exceptions.BandBajGaya;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import stores.ProductDetailsStore;
import stores.ProductStore;
import uploader.util.MyAssert;
import util.ConfigParms;
import util.ProductUtils;
import util.UtilityLogger;
import util.Utils;

public abstract class ProductDetailsParser {
	private final String retailer;
	protected static Logger logger;
	protected Map<String, String> cookies = new HashMap<String, String>();
	ProductDetailsStore prodStore = null;
	
	public ProductDetailsParser(String retailer){
		this.retailer = retailer;
		prodStore = (ProductDetailsStore) ProductStore.Factory.getProductDetailsStore();

	}

	public String getRetailer(){
		return this.retailer;
	}
	

	/**
	 * Gets the document from the url, save the html and parse the html to determine the product information
	 * @param url
	 * @return
	 * @throws BandBajGaya 
	 */
	public ProductSummary parseSaveStore(String url, ProductSummary existingProd) throws BandBajGaya{
		ProductSummary existingProdCopy = ProductUtils.cloneProductSummary(existingProd);

		Document doc;
		ProductSummary updatedProd;
		
		//This is off-line mode where we don't read product detail again from web.  
		//We just use what we have & update one attribute for testing
		if(ConfigParms.MODE.equals(ConfigParms.RUNTIME_MODE.UNITTEST )) 
		{
			updatedProd = existingProdCopy;
			updatedProd.setPrice(existingProd.getPrice() + 5);
		}
		else
		{	
			doc = Utils.connect(url, new HashMap<String, String>(), getRetailer());
			updatedProd =  parseAndUpdateExisting(url, doc, existingProdCopy);
		}
		updatedProd.setDownloadTime(new Date());
		prodStore.save(updatedProd, existingProd);
		return updatedProd;
	}

	/**
	 * Gets the document from the url, save the html and parse the html to determine the product information
	 * @param url
	 * @return
	 * @throws BandBajGaya 
	 */
	public ProductSummary parseSaveStore(String url) throws BandBajGaya{
		Document doc = Utils.connect(url, new HashMap<String, String>(), getRetailer());
		ProductSummary prod = parseAndUpdateExisting(url, doc, null);
		prodStore = (ProductDetailsStore) ProductStore.Factory.getProductDetailsStore();
		prodStore.save(prod, null);
		return prod;
	}
	
	public Product parse(File file){
		return null;
	}
	protected abstract ProductSummary parseAndUpdateExisting(String url, Document doc, ProductSummary existingProd) throws BandBajGaya;
	protected abstract double getPrice(Element elm) throws BandBajGaya;
	protected abstract String getName(Element elm) throws BandBajGaya;
	protected abstract String getDescription(Element elm);
	protected abstract String getModel(Element elm);
	protected abstract String getImageUrl(Element elm);
	protected abstract Map<String, Object> getAttributes(Element elm);
	protected abstract void assertProduct(Product actualProduct, ProductSummary expectedproduct, MyAssert myAssert);
	
	public void testParser() throws SQLException, BandBajGaya {
		ProductsDAO prodDao = new ProductsDAO();
		UtilityLogger.setLogLevel(UtilityLogger.LOG_LEVEL_DEBUG);
		MyAssert myAssert = new MyAssert();
		Map<String, ProductSummary> prevWeekProducts = prodDao.get100DownloadedProductsPreviousWeek(new Date(), this.getRetailer());
		int count=0;
		for(ProductSummary expectedproduct : prevWeekProducts.values()){
			try{
				logger.info("Get data for product "+expectedproduct.getName());
				Product actualProduct = (Product)parseSaveStore(expectedproduct.getUrl(), expectedproduct);
				assertProduct(actualProduct, expectedproduct, myAssert);
				if(count++==9){
					myAssert.printResultSummary();
					count=0;
				}
			}catch(Throwable e){
				UtilityLogger.logError(e);
				continue;
			}
		}
		myAssert.printResultSummary();
	}
}
package datastruct.cache.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.ProductList;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import datastruct.cache.records.ProductCacheRecord;
import datastruct.cache.records.ProductListCacheRecord;
import datastruct.cache.util.ProductCacheRecordFields;
import datastruct.cache.util.ProductListCacheRecordFields;

public class ProductListWireConvertorTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductListWireConvertorTest.class);
	private ProductWireConvertor prodWire = new ProductWireConvertor();
	private ProductListWireConvertor wire = new ProductListWireConvertor();
	private static final ProductCacheRecordFields f = new ProductCacheRecordFields();
	private static final long current = System.currentTimeMillis();
	private static final long INTERVAL = 60*60*1000;
	private static final double DOUBLE = 1.20;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMarshall() {
		logger.info("======= TESTING MARSHALL METHOD =======");
		List<Tick> ticks = new ArrayList<>();
		ticks.add(new Tick(current, DOUBLE));
		PriceHistory priceHist = new PriceHistory(ticks, 
				DOUBLE, 
				2.0*DOUBLE, 
				DOUBLE/2, 
				current-INTERVAL/3, 
				current-INTERVAL/2, 
				current-INTERVAL/5, 
				current-INTERVAL, 
				current);
		priceHist.setAveragePrice(1.5*DOUBLE);
		Product prod1 = new Product(1, 
				"name", 
				"modelNo", 
				"imageUrl", 
				"url", 
				priceHist, 
				new ArrayList<Tick>(), 
				new ArrayList<Review>());
		ProductCacheRecord prodCache1 = new ProductCacheRecord(prod1);
		List<ProductCacheRecord> prodCacheList = new ArrayList<>();
		prodCacheList.add(prodCache1);
		Map<String, String> actual = wire.marshall(new ProductListCacheRecord(1, prodCacheList));
		
		String pre = "0.";
		Map<String, String> expected = new HashMap<>();
		expected.put(ProductListCacheRecordFields.PRODUCT_LIST_SIZE, "1");
		expected.put(pre+f.PRODUCT_ID, "1");
		expected.put(pre+f.NAME, "name");
		expected.put(pre+f.MODEL_NO, "modelNo");
		expected.put(pre+f.IMAGE_URL, "imageUrl");
		expected.put(pre+f.URL, "url");
		expected.put(pre+f.PRICEHISTORY_CURRPRICE, String.valueOf(DOUBLE));
		expected.put(pre+f.PRICEHISTORY_MAXPRICE, String.valueOf(2.0*DOUBLE));
		expected.put(pre+f.PRICEHISTORY_MINPRICE, String.valueOf(DOUBLE/2));
		expected.put(pre+f.PRICEHISTORY_AVGPRICE, String.valueOf(1.5*DOUBLE));
		expected.put(pre+f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(current-INTERVAL/3));
		expected.put(pre+f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(current-INTERVAL/2));
		expected.put(pre+f.PRICEHISTORY_MINPRICE_TO, String.valueOf(current-INTERVAL/5));
		expected.put(pre+f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(current-INTERVAL));
		expected.put(pre+f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(current));
		expected.put(pre+f.PRICEHISTORY_PRICETICKS_SIZE, "1");
		expected.put(pre+"0."+f.PRICETICK_TIME, String.valueOf(current));
		expected.put(pre+"0."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		
		assertMap(actual, expected);
	}
	
	@Test
	public void testUnmarshall() {
		logger.info("======= TESTING UNMARSHALL METHOD =======");
		String pre = "0.";
		Map<String, String> fields = new HashMap<>();
		fields.put("_id", "101");
		fields.put(ProductListCacheRecordFields.PRODUCT_LIST_SIZE, "2");
		fields.put(pre+f.PRODUCT_ID, "1");
		fields.put(pre+f.NAME, "name");
		fields.put(pre+f.MODEL_NO, "modelNo");
		fields.put(pre+f.IMAGE_URL, "imageUrl");
		fields.put(pre+f.URL, "url");
		fields.put(pre+f.PRICEHISTORY_CURRPRICE, String.valueOf(DOUBLE));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE, String.valueOf(2.0*DOUBLE));
		fields.put(pre+f.PRICEHISTORY_MINPRICE, String.valueOf(DOUBLE/2));
		fields.put(pre+f.PRICEHISTORY_AVGPRICE, String.valueOf(1.5*DOUBLE));
		fields.put(pre+f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(current-INTERVAL/3));
		fields.put(pre+f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(current-INTERVAL/2));
		fields.put(pre+f.PRICEHISTORY_MINPRICE_TO, String.valueOf(current-INTERVAL/5));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(current-INTERVAL));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(current));
		fields.put(pre+f.PRICEHISTORY_PRICETICKS_SIZE, "1");
		fields.put(pre+"0."+f.PRICETICK_TIME, String.valueOf(current));
		fields.put(pre+"0."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		
		pre = "1.";
		fields.put(pre+f.PRODUCT_ID, "2");
		fields.put(pre+f.NAME, "name2");
		fields.put(pre+f.MODEL_NO, "modelNo2");
		fields.put(pre+f.IMAGE_URL, "imageUrl2");
		fields.put(pre+f.URL, "url2");
		fields.put(pre+f.PRICEHISTORY_CURRPRICE, String.valueOf(DOUBLE));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE, String.valueOf(2.0*DOUBLE));
		fields.put(pre+f.PRICEHISTORY_MINPRICE, String.valueOf(DOUBLE/2));
		fields.put(pre+f.PRICEHISTORY_AVGPRICE, String.valueOf(1.5*DOUBLE));
		fields.put(pre+f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(current-INTERVAL/3));
		fields.put(pre+f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(current-INTERVAL/2));
		fields.put(pre+f.PRICEHISTORY_MINPRICE_TO, String.valueOf(current-INTERVAL/5));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(current-INTERVAL));
		fields.put(pre+f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(current));
		fields.put(pre+f.PRICEHISTORY_PRICETICKS_SIZE, "2");
		fields.put(pre+"0."+f.PRICETICK_TIME, String.valueOf(current));
		fields.put(pre+"0."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		fields.put(pre+"1."+f.PRICETICK_TIME, String.valueOf(current+INTERVAL));
		fields.put(pre+"1."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		
		ProductListCacheRecord actual = (ProductListCacheRecord)wire.unmarshall(null, fields);
		
		List<Tick> ticks1 = new ArrayList<>();
		ticks1.add(new Tick(current, DOUBLE));
		PriceHistory priceHist1 = new PriceHistory(ticks1, 
				DOUBLE, 
				2.0*DOUBLE, 
				DOUBLE/2, 
				current-INTERVAL/3, 
				current-INTERVAL/2, 
				current-INTERVAL/5, 
				current-INTERVAL, 
				current);
		priceHist1.setAveragePrice(1.5*DOUBLE);
		Product prod1 = new Product(1, 
				"name", 
				"modelNo", 
				"imageUrl", 
				"url", 
				priceHist1, 
				new ArrayList<Tick>(), 
				new ArrayList<Review>());
		ProductCacheRecord prodCache1 = new ProductCacheRecord(prod1);
		List<Tick> ticks2 = new ArrayList<>();
		ticks2.add(new Tick(current, DOUBLE));
		ticks2.add(new Tick(current+INTERVAL, DOUBLE));
		PriceHistory priceHist2 = new PriceHistory(ticks2, 
				DOUBLE, 
				2.0*DOUBLE, 
				DOUBLE/2, 
				current-INTERVAL/3, 
				current-INTERVAL/2, 
				current-INTERVAL/5, 
				current-INTERVAL, 
				current);
		priceHist2.setAveragePrice(1.5*DOUBLE);
		Product prod2 = new Product(2, 
				"name2", 
				"modelNo2", 
				"imageUrl2", 
				"url2", 
				priceHist2, 
				new ArrayList<Tick>(), 
				new ArrayList<Review>());
		ProductCacheRecord prodCache2 = new ProductCacheRecord(prod2);
		List<ProductCacheRecord> prodCacheList = new ArrayList<>();
		prodCacheList.add(prodCache1);
		prodCacheList.add(prodCache2);
		
		ProductListCacheRecord expected = new ProductListCacheRecord(101, prodCacheList);
		
		assertProductListCache(actual, expected);
	}
	
	private void assertMap(Map<String, String> actual, Map<String, String> expected){
		_assertEquals(expected.size(), actual.size());
		for(String key : actual.keySet()){
			_assertEquals(expected.get(key), actual.get(key));
		}
	}
	
	private void assertProductListCache(ProductListCacheRecord actual, ProductListCacheRecord expected){
		_assertEquals(expected.id(), actual.id());
		_assertEquals(expected.getProducts().size(), actual.getProducts().size());
		ProductWireConvertorTest test = new ProductWireConvertorTest();
		for(ProductCacheRecord actualCache : actual.getProducts()){
			boolean found = false;
			for(ProductCacheRecord expectedCache : expected.getProducts()){
				if(actualCache.id().matches(expectedCache.id())){
					found = true;
					test.assertProductCache(actualCache, expectedCache);
				}
			}
			_assertTrue(found);
		}
	}
}

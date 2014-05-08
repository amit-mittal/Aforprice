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
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import datastruct.cache.records.ProductCacheRecord;
import datastruct.cache.util.ProductCacheRecordFields;

public class ProductWireConvertorTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductWireConvertorTest.class);
	private ProductWireConvertor wire = new ProductWireConvertor();
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
		Map<String, String> actual = wire.marshall(new ProductCacheRecord(prod1));
		
		Map<String, String> expected = new HashMap<>();
		expected.put(f.PRODUCT_ID, "1");
		expected.put(f.NAME, "name");
		expected.put(f.MODEL_NO, "modelNo");
		expected.put(f.IMAGE_URL, "imageUrl");
		expected.put(f.URL, "url");
		expected.put(f.PRICEHISTORY_CURRPRICE, String.valueOf(DOUBLE));
		expected.put(f.PRICEHISTORY_MAXPRICE, String.valueOf(2.0*DOUBLE));
		expected.put(f.PRICEHISTORY_MINPRICE, String.valueOf(DOUBLE/2));
		expected.put(f.PRICEHISTORY_AVGPRICE, String.valueOf(1.5*DOUBLE));
		expected.put(f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(current-INTERVAL/3));
		expected.put(f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(current-INTERVAL/2));
		expected.put(f.PRICEHISTORY_MINPRICE_TO, String.valueOf(current-INTERVAL/5));
		expected.put(f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(current-INTERVAL));
		expected.put(f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(current));
		expected.put(f.PRICEHISTORY_PRICETICKS_SIZE, "1");
		expected.put("0."+f.PRICETICK_TIME, String.valueOf(current));
		expected.put("0."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		
		assertMap(actual, expected);
	}
	
	@Test
	public void testUnmarshall() {
		logger.info("======= TESTING UNMARSHALL METHOD - NO PREFIX =======");
		Map<String, String> fields = new HashMap<>();
		fields.put(f.PRODUCT_ID, "1");
		fields.put(f.NAME, "name");
		fields.put(f.MODEL_NO, "modelNo");
		fields.put(f.IMAGE_URL, "imageUrl");
		fields.put(f.URL, "url");
		fields.put(f.PRICEHISTORY_CURRPRICE, String.valueOf(DOUBLE));
		fields.put(f.PRICEHISTORY_MAXPRICE, String.valueOf(2.0*DOUBLE));
		fields.put(f.PRICEHISTORY_MINPRICE, String.valueOf(DOUBLE/2));
		fields.put(f.PRICEHISTORY_AVGPRICE, String.valueOf(1.5*DOUBLE));
		fields.put(f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(current-INTERVAL/3));
		fields.put(f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(current-INTERVAL/2));
		fields.put(f.PRICEHISTORY_MINPRICE_TO, String.valueOf(current-INTERVAL/5));
		fields.put(f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(current-INTERVAL));
		fields.put(f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(current));
		fields.put(f.PRICEHISTORY_PRICETICKS_SIZE, "1");
		fields.put("0."+f.PRICETICK_TIME, String.valueOf(current));
		fields.put("0."+f.PRICETICK_VALUE, String.valueOf(DOUBLE));
		
		ProductCacheRecord actual = (ProductCacheRecord)wire.unmarshall(null, fields);
		
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
		ProductCacheRecord expected = new ProductCacheRecord(prod1);
		//sell rank history and review history not getting checked
		assertProductCache(actual, expected);
		
		logger.info("======= TESTING UNMARSHALL METHOD - WITH PREFIX =======");
		String pre = "x.";
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
		fields.put(pre+"1."+f.PRICETICK_VALUE, String.valueOf(5.0*DOUBLE));
		
		ProductCacheRecord actual2 = (ProductCacheRecord)wire.unmarshall(pre, fields);
		
		List<Tick> ticks2 = new ArrayList<>();
		ticks2.add(new Tick(current, DOUBLE));
		ticks2.add(new Tick(current+INTERVAL, 5.0*DOUBLE));
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
		ProductCacheRecord expected2 = new ProductCacheRecord(prod2);
		//sell rank history and review history not getting checked
		assertProductCache(actual2, expected2);		
	}
	
	private void assertMap(Map<String, String> actual, Map<String, String> expected){
		_assertEquals(expected.size(), actual.size());
		for(String key : actual.keySet()){
			_assertEquals(expected.get(key), actual.get(key));
		}
	}
	
	//getting used in ProductListWireConvertorTest.class
	public void assertProductCache(ProductCacheRecord actual, ProductCacheRecord expected){
		Product a = actual.getProduct();
		Product e = expected.getProduct();
		_assertEquals(e.getProductId(), a.getProductId());
		_assertEquals(e.getName(), a.getName());
		_assertEquals(e.getModelNo(), a.getModelNo());
		_assertEquals(e.getImageUrl(), a.getImageUrl());
		_assertEquals(e.getUrl(), a.getUrl());
		assertPriceHistory(e.getPriceHistory(), a.getPriceHistory());
	}
	
	private void assertPriceHistory(PriceHistory expected, PriceHistory actual){
		assertPriceTicks(expected.getPriceTicks(), actual.getPriceTicks());
		_assertEquals(expected.getCurrPrice(), actual.getCurrPrice(), epsilon);
		_assertEquals(expected.getMaxPrice(), actual.getMaxPrice(), epsilon);
		_assertEquals(expected.getMinPrice(), actual.getMinPrice(), epsilon);
		_assertEquals(expected.getAveragePrice(), actual.getAveragePrice(), epsilon);
		_assertEquals(expected.getCurrPriceFromTime(), actual.getCurrPriceFromTime(), epsilon);
		_assertEquals(expected.getMaxPriceFromTime(), actual.getMaxPriceFromTime(), epsilon);
		_assertEquals(expected.getMaxPriceToTime(), actual.getMaxPriceToTime(), epsilon);
		_assertEquals(expected.getMinPriceFromTime(), actual.getMinPriceFromTime(), epsilon);
		_assertEquals(expected.getMinPriceToTime(), actual.getMinPriceToTime(), epsilon);
	}
	
	private void assertPriceTicks(List<Tick> expected, List<Tick> actual){
		_assertEquals(expected.size(), actual.size());
		//Make sure the order of price ticks is same
		for(int i=0; i<expected.size(); ++i){
			_assertEquals(expected.get(i).getTime(), actual.get(i).getTime());
			_assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
		}
	}
}

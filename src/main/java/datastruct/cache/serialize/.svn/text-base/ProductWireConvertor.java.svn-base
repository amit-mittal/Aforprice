package datastruct.cache.serialize;

import datastruct.cache.ICacheRecord;
import datastruct.cache.records.ProductCacheRecord;
import datastruct.cache.util.ProductCacheRecordFields;
import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anurag on 3/17/14.
 */
public class ProductWireConvertor implements IWireConvertor<ProductCacheRecord>{
	private ProductCacheRecordFields f = new ProductCacheRecordFields();

	@Override
	public ICacheRecord unmarshall(String prefix, Map<String, String> fields) {
		if(prefix == null)
			prefix = "";
		Product product = new Product(Integer.parseInt(fields.get(prefix + f.PRODUCT_ID)), 
				fields.get(prefix + f.NAME), 
				fields.get(prefix + f.MODEL_NO), 
				fields.get(prefix + f.IMAGE_URL), 
				fields.get(prefix + f.URL),  
				parsePriceHistory(prefix, fields), 
				new ArrayList<Tick>(), 
				new ArrayList<Review>());
		ProductCacheRecord entry = new ProductCacheRecord(product);
		return entry;
	}

	/*
		public int productId; // required
		public String name; // required
		public String modelNo; // required
		public String imageUrl; // required
		public String url; // required
		public PriceHistory priceHistory; // required
		public List<Tick> sellRankHistory; // required
		public List<Review> reviewHistory; // required
	 */
	@Override
	public Map<String, String> marshall(ProductCacheRecord record) {
		Map<String, String> serialized = new HashMap<>();
		if(record == null)
			return serialized;
		Product product = record.getProduct();
		serialized.put(f.PRODUCT_ID, String.valueOf(product.getProductId()));
		serialized.put(f.NAME, product.getName());
		serialized.put(f.MODEL_NO, product.getModelNo());
		serialized.put(f.IMAGE_URL, product.getImageUrl());
		serialized.put(f.URL, product.getUrl());
		serialized.putAll(getPriceHistory(product.getPriceHistory()));
		return serialized;
	}

	/*
		public List<Tick> priceTicks; // required
		public double currPrice; // required
		public double maxPrice; // required
		public double minPrice; // required
		public long currPriceFromTime; // required
		public long minPriceFromTime; // required
		public long minPriceToTime; // required
		public long maxPriceFromTime; // required
		public long maxPriceToTime; // required
		public double averagePrice; // optional
	 */
	private Map<String, String> getPriceHistory(PriceHistory priceHistory){
		Map<String, String> serialized = new HashMap<>();
		serialized.put(f.PRICEHISTORY_CURRPRICE, String.valueOf(priceHistory.currPrice));
		serialized.put(f.PRICEHISTORY_MAXPRICE, String.valueOf(priceHistory.maxPrice));
		serialized.put(f.PRICEHISTORY_MINPRICE, String.valueOf(priceHistory.minPrice));
		serialized.put(f.PRICEHISTORY_CURRPRICE_FROM, String.valueOf(priceHistory.currPriceFromTime));
		serialized.put(f.PRICEHISTORY_MINPRICE_FROM, String.valueOf(priceHistory.minPriceFromTime));
		serialized.put(f.PRICEHISTORY_MINPRICE_TO, String.valueOf(priceHistory.minPriceToTime));
		serialized.put(f.PRICEHISTORY_MAXPRICE_FROM, String.valueOf(priceHistory.maxPriceFromTime));
		serialized.put(f.PRICEHISTORY_MAXPRICE_TO, String.valueOf(priceHistory.maxPriceToTime));
		serialized.put(f.PRICEHISTORY_AVGPRICE, String.valueOf(priceHistory.averagePrice));
		serialized.putAll(getPriceTicks(priceHistory.priceTicks));
		return serialized;
	}

	/*
		public long time; // required
		public double value; // required
	 */
	private Map<String, String> getPriceTicks(List<Tick> ticks){
		Map<String, String> serialized = new HashMap<>();
		serialized.put(f.PRICEHISTORY_PRICETICKS_SIZE, String.valueOf(ticks.size()));
		for(int i = 0; i < ticks.size(); i++){
			Tick tick = ticks.get(i);
			serialized.put(i + "." + f.PRICETICK_TIME, String.valueOf(tick.getTime()));
			serialized.put(i + "." + f.PRICETICK_VALUE, String.valueOf(tick.getValue()));
		}
		return serialized;
	}

	private PriceHistory parsePriceHistory(String prefix, Map<String, String> fields) {
		if(prefix == null)
			prefix = "";
		PriceHistory priceHist = new PriceHistory(parsePriceTicks(prefix, fields), 
				Double.parseDouble(fields.get(prefix+f.PRICEHISTORY_CURRPRICE)), 
				Double.parseDouble(fields.get(prefix+f.PRICEHISTORY_MAXPRICE)), 
				Double.parseDouble(fields.get(prefix+f.PRICEHISTORY_MINPRICE)), 
				Long.parseLong(fields.get(prefix+f.PRICEHISTORY_CURRPRICE_FROM)), 
				Long.parseLong(fields.get(prefix+f.PRICEHISTORY_MINPRICE_FROM)), 
				Long.parseLong(fields.get(prefix+f.PRICEHISTORY_MINPRICE_TO)), 
				Long.parseLong(fields.get(prefix+f.PRICEHISTORY_MAXPRICE_FROM)), 
				Long.parseLong(fields.get(prefix+f.PRICEHISTORY_MAXPRICE_TO)));
		priceHist.setAveragePrice(Double.parseDouble(fields.get(prefix+f.PRICEHISTORY_AVGPRICE)));		
		return priceHist;
	}

	private List<Tick> parsePriceTicks(String prefix, Map<String, String> fields){
		if(prefix == null)
			prefix = "";
		int size = Integer.parseInt(fields.get(prefix+f.PRICEHISTORY_PRICETICKS_SIZE));
		List<Tick> ticks = new ArrayList<Tick>(size);
		for(int i = 0; i < size; i++){
			Tick tick = new Tick(Long.parseLong(fields.get(prefix + i + "." + f.PRICETICK_TIME)), 
					Double.parseDouble(fields.get(prefix + i + "." + f.PRICETICK_VALUE)));
			ticks.add(tick);
		}
		return ticks;
	}
}

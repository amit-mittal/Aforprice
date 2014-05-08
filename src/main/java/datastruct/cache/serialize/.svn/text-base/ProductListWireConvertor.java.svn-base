package datastruct.cache.serialize;

import datastruct.cache.ICacheRecord;
import datastruct.cache.util.ProductListCacheRecordFields;
import datastruct.cache.records.ProductCacheRecord;
import datastruct.cache.records.ProductListCacheRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anurag on 3/19/14.
 */
public class ProductListWireConvertor implements IWireConvertor<ProductListCacheRecord>{

	@Override
	public ICacheRecord unmarshall(String prefix, Map<String, String> fields) {
		if(prefix == null)
			prefix = "";
		IWireConvertor<ProductCacheRecord> prodConvertor = WireManager.getWire(ProductCacheRecord.class);
		//size & id will be fetched without the prefix
		int id = Integer.parseInt(fields.get("_id"));
		int size = Integer.valueOf(fields.get(ProductListCacheRecordFields.PRODUCT_LIST_SIZE));
		List<ProductCacheRecord> products = new ArrayList<ProductCacheRecord>(size);
		for(int i = 0; i < size; i++){
			ProductCacheRecord product = (ProductCacheRecord)prodConvertor.unmarshall(prefix + i + ".", fields);
			products.add(product);
		}
		ProductListCacheRecord record = new ProductListCacheRecord(id, products);
		return record;
	}

	@Override
	public Map<String, String> marshall(ProductListCacheRecord record) {
		IWireConvertor<ProductCacheRecord> prodConverttor = WireManager.getWire(ProductCacheRecord.class);
		Map<String, String> serialized = new HashMap<String, String>();
		List<ProductCacheRecord> products = record.getProducts();
		serialized.put(ProductListCacheRecordFields.PRODUCT_LIST_SIZE, String.valueOf(products.size()));
		for(int i = 0; i < products.size(); i++){
			ProductCacheRecord product = products.get(i);
			for(Map.Entry<String, String> entry: prodConverttor.marshall(product).entrySet()){
				serialized.put(i + "." + entry.getKey(), entry.getValue());
			}
		}
		return serialized;
	}
}

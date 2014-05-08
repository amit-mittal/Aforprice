package datastruct.cache.records;

import datastruct.cache.ICacheRecord;
import thrift.genereated.retailer.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag on 3/17/14.
 */
public class ProductListCacheRecord implements ICacheRecord{

    private final String id;
    private final List<ProductCacheRecord> products;

    public ProductListCacheRecord(int id, List<ProductCacheRecord> products){
        this.id = String.valueOf(id);
        this.products = (products == null? new ArrayList<ProductCacheRecord>(): products);
    }

    @Override
    public String id() {
        return id;
    }

    public void add(Product product){
        products.add(new ProductCacheRecord(product));
    }

    public List<ProductCacheRecord> getProducts(){
        return products;
    }
}

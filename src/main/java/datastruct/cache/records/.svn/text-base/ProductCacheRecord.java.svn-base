package datastruct.cache.records;

import datastruct.cache.ICacheRecord;
import thrift.genereated.retailer.Product;

import java.util.Map;

/**
 * Created by Anurag on 3/17/14.
 */
public class ProductCacheRecord implements ICacheRecord{

    private final Product product;

    public ProductCacheRecord(Product product){
        this.product = product;
    }

    @Override
    public String id() {
        return String.valueOf(product.getProductId());
    }

    public Product getProduct(){
        return product;
    }
}

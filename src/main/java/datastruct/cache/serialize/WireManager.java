package datastruct.cache.serialize;

import datastruct.cache.ICacheRecord;
import datastruct.cache.example.ExampleCacheEntry;
import datastruct.cache.records.ProductCacheRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anurag on 3/16/14.
 */
public class WireManager {

    private static Map<Class<?>, IWireConvertor> registrations = new ConcurrentHashMap<>();
    static{
        register(ExampleCacheEntry.class, new ExampleCacheEntryWireConvertor());
        register(ProductCacheRecord.class, new ProductWireConvertor());
    }

    public static void register(Class<?> clazz, IWireConvertor serializer){
        registrations.put(clazz, serializer);
    }

    public static IWireConvertor getWire(Class<?> clazz){
        return registrations.get(clazz);
    }
}

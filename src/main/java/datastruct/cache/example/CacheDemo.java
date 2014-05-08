package datastruct.cache.example;

import datastruct.cache.CacheProvider;
import datastruct.cache.GuavaCache;
import datastruct.cache.ICache;
import mongo.MongoFactory;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by Anurag on 3/15/14.
 */
public class CacheDemo {

    public static void main(String[] args) throws UnknownHostException {
        MongoFactory.init();
        ICache<ExampleCacheEntry> cache = CacheProvider.newCache(ICache.CacheType.GUAVA, 10);
        String id = null;
        for(int i = 0; i < 10000; i++){
            ExampleCacheEntry entry = new ExampleCacheEntry(i, "abc", new Date());
            if(i == 0)
                id = entry.id();
            cache.put(entry);
        }
        ExampleCacheEntry entry = cache.get(id, ExampleCacheEntry.class);
        System.out.println(entry.intField + " " + entry.stringField + " " + entry.dateField);
    }
}

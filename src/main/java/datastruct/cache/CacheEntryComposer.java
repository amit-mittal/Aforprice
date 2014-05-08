package datastruct.cache;

import datastruct.cache.example.ExampleCacheEntry;

import java.util.Date;
import java.util.Map;

/**
 * Created by Anurag on 3/6/14.
 */
public class CacheEntryComposer {
    /**
     * prevent instantiation
     */
    private CacheEntryComposer(){
    }

    public static ICacheRecord compose(Class<?> cacheEntryClass, Map<String, String> fields){
        if(cacheEntryClass.equals(ExampleCacheEntry.class)){
            return composeDemo(fields);
        }
        return null;
    }

    private static ICacheRecord composeDemo(Map<String, String> fields){
        ExampleCacheEntry entry = new ExampleCacheEntry(
                                        Integer.parseInt(fields.get("intField")),
                                        fields.get("stringField"),
                                        new Date(Long.parseLong(fields.get("dateField"))));
        return entry;
    }
}

package datastruct.cache.serialize;

import datastruct.cache.ICacheRecord;
import datastruct.cache.example.ExampleCacheEntry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anurag on 3/17/14.
 */
public class ExampleCacheEntryWireConvertor implements IWireConvertor<ExampleCacheEntry> {

    @Override
    public ICacheRecord unmarshall(String prefix, Map<String, String> fields) {
        ExampleCacheEntry entry = new ExampleCacheEntry(
                Integer.parseInt(fields.get("intField")),
                fields.get("stringField"),
                new Date(Long.parseLong(fields.get("dateField"))));
        return entry;
    }

    @Override
    public Map<String, String> marshall(ExampleCacheEntry e) {
        Map<String, String> serailzed = new HashMap<>();
        serailzed.put("intField", String.valueOf(e.intField));
        serailzed.put("stringField", e.stringField);
        serailzed.put("dateField", String.valueOf(e.dateField.getTime()));
        return serailzed;
    }
}

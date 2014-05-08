package datastruct.cache.example;

import datastruct.cache.ICacheRecord;

import java.util.Date;

/**
 * Created by Anurag on 3/15/14.
 */
public class ExampleCacheEntry implements ICacheRecord {

    public int intField;
    public String stringField;
    public Date dateField;

    public ExampleCacheEntry(int intField, String stringField, Date dateField){
        this.intField = intField;
        this.stringField = stringField;
        this.dateField = dateField;
    }

    @Override
    public String id() {
        return String.valueOf(hashCode());
    }
}
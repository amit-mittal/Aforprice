package datastruct.cache;


/**
 * Created by Anurag on 2/22/14.
 */
public interface ICache<V extends ICacheRecord> {

    public enum CacheType{
        GUAVA
    }

    /**
     * Add entry to the cache
     * @param entry
     * @return
     */
    void put(V entry);

    /**
     * Fetch entry from cache
     * @param key Unique key identifying the cache entry
     * @param clazz Class of the cache entry object
     * @return
     */
    V get(String key, Class clazz);
}
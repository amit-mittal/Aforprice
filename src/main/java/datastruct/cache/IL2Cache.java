package datastruct.cache;

/**
 * Created by Anurag on 2/26/14.
 */
public interface IL2Cache<E extends ICacheRecord>{
    /**
     * Stores into cache
     * @param e
     */
    public void store(E e);

    /**
     * Retrieves from cache
     * @param k Id of the cache entry
     * @param clazz Class of the entry to be retrieved
     * @return
     */
    public E retrieve(String k, Class<?> clazz);
}

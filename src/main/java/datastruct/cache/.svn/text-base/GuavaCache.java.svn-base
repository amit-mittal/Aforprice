package datastruct.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anurag on 2/26/14.
 */
public class GuavaCache<V extends ICacheRecord> implements ICache<V>{

    private Cache<String, V> cache;
    private final IL2Cache<V> l2Cache;

    private GuavaCache(final int maxEntries, final IL2Cache<V> l2Cache){
        cache = CacheBuilder.newBuilder().
                maximumSize(maxEntries).
                removalListener(new RemovalListener<String, V>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, V> notification) {
                        l2Cache.store(notification.getValue());
                    }
                }).build();
        this.l2Cache = l2Cache;
    }

    public static <V extends ICacheRecord> GuavaCache<V> newMongoBackingInstance(final int maxEntries) throws UnknownHostException {
           return new GuavaCache<V>(maxEntries, new MongoDbL2Cache<V>());
    }

    @Override
    public void put(V entry) {
        cache.put(entry.id(), entry);
    }

    @Override
    public V get(final String key, final Class clazz) {
        try {
            return cache.get(key, new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return l2Cache.retrieve(key, clazz);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

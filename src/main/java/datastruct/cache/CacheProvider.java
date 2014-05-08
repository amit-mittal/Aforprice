package datastruct.cache;

import global.exceptions.Bhagte2BandBajGaya;
import datastruct.cache.ICache.CacheType;

import java.net.UnknownHostException;

/**
 * Created by Anurag on 2/22/14.
 */
public class CacheProvider {
    public static <V extends ICacheRecord>ICache<V> newCache(CacheType type, int size) throws UnknownHostException {
        switch(type){
            case GUAVA:
                return GuavaCache.newMongoBackingInstance(size);
        }
        //unreachable code
        throw new Bhagte2BandBajGaya("Unknown cache type " + type);
    }
}

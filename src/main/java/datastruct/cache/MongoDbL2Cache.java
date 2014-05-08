package datastruct.cache;

import com.mongodb.*;
import datastruct.cache.serialize.WireManager;
import mongo.MongoFactory;
import util.props.CrawlerProperties;

import java.net.UnknownHostException;
import java.util.Map;

import static util.props.CrawlerProperties.get;
import static mongo.MongoFactory.*;
import static util.props.PropertyTags.MONGO_CACHE_DB;

/**
 * Created by Anurag on 2/26/14.
 */
public class MongoDbL2Cache<V extends ICacheRecord> implements IL2Cache<V>{

    private static CrawlerProperties props = get(true);

    private DB db;

    private static final String ID = "_id";

    public MongoDbL2Cache() throws UnknownHostException {
        MongoFactory.init();
        db = getMongoClient().getDB(props.getProperty(MONGO_CACHE_DB, "cachedb"));
    }

    @Override
    public void store(V v) {
        Map<String, String> serialized = WireManager.getWire(v.getClass()).marshall(v);
        serialized.put(ID, v.id());
        BasicDBObject dbObject = new BasicDBObject(serialized);
        String collName = v.getClass().getCanonicalName().replaceAll("\\.", "_");
        DBCollection collection = db.getCollection(collName);
        collection.insert(dbObject, WriteConcern.NORMAL);
    }

    @Override
    public V retrieve(String k, Class<?> clazz) {
        BasicDBObject query = new BasicDBObject(ID, k);
        String collName = clazz.getCanonicalName().replaceAll("\\.", "_");
        DBCursor cursor = db.getCollection(collName).find(query);
        DBObject dbObject = null;
        if(cursor.hasNext()){
            dbObject = cursor.next();
        }
        if(dbObject != null)
            return (V)WireManager.getWire(clazz).unmarshall("", dbObject.toMap());
        return null;
    }
}
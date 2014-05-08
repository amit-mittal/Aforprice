package mongo;

import com.mongodb.MongoClient;
import util.props.CrawlerProperties;

import java.net.UnknownHostException;

import static util.props.CrawlerProperties.*;
import static util.props.PropertyTags.MONGO_DB_HOST;
import static util.props.PropertyTags.MONGO_DB_PORT;

/**
 * Created by Anurag on 3/2/14.
 */
public class MongoFactory {
    private static MongoClient mongoClient;
    private static CrawlerProperties props = get(true);

    private static boolean isInited = false;

    public static void init() throws UnknownHostException {
        if(isInited)
            return;
        synchronized (MongoFactory.class){
            isInited = true;
            mongoClient = new MongoClient(
                    props.getProperty(MONGO_DB_HOST, "192.168.1"),
                    props.getIntValue(MONGO_DB_PORT, 27017));
        }
    }

    public static MongoClient getMongoClient(){
        return mongoClient;
    }
}

package util.props;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Anurag on 3/1/14.
 */
public class CrawlerProperties extends Properties{

    public static final CrawlerProperties PROPS_WITH_ENV = new CrawlerProperties(true);
    public static final CrawlerProperties PROPS = new CrawlerProperties(false);

    private CrawlerProperties(Properties props){
        super(props);
        load();
    }

    private CrawlerProperties(boolean loadEnv){
        this(System.getProperties());
    }

    public static CrawlerProperties get(boolean envLoaded){
        if(envLoaded)
            return PROPS_WITH_ENV;
        return PROPS;
    }

    private void load(){
        String propFile = System.getProperty("crawler.prop");
        try {
            if(propFile != null)
                load(new FileReader(propFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIntValue(String key, int def){
        return Integer.parseInt(getProperty(key, String.valueOf(def)));
    }
}
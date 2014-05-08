package receipts.parser;

import entities.Retailer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static entities.Retailer.*;
/**
 * Created by Anurag on 5/4/14.
 */
public class ReceiptParserFactory {

    private static Map<Retailer, IReceiptParser> parsers = new HashMap<>();
    static{
        parsers.put(WALMART, new WalmartReceiptParser());
    }

    public static IReceiptParser getParser(Retailer retailer){
        return parsers.get(retailer);
    }
}

package receipts.parser;

import entities.Retailer;

import java.util.List;

/**
 * Created by Anurag on 5/4/14.
 */
public class ParsedReceipt {
    private Retailer retailer;
    private String storeId;
    private String receiptId;
    private List<ReceiptEntry> receiptEntries;
}

package receipts.parser;

import receipts.Receipt;
import receipts.tasks.ParseReceipt;

import java.util.List;

/**
 * Created by Anurag on 5/4/14.
 */
public interface IReceiptParser {
    ParseReceipt parse(Receipt receipt);
}

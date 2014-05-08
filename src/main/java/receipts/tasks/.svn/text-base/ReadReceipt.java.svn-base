package receipts.tasks;

import concurrent.Task;
import global.exceptions.Bhagte2BandBajGaya;
import receipts.Receipt;
import util.FastLogger;
import util.Utils;

import java.io.IOException;

/**
 * This task takes the input image file and moves that file to the staging directory.
 */
public class ReadReceipt extends Task{

    private static final FastLogger LOGGER = FastLogger.create(ReadReceipt.class);
    private final Receipt receipt;

    public ReadReceipt(ReadReceiptNotifyee notifyee, Receipt receipt) {
        super(notifyee);
        this.receipt = receipt;
    }

    @Override
    public void doWork() {
        try {
            Utils.move(receipt.in(), receipt.stage());
        } catch (IOException e) {
            throw new Bhagte2BandBajGaya(e);
        }
    }
}
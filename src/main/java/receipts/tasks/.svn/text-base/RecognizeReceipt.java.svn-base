package receipts.tasks;

import concurrent.Task;
import concurrent.TaskNotifyee;
import global.exceptions.Bhagte2BandBajGaya;
import receipts.Receipt;
import receipts.ocr.OCREngineFactory;
import util.FastLogger;
import util.Utils;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Anurag on 3/30/14.
 */
public class RecognizeReceipt extends Task{

    private final static FastLogger logger = FastLogger.create(RecognizeReceipt.class);
    private final Receipt receipt;

    public RecognizeReceipt(TaskNotifyee<ParseReceipt> notifyee, Receipt receipt) {
        super(notifyee);
        this.receipt = receipt;
    }

    @Override
    public void doWork() {
        OCREngineFactory.getProvider().scanAndConvert(receipt.stage(), receipt.ocr());
        if(Files.exists(receipt.ocr())){
            logger.info("ocr file: %s", receipt.ocr());
            try{
                Utils.move(receipt.stage(), receipt.processed());
            }catch(IOException e){
                e.printStackTrace();;
                throw new Bhagte2BandBajGaya(e);
            }
        }
    }
}
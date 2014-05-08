package receipts.ocr;

import receipts.ocr.abby.AbbyOCREngine;

/**
 * Created by Anurag on 3/30/14.
 */
public class OCREngineFactory {

    private static final AbbyOCREngine ABBY_OCR = new AbbyOCREngine();

    /**
     * Gets the ocr engine responsible for ocr translation. This factory provider should be used to get ocr engine
     * @return
     */
    public static IOCREngine getProvider(){
        return ABBY_OCR;
    }
}

package receipts.ocr;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Anurag on 3/30/14.
 */
public interface IOCREngine {
    /**
     *Reads an image file and converts to text
     * @param inFile
     * @param outDir
     * @return
     */
    public Path scanAndConvert(Path inFile, Path outDir);
}
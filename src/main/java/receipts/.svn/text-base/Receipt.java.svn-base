package receipts;

import global.exceptions.Bhagte2BandBajGaya;
import util.props.PropertyTags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.props.CrawlerProperties.PROPS_WITH_ENV;
import static util.props.PropertyTags.*;

/**
 * Created by Anurag on 4/26/14.
 */
public class Receipt {

    private static final Path STAGING_DIR = Paths.get(PROPS_WITH_ENV.getProperty(RECEIPTS_STAGING_DIR, "C:\\Temp\\Receipts_Test\\staging"));
    private final Path PROCESSED_DIR = Paths.get(PROPS_WITH_ENV.getProperty(RECEIPTS_PROCESSED_DIR, "C:\\Temp\\Receipts_Test\\processed"));
    private static final String DELIM = PROPS_WITH_ENV.getProperty(PropertyTags.RECEIPTS_INFO_DELIM, "_");

    private final String fileName;
    private final String remoteFileName;
    private final String email;
    private final String retailer;
    private final Path in;
    private final Path stage;
    private final Path processed;
    private final Path ocr;

    public Receipt(Path inFile) throws IOException {
        this.fileName = inFile.getFileName().toString();
        int first = fileName.indexOf(DELIM);
        int last = fileName.lastIndexOf(DELIM);

        if(first == last)
            throw new Bhagte2BandBajGaya("Unsupported format: Expects retailer_email_remotefilename.png, found " + fileName);
        //first part is retailer
        retailer = fileName.substring(0, first);
        email = fileName.substring(first + 1, last);
        remoteFileName = fileName.substring(last + 1);
        in = inFile;
        if(!Files.exists(STAGING_DIR.resolve(retailer))){
            Files.createDirectories(STAGING_DIR.resolve(retailer));
        }
        stage = STAGING_DIR.resolve(retailer).resolve(fileName);
        if(!Files.exists(PROCESSED_DIR.resolve(retailer))){
            Files.createDirectories(PROCESSED_DIR.resolve(retailer));
        }
        processed = PROCESSED_DIR.resolve(retailer).resolve(fileName);
        ocr = PROCESSED_DIR.resolve(retailer).resolve(fileName + ".txt");
    }

    public String getEmail(){
        return email;
    }

    public String getRetailer(){
        return retailer;
    }

    /**
     * The incoming full path of the receipt image file
     * @return
     */
    public Path in(){
        return in;
    }

    /**
     * As soon as the receipt image arrives, it is copied to the staging directory.
     * Staging full path of the receipt image file
     * @return
     */
    public Path stage(){
        return stage;
    }

    /**
     * Once the receipt has been converted to text using OCR engine, it is moved to the processed directory.
     * Full path of the processed receipt image file
     * @return
     */
    public Path processed(){
        return processed;
    }

    /**
     * Full path of the file converted from the original image file using the OCR engine
     * @return
     */
    public Path ocr(){
        return ocr;
    }

    /**
     * The receipt image file on the remote device could be different from the name sent over.
     * Remote receipt image file name
     * @return
     */
    public String remoteFileName(){
        return remoteFileName;
    }
}
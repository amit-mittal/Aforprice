/*
 * @author Chirag Maheshwari
 */
package sitemap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import util.UtilityLogger;

public class GzipDecompress {
	
	private final static int SIZE = 1024;
	private static final Logger logger = Logger.getLogger(GzipDecompress.class);
	
	/*private static void moveCompressed(String filename, String destinationDir) {
		String newDestination = destinationDir + "compressed" + File.separator;
		File path = new File(newDestination);
		if(!path.exists())
			path.mkdirs();
		
		File aFile = new File(destinationDir+filename);
		aFile.renameTo(new File(newDestination+filename));
	}*/
	
	private static String Gzip(String filename, String destinationDir) {
		GZIPInputStream gzipInput = null;
		String outFilename = null;
		
		try {
			gzipInput = new GZIPInputStream(new FileInputStream(destinationDir+filename));
			outFilename = filename.substring(0, filename.lastIndexOf('.'));
			OutputStream out = new FileOutputStream(destinationDir+outFilename);
			byte[] buf = new byte[SIZE];
			int len;
			
			while((len = gzipInput.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			UtilityLogger.logInfo("Uncompressed file: "+filename+" to: "+destinationDir+outFilename);
			gzipInput.close();
			out.close();
			
			//moveCompressed(filename, destinationDir);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return outFilename;
	}
	
	public String Uncompress(String filePath) {
		String destinationDir = filePath.substring(0, filePath.lastIndexOf(File.separator)+1);
		String filename = filePath.substring(filePath.lastIndexOf(File.separator)+1);
		String extension = filename.substring(filename.lastIndexOf('.') + 1);
		
		if(extension.equalsIgnoreCase("gz"))
			return Gzip(filename, destinationDir);
		
		return null;
	}
	
	/*public static void main(String[] args) {
		Uncompress("/home/chirag"+"//sitemap_browse.xml.gz");
	}*/
}

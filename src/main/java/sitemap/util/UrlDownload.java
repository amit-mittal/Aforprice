/*
 * @author Chirag Maheshwari
 */
package sitemap.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import util.UtilityLogger;

public class UrlDownload {
	
	private final static int SIZE = 10240;
	private static final Logger logger = Logger.getLogger(UrlDownload.class);
	
	private static String fileUrl(String fAddress, 
			String localFileName, 
			String destinationDir) {
		OutputStream outStream = null;
		URLConnection urlCon = null;
		InputStream inpStream = null;
		
		try {
			UtilityLogger.logInfo("Downloading: "+fAddress);
			URL url;
			byte[] buf;
			int byteRead, byteWritten = 0;
			
			url = new URL(fAddress);
			outStream = new BufferedOutputStream(new FileOutputStream(destinationDir+localFileName));
			
			urlCon = url.openConnection();
			inpStream = urlCon.getInputStream();
			
			long startTime = System.currentTimeMillis();
			boolean process = true;
			buf = new byte[SIZE];
			while((byteRead = inpStream.read(buf)) != -1) {
				outStream.write(buf, 0, byteRead);
				byteWritten += byteRead;
				if((startTime+600000) < System.currentTimeMillis()) {
					process = false;
					break;
				}
			}
			
			if(process)
				UtilityLogger.logInfo("Downloaded: "+fAddress +" with Bytes: "+byteWritten + " at location: " + destinationDir+localFileName);
			else {
				localFileName = null;
				logger.error("Timeout occured, Error in downloading url: "+fAddress);
			}
		} catch(Exception e) {
			logger.error("Error saving the following sitemap/robots file"+"url: " + fAddress + " path: " + destinationDir+localFileName, e);
			e.printStackTrace();
		} finally {
			try {
				inpStream.close();
				outStream.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return localFileName;
	}
	
	public static String fileDownload(String url, String destinationDir) {
		int slashIndex = url.lastIndexOf('/');
		
		String rootFilename = url.substring(slashIndex+1), filename = rootFilename;
		int fileNumber = 2;
		do {
			File checkExistence = new File(destinationDir+filename);
			if(!checkExistence.exists())
				break;
			filename = Integer.toString(fileNumber).concat("_"+rootFilename);
			fileNumber++;
		} while(true);
		return fileUrl(url, filename, destinationDir);
	}
	
	/*public static void main(String[] args) {
		String url = "http://sitemap.target.com/wcsstore/SiteMap/c/sitemap_001.xml.gz";
		//String url = "http://www.amazon.com/robots.txt";
		String destination = Constants.SITEMAP_ROBOTS_FILES.path("amazon", new Date());
		File path = new File(destination);
		if(!path.exists())
			path.mkdirs();
		
		fileDownload(url, destination);
	}*/
}

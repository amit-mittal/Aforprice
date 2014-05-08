/*
 * @author Chirag Maheshwari
 */
package sitemap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import parsers.robotsProtocol.RobotsTxt;

import sitemap.entities.siteIndexXML.Sitemapindex;
import sitemap.entities.siteIndexXML.TSitemap;
import sitemap.entities.siteMapXML.TUrl;
import sitemap.entities.siteMapXML.Urlset;
import sitemap.util.GzipDecompress;
import sitemap.util.UrlDownload;
import util.Constants;
import util.UtilityLogger;

public class UrlCollector {
	
	private static final String ROBOTS_FILE = "/robots.txt";
	
	private final String retailerId;
	private final String baseURI;
	
	private final String filesDirectory;
	
	private List<String> baseSitemapLinks;
	
	private final String sitIndexXML_PACKAGE = "sitemap.entities.siteIndexXML";
	private final String sitMapXML_PACKAGE = "sitemap.entities.siteMapXML";
	
	private static final Logger logger = Logger.getLogger(UrlCollector.class);
	
	public UrlCollector(String retailerId, String link) {
		this.retailerId = retailerId;
		this.baseURI = link;
		
		filesDirectory = Constants.SITEMAP_ROBOTS_FILES.path(this.retailerId, new Date());
		
		//make sure the path exists first...if not create one
		File path = new File(filesDirectory);
		if(!path.exists())
			path.mkdirs();
		
		baseSitemapLinks = new ArrayList<String>();
	}
	
	public void startCollection() {
		
		//Download the robots.txt file
		String robotsFile = downloadRobotsFile(this.baseURI, this.filesDirectory);
		
		//parse the robots.txt file downloaded and get the base sitemap links
		RobotsTxt robotsParser = new RobotsTxt(this.filesDirectory+robotsFile);
		baseSitemapLinks.addAll(robotsParser.getBaseSitemapIndexes());
		
		// download and extract the URl's
		downloadAndExtract(baseSitemapLinks);
		/*String filename;
		GzipDecompress uncompressor = new GzipDecompress();
		for(String url: baseSitemapLinks) {
			filename = downloadAnyFile(url, filesDirectory);
			if(ifCompressed(filename))
				filename = uncompressor.Uncompress(filesDirectory+filename);
			baseSitemapFiles.add(filename);
		}*/
	}
	
	private void downloadAndExtract(List<String> siteIndexUrls) {
		GzipDecompress uncompressor = new GzipDecompress();
		List<String> filenames = new ArrayList<String>();
		String filename;
		
		for(String url: siteIndexUrls) {
			filename = downloadAnyFile(url, filesDirectory);
			if(filename == null)
				continue;
			if(ifCompressed(filename))
				filename = uncompressor.Uncompress(filesDirectory+filename);
			filenames.add(filename);
		}
		
		for(String file: filenames) {
			extractUrls(file);
		}
	}
	
	private void getLinks(String filename) {
		JAXBContext siteMapXML;
		Unmarshaller unmarshalMap;
		try {
			siteMapXML = JAXBContext.newInstance(sitMapXML_PACKAGE);
			unmarshalMap = siteMapXML.createUnmarshaller();
			
			Urlset urlset = (Urlset)unmarshalMap.unmarshal(new File(this.filesDirectory + filename));
			List<TUrl> siteMapList = urlset.getUrl();
			for(TUrl siteMap: siteMapList) {
				UtilityLogger.logInfo("Final leaf link found: "+siteMap.getLoc());
			}
		} catch(Exception e) {
			logger.error("Error in getting links from : "+filesDirectory+filename+" Error message: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void extractUrls(String filename) {
		JAXBContext siteIndexXML;
		Unmarshaller unmarshalIndex;
		try {
			siteIndexXML = JAXBContext.newInstance(sitIndexXML_PACKAGE);
			unmarshalIndex = siteIndexXML.createUnmarshaller();
			
			Sitemapindex sitemapIndex = (Sitemapindex)unmarshalIndex.unmarshal(new File(this.filesDirectory + filename));
			
			List<TSitemap> tSiteIndexList = sitemapIndex.getSitemap();
			List<String> siteIndexLinks = new ArrayList<String>();
			
			for(TSitemap siteIndex: tSiteIndexList) {
				siteIndexLinks.add(siteIndex.getLoc());
				UtilityLogger.logInfo("Links added: "+siteIndex.getLoc());
			}
			downloadAndExtract(siteIndexLinks);
		} catch(Exception e) {
			//System.out.println("found a sitemap file");
			getLinks(filename);
		}
	}
	
	private static String downloadRobotsFile(String baseURI, String filesDirectory) {
		return UrlDownload.fileDownload(baseURI+ROBOTS_FILE, filesDirectory);
	}
	
	private static String downloadAnyFile(String url, String filesDirectory) {
		return UrlDownload.fileDownload(url, filesDirectory);
	}
	
	private boolean ifCompressed(String filename) {
		String extension = filename.substring(filename.lastIndexOf('.') + 1);
		
		if(extension.equalsIgnoreCase("gz"))
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		UrlCollector collect = new UrlCollector("TARGET", "http://www.target.com");
		collect.startCollection();
	}
}
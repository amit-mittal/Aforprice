package uploader.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import util.Constants;

public class BBYOpenUtils
{
	private static final Logger logger = Logger.getLogger("BBYOpenUtils");
	private static final int BBY_OPEN_CRAWL_FREQ = Integer.getInteger("BBY_OPEN_CRAWL_FREQ", 200); // 200ms
	
	public static void createSaveLocation(String folder)
	{
		File path = new File(folder);
		if (!path.exists())
		{
			path.mkdirs();
		}
		logger.info("Files will be saved in " + folder);
	}
	
	public static String getFileNameToSave(int pageNum, String folder)
	{
		return folder + "/bbyopen_page_" + pageNum + ".xml";
	}

	public static long downloadURL(int pageNum, boolean processDownloadedPages, 
			String folder, String BBurl, long lastDownloadTime)
	{
		try
		{
			if (processDownloadedPages) return lastDownloadTime;
			long time = System.currentTimeMillis();
			long pause = BBY_OPEN_CRAWL_FREQ - (time - lastDownloadTime);
			if (pause > 0)
			{
				logger.info("*************** Sleeping for :" + pause + " ms");
				try
				{
					Thread.sleep(pause);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			URL url = new URL(BBurl + "&page=" + pageNum);
			String fileName = BBYOpenUtils.getFileNameToSave(pageNum, folder );
			logger.info("Downloading page " + pageNum + " into file " + fileName);
			long sleepTime = 10;
			while (true)
				try
				{
					lastDownloadTime = System.currentTimeMillis();
					logger.info(url);
					FileUtils.copyURLToFile(url, new File(fileName));
					logger.info("Done downloading " + fileName);
					break;
				} catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						logger.info("Sleeping for  " + sleepTime + " sec");
						Thread.sleep(sleepTime++ * 1000);
					} catch (Exception ee)
					{
					}
				}
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return lastDownloadTime;
	}

}

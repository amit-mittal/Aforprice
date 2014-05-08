package uploader.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.Constants;
import util.UtilityLogger;
import util.Utils;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.Retailer;

public class BBYOpenCategoryUploader extends NeoCategoryUploader
{
	private static final Logger logger = Logger.getLogger("BBYOpenCategoryUploader");
	private static final String CATEGORIES_TAG = "categories";
	private static final String CATEGORY_TAG = "category";
	private static final String TOTAL_PAGES = "totalPages";
	//public static final String PRICE_FROM_DATE_TAG = "priceUpdateDate";
	public static Date currDate = new Date();
	public static Date runDate = currDate;
	private static long lastDownloadTime = System.currentTimeMillis() - 1000000;
	private static final boolean processDownloadedPages = System.getProperty("RUN_DATE") != null;
	private static final int pageSize = Integer.getInteger("BBY_OPEN_PAGE_SIZE", 100);
	private static String SAVE_FILES_IN_FOLDER = Constants.CATEGORY_HTML_FILES.path("bbyopen", runDate);
	private static final String BBY_OPEN_URL = "http://api.remix.bestbuy.com/v1/categories?pageSize=" + pageSize
			+ "&apiKey=q4zry7es69ggpdznwpkqn4nm";
	private Map<String, Category> categoryById = new HashMap<>();

	public BBYOpenCategoryUploader() throws UploaderException
	{
		super(Retailer.BESTBUY);
		updateDateOverride();
		BBYOpenUtils.createSaveLocation(SAVE_FILES_IN_FOLDER);
	}

	private void updateDateOverride()
	{
		String runDateStr = System.getProperty("RUN_DATE");
		if (runDateStr != null)
		{
			try
			{
				runDate = new SimpleDateFormat("yyyyMMdd").parse(runDateStr);
				SAVE_FILES_IN_FOLDER = Constants.PRODUCT_SUMMARY_HTML_FILES.path("bbyopen", "", runDate, false);
			} catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	private int getTotalPageCount(String fileName)
	{
		int pageCount = 0;
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(fileName));
			// get all the products
			NodeList nodes = doc.getElementsByTagName(CATEGORIES_TAG);
			assert (nodes.getLength() == 1);
			Element elem = (Element) nodes.item(0);
			pageCount = Integer.parseInt(elem.getAttribute(TOTAL_PAGES));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return pageCount;
	}

	public void walkAndStore()
	{
		try
		{
			ConfigParms.getInstance().setMode(RUNTIME_MODE.FILEMODE);
			ConfigParms.getInstance().setFileRunDate(new Date());
			// lets store existing categories first in the cache for rec
			if (this.isDebug())
				setExistingCategories(new CategoryDAO().getAllChildCategoriesForCategory(this.getDebuCategory(),
						this.retailerId));
			else
				setExistingCategories(new CategoryDAO().getActiveCategoriesForRetailer(this.retailerId));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			lastDownloadTime = BBYOpenUtils.downloadURL(1, processDownloadedPages, SAVE_FILES_IN_FOLDER,BBY_OPEN_URL, lastDownloadTime );
			int totalPages = getTotalPageCount(BBYOpenUtils.getFileNameToSave(1, SAVE_FILES_IN_FOLDER ));
			logger.info("Total no of pages = " + totalPages);
			for (int i = 1; i <= totalPages; i++)
			{
				if (i > 1)
					lastDownloadTime = BBYOpenUtils.downloadURL(i, processDownloadedPages, SAVE_FILES_IN_FOLDER,BBY_OPEN_URL, lastDownloadTime );
				List<Category> categories = extractCategories(BBYOpenUtils.getFileNameToSave(i, SAVE_FILES_IN_FOLDER ), db);
				this.storeCategories(categories);
				for(Category category : categories) 
					categoryById.put(category.getRetailerCategoryId().split("-")[0], category);
			}

			//This is a hack.  Parent Id are available only after storing category so we need to store categories
			//Get the parent id & store them again.
			for(Category category : categoryById.values())
			{
				String[] categorySplit = category.getRetailerCategoryId().split("-");
				String parentId = "";
				if( categorySplit.length == 2)
					parentId = categorySplit[1];
				if( parentId.equals("")) continue;
				
				Category parentCategory = categoryById.get(parentId);
				if( parentCategory == null)
				{
					logger.warn("no mapaping for parent category" + category.getName());
					category.setActive(false);
					continue;
				}	
				category.setParentCategoryId(parentCategory.getCategoryId());
			}
			
			this.storeCategories(new LinkedList<>(categoryById.values()));
			
			return;
		} catch (DAOException e)
		{
			e.printStackTrace();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			shutDown();
			if (failedParsingURLs.length() > 0)
				UtilityLogger.logError("Failed Parsing Below URLs\n" + failedParsingURLs.toString());
			if (failedToStoreCategories.length() > 0)
				UtilityLogger.logError("Failed To Store Below Categories in DB\n" + failedToStoreCategories.toString());
			Utils.printUrlGetTimings();
		}
	}

	private List<Category> extractCategories(String fileName, DocumentBuilder db) throws IOException
	{
		FileInputStream fis = new FileInputStream(fileName);
		List<Category> categories = new LinkedList<Category>();
		try
		{
			Document doc = db.parse(fis);
			// get all the products
			NodeList nodes = doc.getElementsByTagName(CATEGORY_TAG);
			for (int i = 0; i < nodes.getLength(); i++)
			{
				Node node = nodes.item(i);
				NodeList categoryPathNode = ((Element) node).getElementsByTagName("path");
				if (categoryPathNode.getLength() == 0)
					continue;
				int totalCategories = ((Element) categoryPathNode.item(0)).getElementsByTagName("name").getLength();
				String categoryName = ((Element) categoryPathNode.item(0)).getElementsByTagName("name")
						.item(totalCategories - 1).getTextContent();
				String categoryId = ((Element) categoryPathNode.item(0)).getElementsByTagName("id")
						.item(totalCategories - 1).getTextContent();
				String parentCategoryName = "";
				String parentCategoryId = "";
				if (totalCategories > 2)
				{
					parentCategoryName = ((Element) categoryPathNode.item(0)).getElementsByTagName("name")
							.item(totalCategories - 2).getTextContent();
					parentCategoryId = ((Element) categoryPathNode.item(0)).getElementsByTagName("id")
							.item(totalCategories - 2).getTextContent();
					
				}
				
				int totalSubCategories = ((Element) node).getElementsByTagName("subCategories").item(0).getChildNodes()
						.getLength();
				CategoryType catType;
				if (totalSubCategories == 0)
					catType = CategoryType.TERMINAL;
				else
					catType = CategoryType.PARENT;
				
				if( categoryName.length() >= 100)
				{
					logger.warn("skip the category, name too long " + categoryName );
					continue;
				}
				categories.add(new CategoryBuilder(Retailer.ID.BESTBUY, 0, parentCategoryName , categoryName, "").
						catType(catType).retailerCategoryId(categoryId + "-" + parentCategoryId).build());
			}
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			fis.close();
		}
		return categories;
	}

	/**
	 * @param args
	 * @throws UploaderException
	 */
	public static void main(String[] args) throws UploaderException
	{
		BBYOpenCategoryUploader uploader = new BBYOpenCategoryUploader();
		try
		{
			uploader.walkAndStore();
			uploader.terminate();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	protected List<Category> getRootCategories()
	{
		return null;
	}

	@Override
	protected List<Category> getSubCategories(int level, Category parentCat, org.jsoup.nodes.Document parentCatDoc)
	{
		return null;
	}

}

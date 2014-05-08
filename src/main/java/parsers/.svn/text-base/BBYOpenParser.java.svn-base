/******************************************
 ** BBYOpenParser.java Created @Jun 15, 2012 12:15:14 AM
 ** @author: Jayanta Hazra
 **
 ******************************************/
package parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import parsers.html.ClassNames;
import parsers.util.DiffEngine;
import stores.NullProductStore;
import stores.ProductStore;
import stores.ProductStoreDbAsync;
import uploader.util.BBYOpenUtils;
import util.Constants;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import db.dao.ProductAttrMappingMgr;
import entities.Category;
import entities.GenericProduct;
import entities.ProdAttributeDetails;
import entities.ProductSummary;
import entities.Retailer;

public class BBYOpenParser extends ProductsParser
{

	private static final Logger logger = Logger.getLogger("BBYOpenParser");

	private static final String PRODUCTS_TAG = "products";
	private static final String PRODUCT_TAG = "product";
	private static final String TOTAL_PAGES = "totalPages";
	public static final String PRICE_FROM_DATE_TAG = "priceUpdateDate";
	private HashMap<SortedSet<String>, Integer> productAttributesMap = new HashMap<SortedSet<String>, Integer>();
	private DiffEngine<String, GenericProduct> prodDiffEngine = null;
	private DiffEngine<String, GenericProduct> priceDiffEngine = null;
	private Map<String, GenericProduct> products = new HashMap<String, GenericProduct>();
	private Map<String, GenericProduct> prices = new HashMap<String, GenericProduct>();
	private Map<String, ProdAttributeDetails> prodAttrMapping = ProductAttrMappingMgr.getInstance()
			.getAttributeMapping(ProductAttrMappingMgr.ATTR_TYPES.PROD);
	private Map<String, ProdAttributeDetails> priceAttrMapping = ProductAttrMappingMgr.getInstance()
			.getAttributeMapping(ProductAttrMappingMgr.ATTR_TYPES.PRICE);
	public static Date currDate = new Date();
	public static Date runDate = currDate;
	private static long lastDownloadTime = System.currentTimeMillis() - 1000000;
	private static final boolean processDownloadedPages = System.getProperty("RUN_DATE") != null;

	private static final int pageSize = Integer.getInteger("BBY_OPEN_PAGE_SIZE", 100);
	// http://api.remix.bestbuy.com/v1/products?pageSize=50&page=1&apiKey=q4zry7es69ggpdznwpkqn4nm
	private static final String BBY_OPEN_URL = "http://api.remix.bestbuy.com/v1/products?pageSize=" + pageSize
			+ "&apiKey=q4zry7es69ggpdznwpkqn4nm&sort=bestSellingRank.asc";
	private static final int BBY_OPEN_CRAWL_FREQ = Integer.getInteger("BBY_OPEN_CRAWL_FREQ", 200); // 200ms
	// "C:/Users/Jaanu/Downloads/work/bbyopen"
	private static String SAVE_FILES_IN_FOLDER = Constants.PRODUCT_SUMMARY_HTML_FILES.path("bbyopen", "", runDate,
			false);
	private static final List<String> detailedAttributeNames = Arrays.asList("categoryPath", "shipping",
			"includedItemList", "protectionPlanDetails", "languageOptions", "protectionPlans",
			"frequentlyPurchasedWith", "accessories", "relatedProducts");
	private static final GenericProductComparator COMPARATOR = new GenericProductComparator();
	private final HashMap<String, Category> bbyCategoryIdToOurCategory = new HashMap<>();

	public BBYOpenParser(String retailerId) throws DAOException
	{
		super(retailerId);
		updateDateOverride();
		BBYOpenUtils.createSaveLocation(SAVE_FILES_IN_FOLDER);
		extractCateogyMap();
	}

	private void extractCateogyMap() throws DAOException
	{
		CategoryDAO categoryReader = new CategoryDAO();
		List<Category> categories = categoryReader.getAllCategoriesForRetailer(this.getRetailerId());
		
		for (Category category : categories)
		{
			String[] categorySplit = category.getRetailerCategoryId().split("-");
			String bbyCategoryId = "";
			if( categorySplit.length >= 1)
				bbyCategoryId = categorySplit[0];
			bbyCategoryIdToOurCategory.put(bbyCategoryId, category );
		}
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

	private void initializeDiffEngine()
	{
		Map<String, GenericProduct> prodsFromDB = getCurrentProducts();
		Map<String, GenericProduct> priceFromDB = getCurrentPrices();
		prodDiffEngine = new DiffEngine<String, GenericProduct>(prodsFromDB, COMPARATOR, false);
		priceDiffEngine = new DiffEngine<String, GenericProduct>(priceFromDB, COMPARATOR, true);
	}

	// should move to base
	private Map<String, GenericProduct> getCurrentProducts()
	{
		Map<String, GenericProduct> prodMap = new HashMap<String, GenericProduct>();
		if (products.size() < 1)
			return prodMap;
		DbConnection dbConnection = null;
		try
		{
			dbConnection = DbConnectionPool.get().getConnection();
			Connection connection = dbConnection.getConnection();
			StringBuilder query = new StringBuilder(Queries.GET_GENERIC_PRODUCTS_SELECTIVE).append("IN ( '");
			for (Iterator<String> it = products.keySet().iterator(); it.hasNext();)
				query.append(it.next()).append("','");
			query.append("dummy' )");
			PreparedStatement stmt = connection.prepareStatement(query.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				GenericProduct prod = new GenericProduct(prodAttrMapping);
				prod.populateProductAttrs(rs);
				prodMap.put(prod.getProdIdInternal(), prod);
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally
		{
			if (dbConnection != null)
				DbConnectionPool.get().releaseConnection(dbConnection);
		}
		return prodMap;
	}

	private Map<String, GenericProduct> getCurrentPrices()
	{
		Map<String, GenericProduct> prodMap = new HashMap<String, GenericProduct>();
		if (prices.size() < 1)
			return prodMap;
		DbConnection dbConnection = null;
		try
		{
			dbConnection = DbConnectionPool.get().getConnection();
			Connection connection = dbConnection.getConnection();
			StringBuilder query = new StringBuilder(Queries.GET_GENERIC_PRICES_SELECTIVE).append("IN ( '");
			for (Iterator<String> it = products.keySet().iterator(); it.hasNext();)
				query.append(it.next()).append("','");
			query.append("dummy' )");
			PreparedStatement stmt = connection.prepareStatement(query.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				GenericProduct prod = new GenericProduct(priceAttrMapping);
				prod.populateProductAttrs(rs);
				prodMap.put(prod.getProdIdInternal(), prod);
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally
		{
			if (dbConnection != null)
				DbConnectionPool.get().releaseConnection(dbConnection);
		}
		return prodMap;
	}

	private void processNewRecords()
	{
		for (Iterator<Entry<String, GenericProduct>> it = products.entrySet().iterator(); it.hasNext();)
		{
			Entry<String, GenericProduct> entry = it.next();
			String key = entry.getKey();
			GenericProduct value = entry.getValue();
			prodDiffEngine.process(key, value);
		}
		for (Iterator<Entry<String, GenericProduct>> it = prices.entrySet().iterator(); it.hasNext();)
		{
			Entry<String, GenericProduct> entry = it.next();
			String key = entry.getKey();
			GenericProduct value = entry.getValue();
			priceDiffEngine.process(key, value);
		}
	}

	@Override
	public List<ProductSummary> parse(String pageUrl, org.jsoup.nodes.Document doc, int categoryId, String categoryName, int startIndex, int count)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getNextURL(org.jsoup.nodes.Document doc)
	{
		return null;
	}

	@Override
	protected ClassNames getClassNames()
	{
		return null;
	}

	@Override
	public void parseAndSave(ProductStore productStore) throws Exception
	{
		// int pageNum = 1;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		 downloadURL(1);

		int totalPages = getTotalPageCount(BBYOpenUtils.getFileNameToSave(1, SAVE_FILES_IN_FOLDER));
		logger.info("Total no of pages = " + totalPages);
		for (int i = 1; i <= totalPages; i++)
		{
			if (i > 1)
				downloadURL(i);

			// if(System.getProperty("RUN_DATE") == null )continue;
			processProducts(new File(BBYOpenUtils.getFileNameToSave(i, SAVE_FILES_IN_FOLDER)).toURI().toURL()
					.toString(), db);
			/*logger.info("Diff Engine Init..start");
			initializeDiffEngine();
			logger.info("Diff Engine Init..done. Process New Records Start");
			processNewRecords();
			logger.info("Process New Records done. Persist start");
			persistRecords();*/
			saveProductSummary(productStore);
			//logger.info("Persist start done");
			cleanup();
			logger.info("Page = " + i + ". Product Attributes table size " + productAttributesMap.size());
		}
		return;
	}

	private void saveProductSummary(ProductStore productStore)
	{
		List<ProductSummary> productsToSave = new ArrayList<ProductSummary>(products.size());
		for (String id : products.keySet())
		{
			GenericProduct product = products.get(id);
			GenericProduct price = prices.get(id);
			
			Category category =  (Category) bbyCategoryIdToOurCategory.get(product.getAttribute("PROD_CATEGORY"));
			int categoryId = -1;
			String categoryName = "";
			if( category!=null ) 
			{
				categoryId = category.getCategoryId();
				categoryName = category.getName();
			}

			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = (String) product.getAttribute("TITLE");
			b.price = ((Float) price.getAttribute("SALE_PRICE")).doubleValue();
			b.url = (String) product.getAttribute("PROD_URL");
			b.desc = null;
			b.imageUrl = (String) product.getAttribute("IMG_URL");
			b.model = (String) product.getAttribute("MODEL_ID");
			
			try
			{
				b.reviewRating = -1;
				String custReviewAvg = (String)product.getAttribute("CUSTOMER_REVIEW_AVERAGE");
				if( custReviewAvg != null && custReviewAvg.trim().length() != 0)
					b.reviewRating = Double.parseDouble(custReviewAvg);
			}
			catch(Exception e)
			{
				b.reviewRating = -1;
				logger.error("Error getting review ratings", e);
			}
			
	     	try
			{
	     		b.numReviews = -1;
	     		String numReview = (String)product.getAttribute("CUSTOMER_REVIEW_COUNT");
	     		if(numReview != null && numReview.trim().length() != 0)
	     			b.numReviews = Integer.parseInt(numReview);
			}
			catch(Exception e)
			{
				b.numReviews = -1;
				logger.error("Error getting customer review count ", e);
			}

	     	try
			{
	     		b.salesRank = -1;
	     		String saleRank = (String)product.getAttribute("BEST_SELLING_RANK");
	     		if(saleRank != null && saleRank.length() != 0)
	     			b.salesRank = Integer.parseInt(saleRank);
			}
			catch(Exception e)
			{
				b.salesRank = -1;
				logger.error("Error getting sales rank ", e);
			}

			b.downloadTime = new Date();
			ProductSummary productSummary = b.build();
			if(!ProductUtils.isProductValid(productSummary))
				logger.warn("Invalid Product:" + product);
			productsToSave.add(productSummary);
			logger.info("New Prod: " + productSummary.getName());
		}
		productStore.save(productsToSave);

	}

	private void cleanup()
	{
		//prodDiffEngine.clearState();
		//priceDiffEngine.clearState();
		products.clear();
		prices.clear();
	}

	private void persistRecords()
	{
		DbConnection dbConnection = null;
		try
		{
			dbConnection = DbConnectionPool.get().getConnection();
			Connection connection = dbConnection.getConnection();
			for (DiffEngine<String, GenericProduct> diffEngine : Arrays.asList(prodDiffEngine, priceDiffEngine))
			{
				logger.info("Persist...UPDATE=" + diffEngine.getUpdates().size() + " INSERT="
						+ diffEngine.getInserts().size() + " NO_CHANGE=" + diffEngine.getNoChange().size()
						+ " CACHE_SIZE=" + prodDiffEngine.getCacheSize());
				for (Iterator<Entry<String, GenericProduct>> it = diffEngine.getExpires().entrySet().iterator(); it
						.hasNext();)
				{
					Entry<String, GenericProduct> entry = it.next();
					String sql = entry.getValue().getExpireSQL();
					logger.info("SQL = " + sql);
					Statement stmt = connection.createStatement();
					int rs = stmt.executeUpdate(sql);
				}
				for (Iterator<Entry<String, GenericProduct>> it = diffEngine.getUpdates().entrySet().iterator(); it
						.hasNext();)
				{
					Entry<String, GenericProduct> entry = it.next();
					String sql = entry.getValue().getUpdateSQL();
					logger.info("SQL = " + sql);
					Statement stmt = connection.createStatement();
					int rs = stmt.executeUpdate(sql);
				}
				for (Iterator<Entry<String, GenericProduct>> it = diffEngine.getInserts().entrySet().iterator(); it
						.hasNext();)
				{
					Entry<String, GenericProduct> entry = it.next();
					String sql = entry.getValue().getInsertSQL();
					logger.info("SQL = " + sql);
					Statement stmt = connection.createStatement();
					int rs = stmt.executeUpdate(sql);
				}
				Map<String, GenericProduct> noChanges = diffEngine.getNoChange();
				if (noChanges.size() > 0)
				{
					GenericProduct prod = noChanges.values().iterator().next();
					String tableName = prod.getAttribMapper().get(GenericProduct.TABLE_NAME).getInternalName();
					StringBuilder sql = new StringBuilder("update ").append(tableName);
					sql.append(" set UPDATE_TIME = '" + new Timestamp(currDate.getTime())
							+ "' where THRU_DATE IS NULL AND PROD_ID_INTERNAL in ('");
					for (Iterator<String> it = noChanges.keySet().iterator(); it.hasNext();)
						sql.append(it.next()).append("','");
					sql.append("DUMMY' )");
					logger.info("SQL = " + sql);
					Statement stmt = connection.createStatement();
					int rs = stmt.executeUpdate(sql.toString());
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally
		{
			if (dbConnection != null)
				DbConnectionPool.get().releaseConnection(dbConnection);
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
			NodeList nodes = doc.getElementsByTagName(PRODUCTS_TAG);
			assert (nodes.getLength() == 1);
			Element elem = (Element) nodes.item(0);
			pageCount = Integer.parseInt(elem.getAttribute(TOTAL_PAGES));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return pageCount;
	}

	private void downloadURL(int pageNum)
	{
		try
		{
			if (processDownloadedPages)
				return;
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
			URL url = new URL(BBY_OPEN_URL + "&page=" + pageNum);
			String fileName = BBYOpenUtils.getFileNameToSave(pageNum, SAVE_FILES_IN_FOLDER);
			logger.info("Downloading page " + pageNum + " into file " + fileName);
			long sleepTime = 10;
			while (true)
				try
				{
					lastDownloadTime = System.currentTimeMillis();
					FileUtils.copyURLToFile(url, new File(fileName), 5000, 10000);
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
	}

	private void processProducts(String url, DocumentBuilder db)
	{
		try
		{
			Document doc = db.parse(new URL(url).openStream());
			// get all the products
			NodeList nodes = doc.getElementsByTagName(PRODUCT_TAG);
			if (nodes == null || nodes.getLength() < 1)
				return;

			// List<GenericProduct> products = new ArrayList<GenericProduct>();
			for (int i = 0; i < nodes.getLength(); i++)
			{
				int count = 0;
				Node node = nodes.item(i);
				NodeList prodAttrNodes = node.getChildNodes();
				if (prodAttrNodes == null || prodAttrNodes.getLength() < 1)
					continue;
				HashMap<String, String> prodAttributes = new HashMap<String, String>();

				Node categoryPathNode = ((Element) node).getElementsByTagName("categoryPath").item(0);
				NodeList categoryPathNodeList = categoryPathNode.getChildNodes();
				int terminalCategoryIdx = -1;
				for(int xy=0; xy<categoryPathNodeList.getLength();xy++)
				{
					if(categoryPathNodeList.item(xy).getNodeType() == Node.ELEMENT_NODE)
						terminalCategoryIdx = xy;
				}
				String categoryName = "EMPTY";
				if (categoryPathNode.getChildNodes().getLength() > 0)
				{
					Node categoryNode = categoryPathNodeList.item(terminalCategoryIdx);
					categoryName = ((Element) categoryNode).getElementsByTagName("id").item(0).getTextContent();
				}
				prodAttributes.put("categoryPath.category.name", categoryName);
				for (int j = 0; j < prodAttrNodes.getLength(); j++)
				{
					Node prodAttrNode = prodAttrNodes.item(j);
					if (prodAttrNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element prodAttrElem = (Element) prodAttrNodes.item(j);
						String name = prodAttrElem.getNodeName();
						if (detailedAttributeNames.contains(name))
						{
							// process detailed node
						} else
						{
							if (prodAttrElem.getChildNodes().getLength() > 1)
								logger.info("*******This node has more than 1 children : " + name);
							else
							{
								prodAttributes.put(name, prodAttrElem.getTextContent());
							}
						}
					}
				}
				GenericProduct prod = new GenericProduct(prodAttrMapping);
				GenericProduct price = new GenericProduct(priceAttrMapping);
				String prodIdInternal = prodAttributes.get("upc");
				if (prodIdInternal.equals("000000000000") || prodIdInternal.equals(""))
				{
					logger.info("Skipping product :" + prodIdInternal + "::" + prodAttributes);
					continue;
				}
				prod.setProdIdInternal(prodIdInternal);
				price.setProdIdInternal(prodIdInternal);
				prod.populateProductAttrs(prodAttributes);
				price.populateProductAttrs(prodAttributes);
				Date priceDate = getPriceDate(prodAttributes);
				price.setFromDate(priceDate);
				prod.setFromDate(priceDate);
				products.put(prodIdInternal, prod);
				prices.put(prodIdInternal, price);
			}
		} catch (FileNotFoundException fne)
		{
			fne.printStackTrace();
			throw new RuntimeException(fne);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Date getPriceDate(Map<String, String> prodAttributes)
	{
		// try {
		// String priceDateStr = prodAttributes.get(PRICE_FROM_DATE_TAG);
		// Date priceDate = priceDateFormat.parse(priceDateStr);
		// return priceDate;
		// }catch(Exception e) {
		// e.printStackTrace();
		// }
		return runDate;
	}

	static class GenericProductComparator implements Comparator<GenericProduct>
	{
		@Override
		public int compare(GenericProduct o1, GenericProduct o2)
		{
			if (o1.equals(o2))
				return 0;
			Date fd1 = o1.getFromDate();
			Date fd2 = o2.getFromDate();
			if (fd1.before(fd2))
				return -1;
			else
				return 1;
		}
	}

	public static void main(String[] args)
	{
		try
		{
			BBYOpenParser parser = new BBYOpenParser(Retailer.ID.BESTBUY);
			ProductStore pStore = ProductStoreDbAsync.get();
			parser.parseAndSave(pStore);

			while(!pStore.allProcessed()){
				logger.info("All records not processed. Sleeping for 90 seconds");
				try {
					Thread.sleep(90000);
				} catch (InterruptedException e) {
				}
			}			
 			pStore.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	protected boolean isSortedByBestSeller(org.jsoup.nodes.Document doc,
			int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected double getReviewRating(org.jsoup.nodes.Element product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNumReviews(org.jsoup.nodes.Element product) {
		// TODO Auto-generated method stub
		return 0;
	}

}

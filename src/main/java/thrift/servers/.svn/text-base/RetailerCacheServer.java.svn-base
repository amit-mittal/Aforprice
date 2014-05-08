package thrift.servers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import db.dao.CategoryDAO;
import db.dao.CategoryFactory;
import db.dao.PriceMovementSummaryDAO;
import db.dao.ProductsDAO;

import entities.Retailer;

import thrift.genereated.retailer.Category;
import thrift.genereated.retailer.LookupIdx;
import thrift.genereated.retailer.PriceDrop;
import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.ProductFilter;
import thrift.genereated.retailer.ProductList;
import thrift.genereated.retailer.RetailerCache;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.SortCriterion;
import thrift.genereated.retailer.Tick;
import util.Metric;
import static thrift.genereated.retailer.retailerConstants.RETAILER_SERVER_PORT;
import static thrift.genereated.retailer.retailerConstants.RETAILERS;

public class RetailerCacheServer implements RetailerCache.Iface,Runnable {
	private static final Logger logger = Logger.getLogger(RetailerCacheServer.class);
	private static Integer port;
	private static List<Retailer> retailers = new ArrayList<Retailer>();
	private static Integer numProducts = -1;
	public CategoryCacheImpl categoryCache;
	public ProductCacheImpl prodCache;
	public static final int TIMER_INTERVAL = 5*60*1000;//5 minutes
	Metric timeIt = new Metric("TimeIt");

	public void run() {
		try {
			TServerSocket serverTransport = new TServerSocket(port);
			timeIt.start();
			logger.info("Initializing the category cache");
			CategoryDAO reader = CategoryFactory.getInstance().getDAO();
			Timestamp categoryInitModifiedTime=reader.getCategoryMaxModifiedTime();
			categoryCache = new CategoryCacheImpl(prodCache, retailers);
			timeIt.end();
			logger.info("Initializing the category cache...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();

			timeIt.start();
			logger.info("Initializing the product cache");
			ProductsDAO prodDao = new ProductsDAO();
			Timestamp productInitModifiedTime = prodDao.getProductMaxModifiedTime();
			prodCache = new ProductCacheImpl(categoryCache, retailers, numProducts);
			timeIt.end();
			logger.info("Initializing the product cache...done, took " + timeIt.getProcessingTimeMinsSecs());
			timeIt.reset();
			System.gc();
			
			logger.info("Starting Category Updater");
			CategoryUpdater categoryUpdater = new CategoryUpdater(categoryCache, categoryInitModifiedTime);
			Timer categoryTimer = new Timer();
			categoryTimer.schedule(categoryUpdater, 0, TIMER_INTERVAL);
			
			logger.info("Starting Product Updater thread");
			ProductUpdater productUpdater = new ProductUpdater(prodCache, productInitModifiedTime);
			Timer productTimer = new Timer();
			productTimer.schedule(productUpdater, 0, TIMER_INTERVAL);
			
			RetailerCache.Processor processor = new RetailerCache.Processor(this);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
			logger.info("Starting server on port: "+port);
			server.serve();
		} catch(Throwable e) {
			logger.error("Message: "+e.getMessage());
			logger.error("StackTrace: ");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		parseArguments(args);
		new Thread(new RetailerCacheServer()).run();
	}
	/**
	 * @param args
	 */
	private static void parseArguments(String[] args){
		Integer serverId = -1;
		Integer instance = -1;
		if(args.length>=1){
			//args format key=value.. serverId=1 instance=1
			for(int i=0; i<args.length; i++){
				logger.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2){
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				}				
				String key = keyVal[0];
				if(key.equalsIgnoreCase("serverid")){
					serverId = Integer.parseInt(keyVal[1]);
					logger.info("ServerId is "+serverId);
					if(!RETAILER_SERVER_PORT.containsKey(serverId))
						throw new IllegalArgumentException("ServerId "+serverId+" is missing in retailer_server_port list");
					retailers = getRetailers(serverId);
				}
				else if(key.equalsIgnoreCase("instance")){
					instance = Integer.parseInt(keyVal[1]);					
				}
				else if(key.equalsIgnoreCase("numProducts")){
					numProducts = Integer.parseInt(keyVal[1]);					
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}//for(int i=0; i<args.length; i++){ ends...
			if(serverId==-1)
				throw new IllegalArgumentException("Missing argument serverid");
			if( instance==-1)
				throw new IllegalArgumentException("missing argument instance");
			List<Integer> serverPorts = RETAILER_SERVER_PORT.get(serverId);
			if( (port=serverPorts.get(instance-1)) == null)
				throw new IllegalArgumentException("instance "+instance+" is missing in product_server_port list of server "+ serverId);
			String strList="";
			for(Retailer retailer:retailers)
				strList += (strList.length()==0?"":",")+retailer.getId();
			logger.info("Retailers="+strList);
			logger.info("Port="+port);
		}
	}
	/**
	 * @param retailers
	 * @param serverId
	 */
	private static List<Retailer> getRetailers(Integer serverId) {
		Retailer retailer;
		List<Retailer> retailers = new ArrayList<Retailer>();
		for(Map.Entry<String, Integer> entry : RETAILERS.entrySet()){
			if(entry.getValue() == serverId){
				if((retailer=Retailer.getRetailer(entry.getKey()))==null)
					throw new IllegalArgumentException("Invalid retailer "+entry.getValue());
				retailers.add(retailer);
			}
		}
		if(retailers.size()==0)
			throw new IllegalArgumentException("ServerId"+serverId+" is missing in retailers list");
		return retailers;
	}

	@Override
	public thrift.genereated.retailer.Retailer getRetailer(String retailerId)
			throws TException {
		return prodCache.getRetailer(retailerId);
	}
	
	@Override
	public Map<Integer, List<Category>> getAllCategoriesByLevelForRetailer(
			String retailerId, List<Integer> levels) throws TException {
		return categoryCache.getAllCategoriesByLevelForRetailer(retailerId, levels);
	}

	@Override
	public List<Category> getHomePageCategories(String retailerId)
			throws TException {
		return categoryCache.getHomePageCategories(retailerId);
	}

	@Override
	public List<Category> getChildCategories(int categoryId) throws TException {
		return categoryCache.getChildCategories(categoryId);
	}

	@Override
	public List<Category> getCategoryPath(int categoryId) throws TException {
		return categoryCache.getCategoryPath(categoryId);
	}
	
	public ProductList getProducts(int categoryId, LookupIdx lookup, List<ProductFilter> filters, SortCriterion sortCriterion) {
		return ProductData.getInstance().getProducts(categoryId, lookup, filters, sortCriterion);
	}

	@Override
	public List<Product> getProductsByIds(List<Integer> productIdList)
			throws TException {
		return prodCache.getProductsByIds(productIdList);
	}

	@Override
	public ProductList getTopPriceDropsByCategory(int categoryId,
			LookupIdx lookup) throws TException {
		//return prodCache.getTopPriceDropsByCategory(categoryId, lookup);
		throw new TException("Unsupported operation - getTopPriceDropsByCategory");
	}

	@Override
	public ProductList getTopPriceDropsByRetailer(String retailer,
			LookupIdx lookup) throws TException {
		throw new TException("Unsupported operation - getTopPriceDropsByRetailer");
	}

	@Override
	public Map<String, ProductList> getPriceDropsByCategory(int categoryId,
			int max) throws TException {
		return prodCache.getPriceDropsByCategory(categoryId, max);
	}

	@Override
	public Map<String, ProductList> getPriceDropsByRetailer(String retailer,
			int max) throws TException {
		return prodCache.getPriceDropsByRetailer(retailer, max);
	}
	
	@Override
	public Map<String, Product> getProductsForURLs(String retailer,
			List<String> urls) throws TException {
		logger.info("getProductsForURLs: retailer: "+retailer+" no. of urls: "+urls.size());
		//Map<String, Product> map = getDummyPriceData();
		Map<String, Product> map = prodCache.getProductsForURL(retailer, urls); 
		return map;
	}

	/**
	 * @return
	 */
	private Map<String, Product> getDummyPriceData() {
		Map<String, Product> map = new HashMap<String, Product>();
		long time = System.currentTimeMillis();
		long TIME_INT = 24*60*60*1000L;
		
		List<Tick> timeTick = new ArrayList<Tick>(3);
		timeTick.add(new Tick(time, 23.22));
		timeTick.add(new Tick(time-TIME_INT, 51.49));
		timeTick.add(new Tick(time+TIME_INT, 47.15));
		List<Tick> timeTick1 = new ArrayList<Tick>(timeTick);
		timeTick1.add(new Tick(time+(2*TIME_INT), 66.77));
		
		PriceHistory priceHist = new PriceHistory(timeTick, 0, 0, 0, 0, 0, 0, 0, 0);
		PriceHistory priceHist1 = new PriceHistory(timeTick1, 0, 0, 0, 0, 0, 0, 0, 0);
		
		Product prod1 = new Product(1, "tablet", "23", "img1", "url1", 
				priceHist, new ArrayList<Tick>(), new ArrayList<Review>());
		Product prod2 = new Product(2, "pokemon", "11", "img2", "url2", 
				priceHist1, new ArrayList<Tick>(), new ArrayList<Review>());
		
		map.put("http://www.toysrus.com/product/index.jsp?productId=20220276&cp=2255956.2273442.2256395.4486211.3254086&parentPage=family", prod1);
		map.put("URL_2", prod2);
		logger.info("getProductsForURLs: returning " + map);
		return map;
	}

	@Override
	public Map<String, ProductList> getPriceDropsForProductFamily(String retailer, List<String> urls, int max)
			throws TException {
		logger.info("getPriceDropsForProductFamily: Size of urls: "+urls.size()+" max: "+max);
		if(urls.size()==0)
			return prodCache.getPriceDropsByRetailer(retailer, max);
		else
			return prodCache.getPriceDropForProductFamily(retailer, urls, max);
	}

	@Override
	public Map<String, Product> getProductsForReceiptIds(String retailer, List<String> receiptIds) throws TException
	{
		return prodCache.getProductsForReceiptIds(retailer, receiptIds);
	}
}

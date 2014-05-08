package adhoc;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;
import thrift.servers.ProductComparators.ProductComparatorByTime;
import thrift.servers.ProductData;
import util.DateTimeUtils;
import categories.CategoryTreeNode;
import db.dao.DAOException;
import db.dao.PostPurchaseStatDAO;
import db.dao.ProductsDAO;
import db.dao.ProductsDAO.HISTORY_QUERY_TYPE;
import entities.Retailer;

public class PostPurchasePricingAnalytics
{
	private static final Logger logger = Logger.getLogger(PostPurchasePricingAnalytics.class);

	private String retailer = null;
	private static final ProductsDAO prodDao = new ProductsDAO();
	static CategoryTreeNode categoryTreeRoot;
	public Map<Integer, List<Integer>> productToCategory = new HashMap<>();
	private static ProductComparatorByTime productComparatorByTime = new ProductComparatorByTime();

	public PostPurchasePricingAnalytics(String retailer) throws SQLException
	{
		this.retailer = retailer;
		categoryTreeRoot = new CategoryTreeNode(retailer);
		
		
		//Create productId Category Map
		for(Integer terminalCategoryId : CategoryTreeNode.terminalCategoryMap.keySet())
		{
			List<Integer> products = prodDao.getProductIdsForCategory(terminalCategoryId);
			for(Integer productId: products)
			{	
				if( productToCategory.containsKey(productId) )
				{
					List<Integer> categories = productToCategory.get(productId); 
					categories.add(terminalCategoryId);
					productToCategory.put(productId, categories);
				}
				else
				{
					List<Integer> categories = new LinkedList<>();
					categories.add(terminalCategoryId);
					productToCategory.put(productId, categories);
				}
			}
		}
	}
	
	public PostPurchasePricingAnalytics()
	{
	}

	public void initProducts(Date endDate) throws SQLException
	{
		logger.info("loading products for retailer " + retailer);
		List<Product> products = prodDao.getAsOfProducts(retailer, -1, endDate);
		for (Product prod : products){
			ProductData.getInstance().addProduct(Retailer.getRetailer(retailer), prod);
		}
		logger.info("Size of the products is :" + products.size());
	}

	private void getPriceHistory() throws SQLException
	{
		logger.info("get Price History " + retailer);
		prodDao.getDataForProductsByRetailer(retailer, HISTORY_QUERY_TYPE.PRICE);
	}

	public static void main(String argv[]) throws ParseException, DAOException
	{
		try
		{
			//argv = new String[] { "oldnavy", "2013-Jan-01", "2013-Jan-03" };
			String retailer = argv[ 0 ];
			String startDateStr = argv[ 1 ];
			String endDateStr = argv[ 2 ];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
			Date startDate = sdf.parse(startDateStr);
			Date endDate = sdf.parse(endDateStr);
			
			PostPurchasePricingAnalytics postPurchasePricingAnalytics = new PostPurchasePricingAnalytics(retailer);
			postPurchasePricingAnalytics.initProducts(endDate);
			postPurchasePricingAnalytics.getPriceHistory();
			postPurchasePricingAnalytics.sortData();
			
			double[] dropAmounts = { 5, 10, 20 };
			for(double dropAmount : dropAmounts)
			{	
				
					//Can be a thread
					Map<Date, Map<Integer,PostPurchaseStat>> postPurchaseStats = postPurchasePricingAnalytics.generateStats(startDate, endDate, true, dropAmount, 11  );
		
					System.out.println("** FINAL RESULTS ** ");
					PostPurchaseStatDAO postPostPurchaseDAO = PostPurchaseStatDAO.getInstance();
					for(Map<Integer, PostPurchaseStat> statByCategory :postPurchaseStats.values())
					{
						for(PostPurchaseStat postPurchaseStat: statByCategory.values())
						{
							postPostPurchaseDAO.store(postPurchaseStat);
							logger.info(postPurchaseStat);
						}	
					}
			}
		} catch (SQLException sqe)
		{
			throw new RuntimeException(sqe);
		}
	}
	
	public class PostPurchaseStat
	{
		public Date statDate;
		public int categoryid = -1;
		public int totalProducts= 0;
		public int dropIn7Days = 0;
		public int dropIn15Days = 0;
		public int dropIn30Days= 0;
		public int dropIn90Days= 0;
		public String retailer = "";
		public double dropAmt = 0;
		public String addenda1 = "";
		public String addenda2 = "";
		public String addenda3 = "";
		
		@Override
		public String toString()
		{
			return ( statDate + "\t" + totalProducts + "\t" + dropIn7Days + "\t" + dropIn15Days + "\t" + dropIn30Days + "\t" + dropIn90Days + String.valueOf(dropAmt) );
		}
	}
	
	private void sortData()
	{
		for (Product product : ProductData.getInstance().getProducts())
		{
			List<Tick> priceTicks = product.getPriceHistory().getPriceTicks();
			Collections.sort(priceTicks, productComparatorByTime);
		}
	}
	
	public Map<Date, Map<Integer, PostPurchaseStat>> generateStats(Date startDate, Date endDate, boolean isSorted, double dropAmt, Integer topCategoryId) throws ParseException
	{
		Map<Date, Map<Integer,PostPurchaseStat>> postPurchaseStatMap = new HashMap<>();

		if( !isSorted ) 
			sortData();
		
		while (startDate.before(endDate))
		{
			Date plus7Day = DateTimeUtils.add(startDate, 7, TimeUnit.DAYS);
			Date plus15Day = DateTimeUtils.add(startDate, 15, TimeUnit.DAYS);
			Date plus30Day = DateTimeUtils.add(startDate, 30, TimeUnit.DAYS);
			Date plus90Date = DateTimeUtils.add(startDate, 90, TimeUnit.DAYS);

			//logger.debug("Top Cat Id" + topCategoryId);
			for (Product product : ProductData.getInstance().getProducts())
			{
				
				Set<Integer> topCategories = new HashSet<>();
				topCategories.add(-1);
				if(topCategoryId != -1)
				{	
					//Find top category List
					List<Integer> terminalCats = productToCategory.get(Integer.valueOf(product.getProductId()));
					if( terminalCats != null )
					{
						logger.debug(product.getProductId() + " : " + "size of terminal category " + terminalCats.size());
						for(Integer terminalCat : terminalCats )
						{
							if(CategoryTreeNode.terminalCategoryMap.get(terminalCat) != null)
								topCategories.add(CategoryTreeNode.terminalCategoryMap.get(terminalCat));
						}
					}
					else
						logger.info("terminal category is null for product " + product.getProductId());
				}
				
				for(Integer topCategory : topCategories)
				{
					PostPurchaseStat postPurchaseStat = new PostPurchaseStat();
					
					if( postPurchaseStatMap.containsKey(startDate) && postPurchaseStatMap.get(startDate).containsKey(topCategory))
						postPurchaseStat = postPurchaseStatMap.get(startDate).get(topCategory);
					else
					{
						postPurchaseStat.statDate = startDate;
						postPurchaseStat.retailer = retailer;
						postPurchaseStat.dropAmt = dropAmt;
						postPurchaseStat.categoryid = topCategory;
						postPurchaseStat.addenda1 = "0";
						postPurchaseStat.addenda2 = "0";
						postPurchaseStat.addenda3 = "0";
 						
						Map<Integer, PostPurchaseStat> statByTopCategory;

						if( postPurchaseStatMap.containsKey(startDate) )
							statByTopCategory = postPurchaseStatMap.get(startDate);
						else
							statByTopCategory = new HashMap<>();
						statByTopCategory.put(topCategory, postPurchaseStat);
						postPurchaseStatMap.put(startDate, statByTopCategory);
					}
					List<Tick> priceTicks = product.getPriceHistory().getPriceTicks();
					Date prevTickDate = null;
					double startPrice = -1;
					double prevPrice = -1;
					boolean found7DayDrop = false;
					boolean found15DayDrop = false;
					boolean found30DayDrop = false;
					boolean found90DayDrop = false;
					double dropAmt7Day = 0;  
					double dropAmt15Day = 0; 
					double dropAmt30Day = 0;
					double dropAmt90Day = 0;
					
					for (Tick tick : priceTicks)
					{
						Date tickDate = new Date(tick.getTime());
						double price = tick.getValue();
						if (prevTickDate == null)
						{	
							prevTickDate = tickDate;
							prevPrice = price;
						}
						if (startPrice == -1)
						{
							if ( prevTickDate.before(startDate) && tickDate.after(startDate) )
							{	
								startPrice = prevPrice;
								if(startPrice >= postPurchaseStat.dropAmt)
									postPurchaseStat.totalProducts++;
							}	
							else if( tickDate.equals(startDate) )
							{
								startPrice = price;
								if(startPrice >= postPurchaseStat.dropAmt)
									postPurchaseStat.totalProducts++;
							}
						}
						
						if( startPrice != -1 && startPrice >= dropAmt)
						{
							if( price + dropAmt < startPrice && tickDate.before(plus7Day) )
							{	
								if(!found7DayDrop)
								{
									postPurchaseStat.dropIn7Days++;
									found7DayDrop = true;
								}
								if( dropAmt7Day < startPrice - price )
									dropAmt7Day = startPrice - price;
							}
							if( price + dropAmt < startPrice && tickDate.before(plus15Day) )
							{
								if(!found15DayDrop)
								{	 
									postPurchaseStat.dropIn15Days++;
									found15DayDrop = true;
								}
								if( dropAmt15Day < startPrice - price )
									dropAmt15Day = startPrice - price;
							}
							if( price + dropAmt < startPrice && tickDate.before(plus30Day))
							{
								if(!found30DayDrop)
								{
									postPurchaseStat.dropIn30Days++;
									found30DayDrop = true;
								}
								if( dropAmt30Day < startPrice - price)
									dropAmt30Day = startPrice - price;
							}
							if( price + dropAmt < startPrice && tickDate.before(plus90Date)  )
							{
								if(!found90DayDrop)
								{	
									postPurchaseStat.dropIn90Days++;
									found90DayDrop = true;
								}
								if( dropAmt90Day < startPrice - price)
									dropAmt90Day = startPrice - price;
							}
							
						}
						prevTickDate = tickDate;
						prevPrice = price;
					}
				    postPurchaseStat.addenda1 = String.valueOf( Double.parseDouble(postPurchaseStat.addenda1) + dropAmt7Day);
				    postPurchaseStat.addenda2 = String.valueOf( Double.parseDouble(postPurchaseStat.addenda2) + dropAmt30Day);
				    postPurchaseStat.addenda3 = String.valueOf( Double.parseDouble(postPurchaseStat.addenda3) + dropAmt90Day);
					//logger.debug(product.getProductId() + "\t" + startDate + "\t" + startPrice + "\t" + postPurchaseStat.categoryid + "\t" + found7DayDrop + "\t" + found30DayDrop);
				}
			}	
				//postPurchaseStats.put(startDate, postPurchaseStat);
			startDate = DateTimeUtils.add(startDate, 1, TimeUnit.DAYS);
		}
		return postPurchaseStatMap;
	}
	
	
	
}

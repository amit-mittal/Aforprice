/**
 * 
 */
package products;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import util.MetricCollector;
import db.dao.DAOException;
import db.dao.LastProcessedDAO;
import db.dao.PriceMovementRecorderDAOFactory;
import db.dao.PriceMovementSummaryDAO;
import db.dao.ProductPricesHistoryDAO;
import db.dao.ProductPricesHistoryDAO.QUERYTYPE;
import entities.PriceMovementSummary;
import entities.ProductPricesHistory;

/**
 * @author arpan
 * 
 */
public class PriceMovementRecorder
{
/*	private static final Logger logger = Logger.getLogger(PriceMovementRecorder.class);

	//constants or final variables
	public static final String PROCESS_NAME = "PRICE_MOVEMENT_RECORDER";
	public static final long PRODUCT_PRICE_HISTORY_QUERY_SPAN = 60;
	//8 hours - 8*60 * 60 * 1000
	private static final long PRICECHECK_INTERVAL = (60*8) * 60 * 1000;
	private LastProcessedDAO lastDAO = PriceMovementRecorderDAOFactory.getInstance().getLastProcDAO();
	private ProductPricesHistoryDAO productPricesHistoryDAO = PriceMovementRecorderDAOFactory.getInstance().getProductPriceHistDAO();
	private PriceMovementSummaryDAO priceMovementSummaryDAO = PriceMovementRecorderDAOFactory.getInstance().getPriceMovementSummaryDAO();
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//Member variables which decides flow of our server
	Map<Integer, PriceMovementSummary> priceMovementSummaryMap = new HashMap<>();
	List<PriceMovementSummary> priceMovementSummaryUpdateList = new ArrayList<>();
	List<PriceMovementSummary> priceMovementSummaryInsertList = new ArrayList<>();
	public Date m_lastProcessedTime;

	PriceMovementRecorder()
	{
		//Thread metricCollectorShuDownRunner = new Thread(MetricCollector.SHUTDOWN_RUNNER);
		//Runtime.getRuntime().addShutdownHook(metricCollectorShuDownRunner);
	}


	public void start() throws SQLException, InterruptedException, ParseException
	{

		String lastProcessedStr = lastDAO.getLastProcessed(PROCESS_NAME);
		 m_lastProcessedTime = dateFormatter.parse(lastProcessedStr);

		while (true)
		{
			// Query Products with price change
			List<ProductPricesHistory> productsWithPriceChange = getProductsWithPriceChange();

			// Process Products with price change
			if (productsWithPriceChange != null && productsWithPriceChange.size() > 0)
				processProductsWithPriceChanges(productsWithPriceChange);

			lastDAO.setLast(PROCESS_NAME, dateFormatter.format(m_lastProcessedTime));
			logger.info("Setting last processed for price movement summarizer to :" + dateFormatter.format(m_lastProcessedTime));
			// Sleep
			if (productsWithPriceChange != null && productsWithPriceChange.size() == 0 && m_lastProcessedTime.getTime() > System.currentTimeMillis() - PRODUCT_PRICE_HISTORY_QUERY_SPAN)
			{
				logger.info("Sleeping for seconds: " + PRICECHECK_INTERVAL/1000);
				Thread.sleep(PRICECHECK_INTERVAL);
			}
		}
	}

	private void processProductsWithPriceChanges(List<ProductPricesHistory> productsWithPriceChange)
			throws SQLException
	{
		for (ProductPricesHistory productWithPriceChange : productsWithPriceChange)
		{
			int productId = productWithPriceChange.getProductId();

			// Query daily price movement summary for this product and store in cache
			PriceMovementSummary priceMovementSummaryForProduct = getPriceMovementSummaryForProduct(productId);

			// That latestPriceChangeTime in price movement summary is later
			// than priceChange objects' time
			// This means the price movement summary is latest so, no need to do
			// anything
			if (priceMovementSummaryForProduct != null
					&& priceMovementSummaryForProduct.getLastPriceChangeTime().after(productWithPriceChange.getTime()))
			{
				logger.debug("Already have newer price movement summary for this product" + productId);
				continue;
			}
			// Query Price History For Product
			Map<Integer, List<ProductPricesHistory>> priceHistoryForProduct = productPricesHistoryDAO
					.getPriceHistoryForProductsWithPriceChange(productWithPriceChange.getProductId(), null, null,
							QUERYTYPE.PRODUCT_ID);
			// Generate Price Movement Summary for Product
			generatePriceMovementSummary(priceHistoryForProduct);

			// Persist Price Movement Summary for Product
			persistPriceMovementSummary();
		}
	}

	 PriceMovementSummary getPriceMovementSummaryForProduct(int productId)
	{
		PriceMovementSummary priceMovementSummaryForProduct = priceMovementSummaryMap.get(productId);
		if (priceMovementSummaryForProduct == null)
		{
			List<PriceMovementSummary> priceMovementSummary = priceMovementSummaryDAO
					.getPriceMovementSummaryForProduct(productId);
			if (priceMovementSummary.size() > 0)
			{
				priceMovementSummaryForProduct = priceMovementSummary.get(0);
				priceMovementSummaryMap.put(productId, priceMovementSummaryForProduct);
			}
		}
		return priceMovementSummaryForProduct;
	}

 	List<ProductPricesHistory> getProductsWithPriceChange() 
	{
		List<ProductPricesHistory> productsWithPriceChange = new ArrayList<>();

		try
		{
			Timestamp fromTime = new Timestamp(m_lastProcessedTime.getTime());
			Timestamp toTime = new Timestamp(m_lastProcessedTime.getTime() + (PRODUCT_PRICE_HISTORY_QUERY_SPAN * 60 * 1000));
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			if (toTime.after(currentTime))
				toTime = currentTime;
			
			productsWithPriceChange = productPricesHistoryDAO.getProductPricesHistoryForSpan(fromTime, toTime);
		    m_lastProcessedTime = toTime;
			logger.info("Found products with price change : "  + productsWithPriceChange.size() );

		}catch(DAOException de)
		{
			de.printStackTrace();
		}
		return productsWithPriceChange;
	}

	public void persistPriceMovementSummary()
	{
		logger.debug("Size of insert list" + priceMovementSummaryInsertList.size());
		logger.debug("Size of update list" + priceMovementSummaryUpdateList.size());

		
		List<PriceMovementSummary> erroredInserts = priceMovementSummaryDAO
				.insertPriceMovementSummary(priceMovementSummaryInsertList);
		priceMovementSummaryInsertList.clear();

		List<PriceMovementSummary> erroredUpdates = priceMovementSummaryDAO
				.updatePriceMovementSummary(priceMovementSummaryUpdateList);
		priceMovementSummaryUpdateList.clear();

		// for (PriceMovementSummary erroredInsert : erroredInserts)
		if(erroredInserts != null && erroredInserts.size() != 0)
			logger.warn("Erroed Insert:" + erroredInserts);
		// for (PriceMovementSummary erroredUpdate : erroredUpdates)
		if(erroredUpdates != null && erroredUpdates.size() != 0)
			logger.warn("Erroed Update:" + erroredUpdates);

	}

	public void generatePriceMovementSummary(Map<Integer, List<ProductPricesHistory>> priceMovementForSpan)
			throws SQLException
	{
		Set<Integer> productsWithPriceMovement = priceMovementForSpan.keySet();
		for (Integer productId : productsWithPriceMovement)
		{
			int index = 0;
			double totalPrice = 0;
			PriceMovementSummary priceMovementSummary = new PriceMovementSummary();
			List<ProductPricesHistory> priceHistoryList = priceMovementForSpan.get(productId);
			priceMovementSummary.setProductId(productId);
			for (ProductPricesHistory priceHistory : priceHistoryList)
			{
				if (priceMovementSummary.getLatestPriceTime() == null
						|| priceHistory.getTime().after(priceMovementSummary.getLatestPriceTime()))
				{
					priceMovementSummary.setLatestPrice(priceHistory.getPrice());
					priceMovementSummary.setLatestPriceTime(priceHistory.getTime());
				}
			}
			Date reallyOldDate = new Date(1901 - 1900, 1, 1);
			priceMovementSummary.setLastPriceChangeTime(reallyOldDate);
			for (ProductPricesHistory priceHistory : priceHistoryList)
			{
				if (priceHistory.getTime().after(priceMovementSummary.getLastPriceChangeTime())
						&& priceHistory.getTime().before(priceMovementSummary.getLatestPriceTime()))
				{
					priceMovementSummary.setLastPrice(priceHistory.getPrice());
					priceMovementSummary.setLastPriceChangeTime(priceHistory.getTime());
				}
				// identify max price
				if (priceHistory.getPrice() >= priceMovementSummary.getMaxPrice())
				{
					priceMovementSummary.setMaxPrice(priceHistory.getPrice());
					priceMovementSummary.setMaxPriceTime(priceHistory.getTime());
				}
				totalPrice = totalPrice + priceHistory.getPrice();
				index++;
			}
			priceMovementSummary.setAveragePrice(totalPrice / index);
			
			//No need to store this product in price movement summary if this was first price for this product
			if (priceMovementSummary.getLastPriceChangeTime().equals(reallyOldDate))
				continue;

			if (priceMovementSummaryMap.containsKey(productId))
			{
				if (!priceMovementSummary.equals(priceMovementSummaryMap.get(productId)))
					priceMovementSummaryUpdateList.add(priceMovementSummary);
			} else
			{
				priceMovementSummaryInsertList.add(priceMovementSummary);
			}
			priceMovementSummaryMap.put(productId, priceMovementSummary);
		}
	}

	public Map<Integer, PriceMovementSummary> getPriceMovementSummaryMap()
	{
		return priceMovementSummaryMap;
	}

	public void setPriceMovementSummaryMap(Map<Integer, PriceMovementSummary> priceMovementSummaryMap)
	{
		this.priceMovementSummaryMap = priceMovementSummaryMap;
	}

	public List<PriceMovementSummary> getPriceMovementSummaryUpdateList()
	{
		return priceMovementSummaryUpdateList;
	}

	public void setPriceMovementSummaryUpdateList(List<PriceMovementSummary> priceMovementSummaryUpdateList)
	{
		this.priceMovementSummaryUpdateList = priceMovementSummaryUpdateList;
	}

	public List<PriceMovementSummary> getPriceMovementSummaryInsertList()
	{
		return priceMovementSummaryInsertList;
	}

	public void setPriceMovementSummaryInsertList(List<PriceMovementSummary> priceMovementSummaryInsertList)
	{
		this.priceMovementSummaryInsertList = priceMovementSummaryInsertList;
	}

	public static void main(String[] args)
	{
		try
		{
			PriceMovementRecorder priceMovementRecorder = new PriceMovementRecorder();
			priceMovementRecorder.start();
		} catch (SQLException se)
		{
			logger.error("Error in DB activity", se);
		} catch (Exception se)
		{
			logger.error("if you are passing args, it should be yyymmdd yyymmdd ( startDate, stopDate)", se);
		}

	}
*/
}


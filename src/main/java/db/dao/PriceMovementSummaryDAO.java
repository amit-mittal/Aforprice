package db.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import util.ConfigParms;

import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import entities.PriceMovementSummary;

public class PriceMovementSummaryDAO extends DataAccessObject
{
/*
	private final Logger logger = Logger.getLogger(PriceMovementSummaryDAO.class);
	private final static DbConnectionPool pool = DbConnectionPool.get();
	private static PriceMovementSummaryDAO instance = new PriceMovementSummaryDAO();
	private static final LastProcessedDAO lastDAO = new LastProcessedDAO();
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static final String PROCESS_NAME = "PRICEMOVEMENTRECORDER";
	static  int LOOKBACK_SPAN = -7;
	static  int LOOKBACK_SPAN_WINDOWS = -365;

	private PriceMovementSummaryDAO()
	{
	}

	public static PriceMovementSummaryDAO getInstance()
	{
		return instance;
	}

	public static void setInstanceForRegTest(PriceMovementSummaryDAO override)
	{
		instance = override;
	}

	public List<PriceMovementSummary> insertPriceMovementSummary(List<PriceMovementSummary> priceMovementSummaryList)
	{
		logger.debug(" BEGIN insertPriceMovementSummary, Size of priceMovementSummaryList "
				+ priceMovementSummaryList.size());

		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<PriceMovementSummary> erroredPriceMoveSummary = new ArrayList<>();
		try
		{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.INSERT_PRICE_MOVEMENT_SUMMARY);

			for (PriceMovementSummary priceMovementSummary : priceMovementSummaryList)
			{
				try
				{
					int i = 1;
					stmt.setInt(i++, priceMovementSummary.getProductId());
					stmt.setFloat(i++, (float) priceMovementSummary.getLatestPrice());
					stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getLatestPriceTime().getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getLastPrice());
					stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getLastPriceChangeTime().getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getMaxPrice());
					if (priceMovementSummary.getMaxPriceTime() != null)
						stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getMaxPriceTime().getTime()));
					else
						stmt.setTimestamp(i++, new Timestamp(new Date(1901 - 1900, 1, 1).getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getAveragePrice());
					stmt.setString(i++, String.valueOf(priceMovementSummary.isPriceDrop()));
					stmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					stmt.addBatch();
				} catch (SQLException e)
				{
					logger.error(e.getMessage(), e);
					erroredPriceMoveSummary.add(priceMovementSummary);
				}
			}
			try
			{
				stmt.executeBatch();
			} catch (BatchUpdateException be)
			{
				logger.error(be.getMessage(), be);
				erroredPriceMoveSummary.addAll(getInsErr(priceMovementSummaryList, be.getUpdateCounts()));
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
				return priceMovementSummaryList;
			}
		} catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			return priceMovementSummaryList;
		} finally
		{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		logger.debug("END insertPriceMovementSummary");
		return erroredPriceMoveSummary;
	}

	private Collection<? extends PriceMovementSummary> getInsErr(List<PriceMovementSummary> priceMovementSummaryList,
			int[] retCodes)
	{
		List<PriceMovementSummary> erroredPriceMoveSummary = new ArrayList<PriceMovementSummary>();
		for (int j = 0; j < priceMovementSummaryList.size(); j++)
		{
			if (retCodes.length >= j + 1)
			{
				if (retCodes[j] == Statement.EXECUTE_FAILED)
				{
					erroredPriceMoveSummary.add(priceMovementSummaryList.get(j));
				}
				j++;
				continue;
			}
			erroredPriceMoveSummary.add(priceMovementSummaryList.get(j));
		}
		return erroredPriceMoveSummary;

	}

	public List<PriceMovementSummary> updatePriceMovementSummary(List<PriceMovementSummary> priceMovementSummaryList)
	{
		logger.debug("BEGIN updatePriceMovementSummary, size Of priceMovementSummaryList "
				+ priceMovementSummaryList.size());

		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<PriceMovementSummary> erroredPriceMoveSummary = new ArrayList<>();
		try
		{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.UPDATE_PRICE_MOVEMENT_SUMMARY);

			for (PriceMovementSummary priceMovementSummary : priceMovementSummaryList)
			{
				try
				{
					int i = 1;
					stmt.setFloat(i++, (float) priceMovementSummary.getLatestPrice());
					stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getLatestPriceTime().getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getLastPrice());
					stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getLastPriceChangeTime().getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getMaxPrice());
					stmt.setTimestamp(i++, new Timestamp(priceMovementSummary.getMaxPriceTime().getTime()));
					stmt.setFloat(i++, (float) priceMovementSummary.getAveragePrice());
					stmt.setString(i++, String.valueOf(priceMovementSummary.isPriceDrop()));
					stmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					stmt.setInt(i++, priceMovementSummary.getProductId());
					stmt.executeUpdate();
				} catch (SQLException e)
				{
					logger.error(e.getMessage(), e);
					erroredPriceMoveSummary.add(priceMovementSummary);
				}
			}
		} catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			return priceMovementSummaryList;
		} finally
		{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		logger.debug("END updatePriceMovementSummary");
		return erroredPriceMoveSummary;
	}

	public Map<Integer, PriceMovementSummary> getPriceMovementSummaryByDateAsMap(Date calenderDate)
	{
		List<PriceMovementSummary> priceMovementSummaryList = getPriceMovementSummary(calenderDate, -1, null);
		Map<Integer, PriceMovementSummary> priceMovementSummaryMap = new HashMap<>();
		for (PriceMovementSummary pm : priceMovementSummaryList)
			priceMovementSummaryMap.put(pm.getProductId(), pm);
		return priceMovementSummaryMap;
	}

	public List<PriceMovementSummary> getPriceMovementSummaryByDate(Date calenderDate)
	{
		return getPriceMovementSummary(calenderDate, -1, null);
	}

	public List<PriceMovementSummary> getPriceMovementSummaryByCategory(int category_id)
	{
		return getPriceMovementSummary(new Date(System.currentTimeMillis()), category_id, null);
	}

	public List<PriceMovementSummary> getPriceMovementSummaryForToday()
	{
		return getPriceMovementSummary(new Date(System.currentTimeMillis()), -1, null);
	}
	
	//TODO test is pending
	public List<PriceMovementSummary> getPriceMovementSummaryForTodayByRetailer(String retailerId)
	{
		return getPriceMovementSummary(new Date(System.currentTimeMillis()), -1, retailerId);
	}

	//TODO updated tests are pending
	public List<PriceMovementSummary> getPriceMovementUpdates(String retailerId, Timestamp modifiedTime)
	{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<PriceMovementSummary> priceMovementSummaryList = new LinkedList<>();
		try
		{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			Date calendarDate = getPriceMovementRecorderDate();
			if(retailerId==null)
				stmt = sqlConn.prepareStatement(Queries.GET_PRICE_MOVEMENT_SUMMARY_BY_TIMEMODIFIED);
			else
				stmt = sqlConn.prepareStatement(Queries.GET_PRICE_MOVEMENT_SUMMARY_OF_RETAILER_BY_TIMEMODIFIED);
				
			stmt.setTimestamp(1, modifiedTime);
			if(retailerId!=null)
				stmt.setString(2, retailerId);
			
			ResultSet rs = stmt.executeQuery();
			priceMovementSummaryList = getPriceMovementSummaryFromResultSet(null, rs);
		} catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			return priceMovementSummaryList;
		} finally
		{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return priceMovementSummaryList;
	 }

	public Date getPriceMovementRecorderDate()
	{
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}

	public void setPriceMovementRecorderDate(Date p_date)
	{
		lastDAO.setLast(PROCESS_NAME, sdf.format(p_date));
	}

	//TODO Its unit test is pending as have updated this method: added retailerId
	private List<PriceMovementSummary> getPriceMovementSummary(Date calenderDate, int category_id, String retailerId)
	{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<PriceMovementSummary> priceMovementSummaryList = new LinkedList<>();
		try
		{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			if (category_id == -1 && retailerId == null)
				stmt = sqlConn.prepareStatement(Queries.GET_PRICE_MOVEMENT_SUMMARY_BY_DATE);
			else if(category_id != -1 && retailerId == null)
				stmt = sqlConn.prepareStatement(Queries.GET_PRICE_MOVEMENT_SUMMARY_BY_CATEGORY);
			else if(category_id == -1 && retailerId != null)
				stmt = sqlConn.prepareStatement(Queries.GET_ACTIVE_PRICE_MOVEMENT_SUMMARY_BY_RETAILER);

			
			if(ConfigParms.isWindows())//we don't run price movement daily job on windows box
					LOOKBACK_SPAN=LOOKBACK_SPAN_WINDOWS;
			
			if(category_id == -1 && retailerId == null)
			{
				stmt.setTimestamp(1, new Timestamp(DateUtils.addDays(calenderDate, LOOKBACK_SPAN).getTime()));
				stmt.setTimestamp(2, new Timestamp(calenderDate.getTime()));
			}
			else if(category_id != -1 && retailerId == null)
				stmt.setInt(1, category_id);
			else if(category_id == -1 && retailerId != null)
				stmt.setString(1, retailerId);

			ResultSet rs = stmt.executeQuery();
			priceMovementSummaryList = getPriceMovementSummaryFromResultSet(calenderDate, rs);
		} catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			return priceMovementSummaryList;
		} finally
		{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return priceMovementSummaryList;
	}

	private List<PriceMovementSummary> getDummyPriceMovements(){
		PriceMovementSummary priceMovementSummary = createDummy(1, 2.0, new Date(), 2.5, new Date(System.currentTimeMillis()-1000000),
			2.5, new Date(System.currentTimeMillis()-100000), 2.7, "product1", "www.product1.com", "http://i.walmartimages.com/i/p/00/84/72/75/00/0084727500029_180X180.jpg", 3883);
		List<PriceMovementSummary> list = new LinkedList<PriceMovementSummary>();
		list.add(priceMovementSummary);
		
		priceMovementSummary = createDummy(2, 2.0, new Date(), 2.5, new Date(System.currentTimeMillis()-1000000),
				2.5, new Date(System.currentTimeMillis()-100000), 2.7, "product22", "www.product1.com", "http://i.walmartimages.com/i/mp/00/73/50/29/25/0073502925968_P255045_180X180.jpg", 3883);
			list.add(priceMovementSummary);
		
		return list;
	}

	private PriceMovementSummary createDummy(int productId, double price, Date latestPriceTime, double lastPrice, Date lastPriceTime,
			double maxPrice, Date maxPriceTime, double avgPrice, String name, String url, String imageUrl, int categoryId) {
		PriceMovementSummary priceMovementSummary = new PriceMovementSummary();
		priceMovementSummary.setProductId(productId);
		priceMovementSummary.setLatestPrice(price);
		priceMovementSummary.setLatestPriceTime(latestPriceTime);
		priceMovementSummary.setLastPrice(lastPrice);
		priceMovementSummary.setLastPriceChangeTime(lastPriceTime);
		priceMovementSummary.setMaxPrice(maxPrice);
		priceMovementSummary.setMaxPriceTime(maxPriceTime);
		priceMovementSummary.setAveragePrice(avgPrice);
		priceMovementSummary.setName(name);
		priceMovementSummary.setUrl(url);
		priceMovementSummary.setImageUrl(imageUrl);
		LinkedList<Integer> categoryList = new LinkedList<>();
		categoryList.add(categoryId);
		priceMovementSummary.setCategoryIdList(categoryList);
		return priceMovementSummary;
	}
	
	private List<PriceMovementSummary> getPriceMovementSummaryFromResultSet(Date calenderDate, ResultSet rs) throws SQLException
	{
		Map<Integer, PriceMovementSummary> priceMovementSummaryMap = new HashMap<>();
		int counter = 0;
		while (rs.next())
		{
			PriceMovementSummary priceMovementSummary = new PriceMovementSummary();
			priceMovementSummary.setProductId(rs.getInt("PRODUCT_ID"));
			priceMovementSummary.setLatestPrice(rs.getDouble("LATEST_PRICE"));
			priceMovementSummary.setLatestPriceTime(rs.getTime("LATEST_PRICE_TIME"));
			priceMovementSummary.setLastPrice(rs.getDouble("LAST_PRICE"));
			priceMovementSummary.setLastPriceChangeTime(rs.getTimestamp("LAST_PRICE_TIME"));
			priceMovementSummary.setMaxPrice(rs.getDouble("MAX_PRICE"));
			priceMovementSummary.setMaxPriceTime(rs.getTimestamp("MAX_PRICE_TIME"));
			priceMovementSummary.setAveragePrice(rs.getDouble("AVERAGE_PRICE"));
			priceMovementSummary.setName(rs.getString("PRODUCT_NAME"));
			priceMovementSummary.setUrl(rs.getString("URL"));
			priceMovementSummary.setImageUrl(rs.getString("IMAGE_URL"));
			
			List<Integer> categoryList = null;
			if(priceMovementSummaryMap.containsKey(priceMovementSummary.getProductId()))
				categoryList = priceMovementSummaryMap.get(priceMovementSummary.getProductId()).getCategoryIdList();
			else
				categoryList = new LinkedList<>();
			categoryList.add(rs.getInt("CATEGORY_ID"));
			priceMovementSummary.setCategoryIdList(categoryList);
			priceMovementSummaryMap.put(priceMovementSummary.getProductId(), priceMovementSummary);
			
			if(counter++ % 50000 == 0)
				logger.info("Fetched rows, still fetching:" + counter);
		}

		return new LinkedList<>(priceMovementSummaryMap.values()); 
	}

	public Timestamp getPriceMovementMaxModifiedTime() throws SQLException
	{
		DbConnection conn = null;
		PreparedStatement prodStmt = null;
		ResultSet resultsRS = null;
		Timestamp modifiedTime = null;
		try
		{
			conn = pool.getConnection();
			Connection sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_MAX_TIME_MODIFIED_PRICE_MOVEMENT_SUMMARY);
			resultsRS = prodStmt.executeQuery();

			while (resultsRS.next())
			{
				String time = resultsRS.getString(1);
				if(time!=null)
					modifiedTime = Timestamp.valueOf(time);
				else
					modifiedTime = new Timestamp(System.currentTimeMillis());
			}
			return modifiedTime;
		} finally
		{
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public List<PriceMovementSummary> getPriceMovementSummaryForProduct(int product_id)
	{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<PriceMovementSummary> priceMovementSummaryList = new LinkedList<>();
		try
		{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.GET_PRICE_MOVEMENT_SUMMARY_BY_PRODUCT);

			stmt.setInt(1, product_id);

			ResultSet rs = stmt.executeQuery();
			priceMovementSummaryList = getPriceMovementSummaryFromResultSet(null, rs);
		} catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			return priceMovementSummaryList;
		} finally
		{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return priceMovementSummaryList;
	}
*/
}
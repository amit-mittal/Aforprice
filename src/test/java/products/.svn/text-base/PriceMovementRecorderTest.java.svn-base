package products;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;
import util.build.ProductPricesHistoryBuilder;
import db.dao.DAOException;
import db.dao.LastProcessedDAO;
import db.dao.PriceMovementRecorderDAOFactory;
import db.dao.PriceMovementSummaryDAO;
import db.dao.ProductPricesHistoryDAO;
import entities.PriceMovementSummary;
import entities.ProductPricesHistory;

public class PriceMovementRecorderTest extends AbstractTest
{
/*	private ProductPricesHistory P1;
	private ProductPricesHistory P11;
	private ProductPricesHistory P2;
	private ProductPricesHistory P12;
	private PriceMovementSummaryDAO mockPriceMovementSummaryDAO = mock(PriceMovementSummaryDAO.class);
	private ProductPricesHistoryDAO mockProdPriceHistDAO = mock(ProductPricesHistoryDAO.class);
	private LastProcessedDAO mockLastProcDAO = mock(LastProcessedDAO.class);

	
	public void setupmock()
	{
		PriceMovementRecorderDAOFactory.getInstance().setLastProcDAO(mockLastProcDAO);
		PriceMovementRecorderDAOFactory.getInstance().setPriceMovementSummaryDAO(mockPriceMovementSummaryDAO);
		PriceMovementRecorderDAOFactory.getInstance().setProductPriceHistDAO(mockProdPriceHistDAO);
	}
	
	@Before
	public void setup()
	{
		Date currentTime = new Date();
		Date modifiedTime = new Date();
		
		ProductPricesHistoryBuilder PB1 = new ProductPricesHistoryBuilder();
		PB1.productId = 1234;
		PB1.price = 100;
		PB1.time = currentTime;
		PB1.timeModified = modifiedTime;
		P1 = PB1.build();
		
		PB1.productId = 1234;
		PB1.price = 200;
		PB1.time = DateUtils.addMinutes(currentTime, 1);
		PB1.timeModified = DateUtils.addMinutes(currentTime, 5);
		P11 = PB1.build();
		
		PB1.productId = 4567;
		PB1.price = 300;
		PB1.time = DateUtils.addMinutes(currentTime, 5);
		PB1.timeModified = DateUtils.addMinutes(currentTime, 10);
		P2 = PB1.build();
				
		PB1.productId = 1234;
		PB1.price = 300;
		PB1.time = DateUtils.addMinutes(currentTime, 1);
		PB1.timeModified = DateUtils.addMinutes(currentTime, 5);
		P12 = PB1.build();
		
		setupmock();
	}
	
	@Test
	public void testGeneratePriceMovementSummary() throws  SQLException
	{
		PriceMovementRecorder priceMovementRecorder = new PriceMovementRecorder();

		Map<Integer, List<ProductPricesHistory>> priceMovementForSpan = new HashMap<>();
		
		List<ProductPricesHistory> productPricesHistory = new ArrayList<>();
		productPricesHistory.add(P1);
		productPricesHistory.add(P11);
		priceMovementForSpan.put(Integer.valueOf(P1.getProductId()), productPricesHistory);
		
		productPricesHistory = new ArrayList<>();
		productPricesHistory.add(P2);
		priceMovementForSpan.put(Integer.valueOf(P2.getProductId()), productPricesHistory);
		
		//Test 1: Create Empty Map & have bunch of rows for inserts
		priceMovementRecorder.generatePriceMovementSummary(priceMovementForSpan);
		assertTrue(priceMovementRecorder.getPriceMovementSummaryInsertList().size() == 1);
		assertTrue(priceMovementRecorder.getPriceMovementSummaryUpdateList().size() == 0);
		
		for( PriceMovementSummary p: priceMovementRecorder.getPriceMovementSummaryInsertList() )
		{
			if( p.getProductId() == P1.getProductId() )
			{
				assertEquals("Max Price doesn't match", p.getMaxPrice(), Double.parseDouble("200"), 0.1); 
				assertEquals("Latest Price doesn't match", p.getLatestPrice(), Double.parseDouble("200"), 0.1); 
				assertEquals("Average Price doesn't match", p.getAveragePrice(), Double.parseDouble("150"), 0.1); 
				assertEquals("Last Price doesn't match", p.getLastPrice(), Double.parseDouble("100"), 0.1); 
			}
			if( p.getProductId() == P2.getProductId() )
			{
				assertEquals("Max Price doesn't match", p.getMaxPrice(), Double.parseDouble("300"), 0.1); 
				assertEquals("Latest Price doesn't match", p.getLatestPrice(), Double.parseDouble("300"), 0.1); 
				assertEquals("Average Price doesn't match", p.getAveragePrice(), Double.parseDouble("300"), 0.1); 
				assertEquals("Last Price doesn't match", p.getLastPrice(), Double.parseDouble("0"), 0.1); 
			}
		}

		//Test 2: For the same Map update some existing products ( Price Up & Price Down )
		priceMovementRecorder.setPriceMovementSummaryInsertList(new ArrayList<PriceMovementSummary>());
		priceMovementRecorder.setPriceMovementSummaryUpdateList(new ArrayList<PriceMovementSummary>());
		priceMovementForSpan.get(P12.getProductId()).add(P12);
		priceMovementRecorder.generatePriceMovementSummary(priceMovementForSpan);
		assertTrue(priceMovementRecorder.getPriceMovementSummaryInsertList().size() == 0);
		assertTrue(priceMovementRecorder.getPriceMovementSummaryUpdateList().size() == 1);
		
		for( PriceMovementSummary p: priceMovementRecorder.getPriceMovementSummaryInsertList() )
		{
			if( p.getProductId() == P1.getProductId() )
			{
				assertEquals("Max Price doesn't match", p.getMaxPrice(), Double.parseDouble("300"), 0.1); 
				assertEquals("Latest Price doesn't match", p.getLatestPrice(), Double.parseDouble("300"), 0.1); 
				assertEquals("Average Price doesn't match", p.getAveragePrice(), Double.parseDouble("200"), 0.1); 
				assertEquals("Last Price doesn't match", p.getLastPrice(), Double.parseDouble("200"), 0.1); 
			}
		}
		
	}
	
	@Test
	public void testGetPriceMovementSummaryForProduct()
	{
		PriceMovementRecorder priceMovementRecorder = new PriceMovementRecorder();

		List<PriceMovementSummary> priceMovementSummaryList = new ArrayList<PriceMovementSummary>();
		PriceMovementSummary pms1 = new PriceMovementSummary();
		pms1.setProductId(123);
		pms1.setLastPrice(8);
		pms1.setLatestPrice(10);
		pms1.setAveragePrice(9);
		pms1.setLastPriceChangeTime(new Date());
		priceMovementSummaryList.add(pms1);
		when(mockPriceMovementSummaryDAO.getPriceMovementSummaryForProduct(123)).thenReturn(priceMovementSummaryList);
		
		PriceMovementSummary priceMovementSummary = priceMovementRecorder.getPriceMovementSummaryForProduct(123);
		_assertNotNull(priceMovementRecorder.priceMovementSummaryMap.get(Integer.valueOf(123)), "Expecting priceMovementSummary for product 123");
		_assertEquals(priceMovementSummary.getAveragePrice(), pms1.getAveragePrice());
		_assertEquals(priceMovementSummary.getLastPrice(), pms1.getLastPrice());
		
		PriceMovementSummary pms2 = new PriceMovementSummary();
		pms2.setProductId(123);
		pms2.setLastPrice(8);
		pms2.setLatestPrice(10);
		pms2.setAveragePrice(20);
		pms2.setLastPriceChangeTime(new Date());
		priceMovementRecorder.priceMovementSummaryMap.put(123, pms2);

		priceMovementSummary = priceMovementRecorder.getPriceMovementSummaryForProduct(123);
		_assertNotNull(priceMovementRecorder.priceMovementSummaryMap.get(123), "Expecting priceMovementSummary for product 123");
		_assertEquals(priceMovementRecorder.priceMovementSummaryMap.get(123).getAveragePrice(), pms2.getAveragePrice());
	}
	
	@Test
	public void testGetProductsWithPriceChange() throws DAOException, ParseException
	{
		PriceMovementRecorder priceMovementRecorder = new PriceMovementRecorder();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		priceMovementRecorder.m_lastProcessedTime = sdf.parse("2012-01-01");
		
		Timestamp fromTime = new Timestamp(priceMovementRecorder.m_lastProcessedTime.getTime());
		Timestamp toTime = new Timestamp(priceMovementRecorder.m_lastProcessedTime.getTime() + (PriceMovementRecorder.PRODUCT_PRICE_HISTORY_QUERY_SPAN * 60 * 1000));
				
		priceMovementRecorder.getProductsWithPriceChange();
		when(mockProdPriceHistDAO.getProductPricesHistoryForSpan(fromTime, toTime)).thenReturn(null);
		_assertEquals(priceMovementRecorder.m_lastProcessedTime, toTime );

		long currentTime = System.currentTimeMillis();
		priceMovementRecorder.m_lastProcessedTime = new Date(currentTime);
		
		fromTime = new Timestamp(priceMovementRecorder.m_lastProcessedTime.getTime());
		toTime = new Timestamp(priceMovementRecorder.m_lastProcessedTime.getTime() + (PriceMovementRecorder.PRODUCT_PRICE_HISTORY_QUERY_SPAN * 60 * 1000));
				
		priceMovementRecorder.getProductsWithPriceChange();
		when(mockProdPriceHistDAO.getProductPricesHistoryForSpan(fromTime, toTime)).thenReturn(null);
		_assertTrue(priceMovementRecorder.m_lastProcessedTime.before(toTime), "last processed time should be less than totime here" );

		
	}
	@After
	public void cleanup()
	{
	}
*/
}

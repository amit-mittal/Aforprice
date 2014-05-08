package entities;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;

public class PriceMovementSummaryTest extends AbstractTest
{
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testPriceMovementSummaryOrdering()
	{
		PriceMovementSummary pm1 = new PriceMovementSummary(1234, 500, new Date(), 400, 500, 400);		
		PriceMovementSummary pm2 = new PriceMovementSummary(4567, 350, new Date(),
				400, 500, 400);		
		_assertTrue( pm1.compareTo(pm2) > 0, "pm1.compareTo(pm2) > 0 : If the price was above from last & equal to max, it is bad");
		_assertTrue( !pm1.isPriceDrop(), "pm1 should not be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );

		pm1 = new PriceMovementSummary(1234, 300, new Date(),
				400,  500, 400);		
		pm2 = new PriceMovementSummary(4567, 350, new Date(),
				400, 500, 400);		
		_assertTrue( pm1.compareTo(pm2) < 0, "pm1.compareTo(pm2) < 0 : Both price below last as well as max but one is better");
		
		pm1 = new PriceMovementSummary(1234, 420, new Date(),
				400, 500, 460);		
		pm2 = new PriceMovementSummary(4567, 450, new Date(),
				400, 500, 460);		
		_assertTrue( pm1.compareTo(pm2) < 0, "pm1.compareTo(pm2) < 0 : Both price below max above last but better that average");
		_assertTrue( pm1.isPriceDrop(), "pm1 should be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );
		
		pm1 = new PriceMovementSummary(1234, 500, new Date(),
				1000, 1000, 460);		
		pm2 = new PriceMovementSummary(4567, 5, new Date(),
				400, 500, 300);		
		_assertTrue( pm1.compareTo(pm2) < 0, "pm1.compareTo(pm2) < 0 : sort by price drop amount first");
		_assertTrue( pm1.isPriceDrop(), "pm1 should be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );	
		
		pm1 = new PriceMovementSummary(1234, 500, new Date(),
				1000, 1000, 460);		
		pm2 = new PriceMovementSummary(4567, 5, new Date(),
				505, 500, 300);		
		_assertTrue( pm1.compareTo(pm2) > 0, "pm1.compareTo(pm2) > 0 : sort by percentage when price drop amount same");
		_assertTrue( pm1.isPriceDrop(), "pm1 should be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );	
	

		pm1 = new PriceMovementSummary(1234, 500, new Date(),
				1000, 1000, 460);		
		pm2 = new PriceMovementSummary(4567, 5, new Date(),
				505, 500, 300);		
		_assertTrue( pm1.compareTo(pm2) > 0, "pm1.compareTo(pm2) > 0 : sort by percentage when price drop amount same");
		_assertTrue( pm1.isPriceDrop(), "pm1 should be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );	
		
		pm1 = new PriceMovementSummary(1234, 500, new Date(),
				1000, 1000, 600);		
		pm2 = new PriceMovementSummary(4567, 500, new Date(),
				1000, 1000, 700);		
		_assertTrue( pm1.compareTo(pm2) > 0, "pm1.compareTo(pm2) > 0 : sort by drop from average with price amount and percentage are same]");
		_assertTrue( pm1.isPriceDrop(), "pm1 should be a price drop" );
		_assertTrue( pm2.isPriceDrop(), "pm2 should be a price drop" );	

		pm1 = new PriceMovementSummary(1234, 1100, new Date(),
				1000, 1000, 600);		
		pm2 = new PriceMovementSummary(4567, 1000, new Date(),
				1000, 1200, 700);		
		_assertTrue( !pm1.isPriceDrop(), "pm1 should not be a price drop" );
		_assertTrue( !pm2.isPriceDrop(), "pm2 should not be a price drop" );	
	
	}
	
	@After
	public void tearDown() throws Exception
	{
	}
}

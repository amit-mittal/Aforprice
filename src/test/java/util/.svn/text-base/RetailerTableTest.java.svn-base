package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.Retailer;

public class RetailerTableTest extends AbstractTest{
	private final String PRODUCT_PRICES_HISTORY = "PRODUCT_PRICES_HISTORY";
	private final String PRODUCT_REVIEWS_HISTORY = "PRODUCT_REVIEWS_HISTORY";
	//private final String PRODUCT_SELL_RANKS_HISTORY = "PRODUCT_SELL_RANKS_HISTORY";

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public final void testGetPricesHistoryTable() {
		if(!RetailerTable.isTableSplitEnabled()){
			_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.BANANAREPUBLIC.getId()), PRODUCT_PRICES_HISTORY);
			return;			
		}
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.BANANAREPUBLIC.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.BJS.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.BESTBUY.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.COSTCO.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.CVS.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.GAP.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.HOMEDEPOT.getId()), PRODUCT_PRICES_HISTORY+"_1");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.JCPENNY.getId()), PRODUCT_PRICES_HISTORY+"_1");

		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.KOHLS.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.LOWES.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.MACYS.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.NORDSTROM.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.OLDNAVY.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.RITEAID.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.SEARS.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.SAMSCLUB.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.STAPLES.getId()), PRODUCT_PRICES_HISTORY+"_2");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.WALGREENS.getId()), PRODUCT_PRICES_HISTORY+"_2");

		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.WALMART.getId()), PRODUCT_PRICES_HISTORY+"_3");


		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.TARGET_MOBILE.getId()), PRODUCT_PRICES_HISTORY+"_4");

		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.AWSAMAZON.getId()), PRODUCT_PRICES_HISTORY+"_5");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.AMAZONBESTSELLER.getId()), PRODUCT_PRICES_HISTORY+"_5");

		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.BABYSRUS.getId()), PRODUCT_PRICES_HISTORY+"_6");
		_assertEquals(RetailerTable.getPricesHistoryTable(Retailer.TOYSRUS.getId()), PRODUCT_PRICES_HISTORY+"_6");

	}

	@Test
	public final void testGetReviewsHistoryTable() {
		if(!RetailerTable.isTableSplitEnabled()){
			_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.BANANAREPUBLIC.getId()), PRODUCT_REVIEWS_HISTORY);
			return;			
		}
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.BANANAREPUBLIC.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.BJS.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.BESTBUY.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.COSTCO.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.CVS.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.GAP.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.HOMEDEPOT.getId()), PRODUCT_REVIEWS_HISTORY+"_1");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.JCPENNY.getId()), PRODUCT_REVIEWS_HISTORY+"_1");

		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.KOHLS.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.LOWES.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.MACYS.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.NORDSTROM.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.OLDNAVY.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.RITEAID.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.SEARS.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.SAMSCLUB.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.STAPLES.getId()), PRODUCT_REVIEWS_HISTORY+"_2");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.WALGREENS.getId()), PRODUCT_REVIEWS_HISTORY+"_2");

		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.WALMART.getId()), PRODUCT_REVIEWS_HISTORY+"_3");


		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.TARGET_MOBILE.getId()), PRODUCT_REVIEWS_HISTORY+"_4");

		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.AWSAMAZON.getId()), PRODUCT_REVIEWS_HISTORY+"_5");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.AMAZONBESTSELLER.getId()), PRODUCT_REVIEWS_HISTORY+"_5");

		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.BABYSRUS.getId()), PRODUCT_REVIEWS_HISTORY+"_6");
		_assertEquals(RetailerTable.getReviewsHistoryTable(Retailer.TOYSRUS.getId()), PRODUCT_REVIEWS_HISTORY+"_6");
	}

	/*
	@Test
	public final void testGetSellRanksHistoryTable() {
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.BANANAREPUBLIC.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.BJS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.BESTBUY.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.COSTCO.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.CVS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.GAP.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.HOMEDEPOT.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.JCPENNY.getId()), PRODUCT_SELL_RANKS_HISTORY+"_1");

		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.KOHLS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.LOWES.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.MACYS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.NORDSTROM.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.OLDNAVY.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.RITEAID.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.SEARS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.SAMSCLUB.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.STAPLES.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.WALGREENS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_2");

		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.WALMART.getId()), PRODUCT_SELL_RANKS_HISTORY+"_3");


		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.TARGET_MOBILE.getId()), PRODUCT_SELL_RANKS_HISTORY+"_4");

		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.AWSAMAZON.getId()), PRODUCT_SELL_RANKS_HISTORY+"_5");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.AMAZONBESTSELLER.getId()), PRODUCT_SELL_RANKS_HISTORY+"_5");

		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.BABYSRUS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_6");
		_assertEquals(RetailerTable.getSellRanksHistoryTable(Retailer.TOYSRUS.getId()), PRODUCT_SELL_RANKS_HISTORY+"_6");
	}
	*/
	

}

package entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import util.AbstractTest;

public class RetailerTest extends AbstractTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testIsSortedBySellRank() {
		_assertTrue(Retailer.AWSAMAZON.isSortedBySellRank()==false);
		_assertTrue(Retailer.AMAZONBESTSELLER.isSortedBySellRank()==true);
		_assertTrue(Retailer.BABYSRUS.isSortedBySellRank()==true);
		_assertTrue(Retailer.BANANAREPUBLIC.isSortedBySellRank()==false);
		_assertTrue(Retailer.BJS.isSortedBySellRank()==false);
		_assertTrue(Retailer.BESTBUY.isSortedBySellRank()==false);
		_assertTrue(Retailer.COSTCO.isSortedBySellRank()==false);
		_assertTrue(Retailer.CVS.isSortedBySellRank()==false);
		_assertTrue(Retailer.GAP.isSortedBySellRank()==false);
		_assertTrue(Retailer.HOMEDEPOT.isSortedBySellRank()==true);
		_assertTrue(Retailer.JCPENNY.isSortedBySellRank()==false);
		_assertTrue(Retailer.KOHLS.isSortedBySellRank()==true);
		_assertTrue(Retailer.LOWES.isSortedBySellRank()==true);
		_assertTrue(Retailer.MACYS.isSortedBySellRank()==false);
		_assertTrue(Retailer.NORDSTROM.isSortedBySellRank()==false);
		_assertTrue(Retailer.OLDNAVY.isSortedBySellRank()==false);
		_assertTrue(Retailer.RITEAID.isSortedBySellRank()==true);
		_assertTrue(Retailer.SEARS.isSortedBySellRank()==false);
		_assertTrue(Retailer.SAMSCLUB.isSortedBySellRank()==true);
		_assertTrue(Retailer.STAPLES.isSortedBySellRank()==false);
		_assertTrue(Retailer.TARGET.isSortedBySellRank()==false);
		_assertTrue(Retailer.TARGET_MOBILE.isSortedBySellRank()==true);
		_assertTrue(Retailer.TOYSRUS.isSortedBySellRank()==true);
		_assertTrue(Retailer.WALGREENS.isSortedBySellRank()==true);
		_assertTrue(Retailer.WALMART.isSortedBySellRank()==true);
	}

	@Test
	public final void testHasReviews() {
		_assertTrue(Retailer.AWSAMAZON.hasReviews()==false);
		_assertTrue(Retailer.AMAZONBESTSELLER.hasReviews()==true);
		_assertTrue(Retailer.BABYSRUS.hasReviews()==true);
		_assertTrue(Retailer.BANANAREPUBLIC.hasReviews()==false);
		_assertTrue(Retailer.BJS.hasReviews()==true);
		_assertTrue(Retailer.BESTBUY.hasReviews()==false);
		_assertTrue(Retailer.COSTCO.hasReviews()==true);
		_assertTrue(Retailer.CVS.hasReviews()==true);
		_assertTrue(Retailer.GAP.hasReviews()==false);
		_assertTrue(Retailer.HOMEDEPOT.hasReviews()==true);
		_assertTrue(Retailer.JCPENNY.hasReviews()==true);
		_assertTrue(Retailer.KOHLS.hasReviews()==true);
		_assertTrue(Retailer.LOWES.hasReviews()==true);
		_assertTrue(Retailer.MACYS.hasReviews()==false);
		_assertTrue(Retailer.NORDSTROM.hasReviews()==false);
		_assertTrue(Retailer.OLDNAVY.hasReviews()==false);
		_assertTrue(Retailer.RITEAID.hasReviews()==false);
		_assertTrue(Retailer.SEARS.hasReviews()==false);
		_assertTrue(Retailer.SAMSCLUB.hasReviews()==false);
		_assertTrue(Retailer.STAPLES.hasReviews()==true);
		_assertTrue(Retailer.TARGET.hasReviews()==false);
		_assertTrue(Retailer.TARGET_MOBILE.hasReviews()==true);
		_assertTrue(Retailer.TOYSRUS.hasReviews()==true);
		_assertTrue(Retailer.WALGREENS.hasReviews()==true);
		_assertTrue(Retailer.WALMART.hasReviews()==true);
	}

	@Test
	public final void testHasNumReviewers() {
		_assertTrue(Retailer.AWSAMAZON.hasNumReviewers()==false);
		_assertTrue(Retailer.AMAZONBESTSELLER.hasNumReviewers()==true);
		_assertTrue(Retailer.BABYSRUS.hasNumReviewers()==false);
		_assertTrue(Retailer.BANANAREPUBLIC.hasNumReviewers()==false);
		_assertTrue(Retailer.BJS.hasNumReviewers()==true);
		_assertTrue(Retailer.BESTBUY.hasNumReviewers()==false);
		_assertTrue(Retailer.COSTCO.hasNumReviewers()==true);
		_assertTrue(Retailer.CVS.hasNumReviewers()==true);
		_assertTrue(Retailer.GAP.hasNumReviewers()==false);
		_assertTrue(Retailer.HOMEDEPOT.hasNumReviewers()==true);
		_assertTrue(Retailer.JCPENNY.hasNumReviewers()==true);
		_assertTrue(Retailer.KOHLS.hasNumReviewers()==true);
		_assertTrue(Retailer.LOWES.hasNumReviewers()==true);
		_assertTrue(Retailer.MACYS.hasNumReviewers()==false);
		_assertTrue(Retailer.NORDSTROM.hasNumReviewers()==false);
		_assertTrue(Retailer.OLDNAVY.hasNumReviewers()==false);
		_assertTrue(Retailer.RITEAID.hasNumReviewers()==true);
		_assertTrue(Retailer.SEARS.hasNumReviewers()==false);
		_assertTrue(Retailer.SAMSCLUB.hasNumReviewers()==false);
		_assertTrue(Retailer.STAPLES.hasNumReviewers()==true);
		_assertTrue(Retailer.TARGET.hasNumReviewers()==false);
		_assertTrue(Retailer.TARGET_MOBILE.hasNumReviewers()==true);
		_assertTrue(Retailer.TOYSRUS.hasNumReviewers()==false);
		_assertTrue(Retailer.WALGREENS.hasNumReviewers()==true);
		_assertTrue(Retailer.WALMART.hasNumReviewers()==true);
	}

}

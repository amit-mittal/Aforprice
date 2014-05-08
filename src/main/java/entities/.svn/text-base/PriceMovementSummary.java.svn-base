package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;

public class PriceMovementSummary implements Comparable<PriceMovementSummary>
{
	private static final Logger logger = Logger.getLogger(PriceMovementSummary.class);
	Product product;
	
	final static double EPSILON = 0.000001;
	final static int BEFORE = -1;
	final static int EQUAL = 0;
	final static int AFTER = 1;
	final static int DROPPED_IN_DURATION1 = 0; 
	final static int DROPPED_IN_DURATION2 = -1; 
    final static int DROPPED_IN_DURATION3 = -2;
    final static int DROPPED_IN_DURATION4 = -3;
    final static int DROP_TOO_OLD = -100;
    final static int DROP_DURATION_1 = 7;
    final static int DROP_DURATION_2 = 30;
    final static int DROP_DURATION_3 = 90;
    final static int DROP_DURATION_4 = 365;
    
    //only kept it to avoid changing tests otherwise pass PriceHistory as argument
    @Deprecated
	public PriceMovementSummary(int productId, double latestPrice, Date latestPriceTime,
			double lastPrice, double maxPrice, double averagePrice)
	{
		this.product = new Product();
		product.productId = productId;
		PriceHistory priceHistory = new PriceHistory();
		product.setPriceHistory(priceHistory);
		priceHistory.maxPrice = maxPrice;
		priceHistory.averagePrice = averagePrice;
		List<Tick> priceTicks = new ArrayList<Tick>();
		priceHistory.setPriceTicks(priceTicks);
		priceTicks.add(new Tick( latestPriceTime.getTime(), lastPrice));
		priceTicks.add(new Tick(latestPriceTime.getTime(), latestPrice));
	}
	
	public PriceMovementSummary(Product product){
		this.product = product;
	}
	public PriceMovementSummary(PriceMovementSummaryBuilder builder){
		this(builder.productId, builder.latestPrice, builder.latestPriceTime, 
				builder.lastPrice, builder.maxPrice, builder.averagePrice);
	}

	/**
	 * create a copy of this object
	 * @return new object with same data as 'from' object
	 */
	public PriceMovementSummary clone(){
		PriceMovementSummary to = new PriceMovementSummary(this.product);
		return to;
	}
	
	public Integer getProductId(){
		return this.product.getProductId();
	}

	private static final Tick DUMMY_TICK = new Tick(System.currentTimeMillis(), Double.NaN);
	
	private Tick getCurrentPriceTick(){
		List<Tick> priceTicks = this.product.getPriceHistory().getPriceTicks();
		if(priceTicks.size()==0){
			logger.error("No price ticks for productid "+getProductId());
			return DUMMY_TICK;
		}
		return priceTicks.get(priceTicks.size()-1);	
	}

	/**
	 * get 2nd last price
	 * @return
	 */
	private Tick getPreviousPriceTick(){
		List<Tick> priceTicks = this.product.getPriceHistory().getPriceTicks();
		if(priceTicks.size()<=1)
			return getCurrentPriceTick();
		return priceTicks.get(priceTicks.size()-2);	
	}

	public double getCurrentPrice(){
		return getCurrentPriceTick().getValue();
	}

	public Date getCurrentPriceTime(){
		return new Date(getCurrentPriceTick().getTime());
	}

	public double getPreviousPrice(){
		return getPreviousPriceTick().getValue();
	}

	public double getMaxPrice(){
		return product.getPriceHistory().getMaxPrice();
	}

	public double getAveragePrice(){
		return product.getPriceHistory().getAveragePrice();
	}

	public double getCurrentPriceDrop(){
		return (getPreviousPrice() - getCurrentPrice());
	}

	public double getCurrentPercentagePriceDrop(){
		return 100 * (getCurrentPriceDrop()) / getCurrentPrice();
	}

	public double getPriceDropFromAverage(){
		return (getAveragePrice() - getCurrentPrice());
	}
	
	//A price movement is considered DROP if latest price less than last price or latest price less than average price
	public boolean isPriceDrop(){
		return( getCurrentPrice() < getPreviousPrice() || getCurrentPrice() < getAveragePrice() );
	}
	
	public int getWeightByPriceMovementDate(){
		if( DateUtils.addDays( getCurrentPriceTime(), DROP_DURATION_1 ).getTime() > System.currentTimeMillis() )
			return DROPPED_IN_DURATION1;
		if( DateUtils.addDays( getCurrentPriceTime(), DROP_DURATION_2 ).getTime() > System.currentTimeMillis() )
			return DROPPED_IN_DURATION2;
		if( DateUtils.addDays( getCurrentPriceTime(), DROP_DURATION_3 ).getTime() > System.currentTimeMillis() )
			return DROPPED_IN_DURATION3;
		if( DateUtils.addDays( getCurrentPriceTime(), DROP_DURATION_4 ).getTime() > System.currentTimeMillis() )
			return DROPPED_IN_DURATION4;		
		return DROP_TOO_OLD;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceMovementSummary other = (PriceMovementSummary) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public int compareTo(PriceMovementSummary toCompare)
	{
		if (this == toCompare)
			return EQUAL;
		
		//1 product has price drop while other doesn't have it.
		if( isPriceDrop() && !toCompare.isPriceDrop() )
			return BEFORE;
		else if( toCompare.isPriceDrop() && !isPriceDrop())
			return AFTER;

		//Both products have drop or both have upside movement
		//Case 1 : both has price drop
		if( isPriceDrop() && toCompare.isPriceDrop())
		{
			//Factor in the date when drop happened, the drop in last week is most important vs. one in last month
			if( getWeightByPriceMovementDate() > toCompare.getWeightByPriceMovementDate() )
				return BEFORE;
			else if( getWeightByPriceMovementDate() < toCompare.getWeightByPriceMovementDate() )
				return AFTER;
			
			// The absolute $ price drop matters the most, products with more $
			// value in price drop comes at top.
			if (getCurrentPriceDrop() > 0 && getCurrentPriceDrop() > toCompare.getCurrentPriceDrop())
				return BEFORE;
			else if (toCompare.getCurrentPriceDrop() > 0
					&& toCompare.getCurrentPriceDrop() > this.getCurrentPriceDrop())
				return AFTER;
	
			// The percentage price drop matters when $ price drop is same, products with more
			// percentage drop comes at top
			if (getCurrentPercentagePriceDrop() > 0
					&& getCurrentPercentagePriceDrop() > toCompare.getCurrentPercentagePriceDrop())
				return BEFORE;
			else if (toCompare.getCurrentPercentagePriceDrop() > 0
					&& toCompare.getCurrentPercentagePriceDrop() > getCurrentPercentagePriceDrop())
				return AFTER;
			
			// This means the price drop is negative for both price movement, let us look
			// at the price differential from average
			if (getPriceDropFromAverage() > 0 && getPriceDropFromAverage() > toCompare.getPriceDropFromAverage())
				return BEFORE;
			else if (toCompare.getPriceDropFromAverage() > 0
					&& toCompare.getPriceDropFromAverage() > getPriceDropFromAverage())
				return AFTER;
		}
		
		//Case 2: Price same or upward movement

		// Same price from prior is better than price increase
		if(getCurrentPriceDrop() != 0 && Math.abs(toCompare.getCurrentPercentagePriceDrop()) < EPSILON)
			return AFTER;
		else if(toCompare.getCurrentPercentagePriceDrop() != 0 && Math.abs(getCurrentPriceDrop()) < EPSILON)
			return BEFORE;

		//If we came here price went up for both products
		
		// whatever products price went up less comes BEFORE
		if (getCurrentPriceDrop() < 0 && getCurrentPriceDrop() > toCompare.getCurrentPriceDrop())
			return BEFORE;
		else if (toCompare.getCurrentPriceDrop() < 0
				&& toCompare.getCurrentPriceDrop() > this.getCurrentPriceDrop())
			return AFTER;

		//There is no price drop so these objects are equal in top drop calculation
		return this.getProductId().compareTo(toCompare.getProductId());
	}

	@Override
	public String toString(){
		return "PriceMovementSummary [ productId=" + getProductId() + ", currentPrice=" + getCurrentPrice()
				+ ", currentPriceTime=" + getCurrentPriceTime() + ", previousPrice="
				+ getPreviousPrice() + ", maxPrice=" + getMaxPrice() + ", averagePrice=" + getAveragePrice() + "]\n";
	}
	

	public static class PriceMovementSummaryBuilder{
		private Integer productId;
		private double latestPrice;
		private Date latestPriceTime;
		private double lastPrice;
		private double maxPrice;
		private double averagePrice;
		
		public PriceMovementSummaryBuilder productId(Integer productId){
			this.productId = productId;
			return this;
		}
		public PriceMovementSummaryBuilder latestPrice(double latestPrice){
			this.latestPrice = latestPrice;
			return this;
		}
		public PriceMovementSummaryBuilder latestPriceTime(Date latestPriceTime){
			this.latestPriceTime = latestPriceTime;
			return this;
		}
		public PriceMovementSummaryBuilder lastPrice(double lastPrice){
			this.lastPrice = lastPrice;
			return this;
		}
		public PriceMovementSummaryBuilder maxPrice(double maxPrice){
			this.maxPrice = maxPrice;
			return this;
		}
		public PriceMovementSummaryBuilder averagePrice(double averagePrice){
			this.averagePrice = averagePrice;
			return this;
		}
		public PriceMovementSummary build(){
			return new PriceMovementSummary(this);
		}
		
	}
}

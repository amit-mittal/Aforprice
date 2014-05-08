package thrift.servers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.ConfigParms;

/**
 * @author AMIT
 * Test in ProductCacheImplUtilTest 
 */
public class ProductCacheImplUtil {
	private final static Logger logger = Logger.getLogger(ProductCacheImplUtil.class);
	private static int MAX_SELL_RANK_HISTORY_SIZE = 5;
	private static int MAX_REVIEW_HISTORY_SIZE = 5;//-1 means do no tamper except removal of negative values
	private static final double EPSILON = 0.00000001;
	
	public void overrideMaxSellRankHistorySize(int maxSellRankHistorySize){
		MAX_SELL_RANK_HISTORY_SIZE = maxSellRankHistorySize;
	}
	
	public void overrideMaxReviewHistorySize(int maxReviewHistorySize){
		MAX_REVIEW_HISTORY_SIZE = maxReviewHistorySize;
	}
	/**
	 * remove any noise like zero values
	 * @param priceTicks
	 */
	public void cleanUpTicks(List<Tick> priceTicks){
		List<Tick> newPriceTicks = new ArrayList<Tick>();
		for(Tick tick : priceTicks){
			if(tick.getValue()>EPSILON)
				newPriceTicks.add(tick);
			//ToDo: remove this method if below is not printed in logs
			else
				logger.info("DEBUG ProductCacheImplUtil:cleanUpTicks is needed, price is less than EPSILON ");
		}
		priceTicks.clear();
		priceTicks.addAll(newPriceTicks);
	}
	
	//TODO this method has problem, what if 1st entry has -ve numReviews but +ve review rating
	public void cleanUpReviewHistory(List<Review> reviewHistory){
		List<Review> newReviewHistory = new ArrayList<Review>();
		int lastNumReviews = 0;
		for(Review review : reviewHistory){
			if(review.getReviewRating()>EPSILON){
				if(review.getNumReviews()<0)
					review.setNumReviews(lastNumReviews);
				newReviewHistory.add(review);
				lastNumReviews = review.getNumReviews();
			}
		}
		reviewHistory.clear();
		reviewHistory.addAll(newReviewHistory);
	}
	
	/**
	 * Fill price history with min, max value, time etc using price ticks in its history
	 * @param priceHist
	 */
	public void processPriceHistory(PriceHistory priceHist){
		cleanUpTicks(priceHist.getPriceTicks());
		List<Tick> priceTicks = priceHist.getPriceTicks();
		if(priceTicks.size()==0)//sanity-check
			return;
		
		priceHist.setCurrPrice(priceTicks.get(priceTicks.size()-1).getValue());
		priceHist.setCurrPriceFromTime(priceTicks.get(priceTicks.size()-1).getTime());
		
		int maxIndex = 0;
		Tick maxTick = priceTicks.get(0);
		int minIndex = 0;
		Tick minTick = priceTicks.get(0);
		double totalPrice=0;
		for(Integer index=0 ; index<priceTicks.size() ; ++index){
			Tick priceTick = priceTicks.get(index);
			double price = priceTick.getValue();
			if(price<0)
				continue;
			if(price>maxTick.getValue() || Math.abs(price-maxTick.getValue())<EPSILON){
				maxTick = priceTick;
				maxIndex = index;
			}
			if(price<minTick.getValue() || Math.abs(price-minTick.getValue())<EPSILON){
				minTick = priceTick;
				minIndex = index;
			}
			totalPrice += price;
		}
		priceHist.setMaxPrice(maxTick.getValue());
		priceHist.setMaxPriceFromTime(maxTick.getTime());
		if(maxIndex+1<priceTicks.size())
			priceHist.setMaxPriceToTime(priceTicks.get(maxIndex+1).getTime());
		else
			priceHist.setMaxPriceToTime(-1);//putting special value as currPrice = maxPrice
		priceHist.setMinPrice(minTick.getValue());
		priceHist.setMinPriceFromTime(minTick.getTime());
		if(minIndex+1<priceTicks.size())
			priceHist.setMinPriceToTime(priceTicks.get(minIndex+1).getTime());
		else
			priceHist.setMinPriceToTime(-1);//putting special value as currPrice = minPrice
		priceHist.setAveragePrice(totalPrice/priceTicks.size());
	}
	
	//TODO Ask bhaiya to check its logic
	//time thing would create a prob if b/w T/5 and 2T/5 there is no movement
	//but b/w 2T/5 and 3T/5 say there are 4 movements
	public void processSellRankHistory(List<Tick> sellRankHistory){
		cleanUpTicks(sellRankHistory);
		if(MAX_SELL_RANK_HISTORY_SIZE==-1)
			return;
		if(sellRankHistory.size()<=MAX_SELL_RANK_HISTORY_SIZE)
			return;
		List<Tick> newSellRankHistory = new ArrayList<Tick>();
		long start = sellRankHistory.get(0).getTime();
		long diff = (sellRankHistory.get(sellRankHistory.size()-1).getTime() - start)/MAX_SELL_RANK_HISTORY_SIZE;
		int interval = 1;
		for(int index = 0;index < sellRankHistory.size()-1;index++){
			long time = start + (interval)*diff;
			if(sellRankHistory.get(index).getTime() <= time){
				if(sellRankHistory.get(index+1).getTime() > time){
					newSellRankHistory.add(sellRankHistory.get(index));
					++interval;
				}
			}
		}
		newSellRankHistory.add(sellRankHistory.get(sellRankHistory.size()-1));
		sellRankHistory.clear();
		sellRankHistory.addAll(newSellRankHistory);
	}
	
	public void processReviewHistory(List<Review> reviewHistory){
		cleanUpReviewHistory(reviewHistory);
		if(MAX_REVIEW_HISTORY_SIZE==-1)
			return;
		if(reviewHistory.size()<=MAX_REVIEW_HISTORY_SIZE)
			return;
		List<Review> newReviewHistory = new ArrayList<Review>();
		long start = reviewHistory.get(0).getTime();
		long diff = (reviewHistory.get(reviewHistory.size()-1).getTime() - start)/MAX_REVIEW_HISTORY_SIZE;
		int interval = 1;
		for(int index = 0;index < reviewHistory.size()-1;index++){
			long time = start + (interval)*diff;
			if(reviewHistory.get(index).getTime() <= time){
				if(reviewHistory.get(index+1).getTime() > time){
					newReviewHistory.add(reviewHistory.get(index));
					++interval;
				}
			}
		}
		newReviewHistory.add(reviewHistory.get(reviewHistory.size()-1));
		reviewHistory.clear();
		reviewHistory.addAll(newReviewHistory);
	}
	
	/**
	 * set price history and review history objects inside product
	 * @param product
	 */
	public void processNewProduct(Product product){
		//cleaning up the latest entry of review history
		int reviewHistorySize = product.getReviewHistorySize();
		if(reviewHistorySize>0){
			Review latestReview = product.getReviewHistory().get(reviewHistorySize-1);
			if(latestReview.getReviewRating()<=EPSILON)
				product.getReviewHistory().remove(reviewHistorySize-1);
			else {
				if(latestReview.getNumReviews()<0)
					latestReview.setNumReviews(0);
			}
		}
		
		//cleaning up the latest entry of price history
		int priceHistorySize = product.getPriceHistory().getPriceTicksSize();
		if(priceHistorySize>0){
			Tick latestPrice = product.getPriceHistory().getPriceTicks().get(priceHistorySize-1);
			if(latestPrice.getValue()<=EPSILON)
				product.getPriceHistory().getPriceTicks().remove(priceHistorySize-1);
			else{//processing price history
				product.getPriceHistory().setCurrPrice(latestPrice.getValue());
				product.getPriceHistory().setCurrPriceFromTime(latestPrice.getTime());
				product.getPriceHistory().setMinPrice(latestPrice.getValue());
				product.getPriceHistory().setMinPriceFromTime(latestPrice.getTime());
				product.getPriceHistory().setMinPriceToTime(-1);
				product.getPriceHistory().setMaxPrice(latestPrice.getValue());
				product.getPriceHistory().setMaxPriceFromTime(latestPrice.getTime());
				product.getPriceHistory().setMaxPriceToTime(-1);
				product.getPriceHistory().setAveragePrice(latestPrice.getValue());
			}
		}
	}
	
	/**
	 * update price history of this product with new min, max, avg value etc
	 * @param product
	 */
	public void processUpdatedProduct(Product product){
		if(ConfigParms.getInstance().isWebsiteMode()){
			//cleaning up the latest entry of review history
			int reviewHistorySize = product.getReviewHistorySize();
			if(reviewHistorySize>0){
				Review latestReview = product.getReviewHistory().get(reviewHistorySize-1);
				if(latestReview.getReviewRating()<=EPSILON)
					product.getReviewHistory().remove(reviewHistorySize-1);
				else {
					if(latestReview.getNumReviews()<0)
						latestReview.setNumReviews(0);
					if(reviewHistorySize>1){
						Review lastReview = product.getReviewHistory().get(reviewHistorySize-2);
						if(latestReview.getNumReviews()<=0)
							latestReview.setNumReviews(lastReview.getNumReviews());
						if(Math.abs(lastReview.getReviewRating() - latestReview.getReviewRating())<EPSILON && 
								lastReview.getNumReviews() == latestReview.getNumReviews())
							product.getReviewHistory().remove(reviewHistorySize-1);
					}
				}
			}
		}

		//cleaning up the last 2 entries if they were below zero
		int priceHistorySize = product.getPriceHistory().getPriceTicksSize();
		boolean removed = false;
		if(priceHistorySize>0){
			Tick latestPrice = product.getPriceHistory().getPriceTicks().get(priceHistorySize-1);
			if(latestPrice.getValue()<=EPSILON){
				product.getPriceHistory().getPriceTicks().remove(priceHistorySize-1);
				removed = true;
			}
			else{
				if(priceHistorySize>1){
					Tick lastPrice = product.getPriceHistory().getPriceTicks().get(priceHistorySize-2);
					if(Math.abs(lastPrice.getValue() - latestPrice.getValue())<EPSILON){
						product.getPriceHistory().getPriceTicks().remove(priceHistorySize-1);
						removed = true;
					}
				}
			}

			//processing price history
			if(!removed){
				product.getPriceHistory().setCurrPrice(latestPrice.getValue());
				product.getPriceHistory().setCurrPriceFromTime(latestPrice.getTime());

				if(product.getPriceHistory().getMinPrice()>=latestPrice.getValue() || priceHistorySize<=1){
					product.getPriceHistory().setMinPrice(latestPrice.getValue());
					product.getPriceHistory().setMinPriceFromTime(latestPrice.getTime());
					product.getPriceHistory().setMinPriceToTime(-1);
				} else {
					if(product.getPriceHistory().getMinPriceToTime() == -1)
						product.getPriceHistory().setMinPriceToTime(latestPrice.getTime());
				}

				if(latestPrice.getValue()>=product.getPriceHistory().getMaxPrice()){
					product.getPriceHistory().setMaxPrice(latestPrice.getValue());
					product.getPriceHistory().setMaxPriceFromTime(latestPrice.getTime());
					product.getPriceHistory().setMaxPriceToTime(-1);
				} else {
					if(product.getPriceHistory().getMaxPriceToTime() == -1)
						product.getPriceHistory().setMaxPriceToTime(latestPrice.getTime());
				}
				if(latestPrice.getValue()>=EPSILON){//make sure new price is good
					double totalPrice = (product.getPriceHistory().getAveragePrice()*(product.getPriceHistory().getPriceTicksSize()-1));//total before the update
					totalPrice += latestPrice.getValue();
					product.getPriceHistory().setAveragePrice(totalPrice/product.getPriceHistory().getPriceTicksSize());
				}

			}//if(!removed) ends...
		}//if(priceHistorySize>0){ ends...
	}
}
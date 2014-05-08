package thrift.servers;

import java.util.Comparator;
import java.util.List;

import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;

public class ProductComparators {
	
	/*
	 * Sorts according to Current Price in Ascending Order
	 */
	public static class ProductComparatorByPrice implements Comparator<Product>{
		@Override
		public int compare(Product o1, Product o2) {
			//TODO this should never happen
			if(o1==null && o2==null)
				return 0;
			else if(o1==null)
				return 1;
			else if(o2==null)
				return -1;
			
			if(o1.getPriceHistory().currPrice > o2.getPriceHistory().currPrice)
				return 1;
			else if(o1.getPriceHistory().currPrice < o2.getPriceHistory().currPrice)
				return -1;
			return 0;
		}
	}
	
	/*
	 * Sorts according to Review in Descending Order
	 */
	public static class ProductComparatorByReview implements Comparator<Product>{
		@Override
		public int compare(Product o1, Product o2) {
			//TODO this should never happen
			if(o1==null && o2==null)
				return 0;
			else if(o1==null)
				return 1;
			else if(o2==null)
				return -1;
			
			List<Review> o1ReviewHistory = o1.getReviewHistory();
			List<Review> o2ReviewHistory = o2.getReviewHistory();
			
			if(o1ReviewHistory.size()==0 && o2ReviewHistory.size()==0)
				return 0;
			else if(o1ReviewHistory.size()==0)
				return 1;
			else if(o2ReviewHistory.size()==0)
				return -1;
			
			if(o1ReviewHistory.get(o1ReviewHistory.size()-1).getReviewRating() > o2ReviewHistory.get(o2ReviewHistory.size()-1).getReviewRating())
				return -1;
			else if (o1ReviewHistory.get(o1ReviewHistory.size()-1).getReviewRating() < o2ReviewHistory.get(o2ReviewHistory.size()-1).getReviewRating())
				return 1;
			return 0;
		}
	}
	
	/*
	 * Sorts according to Sell Rank in Ascending Order
	 */
	public static class ProductComparatorBySellRank implements Comparator<Product>{
		@Override
		public int compare(Product o1, Product o2) {
			//TODO this should never happen
			if(o1==null && o2==null)
				return 0;
			else if(o1==null)
				return 1;
			else if(o2==null)
				return -1;
			
			List<Tick> o1SellRankHistory = o1.getSellRankHistory();
			List<Tick> o2SellRankHistory = o2.getSellRankHistory();
			
			if(o1SellRankHistory.size()==0 && o2SellRankHistory.size()==0)
				return 0;
			else if(o1SellRankHistory.size()==0)
				return 1;
			else if(o2SellRankHistory.size()==0)
				return -1;
			
			if(o1SellRankHistory.get(o1SellRankHistory.size()-1).getValue() > o2SellRankHistory.get(o2SellRankHistory.size()-1).getValue())
				return 1;
			else if (o1SellRankHistory.get(o1SellRankHistory.size()-1).getValue() < o2SellRankHistory.get(o2SellRankHistory.size()-1).getValue())
				return -1;
			return 0;
		}
	}
	
	/*
	 * Sorts according to Time in Ascending Order
	 */
	public static class ProductComparatorByTime implements Comparator<Tick>{
		@Override
		public int compare(Tick priceTick1, Tick priceTick2)
		{
			if( priceTick1.getTime() < priceTick2.getTime() )
				return -1;
			if( priceTick1.getTime() > priceTick2.getTime() )
				return 1;			
			return 0;
		}
	}
	
}

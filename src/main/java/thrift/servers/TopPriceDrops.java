package thrift.servers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import thrift.genereated.retailer.PriceDrop;
import thrift.genereated.retailer.PriceDropByCategory;

import entities.PriceMovementSummary;

public class TopPriceDrops {
 Map<Integer, Set<PriceMovementSummary>> categoryProducts = new HashMap<Integer, Set<PriceMovementSummary>>();
 
 /*if this is terminal category then its value will only one entry inside it
  *catetoryid		
  *c1				c1	list<priceDrops>

  *if this is a parent category then its value will have one entry per its child. 
  *catetoryid		
  *p1				c1	list<priceDrops>
  *					c2	list<priceDrops>
  */
 
 //to send to client, list<pricedrop> only has 10 (configurable) products
 //   this-category, child-category, list
 Map<Integer, Map<Integer, List<PriceDrop>>> categoryIdToPriceDropByChildCategory = new HashMap<Integer, Map<Integer, List<PriceDrop>>>();
 Map<Integer, List<PriceDrop>> emptyPriceDrops = new HashMap<Integer, List<PriceDrop>>();
 public void reset(){
	 categoryProducts.clear();
	 categoryIdToPriceDropByChildCategory.clear();
 }

 /*
  * For terminal category, childCategoryId = categoryId
  */
 public void addProduct(Integer categoryId, Integer childCategoryId, PriceMovementSummary p){
	 //first we add this new product to this category's map which will be used by its parent to update its list
	 Set<PriceMovementSummary> products;
	 if((products=categoryProducts.get(categoryId))==null){
		products = new TreeSet<PriceMovementSummary>();
		categoryProducts.put(categoryId, products);
	 }
	 products.add(p);
	 
	 /*Now we refresh the childCategory product list for this category
	  * get first X products for childCategory and update parent's cache with it.
	  * Create list to send to php since it doesn't support set data structure
	  * 
	  */
	 ArrayList<PriceDrop> productsList = new ArrayList<PriceDrop>();
	 Set<PriceMovementSummary> childCategoryProducts = categoryProducts.get(childCategoryId);
//	 Iterator<PriceMovementSummary> itr = childCategoryProducts.iterator();
//	 int count=0;
//	 while(itr.hasNext() && count<10){
//		 PriceMovementSummary m = itr.next();
//		 PriceDrop priceDrop = new PriceDrop(categoryId, m.getProductId(), 
//				null,
//				m.getName(),
//				null,
//				 m.getLatestPrice(),
//				 m.getLastPrice(),
//				 m.getAveragePrice(),
//				 m.getMaxPrice(),
//				 Byte.valueOf("-1")//reviewStars
//				 );
//		 productsList.add(priceDrop);
//		 count++;
//	 }
	 Map<Integer,List<PriceDrop>> priceDropsByChildCategory = categoryIdToPriceDropByChildCategory.get(categoryId);
	 if(priceDropsByChildCategory==null){
		 priceDropsByChildCategory = new HashMap<Integer, List<PriceDrop>>();
		 categoryIdToPriceDropByChildCategory.put(categoryId, priceDropsByChildCategory);
	 }
	 priceDropsByChildCategory.put(childCategoryId, productsList);
 }
 
 public Set<PriceMovementSummary> getProducts(Integer categoryId){
	 return categoryProducts.get(categoryId);
 }

 /*
  * returns prices drops for given categoryid, 
  *  in the format of map where key is child categoryids and value is drops for that cateogry
  */
 public Map<Integer, List<PriceDrop>> getPriceDropsForCategory(Integer categoryId){
	 Map<Integer, List<PriceDrop>> result = categoryIdToPriceDropByChildCategory.get(categoryId);
	 if(result==null){
		 return emptyPriceDrops;
	 }
	 else 
		 return result;
 }
 
 public static void main(String args[]){
//	 TopPriceDrops p = new TopPriceDrops();
//		PriceMovementSummary pms1 = new PriceMovementSummary();
//		pms1.setCalenderDay(new Date());
//		pms1.setProductId(5678);
//		pms1.setMaxPriceTime(new Date(System.currentTimeMillis()));
//		pms1.setMaxPrice(700);
//		pms1.setLastPriceChangeTime(new Date(System.currentTimeMillis()));
//		pms1.setLastPrice(200);
//		pms1.setLatestPrice(600);
//		pms1.setLatestPriceTime(new Date(System.currentTimeMillis()));
//		pms1.setAveragePrice(480);
//		p.addProduct(1, 2, pms1);
//		
//		PriceMovementSummary pms2 = new PriceMovementSummary();
//		pms2.setCalenderDay(new Date());
//		pms2.setProductId(5679);
//		pms2.setMaxPriceTime(new Date(System.currentTimeMillis()));
//		pms2.setMaxPrice(700);
//		pms2.setLastPriceChangeTime(new Date(System.currentTimeMillis()));
//		pms2.setLastPrice(200);
//		pms2.setLatestPrice(600);
//		pms2.setLatestPriceTime(new Date(System.currentTimeMillis()));
//		pms2.setAveragePrice(480);
//		p.addProduct(1,  2, pms2);
//	 System.out.println(p.getProducts(1));

 }
}
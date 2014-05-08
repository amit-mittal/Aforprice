/**
 * 
 */
package thrift.servers;

import static entities.Retailer.TESTRETAILER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.ProductSummaryBuilder;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import entities.Retailer;

/**
 * @author Ashish
 *
 */
public class ProductCacheImplUpdaterTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductCacheImplUpdaterTest.class);
	private static long currentTime = System.currentTimeMillis();
	private static List<Retailer> myRetailers;
	int expNoOfProductsInRetailer, expNoOfProductsInCategory, expNoOfCategoriesForProduct, expPriceTicksSize;
	ProductCacheImpl cache;
	ProductsDAO dao = new ProductsDAO();
	
	public void setUp(){
		try {
			ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
			dao.regTestRemoveProducts(TESTRETAILER.getId());
			myRetailers = new ArrayList<Retailer>();
			myRetailers.add(TESTRETAILER);
			ProductData.getInstance().resetUnitTest();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testUpdateProducts() throws SQLException {
		//ADD ONE PRODUCT
		cache = new ProductCacheImpl(myRetailers);
		Set<Integer> updatedCategoryIds = new HashSet<Integer>();
		logger.info("====== Test - AddProduct - Adding Very First Product");
		ProductSummary product= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(10000).productId(1).price(1.0).downloadTime(new Date(currentTime)).reviewRating(2.0).numReviews(13).build();
		List<ProductSummary> updates = new ArrayList<ProductSummary>();
		updates.add(product);
		cache.updateProducts(updates);
		updatedCategoryIds.add(product.getCategoryId());
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCache(product);
		//assert that all maps are referring to same object - one off check
		_assertTrue(ProductData.getInstance().getProduct(product.getId())==ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId()).get(0));
		
		//UPDATE PRODUCT
		logger.info("====== Test - UpdateProduct - change price");
		product.setPrice(1.1);
		cache.updateProducts(updates);
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		expPriceTicksSize=2;
		assertCache(product);

		logger.info("====== Test - UpdateProduct - change name/imageurl/model, same price");
		product.setName("product1-nameupdated");
		product.setImageUrl("www.image1-updated.com");
		product.setModel("model1-updated");
		cache.updateProducts(updates);
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		assertCache(product);

		
		//ADD SECOND PRODUCT - SAME CATEGORY
		logger.info("====== Test - AddProduct - second product in same category");
		currentTime++;
		ProductSummary product2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product2", "http://www.testretailer.com/16419433", "www.image2.com", "model2").
				categoryId(10000).productId(2).price(1.2).downloadTime(new Date(currentTime)).reviewRating(3.5).numReviews(-1).build();
		updates.clear();
		updates.add(product2);
		cache.updateProducts(updates);
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		expNoOfProductsInRetailer=2; expNoOfProductsInCategory=2; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCache(product2);
		
		
		logger.info("====== Test - UpdateProduct - second product again in different category");
		//ADD SECOND PROUDCT AGAIN - DIFFERENT CATEGORY (scenario: a product found under another category which does not exists)
		currentTime++;
		ProductSummary product2_2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product2", "http://www.testretailer.com/16419433", "www.image2.com", "model2").
				categoryId(10001).productId(2).price(1.2).downloadTime(new Date(currentTime)).reviewRating(-1.0).numReviews(-2).build();
		updates.clear();
		updates.add(product2_2);
		cache.updateProducts(updates);
		updatedCategoryIds.clear();
		updatedCategoryIds.add(product2_2.getCategoryId());
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		expNoOfProductsInRetailer=2; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=2;expPriceTicksSize=1;
		assertCache(product2_2);
		//ADD FIRST PRODUCT IN SECOND CATEGORY (scenario: a product found under another category which already exists)
		logger.info("====== Test - AddProduct - Add first product in second category");
		product.setCategoryId(10001);
		updates.clear();updates.add(product);cache.updateProducts(updates);
		ProductData.getInstance().processUpdatedCategories(updatedCategoryIds);
		expNoOfProductsInRetailer=2; expNoOfProductsInCategory=2; expNoOfCategoriesForProduct=2;expPriceTicksSize=2;
		assertCache(product);
		
		//Making the PRODUCT inActive to check RemoveProduct function
		logger.info("====== Test - RemoveProduct - Making product inactive");
		product.setActive(false);
		updates.clear();
		updates.add(product);
		cache.updateProducts(updates);
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=0;expPriceTicksSize=0;
		assertCache(product);
	}

	//TODO: Enable tests
	//@Test
	/*public void testProductUpdater() throws DAOException, InterruptedException, SQLException{
		//insert a new product
		logger.info("====== Test - ProductUpdater - add a new product");
		Category category = new CategoryBuilder(TESTRETAILER.getId(), null, "category1", "cat1.com").catType(CategoryType.TERMINAL)
				.build();
		CategoryDAO recorder = new CategoryDAO();
		recorder.recordCategory(category);

		ProductSummary product= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(category.getCategoryId()).price(1.0).downloadTime(new Date(currentTime)).creationTime(new Date(currentTime)).build();
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		products.add(product);
		HashMap<ProductSummary, ProductSummary> existingProducts = new HashMap<ProductSummary, ProductSummary>();
		dao.insertUpdateProductSummary(products, existingProducts);
		//start cache service and confirm one product in cache
		//ProductCacheImpl.overrideTimeIntervalForRegTest(1);
		cache = new ProductCacheImpl(myRetailers);
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCache(product);
		
		//update the product and confirm that update is received
		logger.info("====== Test - ProductUpdater - add new price for an existing product");
		currentTime=currentTime+1000;
		ProductSummary product2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(category.getCategoryId()).price(1.1).downloadTime(new Date(currentTime)).creationTime(new Date(currentTime)).build();
		products.clear();products.add(product2);
		dao.insertUpdateProductSummary(products, existingProducts);
		ProductUpdater updater = new ProductUpdater(cache, new Timestamp(currentTime-INTERVAL));
		updater.doWorkRegTest();
		expNoOfProductsInRetailer=1; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=1;expPriceTicksSize=2;
		assertCache(product2);
	}*/
	
	/**
	 * assert maps in ProductCacheImpl
	 * @param product	 
	 *  
	 */
	public void assertCache(ProductSummary product) {
		List<Product> productsInCategoryMap = ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId());
		_assertEquals(expNoOfProductsInCategory, productsInCategoryMap.size());
		_assertEquals(expNoOfProductsInCategory, ProductData.getInstance().getCategoryProductsByPriceMap().get(product.getCategoryId()).size());
//		_assertEquals(expNoOfProductsInCategory, ProductData.getInstance().getCategoryProductsByReviewMap().get(product.getCategoryId()).size());
		
		checkIfProductListSortedByPrice(ProductData.getInstance().getCategoryProductsByPriceMap().get(product.getCategoryId()));
//		checkIfProductListSortedByReview(ProductData.getInstance().getCategoryProductsByReviewMap().get(product.getCategoryId()));
		
		if(product.isActive()){
			_assertEquals(expNoOfCategoriesForProduct, ProductData.getInstance().getProductIdCategoryMap().get(product.getId()).size());
			_assertTrue(ProductData.getInstance().getProduct(product.getId())!=null);
			_assertTrue(ProductData.getInstance().getProductIdCategoryMap().get(product.getId()).contains(product.getCategoryId()));
			
			Product actualProduct = ProductData.getInstance().getProduct(product.getId());
			_assertEquals(product.getName(), actualProduct.getName());
			_assertEquals(product.getModel(), actualProduct.getModelNo());
			_assertEquals(product.getUrl(), actualProduct.getUrl());
			_assertEquals(product.getImageUrl(), actualProduct.getImageUrl());
			_assertEquals(product.getName(), actualProduct.getName());
			List<Tick> actualPriceTicks = actualProduct.getPriceHistory().getPriceTicks();
			_assertEquals(expPriceTicksSize, actualPriceTicks.size());
			_assertTime(product.getDownloadTime().getTime(), actualPriceTicks.get(expPriceTicksSize-1).getTime());
			_assertEquals(product.getPrice(), actualPriceTicks.get(expPriceTicksSize-1).getValue());
			_assertTime(product.getDownloadTime().getTime(), actualProduct.getPriceHistory().getCurrPriceFromTime());
			_assertEquals(product.getPrice(), actualProduct.getPriceHistory().getCurrPrice());
			checkPriceTickList(actualProduct.getPriceHistory().getPriceTicks());
			checkReviewList(actualProduct.getReviewHistory());
			//checkSellRankList(actualProduct.getSellRankHistory());
			//TODO have to test maxPrice and minPrice and time also
			//TODO also have not tested sell rank and review history data
		}
		else{
			_assertTrue(ProductData.getInstance().getProduct(product.getId())==null);
			_assertTrue(ProductData.getInstance().getProductIdCategoryMap().get(product.getId())==null);
			if(ProductData.getInstance().getCategoryProductsMap().containsKey(product.getCategoryId())){
				List<Product> productList=ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId());
				_assertTrue(productList.contains(product)==false);
			}
		}
	}
	
	public void checkPriceTickList(List<Tick> ticks){
		logger.info("Asserting if the price tick list is sorted and sanitized.");
		long prevTime = 0;
		for(Tick tick : ticks){
			_assertTrue(tick.getTime()>=prevTime);
			_assertTrue(tick.getValue()>=0);
			prevTime = tick.getTime();
		}
	}
	
	public void checkReviewList(List<Review> reviews){
		logger.info("Asserting if the review list is sorted and sanitized.");
		long prevTime = 0;
		for(Review review : reviews){
			logger.info(review);
			_assertTrue(review.getTime()>=prevTime);
			_assertTrue(review.getReviewRating()>=0);
			_assertTrue(review.getNumReviews()>=0);
			prevTime = review.getTime();
		}
	}
	
	public void checkSellRankList(List<Tick> ticks){
		logger.info("Asserting if the sell rank list is sorted and sanitized.");
		long prevTime = 0;
		for(Tick tick : ticks){
			_assertTrue(tick.getTime()>=prevTime);
			_assertTrue(tick.getValue()>=0);
			prevTime = tick.getTime();
		}
	}
	
	public void checkPriceHistory(PriceHistory expected, PriceHistory actual){
		logger.info("Asserting if the Price History is correct");
		_assertEquals(expected.getCurrPrice(), actual.getCurrPrice());
		_assertEquals(expected.getMaxPrice(), actual.getMaxPrice());
		_assertEquals(expected.getMinPrice(), actual.getMinPrice());
		_assertEquals(expected.getCurrPriceFromTime(), actual.getCurrPriceFromTime());
		_assertEquals(expected.getMaxPriceFromTime(), actual.getMaxPriceFromTime());
		_assertEquals(expected.getMaxPriceToTime(), actual.getMaxPriceToTime());
		_assertEquals(expected.getMinPriceFromTime(), actual.getMinPriceFromTime());
		_assertEquals(expected.getMinPriceToTime(), actual.getMinPriceToTime());
	}
	
	public void checkIfProductListSortedByPrice(List<Product> products){
		logger.info("Asserting if the product list is sorted by price.");
		double prevPrice = 0.00;
		for(Product prod : products){
			_assertTrue(!(prod.getPriceHistory().getCurrPrice()<prevPrice));
			prevPrice = prod.getPriceHistory().getCurrPrice();
		}
	}
	
	public void checkIfProductListSortedByReview(List<Product> products){
		logger.info("Asserting if the product list is sorted by reviews.");
		double prevReview = 1000000.00;//Very large value here
		for(Product prod : products){
			List<Review> reviewHistory = prod.getReviewHistory();
			if(reviewHistory.size()>0){
				_assertTrue(!(reviewHistory.get(reviewHistory.size()-1).getReviewRating()>prevReview));
				prevReview = reviewHistory.get(reviewHistory.size()-1).getReviewRating();
			}
		}
	}
	/*
	public void checkIfProductListSortedBySellRank(List<Product> products){
		logger.info("Asserting if the product list is sorted by sell rank.");
		double prevSellRank = 0;
		for(Product prod : products){
			List<Tick> sellRankHistory = prod.getSellRankHistory();
			if(sellRankHistory.size()>0){
				_assertTrue(!(sellRankHistory.get(sellRankHistory.size()-1).value<prevSellRank));
				prevSellRank = sellRankHistory.get(sellRankHistory.size()-1).value;
			}
		}
	}
	*/
}

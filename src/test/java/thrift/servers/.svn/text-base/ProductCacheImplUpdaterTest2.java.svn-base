package thrift.servers;

import static entities.Retailer.TESTRETAILER;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.build.ProductSummaryBuilder;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import entities.Retailer;

/**
 * @author Amit
 *
 */
public class ProductCacheImplUpdaterTest2 extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductCacheImplUpdaterTest2.class);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asserting if the updated product's categories
	 * get sorted or not
	 * Only testing PriceMap
	 * @throws SQLException
	 */
	@Test
	public void testSortProducts() throws SQLException {
		//ADD ONE PRODUCT
		cache = new ProductCacheImpl(myRetailers);
		List<ProductSummary> updates = new ArrayList<ProductSummary>();
		
		logger.info("====== TEST - ADDING PRODUCTS - SAME CATEGORY ID");
		ProductSummary product1= new ProductSummaryBuilder(TESTRETAILER.getId(), "product1", "http://www.testretailer.com/16419432", "www.image1.com", "model1").
				categoryId(10000).productId(1).price(4.0).downloadTime(new Date(currentTime)).build();
		ProductSummary product2= new ProductSummaryBuilder(TESTRETAILER.getId(), "product2", "http://www.testretailer.com/16419432", "www.image2.com", "model2").
				categoryId(10000).productId(2).price(2.0).downloadTime(new Date(currentTime)).build();
		ProductSummary product3= new ProductSummaryBuilder(TESTRETAILER.getId(), "product3", "http://www.testretailer.com/16419432", "www.image3.com", "model3").
				categoryId(10000).productId(3).price(8.5).downloadTime(new Date(currentTime)).build();
		updates.add(product1);updates.add(product2);updates.add(product3);
		ProductUpdater updater = new ProductUpdater(cache, new Timestamp(currentTime));
		cache.updateProducts(updates);
		updater.processUpdatedCategories(updates);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=3; expNoOfCategoriesForProduct=1;expPriceTicksSize=1;
		assertCache(product1);
		assertCache(product2);
		assertCache(product3);
		
		logger.info("====== TEST - UPDATE PRODUCT - NEW CATEGORY ID");
		product1.setCategoryId(10001);
		product1.setPrice(12.0);
		updates.clear();
		updates.add(product1);
		cache.updateProducts(updates);
		updater.processUpdatedCategories(updates);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=1; expNoOfCategoriesForProduct=2;expPriceTicksSize=2;
		assertCache(product1);
		
		logger.info("====== TEST - UPDATE PRODUCT - OLD CATEGORY ID");
		product3.setCategoryId(10001);
		product3.setPrice(1.0);
		updates.clear();
		updates.add(product3);
		cache.updateProducts(updates);
		updater.processUpdatedCategories(updates);
		expNoOfProductsInRetailer=3; expNoOfProductsInCategory=2; expNoOfCategoriesForProduct=2;expPriceTicksSize=2;
		assertCache(product3);
	}
	
	/**
	 * assert maps in ProductCacheImpl
	 * @param product	 
	 */
	public void assertCache(ProductSummary product) {
		List<Product> productsInCategoryMap = ProductData.getInstance().getCategoryProductsMap().get(product.getCategoryId());
		_assertEquals(expNoOfProductsInCategory, productsInCategoryMap.size());
		
		//checking if map is sorted
		List<Integer> categoryList = ProductData.getInstance().getProductIdCategoryMap().get(product.getId());
		for(int categoryId : categoryList){
			logger.info("Asserting if the categoryId " + categoryId + " is sorted by price.");
			checkIfProductListSortedByPrice(ProductData.getInstance().getCategoryProductsByPriceMap().get(categoryId));
		}
		
		//asserting product
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
		}
	}
	
	public void checkIfProductListSortedByPrice(List<Product> products){
		double prevPrice = 0.00;
		for(Product prod : products){
			_assertTrue(!(prod.getPriceHistory().getCurrPrice()<prevPrice));
			prevPrice = prod.getPriceHistory().getCurrPrice();
		}
	}
}

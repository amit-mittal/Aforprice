package thrift.servers;

import static entities.Retailer.TESTRETAILER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.junit.Test;

import thrift.genereated.retailer.LookupIdx;
import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.ProductFilter;
import thrift.genereated.retailer.ProductFilterType;
import thrift.genereated.retailer.ProductList;
import thrift.genereated.retailer.Review;
import thrift.genereated.retailer.SortCriterion;
import thrift.genereated.retailer.Tick;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import db.dao.ProductsDAO;
import entities.Retailer;

/**
 * @author Amit
 * Test of THRIFT methods of class ProductCacheImpl
 */

public class ProductCacheImplThriftTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(ProductCacheImplThriftTest.class);
	private static long currentTime = System.currentTimeMillis();
	private static List<Retailer> myRetailers;
	private ProductsDAO prodDAO = new ProductsDAO();
	private ProductCacheImpl cache;
	private static final long TIME_DIFF = 60*60*1000L;
	
	public void setUp(){
		try {
			ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
			prodDAO.regTestRemoveProducts(TESTRETAILER.getId());
			myRetailers = new ArrayList<Retailer>();
			myRetailers.add(TESTRETAILER);
			ProductData.getInstance().resetUnitTest();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_GetProducts() throws TException{
		cache = new ProductCacheImpl(myRetailers);
		logger.info("======= TESTING GET PRODUCTS METHOD =======");
		logger.info("======= MAKING PRODUCT1 =======");
		Product prod1 = new Product(1, "prod1", "model1", "http://image1.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks1 = new ArrayList<Tick>();
		Tick priceTick1_1 = new Tick(currentTime+TIME_DIFF, 15.39);
		priceTicks1.add(priceTick1_1);
		prod1.getPriceHistory().setPriceTicks(priceTicks1);
		
		Review review1_1 = new Review(currentTime, 3, 234);
		prod1.getReviewHistory().add(review1_1);
		
		//Tick sellRankTick1_1 = new Tick(currentTime+TIME_DIFF, 200);
		//prod1.getSellRankHistory().add(sellRankTick1_1);
			
		logger.info("======= MAKING PRODUCT2 - SAME CATEGORY ID =======");
		Product prod2 = new Product(2, "prod2", "model2", "http://image2.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks2 = new ArrayList<Tick>();
		Tick priceTick2_1 = new Tick(currentTime+TIME_DIFF, 13.39);
		Tick priceTick2_2 = new Tick(currentTime+TIME_DIFF/2, 11.39);
		priceTicks2.add(priceTick2_1);
		priceTicks2.add(priceTick2_2);
		prod2.getPriceHistory().setPriceTicks(priceTicks2);
		
		Review review2_1 = new Review(currentTime, 3.9, 14);
		Review review2_2 = new Review(currentTime-TIME_DIFF, 3.3, 5);
		prod2.getReviewHistory().add(review2_1);
		prod2.getReviewHistory().add(review2_2);
		
		/*Tick sellRankTick2_1 = new Tick(currentTime, 20);
		Tick sellRankTick2_2 = new Tick(currentTime-TIME_DIFF, 150);
		prod2.getSellRankHistory().add(sellRankTick2_1);
		prod2.getSellRankHistory().add(sellRankTick2_2);*/
		
		logger.info("======= MAKING PRODUCT3 - SAME CATEGORY ID =======");
		Product prod3 = new Product(3, "prod3", "model3", "http://image3.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks3 = new ArrayList<Tick>();
		Tick priceTick3_1 = new Tick(currentTime+TIME_DIFF, 150.39);
		priceTicks3.add(priceTick3_1);
		prod3.getPriceHistory().setPriceTicks(priceTicks3);
		
		Review review3_1 = new Review(currentTime, 4.3, 100);
		prod3.getReviewHistory().add(review3_1);
		
		//Tick sellRankTick3_1 = new Tick(currentTime, 120);
		//prod3.getSellRankHistory().add(sellRankTick3_1);
		
		logger.info("======= MAKING PRODUCT4 - DIFFERENT CATEGORY ID =======");
		Product prod4 = new Product(4, "prod4", "model4", "http://image4.jpg", "http://test.html", new PriceHistory(), new ArrayList<Tick>(), new ArrayList<Review>());
		
		List<Tick> priceTicks4 = new ArrayList<Tick>();
		Tick priceTick4_1 = new Tick(currentTime, 22.39);
		priceTicks4.add(priceTick4_1);
		prod4.getPriceHistory().setPriceTicks(priceTicks4);		
		//review is null but this retailer has reviews
		//sell rank is null and this retailer does not provide sell ranks
		
		logger.info("======= MAKING ALL THE HASHMAPS =======");
		//making productIdProductMap
		ProductData.getInstance().addProduct(TESTRETAILER, prod1);
		ProductData.getInstance().addProduct(TESTRETAILER, prod2);
		ProductData.getInstance().addProduct(TESTRETAILER, prod3);
		ProductData.getInstance().addProduct(TESTRETAILER, prod4);
		//making category map for categoryId: 1
		List<Product> categoryList1 = new ArrayList<Product>();
		categoryList1.add(prod1);
		categoryList1.add(prod2);
		categoryList1.add(prod3);
		ProductData.getInstance().getCategoryProductsMap().put(1, categoryList1);
		ProductData.getInstance().getCategoryProductsByPriceMap().put(1, new ArrayList<>(categoryList1));
		ProductData.getInstance().getCategoryProductsByReviewMap().put(1, new ArrayList<>(categoryList1));
		//making category map for categoryId: 2
		List<Product> categoryList2 = new ArrayList<Product>();
		categoryList2.add(prod4);
		ProductData.getInstance().getCategoryProductsMap().put(2, categoryList2);
		ProductData.getInstance().getCategoryProductsByPriceMap().put(2, new ArrayList<>(categoryList2));
		ProductData.getInstance().getCategoryProductsByReviewMap().put(2, new ArrayList<>(categoryList2));
		
		logger.info("======= FINALLY PROCESSING ALL THE PRODUCTS AND SORTING THE MAPS =======");
		cache.processProductHistoryData();		
		ProductData.getInstance().sortAllCategoryProductsMap();
		
		logger.info("======= GETTING PRODUCTS - IF NO FILTER - PRICE_ASC SORT CRITERION =======");
		List<ProductFilter> filters = new ArrayList<ProductFilter>();
		ProductList prodList = ProductData.getInstance().getProducts(1, new LookupIdx(0, 3), filters, SortCriterion.PRICE_ASC);
		List<Product> expectedProdList1 = new ArrayList<Product>();
		expectedProdList1.add(prod2);
		expectedProdList1.add(prod1);
		expectedProdList1.add(prod3);
		//TODO Currently not testing the filter maps
		ProductList expectedProductList1 = new ProductList(expectedProdList1, 3);
		assertProductList(expectedProductList1, prodList);
		
		logger.info("======= GETTING PRODUCTS - IF NO FILTER - PRICE_ASC SORT CRITERION - CHECKING LOOKUP ID =======");
		prodList = ProductData.getInstance().getProducts(1, new LookupIdx(1, 3), filters, SortCriterion.PRICE_ASC);
		expectedProdList1.clear();
		expectedProdList1.add(prod1);
		expectedProdList1.add(prod3);
		expectedProductList1.setProducts(expectedProdList1);
		expectedProductList1.setTotalCount(3);
		assertProductList(expectedProductList1, prodList);
		
		logger.info("======= GETTING PRODUCTS - IF 1 FILTER - PRICE_DESC SORT CRITERION =======");
		filters.clear();
		filters.add(new ProductFilter(ProductFilterType.PRICE, 10.00, 20.00));
		prodList = ProductData.getInstance().getProducts(1, new LookupIdx(0, 3), filters, SortCriterion.PRICE_DESC);
		expectedProdList1.clear();
		expectedProdList1.add(prod1);
		expectedProdList1.add(prod2);
		expectedProductList1.setProducts(expectedProdList1);
		expectedProductList1.setTotalCount(2);
		assertProductList(expectedProductList1, prodList);
		
		logger.info("======= GETTING PRODUCTS - IF 2 FILTERS - REVIEW SORT CRITERION =======");
		filters.clear();
		filters.add(new ProductFilter(ProductFilterType.PRICE, 14.00, 200.00));
		filters.add(new ProductFilter(ProductFilterType.REVIEW_RATINGS, 3.5, 5.0));
		prodList = ProductData.getInstance().getProducts(1, new LookupIdx(0, 3), filters, SortCriterion.REVIEW_RATINGS);
		expectedProdList1.clear();
		expectedProdList1.add(prod3);
		expectedProductList1.setProducts(expectedProdList1);
		expectedProductList1.setTotalCount(1);
		assertProductList(expectedProductList1, prodList);
		
		logger.info("======= GETTING PRODUCTS - EXISTANT SORT CRITERION BUT FIELD IS NULL =======");
		filters.clear();
		prodList = ProductData.getInstance().getProducts(2, new LookupIdx(0, 3), filters, SortCriterion.REVIEW_RATINGS);
		expectedProdList1.clear();
		expectedProdList1.add(prod4);
		expectedProductList1.setProducts(expectedProdList1);
		expectedProductList1.setTotalCount(1);
		assertProductList(expectedProductList1, prodList);
		/*
		logger.info("======= GETTING PRODUCTS - NON EXISTANT SORT CRITERION AND FIELD IS NULL =======");
		filters.clear();
		prodList = ProductData.getInstance().getProducts(2, new LookupIdx(0, 3), filters, SortCriterion.BEST_SELLERS);
		expectedProdList1.clear();
		expectedProductList1.setProducts(expectedProdList1);
		expectedProductList1.setTotalCount(0);
		assertProductList(expectedProductList1, prodList);
		*/
	}
	
	private void assertProductList(ProductList expected, ProductList actual){
		_assertEquals(expected.totalCount, actual.totalCount);
		for(int index=0;index<actual.getProductsSize();++index){
			assertProducts(expected.getProducts().get(index), actual.getProducts().get(index));
		}
	}
	
	private void assertProducts(Product expected, Product actual){
		logger.info("Asserting the 2 products");
		_assertEquals(expected.getProductId(), actual.getProductId());
		_assertEquals(expected.getName(), actual.getName());
		_assertEquals(expected.getModelNo(), actual.getModelNo());
		_assertEquals(expected.getImageUrl(), actual.getImageUrl());
		_assertEquals(expected.getUrl(), actual.getUrl());
	}
}

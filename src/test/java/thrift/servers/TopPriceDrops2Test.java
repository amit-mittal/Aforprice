package thrift.servers;

import static entities.Retailer.TESTRETAILER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uploader.util.CategoryType.PARENT;
import static uploader.util.CategoryType.TERMINAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import thrift.genereated.retailer.PriceHistory;
import thrift.genereated.retailer.Product;
import thrift.genereated.retailer.Tick;
import uploader.util.CategoryType;
import util.DateTimeUtils;
import util.build.CategoryBuilder;
import db.dao.CategoryDAO;
import db.dao.CategoryFactory;
import db.dao.DAOException;
import entities.Category;

public class TopPriceDrops2Test {
	private static final Logger logger = Logger.getLogger(TopPriceDrops2Test.class);
	CategoryDAO mockCategoryReader = mock(CategoryDAO.class);
	ProductCacheImpl mockProductCache = mock(ProductCacheImpl.class);
	CategoryCacheImpl mockCategoryCache = mock(CategoryCacheImpl.class);
	ProductData mockProductData = mock(ProductData.class);
	
	@Before
	public void setUp() throws Exception {
		setupMock();
	}
	
	@After
	public void destroy(){
		ProductData.resetInstanceUnitTest();
	}
	private void setupMock(){
		when(mockProductCache.getCategoryCache()).thenReturn(mockCategoryCache);
		ProductData.setInstanceUnitTest(mockProductData);
	}

	public thrift.genereated.retailer.Category getThriftCategory(Category cat){
	 return new thrift.genereated.retailer.Category(cat.getCategoryId(),cat.getName(), cat.getRetailerId(), 
			cat.getParentCategoryId(), cat.getParentName(), cat.getUrl(), cat.getType()==CategoryType.PARENT);
	}
	
	@Test
	public final void testProcessPriveMovementUpdates() {
		CategoryFactory.getInstance().setCategoryDAOOverride(mockCategoryReader);

		/*	category tree
		 * 			1
		 * 		11		12
		 * 111
		 */
		Category cat1 = new CategoryBuilder(TESTRETAILER.getId(), 0, "root", "cloth", "http://www.testretailer.com/").categoryId(1).catType(PARENT).build();
		thrift.genereated.retailer.Category thriftCat1 = getThriftCategory(cat1);
		when(mockProductCache.getCategory(cat1.getCategoryId())).thenReturn(thriftCat1);
		List<thrift.genereated.retailer.Category> rootCategories = new ArrayList<thrift.genereated.retailer.Category>();
		rootCategories.add(thriftCat1);
		when(mockProductCache.getRootCategories(TESTRETAILER.getId())).thenReturn(rootCategories);
		
		Category cat11 = new CategoryBuilder(TESTRETAILER.getId(), 1, "cloth", "men", "http://www.testretailer.com/").categoryId(11).catType(PARENT).build();
		thrift.genereated.retailer.Category thriftCat11 = getThriftCategory(cat11);
		when(mockProductCache.getCategory(cat11.getCategoryId())).thenReturn(thriftCat11);
		when(mockProductCache.getParentCategory(thriftCat11)).thenReturn(thriftCat1);
		
		Category cat111 = new CategoryBuilder(TESTRETAILER.getId(), 11, "men", "shirt", "http://www.testretailer.com/").categoryId(111).catType(TERMINAL).build();
		thrift.genereated.retailer.Category thriftCat111 = getThriftCategory(cat111);
		when(mockProductCache.getCategory(cat111.getCategoryId())).thenReturn(thriftCat111);
		when(mockProductCache.getParentCategory(thriftCat111)).thenReturn(thriftCat11);
		
		Category cat12 = new CategoryBuilder(TESTRETAILER.getId(), 1, "cloth", "women", "http://www.testretailer.com/").categoryId(12).catType(TERMINAL).build();
		thrift.genereated.retailer.Category thriftCat12 = getThriftCategory(cat12);
		when(mockProductCache.getCategory(cat12.getCategoryId())).thenReturn(thriftCat12);
		when(mockProductCache.getParentCategory(thriftCat12)).thenReturn(thriftCat1);

		//establish parent-child relationships
		List<thrift.genereated.retailer.Category> cat1ChildCats = new ArrayList<thrift.genereated.retailer.Category>();
		cat1ChildCats.add(thriftCat11);
		cat1ChildCats.add(thriftCat12);
		when(mockProductCache.getChildCategories(cat1.getCategoryId())).thenReturn(cat1ChildCats);
		List<thrift.genereated.retailer.Category> cat11ChildCats = new ArrayList<thrift.genereated.retailer.Category>();
		cat11ChildCats.add(thriftCat111);
		when(mockProductCache.getChildCategories(cat11.getCategoryId())).thenReturn(cat11ChildCats);
		
		
		List<Category> updates = new ArrayList<Category>();
		updates.add(cat1);
		updates.add(cat11);
		updates.add(cat111);
		updates.add(cat12);
		try {
			when(mockCategoryReader.getActiveCategoriesForRetailer(TESTRETAILER.getId())).thenReturn(updates);
		} catch (DAOException e) {
			fail(e.toString());
		}
		Date tonight = DateTimeUtils.getTodaysMidNight();
		
		TopPriceDrops2 priceDropsCache = new TopPriceDrops2(mockProductCache);
		logger.info("====== testPriceMovementInitUpdates: price movement initialization, two categories with two and one product respectively");
//		List<PriceMovementSummary> topDrops = new ArrayList<PriceMovementSummary>();
		//first category, first product
//		PriceMovementSummary drop1 = new PriceMovementSummaryBuilder().productId(80000001).lastPrice(10).latestPrice(9).
//				latestPriceTime(DateTimeUtils.getPrevMidNight(tonight).getTime()).build();
		List<Integer> categoryIds = new LinkedList<>();
		categoryIds.add(cat111.getCategoryId());
		
		List<Tick> priceTicks = new ArrayList<Tick>();
		priceTicks.add(new Tick(DateTimeUtils.getPrevMidNight(tonight).getTimeInMillis(), 10));
		priceTicks.add(new Tick(tonight.getTime(), 9));
		PriceHistory priceHistory1 = new PriceHistory();
		priceHistory1.setPriceTicks(priceTicks);
		Product product1 = new Product(80000001, null, null, null, null, priceHistory1, null, null);
		
//		topDrops.add(drop1);
		when(mockProductData.getCategoriesOfProduct(80000001)).thenReturn(categoryIds);
		when(ProductData.getInstance().getProduct(80000001)).thenReturn(product1);
		//first and second category, second product with larger drop
//		PriceMovementSummary drop2 = new PriceMovementSummaryBuilder().productId(80000002).lastPrice(20).latestPrice(10).
//				latestPriceTime(DateTimeUtils.getPrevNDaysMidNight(tonight, 2).getTime()).build();
		priceTicks = new ArrayList<Tick>();
		priceTicks.add(new Tick(DateTimeUtils.getPrevNDaysMidNight(tonight,2).getTimeInMillis(), 20));
		priceTicks.add(new Tick(tonight.getTime(), 10));
		PriceHistory priceHistory2 = new PriceHistory();
		priceHistory2.setPriceTicks(priceTicks);
		Product product2 = new Product(80000002, null, null, null, null, priceHistory2, null, null);
//		topDrops.add(drop2);
		List<Integer> categoryIds2 = new LinkedList<Integer>();
		categoryIds2.add(cat111.getCategoryId());
		categoryIds2.add(cat12.getCategoryId());
		when(mockProductData.getCategoriesOfProduct(80000002)).thenReturn(categoryIds2);
		when(ProductData.getInstance().getProduct(80000002)).thenReturn(product2);
		
		List<Product> newProducts = new ArrayList<Product>();
		newProducts.add(product1);
		newProducts.add(product2);
		priceDropsCache.addPriceDrops(newProducts);
		//cat111 has two drops
		Map<Integer, List<Product>> drops = priceDropsCache.getPriceDropsForCategory(cat111.getCategoryId());
		assertEquals(1, drops.size());
		List<Product> catDrops = drops.get(cat111.getCategoryId());
		assertEquals(2, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());
		assertEquals(product1.getProductId(), catDrops.get(1).getProductId());
		
		//cat11 will have same two drops
		drops = priceDropsCache.getPriceDropsForCategory(cat11.getCategoryId());
		assertEquals(1, drops.size());
		catDrops = drops.get(cat111.getCategoryId());
		assertEquals(2, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());
		assertEquals(product1.getProductId(), catDrops.get(1).getProductId());

		//cat12 will have one drop
		drops = priceDropsCache.getPriceDropsForCategory(cat12.getCategoryId());
		assertEquals(1, drops.size());
		catDrops = drops.get(cat12.getCategoryId());
		assertEquals(1, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());

		//cat1 will have same two drops
		drops = priceDropsCache.getPriceDropsForCategory(cat1.getCategoryId());
		assertEquals(2, drops.size());
		catDrops = drops.get(cat11.getCategoryId());
		assertEquals(2, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());
		assertEquals(product1.getProductId(), catDrops.get(1).getProductId());
		catDrops = drops.get(cat12.getCategoryId());
		assertEquals(1, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());
		
		//assert at retailer level
		drops = priceDropsCache.getPriceDropsForRetailer(TESTRETAILER.getId());
		assertEquals(1, drops.size());
		catDrops = drops.get(0);
		assertEquals(2, catDrops.size());
	}

	@Test
	public final void testRemovePriceDropsForUpdatedProducts() {
		//add to drops to the cache and remove them in different orders to make sure it works.
		
		CategoryFactory.getInstance().setCategoryDAOOverride(mockCategoryReader);
		Category cat111 = new CategoryBuilder(TESTRETAILER.getId(), 11, "men", "shirt", "http://www.testretailer.com/").categoryId(111).build();
		thrift.genereated.retailer.Category thriftCat111 = getThriftCategory(cat111);
		when(mockProductCache.getCategory(cat111.getCategoryId())).thenReturn(thriftCat111);

		List<Category> updates = new ArrayList<Category>();
		updates.add(cat111);
		try {
			when(mockCategoryReader.getActiveCategoriesForRetailer(TESTRETAILER.getId())).thenReturn(updates);
		} catch (DAOException e) {
			fail(e.toString());
		}
		Date tonight = DateTimeUtils.getTodaysMidNight();
		
		TopPriceDrops2 priceDropsCache = new TopPriceDrops2(mockProductCache);
		logger.info("====== testPriceMovementInitUpdates: price movement initialization, two categories with two and one product respectively");
		List<Integer> categoryIds = new LinkedList<>();
		categoryIds.add(cat111.getCategoryId());

		
		List<Tick> priceTicks = new ArrayList<Tick>();
		priceTicks.add(new Tick(DateTimeUtils.getPrevMidNight(tonight).getTimeInMillis(), 10));
		priceTicks.add(new Tick(tonight.getTime(), 9));
		PriceHistory priceHistory1 = new PriceHistory();
		priceHistory1.setPriceTicks(priceTicks);
		Product product1 = new Product(80000001, null, null, null, null, priceHistory1, null, null);
		
		when(mockProductData.getCategoriesOfProduct(80000001)).thenReturn(categoryIds);
		when(ProductData.getInstance().getProduct(80000001)).thenReturn(product1);
		//first and second category, second product with larger drop
		priceTicks = new ArrayList<Tick>();
		priceTicks.add(new Tick(DateTimeUtils.getPrevNDaysMidNight(tonight,2).getTimeInMillis(), 20));
		priceTicks.add(new Tick(tonight.getTime(), 10));
		PriceHistory priceHistory2 = new PriceHistory();
		priceHistory2.setPriceTicks(priceTicks);
		Product product2 = new Product(80000002, null, null, null, null, priceHistory2, null, null);
		List<Integer> categoryIds2 = new LinkedList<Integer>();
		categoryIds2.add(cat111.getCategoryId());
		when(mockProductData.getCategoriesOfProduct(80000002)).thenReturn(categoryIds2);
		when(ProductData.getInstance().getProduct(80000002)).thenReturn(product2);
		
		List<Product> newProducts = new ArrayList<Product>();
		newProducts.add(product1);
		newProducts.add(product2);
		priceDropsCache.addPriceDrops(newProducts);

		//cat111 has two drops
		Map<Integer, List<Product>> drops = priceDropsCache.getPriceDropsForCategory(cat111.getCategoryId());
		assertEquals(1, drops.size());
		List<Product> catDrops = drops.get(cat111.getCategoryId());
		assertEquals(2, catDrops.size());
		assertEquals(product2.getProductId(), catDrops.get(0).getProductId());
		assertEquals(product1.getProductId(), catDrops.get(1).getProductId());

		//drop2 (80000002) changed and has smaller drop compare to drop1
		//create a clone object, don't change drop2 otherwise its messes up TreeSet ordering which doesn't reflect the ordering
		//per new value and may not find the object when remove it called
		Product product2updated = product2.deepCopy();
		product2updated.getPriceHistory().addToPriceTicks(new Tick(tonight.getTime()+100, 9.5));
		List<Product> updatedProducts = new ArrayList<Product>();
		updatedProducts.add(product2updated);
		priceDropsCache.updatePriceDrops(updatedProducts, TESTRETAILER.getId());		
		drops = priceDropsCache.getPriceDropsForCategory(cat111.getCategoryId());
		catDrops = drops.get(cat111.getCategoryId());
		assertEquals(2, catDrops.size());
		assertEquals(product1.getProductId(), catDrops.get(0).getProductId());//order reversed now, 80000001
		assertEquals(product2.getProductId(), catDrops.get(1).getProductId());//80000002
	}

	//todo: weight by duration test
}

package thrift.servers;

import static entities.Retailer.TESTRETAILER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.MappedByteBuffer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.junit.Test;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.AnyVararg;
import org.mockito.internal.matchers.NotNull;
import static org.mockito.Matchers.*;
import thrift.genereated.retailer.PriceDrop;
import uploader.util.CategoryType;
import util.AbstractTest;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.DateTimeUtils;
import db.dao.CategoryDAO;
import db.dao.CategoryFactory;
import db.dao.DAOException;
import db.dao.PriceMovementSummaryDAO;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.PriceMovementSummary;
import entities.Retailer;

public class CategoryCacheImplTest extends AbstractTest{
	private static final Logger logger = Logger.getLogger(CategoryCacheImpl.class);
	private static Retailer[] myRetailers;
	int expNoOfCategoriesInRetailer, expNoOfCategoriesInParent;
	CategoryCacheImpl cache;
	PriceMovementSummaryDAO mockPriceDao = mock(PriceMovementSummaryDAO.class);
	CategoryDAO mockCategoryReader = mock(CategoryDAO.class);
	Date runDate = new Date();
	private static long currentTime = System.currentTimeMillis();
	private final static int INTERVAL = 10*60*1000;
	static{
		ConfigParms.MODE = RUNTIME_MODE.UNITTEST;
	}
	public void setUp(){
		try {
			CategoryDAO reader = new CategoryDAO();
			reader.regTestRemoveCategories(TESTRETAILER.getId());
			myRetailers = new Retailer[]{TESTRETAILER};
			setupMock();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	private void setupMock() throws SQLException{
		CategoryFactory.getInstance().setCategoryDAOOverride(mockCategoryReader);
		when(mockCategoryReader.getCategoryMaxModifiedTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
	//	PriceMovementSummaryDAO.setInstanceForRegTest(mockPriceDao);
	//	when(mockPriceDao.getPriceMovementMaxModifiedTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
	//	when(mockPriceDao.getPriceMovementRecorderDate()).thenReturn(runDate);
	}
	@Test
	public void testUpdateCategories() throws SQLException, DAOException, TException {
		List<Category> updates = new ArrayList<Category>();
		when(mockCategoryReader.getActiveCategoriesForRetailer(TESTRETAILER.getId())).thenReturn(updates);
		cache = new CategoryCacheImpl(myRetailers);	

		//ADD ONE CATEGORY
		logger.info("====== Test - AddCategory - Adding Very First Category");
		Category cat = new CategoryBuilder(TESTRETAILER.getId(), 1, "men", "TEST2", "http://www.testretailer.com/").categoryId(1).build();
		updates.add(cat);
		logger.info("To be added is: "+cat);
		cache.updateCategories(updates);
		expNoOfCategoriesInRetailer=1; expNoOfCategoriesInParent=1;
		assertCache(cat);
		//assert that all maps are referring to same object - one off check
		_assertTrue(cache.getCategoryIdCategoryMap().get(cat.getCategoryId())==cache.getRetailerAllCategoriesMap().get(cat.getRetailerId()).get(0));
		_assertTrue(cache.getCategoryIdCategoryMap().get(cat.getCategoryId())==cache.getParentChildCategoriesMap().get(cat.getParentCategoryId()).get(0));
		
		//UPDATE CATEGORY
		logger.info("====== Test - UpdateCategory - change url");
		cat.setUrl("http://www.NEWtestretailer.com/");
		cache.updateCategories(updates);
		assertCache(cat);

		logger.info("====== Test - UpdateCategory - change name and url");
		cat.setName("TEST2_updated");
		cat.setUrl("http://www.NEW_2_testretailer.com/");
		cache.updateCategories(updates);
		assertCache(cat);

		
		//ADD SECOND CATEGORY - Same retailer, New Parent, Different CategoryId
		logger.info("====== Test - AddCategory - first category in this parent");
		Category cat2= new CategoryBuilder(TESTRETAILER.getId(), 6, "women", "XYZ-TEST3", "http://www.test_3_retailer.com/").categoryId(2).build();
		updates.clear();
		updates.add(cat2);
		cache.updateCategories(updates);
		expNoOfCategoriesInParent = 1; expNoOfCategoriesInRetailer = 2;
		assertCache(cat2);	
		
		//ADD SECOND CATEGORY - Same retailer, Same Parent's second entry, Different CategoryId
		logger.info("====== Test - AddCategory - Add second category in second parent id"); 
		Category cat3=new CategoryBuilder(TESTRETAILER.getId(), 6, "women", "ABC-TEST3", "http://www.test_4_retailer.com/").categoryId(3).build();
		updates.clear();
		updates.add(cat3);
		cache.updateCategories(updates);
		expNoOfCategoriesInParent=2; expNoOfCategoriesInRetailer=3;
		assertCache(cat3);
		
		//confirm they are stored in sorted order
		List<thrift.genereated.retailer.Category> childs = cache.getChildCategories(6);
		_assertEquals("ABC-TEST3", childs.get(0).getCategoryName());		
		_assertEquals("XYZ-TEST3", childs.get(1).getCategoryName());
		
		//ADDED FIRST HOMEPAGE CATEGORY 
		logger.info("====== Test - AddCategory - Add first homepage category");
		Category cat5=new CategoryBuilder(TESTRETAILER.getId(), 0, "women", "TEST5", "http://www.test_5_retailer.com/").categoryId(5).build();
		updates.clear();
		updates.add(cat5);
		cache.updateCategories(updates);
		expNoOfCategoriesInParent=1; expNoOfCategoriesInRetailer=4;
		assertCache(cat5);
		
		//REMOVE CATEGORY - which is not homepage category
		logger.info("====== Test - RemoveCategory - Deleting category that is not HomepageCategory");
		cat3.setActive(false);
		updates.clear();
		updates.add(cat3);
		cache.updateCategories(updates);
		expNoOfCategoriesInParent=1; expNoOfCategoriesInRetailer=3;
		assertCache(cat3);
		
		//REMOVE CATEGORY - which is homepage category
		logger.info("====== Test - RemoveCategory - Deleting HomepageCategory");
		cat5.setActive(false);
		updates.clear();
		updates.add(cat5);
		cache.updateCategories(updates);
		expNoOfCategoriesInParent=0; expNoOfCategoriesInRetailer=2;
		assertCache(cat5);
	}
	
	/*
	 * verifies updater thread which gets updates from db
	 */
	@Test
	public void testCategoryUpdater() throws DAOException, InterruptedException, SQLException{
		CategoryFactory.getInstance().setCategoryDAOOverride(mockCategoryReader);
		List<Category> updates = new ArrayList<Category>();
		//insert a new category
		logger.info("====== Test - CategoryUpdater - add a new category");
		CategoryDAO recorder = new CategoryDAO();
		
		Category category = new CategoryBuilder(TESTRETAILER.getId(), 1, "men", "TEST_1", "http://www.testretailer.com/16419432").categoryId(100000).catType(CategoryType.TERMINAL).build();
		recorder.recordCategory(category);
		updates.add(category);
		when(mockCategoryReader.getActiveCategoriesForRetailer(TESTRETAILER.getId())).thenReturn(updates);
		//start cache service and confirm one category in cache
		cache = new CategoryCacheImpl(myRetailers);
		expNoOfCategoriesInParent=1; expNoOfCategoriesInRetailer=1;
		assertCache(category);
		
		//adding the second category
		logger.info("====== Test - CategoryUpdater - adding second category");
		Category category2 = new CategoryBuilder(TESTRETAILER.getId(), 2, "women", "TEST_2", "http://www.testretailer_2.com/16419432").categoryId(100001).catType(CategoryType.TERMINAL).build();
		updates.clear();updates.add(category2);
		when(mockCategoryReader.getUpdatedCategories((Timestamp) notNull())).thenReturn(updates);
		CategoryUpdater updater = new CategoryUpdater(cache, new Timestamp(currentTime-INTERVAL));
		updater.doWorkRegTest();
		expNoOfCategoriesInParent=1; expNoOfCategoriesInRetailer=2;
		assertCache(category2);
		
		//update the category and confirm that update is received
		logger.info("====== Test - CategoryUpdater - change url for an existing category");
		category2.setUrl("http://www.updated_testretailer_2.com");
		updates.clear();updates.add(category2);
		updater.doWorkRegTest();
		expNoOfCategoriesInParent=1; expNoOfCategoriesInRetailer=2;
		assertCache(category2);
		
		//update the category and confirm that update is received - making category inActive
		logger.info("====== Test - CategoryUpdater - making second category inActive");
		category2.setActive(false);
		updates.clear();updates.add(category2);
		updater.doWorkRegTest();
		expNoOfCategoriesInParent=0; expNoOfCategoriesInRetailer=1;
		assertCache(category2);
	}
	
	/**
	 * assert maps in ProductCacheImpl
	 * @param product	 
	 *  
	 */
	public void assertCache(Category category) {
		List<thrift.genereated.retailer.Category> categoriesInRetailerMap = cache.getRetailerAllCategoriesMap().get(category.getRetailerId());
		List<thrift.genereated.retailer.Category> categoriesInParentMap = cache.getParentChildCategoriesMap().get(category.getParentCategoryId());
		_assertEquals(expNoOfCategoriesInRetailer, categoriesInRetailerMap.size());
		_assertEquals(expNoOfCategoriesInParent, categoriesInParentMap.size());
		if(category.isActive()){
			_assertTrue(cache.getCategoryIdCategoryMap().get(category.getCategoryId())!=null);
			_assertTrue(cache.getRetailerAllCategoriesMap().get(category.getRetailerId())!=null);
			_assertTrue(cache.getParentChildCategoriesMap().get(category.getParentCategoryId())!=null);
			
			thrift.genereated.retailer.Category actualCategory = cache.getCategoryIdCategoryMap().get(category.getCategoryId());
			_assertEquals(category.getName(), actualCategory.getCategoryName());
			_assertEquals(category.getRetailerId(), actualCategory.getRetailerId());
			_assertEquals(category.getParentName(), actualCategory.getParentCategoryName());
			_assertEquals(category.getParentCategoryId(), actualCategory.getParentCategoryId());
			_assertEquals(category.getUrl(), actualCategory.getUrl());
		}
		else{
			thrift.genereated.retailer.Category cat=new thrift.genereated.retailer.Category(category.getCategoryId(),category.getName(), category.getRetailerId(), 
					category.getParentCategoryId(), category.getParentName(), category.getUrl(), category.getType()==CategoryType.PARENT);
			_assertTrue(cache.getCategoryIdCategoryMap().get(category.getCategoryId())==null);
			if(cache.getParentChildCategoriesMap().containsKey(category.getParentCategoryId())){
				List<thrift.genereated.retailer.Category> categoryList=cache.getParentChildCategoriesMap().get(category.getParentCategoryId());
				_assertTrue(categoryList.contains(cat)==false);
			}
			if(category.getParentCategoryId()==0){
				if(cache.getRetailerHomepageCategoriesMap().containsKey(category.getParentCategoryId())){
					List<thrift.genereated.retailer.Category> categoryList=cache.getRetailerHomepageCategoriesMap().get(category.getRetailerId());
					_assertTrue(categoryList.contains(cat)==false);
				}
			}
		}
	}
}

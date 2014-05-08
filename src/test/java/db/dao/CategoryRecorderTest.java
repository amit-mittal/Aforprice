package db.dao;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uploader.util.CategoryType;
import util.AbstractTest;
import util.ConfigParms;
import db.DbConnection;
import db.DbConnectionPool;
import db.dao.CategoryDAO.DB_RESULT;
import entities.Category;
import entities.Category.CategoryBuilder;

public class CategoryRecorderTest extends AbstractTest {
	CategoryDAO categoryDAO;
	private static long categoryNameCounter = System.currentTimeMillis();
	DB_RESULT result;
	Category inputCategory, dbCategoryImage;
	DbConnection connection = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("ENVIRONMENT", "QA");
		if(ConfigParms.getInstance().isAshishPC())
			System.setProperty("log4j.configuration", "file:D:\\Ashish\\Eclipse\\workspace\\crawler\\src\\main\\config\\log4j\\log4j.xml");
		else
			System.setProperty("log4j.configuration", "file:/home/batchprod/dist/cur/config/log4j/log4j.xml");
		Logger.getLogger(CategoryRecorderTest.class);
		categoryDAO = new CategoryDAO();
	}
	@After
	public void tearDown() throws Exception {
		DbConnectionPool.get().releaseConnection(connection);
	}
	@Test
	public void testCategoryInsert() throws DAOException {
		String categoryName = "TestCategory1-"+categoryNameCounter++;
		String parentName = "Parent11-"+categoryNameCounter;
		inputCategory = new CategoryBuilder("TestRetailer", null, categoryName, "http://www.test.com")
							.parentCategoryId(11).parentName(parentName).catType(CategoryType.PARENT).build();
		result = categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.INSERTED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
		//same category under different retailer
		parentName = "Parent12-"+categoryNameCounter;
		inputCategory = new CategoryBuilder("TestRetailer2", null, categoryName, "http://www.test.com")
		.parentCategoryId(12).parentName(parentName).catType(CategoryType.PARENT).build();
		result = categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.INSERTED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
	}

	@Test
	public void testCategoryUpdate() throws DAOException {
		String categoryName = "TestCategory2-"+categoryNameCounter++;
		String parentName = "Parent2-"+categoryNameCounter;
		inputCategory = new CategoryBuilder("TestRetailer", null, categoryName, "http://www.test2.com")
		.parentCategoryId(2).parentName(parentName).catType(CategoryType.PARENT).build();
		result = categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.INSERTED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
		//CHANGE URL
		inputCategory.setUrl("http://www.test2-updated.com");
		result= categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.UPDATED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
		//CHANGE TO INACTIVE
		inputCategory.setActive(false);
		result= categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.UPDATED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
		//CHANGE from PARENT -> TERMINAL
		inputCategory.setType(CategoryType.TERMINAL);
		result= categoryDAO.recordCategory(inputCategory);
		_assertEquals(DB_RESULT.UPDATED, result);
		dbCategoryImage = categoryDAO.getCategory(inputCategory.getCategoryId());
		assertCategory(inputCategory, dbCategoryImage);
		
	}

	private void assertCategory(Category expected, Category actual){
		_assertEquals(expected.getCategoryId(), actual.getCategoryId());
		_assertEquals(expected.getName(), actual.getName());
		_assertEquals(expected.getUrl(), actual.getUrl());
		_assertEquals(expected.getRetailerId(), actual.getRetailerId());
		_assertEquals(expected.getParentCategoryId(), actual.getParentCategoryId());
		_assertEquals(expected.getParentName(), actual.getParentName());
		_assertEquals(expected.getGenericCategoryId(), actual.getGenericCategoryId());
		_assertEquals(expected.getGenericName(), actual.getGenericName());
		_assertEquals(expected.isActive(), actual.isActive());
		_assertEquals(expected.getType(), actual.getType());
	}

}

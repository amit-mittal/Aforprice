/**
 * 
 */
package db.dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uploader.util.CategoryType;
import util.Metric;
import db.DbConnection;
import db.Queries;
import entities.Category;
import entities.Category.CategoryBuilder;
import entities.CategorySummary;


public class CategoryDAO extends DataAccessObject{
	
	public static enum DB_RESULT{
		SUCCESS,
		FAILURE,
		INSERTED,
		UNCHANGED,
		UPDATED,
		DUPLICATE_SKIPPED
	}
	private final static Logger logger = Logger.getLogger(CategoryReader.class);	
	private PreparedStatement categoriesStmt, selectChildCategories, selectCategory;

	public CategoryDAO() throws DAOException{
		DbConnection pooledConn = pool.getConnection();
		Connection connection = pooledConn.getConnection();
		try {			
			categoriesStmt = connection.prepareStatement(Queries.SELECT_CATEGORIES_SUMMARY);
			selectChildCategories = connection.prepareStatement(Queries.SELECT_CHILD_CATEGORIES);
			selectCategory = connection.prepareStatement(Queries.SELECT_CATEGORY_ALL_ATTR);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			pool.releaseConnection(pooledConn);
		}
	}

		
	public DB_RESULT recordCategory(Category category) throws DAOException{
		DbConnection conn = null;
		CallableStatement recordCategory = null;
		try{
			conn = pool.getConnection();
			int i=1;
			recordCategory = conn.getConnection().prepareCall("{call PRODUCTS.UpdateCategory(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			recordCategory.setString(i++, category.getName());//name
			recordCategory.setInt(i++, category.getParentCategoryId());//parent_category_id
			recordCategory.setString(i++, category.getParentName());//parent_category_name
			recordCategory.setString(i++, category.getRetailerId());//retailer_id
			recordCategory.setString(i++, category.getUrl());//
			recordCategory.setBoolean(i++, category.isActive());
			recordCategory.setString(i++, category.getType().toDbString());
			recordCategory.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
			recordCategory.setString(i++, category.getRetailerCategoryId());

			recordCategory.registerOutParameter(i++, java.sql.Types.INTEGER);
			recordCategory.registerOutParameter(i, java.sql.Types.INTEGER);
			recordCategory.execute();
			int result = recordCategory.getInt(i-1);
			category.setCategoryId(recordCategory.getInt(i));
			switch(result){
			case 0: 
				return DB_RESULT.INSERTED;
			case 1:
				return DB_RESULT.UPDATED;
			case 2: 
				return DB_RESULT.UNCHANGED;
			default: 
				return DB_RESULT.SUCCESS;
		}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}	
		finally {		
			closeStmt(recordCategory);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * disable given category in the database
	 */
	public void disableCategory(Integer categoryId) throws DAOException{
		DbConnection conn = null;
		PreparedStatement disableCategory = null;
		try{
			int i=1;
			conn = pool.getConnection();
			disableCategory = conn.getConnection().prepareStatement(Queries.DISABLE_CATEGORY);				
			disableCategory.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
			disableCategory.setInt(i++, categoryId);		
			disableCategory.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(disableCategory);
			pool.releaseConnection(conn);
		}
	}	
	public List<Category> getActiveCategoriesForRetailer(String retailerId) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement selectCatForRetailer = null, selectCountCatForRetailer = null;	
		ResultSet categoryRS=null,countRS=null;
		int count=0;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			selectCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ACTIVE_CATEGORIES_FOR_RETAILER);
			selectCountCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ACTIVE_CATEGORIES_FOR_RETAILER_COUNT);
			selectCatForRetailer.setString(1, retailerId);
			selectCountCatForRetailer.setString(1, retailerId);
			categoryRS = selectCatForRetailer.executeQuery();
			countRS = selectCountCatForRetailer.executeQuery();
			while(countRS.next())
				count=countRS.getInt(1);
			Category prodCat = null;
			ArrayList<Category> categories = new ArrayList<Category>(count);
			while(categoryRS.next()){
				prodCat = new CategoryBuilder(
						retailerId,
						categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
						categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
						categoryRS.getString(Category.Columns.CATEGORY_NAME),
						categoryRS.getString(Category.Columns.URL))
						.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
						.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
						.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
						.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
						.build();		
				categories.add(prodCat);
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(selectCatForRetailer);
			closeStmt(selectCountCatForRetailer);
			pool.releaseConnection(conn);
		}
	}

	public ArrayList<Category> getAllCategoriesForRetailer(String retailerId) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement selectCatForRetailer = null;				
		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			selectCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ALL_CATEGORIES_FOR_RETAILER);
			selectCatForRetailer.setString(1, retailerId);
			ResultSet categoryRS = selectCatForRetailer.executeQuery();
			Category prodCat = null;
			ArrayList<Category> categories = new ArrayList<Category>();
			while(categoryRS.next()){
				prodCat = new CategoryBuilder(
						retailerId,
						categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
						categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
						categoryRS.getString(Category.Columns.CATEGORY_NAME),
						categoryRS.getString(Category.Columns.URL))
						.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
						.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
						.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
						.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
						.retailerCategoryId(categoryRS.getString(Category.Columns.EXT_ID))
						.build();		
				categories.add(prodCat);
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(selectCatForRetailer);
			pool.releaseConnection(conn);
		}
	}
	
	public ArrayList<CategorySummary> getAllCategories() throws DAOException{
		try {			
			ResultSet categoryRS = categoriesStmt.executeQuery();
			CategorySummary prodCat = null;
			ArrayList<CategorySummary> categories = new ArrayList<CategorySummary>();
			while(categoryRS.next()){				
				String active = categoryRS.getString(Category.Columns.ACTIVE);				
				prodCat = new CategorySummary(
						categoryRS.getString(Category.Columns.RETAILER_ID),
						categoryRS.getInt(Category.Columns.CATEGORY_ID),
						CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)),
						active.equals("1")?true: false						
						);
				categories.add(prodCat);
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
	}
	
	public ArrayList<Category> getAllChildCategoriesForCategory(String parentCategoryName, String retailerId) throws DAOException{
		try{
			ArrayList<Category> categories = new ArrayList<Category>();
			LinkedList<String> queryCategoryList = new LinkedList<String>();
			queryCategoryList.add(parentCategoryName);			
			while(queryCategoryList.size()>0){
				String category = queryCategoryList.pollFirst();
				selectChildCategories.setString(1, retailerId);
				selectChildCategories.setString(2, category);
				ResultSet categoryRS = selectChildCategories.executeQuery();
				Category prodCat = null;
				while(categoryRS.next()){
					prodCat = new CategoryBuilder(
							retailerId,
							categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
							categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
							categoryRS.getString(Category.Columns.CATEGORY_NAME),
							categoryRS.getString(Category.Columns.URL))
							.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
							.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
							.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
							.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
							.build();		
					categories.add(prodCat);
					if(prodCat.getType() == CategoryType.PARENT)
						queryCategoryList.add(prodCat.getName());
				}
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
	}
	

	public Category getCategory(int categoryId) throws DAOException{
		ResultSet categoryRS = null;
		try{
			selectCategory.setInt(1, categoryId);
			categoryRS = selectCategory.executeQuery();
			if(categoryRS.next()){
				return getCategory(categoryRS);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e.toString());
		}
		finally{
			closeRS(categoryRS);
		}
		return null;
	}
	
	public List<Category> getUpdatedCategories(Timestamp modifiedTime) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement selectCat = null;
		PreparedStatement selectCountCat = null;
		int count = 0;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			selectCountCat = sqlConn.prepareStatement(Queries.GET_UPDATED_CATEGORIES_COUNT);
			selectCountCat.setString(1, modifiedTime.toString());
			ResultSet countRS = selectCountCat.executeQuery();
			if(countRS.next())
				count = countRS.getInt(1);
			if(count>0){
				selectCat = sqlConn.prepareStatement(Queries.GET_UPDATED_CATEGORIES);
				selectCat.setString(1, modifiedTime.toString());
				ResultSet categoryRS = selectCat.executeQuery();
				Category cat = null;
				ArrayList<Category> categories = new ArrayList<Category>(count);
				
				while(categoryRS.next()){
					cat = new CategoryBuilder(
							categoryRS.getString(Category.Columns.RETAILER_ID),
							categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
							categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
							categoryRS.getString(Category.Columns.CATEGORY_NAME),
							categoryRS.getString(Category.Columns.URL))
							.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
							.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
							.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
							.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
							.build();
					cat.setActive(categoryRS.getBoolean(Category.Columns.ACTIVE));
					categories.add(cat);
				}	
				return categories;
			}
			else{
				return new ArrayList<Category>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(selectCat);
			pool.releaseConnection(conn);
		}
	}
	

	/*
	 * Return max time_modified from category table
	 */
	public Timestamp getCategoryMaxModifiedTime() throws SQLException{
		DbConnection conn = null;
		PreparedStatement prodStmt = null;
		ResultSet resultsRS = null;
		Timestamp modifiedTime =null;
		try {
			conn = pool.getConnection();
			Connection sqlConn = conn.getConnection();
			prodStmt = sqlConn.prepareStatement(Queries.GET_MAX_TIME_MODIFIED_CATEGORIES);
			resultsRS = prodStmt.executeQuery();
			
			while (resultsRS.next()) {
				String time = resultsRS.getString(1);
				modifiedTime = Timestamp.valueOf(time);
			}
			return modifiedTime;
		}
		finally {
			closeStmt(prodStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * Test code for categoryCacheImplTest
	 */
	public void regTestRemoveCategories(String retailerId) throws SQLException{
		DbConnection conn = pool.getConnection();
		Metric metRegTestRemove = new Metric("RegTestRemoveCategories");
		PreparedStatement removeCategoryStmt=null;
		int rowsDeleted;
		try {
			metRegTestRemove.start();
			Connection sqlConn = conn.getConnection();
			removeCategoryStmt = sqlConn.prepareStatement(Queries.REMOVE_CATEGORY);
			removeCategoryStmt.setString(1, retailerId);
			metRegTestRemove.end();
			rowsDeleted = removeCategoryStmt.executeUpdate();
			logger.info("Deleted "+rowsDeleted+ " category rows");
			logger.info("\t" + metRegTestRemove.currentStats());
		}
		catch(SQLException e ){			
			logger.error(e.toString(), e);
			throw e;
		}
		finally{
			closeStmt(removeCategoryStmt);
			pool.releaseConnection(conn);
		}
		
	}
	
	/**
	 * All immediate child categories. Does not recurse.
	 * @param catId category id for which the children are needed.
	 * @return
	 */
	public List<Category> getChildCategories(int catId){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement childCatsStmt = null;
		ResultSet resultsRS = null;
		List<Category> children = new ArrayList<Category>();		
		try {
			childCatsStmt = sqlConn.prepareStatement(Queries.SELECT_CHILD_CATEGORIES_CAT_ID);
			childCatsStmt.setInt(1, catId);
			resultsRS = childCatsStmt.executeQuery();
			while(resultsRS.next()){
				children.add(getCategory(resultsRS));
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		finally{			
			closeRS(resultsRS);
			closeStmt(childCatsStmt);
			pool.releaseConnection(conn);
		}
		return children;
	}	
	
	/**
	 * All immediate child categories. Does not recurse.
	 * @param catId category id for which the children are needed.
	 * @return
	 */
	public List<Category> getChildCategories(String retailerId){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement childCatsStmt = null;
		ResultSet resultsRS = null;
		List<Category> children = new ArrayList<Category>();		
		try {
			childCatsStmt = sqlConn.prepareStatement(Queries.SELECT_CHILD_CATEGORIES_RETAILER);
			childCatsStmt.setString(1, retailerId);
			resultsRS = childCatsStmt.executeQuery();
			while(resultsRS.next()){
				children.add(getCategory(resultsRS));
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		finally{			
			closeRS(resultsRS);
			closeStmt(childCatsStmt);
			pool.releaseConnection(conn);
		}
		return children;
	}
	
	/**
	 * Get category from the result set. The resultset should have all the fields of the category table.
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	private Category getCategory(ResultSet result) throws SQLException{
		util.build.CategoryBuilder b = new util.build.CategoryBuilder(
				result.getString(Category.Columns.RETAILER_ID), 
				result.getInt(Category.Columns.PARENT_CATEGORY_ID), 
				result.getString(Category.Columns.PARENT_CATEGORY_NAME), 
				result.getString(Category.Columns.CATEGORY_NAME), 
				result.getString(Category.Columns.URL));
		b.categoryId = result.getInt(Category.Columns.CATEGORY_ID);
		b.catType = CategoryType.valueOfDbString(result.getString(Category.Columns.PARENT));
		b.genericCategoryId = result.getInt(Category.Columns.GENERIC_CATEGORY_ID);
		b.isActive = result.getString(Category.Columns.ACTIVE) != null?(result.getString(Category.Columns.ACTIVE).equals("1")?true:false):false;
		b.timeModified = result.getTimestamp(Category.Columns.TIME_MODIFIED);
		return b.build();
	}

	
	/**
	 * Set the category inactive
	 * @param catId
	 * @return True if a category was updated
	 */
	public boolean setInactive(int catId){
		DbConnection conn = pool.getConnection();
		Connection sqlConn = conn.getConnection();
		PreparedStatement stmt = null;		
		try {
			stmt = sqlConn.prepareStatement(Queries.UPDATE_CATEGORY_INACTIVE);
			stmt.setInt(1, catId);
			return stmt.executeUpdate() == 1;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		finally{			
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return false;
	}
	
	/*
	 * insert categoryid to ancestory_category_id map
	 */
	public void insertCategoryParentMapping(List<Integer> categoryIds, List<Integer> parentIds) throws DAOException{
		DbConnection conn = null;
		PreparedStatement insertStmt = null;
		try{
			conn = pool.getConnection();
			insertStmt = conn.getConnection().prepareStatement(Queries.INSERT_CAT_PARENT_MAP);
			for(int x=0;x<categoryIds.size();x++){
				int i=1;
				insertStmt.setInt(i++, categoryIds.get(x));		
				insertStmt.setInt(i++, parentIds.get(x));		
				insertStmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(insertStmt);
			pool.releaseConnection(conn);
		}
	}	

	/*
	 * get categoryid to ancestory_category_id map
	 */
	public Map<Integer, List<Integer>> getCategoryParentMapping() throws DAOException{
		DbConnection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		try{
			conn = pool.getConnection();
			selectStmt = conn.getConnection().prepareStatement(Queries.SELECT_CAT_PARENT_MAP);
			rs = selectStmt.executeQuery();
			Map<Integer, List<Integer>> categoryAncestorMap = new HashMap<Integer, List<Integer>>();
			while(rs.next()){
				Integer catId = rs.getInt(Category.Columns.CATEGORY_ID);
				Integer parentId = rs.getInt(Category.Columns.PARENT_CATEGORY_ID);
				List<Integer> parentIds = categoryAncestorMap.get(catId);
				if(parentIds == null){
					parentIds = new ArrayList<Integer>();
					categoryAncestorMap.put(catId, parentIds);
				}
				parentIds.add(parentId);
			}
			return categoryAncestorMap;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}
		finally{
			closeStmt(selectStmt);
			closeRS(rs);
			pool.releaseConnection(conn);
		}
	}	
}
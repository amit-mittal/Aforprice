/**
 * 
 */
package db.dao;



/**
 * @author Ashish
 *
 */

public class CategoryReader {
//	public static enum DB_RESULT{
//		SUCCESS,
//		FAILURE,
//		DUPLICATE_SKIPPED
//	}
//	private final static Logger logger = Logger.getLogger(CategoryReader.class);	
//	private static final DbConnectionPool connectionPool = DbConnectionPool.get();
//	private PreparedStatement categoriesStmt, selectChildCategories, selectCategory;
//
//	public CategoryReader() throws DAOException{
//		DbConnection pooledConn = connectionPool.getConnection();
//		Connection connection = pooledConn.getConnection();
//		try {			
//			categoriesStmt = connection.prepareStatement(Queries.SELECT_CATEGORIES_SUMMARY);
//			selectChildCategories = connection.prepareStatement(Queries.SELECT_CHILD_CATEGORIES);
//			selectCategory = connection.prepareStatement(Queries.SELECT_CATEGORY_ALL_ATTR);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//		finally{
//			connectionPool.releaseConnection(pooledConn);
//		}
//	}
//	
//	public List<Category> getActiveCategoriesForRetailer(String retailerId) throws DAOException{
//		DbConnection conn = null;
//		Connection sqlConn = null;
//		PreparedStatement selectCatForRetailer = null, selectCountCatForRetailer = null;	
//		ResultSet categoryRS=null,countRS=null;
//		int count=0;
//		try {
//			conn = connectionPool.getConnection();
//			sqlConn = conn.getConnection();
//			selectCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ACTIVE_CATEGORIES_FOR_RETAILER);
//			selectCountCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ACTIVE_CATEGORIES_FOR_RETAILER_COUNT);
//			selectCatForRetailer.setString(1, retailerId);
//			selectCountCatForRetailer.setString(1, retailerId);
//			categoryRS = selectCatForRetailer.executeQuery();
//			countRS = selectCountCatForRetailer.executeQuery();
//			while(countRS.next())
//				count=countRS.getInt(1);
//			Category prodCat = null;
//			ArrayList<Category> categories = new ArrayList<Category>(count);
//			while(categoryRS.next()){
//				prodCat = new CategoryBuilder(
//						retailerId,
//						categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
//						categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
//						categoryRS.getString(Category.Columns.CATEGORY_NAME),
//						categoryRS.getString(Category.Columns.URL))
//						.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
//						.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
//						.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
//						.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
//						.build();		
//				categories.add(prodCat);
//			}
//			return categories;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//		finally{
//			closeStmt(selectCatForRetailer);
//			closeStmt(selectCountCatForRetailer);
//			connectionPool.releaseConnection(conn);
//		}
//	}
//
//	public ArrayList<Category> getAllCategoriesForRetailer(String retailerId) throws DAOException{
//		DbConnection conn = null;
//		Connection sqlConn = null;
//		PreparedStatement selectCatForRetailer = null;				
//		
//		try {
//			conn = connectionPool.getConnection();
//			sqlConn = conn.getConnection();
//			selectCatForRetailer = sqlConn.prepareStatement(Queries.SELECT_ALL_CATEGORIES_FOR_RETAILER);
//			selectCatForRetailer.setString(1, retailerId);
//			ResultSet categoryRS = selectCatForRetailer.executeQuery();
//			Category prodCat = null;
//			ArrayList<Category> categories = new ArrayList<Category>();
//			while(categoryRS.next()){
//				prodCat = new CategoryBuilder(
//						retailerId,
//						categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
//						categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
//						categoryRS.getString(Category.Columns.CATEGORY_NAME),
//						categoryRS.getString(Category.Columns.URL))
//						.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
//						.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
//						.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
//						.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
//						.build();		
//				categories.add(prodCat);
//			}
//			return categories;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//		finally{
//			closeStmt(selectCatForRetailer);
//			connectionPool.releaseConnection(conn);
//		}
//	}
//	
//	public ArrayList<CategorySummary> getAllCategories() throws DAOException{
//		try {			
//			ResultSet categoryRS = categoriesStmt.executeQuery();
//			CategorySummary prodCat = null;
//			ArrayList<CategorySummary> categories = new ArrayList<CategorySummary>();
//			while(categoryRS.next()){				
//				String active = categoryRS.getString(Category.Columns.ACTIVE);				
//				prodCat = new CategorySummary(
//						categoryRS.getString(Category.Columns.RETAILER_ID),
//						categoryRS.getInt(Category.Columns.CATEGORY_ID),
//						CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)),
//						active.equals("1")?true: false						
//						);
//				categories.add(prodCat);
//			}
//			return categories;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//	}
//	
//	public ArrayList<Category> getAllChildCategoriesForCategory(String parentCategoryName, String retailerId) throws DAOException{
//		try{
//			ArrayList<Category> categories = new ArrayList<Category>();
//			LinkedList<String> queryCategoryList = new LinkedList<String>();
//			queryCategoryList.add(parentCategoryName);			
//			while(queryCategoryList.size()>0){
//				String category = queryCategoryList.pollFirst();
//				selectChildCategories.setString(1, retailerId);
//				selectChildCategories.setString(2, category);
//				ResultSet categoryRS = selectChildCategories.executeQuery();
//				Category prodCat = null;
//				while(categoryRS.next()){
//					prodCat = new CategoryBuilder(
//							retailerId,
//							categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
//							categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
//							categoryRS.getString(Category.Columns.CATEGORY_NAME),
//							categoryRS.getString(Category.Columns.URL))
//							.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
//							.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
//							.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
//							.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
//							.build();		
//					categories.add(prodCat);
//					if(prodCat.getType() == CategoryType.PARENT)
//						queryCategoryList.add(prodCat.getName());
//				}
//			}
//			return categories;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//	}
//	
//	public Category getCategory(int categoryId) throws DAOException{
//		try{
//			selectCategory.setInt(1, categoryId);
//			ResultSet categoryRS = selectCategory.executeQuery();
//			categoryRS.next();
//			Category category = new CategoryBuilder(
//					categoryRS.getString(Category.Columns.RETAILER_ID),
//					categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
//					categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
//					categoryRS.getString(Category.Columns.CATEGORY_NAME),
//					categoryRS.getString(Category.Columns.URL))
//			.categoryId(categoryId)
//			.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
//			.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
//			.isActive(categoryRS.getBoolean(Category.Columns.ACTIVE))
//			.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
//			.build();
//			return category;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//	}
//
//	private void closeStmt(Statement stmt){
//		try{
//			if(stmt != null)
//				stmt.close();
//		}catch(Exception e){
//			logger.error(e.getMessage(), e);
//		}
//	}
//	
//	public List<Category> getUpdatedCategories(Timestamp modifiedTime) throws DAOException{
//		DbConnection conn = null;
//		Connection sqlConn = null;
//		PreparedStatement selectCat = null;
//		PreparedStatement selectCountCat = null;
//		int count = 0;
//		try {
//			conn = connectionPool.getConnection();
//			sqlConn = conn.getConnection();
//			selectCountCat = sqlConn.prepareStatement(Queries.GET_UPDATED_CATEGORIES_COUNT);
//			selectCountCat.setString(1, modifiedTime.toString());
//			ResultSet countRS = selectCountCat.executeQuery();
//			if(countRS.next())
//				count = countRS.getInt(1);
//			if(count>0){
//				selectCat = sqlConn.prepareStatement(Queries.GET_UPDATED_CATEGORIES);
//				selectCat.setString(1, modifiedTime.toString());
//				ResultSet categoryRS = selectCat.executeQuery();
//				Category cat = null;
//				ArrayList<Category> categories = new ArrayList<Category>(count);
//				
//				while(categoryRS.next()){
//					cat = new CategoryBuilder(
//							categoryRS.getString(Category.Columns.RETAILER_ID),
//							categoryRS.getInt(Category.Columns.PARENT_CATEGORY_ID),
//							categoryRS.getString(Category.Columns.PARENT_CATEGORY_NAME),						
//							categoryRS.getString(Category.Columns.CATEGORY_NAME),
//							categoryRS.getString(Category.Columns.URL))
//							.categoryId(categoryRS.getInt(Category.Columns.CATEGORY_ID))
//							.genericCategoryId(categoryRS.getInt(Category.Columns.GENERIC_CATEGORY_ID))
//							.genericName(categoryRS.getString(Category.Columns.GENERIC_CATEGORY_NAME))
//							.catType(CategoryType.valueOfDbString(categoryRS.getString(Category.Columns.PARENT)))
//							.build();
//					cat.setActive(categoryRS.getBoolean(Category.Columns.ACTIVE));
//					categories.add(cat);
//				}	
//				return categories;
//			}
//			else{
//				return new ArrayList<Category>();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//		finally{
//			closeStmt(selectCat);
//			connectionPool.releaseConnection(conn);
//		}
//	}
//	
//	/*
//	 * Return max time_modified from category table
//	 */
//	public Timestamp getCategoryMaxModifiedTime() throws SQLException{
//		DbConnection conn = null;
//		PreparedStatement prodStmt = null;
//		ResultSet resultsRS = null;
//		Timestamp modifiedTime =null;
//		try {
//			conn = connectionPool.getConnection();
//			Connection sqlConn = conn.getConnection();
//			prodStmt = sqlConn.prepareStatement(Queries.GET_MAX_TIME_MODIFIED_CATEGORIES);
//			resultsRS = prodStmt.executeQuery();
//			
//			while (resultsRS.next()) {
//				String time = resultsRS.getString(1);
//				modifiedTime = Timestamp.valueOf(time);
//			}
//			return modifiedTime;
//		}
//		finally {
//			closeStmt(prodStmt);
//			connectionPool.releaseConnection(conn);
//		}
//	}
//	
//	/*
//	 * Test code for categoryCacheImplTest
//	 */
//	public void regTestRemoveCategories(String retailerId) throws SQLException{
//		DbConnection conn = connectionPool.getConnection();
//		Metric metRegTestRemove = new Metric("RegTestRemoveCategories");
//		PreparedStatement removeCategoryStmt=null;
//		int rowsDeleted;
//		try {
//			metRegTestRemove.start();
//			Connection sqlConn = conn.getConnection();
//			removeCategoryStmt = sqlConn.prepareStatement(Queries.REMOVE_CATEGORY);
//			removeCategoryStmt.setString(1, retailerId);
//			metRegTestRemove.end();
//			rowsDeleted = removeCategoryStmt.executeUpdate();
//			logger.info("Deleted "+rowsDeleted+ " category rows");
//			logger.info("\t" + metRegTestRemove.currentStats());
//		}
//		catch(SQLException e ){			
//			logger.error(e.toString(), e);
//			throw e;
//		}
//		finally{
//			closeStmt(removeCategoryStmt);
//			connectionPool.releaseConnection(conn);
//		}
//		
//	}
}

/**
 * 
 */
package db.dao;


/**
 * @author Ashish
 *
 */
public class CategoryRecorder extends DataAccessObject {
//	public static enum DB_RESULT{
//		INSERTED,
//		SUCCESS,
//		FAILURE,
//		UNCHANGED,
//		UPDATED
//	}
//	/*
//	 * Store price in database if its changed from previous price
//	 * It calls stored procedure UpdatePrice
//	 */
//	public DB_RESULT recordCategory(Category category) throws DAOException{
//		DbConnection conn = null;
//		CallableStatement recordCategory = null;
//		try{
//			conn = pool.getConnection();
//			int i=1;
//			recordCategory = conn.getConnection().prepareCall("{call PRODUCTS.UpdateCategory(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
//			recordCategory.setString(i++, category.getName());//name
//			recordCategory.setInt(i++, category.getParentCategoryId());//parent_category_id
//			recordCategory.setString(i++, category.getParentName());//parent_category_name
//			recordCategory.setString(i++, category.getRetailerId());//retailer_id
//			recordCategory.setString(i++, category.getUrl());//
//			recordCategory.setBoolean(i++, category.isActive());
//			recordCategory.setString(i++, category.getType().toDbString());
//			recordCategory.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
//			recordCategory.registerOutParameter(i++, java.sql.Types.INTEGER);
//			recordCategory.registerOutParameter(i, java.sql.Types.INTEGER);
//			recordCategory.execute();
//			int result = recordCategory.getInt(i-1);
//			category.setCategoryId(recordCategory.getInt(i));
//			switch(result){
//			case 0: 
//				return DB_RESULT.INSERTED;
//			case 1:
//				return DB_RESULT.UPDATED;
//			case 2: 
//				return DB_RESULT.UNCHANGED;
//			default: 
//				return DB_RESULT.SUCCESS;
//		}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}	
//		finally {		
//			closeStmt(recordCategory);
//			pool.releaseConnection(conn);
//		}
//	}
//	
//	/*
//	 * disable given category in the database
//	 */
//	public void disableCategory(Integer categoryId) throws DAOException{
//		DbConnection conn = null;
//		PreparedStatement disableCategory = null;
//		try{
//			int i=1;
//			conn = pool.getConnection();
//			disableCategory = conn.getConnection().prepareStatement(Queries.DISABLE_CATEGORY);				
//			disableCategory.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
//			disableCategory.setInt(i++, categoryId);		
//			disableCategory.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DAOException(e.toString());
//		}
//		finally{
//			closeStmt(disableCategory);
//			pool.releaseConnection(conn);
//		}
//	}

}

package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import thrift.servers.ProductData;
import util.Metric;
import util.build.ProductCategoryBuilder;
import db.DbConnection;
import db.Queries;
import entities.ProductCategory;
import entities.ProductSummary;

public class ProductCategoryDAO extends DataAccessObject {
	private final static Logger logger = Logger.getLogger(ProductCategoryDAO.class);
	public ProductCategoryDAO() {
		// TODO Auto-generated constructor stub
	}	

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @throws SQLException
	 */
	public void startInsertBatch() throws SQLException{
		super.startInsertBatch(Queries.INSERT_PRODUCT_CATEGORY);
	}

	/**
	 * The semantics of doing an insertBatch is to call startInsertBatch before calling insertBatch
	 * @param sqlConn connection which needs to be used for creating the prepared statement
	 * @throws SQLException
	 */
	public void startInsertBatch(Connection sqlConn) throws SQLException{
		super.startInsertBatch(sqlConn, Queries.INSERT_PRODUCT_CATEGORY);
	}

	public ProductCategory insertBatch(ProductSummary prod, boolean flush) throws SQLException{
		int i=0;
		PreparedStatement s = getInsertStmt();
		ProductCategoryBuilder b = new ProductCategoryBuilder();
		b.prodId = prod.getId();
		s.setInt(++i, b.prodId);
		b.categoryId = prod.getCategoryId(); 
		s.setInt(++i, b.categoryId);
		b.timeModified = new Timestamp(System.currentTimeMillis());
		s.setTimestamp(++i, b.timeModified);
		s.addBatch();
		if(flush)
			s.executeUpdate();
		return b.build();

	}
	/**
	 * ProductReviewHistory from the result set of <tt>PRODUCT_SELL_RANKS_HISTORY</tt> table 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public ProductCategory get(ResultSet rs) throws SQLException{
		ProductCategoryBuilder b = new ProductCategoryBuilder();
		b.categoryId = rs.getInt(ProductCategory.Columns.CATEGORY_ID);
		b.prodId = rs.getInt(ProductCategory.Columns.PRODUCT_ID);
		b.timeModified = rs.getTimestamp(ProductCategory.Columns.TIME_MODIFIED);
		return b.build();
	}
	
	/**
	 * @param existingProducts only query categories for given products
	 * @return update given products with its categories
	 * @throws SQLException 
	 */
	public void populateProductCategories(HashMap<ProductSummary, ProductSummary> existingProducts) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			//make hash set of productIds to filter out categories we don't need
			Metric metMapCreation = new Metric("ProductIdProductMapCreationTime");
			metMapCreation.start();
			Map<Integer, ProductSummary> productIdProductMap = new HashMap<Integer, ProductSummary>(existingProducts.size());
			for(ProductSummary product : existingProducts.values()){
				productIdProductMap.put(product.getId(), product);
			}
			metMapCreation.end();
			logger.info(metMapCreation.currentStats());
			
			Metric metProdCatCreation = new Metric("ProductCategoryCachingTime");
			metProdCatCreation.start();
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_CATEGORIES_ALL);
			rs = stmt.executeQuery();
			long fetchedRows=0;
			long skippedRows=0;
			logger.info("fetching product_category rows");
			while(rs.next()){
				Integer categoryId = rs.getInt(ProductCategory.Columns.CATEGORY_ID);
				Integer productId = rs.getInt(ProductCategory.Columns.PRODUCT_ID);
				ProductSummary existing = productIdProductMap.get(productId);
				if(existing!=null){
					existing.addCategory(categoryId);
					fetchedRows++;
					if(fetchedRows%1e5==0)//keep this inside if otherwise we get lots of logs when value is 0
						logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
				}else
					skippedRows++;
				if(skippedRows%1e5==0)
					logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
			}//while(rs.next()){ ends...
			metProdCatCreation.end();
			logger.info(metProdCatCreation.currentStats(TimeUnit.MINUTES));
			logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
		}finally {		
			closeRS(rs);
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
	}

	/**
	 * @return snapshot of product_category table as map of productid->categoryids
	 * @throws SQLException 
	 */
	public Map<Integer, List<Integer>> getProductCategoriesMap() throws SQLException{
		Map<Integer, List<Integer>> productCategoriesMap = new HashMap<Integer, List<Integer>>();
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			Metric metProdCatCreation = new Metric("ProductCategoryCachingTime");
			metProdCatCreation.start();
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.GET_PRODUCT_CATEGORIES_ALL);
			rs = stmt.executeQuery();
			long fetchedRows=0;
			long skippedRows=0;
			logger.info("fetching product_category rows");
			while(rs.next()){
				Integer categoryId = rs.getInt(ProductCategory.Columns.CATEGORY_ID);
				Integer productId = rs.getInt(ProductCategory.Columns.PRODUCT_ID);
				if(ProductData.getInstance().containsProduct(productId)){
					if(!productCategoriesMap.containsKey(productId))
						productCategoriesMap.put(productId, new ArrayList<Integer>());
					productCategoriesMap.get(productId).add(categoryId);
					fetchedRows++;
					if(fetchedRows%1e5==0)//keep this inside if otherwise we get lots of logs when value is 0
						logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
				}else
					skippedRows++;
				if(skippedRows%1e5==0)
					logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
			}//while(rs.next()){ ends...
			metProdCatCreation.end();
			logger.info(metProdCatCreation.currentStats(TimeUnit.MINUTES));
			logger.info("fetchedRows=" + fetchedRows + ", skippedRows=" + skippedRows );
		}finally {		
			closeRS(rs);
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return productCategoriesMap;
	}
}
package db.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.Utils;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import entities.ProductSummary;

public class ProductsDownloadDAO {
	
	private static final Logger logger= Logger.getLogger(ProductsDownloadDAO.class);
	
	private final static DbConnectionPool pool = DbConnectionPool.get();
	
	private static ProductsDownloadDAO instance = new ProductsDownloadDAO();
	
	private ProductsDownloadDAO(){
	}
	
	public synchronized static ProductsDownloadDAO get(){
		return instance;
	}
	
	/**
	 * INSERT INTO PRODUCT_DOWNLOAD (RETAILER_ID, CATEGORY_ID, PRODUCT_NAME, DOWNLOAD_TIME, MODEL_NO, DESCRIPTION, IMAGE_URL, URL, PRICE)
			  				 VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? );";	
	 * @param category
	 * @return
	 * @throws DAOException
	 */
	public List<ProductSummary> recordProducts(List<ProductSummary> products){
		List<ProductSummary> errProds = new ArrayList<ProductSummary>();
		DbConnection pooledConn = pool.getConnection();
		Connection sqlConn = pooledConn.getConnection();
		PreparedStatement insertProd = null; 
		try{
			insertProd = sqlConn.prepareStatement(Queries.INSERT_DOWN_PRODUCTS);

			for(ProductSummary product: products){
				try{
					int i=1;
					insertProd.setString(i++, product.getRetailerId());
					insertProd.setInt(i++, product.getCategoryId());
					insertProd.setString(i++, product.getName());
					insertProd.setTimestamp(i++, new Timestamp(product.getDownloadTime().getTime()));				
					insertProd.setString(i++, product.getModel());//MODEL
					insertProd.setString(i++, "" /*product.getDesc()*/);//DESCRIPTION - stop storing it for time being
					insertProd.setString(i++, product.getImageUrl());//IMAGE_URL				
					insertProd.setString(i++, product.getUrl());//URL
					insertProd.setDouble(i++, product.getPrice());		
					insertProd.setString(i++, "N");
					insertProd.setString(i++, ConfigParms.getDownloadMode().getDbValue());
					insertProd.setDouble(i++, product.getReviewRating());
					insertProd.setInt(i++, product.getNumReviews());
					insertProd.setInt(i++, product.getSalesRank());
					insertProd.addBatch();				
				} catch (SQLException e) {
					e.printStackTrace();
					errProds.add(product);
				}
			}
			try{
				insertProd.executeBatch();
			}catch(BatchUpdateException be){
				logger.error(be.getMessage(), be);
				errProds.addAll(getProdsInErr(products, be.getUpdateCounts()));
			}
			catch(Exception e){
				logger.error(e.getMessage(), e);
				return products;
			}
		}
		catch(SQLException e){
			logger.error(e.getMessage(), e);
			return products;
		}
		finally{
			Utils.closeStmt(insertProd);
			pool.releaseConnection(pooledConn);
		}
		return errProds;
	}
	
	private List<ProductSummary> getProdsInErr(List<ProductSummary> products, int[] retCodes){
		List<ProductSummary> errProds = new ArrayList<ProductSummary>();
		for(int j = 0; j < products.size(); j++){			
			if(retCodes.length >= j + 1){
				if(retCodes[j] == Statement.EXECUTE_FAILED){
					errProds.add(products.get(j));
				}
				j++;
				continue;
			}
			errProds.add(products.get(j));			
		}
		return errProds;
	}
}
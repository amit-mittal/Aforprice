package db.dao;

import static entities.ProductsDownloadRetry.Columns.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.YesNo;
import util.build.ProductsDownloadRetryBuilder;

import db.DbConnection;
import db.Queries;
import entities.ProductsDownloadRetry;

public class ProductsDownloadRetryDAO extends DataAccessObject {

	public ProductsDownloadRetryDAO() {
	}
	
	public void insert(ProductsDownloadRetry entry) throws SQLException{
		PreparedStatement statment = null;
		DbConnection conn = pool.getConnection();
		try {
			Connection sqlConn = conn.getConnection();			
			statment = sqlConn.prepareStatement(Queries.INSERT_PRODUCTS_DOWNLOAD_RETRY);
			int i = 0;
			statment.setString(++i, entry.getRetailerId());
			statment.setInt(++i, entry.getCategoryId());
			statment.setString(++i, entry.getCategoryName());
			statment.setString(++i, entry.getStartUrl());
			statment.setTimestamp(++i, new Timestamp(entry.getCreateTime().getTime()));
			statment.setString(++i, entry.getRetryReason());
			statment.executeUpdate();
		}
		finally{
			closeStmt(statment);
			pool.releaseConnection(conn);

		}
	}
	
	public List<ProductsDownloadRetry> getDownloadsToRetry(String retailer, Date time) throws SQLException{
		PreparedStatement statment = null;
		ResultSet results = null;
		DbConnection conn = pool.getConnection();
		List<ProductsDownloadRetry> retries = new ArrayList<>();
		try {
			Connection sqlConn = conn.getConnection();			
			statment = sqlConn.prepareStatement(Queries.GET_PRODUCT_DOWNLOAD_RETRY);
			int i = 0;
			statment.setString(++i, retailer);
			statment.setTimestamp(++i, new Timestamp(time.getTime()));
			results = statment.executeQuery();
			while(results.next()){
				retries.add(get(results));
			}
		}
		finally{
			closeRS(results);
			closeStmt(statment);
			pool.releaseConnection(conn);
		}
		return retries;
	}
	
	public void setProcessed(int id) throws SQLException{
		PreparedStatement statment = null;
		DbConnection conn = pool.getConnection();
		try {
			Connection sqlConn = conn.getConnection();			
			statment = sqlConn.prepareStatement(Queries.SET_PROCESSED);
			statment.setInt(1, id);
			statment.executeUpdate();
		}
		finally{
			closeStmt(statment);
			pool.releaseConnection(conn);
		}
	}
	
	private ProductsDownloadRetry get(ResultSet results) throws SQLException{
		ProductsDownloadRetryBuilder b = new ProductsDownloadRetryBuilder();
		b.categoryId = results.getInt(CATEGORY_ID);
		b.categoryName = results.getString(CATEGORY_NAME);
		b.createTime = results.getDate(CREATE_TIME);
		b.id = results.getInt(ID);
		String processed = results.getString(PROCESSED);
		b.processd = processed == null?YesNo.N:(processed.equals("Y")?YesNo.Y:YesNo.N);
		b.retailerId = results.getString(RETAILER_ID);
		b.retryReason = results.getString(RETRY_REASON);
		b.startUrl = results.getString(START_URL);
		return b.build();
	}
}
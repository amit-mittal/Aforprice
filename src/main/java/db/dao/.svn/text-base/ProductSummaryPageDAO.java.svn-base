package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import util.DateTimeUtils;

import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import entities.ProductSummary;

public class ProductSummaryPageDAO {
	private static final DbConnectionPool connPool = DbConnectionPool.get();
	private Logger LOGGER = Logger.getLogger(ProductSummaryPageDAO.class);
	
	public void create(List<ProductSummary> products, String url, String path, Date date){
		if(products == null || products.size() == 0)
			return;
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodSummListPgStmt = null;
		PreparedStatement prodSummPgStmt = null;
		int i = 0;
		try {
			conn = connPool.getConnection();
			sqlConn = conn.getConnection();
			prodSummListPgStmt = sqlConn.prepareStatement(Queries.INSERT_PRODUCT_SUMMARY_LIST_PAGE, Statement.RETURN_GENERATED_KEYS);
			prodSummListPgStmt.setString(++i, url);
			prodSummListPgStmt.setString(++i, path);
			prodSummListPgStmt.executeUpdate();
			int id = -1;
			ResultSet genKeyRS = prodSummListPgStmt.getGeneratedKeys();
			if(genKeyRS.next()){
				id = genKeyRS.getInt(1);
				prodSummPgStmt = sqlConn.prepareStatement(Queries.INSERT_PRODUCT_SUMMARY_PAGE);
				for(ProductSummary prod: products){
					try{
						i = 0;
						prodSummPgStmt.setString(++i, prod.getName());
						prodSummPgStmt.setInt(++i, id);
						prodSummPgStmt.setTimestamp(++i, new Timestamp(date.getTime()));
						prodSummPgStmt.executeUpdate();
					}catch(SQLException e){
						LOGGER.error("Error storing=>" + DateTimeUtils.currentDateYYYYMMDD(date) + ":" + prod.getName() + ":" + url + ":" + path, e);						
					}
				}
			}
			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);			
		}
		finally{
			connPool.releaseConnection(conn);
			closeStmt(prodSummPgStmt);
			closeStmt(prodSummListPgStmt);			
		}
	}
	
	private void closeStmt(Statement stmt){
		try{
			if(stmt != null)
				stmt.close();
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
	}
}
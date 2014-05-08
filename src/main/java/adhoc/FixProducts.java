package adhoc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import db.DbConnectionPool;

public class FixProducts {
	
	public static String lastProdIdFixedContainer = "/home/anurag/tmp/fixproducts/lastprodid.txt";
	
	public static void fixProductHistory() throws IOException, SQLException{
		Connection connection = null;	
		Connection conn2 = null;
		try{
		int lastProdId = lastFixedProductId();
		connection = DbConnectionPool.get().getConnection().getConnection();	
		conn2 = DbConnectionPool.get().getConnection().getConnection();
		PreparedStatement stmt = connection.prepareStatement(QueriesTemp.GET_PRICE_HIST_TO_FIX, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		stmt.setFetchSize(Integer.MIN_VALUE);
		stmt.setInt(1, lastProdId);
		PreparedStatement insertStmt = conn2.prepareStatement(QueriesTemp.INSERT_PRODUCT_HIST);
		
		
		ResultSet results = stmt.executeQuery();
		int curProdId = -1;
		double lastPrice = Double.NaN;
		int k = 0;
		while(results.next()){
			k++;
			/*
			 * PRODUCT_ID, TIME, PRICE, RETAILER_ID, TIME_MODIFIED
			 */
			int prodId = results.getInt("PRODUCT_ID");
			double price = results.getDouble("PRICE");
			if(curProdId != prodId){
				System.out.println("Processing prodId = " + prodId);
				storeFixedProducId(curProdId);
				curProdId = prodId;
				lastPrice = price;
				insert(results, insertStmt);
				continue;
			}			
			
			if(price == lastPrice){
				//This row needs to be skipped
				continue;				
			}
			price = lastPrice;
			//Price is different
			insert(results, insertStmt);
			
			if(k%10000 == 0){
				System.gc();
			}
		}
		}finally{
			
		}
	}
	
	public static void insert(ResultSet result, PreparedStatement insertStmt) throws SQLException{
		int i = 0;
		insertStmt.setInt(++i, result.getInt(("PRODUCT_ID")));
		insertStmt.setTimestamp(++i, result.getTimestamp("TIME"));
		insertStmt.setDouble(++i, result.getDouble("PRICE"));
		insertStmt.setString(++i, result.getString("RETAILER_ID"));
		Timestamp ts = null;
		try{
			ts = result.getTimestamp("TIME_MODIFIED");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		insertStmt.setTimestamp(++i, ts);
		int ret = insertStmt.executeUpdate();
		if(ret != 1){
			throw new SQLException("Unable to insert the row: " + result.getString("RETAILER_ID") + "," + result.getDouble("PRICE"));
		}
	}
	
	public static int lastFixedProductId() throws IOException{		
		DataInputStream s = null;
		try{
			s = new DataInputStream(new FileInputStream(new File(lastProdIdFixedContainer)));
			return s.readInt();			
		}finally{
			s.close();
		}		
	}
	
	public static void storeFixedProducId(int prodId) throws IOException{
		DataOutputStream s = null;
		try{
			s = new DataOutputStream(new FileOutputStream(lastProdIdFixedContainer, false));
			s.writeInt(prodId);
			System.out.println("Done processing prodId = " + prodId);
		}finally{
			s.close();
		}
	}
	
	public static void main(String[] args) throws IOException, SQLException{
		fixProductHistory();
	}

}

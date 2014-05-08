package adhoc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class TestMySQL {

	private Connection _connection = null;
	
	public void getConnection() throws SQLException{
		_connection = DriverManager.getConnection("jdbc:mysql://192.168.1.146/PRODUCTS?user=root&password=iluviya");		    
	    System.out.println("conn established");
	
	}
	
	public void runQueries() throws SQLException{
	    Statement stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery("select RETAILER_ID, RETAILER_NAME, URL, LOGO, TIME_MODIFIED from retailer");
	    while(rs.next()){
	    	log("print retailer table");
	    	System.out.println(rs.getString(1));
	    }		
	}
	
	public void insertPrice(String productId, float price, String url, long timeOfPrice) throws SQLException{
		CallableStatement updatePrice = _connection.prepareCall("{call PRODUCTS.UpdatePrice(?, ?, ?, ?)}");
		int i=1;
		updatePrice.setString(i++, productId);
		updatePrice.setFloat(i++, price);
		updatePrice.setString(i++, url);
		updatePrice.setTimestamp(i++, new Timestamp(timeOfPrice));
		updatePrice.execute();
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestMySQL testMySQL = new TestMySQL();
		try {
			testMySQL.getConnection();
			testMySQL.runQueries();
			testMySQL.insertPrice("ABC", (float) 1.5, "ABC_URL", System.currentTimeMillis());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void log(String log){
		System.out.println(log);
	}

}

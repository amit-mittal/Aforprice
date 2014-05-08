/**
 * 
 */
package db;

import global.exceptions.Bhagte2BandBajGaya;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.UtilityLogger;

/**
 * @author Ashish
 *
 */
public class DbConnection {
	private Connection _connection = null;

	public final static String MYDB = "MYSQL";
	private String host, user, password, dbName;
	private boolean leased = false;
	private long leasedTime;
	
	public DbConnection(String host, String user, String password, String dbName){
		try {
			this.host = host;
			this.user = user;
			this.password = password;
			this.dbName = dbName;
			connect();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Bhagte2BandBajGaya(e.toString());
		}
	}
	
	private void connect() throws SQLException{
		UtilityLogger.logInfo(String.format("Connecting to database: host=%s, db=%s, user=%s, param=&rewriteBatchedStatements=true&autoReconnect=true", host, dbName, user));
		String dbConnectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&rewriteBatchedStatements=true&autoReconnect=true", host, dbName, user, password);
		_connection = DriverManager.getConnection(dbConnectionString);
		UtilityLogger.logInfo("conn established");	
	}
	
	public Connection getConnection(){
		try {
			if(_connection.isClosed())
				connect();
			return _connection;
		} catch (SQLException e) {
			close();
			throw new Bhagte2BandBajGaya(e.toString());
		}				
	}
	
	public void lease(){
		this.leased = true;
		this.leasedTime = System.currentTimeMillis();
	}
	
	public boolean canLease(){
		return !this.leased;
	}
	
	public void expireLease(){
		this.leased = false;
	}
	
	public void close(){
		if (_connection!=null)
			try {
				_connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
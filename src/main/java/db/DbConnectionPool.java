package db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.Constants;
import entities.DbCredentials;
import global.exceptions.Bhagte2BandBajGaya;

public class DbConnectionPool {
	private static final Logger LOGGER = Logger.getLogger(DbConnectionPool.class);
		
	private final List<DbConnection> connections = new ArrayList<DbConnection>(Constants.DB_POOL_SIZE.maxSize());
	private final ReentrantLock connectionPoolLock = new ReentrantLock();
	private final Condition connectionAvailable = connectionPoolLock.newCondition();
	
	private final static DbCredentials dbLogin = ConfigParms.getInstance().getDbCredentials(); 
	private final static DbConnectionPool pool = new DbConnectionPool();
	
	
	private DbConnectionPool(){
		LOGGER.info("Creating pool of connections");
		for(int i=0; i< Constants.DB_POOL_SIZE.initialSize(); i++)
			connections.add(new DbConnection(dbLogin.getHost(), dbLogin.getUser(), dbLogin.getPassword(), dbLogin.getDbName()));
		LOGGER.info("Created initial pool of " + Constants.DB_POOL_SIZE.initialSize() + " connnections");
	}
	
	public static DbConnectionPool get(){
		return pool;
	}
	
	public DbConnection getConnection(){
		try {
			connectionPoolLock.lock();
			for(DbConnection connection : connections){
				if(connection.canLease()){
					connection.lease();
					return connection;
				}
			}
			if(connections.size() < Constants.DB_POOL_SIZE.maxSize()){
				long start = System.currentTimeMillis();
				DbConnection conn = new DbConnection(dbLogin.getHost(), dbLogin.getUser(), dbLogin.getPassword(), dbLogin.getDbName());
				conn.lease();
				LOGGER.info("added one connction to the pool. time taken(ms) to add conection = " + (System.currentTimeMillis() - start));
				connections.add(conn);
				return conn;
			}
			LOGGER.warn("connection not available, wait for connection to be available");			
			if(!connectionAvailable.await(1000, TimeUnit.MILLISECONDS)){//wake up when connection is available
				throw new Bhagte2BandBajGaya("Connection not available after waiting for 1000 ms" );
			}
			return getConnection(); //this will make recursive call to getConnection and it has the lock, 
									//woken up by another thread which release the lock so guarantee to get connection
		}catch(InterruptedException e){
			e.printStackTrace();
			throw new Bhagte2BandBajGaya(e.toString());
		}
		finally{
			connectionPoolLock.unlock();
		}		
	}
	
	public void releaseConnection(DbConnection connection){
		if(connection == null)
			return;
		connectionPoolLock.lock();
		connection.expireLease();
		connectionAvailable.signal();
		connectionPoolLock.unlock();
	}
}

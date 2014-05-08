package db.util;

import global.exceptions.Bhagte2BandBajGaya;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;

public class UniqIdGenerator {	
	
	private int prodId;
	private static UniqIdGenerator self = null;
	
	private UniqIdGenerator(){
	}
		
	public synchronized static UniqIdGenerator get(){
		if(self == null)
			self = new UniqIdGenerator();
		return self;
	}
	
	public int getNextProdId(){
 		DbConnection pooledConn = DbConnectionPool.get().getConnection(); 
		try{
			Connection conn = pooledConn.getConnection();		
			ResultSet result = conn.createStatement().executeQuery(Queries.MAX_PRODUCT_ID);
			if(result.next()){			
				prodId = result.getInt(1);
				return ++prodId;
			}else
				throw new Bhagte2BandBajGaya("Next Product Id is unknown");
		}catch(SQLException sqe){
			throw new Bhagte2BandBajGaya(sqe.getMessage(), sqe);
		}
		finally{
			DbConnectionPool.get().releaseConnection(pooledConn);
		}
	}
}
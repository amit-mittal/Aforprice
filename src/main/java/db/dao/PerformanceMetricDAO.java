package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import util.MetricSummary;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;

public class PerformanceMetricDAO{	

	private final Logger logger = Logger.getLogger(PerformanceMetricDAO.class);
	private final static DbConnectionPool pool = DbConnectionPool.get();
	private final static PerformanceMetricDAO instance = new PerformanceMetricDAO();
	
	private PerformanceMetricDAO(){	
	}
	
	public static PerformanceMetricDAO getInstance(){
		return instance;		
	}
	
	public boolean storeMetricSummary(MetricSummary metricSummary) throws DAOException{		
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.INSERT_PERFORMANCE_METRIC);
			int i=1;
			//NAME, PERIOD, TOTAL_PROCESS_TIME, TOTAL_PROCESS_COUNT, 
			stmt.setString(i++,  metricSummary.getName() );
			stmt.setString(i++, metricSummary.getPeriod() );
			stmt.setLong(i++, metricSummary.getTotalProcessTime());
			stmt.setLong(i++, metricSummary.getTotalProcessCount());
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}	
		finally{
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
	}
	
	private void closeStmt(Statement stmt){
		try{
			if(stmt != null)
				stmt.close();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
}

package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import db.DbConnection;

public class ReplicationDAO extends DataAccessObject{	

	private final static Logger logger = Logger.getLogger(ReconilationDAO.class);

	public ReplicationInfo getReplicationInfo() throws SQLException{
		DbConnection conn = null;
		PreparedStatement replicationStmt = null;
		ResultSet resultsRS = null;
		try {
			conn = pool.getConnection();
			Connection sqlConn = conn.getConnection();
			replicationStmt = sqlConn.prepareStatement("show slave status");
			resultsRS = replicationStmt.executeQuery();
			ReplicationInfo info = new ReplicationInfo();
			while(resultsRS.next()){
				info.error = resultsRS.getString("Last_Error");
				String delay = resultsRS.getString("Seconds_Behind_Master");
				if(delay!=null)
					info.delayInSeconds= Integer.parseInt(delay);
				info.isSQLThreadRunning = Boolean.valueOf(resultsRS.getBoolean("Slave_SQL_Running"));
				info.isIOThreadRunning = Boolean.valueOf(resultsRS.getBoolean("Slave_IO_Running"));
			}
			return info;
		}
		finally {
			closeRS(resultsRS);
			closeStmt(replicationStmt);
			pool.releaseConnection(conn);
		}
	}

	public static class ReplicationInfo{
		public String error;
		public int delayInSeconds=-1;
		public boolean isSQLThreadRunning;
		public boolean isIOThreadRunning;
	}
}

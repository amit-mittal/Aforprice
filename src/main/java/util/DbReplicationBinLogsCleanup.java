package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.ConfigParms.Environment;
import db.DbConnection;
import db.dao.ReplicationDAO;
import db.dao.ReplicationDAO.ReplicationInfo;
import entities.DbCredentials;
import global.exceptions.Bhagte2BandBajGaya;

public class DbReplicationBinLogsCleanup {
	private final static Logger logger = Logger.getLogger(DbReplicationBinLogsCleanup.class);
	ReplicationDAO dao = new ReplicationDAO();
	private final int SECONDS_IN_THREE_DAYS = 3 * 24 * 3600;
	public void cleanup() {
		try {
			ReplicationInfo info = dao.getReplicationInfo();
			//if delay is less than 3 days then clean up file older than that
			int delay = dao.getReplicationInfo().delayInSeconds;
			if(delay==-1){
				logger.error("Can't determine replication delay, exit");
				return;
			}
			logger.info(String.format("Replication Delay: %d hours, %d minutes, %d seconds<", 
					info.delayInSeconds/3600, (info.delayInSeconds%3600)/60, info.delayInSeconds%60));
			
			if(delay>SECONDS_IN_THREE_DAYS){
				logger.error("Delay is more than 3 days, not cleaning any files");
				return;
			}
			
			SimpleDateFormat dateFormatYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = dateFormatYYMMDD_HHMMSS.format(DateTimeUtils.add(new Date(), -3, TimeUnit.DAYS, 0));			
			String purgeQuery = "PURGE BINARY LOGS BEFORE '" + dateTime +"'";
			logger.info("Running query: " + purgeQuery);
			DbConnection conn = getPrimaryConnection();
			Connection sqlConn = conn.getConnection();
			PreparedStatement stmt = sqlConn.prepareStatement(purgeQuery);
			stmt.executeUpdate();
			logger.info("Removed old files");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private DbConnection getPrimaryConnection(){
		DbCredentials dbLogin;
		if(ConfigParms.getInstance().getEnvironment()==Environment.QA_REPLICA)
			dbLogin = ConfigParms.getInstance().getDbCredentials(Environment.QA);
		else if(ConfigParms.getInstance().getEnvironment()==Environment.PRODUCTION_REPLICA)
			dbLogin = ConfigParms.getInstance().getDbCredentials(Environment.PRODUCTION);
		else
			throw new Bhagte2BandBajGaya("Unexepected Environment Setting, only QA_REPLICA and PRODUCTION_REPLICA are supported");
		return new DbConnection(dbLogin.getHost(), dbLogin.getUser(), dbLogin.getPassword(), dbLogin.getDbName());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(ConfigParms.getInstance().getEnvironment()!=Environment.QA_REPLICA && 
				ConfigParms.getInstance().getEnvironment()!=Environment.PRODUCTION_REPLICA)
			throw new Bhagte2BandBajGaya("Unexepected Environment Setting, only QA_REPLICA and PRODUCTION_REPLICA are supported");
		DbReplicationBinLogsCleanup monitor = new DbReplicationBinLogsCleanup();		
		monitor.cleanup();
	}


}

package util;

import java.sql.SQLException;
import java.util.Date;
import org.apache.log4j.Logger;
import db.dao.ReplicationDAO;
import db.dao.ReplicationDAO.ReplicationInfo;

public class ReplicationMonitor {
	private final static Logger logger = Logger.getLogger(ReplicationMonitor.class);
	ReplicationDAO dao = new ReplicationDAO();
	private final int SECONDS_IN_HOUR = 3600;
	public void check() {
		try {
			ReplicationInfo info = dao.getReplicationInfo();
			String error="";
			if(info.error!=null && info.error.length()!=0)
				error += "<p>Error: "+info.error+"</p>";
			if(!info.isIOThreadRunning)
				error +="<p>IOThread: OFF</p>";
			if(!info.isSQLThreadRunning)
				error +="<p>SQL Thread: OFF</p>";
			String delayStr = String.format("<p>Replication Delay: %d hours, %d minutes, %d seconds</p>", 
					info.delayInSeconds/SECONDS_IN_HOUR, (info.delayInSeconds%SECONDS_IN_HOUR)/60, info.delayInSeconds%60);
			if(info.delayInSeconds>3*SECONDS_IN_HOUR) //3 HOURS
				error += delayStr;
			logger.info("IOThread:"+info.isIOThreadRunning + ", SQLThread:"+info.isSQLThreadRunning + "Delay="+delayStr+ ",Error:"+info.error);
			if(error.length()!=0)
				generateAlert(error);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * send email alert with test result errors and summary
	 */
	private void generateAlert(String error){
		Date runDate = new Date();
		String subject = DateTimeUtils.currentDateYYYYMMDD(runDate) + "- Replication Monitor Alert";
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append(error);
		html.append("</body></html>");
		Emailer.getInstance().sendHTML(subject, html.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReplicationMonitor monitor = new ReplicationMonitor();
		monitor.check();

	}


}

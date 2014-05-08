package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import util.RECON_FIELDS;
import util.ReconStat;
import util.build.ReconStatBuilder;

import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import entities.DailyRecon;

public class ReconilationDAO extends DataAccessObject{
	public enum TYPE{
		TOTAL,
		NOUID,
		FAILED,
		NEW,
		EXPECTED,
		RECEIVED,
		CATMISMATCH,
		COUNTMISMATCH,
		STALE,	//IN DB, NOT IN DOWNLOAD LIST
		NEWCOUNT,
		STALECOUNT,
		NODESCNEW,		
		NOMODELNEW,
		NOURLNEW,
		NONAME,
		NONAMENEW,
		NOURL,
		NOIMGURL,
		NOIMGURLNEW,
		NOPRICENEW, //No price in the intersection of yesterday's and today's products
		NOPRICE,
		HASREVIEWS,
		NOREVIEWSNEW,
		NORANK,
		NORANKNEW,
		HASREVIEWERS,
		NONUMREVIEWERSNEW,
		PRODUCTSPERCATEGORY,
		CATSTALE_CHILDRENNOTSTALE,
		CATNOTSTALE_ALLCHILDRENSTALE,
		CATSTALE,
		ACTIVECATSCOUNT,
		PRICEUP,
		PRICEDN,
		PRICEUP5ORMORE,
		PRICEDN5ORMORE
		
	}
	public enum NAME{
		PRODUCT,
		CATEGORY,
		COUNTER
	}

	private final Logger logger = Logger.getLogger(ReconilationDAO.class);
	private final static DbConnectionPool pool = DbConnectionPool.get();
	private final static ReconilationDAO instance = new ReconilationDAO();
	
	private ReconilationDAO(){	
	}
	
	public static ReconilationDAO getInstance(){
		return instance;		
	}
	
	public boolean storeRec(String id, Timestamp runDate, NAME recName, TYPE recType, String text, Integer count, Timestamp currentTime) throws DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.INSERT_DAILY_REC);
			int i=1;
			stmt.setString(i++, id);
			stmt.setString(i++, recName.toString());
			stmt.setTimestamp(i++, runDate);
			stmt.setString(i++, recType.toString());
			stmt.setString(i++, text);
			stmt.setInt(i++, count);			
			stmt.setTimestamp(i++, currentTime);
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
	
	public List<DailyRecon> getProductCountMismatches(String retailerId, Date fromRunTime, Date toRunTime) throws DAOException
	{
		List<DailyRecon> dailyRecons = getDailyRecons(retailerId, NAME.COUNTER, RECON_FIELDS.PRODUCTSPERCATEGORY, fromRunTime, toRunTime );		
		return dailyRecons;
	}
	
	public List<DailyRecon> getDailyRecons(String id, NAME recName, RECON_FIELDS field, Date fromRunTime, Date toRunTime ) throws DAOException{		
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement stmt = null;
		List<DailyRecon> dailyRecons = new ArrayList<>();
		ResultSet rs = null;
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			stmt = sqlConn.prepareStatement(Queries.DAILY_RECON_GET);
			int i=1;
			stmt.setString(i++, id +"%");
			stmt.setString(i++,  field.name());
			stmt.setString(i++, recName.name());
			stmt.setTimestamp(i++, new Timestamp(fromRunTime.getTime()));
			stmt.setTimestamp(i++, new Timestamp(toRunTime.getTime()));
			//stmt.setDate(i++, );
			
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				DailyRecon drecon = new DailyRecon(
						rs.getString(1),
						rs.getString(2),
						rs.getDate(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6),
						rs.getDate(7)
						);
				dailyRecons.add(drecon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.toString());
		}	
		finally{
			closeRS(rs);
			closeStmt(stmt);
			pool.releaseConnection(conn);
		}
		return dailyRecons;
	}
	
	public ReconStat getReconStats(String id, NAME recName, RECON_FIELDS field, Date fromRunTime, Date toRunTime) throws DAOException{
		List<DailyRecon> recons = getDailyRecons(id, recName, field, fromRunTime, toRunTime);
		int max = 0, min = Integer.MAX_VALUE, avg = 0;
		int nonZeros = 0;
		for(DailyRecon recon: recons){
			//Do not include any zeros. This is to make sure that the numbers do not start looking low because of zeros
			if(recon.getCount() == 0)
				continue;
			++nonZeros;
			avg += recon.getCount();
			min = Math.min(min, recon.getCount());
			max = Math.max(max, recon.getCount());
		}
		if(nonZeros == 0)
			min = 0;
		if(nonZeros > 0)
			avg /= nonZeros;
		ReconStatBuilder b = new ReconStatBuilder();
		b.avg = avg;
		b.count = recons.size();
		b.field = field;
		b.max = max;
		b.min = min;
		return b.build();
	}
}
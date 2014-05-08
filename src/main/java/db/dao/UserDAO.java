package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import db.DbConnection;
import db.Queries;
import util.build.UserBuilder;
import entities.User;

public class UserDAO extends DataAccessObject{

	private final static Logger logger = Logger.getLogger(UserDAO.class);
	
	/*
	 * This checks if the email is already existing in the database
	 * or not
	 * @param emailId
	 * @throws SQLException
	 */
	public boolean isEmailIdExisting(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userCountStmt = null;
		ResultSet countRS = null;
		int count;
		emailId=emailId.toLowerCase();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userCountStmt = sqlConn.prepareStatement(Queries.GET_COUNT_EMAIL_ID);
			userCountStmt.setString(1, emailId);
			countRS = userCountStmt.executeQuery();
			if(countRS.next()){
				count = countRS.getInt(1);
				if(count>0)
					return true;
			}
			return false;
		}
		finally{
			closeRS(countRS);
			closeStmt(userCountStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * This checks if the email is registered or nor
	 * @param emailId
	 * @throws SQLException
	 */
	public boolean isEmailIdRegistered(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userCountStmt = null;
		ResultSet countRS = null;
		int count;
		emailId=emailId.toLowerCase();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userCountStmt = sqlConn.prepareStatement(Queries.GET_COUNT_REGISTERED_EMAIL_ID);
			userCountStmt.setString(1, emailId);
			countRS = userCountStmt.executeQuery();
			if(countRS.next()){
				count = countRS.getInt(1);
				if(count>0)
					return true;
			}
			return false;
		}
		finally{
			closeRS(countRS);
			closeStmt(userCountStmt);
			pool.releaseConnection(conn);
		}
	}
	
/*	public int getUniqueUserId() throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userStmt = null;
		ResultSet userRS = null;
		AtomicInteger userId = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userStmt = sqlConn.prepareStatement(Queries.GET_MAX_USER_ID);
			userRS = userStmt.executeQuery();
			if(userRS.next())
				userId = new AtomicInteger(userRS.getInt(1));
			
			if(userId != null)
				return userId.incrementAndGet();
			return 1;
		}
		finally{
			closeRS(userRS);
			closeStmt(userStmt);
			pool.releaseConnection(conn);
		}
	}
*/	
	public void addUser(User user) throws SQLException, DAOException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userStmt = null;

		try{
			if(isEmailIdExisting(user.getEmailId())){
				throw new DAOException("Email Id already exist in table");
			}
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userStmt = sqlConn.prepareStatement(Queries.INSERT_USER);
			int i=0;
			userStmt.setString(++i, user.getEmailId().toLowerCase());
			userStmt.setString(++i, user.getName().toLowerCase());
			userStmt.setString(++i, user.getPassword());
			userStmt.setString(++i, user.getCountry().toLowerCase());
			userStmt.setString(++i, user.getPhone());
			userStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			userStmt.setBoolean(++i, user.isActive());
			userStmt.setBoolean(++i, user.isNewsletter());
			userStmt.setBoolean(++i, user.isRegistered());
			userStmt.executeUpdate();
			logger.info("addUser: "+user);
		}
		finally{
			closeStmt(userStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public void updateUser(User user) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userStmt = sqlConn.prepareStatement(Queries.UPDATE_USER);
			
			int i=0;
			userStmt.setString(++i, user.getName().toLowerCase());
			userStmt.setString(++i, user.getPassword());
			userStmt.setString(++i, user.getCountry().toLowerCase());
			userStmt.setString(++i, user.getPhone());
			userStmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
			userStmt.setBoolean(++i, user.isActive());
			userStmt.setBoolean(++i, user.isNewsletter());
			userStmt.setBoolean(++i, user.isRegistered());
			userStmt.setString(++i, user.getEmailId().toLowerCase());
			userStmt.executeUpdate();
			logger.info("updateUser: "+user);
		}
		finally{
			closeStmt(userStmt);
			pool.releaseConnection(conn);
		}
	}
	
	public boolean verifyUser(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userStmt = null;
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userStmt = sqlConn.prepareStatement(Queries.VERIFY_USER);
			
			userStmt.setString(1, emailId.toLowerCase());
			int numUpdates = userStmt.executeUpdate();
			
			if(numUpdates == 1)
				return true;
			else
				return false;
		}
		finally{
			closeStmt(userStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * Returns user corresponding to the emailId if it exists
	 * else null
	 */
	public User getUser(String emailId) throws SQLException{
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement userStmt = null;
		ResultSet userRS = null;
		emailId = emailId.toLowerCase();
		
		try{
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			userStmt = sqlConn.prepareStatement(Queries.GET_USER);
			userStmt.setString(1, emailId);
			
			userRS = userStmt.executeQuery();
			if(userRS.next()){
				User user = getUser(userRS);
				return user;
			}
			return null;
		}
		finally{
			closeRS(userRS);
			closeStmt(userStmt);
			pool.releaseConnection(conn);
		}
	}
	
	/*
	 * Makes user from the resultSet and returns it
	 */
	public User getUser(ResultSet result) throws SQLException{
		UserBuilder b = new UserBuilder();
		b.userId = result.getInt(User.Columns.USER_ID);
		b.emailId = result.getString(User.Columns.EMAIL_ID);
		b.name = result.getString(User.Columns.NAME);
		b.password = result.getString(User.Columns.PASSWORD);
		b.country = result.getString(User.Columns.COUNTRY);
		b.phone = result.getString(User.Columns.PHONE);
		b.lastLoggedIn = result.getTimestamp(User.Columns.LAST_LOGGED_IN);
		b.active = result.getBoolean(User.Columns.ACTIVE);
		b.newsletter = result.getBoolean(User.Columns.NEWSLETTER);
		b.registered = result.getBoolean(User.Columns.REGISTERED);
		User user = b.build();
		logger.info("getUser: "+user);
		return user;
	}
}
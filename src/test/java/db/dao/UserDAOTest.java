package db.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import util.DateTimeUtils;
import util.build.UserBuilder;
import entities.User;

public class UserDAOTest {
	private final static Logger logger = Logger.getLogger(UserDAOTest.class);
	private ExpectedException excpetion = ExpectedException.none();
	UserDAO dao = new UserDAO();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUserDAO() throws SQLException{
		long uniqId = System.currentTimeMillis();
		try{
			logger.info("===============ADDING USER TO THE DATABASE===============");
			Date t0 = new Date();
			UserBuilder user1 = new UserBuilder();
			user1.emailId="test1@gmail.com"+uniqId;
			user1.name="user"+uniqId;
			user1.password="password1";
			user1.lastLoggedIn=t0;
			user1.active=false;
			user1.newsletter=false;
			user1.registered=true;
			User usr1=user1.build();
			dao.addUser(usr1);
			assertExpected("SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID='"+user1.emailId+"'", usr1);
			
			logger.info("===============UPDATING EXISTING USER IN THE DATABASE===============");
			user1.password="password_updated";
			usr1=user1.build();
			dao.updateUser(usr1);
			assertExpected("SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID='"+user1.emailId+"'", usr1);
			
			logger.info("===============GETTING USER FROM THE DATABASE===============");
			User usr2 = dao.getUser(user1.emailId);
			assertTrue(usr2.equals(usr1));
			assertExpected("SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID='"+user1.emailId+"'", usr2);
			
			logger.info("===============VERIFYING USER EXISTING IN THE DATABASE===============");
			boolean verification = dao.verifyUser(user1.emailId);
			User usr3 = usr2;
			usr3.setActive(true);
			assertTrue(verification);
			assertExpected("SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID='"+user1.emailId+"'", usr3);
			
			logger.info("===============ADDING ANOTHER USER TO THE DATABASE===============");
			Date t1 = new Date();
			UserBuilder user4 = new UserBuilder();
			user4.emailId="test2@gmail.com"+uniqId;
			user4.name="user"+uniqId;
			user4.password="password2";
			user4.lastLoggedIn=t1;
			user4.active=false;
			user4.newsletter=false;
			user4.registered=false;
			User usr4=user4.build();
			dao.addUser(usr4);
			assertExpected("SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID='"+user4.emailId+"'", usr4);
			
			logger.info("===============CHECKING IF USER EXISTING IN THE DATABASE===============");
			boolean existence = dao.isEmailIdExisting(user4.emailId);
			assertTrue(existence);
			
			existence = dao.isEmailIdExisting("test_not_existing@gmail.com");
			assertTrue(!existence);
			
			logger.info("===============CHECKING IF USER IS REGISTERED===============");
			boolean registeration = dao.isEmailIdRegistered(user4.emailId);
			assertTrue(!registeration);
			
			registeration = dao.isEmailIdRegistered(user1.emailId);
			assertTrue(registeration);
			
			logger.info("===============INSERTING EXISTING EMAIL ID INTO THE DATABASE===============");
			dao.addUser(usr1);
			fail("Expecting DAOException as duplicate user");
		}
		catch(DAOException e){
			logger.info("Got DAOException while adding inserting existing Email Id");
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			cleanUp(uniqId);
		}
	}
	
	private void assertExpected(String qryActual, User expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			User usr = dao.getUser(rs);
			assertTrue(usr.getEmailId().equals(expected.getEmailId()));
			assertTrue(usr.getName().equals(expected.getName()));
			assertTrue(usr.getPassword().equals(expected.getPassword()));
			assertTrue(usr.getCountry().equals(expected.getCountry()));
			assertTrue(usr.getPhone().equals(expected.getPhone()));
			assertTrue(Math.abs(DateTimeUtils.diff(usr.getLastLoggedIn(), expected.getLastLoggedIn(), TimeUnit.SECONDS)) <= 1);
			assertTrue(usr.isActive() == expected.isActive());
			assertTrue(usr.isNewsletter() == expected.isNewsletter());
			assertTrue(usr.isRegistered() == expected.isRegistered());
			assertTrue(!rs.next());
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
	
	private void cleanUp(long uniqId){
		ResultSet rs=null;
		try{
			logger.info("===============CLEANING UP THE DATABASE===============");
			DataAccessObject.executeUpdate("DELETE FROM PRODUCTS.USER WHERE NAME LIKE '%" + uniqId + "%'");
		}
		catch(Exception e){
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
}
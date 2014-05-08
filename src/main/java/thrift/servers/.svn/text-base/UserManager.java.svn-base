package thrift.servers;

import static thrift.genereated.user.userConstants.USER_SERVER_PORT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import org.apache.log4j.Logger;

import thrift.genereated.user.userConstants;
import thrift.genereated.user.UserThrift;
import thrift.genereated.user.UserService;
import thrift.genereated.user.UserException;

import db.dao.DAOException;
import db.dao.UserDAO;
import entities.User;
import util.ConfigParms;
import util.Emailer;
import util.build.UserBuilder;

public class UserManager implements UserService.Iface{
	private static final Logger logger = Logger.getLogger(UserManager.class);
	private static Integer port;
	private static final UserDAO dao = new UserDAO();
	public static final long TIME_INT = 30*24*60*60*1000L;//1 month
	//TODO: Have to implement methods such that able to transfer exceptions from JAVA to PHP
	
	public String makeHTMLEmail(User user){
		//TODO this link should be encrypted before sending the again should be decrypted before verification, this way user can't change emailId in the url
		StringBuilder link = new StringBuilder();
		link.append("http://69.136.251.135/aforprice/Testing/testThriftUserClient.php?action=verify&");
		link.append("emailId="+user.getEmailId());
		
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");		
		html.append("<h2>").append("User Details").append("</h2>");
		html.append("<table border=\"1\">");
		
		html.append("<tr>");
			html.append("<td>").append("Name").append("</td>");
			html.append("<td>").append(user.getName()).append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
			html.append("<td>").append("Email Id").append("</td>");
			html.append("<td>").append(user.getEmailId()).append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
			html.append("<td>").append("Verification Link").append("</td>");
			html.append("<td>").append("<a href='"+link.toString()+"'>LINK</a>").append("</td>");
		html.append("</tr>");		
		
		html.append("</table>").append("</body>").append("</html>");
		return html.toString();
	}

	public void sendVerificationEmail(User user) throws SQLException{
		logger.info("Sending the email alert to EmailId: "+user.getEmailId());
		
		String subject = "User Verification from Pricerite";
		String emailTemplate = makeHTMLEmail(user);
		List<String> to = new ArrayList<String>();
		to.add(user.getEmailId());
		
		if(ConfigParms.getInstance().isProduction())
			Emailer.getInstance().send(to, null, null, subject, emailTemplate, true);
	}
	
	@Override
	public boolean addUserThrift(UserThrift user) throws TException {
		try{
			User newUser = new UserBuilder().emailId(user.getEmailId()).name(user.getName()).password(user.getPassword()).country(user.getCountry()).
					phone(user.getPhone()).lastLoggedIn(new Date()).active(true).newsletter(user.isNewsletter()).registered(true).build();
			
			if(!dao.isEmailIdExisting(user.getEmailId()))
				dao.addUser(newUser);

			return true;
		} catch(SQLException e){
			logger.error(e.getMessage());
			throw new UserException(1, "SQL Exception thrown by java: addUserThrift");
		} catch(DAOException e){
			logger.error(e.getMessage());
			throw new UserException(2, "DAO Exception thrown by java: addUserThrift");
		}
	}

	@Override
	public boolean updateUserThrift(UserThrift user) throws TException {
		try{
			User updatedUser = new UserBuilder().emailId(user.getEmailId()).name(user.getName()).password(user.getPassword()).country(user.getCountry()).
					phone(user.getPhone()).lastLoggedIn(new Date()).active(user.isActive()).newsletter(user.isNewsletter()).registered(user.isRegistered()).build();
			dao.updateUser(updatedUser);
			return true;
		} catch(SQLException e){
			logger.error(e.getMessage());
			throw new UserException(1, "SQL Exception thrown by java: updateUserThrift");
		}
	}

	@Override
	public boolean loginUserThrift(String emailId, String password) throws TException {
		try{
			User user = dao.getUser(emailId);
			if(user == null)
				throw new UserException(4, "Email Id is not registered");
			
			if(user.getPassword().equals(password)){
				dao.updateUser(user);
				return true;
			} else{
				throw new UserException(5, "Incorrect Email Id or Password");
			}
		} catch(SQLException e){
			logger.error(e.getMessage());
			throw new UserException(1, "SQL Exception thrown by java: loginUserThrift");
		}
	}

	@Override
	public boolean verifyUserThrift(String emailId) throws TException {
		try{
			boolean verified = dao.verifyUser(emailId);
			return verified;
		}  catch(SQLException e){
			logger.error(e.getMessage());
			throw new UserException(1, "SQL Exception thrown by java: verifyUserThrift");
		}
	}
	
	@Override
	public UserThrift getUserThrift(String emailId) throws TException {
		try{
			if(!dao.isEmailIdExisting(emailId))
				throw new UserException(6, "Email Id does not exist");
			
			User user = dao.getUser(emailId);
			
			UserThrift thriftUser = new UserThrift();
			thriftUser.setEmailId(user.getEmailId());
			thriftUser.setName(user.getName());
			thriftUser.setPassword(null);
			thriftUser.setCountry(user.getCountry());
			thriftUser.setPhone(user.getPhone());
			thriftUser.setLastLoggedIn(user.getLastLoggedIn().toString());
			thriftUser.setActive(user.isActive());
			thriftUser.setNewsletter(user.isNewsletter());
			thriftUser.setRegistered(user.isRegistered());
			
			return thriftUser;
		} catch(SQLException e){
			logger.error(e.getMessage());
			throw new UserException(1, "SQL Exception thrown by java: getUserThrift");
		}
	}
	
	public static void main(String[] args){
		Integer port = -1;
		Integer instance = -1;
		if(args.length>=1){
			//args format key=value.. serverId=1 instance=1
			for(int i=0; i<args.length; i++){
				logger.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2)
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				String key = keyVal[0];
				if(key.equalsIgnoreCase("instance")){
					instance = Integer.parseInt(keyVal[1]);
					if( (port=USER_SERVER_PORT.get(instance-1)) == null)
						throw new IllegalArgumentException("instance "+instance+" is missing in USER_SERVER_PORT list");
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}//for(int i=0; i<args.length; i++){ ends...
			if( instance==-1)
				throw new IllegalArgumentException("missing argument instance");
			logger.info("Port="+port);
		}//if(args.length>=1) ends...
		
		try {
			TServerSocket serverTransport = new TServerSocket(port);
			UserService.Processor processor = new UserService.Processor(new UserManager());
	        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
	        System.out.println("Starting server on port: "+port);
	        server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}

package entities;

import java.util.Date;

import util.Constants;
import util.ToStringBuilder;
import util.build.UserBuilder;

public class User implements ToStringBuilder{

	private int userId = -1;
	private final String emailId;
	private final String name;
	private final String password;
	private final String country;	
	private final String phone;
	private Date lastLoggedIn;
	private boolean active;
	private boolean newsletter;
	private boolean registered;
	
	public User(UserBuilder builder){
		this.userId=builder.userId;
		this.emailId=builder.emailId;
		this.name=builder.name;
		this.password=builder.password;
		this.country=builder.country;
		this.phone=builder.phone;
		this.lastLoggedIn=builder.lastLoggedIn;
		this.active=builder.active;
		this.newsletter=builder.newsletter;
		this.registered=builder.registered;
	}
	
	public User(int userId, String emailId,
				String name, String password,
				String country, String phone, Date lastLoggedIn,
				boolean active, boolean newsletter,
				boolean registered){
		this.userId=userId;
		this.emailId=emailId;
		this.name=name;
		this.password=password;
		this.country=country;
		this.phone=phone;
		this.lastLoggedIn=lastLoggedIn;
		this.active=active;
		this.newsletter=newsletter;
		this.registered=registered;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getPhone() {
		return phone!=null?phone:"";
	}
	
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}
	
	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isNewsletter() {
		return newsletter;
	}
	
	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	public String getEmailId() {
		return emailId;
	}
	
	public String getName() {
		return name!=null?name:"";
	}
	
	public String getPassword() {
		return password!=null?password:"";
	}
	
	public String getCountry() {
		return country!=null?country:"";
	}
	
	@Override
	public String toString(){
		return toString(new StringBuilder());
	}
	
	@Override
	public boolean equals(Object obj){
		try{
			User that = (User)obj;
			if(this==null||that==null)
				return false;
			
			if(this.emailId.equals(that.emailId))
				return true;
			else
				return false;
		}
		catch(Exception e){
			return false;
		}
	}
	
	@Override
	public String toString(StringBuilder sb) {
		sb.append(userId).append(Constants.USERS_ATTR_COL_SEP)
		.append(emailId).append(Constants.USERS_ATTR_COL_SEP)
		.append(name).append(Constants.USERS_ATTR_COL_SEP)
		.append(password).append(Constants.USERS_ATTR_COL_SEP)
		.append(country).append(Constants.USERS_ATTR_COL_SEP)
		.append(phone).append(Constants.USERS_ATTR_COL_SEP)
		.append(lastLoggedIn).append(Constants.USERS_ATTR_COL_SEP)
		.append(active).append(Constants.USERS_ATTR_COL_SEP)
		.append(newsletter).append(Constants.USERS_ATTR_COL_SEP)
		.append(registered);
		return sb.toString();
	}
	
	public static final class Columns{
		public static final String USER_ID = "USER_ID";
		public static final String EMAIL_ID = "EMAIL_ID";
		public static final String NAME = "NAME";
		public static final String PASSWORD = "PASSWORD";
		public static final String COUNTRY = "COUNTRY";
		public static final String PHONE = "PHONE";
		public static final String LAST_LOGGED_IN = "LAST_LOGGED_IN";
		public static final String ACTIVE = "ACTIVE";		
		public static final String NEWSLETTER = "NEWSLETTER";		
		public static final String REGISTERED = "REGISTERED";
	}
}
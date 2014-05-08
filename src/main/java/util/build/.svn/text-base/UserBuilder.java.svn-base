package util.build;

import java.util.Date;

import entities.User;

public class UserBuilder extends ObjectBuilder<User>{

	public int userId = -1;
	public String emailId;
	public String name;
	public String password;
	public String country;	
	public String phone;
	public Date lastLoggedIn;
	public boolean active;
	public boolean newsletter;
	public boolean registered;
	
	public UserBuilder(){
	}
	
	@Override
	public User build() {
		return new User(this);
	}
	
	public UserBuilder userId(int userId){
		this.userId=userId;
		return this;
	}

	public UserBuilder emailId(String emailId){
		this.emailId=emailId;
		return this;
	}
	
	public UserBuilder name(String name){
		this.name=name;
		return this;
	}
	
	public UserBuilder password(String password){
		this.password=password;
		return this;
	}
	
	public UserBuilder country(String country){
		this.country=country;
		return this;
	}
	
	public UserBuilder phone(String phone){
		this.phone=phone;
		return this;
	}
	
	public UserBuilder lastLoggedIn(Date lastLoggedIn){
		this.lastLoggedIn=lastLoggedIn;
		return this;
	}
	
	public UserBuilder active(boolean active){
		this.active=active;
		return this;
	}
	
	public UserBuilder newsletter(boolean newsletter){
		this.newsletter=newsletter;
		return this;
	}
	
	public UserBuilder registered(boolean registered){
		this.registered=registered;
		return this;
	}
}

package org.openhab.action.carddav.internal;

public class CarddavConfiguration {
	private String address;
	private String username;
	private String password;
	
	public CarddavConfiguration() {
		super();
	}
	public CarddavConfiguration(String address, String username, String password) {
		super();
		this.address = address;
		this.username = username;
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

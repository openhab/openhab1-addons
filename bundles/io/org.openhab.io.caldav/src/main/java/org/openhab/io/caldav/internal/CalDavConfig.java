package org.openhab.io.caldav.internal;

public class CalDavConfig {
	private String key;
	private String username;
	private String password;
	private String url;
	private int reloadMinutes;
	private int preloadMinutes;
	
	public CalDavConfig() {
		super();
	}

	public CalDavConfig(String key, String username, String password, String url, int reloadMinutes) {
		super();
		this.key = key;
		this.username = username;
		this.password = password;
		this.url = url;
		this.reloadMinutes = reloadMinutes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getReloadMinutes() {
		return reloadMinutes;
	}

	public void setReloadMinutes(int reloadMinutes) {
		this.reloadMinutes = reloadMinutes;
	}

	public int getPreloadMinutes() {
		return preloadMinutes;
	}

	public void setPreloadMinutes(int preloadMinutes) {
		this.preloadMinutes = preloadMinutes;
	}
	
	
}

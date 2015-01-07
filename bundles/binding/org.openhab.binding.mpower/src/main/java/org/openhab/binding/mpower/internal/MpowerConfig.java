package org.openhab.binding.mpower.internal;

/**
 * Ubiquiti mPower strip binding
 * 
 * this class reflects the settings within openhab.cfg
 * 
 * @author magcode
 */
public class MpowerConfig {
	private String id;
	private String user;
	private String host;
	private String password;
	private boolean secure;
	private long refreshInterval;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
}

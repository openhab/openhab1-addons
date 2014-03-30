package org.openhab.binding.daikin.internal;

public class DaikinHost {
	private final String id;
	
	private String host;
	private long refresh;

	public DaikinHost(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getRefresh() {
		return refresh;
	}

	public void setRefresh(long refresh) {
		this.refresh = refresh;
	}	
}

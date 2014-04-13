/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.internal;

/**
 * Connection properties for an XBMC instance
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public class XbmcHost {
	
	private String hostname = "127.0.0.1";
	
	private String username = "xbmc";
	private String password = "xbmc";
	
	private int rsPort = 8080;
	private int wsPort = 9090;
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return rsPort;
	}
	
	public int getWSPort() {
		return wsPort;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRsPort(int rsPort) {
		this.rsPort = rsPort;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setWsPort(int wsPort) {
		this.wsPort = wsPort;
	}
}

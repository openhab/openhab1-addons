/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

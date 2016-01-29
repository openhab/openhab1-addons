/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

/**
 * Information about a connect.
 * 
 * @author Heinz
 *
 */
public class ConnectionInformation {

	private String username;
	private String password;

	private String url;
	private int port;

	public ConnectionInformation(String user, String pw, String target,
			int portnum) {
		username = user;
		password = pw;
		url = target;
		port = portnum;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public int getPort() {
		return port;
	}

}

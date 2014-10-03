/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Holds a OpenWebIf receiver configuration from the openhab.cfg.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class OpenWebIfConfig {
	private String name;
	private String host;
	private int port;
	private boolean https;
	private String user;
	private String password;

	/**
	 * Returns the name of the receiver.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the receiver.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the host of the receiver.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host of the receiver.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns the port of the receiver.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port of the receiver.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Returns true, if a https connection is required.
	 */
	public boolean isHttps() {
		return https;
	}

	/**
	 * Sets if a https connection is required.
	 */
	public void setHttps(boolean https) {
		this.https = https;
	}

	/**
	 * Returns the user to connect to the receiver.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user to connect to the receiver.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the password to connect to the receiver.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password to connect to the receiver.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns true, if a user and a password is set.
	 */
	public boolean hasLogin() {
		return user != null && password != null;
	}

	/**
	 * Returns true, if this config is valid.
	 */
	public boolean isValid() {
		return host != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("host", host)
				.append("port", port).append("https", https).append("user", user)
				.append("password", password == null ? "no" : "yes").toString();
	}
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Simple info object to hold server specific informations.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ServerId {
	private String name;
	private String version;
	private String address;

	public ServerId(String serverName) {
		if (StringUtils.equalsIgnoreCase(serverName, "Homegear")) {
			name = "Homegear";
		} else {
			name = "CCU";
		}
	}

	/**
	 * Returns the name of the server.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the version of the server.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version of the server.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the address of the Homematic server.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of the Homematic server.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Returns true, if the server is a Homegear server.
	 */
	public boolean isHomegear() {
		return "homegear".equalsIgnoreCase(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append(
				"version", StringUtils.defaultIfBlank(version, "unknown"));
		if (address != null) {
			tsb.append("address", address);
		}
		return tsb.toString();
	}
}

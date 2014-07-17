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

	public ServerId(String versionString) {
		String[] parts = StringUtils.split(versionString);
		if (parts != null && parts.length == 2) {
			name = parts[0];
			version = parts[1];
		} else {
			name = "CCU";
			version = versionString;
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
	 * Returns true, if the server is a Homegear server.
	 */
	public boolean isHomegear() {
		return "homegear".equalsIgnoreCase(name);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name)
				.append("version", version).toString();
	}

}

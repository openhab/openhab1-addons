/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

import java.util.List;

import org.openhab.binding.stiebelheatpump.protocol.Request;

/**
 * Config locator class. This class located the configuration file in the
 * resources and converts it into a list of requests
 * 
 * @author Peter Kreutzer
 */
public class ConfigLocator {

	private String file;
	private ConfigParser configParser = new ConfigParser();

	public ConfigLocator() {
	}

	/**
	 * @param file
	 *            that shall be located in the resources the file shall contain
	 *            the configuration of the specific request to the firmware
	 *            version naming convention shall be "version.xml" , e.g.
	 *            2.06.xml
	 */
	public ConfigLocator(String file) {
		this.file = file;
	}

	/**
	 * Searches for the given files in the class path.
	 * 
	 * @return All found Configurations
	 */
	public List<Request> getConfig() {
		System.out.println("Loading heat pump configuration file for " + file);
		List<Request> config = configParser.parseConfig(file);
		return config;
	}
}

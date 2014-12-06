/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
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

/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.bluetooth.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes the configuration information from the main config file.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class BluetoothConfig implements ManagedService {
	
	// the namespace for configuration entries
	private static final String SERVICE_PID = "bluetooth";
	
	// allows the user to define a refresh rate
	private static final String REFRESH_RATE = "refresh";

	private static final Logger logger = LoggerFactory.getLogger(BluetoothConfig.class);

	/** the frequency of how often a new Bluetooth device scan is triggered in seconds */
	static public int refreshRate = 30;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			String value = (String) config.get(REFRESH_RATE);
			try {
				int newRefreshRate = Integer.parseInt(value);
				refreshRate = newRefreshRate;
			} catch(NumberFormatException e) {
				logger.warn("Invalid configuration value '{}' for parameter '" + SERVICE_PID + ":" + REFRESH_RATE+ "'", value);
			}
		}
	}

}

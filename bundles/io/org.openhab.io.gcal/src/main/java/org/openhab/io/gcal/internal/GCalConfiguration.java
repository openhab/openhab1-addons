/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.io.gcal.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Configuration class which implements {@link ManagedService} to act as a central
 * handler for configuration issues. It holds the current configuration values
 * and gives acces through static member fields.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalConfiguration implements ManagedService {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(GCalConfiguration.class);

	public static String username = "";
	public static String password = "";
	public static String url = "";
	
	/** holds the current refresh interval, default to 900000ms (15 minutes) */
	public static int refreshInterval = 900000;
	
	/** the offset (in days) which will used to store future events */
	public static int offset = 14;
	
	/**
	 * the base script which is written to the newly created Calendar-Events by
	 * the GCal based presence simulation. It must contain two format markers
	 * <code>%s</code>. The first marker represents the Item to send the command
	 * to and the second represents the State.
	 */
	public static String executeScript = 
		"> if (PresenceSimulation.state == ON) sendCommand(%s,%s)";
	
	private static boolean initialized = false;
	
	
	public static boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String usernameString = (String) config.get("username");
			username = usernameString;
			if (StringUtils.isBlank(username)) {
				throw new ConfigurationException("gcal:username", "username must not be blank - please configure an aproppriate username in openhab.cfg");
			}

			String passwordString = (String) config.get("password");
			password = passwordString;
			if (StringUtils.isBlank(password)) {
				throw new ConfigurationException("gcal:password", "password must not be blank - please configure an aproppriate password in openhab.cfg");
			}

			String urlString = (String) config.get("url");
			url = urlString;
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException("gcal:url", "url must not be blank - please configure an aproppriate url in openhab.cfg");
			}
			
			String refreshString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshString)) {
				refreshInterval = Integer.parseInt(refreshString);
			}
			
			String offsetString = (String) config.get("offset");
			if (StringUtils.isNotBlank(offsetString)) {
				try {
					offset = Integer.valueOf(offsetString);
				}
				catch (IllegalArgumentException iae) {
					logger.warn("couldn't parse '{}' to an integer");
				}
			}
			
			String executeScriptString = (String) config.get("executescript");
			if (StringUtils.isNotBlank(executeScriptString)) {
				executeScript = executeScriptString;
			}
			
			initialized = true;
		}
	}
	
	
}

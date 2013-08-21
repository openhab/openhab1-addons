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
package org.openhab.action.nma.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the NotifyMyAndroid action.
 * 
 * @author Till Klocke
 * @since 1.3.0
 */
public class NotifyMyAndroidActionService implements ActionService, ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(NotifyMyAndroidActionService.class);

	/**
	 * Developer key (optional). See http://www.notifymyandroid.com/api.jsp
	 */
	private final static String PARAM_KEY_DEV_API_KEY = "developerKey";
	/**
	 * Name of the sending app (optional). Defaults to 'openHAB'
	 */
	private final static String PARAM_KEY_APP_NAME = "appName";
	/**
	 * Timeout in milliseconds for the connection to notifymyandroid.com
	 * (optional). Defaults to 10 seconds
	 */
	private final static String PARAM_KEY_TIMEOUT = "timeout";
	/**
	 * Default apiKey (account id) to use for sending message if null or not
	 * specified in the command. (optional)
	 */
	private final static String PARAM_KEY_API_KEY = "apiKey";
	/**
	 * Priority to use for messages if not specified otherwise (optional).
	 * Defaults to 0. Valid values range from -2 to 2.
	 */
	private final static String PARAM_KEY_DEFAULT_PRIORITY = "defaultPriority";
	/**
	 * Url to attach to the message if not specified in the command (optional).
	 * This can be used to trigger actions on the device
	 */
	private final static String PARAM_KEY_DEFAULT_URL = "defaultUrl";

	private final static String REASON_CANT_PARSE_NUMBER = "Can't parse number";

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the action
	 * methods before executing code.
	 */
	/* default */static boolean isProperlyConfigured = false;

	public NotifyMyAndroidActionService() {
	}

	public void activate() {
		logger.debug("NotifyMyAndroid action service activated");
	}

	public void deactivate() {
		logger.debug("NotifyMyAndroid action service deactivated");
	}

	@Override
	public String getActionClassName() {
		return NotifyMyAndroid.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return NotifyMyAndroid.class;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Updating config");
		if (config != null) {
			logger.debug("Received new config");
			NotifyMyAndroid.developerKey = (String) config.get(PARAM_KEY_DEV_API_KEY);
			NotifyMyAndroid.appName = (String) config.get(PARAM_KEY_APP_NAME);
			
			try {
				NotifyMyAndroid.timeout = Integer.parseInt((String) config.get(PARAM_KEY_TIMEOUT));
			} catch (NumberFormatException e) {
				throw new ConfigurationException(PARAM_KEY_DEFAULT_PRIORITY, REASON_CANT_PARSE_NUMBER, e);
			}
			NotifyMyAndroid.apiKey = (String) config.get(PARAM_KEY_API_KEY);
			
			try {
				NotifyMyAndroid.defaultPriotiy = Integer.parseInt((String) config.get(PARAM_KEY_DEFAULT_PRIORITY));
			} catch (NumberFormatException e) {
				throw new ConfigurationException(PARAM_KEY_DEFAULT_PRIORITY, REASON_CANT_PARSE_NUMBER, e);
			}
			
			NotifyMyAndroid.defaultUrl = (String) config.get(PARAM_KEY_DEFAULT_URL);
		} else {
			// We don't need necessarily any config
			logger.warn("Dictionary was NULL, didn't read any configuration");
		}
		isProperlyConfigured = true;
	}
	
}

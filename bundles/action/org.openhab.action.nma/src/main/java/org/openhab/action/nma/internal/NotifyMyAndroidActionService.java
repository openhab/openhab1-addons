/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.nma.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
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
			
			String appName = (String) config.get(PARAM_KEY_APP_NAME);
			if (!StringUtils.isEmpty(appName)) {
				NotifyMyAndroid.appName = appName;
			}
			
			String timeOut = (String) config.get(PARAM_KEY_TIMEOUT);
			if (!StringUtils.isEmpty(timeOut)) {
				try {
					NotifyMyAndroid.timeout = Integer.parseInt((String) config.get(PARAM_KEY_TIMEOUT));
				} catch (NumberFormatException e) {
					logger.warn("Can't parse the timeout value, falling back to default value");
				}
			}
			
			NotifyMyAndroid.apiKey = (String) config.get(PARAM_KEY_API_KEY);
			
			String defaultPriority = (String) config.get(PARAM_KEY_DEFAULT_PRIORITY);
			if (!StringUtils.isEmpty(defaultPriority)) {
				try {
					NotifyMyAndroid.defaultPriotiy = Integer.parseInt(
							(String) config.get(PARAM_KEY_DEFAULT_PRIORITY));
				} catch (NumberFormatException e) {
					logger.warn("Can't parse the default priority value, falling back to default value");
				}
			}
			
			NotifyMyAndroid.defaultUrl = (String) config.get(PARAM_KEY_DEFAULT_URL);
		} else {
			// We don't need necessarily any config
			logger.debug("Dictionary was NULL, didn't read any configuration");
		}
		
		isProperlyConfigured = true;
	}
	
}

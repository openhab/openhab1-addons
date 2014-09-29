/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushover.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Pushover action. Pushover is
 * a web based service that allows pushing of messages to mobile devices.
 * 
 * @author Chris Graham
 * @since 1.5.0
 */
public class PushoverActionService implements ActionService, ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(PushoverActionService.class);

	/**
	 * Your applications API token. See https://pushover.net/api
	 */
	private final static String PARAM_KEY_API_KEY = "defaultToken";
	/**
	 * The user/group key (not e-mail address) of your user (or you).
	 */
	private final static String PARAM_KEY_USER = "defaultUser";
	/**
	 * Name of the sending app (optional). Defaults to 'openHAB'
	 */
	private final static String PARAM_KEY_TITLE = "defaultTitle";
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
	 * Url title to attach to the message if not specified in the command (optional).
	 * This can be used to trigger actions on the device
	 */
	private final static String PARAM_KEY_DEFAULT_URL_TITLE = "defaultUrlTitle";

	/**
	 * Timeout in milliseconds for the connection to pushover.net
	 * (optional). Defaults to 10 seconds
	 */
	private final static String PARAM_KEY_TIMEOUT = "defaultTimeout";
	
	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the action
	 * methods before executing code.
	 */
	static boolean isProperlyConfigured = false;
	
	public PushoverActionService() {
		// nothing to do
	}
	
	public void activate() {
		logger.debug("Pushover action service activated");
	}

	public void deactivate() {
		logger.debug("Pushover action service deactivated");
	}
	
	@Override
	public String getActionClassName() {
		return Pushover.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Pushover.class;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Configuration file is being parsed.");
		if (config != null) {
			logger.debug("Configuration data exists. Parsing the paramaters.");
			
			String apiKey = (String) config.get(PARAM_KEY_API_KEY);
			if (!StringUtils.isEmpty(apiKey)) {
				Pushover.defaultApiKey = apiKey;
			}
			
			String user = (String) config.get(PARAM_KEY_USER);
			if (!StringUtils.isEmpty(user)) {
				Pushover.defaultUser = user;
			}

			String title = (String) config.get(PARAM_KEY_TITLE);
			if (!StringUtils.isEmpty(title)) {
				Pushover.defaultTitle = title;
			}

			String url = (String) config.get(PARAM_KEY_DEFAULT_URL);
			if (!StringUtils.isEmpty(url)) {
				Pushover.defaultUrl = url;
			}
			
			String urlTitle = (String) config.get(PARAM_KEY_DEFAULT_URL_TITLE);
			if (!StringUtils.isEmpty(urlTitle)) {
				Pushover.defaultUrlTitle = urlTitle;
			}

			String defaultPriority = (String) config.get(PARAM_KEY_DEFAULT_PRIORITY);
			if (!StringUtils.isEmpty(defaultPriority)) {
				try {
					Pushover.defaultPriority = Integer.parseInt((String) config.get(PARAM_KEY_DEFAULT_PRIORITY));
				} catch (NumberFormatException e) {
					logger.warn("Can't parse the default priority value, falling back to default value.");
				}
			}
			
			String timeOut = (String) config.get(PARAM_KEY_TIMEOUT);
			
			if (!StringUtils.isEmpty(timeOut)) {
				try {
					Pushover.timeout = Integer.parseInt((String) config.get(PARAM_KEY_TIMEOUT));
				} catch (NumberFormatException e) {
					logger.warn("Can't parse the timeout value, falling back to default value");
				}
			}
		} else {
			// Messages can be sent by providing API Key and User key in the action binding, so no issue here.
			logger.debug("The configurations information was empty. No defaults for Pushover loaded.");
		}
		
		isProperlyConfigured = true;
	}
	
}

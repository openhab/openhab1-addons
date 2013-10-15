/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.squeezebox.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Squeezebox action.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class SqueezeboxActionService implements ActionService, ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(SqueezeboxActionService.class);

	// CRIT: copied from SqueezeboxBinding - should be somewhere common
	private final static int DEFAULT_CLI_PORT = 9090;
	
	/** RegEx to validate SqueezeServer config <code>'^(squeeze:)(host|cliport|webport)=.+$'</code> */
	private static final Pattern EXTRACT_SERVER_CONFIG_PATTERN = Pattern.compile("^(server)\\.(host|cliport|webport)$");
	
	/** RegEx to validate a mpdPlayer config <code>'^(.*?)\\.(id)$'</code> */
	private static final Pattern EXTRACT_PLAYER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(id)$");
	
	public void activate() {
		logger.debug("Squeezebox action service activated");
	}

	public void deactivate() {
		logger.debug("Squeezebox action service deactivated");
	}
	
	@Override
	public String getActionClassName() {
		return Squeezebox.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Squeezebox.class;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			logger.debug("Received new config");
			Squeezebox.disconnect();
			
			String host = null;
			int port = DEFAULT_CLI_PORT;
			
			Map<String, String> playerMap = new HashMap<String, String>();
			
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				
				String key = (String) keys.nextElement();
				
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				
				Matcher serverMatcher = EXTRACT_SERVER_CONFIG_PATTERN.matcher(key);
				Matcher playerMatcher = EXTRACT_PLAYER_CONFIG_PATTERN.matcher(key);
				
				String value = (String) config.get(key);
				
				if (serverMatcher.matches()) {	
					serverMatcher.reset();
					serverMatcher.find();
					
					if ("host".equals(serverMatcher.group(2))) {
						host = value;
					}
					else if ("cliport".equals(serverMatcher.group(2))) {
						port = Integer.valueOf(value);
					}
				} else if (playerMatcher.matches()) {
					// NOTE: we key on playerId - opposite to the config parsing in SqueezeboxBinding
					playerMap.put(playerMatcher.group(1), value.toString());
				}
			}
			
			if (StringUtils.isEmpty(host))
				throw new ConfigurationException("host", "No SqueezeServer host is specified - this property is mandatory");
			
			Squeezebox.connect(host, port, playerMap);
		} else {
			logger.error("Config was NULL");
		}
	}
}

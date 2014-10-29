/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.io.multimedia.tts.TTSService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a pure Java TTS service implementation, based on SpeechDispatcherTTS.
 * 
 * @author Gaël L'hopital
 * @since 1.6.0
 * 
 */
public class TTSServiceSpeechDispatcherTTS implements TTSService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(TTSServiceSpeechDispatcherTTS.class);
	
	SpeechDispatcherConnection defaultOutput = null;
	
	/** Map table to store all available speech dispatchers configured by the user */
	protected Map<String, SpeechDispatcherConnection> deviceConfigCache = null;
	
	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port)$");
	
	public void activate() {		
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		closeAllConnections();
	}

	private void closeAllConnections() {
		 for (Entry<String, SpeechDispatcherConnection> deviceConfig : deviceConfigCache.entrySet() ) {
			 SpeechDispatcherConnection connection = deviceConfig.getValue();
			 connection.closeConnection();
		 }
	}

	/**
	 * {@inheritDoc}
	 */
	public void say(String text, String voiceName, String outputDevice) {
		SpeechDispatcherConnection connection = null;
		if (outputDevice != null) {
			connection = deviceConfigCache.get(outputDevice);
		} else {
			connection = defaultOutput;
		}
		
		if (connection != null) {
			connection.say(text, voiceName);	
		} else {
			logger.error("Output device not configured [{}]", outputDevice);
		}
		
	}

	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		if (properties != null) {
			Enumeration<String> keys = properties.keys();
			
			if ( deviceConfigCache == null ) {
				deviceConfigCache = new HashMap<String, SpeechDispatcherConnection>();
			}
			
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the property-key enumeration can contain additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key) || "os".equals(key)) {
					continue;
				}
				
				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("Given config key '" + key + "' does not follow the expected pattern '<id>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				SpeechDispatcherConnection deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new SpeechDispatcherConnection();
					deviceConfigCache.put(deviceId, deviceConfig);
					defaultOutput = deviceConfig;
				}

				String configKey = matcher.group(2);
				String value = (String) properties.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = value;
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
		}
	}
}



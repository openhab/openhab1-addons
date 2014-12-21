/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal;

import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.openhab.action.openwebif.internal.impl.config.OpenWebIfConfig;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the OpenWebIf action.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class OpenWebIfActionService implements ActionService, ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(OpenWebIfActionService.class);

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getActionClassName() {
		return OpenWebIf.class.getCanonicalName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getActionClass() {
		return OpenWebIf.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties != null) {
			Enumeration<String> keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				String value = StringUtils.trimToNull((String) properties.get(key));
				if (StringUtils.startsWithIgnoreCase(key, "receiver")) {
					parseConfig(key, value);
				}
			}

			for (OpenWebIfConfig config : OpenWebIf.getConfigs().values()) {
				if (!config.isValid()) {
					throw new ConfigurationException("openwebif", "Invalid OpenWebIf receiver configuration: "
							+ config.toString());
				}
				logger.info("{}", config.toString());
			}
		}
	}

	/**
	 * Parses the properties for a OpenWebIfConfig.
	 */
	private void parseConfig(String key, String value) throws ConfigurationException {
		if (value == null) {
			throw new ConfigurationException("openwebif", "Empty property '" + key
					+ "', please check your openhab.cfg!");
		}

		String receiverName = StringUtils.substringBetween(key, ".");
		if (receiverName == null) {
			throw new ConfigurationException("openwebif", "Malformed receiver property '" + key
					+ "', please check your openhab.cfg!");
		}

		OpenWebIfConfig rc = OpenWebIf.getConfigs().get(receiverName);
		if (rc == null) {
			rc = new OpenWebIfConfig();
			rc.setName(receiverName);
			OpenWebIf.getConfigs().put(receiverName, rc);
		}

		String keyId = StringUtils.substringAfterLast(key, ".");
		if (StringUtils.equalsIgnoreCase(keyId, "host")) {
			rc.setHost(value);
		} else if (StringUtils.equalsIgnoreCase(keyId, "port")) {
			rc.setPort(parseNumber(key, value).intValue());
		} else if (StringUtils.equalsIgnoreCase(keyId, "user")) {
			rc.setUser(value);
		} else if (StringUtils.equalsIgnoreCase(keyId, "password")) {
			rc.setPassword(value);
		} else if (StringUtils.equalsIgnoreCase(keyId, "https")) {
			rc.setHttps(Boolean.parseBoolean(value));
		} else {
			throw new ConfigurationException("openwebif", "Unknown configuration key '" + key
					+ "', please check your openhab.cfg!");
		}
	}

	/**
	 * Parse a double value from a string.
	 */
	private Double parseNumber(String key, String value) throws ConfigurationException {
		try {
			return Double.parseDouble(value);
		} catch (Exception ex) {
			throw new ConfigurationException("openwebif", "Parameter '" + key
					+ "' empty or in wrong format, please check your openhab.cfg!");
		}
	}
}

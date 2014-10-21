/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.utils.PropertyResolver;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses the config in openhab.cfg.
 * 
 * <pre>
 * ############################## Weather Binding ##############################
 * #
 * # The apikey for the different weather providers, at least one must be specified
 * # Note: Hamweather requires two apikeys: client_id=apikey, client_secret=apikey2
 * #weather:apikey.ForecastIo=
 * #weather:apikey.OpenWeatherMap=
 * #weather:apikey.WorldWeatherOnline=
 * #weather:apikey.Wunderground=
 * #weather:apikey.Hamweather=
 * #weather:apikey2.Hamweather=
 * 
 * # location configuration, you can specify multiple locations
 * #weather:location.<locationId1>.latitude=
 * #weather:location.<locationId1>.longitude=
 * #weather:location.<locationId1>.provider=
 * #weather:location.<locationId1>.language=
 * #weather:location.<locationId1>.updateInterval=
 * 
 * #weather:location.<locationId2>.latitude=
 * #weather:location.<locationId2>.longitude=
 * #weather:location.<locationId2>.provider=
 * #weather:location.<locationId2>.language=
 * #weather:location.<locationId2>.updateInterval=
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherConfig {
	private static final Logger logger = LoggerFactory.getLogger(WeatherConfig.class);

	private Map<ProviderName, ProviderConfig> providerConfigs = new HashMap<ProviderName, ProviderConfig>();
	private Map<String, LocationConfig> locationConfigs = new HashMap<String, LocationConfig>();

	private boolean valid = false;

	/**
	 * Parses and validates the properties in openhab.cfg.
	 */
	public void parse(Dictionary<String, ?> properties) throws ConfigurationException {
		valid = false;
		if (properties != null) {
			Enumeration<String> keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				String value = StringUtils.trimToNull((String) properties.get(key));
				if (StringUtils.startsWithIgnoreCase(key, "apikey")) {
					parseApiKey(key, value);
				} else if (StringUtils.startsWithIgnoreCase(key, "location")) {
					parseLocation(key, value);
				}
			}

			// check all LocationConfigs
			for (LocationConfig lc : locationConfigs.values()) {
				if (!lc.isValid()) {
					throw new ConfigurationException("weather", "Incomplete location config for locationId '"
							+ lc.getLocationId() + "', please check your openhab.cfg!");
				}
				if (lc.getProviderName() != ProviderName.YAHOO && !providerConfigs.containsKey(lc.getProviderName())) {
					throw new ConfigurationException("weather", "No apikey found for provider '" + lc.getProviderName()
							+ "', please check your openhab.cfg!");
				}
			}

			// check all ProviderConfigs
			for (ProviderConfig pc : providerConfigs.values()) {
				if (!pc.isValid()) {
					throw new ConfigurationException("weather", "Invalid apikey config for provider '"
							+ pc.getProviderName() + "', please check your openhab.cfg!");
				}
			}

			valid = locationConfigs.size() > 0;
		}
	}

	/**
	 * Parses the properties for a location config.
	 */
	private void parseLocation(String key, String value) throws ConfigurationException {
		if (value == null) {
			throw new ConfigurationException("weather", "Empty property '" + key + "', please check your openhab.cfg!");
		}

		String locationId = StringUtils.substringBetween(key, ".");
		if (locationId == null) {
			throw new ConfigurationException("weather", "Malformed location property '" + key
					+ "', please check your openhab.cfg!");
		}

		LocationConfig lc = locationConfigs.get(locationId);
		if (lc == null) {
			lc = new LocationConfig();
			lc.setLocationId(locationId);
			locationConfigs.put(locationId, lc);
		}

		String keyId = PropertyResolver.last(key);
		if (StringUtils.equalsIgnoreCase(keyId, "provider")) {
			lc.setProviderName(getProviderName(value));
		} else if (StringUtils.equalsIgnoreCase(keyId, "updateInterval")) {
			lc.setUpdateInterval(parseNumber(key, value).intValue());
		} else if (StringUtils.equalsIgnoreCase(keyId, "latitude")) {
			lc.setLatitude(parseNumber(key, value));
		} else if (StringUtils.equalsIgnoreCase(keyId, "longitude")) {
			lc.setLongitude(parseNumber(key, value));
		} else if (StringUtils.equalsIgnoreCase(keyId, "language")) {
			lc.setLanguage(value);
		} else if (StringUtils.equalsIgnoreCase(keyId, "name")) {
			lc.setName(value);
		} else {
			throw new ConfigurationException("weather", "Unknown configuration key '" + key
					+ "', please check your openhab.cfg!");
		}
	}

	/**
	 * Parses the properties for a provider config.
	 */
	private void parseApiKey(String key, String value) throws ConfigurationException {
		String provider = PropertyResolver.last(key);
		ProviderName providerName = getProviderName(provider);

		ProviderConfig pConfig = providerConfigs.get(providerName);
		if (pConfig == null) {
			pConfig = new ProviderConfig();
			pConfig.setProviderName(providerName);
			providerConfigs.put(providerName, pConfig);
		}

		String keyId = PropertyResolver.first(key);
		if (StringUtils.equalsIgnoreCase(keyId, "apikey")) {
			pConfig.setApiKey(value);
		} else if (StringUtils.equalsIgnoreCase(keyId, "apikey2")) {
			pConfig.setApiKey2(value);
		} else {
			throw new ConfigurationException("weather", "Unknown configuration key '" + key
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
			throw new ConfigurationException("weather", "Parameter '" + key
					+ "' empty or in wrong format, please check your openhab.cfg!");
		}
	}

	/**
	 * Parse a ProviderName from a string.
	 */
	private ProviderName getProviderName(String name) throws ConfigurationException {
		ProviderName providerName = ProviderName.parse(name);
		if (providerName == null) {
			throw new ConfigurationException("weather", "Provider with name '" + name
					+ "' not found, please check your openhab.cfg!");
		}
		return providerName;
	}

	/**
	 * Returns the LocationConfig for the specified locationId.
	 */
	public LocationConfig getLocationConfig(String locationId) {
		return locationConfigs.get(locationId);
	}

	/**
	 * Returns all LocationConfigurations.
	 */
	public Collection<LocationConfig> getAllLocationConfigs() {
		return locationConfigs.values();
	}

	/**
	 * Returns the ProviderConfig for the specified providerName.
	 */
	public ProviderConfig getProviderConfig(ProviderName providerName) {
		return providerConfigs.get(providerName);
	}

	/**
	 * Returns true, if all configurations are valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Dumps the config to the log.
	 */
	public void dump() {
		for (ProviderName providerName : providerConfigs.keySet()) {
			logger.info("{}", providerConfigs.get(providerName));
		}

		for (String locationId : locationConfigs.keySet()) {
			logger.info("{}", locationConfigs.get(locationId));
		}
	}
}

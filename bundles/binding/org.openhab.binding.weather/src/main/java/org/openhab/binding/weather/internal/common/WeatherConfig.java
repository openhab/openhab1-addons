/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * #weather:apikey.Hamweather=
 * #weather:apikey2.Hamweather=
 * #weather:apikey.Meteoblue=
 * #weather:apikey.ApiXU=
 * #weather:apikey.Weatherbit=
 *
 * # location configuration, you can specify multiple locations
 * #weather:location.<locationId1>.latitude=
 * #weather:location.<locationId1>.longitude=
 * #weather:location.<locationId1>.provider=
 * #weather:location.<locationId1>.language=
 * #weather:location.<locationId1>.updateInterval= (optional, defaults to 240)
 * #weather:location.<locationId1>.units=      (optional; defaults to "si")
 *
 * #weather:location.<locationId2>.latitude=
 * #weather:location.<locationId2>.longitude=
 * #weather:location.<locationId2>.provider=
 * #weather:location.<locationId2>.language=
 * #weather:location.<locationId2>.updateInterval= (optional, defaults to 240)
 * #weather:location.<locationId2>.units=      (optional; defaults to "si")
 * </pre>
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherConfig {
    private static final Logger logger = LoggerFactory.getLogger(WeatherConfig.class);
    private Map<ProviderName, ProviderConfig> providerConfigs = new HashMap<ProviderName, ProviderConfig>();
    private Map<String, LocationConfig> locationConfigs = new HashMap<String, LocationConfig>();
    private boolean valid;
    private boolean parseCompleted;

    /**
     * Parses and validates the properties in openhab.cfg.
     */
    public void parse(Dictionary<String, ?> properties) throws ConfigurationException {
        parseCompleted = false;
        valid = false;
        if (properties == null) {
            parseCompleted = true;
            logger.warn("No configuration found for the weather binding. Check the binding config.");
            throw new ConfigurationException("weather",
                    "No configuration found for the weather binding. Check the binding config.");
        }

        Enumeration<String> keys = properties.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

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
                parseCompleted = true;
                logger.warn("Incomplete location config for locationId '{}'. Check the binding config.",
                        lc.getLocationId());
                throw new ConfigurationException("weather", "Incomplete location config for locationId '"
                        + lc.getLocationId() + "'. Check the binding config.");
            }

            if (!providerConfigs.containsKey(lc.getProviderName())) {
                parseCompleted = true;
                logger.warn("No apikey found for provider '{}'. Check the binding config.", lc.getProviderName());
                throw new ConfigurationException("weather",
                        "No apikey found for provider '" + lc.getProviderName() + "'. Check the binding config.");
            }
        }

        // check all ProviderConfigs
        for (ProviderConfig pc : providerConfigs.values()) {
            if (!pc.isValid()) {
                parseCompleted = true;
                logger.warn("Invalid apikey config for provider '{}'. Check the binding config.", pc.getProviderName());
                throw new ConfigurationException("weather",
                        "Invalid apikey config for provider '" + pc.getProviderName() + "'. Check the binding config.");
            }
        }

        valid = locationConfigs.size() > 0;
        parseCompleted = true;
        logger.debug("Parsing of weather configuration settings completed.");
    }

    /**
     * Parses the properties for a location config.
     */
    private void parseLocation(String key, String value) throws ConfigurationException {
        if (value == null) {
            logger.warn("Weather location setting '{}' has no value. Check the binding config.", key);

            return;
        }

        String locationId = StringUtils.substringBetween(key, ".");

        if (StringUtils.isBlank(locationId)) {
            logger.warn("Weather location setting '{}' is missing its location. Check the binding config.", key);
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
        } else if (StringUtils.equalsIgnoreCase(keyId, "units")) {
            lc.setMeasurementUnits(value.toLowerCase());
        } else {
            logger.debug("Unknown weather configuration setting '{}'. Check the binding config.", key);
        }
    }

    /**
     * Parses the properties for a provider config.
     */
    private void parseApiKey(String key, String value) throws ConfigurationException {
        if (value == null) {
            logger.warn("Weather apikey setting '{}' has no value. Check the binding config.", key);
            return;
        }

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
            logger.warn("Unknown configuration key '{}'. Check the binding config.", key);
        }
    }

    /**
     * Parse a double value from a string.
     */
    private Double parseNumber(String key, String value) throws ConfigurationException {
        try {
            return Double.parseDouble(value);
        } catch (Exception ex) {
            logger.warn("Parameter '{}' empty or in wrong format ('{}'). Check the binding config.", key, value);
            throw new ConfigurationException("weather",
                    "Parameter '" + key + "' empty or in wrong format ('" + value + "'). Check the binding config.");
        }
    }

    /**
     * Parse a ProviderName from a string.
     */
    private ProviderName getProviderName(String name) throws ConfigurationException {
        ProviderName providerName = ProviderName.parse(name);

        if (providerName == null) {
            logger.warn("Provider with name '{}' not found. Check the binding config.", name);
            throw new ConfigurationException("weather",
                    "Provider with name '" + name + "' not found. Check the binding config.");
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
     * Returns true if the parse routine is not currently in progress.
     */
    public boolean finishedParsing() {
        return parseCompleted;
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

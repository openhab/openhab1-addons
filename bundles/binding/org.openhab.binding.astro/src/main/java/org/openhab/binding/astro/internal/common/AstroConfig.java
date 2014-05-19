/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.common;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.service.cm.ConfigurationException;

/**
 * Parses the config in openhab.cfg.
 * 
 * <pre>
 * ############################## Astro Binding ##############################
 * #
 * # The latitude
 * astro:latitude=nn.nnnnnn
 * 
 * # The longitude
 * astro:longitude=nn.nnnnnn
 * 
 * # Refresh interval for azimuth and elevation calculation in seconds (optional, defaults to disabled)
 * astro:interval=nnn
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroConfig {
	private static final String CONFIG_KEY_LATITUDE = "latitude";
	private static final String CONFIG_KEY_LONGITUDE = "longitude";
	private static final String CONFIG_KEY_INTERVAL = "interval";

	private double latitude;
	private double longitude;
	private int interval;
	private boolean valid;

	/**
	 * Parses and validates the properties in the openhab.cfg.
	 */
	public void parse(Dictionary<String, ?> properties) throws ConfigurationException {
		valid = false;

		String cfgLatitude = (String) properties.get(AstroConfig.CONFIG_KEY_LATITUDE);
		String cfgLongitude = (String) properties.get(AstroConfig.CONFIG_KEY_LONGITUDE);

		if (StringUtils.isBlank(cfgLatitude) || StringUtils.isBlank(cfgLongitude)) {
			throw new ConfigurationException("astro",
					"Parameters latitude and longitude are mandatory and must be configured. Please check your openhab.cfg!");
		}

		try {
			latitude = Double.parseDouble(cfgLatitude);
			longitude = Double.parseDouble(cfgLongitude);
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("astro",
					"Parameters latitude and/or longitude in wrong format. Please check your openhab.cfg!");
		}

		interval = parseInt(properties, CONFIG_KEY_INTERVAL, 0);

		valid = true;
	}

	/**
	 * Parses a integer property.
	 */
	private Integer parseInt(Dictionary<String, ?> properties, String key, Integer defaultValue)
			throws ConfigurationException {
		String value = (String) properties.get(key);
		if (StringUtils.isNotBlank(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				throw new ConfigurationException("astro", "Parameter " + key
						+ " in wrong format. Please check your openhab.cfg!");
			}

		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns true if the AstroConfig has valid parameters.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Returns the specified latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Returns the specified longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Returns the specified interval.
	 */
	public int getInterval() {
		return interval;
	}

	@Override
	public String toString() {
		String intervallMessage = (interval == 0 ? "disabled" : String.valueOf(interval));

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("latitude", latitude)
				.append("longitude", longitude).append("interval", intervallMessage).toString();
	}

}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

/**
 * Definition of all supported weather prividers.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum ProviderName {
	WUNDERGROUND, OPENWEATHERMAP, FORECASTIO, WORLDWEATHERONLINE, YAHOO, HAMWEATHER;

	/**
	 * Parses the string and returns the ProviderName enum.
	 */
	public static ProviderName parse(String name) {
		if (name == null) {
			return null;
		} else if (WUNDERGROUND.toString().equalsIgnoreCase(name)) {
			return WUNDERGROUND;
		} else if (OPENWEATHERMAP.toString().equalsIgnoreCase(name)) {
			return OPENWEATHERMAP;
		} else if (FORECASTIO.toString().equalsIgnoreCase(name)) {
			return FORECASTIO;
		} else if (WORLDWEATHERONLINE.toString().equalsIgnoreCase(name)) {
			return WORLDWEATHERONLINE;
		} else if (YAHOO.toString().equalsIgnoreCase(name)) {
			return YAHOO;
		} else if (HAMWEATHER.toString().equalsIgnoreCase(name)) {
			return HAMWEATHER;
		} else {
			return null;
		}
	}
}

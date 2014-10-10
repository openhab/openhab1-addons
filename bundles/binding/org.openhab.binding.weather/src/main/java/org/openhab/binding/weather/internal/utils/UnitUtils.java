/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.utils;

import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;

/**
 * Utility class for different unit conversions.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UnitUtils {

	/**
	 * Converts celsius to fahrenheit.
	 */
	public static Double celsiusToFahrenheit(Double celsius) {
		if (celsius == null) {
			return null;
		}
		return celsius * 1.8 + 32.0;
	}

	/**
	 * Converts millimeters to inches.
	 */
	public static Double millimetersToInches(Double millimeters) {
		if (millimeters == null) {
			return null;
		}
		return millimeters * 0.0393700787;
	}

	/**
	 * Converts kilometers per hour to miles per hour.
	 */
	public static Double kmhToMph(Double kmh) {
		if (kmh == null) {
			return null;
		}
		return kmh * 0.621371192;
	}

	/**
	 * Converts kilometers per hour to knots.
	 */
	public static Double kmhToKnots(Double kmh) {
		if (kmh == null) {
			return null;
		}
		return kmh * 0.539956803;
	}

	/**
	 * Converts kilometers per hour to meter per seconds.
	 */
	public static Double kmhToMps(Double kmh) {
		if (kmh == null) {
			return null;
		}
		return kmh * 0.277777778;
	}

	/**
	 * Converts meter per seconds to kilometers per hour.
	 */
	public static Double mpsToKmh(Double mps) {
		if (mps == null) {
			return null;
		}
		return mps / 0.277777778;
	}

	/**
	 * Converts kilometers per hour to beaufort.
	 */
	public static Double kmhToBeaufort(Double kmh) {
		if (kmh == null) {
			return null;
		}
		return new Double(Math.round(Math.pow(kmh / 3.01, 0.666666666)));
	}

	/**
	 * Converts millibar to inches.
	 */
	public static Double millibarToInches(Double millibar) {
		if (millibar == null) {
			return null;
		}
		return millibar * 0.0295299830714;
	}

	/**
	 * Converts meter to feet.
	 */
	public static Double meterToFeet(Double meter) {
		if (meter == null) {
			return null;
		}
		return meter * 3.2808399;
	}

	/**
	 * Converts centimeter to millimeter.
	 */
	public static Double centimeterToMillimeter(Double centimeter) {
		if (centimeter == null) {
			return null;
		}
		return centimeter * 100;
	}

	/**
	 * Calculates the humidex (feels like temperature) from temperature and
	 * humidity.
	 */
	public static double getHumidex(double temp, int humidity) {
		Double x = 7.5 * temp / (237.7 + temp);
		Double e = 6.112 * Math.pow(10, x) * humidity / 100;
		return temp + (5d / 9d) * (e - 10);
	}

	/**
	 * Returns the wind direction based on degree.
	 */
	public static String getWindDirection(Integer degree) {
		String[] directions = new String[] { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW",
				"W", "WNW", "NW", "NNW" };

		double step = 360.0 / directions.length;
		double b = Math.floor((degree + (step / 2.0)) / step);
		return directions[(int) (b % directions.length)];
	}

	/**
	 * Converts a value to the unit configured in the item binding.
	 */
	public static Double convertUnit(Double value, WeatherBindingConfig bindingConfig) {
		if (bindingConfig.hasUnit()) {
			switch (bindingConfig.getUnit()) {
			case FAHRENHEIT:
				return celsiusToFahrenheit(value);
			case MPH:
				return kmhToMph(value);
			case INCHES:
				if ("athmosphere.pressure".equals(bindingConfig.getWeatherProperty())) {
					return millibarToInches(value);
				} else if ("precipitation.snow".equals(bindingConfig.getWeatherProperty())) {
					return millimetersToInches(centimeterToMillimeter(value));
				} else {
					return millimetersToInches(value);
				}
			case BEAUFORT:
				return kmhToBeaufort(value);
			case KNOTS:
				return kmhToKnots(value);
			case MPS:
				return kmhToMps(value);
			}
		}
		return value;
	}

}

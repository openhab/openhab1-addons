/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

import java.math.BigDecimal;
import java.math.MathContext;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Temperature values are expressed as degrees Fahrenheit, multiplied by 10. For example, a temperature of 72F would be
 * expressed as the value 720. If the user preferences indicate the use of Celsius values, it is the responsibility of
 * the caller to convert values to and from Celsius.
 * 
 * <p>
 * Where specified explicitly Degrees F indicates that the temperature will be returned in degrees Fahrenheit accurate
 * to one decimal place (i.e. 18.5F).
 * 
 * <p>
 * Methods are included to construct Temperature objects from Celsius, Fahrenheit, or whichever the local temperature
 * scale is.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml#representation">Values
 *      and Representation</a>
 * @author John Cocula
 * @since 1.7.0
 */
public class Temperature {

	/**
	 * Enum for representing the temperature scale that will be reported.
	 */
	public static enum Scale {
		CELSIUS("C"), FAHRENHEIT("F");

		private final String scale;

		private Scale(final String scale) {
			this.scale = scale;
		}

		public String value() {
			return scale;
		}

		public static Scale forValue(String v) {
			for (Scale s : Scale.values()) {
				if (s.scale.equals(v)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Invalid temperature scale: " + v);
		}
	}

	/**
	 * The local temperature scale used throughout this JVM. Defaults to Fahrenheit.
	 */
	private static Scale localScale = Scale.FAHRENHEIT;

	public static void setLocalScale(final Scale localScale) {
		Temperature.localScale = localScale;
	}

	private BigDecimal temp;

	@JsonValue
	public int value() {
		return temp.intValue();
	}

	/**
	 * Construct a Temperature from the Ecobee-style temperature value (Fahrenheit times 10).
	 * 
	 * @param temp
	 *            Ecobee-style temperature value (Fahrenheit times 10)
	 */
	@JsonCreator
	public Temperature(int temp) {
		this.temp = new BigDecimal(temp);
	}

	/**
	 * Construct a Temperature from the Ecobee-style temperature value (Fahrenheit times 10).
	 * 
	 * @param temp
	 *            Ecobee-style temperature value (Fahrenheit times 10)
	 */
	public Temperature(BigDecimal temp) {
		this.temp = temp;
	}

	/**
	 * Factory method to construct a Temperature from the local temperature scale.
	 * 
	 * @param localTemp
	 *            the temperature in the local temperature scale.
	 * @return a new Temperature object
	 */
	public static Temperature fromLocalTemperature(BigDecimal localTemp) {
		if (localScale.equals(Scale.CELSIUS)) {
			return fromCelsius(localTemp);
		} else {
			return fromFahrenheit(localTemp);
		}
	}

	/**
	 * Factory method to construct a Temperature from Fahrenheit.
	 * 
	 * @param fahrenheit
	 *            the Fahrenheit temperature
	 */
	public static Temperature fromFahrenheit(BigDecimal fahrenheit) {
		return new Temperature(fahrenheit.movePointRight(1));
	}

	private static BigDecimal ONE_POINT_EIGHT = new BigDecimal("1.8");
	private static BigDecimal THIRTY_TWO = new BigDecimal("32");

	/**
	 * Factory method to construct a Temperature from Celsius.
	 * 
	 * @param celsius
	 *            the Celsius temperature
	 */
	public static Temperature fromCelsius(BigDecimal celsius) {
		return new Temperature(celsius.multiply(ONE_POINT_EIGHT).add(THIRTY_TWO).movePointRight(1));
	}

	/**
	 * Convert this temperature to a temperature in the local temperature scale.
	 * 
	 * @return temperature in the local temperature scale
	 */
	public final BigDecimal toLocalTemperature() {
		return localScale == Scale.CELSIUS ? toCelsius() : toFahrenheit();
	}

	/**
	 * Convert this Temperature to Fahrenheit.
	 * 
	 * @return temperature in Fahrenheit
	 */
	public final BigDecimal toFahrenheit() {
		return temp.movePointLeft(1);
	}

	/**
	 * Convert this Temperature to Celsius.
	 * 
	 * @return temperature in Celsius
	 */
	public final BigDecimal toCelsius() {
		return temp.movePointLeft(1).subtract(THIRTY_TWO).divide(ONE_POINT_EIGHT, MathContext.DECIMAL32);
	}

	@Override
	public String toString() {
		return temp.toString();
	}
}

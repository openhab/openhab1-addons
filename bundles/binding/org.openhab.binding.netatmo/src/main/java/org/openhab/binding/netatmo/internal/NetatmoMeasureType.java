/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import org.apache.commons.lang.StringUtils;

/**
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @since 1.6.0
 *
 *        This enum holds all the different measures and states available to be
 *        retrieved by the Netatmo binding
 */
public enum NetatmoMeasureType {
	CO2("CO2", NetatmoScale.MAX), TEMPERATURE("Temperature", NetatmoScale.MAX),
	HUMIDITY("Humidity", NetatmoScale.MAX), NOISE("Noise", NetatmoScale.MAX),
	PRESSURE("Pressure", NetatmoScale.MAX), RAIN("Rain", NetatmoScale.MAX),
	WIFISTATUS("WifiStatus", NetatmoScale.MAX), ALTITUDE("Altitude",NetatmoScale.MAX),
	LATITUDE("Latitude", NetatmoScale.MAX), LONGITUDE("Longitude", NetatmoScale.MAX),
	RFSTATUS("RfStatus", NetatmoScale.MAX), BATTERYVP("BatteryVp", NetatmoScale.MAX),
	TIMESTAMP("TimeStamp", NetatmoScale.MAX), MODULENAME("ModuleName",NetatmoScale.MAX),
	STATIONNAME("StationName", NetatmoScale.MAX), COORDINATE("Coordinate", NetatmoScale.MAX),
	MIN_TEMP("min_temp", NetatmoScale.ONE_DAY), MAX_TEMP("max_temp", NetatmoScale.ONE_DAY),
	MIN_HUM("min_hum", NetatmoScale.ONE_DAY), MAX_HUM("max_hum", NetatmoScale.ONE_DAY),
	MIN_PRESSURE("min_pressure", NetatmoScale.ONE_DAY), MAX_PRESSURE("max_pressure", NetatmoScale.ONE_DAY),
	MIN_NOISE("min_noise", NetatmoScale.ONE_DAY), MAX_NOISE("max_noise", NetatmoScale.ONE_DAY),
	MIN_CO2("min_co2", NetatmoScale.ONE_DAY), MAX_CO2("max_co2", NetatmoScale.ONE_DAY),
	SUM_RAIN("sum_rain", NetatmoScale.ONE_DAY), DATE_MIN_TEMP("date_min_temp", NetatmoScale.ONE_DAY),
	DATE_MAX_TEMP("date_max_temp", NetatmoScale.ONE_DAY), DATE_MIN_HUM("date_min_hum", NetatmoScale.ONE_DAY),
	DATE_MAX_HUM("date_max_hum", NetatmoScale.ONE_DAY), DATE_MIN_PRESSURE("date_min_pressure", NetatmoScale.ONE_DAY),
	DATE_MAX_PRESSURE("date_max_pressure", NetatmoScale.ONE_DAY), DATE_MIN_NOISE("date_min_noise", NetatmoScale.ONE_DAY),
	DATE_MAX_NOISE("date_max_noise", NetatmoScale.ONE_DAY), DATE_MIN_CO2("date_min_co2", NetatmoScale.ONE_DAY),
	DATE_MAX_CO2("date_max_co2", NetatmoScale.ONE_DAY), WINDSTRENGTH("WindStrength", NetatmoScale.MAX),
	WINDANGLE("WindAngle", NetatmoScale.MAX), GUSTSTRENGTH("GustStrength", NetatmoScale.MAX),
	GUSTANGLE("GustAngle", NetatmoScale.MAX), DATE_MAX_GUST("date_max_gust", NetatmoScale.ONE_DAY);

	final String measure;

	final NetatmoScale defaultScale;

	private NetatmoMeasureType(String measure, NetatmoScale defaultScale) {
		this.measure = measure;
		this.defaultScale = defaultScale;
	}

	public String getMeasure() {
		return measure;
	}

	public static NetatmoMeasureType fromString(String measure) {
		if (!StringUtils.isEmpty(measure)) {
			for (NetatmoMeasureType measureType : NetatmoMeasureType.values()) {
				if (measureType.toString().equalsIgnoreCase(measure)) {
					return measureType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid measure: " + measure);
	}

	public NetatmoScale getDefaultScale() {
		return defaultScale;
	}

	public static boolean isPressure(NetatmoMeasureType measureType) {
		return measureType == NetatmoMeasureType.PRESSURE
				|| measureType == NetatmoMeasureType.MIN_PRESSURE
				|| measureType == NetatmoMeasureType.MAX_PRESSURE;
	}

	public static boolean isRain(NetatmoMeasureType measureType) {
		return measureType == NetatmoMeasureType.RAIN
				|| measureType == NetatmoMeasureType.SUM_RAIN;
	}

	public static boolean isTemperature(NetatmoMeasureType measureType) {
		return measureType == NetatmoMeasureType.TEMPERATURE
				|| measureType == NetatmoMeasureType.MIN_TEMP
				|| measureType == NetatmoMeasureType.MAX_TEMP;
	}

	public static boolean isWind(NetatmoMeasureType measureType) {
		return measureType == NetatmoMeasureType.WINDSTRENGTH
				|| measureType == NetatmoMeasureType.GUSTSTRENGTH;
	}
}

/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
 * @since 1.6.0
 * 
 * This enum holds all the different measures and states available
 * to be retrieved by the Netatmo binding
 */
public enum NetatmoMeasureType {
	CO2("co2"),
	TEMPERATURE("Temperature"),
	HUMIDITY("Humidity"),
	NOISE("Noise"),
	PRESSURE("Pressure"),
	WIFISTATUS("WifiStatus"),
	ALTITUDE("Altitude"),
	LATITUDE("Latitude"),
	LONGITUDE("Longitude"),
	RFSTATUS("RfStatus"),
	BATTERYVP("BatteryVp");
	
	String measure;

	private NetatmoMeasureType(String measure) {
		this.measure = measure;
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
}

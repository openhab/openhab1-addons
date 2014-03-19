/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.constants;

import java.util.HashMap;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public enum SensorEnum {
	
	ACTIVE_POWER				(4, "Watts (W)",			0.0f,	4095.0f,	1.0f, 	12),
	OUTPUT_CURRENT				(5, "Ampere (mA)",			0.0f,	4095.0f,	1.0f, 	12),
	ELECTRIC_METER				(6, "Watthours (kWh)",		0.0f,	40.95f,		0.1f, 	12),
	TEMPERATURE_INDOORS			(9, "Kelvin (K)",			230.0f,	332.3f,		0.025f,	10),
	TEMPERATURE_OUTDOORS		(10, "Kelvin (K)",			230.0f,	332.3f,		0.025f,	10),
	RELATIVE_HUMIDITY_INDOORS	(13, "Percent (%)",			0.0f,	102.3f,		0.1f,	10),
	RELATIVE_HUMIDITY_OUTDOORS	(14, "Percent (%)",			0.0f,	102.3f,		0.1f,	10),
	AIR_PRESSURE				(15, "Pascal (hPa)",		0.0f,	102.3f,		0.1f,	10),
	WIND_SPEED					(18, "m/s",					0.0f,	102.3f,		0.1f,	10),
	PRECIPITATION				(20, "mm/m2",				0.0f,	102.3f,		0.1f,	10),
	OUTPUT_CURRENT_H			(64, "Ampere (mA)",			0.0f,	16380.0f,	4.0f,	12),
	POWER_CONSUMPTION			(65, "Volt-Ampere (VA)",	0.0f,	4095.0f,	1.0f,	12);
	
	private final int		sensorType;
	private final String	unit;
	private final float		minValue;
	private final float		maxvalue;
	private final float		resolution;
	private final int		valueRange;
	
	static final HashMap<Integer, SensorEnum> sensorEnums = new HashMap<Integer, SensorEnum>();
	
	static {
		for (SensorEnum sensor:SensorEnum.values()) {
			sensorEnums.put(sensor.getSensorType(), sensor);
		}
	}
	
	public static boolean containsSensor(Integer index) {
		return sensorEnums.keySet().contains(index);
	}
	
	public static SensorEnum getSensor(Integer index) {
		return sensorEnums.get(index);
	}
	
	SensorEnum(int sensorType, String description, float min, float max, float resolution, int valueRange) {
		this.sensorType = sensorType;
		this.unit	= description;
		this.minValue	= min;
		this.maxvalue	= max;
		this.resolution	= resolution;
		this.valueRange	= valueRange;
	}
	
	public int getSensorType() {
		return this.sensorType;
	}
	
	public String getUnit() {
		return this.unit;
	}
	
	public float getMinValue() {
		return this.minValue;
	}
	
	public float getMaxValue() {
		return this.maxvalue;
	}
	
	public float getResolution() {
		return this.resolution;
	}
	
	/**
	 * Get value range (in bits)
	 * @return	value range of bits
	 */
	public int getRange() {
		return this.valueRange;
	}

}

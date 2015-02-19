/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.gps.types;

import java.util.Formatter;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;


/**
 * This type can be used for items that are dealing with GPS functionality.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class CoordinateType implements ComplexType, Command, State {
	
	private static final String SEPARATOR = ",";
	private DecimalType latitude;
	private DecimalType longitude;
	private DecimalType altitude = null;
	
	// constants for the constituents
	static final public String KEY_LATITUDE = "lat";
	static final public String KEY_LONGITUDE = "lng";
	static final public String KEY_ALTITUDE = "alt";
		
	public static final State EMPTY = new CoordinateType(new DecimalType(0), new DecimalType(0));
	
	public CoordinateType(DecimalType latitude, DecimalType longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public CoordinateType(DecimalType latitude, DecimalType longitude, DecimalType altitude) {
		this(latitude, longitude);
		this.altitude = altitude;
	}
	
	public CoordinateType(StringType latitude, StringType longitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(longitude.toString()));
	}
	
	public CoordinateType(StringType latitude, StringType longitude, StringType altitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(longitude.toString()), new DecimalType(altitude.toString()));
	}
	
	public CoordinateType(String value) {
		if (StringUtils.isNotBlank(value)) {
			String[] elements = value.split(SEPARATOR);
			if (elements.length >= 2) {
				this.latitude = new DecimalType(elements[0]);
				this.longitude = new DecimalType(elements[1]);
				if (elements.length == 3) {
					this.altitude = new DecimalType(elements[2]);
				}
			}
		}
	}
		
	public DecimalType getLatitude() {
		return latitude;
	}
	
	public DecimalType getLongitude() {
		return longitude;
	}
	
	public DecimalType getAltitude() {
		return altitude;
	}
	
	/**
	 * <p>Formats the value of this type according to a pattern (@see 
	 * {@link Formatter}). One single value of this type can be referenced
	 * by the pattern using an index. The item order is defined by the natural
	 * (alphabetical) order of their keys.</p>
	 * 
	 * @param pattern the pattern to use containing indexes to reference the
	 * single elements of this type.
	 */
	public String format(String pattern) {
		return String.format(pattern, getConstituents().values().toArray());
	}
	
	public CoordinateType valueOf(String value) {
		return new CoordinateType(value);
	}
		
	@Override
	public String toString() {
		String result = String.format("%1$.3f°N, %2$.3f°W", getLatitude().doubleValue(), getLongitude().doubleValue());
		if (getAltitude() != null) {
			result = String.format("%1$s , %2$.3f m", result, getAltitude().doubleValue());
		}
		return result;
	}

	@Override
	public SortedMap<String, PrimitiveType> getConstituents() {
		SortedMap<String, PrimitiveType> result = new TreeMap<String, PrimitiveType>();
		result.put(KEY_LATITUDE, getLatitude());
		result.put(KEY_LONGITUDE, getLongitude());
		result.put(KEY_ALTITUDE, getAltitude());
		return result;
	}

	
}

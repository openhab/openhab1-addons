/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import java.util.Formatter;
import java.util.SortedMap;
import java.util.TreeMap;


import org.apache.commons.lang.StringUtils;
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
public class PointType implements ComplexType, Command, State {
	
	public static final double EARTH_GRAVITATIONAL_CONSTANT = 3.986004418e14;
	public static final double WGS84_a = 6378137;				// The equatorial radius of WGS84 ellipsoid (6378137 m).
	
	private double latitude;									// in decimal degrees
	private double longitude;									// in decimal degrees 
	private double altitude = 0;								// in decimal meters
	
	// constants for the constituents
	static final public String KEY_LATITUDE = "lat";
	static final public String KEY_LONGITUDE = "long";
	static final public String KEY_ALTITUDE = "alt";
		
	public static final State EMPTY = new PointType(new DecimalType(0), new DecimalType(0));
	
	public PointType(DecimalType latitude, DecimalType longitude) {
		canonicalize(latitude.doubleValue(),longitude.doubleValue());
	}
	
	public PointType(DecimalType latitude, DecimalType longitude, DecimalType altitude) {
		this(latitude, longitude);
		this.altitude = altitude.doubleValue();
	}
	
	public PointType(StringType latitude, StringType longitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(longitude.toString()));
	}
	
	public PointType(StringType latitude, StringType longitude, StringType altitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(longitude.toString()), new DecimalType(altitude.toString()));
	}
	
	public PointType(String value) {
		if (StringUtils.isNotBlank(value)) {
			String[] elements = value.split(",");
			if (elements.length >= 2) {
				canonicalize(Double.parseDouble(elements[0]),Double.parseDouble(elements[1]));
				if (elements.length == 3) {
					this.altitude = Double.parseDouble(elements[2]);
				}
			}
		}
	}
		
	public DecimalType getLatitude() {
		return new DecimalType(latitude);
	}
	
	public DecimalType getLongitude() {
		return new DecimalType(longitude);
	}
	
	public DecimalType getAltitude() {
		return new DecimalType(altitude);
	}
	
	public void setAltitude(DecimalType altitude) {
		this.altitude = altitude.doubleValue();
	}
	
	public DecimalType getGravity() {
		double latRad = Math.toRadians(latitude);
	    double deltaG = -2000.0 * (altitude/1000) * EARTH_GRAVITATIONAL_CONSTANT / ( Math.pow(WGS84_a,3.0) );
        double sin2lat = Math.sin(latRad) * Math.sin(latRad);
        double sin22lat = Math.sin(2.0*latRad) * Math.sin(2.0*latRad);
        double result = (9.780327 * (1.0 + 5.3024e-3 * sin2lat - 5.8e-6 * sin22lat) + deltaG); 
        return new DecimalType(result);
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
	
	public PointType valueOf(String value) {
		return new PointType(value);
	}
		
	@Override
	public String toString() {
		return String.format("%1$.2f°N, %2$.2f°W, %2$.2f m", latitude, longitude, altitude);
	}

	@Override
	public SortedMap<String, PrimitiveType> getConstituents() {
		SortedMap<String, PrimitiveType> result = new TreeMap<String, PrimitiveType>();
		result.put(KEY_LATITUDE, getLatitude());
		result.put(KEY_LONGITUDE, getLongitude());
		result.put(KEY_ALTITUDE, getAltitude());
		return result;
	}
	
	/**
	 * Canonicalize the current latitude and longitude values such that:
	 * 
	 * <pre>
	 * -90 &lt;= latitude &lt;= +90 - 180 &lt; longitude &lt;= +180
	 * </pre>
	 */
	private void canonicalize(double aLat, double aLon) {
		latitude = (aLat + 180) % 360;
		longitude = aLon;
		if (latitude < 0) this.latitude += 360;
		
		latitude -= 180;
		if (latitude > 90) {
			latitude = 180 - latitude;
			longitude += 180;
		} else if (latitude < -90) {
			latitude = -180 - latitude;
			longitude += 180;
		}

		longitude = ((longitude + 180) % 360);
		if (longitude <= 0) longitude += 360;
		longitude -= 180;
	}
	
}

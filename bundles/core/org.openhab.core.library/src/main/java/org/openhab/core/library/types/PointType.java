/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.SortedMap;
import java.util.TreeMap;

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
	public static final double WGS84_a = 6378137; // The equatorial radius of
													// WGS84 ellipsoid (6378137
													// m).
	private BigDecimal latitude = BigDecimal.ZERO; // in decimal degrees
	private BigDecimal longitude = BigDecimal.ZERO; // in decimal degrees
	private BigDecimal altitude = BigDecimal.ZERO; // in decimal meters
	// constants for the constituents
	static final public String KEY_LATITUDE = "lat";
	static final public String KEY_LONGITUDE = "long";
	static final public String KEY_ALTITUDE = "alt";
	private static final BigDecimal circle = new BigDecimal(360);
	private static final BigDecimal flat = new BigDecimal(180);
	private static final BigDecimal right = new BigDecimal(90);
	public static final PointType EMPTY = new PointType(new DecimalType(0),
			new DecimalType(0));

	/**
	 * Default constructor creates a point at sea level where the equator
	 * (0° latitude) and the prime meridian (0° longitude) intersect. (A
	 * nullary constructor is needed by
	 * {@link org.openhab.core.internal.items.ItemUpdater#receiveUpdate})
	 */
	@SuppressWarnings("restriction")
	public PointType() {}

	public PointType(DecimalType latitude, DecimalType longitude) {
		canonicalize(latitude, longitude);
	}

	public PointType(DecimalType latitude, DecimalType longitude,
			DecimalType altitude) {
		this(latitude, longitude);
		setAltitude(altitude);
	}

	public PointType(StringType latitude, StringType longitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(
				longitude.toString()));
	}

	public PointType(StringType latitude, StringType longitude,
			StringType altitude) {
		this(new DecimalType(latitude.toString()), new DecimalType(
				longitude.toString()), new DecimalType(altitude.toString()));
	}

	public PointType(String value) {
		if (!value.isEmpty()) {
			String[] elements = value.split(",");
			if (elements.length >= 2) {
				canonicalize(new DecimalType(elements[0]), new DecimalType(
						elements[1]));
				if (elements.length == 3) {
					setAltitude(new DecimalType(elements[2]));
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
		this.altitude = altitude.toBigDecimal();
	}

	public DecimalType getGravity() {
		double latRad = Math.toRadians(latitude.doubleValue());
		double deltaG = -2000.0 * (altitude.doubleValue() / 1000)
				* EARTH_GRAVITATIONAL_CONSTANT / (Math.pow(WGS84_a, 3.0));
		double sin2lat = Math.sin(latRad) * Math.sin(latRad);
		double sin22lat = Math.sin(2.0 * latRad) * Math.sin(2.0 * latRad);
		double result = (9.780327 * (1.0 + 5.3024e-3 * sin2lat - 5.8e-6 * sin22lat) + deltaG);
		return new DecimalType(result);
	}

	/**
	 * <p>
	 * Formats the value of this type according to a pattern (@see
	 * {@link Formatter}). One single value of this type can be referenced by
	 * the pattern using an index. The item order is defined by the natural
	 * (alphabetical) order of their keys.
	 * </p>
	 *
	 * @param pattern
	 *            the pattern to use containing indexes to reference the single
	 *            elements of this type.
	 */
	@Override
	public String format(String pattern) {
		return String.format(pattern, getConstituents().values().toArray());
	}

	public PointType valueOf(String value) {
		return new PointType(value);
	}

	@Override
	public String toString() {
		return String.format("%1$.2f°N, %2$.2f°W, %3$.2f m", latitude, longitude, altitude);
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
	private void canonicalize(DecimalType aLat, DecimalType aLon) {
		latitude = flat.add(aLat.toBigDecimal()).remainder(circle);
		longitude = aLon.toBigDecimal();
		if (latitude.compareTo(BigDecimal.ZERO) == -1)
			latitude.add(circle);
		latitude = latitude.subtract(flat);
		if (latitude.compareTo(right) == 1) {
			latitude = flat.subtract(latitude);
			longitude = longitude.add(flat);
		} else if (latitude.compareTo(right.negate()) == -1) {
			latitude = flat.negate().subtract(latitude);
			longitude = longitude.add(flat);
		}
		longitude = flat.add(longitude).remainder(circle);
		if (longitude.compareTo(BigDecimal.ZERO) <= 0)
			longitude = longitude.add(circle);
		longitude = longitude.subtract(flat);
	}

}

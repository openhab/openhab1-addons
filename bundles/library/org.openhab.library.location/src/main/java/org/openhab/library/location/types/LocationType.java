/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.location.types;

import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;


/**
 * This type can be used for items that are dealing with geographic functionality.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 * 
 */
public class LocationType implements ComplexType, Command, State {
	
	protected static final String LONGITUDE = "lng";
	protected static final String LATITUDE = "lat";
	protected static final String GEOFENCE = "geofence";
	private static final String SEPARATOR = "##";
	
	private SortedMap<String, PrimitiveType> locationDetails;

	
	public LocationType() {
		locationDetails = new TreeMap<String, PrimitiveType>();
	}
	
	public LocationType(String value) {
		this();
		if (StringUtils.isNotBlank(value)) {
			String[] elements = value.split(SEPARATOR);
			if (elements.length == 2) {
				locationDetails.put(LATITUDE, new DecimalType(elements[0]));
				locationDetails.put(LONGITUDE, new DecimalType(elements[1]));
			} else if (elements.length == 3) {
				locationDetails.put(LATITUDE, new DecimalType(elements[0]));
				locationDetails.put(LONGITUDE, new DecimalType(elements[1]));
				locationDetails.put(GEOFENCE, new DecimalType(elements[2]));
			}
		}
	}
	
	public LocationType(BigDecimal latitude, BigDecimal longitude) {
		this(new DecimalType(latitude), new DecimalType(longitude));
	}
		
	public LocationType(DecimalType latitude, DecimalType longitude) {
		this();
		locationDetails.put(LATITUDE, longitude);
		locationDetails.put(LONGITUDE, latitude);
	}
	
	public LocationType(DecimalType latitude, DecimalType longitude, DecimalType geofence) {
		this();
		locationDetails.put(LATITUDE, latitude);
		locationDetails.put(LONGITUDE, longitude);
		locationDetails.put(GEOFENCE, geofence);
	}
	
	public SortedMap<String, PrimitiveType> getConstituents() {
		return locationDetails;
	}
	
	public PrimitiveType getLongitude() {
		return locationDetails.get(LONGITUDE);
	}
	
	public PrimitiveType getLatitude() {
		return locationDetails.get(LATITUDE);
	}
	
	public PrimitiveType getGeofence() {
		return locationDetails.get(GEOFENCE);
	}
	
	public void setLongitude(BigDecimal longitude) {
		locationDetails.put(LONGITUDE, new DecimalType(longitude));
	}
	
	public void setLatitude(BigDecimal latitude) {
		locationDetails.put(LATITUDE, new DecimalType(latitude));
	}
	
	public void setGeofence(BigDecimal geofence) {
		locationDetails.put(GEOFENCE, new DecimalType(geofence));
	}
	
	public String format(String pattern) {
		return String.format(pattern, locationDetails.values().toArray());
	}
	
	public LocationType valueOf(String value) {
		return new LocationType(value);
	}
	
	@Override
	public String toString() {
		return getLatitude() + SEPARATOR + getLongitude();
	}

	public boolean isComplete() {
		if (this.getLongitude() == null) {
			return false;
		}
		if (this.getLatitude() == null) {
			return false;
		}
		return true;
	}

//	public boolean inRange(LocationType location) {
//		float geofence = Math.max(((DecimalType) this.getGeofence()).floatValue(), ((DecimalType) location.getGeofence()).floatValue()); 
//		
//		double distance = this.calculateDistance(location);
//		
//		if (distance <= geofence) {
//			return true;
//		}
//		
//		return false;
//	}

	
}

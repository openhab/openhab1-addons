/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.gps.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.library.gps.types.CoordinateType;


/**
 * This item identifies a telephone call by its origin and destination.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class CoordinateItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();
	
	private final double EARTH_RADIUS_METER = 3958.75 * 2 * 1609;
	
	static {
		acceptedDataTypes.add(CoordinateType.class);
		acceptedDataTypes.add(UnDefType.class);
	}
	
	public CoordinateItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	/**
	 * Compute the with another Coordinate type,
	 *
	 * @return distance between the two points in meters
	 */
	public DecimalType distanceFrom(CoordinateType away){
			
		double dist = -1;
		
		if ((this.state != UnDefType.UNDEF) && (away != null)) {
			
			CoordinateType me = (CoordinateType) this.state;
			
			double dLat = Math.pow(Math.sin(Math.toRadians(away.getLatitude().doubleValue() - me.getLatitude().doubleValue()) / 2),2);
			double dLng = Math.pow(Math.sin(Math.toRadians(away.getLongitude().doubleValue() - me.getLongitude().doubleValue()) / 2),2);
			double a = dLat + Math.cos(Math.toRadians(me.getLatitude().doubleValue()))  
							* Math.cos(Math.toRadians(away.getLatitude().doubleValue())) * dLng;
			
			dist = EARTH_RADIUS_METER * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		}
		
		return new DecimalType(dist);
	}

	
}

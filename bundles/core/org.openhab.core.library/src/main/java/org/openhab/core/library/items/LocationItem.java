/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * A LocationItem can be used to store GPS related informations, addresses...
 * This is useful for location awareness related functions
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class LocationItem extends GenericItem {
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();
	
	static {
		acceptedDataTypes.add(PointType.class);
		acceptedDataTypes.add(UnDefType.class);
		acceptedCommandTypes.add(PointType.class);
	}
	
	public LocationItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	/**
	 * Return the distance from another LocationItem.
	 * @return distance between the two points in meters
	 */
	public DecimalType distanceFrom(LocationItem awayItem) {
		if (awayItem != null && awayItem.state instanceof PointType && this.state instanceof PointType) {
			PointType thisPoint = (PointType)this.state;
			PointType awayPoint = (PointType)awayItem.state;
			return thisPoint.distanceFrom(awayPoint);
		}
		return new DecimalType(-1);
	}
	
}

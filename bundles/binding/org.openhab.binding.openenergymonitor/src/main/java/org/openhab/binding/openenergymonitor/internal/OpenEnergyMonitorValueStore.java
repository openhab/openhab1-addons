/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.internal;

import java.util.HashMap;

/**
 * This class provide temporary value store for Open Energy Monitor values.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorValueStore {

	HashMap<String, Double> latestValues = null;

	OpenEnergyMonitorValueStore() {
		latestValues = new HashMap<String, Double>();
	}
	
	public double getValue(String item) throws IllegalArgumentException {
		
		if (latestValues.containsKey(item)) {
			return latestValues.get(item);
		}
		
		throw new IllegalArgumentException();
	}
	
	public void setValue(String item, double value) {
		latestValues.put(item, value);
	}
	
	public double incValue(String item, double value)  throws IllegalArgumentException {
		double val = getValue(item) + value;
		setValue(item, val);
		return val;
	}

}

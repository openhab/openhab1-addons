/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
	
	public double getLatestValue(String item) {
		
		if (latestValues.containsKey(item)) {
			return latestValues.get(item);
		}
		
		return 0;
	}
	
	public void setLatestValue(String item, double value) {
		latestValues.put(item, value);
	}
	
	public double incLatestValue(String item, double value) {
		double val = getLatestValue(item) + value;
		setLatestValue(item, val);
		return val;
	}

}

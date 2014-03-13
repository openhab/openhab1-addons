/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public enum NeoStatProperty {
	CurrentTemperature("CurrentTemperature", "CURRENT_TEMPERATURE"),
	CurrentSetTemperature("CurrentSetTemperature", "CURRENT_SET_TEMPERATURE"),
	CurrentFloorTemperature("CurrentFloorTemperature", "CURRENT_FLOOR_TEMPERATURE"),
	
	DeviceName("DeviceName", "device"),
	Heating("Heating", "HEATING"),
	Away("Away", "AWAY"),
	Standby("Standby", "STANDBY");
	
	
	public final String binding;
	public final String protocolName;
	
	/**
	 * 
	 * @param b name in configuration file
	 * @param k name in neohub protocol
	 */
	NeoStatProperty(final String b, final String k) {
		binding = b;
		protocolName = k;
	}
	
	public static NeoStatProperty fromBinding(String b){
		for (NeoStatProperty p : NeoStatProperty.values()) {
			if(p.binding.equals(b)){
				return p;
			}
		}
		return null;
	}
	
	public static List<String> getBindings() {
		return Lists.transform(Arrays.asList(NeoStatProperty.values()), new Function<NeoStatProperty, String>() {
	
		@Override
		public String apply(NeoStatProperty property) {
			return property.binding;
		}
	});
	}
}

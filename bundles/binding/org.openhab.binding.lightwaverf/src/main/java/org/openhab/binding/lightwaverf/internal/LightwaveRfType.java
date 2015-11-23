/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

/**
 * The types of LightwaveRF Devicies (Dimmer, Switch) or the parameters on a
 * (HEATING_*) device for multi paramter devices like the Radiator valves.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public enum LightwaveRfType {
	DIMMER, 
	SWITCH, 
	
	VERSION, 
	UPDATETIME, 
	SIGNAL,
	
	HEATING_CURRENT_TEMP, 
	HEATING_BATTERY, 
	HEATING_SET_TEMP, 
	HEATING_MODE, 

	ENERGY_YESTERDAY_USAGE, 
	ENERGY_TODAY_USAGE, 
	ENERGY_MAX_USAGE, 
	ENERGY_CURRENT_USAGE; 
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal.thermostat;

/**
 * Thermostat class for the PRT thermostat (Programmable Room Thermostat)
 * Most functions are handled by the base class
 * This thermostat does no include hot water, so these functions are overridden
 * and disabled
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class HeatmiserPRT extends HeatmiserThermostat {

	public HeatmiserPRT() {
		DCB_READ_WATER_STATE = 0;	
	}
}

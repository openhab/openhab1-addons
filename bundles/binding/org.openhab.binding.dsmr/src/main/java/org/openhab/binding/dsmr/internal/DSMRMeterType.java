/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

/**
 * Supported meters
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public enum DSMRMeterType {
	/** Special meter for filtering purposes */
	NA("invalid"),
	/** Electricity meter (electricity.channel) */
	ELECTRICITY("electricity.channel"),
	/** Gas meter (gas.channel) */
	GAS("gas.channel"),
	/** Water meter (water.channel) */
	WATER("water.channel"),
	/** Heating meter (heating.channel) */
	HEATING("heating.channel"),
	/** Cooling meter (cooling.channel) */
	COOLING("cooling.channel"),
	/** Generic meter (generic.channel) */
	GENERIC("generic.channel"),
	/** Slave electricity meter (slaveelectricity.channel) */
	SLAVE_ELECTRICITY("slaveelectricity.channel");

	/** Channel configuration key for openhab.cfg */
	public final String channelConfigKey;

	/**
	 * Creates a new enum
	 * 
	 * @param channelConfigKey
	 *            String containing the channel configuration for this meter
	 */
	private DSMRMeterType(String channelConfigKey) {
		this.channelConfigKey = channelConfigKey;
	}
}

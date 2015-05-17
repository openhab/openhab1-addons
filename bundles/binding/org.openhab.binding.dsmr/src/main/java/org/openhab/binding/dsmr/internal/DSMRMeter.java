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
 * DSMR Meter represents a meter for this binding.
 * <p>
 * The main Electricity meter {@link DSMRMeterType}.ELECTRICTY is available
 * implicit and an instance of this class for this meter is not necessary.
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class DSMRMeter {
	// Meter type
	private final DSMRMeterType meterType;

	// M-Bus channel
	private final int channel;

	/**
	 * Creates a new DSMRMeter
	 * 
	 * @param meterType
	 *            {@link DSMRMeterType}
	 * @param channel
	 *            integer specifying on which M-Bus channel the meter is
	 *            configured
	 */
	public DSMRMeter(DSMRMeterType meterType, int channel) {
		this.meterType = meterType;
		this.channel = channel;
	}

	/**
	 * Returns the DSMRMeterType
	 * 
	 * @return the DSMRMeterType
	 */
	public DSMRMeterType getMeterType() {
		return meterType;
	}

	/**
	 * Returns the channel
	 * 
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.interfaces;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;

/**
 * Interface for handling outlet meters.
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaOutletMeter extends FritzahaDevice {
	/**
	 * Type of meter handled by a config.
	 * 
	 * @author Christian Brauers
	 * @since 1.3.0
	 */
	public static enum MeterType {
		VOLTAGE, CURRENT, POWER, ENERGY;
	};

	/**
	 * Getter for type of meter
	 * 
	 * @return Meter type
	 */
	public MeterType getMeterType();

	/**
	 * Inquires about the meter value and updates item state accordingly. Should
	 * be asynchronous if possible.
	 * 
	 * @param ItemName
	 *            Item to update
	 * @param WebIface
	 *            Web interface to use
	 */
	public void updateMeterValue(String itemName, FritzahaWebInterface webIface);

}

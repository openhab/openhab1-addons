/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rpircswitch.internal;

import java.util.BitSet;

import org.openhab.core.binding.BindingConfig;

/**
 * An {@link RPiRcSwitchBindingConfig} stores group and device address of one RC
 * switch.
 * 
 * @author Matthias Röckl
 * @since 1.8.0
 */
public class RPiRcSwitchBindingConfig implements BindingConfig {

	private BitSet groupAddress;
	private int deviceAddress;

	/**
	 * Creates a new {@link RPiRcSwitchBindingConfig} with the given group and
	 * device address.
	 * 
	 * @param groupAddress
	 *            the group address, e.g. 10101
	 * @param deviceAddress
	 *            the device address, e.g. 4
	 */
	public RPiRcSwitchBindingConfig(BitSet groupAddress, int deviceAddress) {
		this.groupAddress = groupAddress;
		this.deviceAddress = deviceAddress;
	}

	/**
	 * Returns the group address, e.g. 10101.
	 * 
	 * @return the group address
	 */
	public BitSet getGroupAddress() {
		return groupAddress;
	}

	/**
	 * Sets the group address, e.g. 10101.
	 * 
	 * @param groupAddress
	 *            the group address
	 */
	public void setGroupAddress(BitSet groupAddress) {
		this.groupAddress = groupAddress;
	}

	/**
	 * Returns the device address, e.g. 4.
	 * 
	 * @return the device address
	 */
	public int getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * Sets the device address, e.g. 4.
	 * 
	 * @param deviceAddress
	 *            the device address
	 */
	public void setDeviceAddress(int deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

}

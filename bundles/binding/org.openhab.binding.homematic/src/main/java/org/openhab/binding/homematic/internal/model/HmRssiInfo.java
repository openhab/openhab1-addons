/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Object that holds the rssi infos for a RF device.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class HmRssiInfo {
	private String address;
	private Integer device;
	private Integer peer;

	public HmRssiInfo(String address, Integer device, Integer peer) {
		this.address = address;
		this.device = convert(device);
		this.peer = convert(peer);
	}

	/**
	 * Converts the rssi value to null if necessary.
	 */
	private Integer convert(Integer intValue) {
		if (intValue == null || intValue == 65536) {
			return 0;
		}
		return intValue;
	}

	/**
	 * Returns the address of the device.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the device rssi.
	 */
	public Integer getDevice() {
		return device;
	}

	/**
	 * Returns the peer rssi.
	 */
	public Integer getPeer() {
		return peer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("address", address)
				.append("device", device).append("peer", peer).toString();
	}

}

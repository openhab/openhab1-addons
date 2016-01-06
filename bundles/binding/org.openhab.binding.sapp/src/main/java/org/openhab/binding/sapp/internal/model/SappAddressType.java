/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;

import org.apache.commons.lang.StringUtils;

/**
 * The list of supported address types for the Sapp binding
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public enum SappAddressType {

	INPUT("I"), OUTPUT("O"), VIRTUAL("V");

	String address;

	/**
	 * Constructor
	 */
	private SappAddressType(String address) {
		this.address = address;
	}

	/**
	 * address getter
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * builds a SappAddressType from the String representation
	 */
	public static SappAddressType fromString(String address) {
		if (!StringUtils.isEmpty(address)) {
			for (SappAddressType addressType : SappAddressType.values()) {
				if (addressType.getAddress().equals(address)) {
					return addressType;
				}
			}
		}

		throw new IllegalArgumentException("Invalid or unsupported Sapp address: " + address);
	}
}

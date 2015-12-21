/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;

/**
 * Generic Address model
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public abstract class SappAddress {

	private String pnmasId;
	private SappAddressType addressType;
	private int address;
	private String subAddress;

	/**
	 * Constructor
	 */
	public SappAddress(String pnmasId, SappAddressType addressType, int address, String subAddress) {
		super();
		this.pnmasId = pnmasId;
		this.addressType = addressType;
		this.address = address;
		this.subAddress = subAddress;
	}

	/**
	 * pnmasId getter
	 */
	public String getPnmasId() {
		return pnmasId;
	}

	/**
	 * addressType getter
	 */
	public SappAddressType getAddressType() {
		return addressType;
	}

	/**
	 * address getter
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * subAddress getter
	 */
	public String getSubAddress() {
		return subAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s ]", pnmasId, addressType, address, subAddress);
	}
}

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
 * Rollershutter Status Address model
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappAddressRollershutterStatus extends SappAddress {

	private int openValue;
	private int closedValue;

	/**
	 * Constructor
	 */
	public SappAddressRollershutterStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int openValue, int closedValue) {
		super(pnmasId, addressType, address, subAddress);
		this.openValue = openValue;
		this.closedValue = closedValue;
	}

	/**
	 * openValue getter
	 */
	public int getOpenValue() {
		return openValue;
	}

	/**
	 * closedValue getter
	 */
	public int getClosedValue() {
		return closedValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), openValue, closedValue);
	}
}

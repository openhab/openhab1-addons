/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;


public class SappAddressOnOffControl extends SappAddress {

	private int onValue;
	private int offValue;

	public SappAddressOnOffControl(String pnmasId, SappAddressType addressType, int address, String subAddress, int onValue, int offValue) {
		super(pnmasId, addressType, address, subAddress);
		this.onValue = onValue;
		this.offValue = offValue;
	}

	public int getOnValue() {
		return onValue;
	}

	public int getOffValue() {
		return offValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), onValue, offValue);
	}
}

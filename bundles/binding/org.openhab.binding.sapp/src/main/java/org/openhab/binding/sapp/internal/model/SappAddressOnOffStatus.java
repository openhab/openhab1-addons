/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;


public class SappAddressOnOffStatus extends SappAddress {

	private int onValue;

	public SappAddressOnOffStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int onValue) {
		super(pnmasId, addressType, address, subAddress);
		this.onValue = onValue;
	}

	public int getOnValue() {
		return onValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), onValue);
	}
}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;


public class SappAddressOpenClosedStatus extends SappAddress {

	private int openValue;

	public SappAddressOpenClosedStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int openValue) {
		super(pnmasId, addressType, address, subAddress);
		this.openValue = openValue;
	}

	public int getOpenValue() {
		return openValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), openValue);
	}
}

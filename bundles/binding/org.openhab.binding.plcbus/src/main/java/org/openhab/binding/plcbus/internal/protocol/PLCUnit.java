/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Representation of an Unit in PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCUnit {

	private String userCode;
	private String address;

	public PLCUnit(String userCode, String address) {
		this.userCode = userCode;
		this.address = address;
	}

	public String getUsercode() {
		return userCode;
	}

	public String getAddress() {
		return address;
	}
	
}

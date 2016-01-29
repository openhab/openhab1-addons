/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

/**
 * Represents an edimax device discovered.
 * 
 * @author Heinz
 */
public class EdimaxDevice {
	public String ip;
	public String mac; // only store UPPERCASE mac in here.

	public EdimaxDevice() {
	}

	public String getIp() {
		return ip;
	}

	public String getMac() {
		return mac;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
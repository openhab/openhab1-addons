/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.maxcube.internal.message;

/**
* Device information provided b the M message meta information. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class DeviceInformation {

	private DeviceType deviceType = DeviceType.Invalid;
	private String serialNumber = "";
	private String rfAddress = "";
	private String name = "";
	private int roomId = -1;
	
	public DeviceInformation(DeviceType deviceType, String serialNumber, String rfAddress, String name, int roomId) {
		this.deviceType = deviceType;
		this.serialNumber = serialNumber;
		this.rfAddress = rfAddress;
		this.name = name;
		this.roomId = roomId;
	}

	public String getRFAddress() {
		return rfAddress;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public int getRoomId() {
		return roomId;
	}
	
	public String getName() {
		return name;
	}
}
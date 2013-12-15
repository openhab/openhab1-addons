/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;
import org.openhab.binding.maxcube.internal.Utils;

/**
* Base class for configuration provided by the MAX!Cube C_Message. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class Configuration {
	
	private DeviceType deviceType = null;
	private String rfAddress = null;
	private String serialNumber = null;
	private int roomId = -1;
	
	private Configuration(String rfAddress, DeviceType deviceType, String serialNumber) {
		this.rfAddress = rfAddress;
		this.deviceType = deviceType;
		this.serialNumber = serialNumber;
	}
	
	public static Configuration create(Message message) {
		C_Message c_message = (C_Message) message;
		
		Configuration configuration = new Configuration(c_message.getRFAddress(), c_message.getDeviceType(), c_message.getSerialNumber());
	
		return configuration;
	}
	
	public static Configuration create(DeviceInformation di) {
		Configuration configuration = new Configuration(di.getRFAddress(), di.getDeviceType(), di.getSerialNumber());
		return configuration;
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
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}	
}
/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* The M message contains metadata about the MAX!Cube setup. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class M_Message extends Message {

	public ArrayList<RoomInformation> rooms;
	public ArrayList<DeviceInformation> devices;
	
	
	public M_Message(String raw) {
		super(raw);
		
		
		String[] tokens = this.getPayload().split(Message.DELIMETER);
		byte[] bytes = Base64.decodeBase64(tokens[2].getBytes());
			
		rooms = new ArrayList<RoomInformation>();
		devices = new ArrayList<DeviceInformation>();
		
		 int roomCount = bytes[2];

		 int byteOffset = 3; // start of rooms
		 
		 /* process room */
		 
		 for (int i = 0; i < roomCount; i++) {
		
			 int position = bytes[byteOffset++];
			 String name = "";
			 
			 int nameLength = (int) bytes[byteOffset++] & 0xff; 
			 for (int char_idx = 0; char_idx < nameLength; char_idx++) {
				 name += (char) bytes[byteOffset++];
			 }
			 
			 String rfAddress = Utils.toHex(((int)bytes[byteOffset] & 0xff), ((int)bytes[byteOffset+1] & 0xff), ((int)bytes[byteOffset + 2] & 0xff));
			 byteOffset += 3;
			
			 rooms.add(new RoomInformation(position, name, rfAddress));
		 }
		 
		 /* process devices */
		 
		 int deviceCount = bytes[byteOffset++];
	
		 for (int deviceId = 0; deviceId < deviceCount; deviceId++) {
			 DeviceType deviceType = DeviceType.create(bytes[byteOffset++]);
			 
			 String rfAddress = Utils.toHex(((int)bytes[byteOffset]&0xff), ((int)bytes[byteOffset+1]&0xff), ((int)bytes[byteOffset+2]&0xff));
			 byteOffset += 3;
		 
			 String serialNumber = "";

			 for (int i = 0; i < 10; i++) {
				 serialNumber += (char) bytes[byteOffset++];
			 }
		 
			 int nameLength = (int)bytes[byteOffset++] & 0xff;

			 String deviceName = "";

			 for (int char_idx = 0;	 char_idx < nameLength; char_idx++) {
				 deviceName += (char)bytes[byteOffset++];
			 }

			 int roomId = (int)bytes[byteOffset++] & 0xff;

			 devices.add(new DeviceInformation(deviceType, serialNumber, rfAddress, deviceName, roomId));	
		
			 
		 }
	}
	
	@Override
	public void debug(Logger logger) {
		logger.debug("=== M_Message === ");
		logger.trace("\tRAW : {}", this.getPayload());
		for(RoomInformation room: rooms){
			 logger.debug("\t=== Rooms ===");
			 logger.debug("\tRoom Pos   : {}", room.getPosition());
			 logger.debug("\tRoom Name  : {}", room.getName());
			 logger.debug("\tRoom RF Adr: {}",  room.getRFAddress());
			 for(DeviceInformation device: devices){
				 if (room.getPosition() == device.getRoomId()) {
					 logger.debug("\t=== Devices ===");
					 logger.debug("\tDevice Type    : {}", device.getDeviceType());
					 logger.debug("\tDevice Name    : {}", device.getName());
					 logger.debug("\tDevice Serialnr: {}", device.getSerialNumber());
					 logger.debug("\tDevice RF Adr  : {}", device.getRFAddress());
					 logger.debug("\tRoom Id        : {}", device.getRoomId());
				 }
			 }

		}
		
	}

	@Override
	public MessageType getType() {
		 return MessageType.M;
	}
}

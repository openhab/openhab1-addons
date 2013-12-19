/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
	
	public class M_Room {
		int RoomNumber;
		String RoomName;
		String RFAddress;
	}
	
	public class M_dev {
		DeviceType DevType;
		String RFAddr;
		String SerialNum;
		String name;
		int RoomId;
		
		public String getSerialNumber() {
			return SerialNum;
		}

		public String getRFAddress() {
			return RFAddr;
		}

		public DeviceType getDeviceType() {
			return DevType;
		}
		
		public String getName() {
			return name;
		}
		
		public int getRoomId() {
			return RoomId;
		}
	}
	
	public ArrayList<M_Room> rooms;
	public ArrayList<M_dev> devices;
	
	private static final Logger logger = LoggerFactory.getLogger(M_Message.class);
	
	public M_Message(String raw) {
		super(raw);
		
		String[] tokens = this.getPayload().split(Message.DELIMETER);
		
		byte[] bytes = Base64.decodeBase64(tokens[2].getBytes());
		
		logger.debug("Raw message: {}", tokens[2]);
		
		rooms = new ArrayList<M_Room>();
		devices = new ArrayList<M_dev>();
		
		int num_rooms = bytes[2];
		int byte_ptr = 3; /* start of rooms */
		/* process each room */
		for (int rm_idx = 0; rm_idx < num_rooms; rm_idx++)
		{
			/* current do nothing - could have room bindings based 
			 * on RF address it looks like?
			 */
			M_Room room = new M_Room();
			room.RoomNumber = bytes[byte_ptr++];
			int name_len = (int)bytes[byte_ptr++]&0xff;
			room.RoomName = "";
			for (int char_idx = 0; char_idx < name_len; char_idx++)
			{
				room.RoomName += (char)bytes[byte_ptr++];
			}
			room.RFAddress = Utils.toHex(((int)bytes[byte_ptr]&0xff), ((int)bytes[byte_ptr+1]&0xff), ((int)bytes[byte_ptr+2]&0xff));
			byte_ptr += 3;
			
			rooms.add(room);
			logger.debug("Found room num {}: {} with RF addr {}", room.RoomNumber, room.RoomName, room.RFAddress);
		}
		
		int num_dev = bytes[byte_ptr++];
		/* process each device to get room association */
		for (int dev_idx = 0; dev_idx < num_dev; dev_idx++)
		{
			M_dev dev = new M_dev();
			dev.DevType = DeviceType.create(bytes[byte_ptr++]);
			dev.RFAddr = Utils.toHex(((int)bytes[byte_ptr]&0xff), ((int)bytes[byte_ptr+1]&0xff), ((int)bytes[byte_ptr+2]&0xff));
			byte_ptr += 3;
			dev.SerialNum = "";
			for (int char_idx = 0; char_idx < 10; char_idx++)
			{
				dev.SerialNum += (char)bytes[byte_ptr++];
			}
			int name_len = (int)bytes[byte_ptr++]&0xff;
			dev.name = "";
			for (int char_idx = 0; char_idx < name_len; char_idx++)
			{
				dev.name += (char)bytes[byte_ptr++];
			}
			dev.RoomId = (int)bytes[byte_ptr++]&0xff;
			
			devices.add(dev);
			logger.debug("Found device {} in room {}: {} with RF addr {}", dev.SerialNum, dev.RoomId, dev.name, dev.RFAddr);
		}
		
		
	}
	
	@Override
	public void debug(Logger logger) {
		logger.debug("=== M_Message === ");
	}

	@Override
	public MessageType getType() {
		return MessageType.M;
	}
}

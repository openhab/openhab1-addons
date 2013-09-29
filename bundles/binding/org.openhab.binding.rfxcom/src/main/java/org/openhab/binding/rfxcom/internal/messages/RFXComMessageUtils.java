/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.rfxcom.internal.messages;

import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

/**
 * This class provides utilities to encode and decode RFXCOM data.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComMessageUtils {

	/**
	 * Command to reset RFXCOM controller.
	 * 
	 */
	public final static byte[] CMD_RESET = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to get RFXCOM controller status.
	 * 
	 */
	public final static byte[] CMD_STATUS = new byte[] { 0x0D, 0x00, 0x00,
			0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to save RFXCOM controller configuration.
	 * 
	 */
	public final static byte[] CMD_SAVE = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	public static Object decodePacket(byte[] data) throws IllegalArgumentException {

		Object obj = null;

		byte packetType = data[1];

		switch (packetType) {
		case (byte) 0x00:
			obj = new RFXComControlMessage(data);
			break;
		case (byte) 0x01:
			obj = new RFXComInterfaceMessage(data);
			break;
		case (byte) 0x02:
			obj = new RFXComTransmitterMessage(data);
			break;
		case (byte) 0x10:
			obj = new RFXComLighting1Message(data);
			break;
		case (byte) 0x11:
			obj = new RFXComLighting2Message(data);
			break;
		case (byte) 0x14:
			obj = new RFXComLighting5Message(data);
			break;
		case (byte) 0x18:
			obj = new RFXComCurtain1Message(data);
			break;
		case (byte) 0x20:
			obj = new RFXComSecurity1Message(data);
			break;
		case (byte) 0x50:
			obj = new RFXComTemperatureMessage(data);
			break;
		case (byte) 0x52:
			obj = new RFXComTemperatureHumidityMessage(data);
			break;
		case (byte) 0x5A:
			obj = new RFXComEnergyMessage(data);
			break;

		default:
			throw new IllegalArgumentException("Packet type " + (int) packetType
					+ " not implemented!");
		}

		return obj;
	}

	public static byte[] encodePacket(Object obj)  throws IllegalArgumentException {

		byte[] data = null;

		if (obj instanceof RFXComBaseMessage)
			data = ((RFXComBaseMessage) obj).decodeMessage();

		if( data == null ) {
			throw new IllegalArgumentException("No valid encoder implemented!");
		}
	
		return data;
	}

	public static PacketType convertPacketType(String packetType)
			throws IllegalArgumentException {

		for (PacketType p : PacketType.values()) {
			if (p.toString().equals(packetType)) {
				return p;
			}
		}

		throw new IllegalArgumentException("Unknown packet type " + packetType);
	}

	public static Object convertSubType(PacketType packetType, String subType) {

		switch (packetType) {

		case LIGHTING1:
			for (RFXComLighting1Message.SubType s : RFXComLighting1Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

		case LIGHTING2:
			for (RFXComLighting2Message.SubType s : RFXComLighting2Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

		case LIGHTING5:
			for (RFXComLighting5Message.SubType s : RFXComLighting5Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;
			
		case CURTAIN1:
			for (RFXComCurtain1Message.SubType s : RFXComCurtain1Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

		case SECURITY1:
			for (RFXComSecurity1Message.SubType s : RFXComSecurity1Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

		case TEMPERATURE_HUMIDITY:
		case INTERFACE_CONTROL:
		case INTERFACE_MESSAGE:
		case UNKNOWN:
		default:
			break;

		}

		throw new IllegalArgumentException("Unknown sub type " + subType);
	}
	
	
}

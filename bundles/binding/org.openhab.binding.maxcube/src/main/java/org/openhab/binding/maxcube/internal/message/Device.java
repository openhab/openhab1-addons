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
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.openhab.binding.maxcube.internal.Utils;
import org.openhab.core.library.types.OpenClosedType;

/**
 * Base class for devices provided by the MAX!Cube protocol.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public abstract class Device {
	private DeviceStatus deviceStatus;
	private DeviceAnswer deviceAnswer;

	protected String serialNumber;

	public Device(Configuration c) {
		this.serialNumber = c.getSerialNumber();
	}

	public abstract DeviceType getType();

	public abstract String getRFAddress();

	public abstract String getName();

	public abstract Calendar getLastUpdate();

	private static Device create(String rfAddress,
			List<Configuration> configurations) {
		Device returnValue = null;
		for (Configuration c : configurations) {
			if (c.getRFAddress().toUpperCase().equals(rfAddress.toUpperCase())) {
				switch (c.getDeviceType()) {
				case HeatingThermostat:
					return new HeatingThermostat(c);
				case ShutterContact:
					return new ShutterContact(c);
				case WallMountedThermostat:
					return new WallMountedThermostat(c);
				default:
					// TODO
					System.out.println("+++ Device Tyoe not supported in Decvice.create() " + c.getDeviceType());
				}
			}
		}
		return returnValue;
	}

	public static Device create(byte[] raw, List<Configuration> configurations) {

		if (raw.length == 0) {
			return null;
		}

		String rfAddress = Utils.toHex(raw[0] & 0xFF, raw[1] & 0xFF,
				raw[2] & 0xFF);

		// Based on the RF address and the corresponding configuration,
		// create the device based on the type specified in it's configuration

		Device device = Device.create(rfAddress, configurations);

		// byte 4 is skipped

		// multiple device information are encoded in this particular byte
		boolean[] bits = getBits(Utils.fromByte(raw[4]));

		// TODO
		// bit 1 Status initialized 0=not initialized, 1=yes
		// device.setDeviceStatus(bits[1] ? DeviceStatus.Initialized : DeviceStatus.NotInitialized);
		// DeviceStatus.NotInitialized);
		// bit 2 Answer 0=an answer to a command,1=not an answer to a command
		// device.setDeviceAnswer(bits[2] ? DeviceAnswer.NoAnswer :
		// DeviceAnswer.CommandAnswer);
		// bit 3 Error 0=no; 1=Error occurred
		// device.setDeviceError(bits[3] ? DeviceError.ErrorOccured :
		// DeviceError.NoError);
		// bit 4 Valid 0=invalid;1=information provided is valid
		// device.setDeviceInformation(bits[4] ? DeviceInformation.Valid :
		// DeviceInformation.Invalid);

		if (device.getType() == DeviceType.ShutterContact) {
			
			ShutterContact shutterContact = (ShutterContact) device;
			
			bits = getBits(Utils.fromByte(raw[5]));
			
			shutterContact.setLowBattery(bits[7]);
			
			shutterContact.setLinkError(bits[6]);
			
			shutterContact.setPanelLock(bits[5]);
			
			shutterContact.setGatewayOk(bits[4]);
			
			shutterContact.setError(bits[3]);
			
			shutterContact.setValid(bits[2]);
			
			// xxxx xx10 = shutter open, xxxx xx00 = shutter closed
			if (bits[1] == true && bits[0] == false) {
				shutterContact.setShutterState(OpenClosedType.OPEN);
			}
			else if (bits[1] == false && bits[0] == false) {
				shutterContact.setShutterState(OpenClosedType.CLOSED);
			}
			else {
				// TODO handel malformed message
			}
		}
		
		return device;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	
	private static boolean[] getBits(int value) {

		String zeroBitString = String.format("%0" + 8 + 'd', 0);
		String binaryString = Integer.toBinaryString(value);
		binaryString = zeroBitString.substring(binaryString.length())
				+ binaryString;

		boolean[] bits = new boolean[8];

		for (int pos = 7; pos > 0; pos--) {
			bits[7 - pos] = binaryString.substring(pos, pos + 1)
					.equalsIgnoreCase("1") ? true : false;
		}

		// bits are reverse order representing the original binary string
		// e.g. string "0001 0010" is bits[0] -> 0100 1000 <- bits[7]
		for (boolean bit : bits) {
			String b = bit == true ? "1" : "0";
			System.out.print(b);
		}

		return bits;
	}

	private void setInitialized(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	private void setDeviceAnswer(DeviceAnswer deviceAnswer) {
		this.deviceAnswer = deviceAnswer;
	}
}

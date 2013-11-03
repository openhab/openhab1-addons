/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;

/**
 * Command to be send via the MAX!Cube protocol.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class S_Command {

	String baseString = "000440000000";;
	boolean[] bits = null;
	String rfAddress = null;

	/**
	 * Creates a new instance of the MAX! protocol S command.
	 * 
	 * @param rfAddress
	 *            the RF address the command is for
	 * @param setpointTemperature
	 *            the desired setpoint temperature for the device.
	 */
	public S_Command(String rfAddress, double setpointTemperature) {
		this.rfAddress = rfAddress;

		int setpointValue = (int) setpointTemperature * 2;
		boolean[] setpointBits = Utils.getBits(setpointValue);

		// Temperature setpoint, Temp uses 6 bits LSB6 (bit 2 tm 7),
		// 20 deg C = bits 101000 = dec 40/2 = 20 deg C,
		// you need 8 bits to send so add the 2 bits below (sample 10101000 =
		// hex A8)
		// bit 0,1 = 00 = Auto weekprog (no temp is needed, just make the whole
		// byte 00

		bits = new boolean[setpointBits.length + 2];

		// 01 = Permanent
		// 10 = Temporarily
		bits[0] = false;
		bits[1] = true;

		for (int i = 0; i < setpointBits.length; i++) {
			bits[i + 2] = setpointBits[i];
		}
	}

	/**
	 * Returns the Base64 encoded command string to be sent via the MAX!
	 * protocol.
	 * 
	 * @return the string representing the command
	 */
	public String getCommandString() {

		String commandString = baseString + rfAddress + "01" + Utils.toHex(bits);

		byte[] encodedString = Base64.encodeBase64(commandString.getBytes());
		return "s:" + encodedString + "\r\n";
	}
}
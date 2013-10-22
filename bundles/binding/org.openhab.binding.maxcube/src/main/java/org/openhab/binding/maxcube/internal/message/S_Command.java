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

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;

/**
*  Command to be send via by the MAX!Cube protocol. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class S_Command {

	String baseString = "000440000000";;
	boolean[] bits = null;
	String rfAddress = null;
	
	public S_Command(String rfAddress, double setpointTemperature) {		
		this.rfAddress = rfAddress;
		
		int setpointValue = (int) setpointTemperature * 2;
		boolean[] setpointBits =  Utils.getBits(setpointValue);
		
		// Temperature setpoint, Temp uses 6 bits LSB6 (bit 2 tm 7),
		// 20 deg C = bits 101000 = dec 40/2 = 20 deg C,
		// you need 8 bits to send so add the 2 bits below (sample 10101000 = hex A8)
		// bit 0,1 = 00 = Auto weekprog (no temp is needed, just make the whole byte 00
		
		bits = new boolean[setpointBits.length + 2];

		// 01 = Permanent
		// 10 = Temporarily 
		bits [0] = false;
		bits [1] = true;
		
		for (int i = 0; i < setpointBits.length; i++) {
			bits[i+2] = setpointBits[i];
		}
	}

	public String getCommandString() {
		
		String commandString = baseString + rfAddress +  "01" + Utils.toHex(bits);
		
		byte[] encodedString = Base64.encodeBase64(commandString.getBytes());
		return "s:" + encodedString + "\r\n";
	}
}
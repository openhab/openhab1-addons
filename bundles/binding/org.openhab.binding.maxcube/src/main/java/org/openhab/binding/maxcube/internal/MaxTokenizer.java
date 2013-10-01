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
package org.openhab.binding.maxcube.internal;

import java.util.Enumeration;

/**
* The MaxTokenizer parses a L message into the MAX!Cube devices encoded within. The L message contains 
* real time information for multiple devices. Each device starts with the length n bytes. 
* The MaxTokenzier starts with the first device and chops off one device after another from the byte stream. 
* 
* The tokens returned consist of the payload solely, and do not contain the first byte holding the 
* tokens length.
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class MaxTokenizer implements Enumeration<byte[]> {

	private int offset = 0;
	
	private byte[] decodedRawMessage = null;
	
	/**
	 * Creates a new MaxTokenizer.
	 * @param decodedRawMessage
	 * 			The Base64 decoded MAX!Cube protocol L message as byte array
	 */
	public MaxTokenizer(byte[] decodedRawMessage) {
		this.decodedRawMessage = decodedRawMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMoreElements() {
		return offset < decodedRawMessage.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] nextElement() {
		byte length = decodedRawMessage[offset++];
		
		// make sure to get the correct length in case > 127
		byte[] token = new byte[length & 0xFF]; 
		
		for (int i = 0; i < (length & 0xFF); i++) {
			token[i] = decodedRawMessage[offset++];
		}
		
		return token;
	}
}
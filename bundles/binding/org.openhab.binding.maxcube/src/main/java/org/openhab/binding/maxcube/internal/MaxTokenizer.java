/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
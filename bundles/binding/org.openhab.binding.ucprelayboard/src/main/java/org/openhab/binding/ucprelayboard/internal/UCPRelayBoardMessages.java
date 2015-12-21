/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard.internal;
/**
 * Predefined messages
 * 
 * @author Robert Michalak
 * @since 1.8.0
 */
public class UCPRelayBoardMessages {

	public static final byte[][] ON = new byte[][] {
			new byte[] { 0x55, 0x01, 0x4f, 0x00, 0x28 },
			new byte[] { 0x55, 0x01, 0x4f, 0x01, 0x76 },
			new byte[] { 0x55, 0x01, 0x4f, 0x02, (byte) 0x94 },
			new byte[] { 0x55, 0x01, 0x4f, 0x03, (byte) 0xca },
			new byte[] { 0x55, 0x01, 0x4f, 0x04, 0x49 },
			new byte[] { 0x55, 0x01, 0x4f, 0x05, 0x17 },
			new byte[] { 0x55, 0x01, 0x4f, 0x06, (byte) 0xf5 },
			new byte[] { 0x55, 0x01, 0x4f, 0x07, (byte) 0xab } };

	public static final byte[][] OFF = new byte[][] {
			new byte[] { 0x55, 0x01, 0x46, 0x00, (byte) 0x9a },
			new byte[] { 0x55, 0x01, 0x46, 0x01, (byte) 0xc4 },
			new byte[] { 0x55, 0x01, 0x46, 0x02, 0x26 },
			new byte[] { 0x55, 0x01, 0x46, 0x03, 0x78 },
			new byte[] { 0x55, 0x01, 0x46, 0x04, (byte) 0xfb },
			new byte[] { 0x55, 0x01, 0x46, 0x05, (byte) 0xa5 },
			new byte[] { 0x55, 0x01, 0x46, 0x06, 0x47 },
			new byte[] { 0x55, 0x01, 0x46, 0x07, 0x19 } };
	
	/**
	 * This message can be sent to retrieve current state of relays.
	 * Board should respond with 5 bytes ('0x55' '0x01' '0x52' '<relay_state>' '<crc>'), 
	 * where every bit in <relay_state> is a state of one relay.
	 * 
	 * <crc> is computed using 'Dow CRC' algorithm for bytes 2-4 in a message
	 */
	public static final byte[] GET_STATE = new byte[] {0x55, 0x01, 0x47, 0x00, 0x5e};

}

/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: query for lost nodes, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
/**
 * Transport layer supported by the Velux bridge.
 * <P>
 * Module semantic: encoding and decoding of frames according to RFC 1055.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SlipEncoding {

	private final Logger logger = LoggerFactory.getLogger(SlipEncoding.class);

	private static final byte PROTOCOL_ID = 0;
	private static boolean encodingValid;
	private static byte[] message;


	/**
	 * Builds a message based on command and parameters.
	 *
	 * @param command	Message type as short.
	 * @param data	    Parameters as Array of bytes.
	 */
	public SlipEncoding(short command, byte[] data) {
		logger.trace("SlipEncoding(constructor) for command 0x{} with data size {} called.", Integer.toHexString(new Short(command).intValue()), data.length);
		if (data.length > 250) {
			logger.error("SlipEncoding(constructor) called with data size {}: too big, aborting.");
			encodingValid = false;
		} else {
			byte checksum = 0;
			message = new byte[data.length+5];
			message[0] = PROTOCOL_ID;
			message[1] = (byte) (3 + data.length);
			message[2] = (byte) (command >>> 8);
			message[3] = (byte) command;
			message[4+data.length] = 0;
			System.arraycopy(data, 0, message, 4, data.length);
			for(byte b : message) {
				checksum = (byte) (checksum ^ b);
			}
			message[4+data.length] = checksum;
			logger.trace("SlipEncoding(constructor) successfully initialized, storing bytes: {}.", this.toString());
			encodingValid = true;
		}
	}

	/**
	 * Validates a message based on transfer syntax as Array-of-bytes.
	 *
	 * @param thisPacket	Message as Array of bytes.
	 */

	public SlipEncoding(byte[] thisPacket) {
		logger.trace("SlipEncoding(constructor) called for decoding a packet with size {}.", thisPacket.length);
		message = thisPacket;
		encodingValid = false;
		do {
			// ProtocolID:Length:Command:Data(0-250):Checksum
			if (message.length < 5) {
				logger.error("SlipEncoding(constructor) called with data size {}: Packet too short.", message.length);
				break;
			}
			if (message[0] != PROTOCOL_ID) {
				logger.error("SlipEncoding(constructor) called: Unexpected PROTOCOL_ID (got {}).",Packet.shortToString(message[0]));
				break;
			}
			byte checksum = 0;
			for (int i = 0; i < message.length-1; i++){
				checksum = (byte) (checksum ^ message[i]);
			}
			if (message[message.length - 1] != checksum) {
				logger.error("SlipEncoding(constructor) Invalid packet checksum (got {} != calculated {}).",
						Packet.shortToString(message[message.length - 1]),Packet.shortToString(checksum));
				logger.debug("SlipEncoding(constructor) packet is {}.", new Packet(message).toString(":"));
				break;
			}
			logger.trace("SlipEncoding(constructor) successfully initialized with command 0x{} and data {}.",
					Packet.shortToString(this.getCommand()), new Packet(this.getData()).toString());
			encodingValid = true;
		} while (false);
	}

	/**
	 * Extracts the command.
	 *
	 * @return <b>encodingValid</b>
	 *         of type boolean as status of the encoding or decoding.
	 */
	public boolean isValid() {
		return encodingValid;
	}

	/**
	 * Extracts the command.
	 *
	 * @return <b>command</b>
	 *         of type short as encoded within the message.
	 */
	public short getCommand() {
		short command = ByteBuffer.wrap(new byte[]{message[2], message[3]}).getShort();
		logger.trace("getCommand() returns 0x{}.", String.format("%02X ", command));
		return command;
	}


	/**
	 * Extracts the data i.e. parameters to the command.
	 *
	 * @return <b>data</b>
	 *         of type Array-of-byte as encoded within the message.
	 */
	public byte[] getData() {
		byte[] data = new byte[message.length-5];
		System.arraycopy(message, 4, data, 0, message.length-5);
		logger.trace("getData() returns {} bytes: {}.", data.length,new Packet(data).toString());
		return data;
	}

	/**
	 * Returns the complete message.
	 *
	 * @return <b>message</b>
	 *         of type Array-of-byte.
	 */
	public byte[] toMessage() {
		return message;
	}

	public String toString()  {
		return new Packet(message).toString();
	}


}
/**
 * end-of-velux/bridge/comm/slip/SlipEncoding.java
 */

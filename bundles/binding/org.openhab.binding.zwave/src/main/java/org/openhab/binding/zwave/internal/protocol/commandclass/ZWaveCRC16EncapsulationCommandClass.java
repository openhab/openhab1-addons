/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the CRC 16 Encapsulation Command command class.
 * 
 * @author Hans Vanbellingen
 * @since 1.7.0
 */
@XStreamAlias("crc16EncapsulationCommandClass")
public class ZWaveCRC16EncapsulationCommandClass extends ZWaveCommandClass {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveCRC16EncapsulationCommandClass.class);

	/**
	 * Creates a new instance of the ZWaveMultiCommandCommandClass class.
	 * 
	 * @param node
	 *            the node this command class belongs to
	 * @param controller
	 *            the controller to use
	 * @param endpoint
	 *            the endpoint this Command class belongs to
	 */
	public ZWaveCRC16EncapsulationCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.CRC_16_ENCAP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpointId) {
		logger.debug("NODE {}: Received CRC 16 Encapsulation Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case 0x01: // crc 16 Encapsulation
			handleCRC16EncapResponse(serialMessage, offset + 1);
			break;
		}
	}

	/**
	 * Handle the crc16 encapsulated message. This processes the received frame,
	 * checks the crc and forwards to the real command class.
	 * 
	 * @param serialMessage
	 *            The received message
	 * @param offset
	 *            The starting offset into the payload
	 */
	private void handleCRC16EncapResponse(SerialMessage serialMessage, int offset) {
		logger.trace("Process CRC16 Encapsulation");

		// calculate CRC
		byte[] payload = serialMessage.getMessagePayload();
		byte[] messageCrc = Arrays.copyOfRange(payload, payload.length - 2, payload.length);
		byte[] tocheck = Arrays.copyOfRange(payload, offset - 2, payload.length - 2);

		short calculatedCrc = crc_ccit(tocheck);
		// check if messageCrc = calculatedCrc
		ByteBuffer byteBuffer = ByteBuffer.allocate(2);
		byteBuffer.putShort(calculatedCrc);
		if (!Arrays.equals(messageCrc, byteBuffer.array())) {
			logger.error("NODE {}: CRC check failed message contains {} but should be {}",
					this.getNode().getNodeId(), SerialMessage.bb2hex(messageCrc), SerialMessage.bb2hex(byteBuffer.array()));
			return;
		}

		// Execute underlying command
		CommandClass commandClass;
		ZWaveCommandClass zwaveCommandClass;
		int commandClassCode = serialMessage.getMessagePayloadByte(offset);
		commandClass = CommandClass.getCommandClass(commandClassCode);
		if (commandClass == null) {
			logger.error(String.format("NODE %d: Unsupported command class 0x%02x", this.getNode().getNodeId(), commandClassCode));
		} else {
			zwaveCommandClass = this.getNode().getCommandClass(commandClass);

			// Apparently, this node supports a command class that we did not
			// get (yet) during initialization.
			// Let's add it now then to support handling this message.
			if (zwaveCommandClass == null) {
				logger.debug(String.format("NODE %d: Command class %s (0x%02x) not found, trying to add it.",
								getNode().getNodeId(), commandClass.getLabel(), commandClass.getKey()));

				zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), getNode(), getController());

				if (zwaveCommandClass != null) {
					logger.debug(String.format("NODE %d: Adding command class %s (0x%02x)",
							getNode().getNodeId(), commandClass.getLabel(), commandClass.getKey()));
					getNode().addCommandClass(zwaveCommandClass);
				}
			}

			if (zwaveCommandClass == null) {
				logger.error(String.format("NODE %d: CommandClass %s (0x%02x) not implemented.",
						this.getNode().getNodeId(), commandClass.getLabel(), commandClassCode));
			} else {
				logger.debug(String.format("NODE %d: Calling handleApplicationCommandRequest.",
						this.getNode().getNodeId()));
				zwaveCommandClass.handleApplicationCommandRequest(serialMessage, offset+1, 0);
			}
		}
	}

	private short crc_ccit(final byte[] args) {
		int crc = 0x1D0F;
		int polynomial = 0x1021;
		for (byte b : args) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				// If coefficient of bit and remainder polynomial = 1 xor crc with polynomial
				if (c15 ^ bit) {
					crc ^= polynomial;
				}
			}
		}

		crc &= 0xffff;
		return (short) crc;
	}
}

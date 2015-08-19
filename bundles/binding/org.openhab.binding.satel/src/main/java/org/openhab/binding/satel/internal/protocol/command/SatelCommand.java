/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol.command;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all command classes.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public abstract class SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(SatelCommand.class);

	/**
	 * Used in extended (INT-RS v2.xx) command version.
	 */
	protected static final byte[] EXTENDED_CMD_PAYLOAD = { 0x00 };

	private static final byte RESPONSE_CODE = (byte) 0xef;

	private EventDispatcher eventDispatcher;

	/**
	 * Creates new instance with given event dispatcher. This dispatcher will be
	 * used to distribute events created during response handling.
	 * 
	 * @param eventDispatcher
	 *            event dispatcher object for all events created by this command
	 */
	public SatelCommand(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * Must be overridden in derived classes to handle response for specific
	 * command or group of commands.
	 * 
	 * @param response
	 *            module response message
	 */
	public abstract void handleResponse(SatelMessage response);

	/**
	 * Builds a message for specific command.
	 * 
	 * @param commandCode
	 *            command code
	 * @param extended
	 *            if <code>true</code> command will be sent as extended (256
	 *            zones or outputs)
	 * @return new instance of message class
	 */
	public static SatelMessage buildMessage(byte commandCode, boolean extended) {
		if (extended) {
			return new SatelMessage(commandCode, EXTENDED_CMD_PAYLOAD);
		} else {
			return new SatelMessage(commandCode);
		}
	}

	protected EventDispatcher getEventDispatcher() {
		return this.eventDispatcher;
	}

	protected static byte[] userCodeToBytes(String userCode) {
		if (userCode.length() > 8) {
			throw new IllegalArgumentException("User code too long");
		}
		byte[] bytes = new byte[8];
		int digitsNbr = 2 * bytes.length;
		for (int i = 0; i < digitsNbr; ++i) {
			if (i < userCode.length()) {
				char digit = userCode.charAt(i);
				if (!Character.isDigit(digit)) {
					throw new IllegalArgumentException("User code must contain digits only");
				}
				if (i % 2 == 0) {
					bytes[i / 2] = (byte) ((digit - '0') << 4);
				} else {
					bytes[i / 2] |= (byte) (digit - '0');
				}
			} else if (i % 2 == 0) {
				bytes[i / 2] = (byte) 0xff;
			} else if (i == userCode.length()) {
				bytes[i / 2] |= 0x0f;
			}
		}

		return bytes;
	}

	protected boolean commandSucceeded(SatelMessage response) {
		// validate response
		if (response.getCommand() != RESPONSE_CODE) {
			logger.error("Invalid response code: {}", response.getCommand());
			return false;
		}
		if (response.getPayload().length != 1) {
			logger.error("Invalid payload length: {}", response.getPayload().length);
			return false;
		}

		byte responseCode = response.getPayload()[0];
		String errorMsg;

		switch (responseCode) {
		case 0:
			// success
			return true;
		case 0x01:
			errorMsg = "Requesting user code not found";
			break;
		case 0x02:
			errorMsg = "No access";
			break;
		case 0x03:
			errorMsg = "Selected user does not exist";
			break;
		case 0x04:
			errorMsg = "Selected user already exists";
			break;
		case 0x05:
			errorMsg = "Wrong code or code already exists";
			break;
		case 0x06:
			errorMsg = "Telephone code already exists";
			break;
		case 0x07:
			errorMsg = "Changed code is the same";
			break;
		case 0x08:
			errorMsg = "Other error";
			break;
		case 0x11:
			errorMsg = "Can not arm, but can use force arm";
			break;
		case 0x12:
			errorMsg = "Can not arm";
			break;
		case (byte) 0xff:
			logger.trace("Command accepted");
			return true;
		default:
			if (responseCode >= 0x80 && responseCode <= 0x8f) {
				errorMsg = String.format("Other error: %02X", responseCode);
			} else {
				errorMsg = String.format("Unknown response code: %02X", responseCode);
			}
		}

		logger.error(errorMsg);
		return false;
	}

	protected static int bcdToInt(byte[] bytes, int offset, int size) {
		int result = 0, digit;
		int byteIdx = offset;
		int digits = size * 2;
		for (int i = 0; i < digits; ++i) {
			if (i % 2 == 0)
				digit = (bytes[byteIdx] >> 4) & 0x0f;
			else {
				digit = (bytes[byteIdx]) & 0x0f;
				byteIdx += 1;
			}
			result = result * 10 + digit;
		}
		return result;
	}
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.MaxCubeBinding;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The C message contains configuration about a MAX! device.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public final class C_Message extends Message {

	private static final Logger logger = LoggerFactory.getLogger(C_Message.class);
	
	private String rfAddress = null;
	private int length = 0;
	private DeviceType deviceType = null;
	private String serialNumber = null;
	
	public C_Message(String raw) {
		super(raw);
		
		String[] tokens = this.getPayload().split(Message.DELIMETER);

		rfAddress = tokens[0];

		byte[] bytes = Base64.decodeBase64(tokens[1].getBytes());

		int[] data = new int[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			data[i] = bytes[i] & 0xFF;
		}

		length = data[0];
		if (length != data.length - 1) {
			logger.debug("C_Message malformed: wrong data length. Expected bytes {}, actual bytes {}", length, data.length - 1);
		}

		String rfAddress2 = Utils.toHex(data[1], data[2], data[3]);
		if (!rfAddress.toUpperCase().equals(rfAddress2.toUpperCase())) {
			logger.debug("C_Message malformed: wrong RF address. Expected address {}, actual address {}", rfAddress.toUpperCase(), rfAddress2.toUpperCase());
		}

		deviceType = DeviceType.create(data[4]);

		serialNumber = getSerialNumber(bytes);
	}

	private String getSerialNumber(byte[] bytes) {
		byte[] sn = new byte[10];

		for (int i = 0; i < 10; i++) {
			sn[i] = (byte) bytes[i + 8];
		}

		try {
			return new String(sn, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("Cannot encode serial number from C message due to encoding issues.");
		}

		return "";
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	@Override
	public MessageType getType() {
		return MessageType.C;
	}

	public String getRFAddress() {
		return rfAddress;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	@Override
	public void debug(Logger logger) {
		logger.debug("=== C_Message === ");
		logger.debug("\tRAW:        " + this.getPayload());
		logger.debug("DeviceType:   " + deviceType.toString());
		logger.debug("SerialNumber: " + serialNumber);
		logger.debug("RFAddress:    " + rfAddress);
	}
}

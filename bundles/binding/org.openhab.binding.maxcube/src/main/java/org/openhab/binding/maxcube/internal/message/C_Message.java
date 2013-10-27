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

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;

/**
 * The C message contains configuration about a MAX! device.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public final class C_Message extends Message {

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
		if (length != data.length) {
			// TODO check for payload length, and throw exception if necessary
			// throw new
			// MessageMalformedException("C_Message malformed: wrong data length");
		}

		String rfAddress2 = Utils.toHex(data[1], data[2], data[3]);
		if (rfAddress != rfAddress2) {
			// TODO check for same address, and throw exception if necessary
			// throw new
			// MessageMalformedException("C_Message malformed: RF address not identical");
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
			// TODO logger...
			e.printStackTrace();
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
	}
}

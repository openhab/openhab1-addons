/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class OctetString extends ASNObject {
	private static final int MAX_LENGTH = 32000;
	protected byte[] octetString;

	public OctetString(byte[] octetString) {
		set(octetString);
		isSelected = true;
	}

	public OctetString(String octetString) {
		this(toArray(octetString));
	}

	public OctetString() {
	}

	public void set(byte[] octetString) {
		if (octetString != null) {
			if (octetString.length < MAX_LENGTH) {
				this.octetString = octetString;
			}
		}
		else {
			this.octetString = new byte[0];
		}
	}

	@Override
	public void print() {

		StringBuilder builder = new StringBuilder();
		if (!isOptional || isSelected) {
			builder.append("OctetString of length " + octetString.length + " hex: ");
			for (byte element : octetString) {
				builder.append(String.format("0x%02x ", element));
			}
			System.out.println(builder.toString());
			System.out.println("String: " + new String(octetString));
		}
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		int numTlField = 1;
		while ((Math.pow(2, 4 * numTlField) - 1 - numTlField) < octetString.length) {
			numTlField++;
		}

		for (int i = numTlField; i > 0; i--) {

			int firstFourBits = 0x00;
			if (i > 1) {
				firstFourBits = 0x80;
			}
			os.writeByte((firstFourBits & 0xff) | (((octetString.length + numTlField) >> ((i - 1) * 4)) & 0xf));

		}

		for (byte element : octetString) {
			os.write(element);
		}
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		int tlLength = 1;
		byte typeLength = is.readByte();
		if (isOptional && (typeLength == 0x01)) {
			isSelected = false;
			return true;
		}
		if (!(((typeLength & 0x70) >> 4) == 0)) {
			return false;
		}
		int length = (typeLength & 0x0f);

		while ((typeLength & 0x80) == 0x80) {
			tlLength++;
			typeLength = is.readByte();
			if (!(((typeLength & 0x70) >> 4) == 0)) {
				return false;
			}
			length = ((length & 0xffffffff) << 4) | (typeLength & 0x0f);
		}

		length = length - tlLength;

		octetString = new byte[length];

		for (int i = 0; i < length; i++) {
			octetString[i] = is.readByte();
		}

		isSelected = true;

		return true;
	}

	public byte[] getOctetString() {
		return octetString;
	}

	/**
	 * Returns a hash code for this octet string.
	 */
	@Override
	public int hashCode() {
		return java.util.Arrays.hashCode(octetString);
	}

	/**
	 * Compares this octet string to the specified object.
	 */
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof OctetString) {
			return Arrays.equals(octetString, ((OctetString) other).getOctetString());
		}
		return false;
	}

	@Override
	public String toString() {
		return new String(octetString);
	}

	/**
	 * Convert octet string to byte array. For example, both strings "test" and "0x74657374" will result in equals byte
	 * arrays.
	 */
	public static byte[] toArray(String octetString) {
		if (octetString != null) {
			int ln = octetString.length();
			if (ln > 2 && (ln & 1) == 0 && octetString.startsWith("0x")) {
				byte[] buf = new byte[(ln >> 1) - 1];
				for (int i = 2; i < ln; i += 2) {
					buf[(i >> 1) - 1] = (byte) ((Character.digit(octetString.charAt(i), 16) << 4) + Character.digit(
							octetString.charAt(i + 1), 16));
				}
				return buf;
			}
			else {
				return octetString.getBytes();
			}
		}
		return new byte[0];
	}

}

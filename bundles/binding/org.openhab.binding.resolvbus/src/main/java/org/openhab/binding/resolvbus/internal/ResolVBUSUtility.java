/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.resolvbus.model.ResolVBUSField;
import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.openhab.binding.resolvbus.model.ResolVBUSPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael Heckmann
 * @since 1.7.0
 */

public class ResolVBUSUtility {
	// Constants
	public static final int VBUS_PROTOCOL_V1 = 0x10;
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static final Logger logger = 
			LoggerFactory.getLogger(ResolVBUSUtility.class);

	/**
	 * Returns the checksum for the specified bytes.
	 * 
	 * @param buffer
	 *            the buffer where the bytes are in.
	 * @param offset
	 *            the offset from where to start calculating the checksum.
	 * @param length
	 *            the length of bytes to calculate the checksum for.
	 * @return the checksum for the specified bytes.
	 */
	
	public static byte calcChecksum(byte[] buffer, int offset, int length) {
		byte crc = 0x7F;

		for (int i = 0; i < length; i++) {
			crc = (byte) ((crc - buffer[offset + i]) & 0x7F);
		}

		return crc;
	}

	/**
	 * This method injects the outsourced MSB into the origin bytes
	 * 
	 * @param buffer
	 *            the buffer where the bytes are in.
	 * @param offset
	 *            the offset from where to start injecting the septett.
	 * @param length
	 *            the length of bytes to inject the septett to.
	 */
	public static void VBus_InjectSeptett(char[] buffer, int offset, int length) {
		char septett;
		int i;
		septett = buffer[offset + length];
		for (i = 0; i < length; i++) {
			if ((septett & (1 << i)) == (1 << i)) {
				buffer[offset + i] = (char) (buffer[offset + i] | 0x80);
			}
		}
	}
	
	public static void VBus_InjectSeptett(byte[] buffer, int offset, int length) {
		byte septett;
		int i;
		septett = buffer[offset + length];
		for (i = 0; i < length; i++) {
			if ((septett & (1 << i)) == (1 << i)) {
				buffer[offset + i] = (byte) (buffer[offset + i] | 0x80);
			}
		}
	}

	/**
	 * This methods parses the VBusPacket and stores the
	 * 
	 * @param vbPacket
	 * @param xmlPacket
	 */
	public static List<Integer> parsePacket(ResolVBUSInputStream vInputStream, ResolVBUSPacket vPacket) {
		List<Integer> valueList = new ArrayList<Integer>();

		for (ResolVBUSField field : vPacket.getField()) {
			int fieldValue = 0;
			
			// Calculate number of bytes from bitsize
			int byteLength = (field.getBitSize().intValue() + 7) / 8;
			
			int freeBits = byteLength * 8 - field.getBitSize().intValue();

			// bitSize beachten, alles da¸aber ignorieren
			for (int i = 1; i <= freeBits; i++) {
				fieldValue |= 1 << byteLength * 8 - i;
			}
//			System.out.println("Bytes to int: "+bytesToInt(vInputStream.getPayloadByte(), field.getOffset().intValue(), byteLength));
			fieldValue = ~fieldValue & bytesToInt(vInputStream.getPayloadByte(), field.getOffset().intValue(), byteLength);
			if (fieldValue > (int) (Math.pow(2, field.getBitSize().intValue()) - 1) / 2) {
				for (int i = field.getBitSize().intValue(); i < 32; i++) {
					fieldValue |= 1 << i;
				}
			}
			logger.debug("Adding Value from InputStream to ArrayList: "+fieldValue);
			valueList.add(fieldValue);
		}
		return valueList;
	}
	
	public static double getValueWithVBUSField(ResolVBUSInputStream vInputStream, ResolVBUSField field) {
		
		int fieldValue = 0;
		
		if (field == null) {
			logger.debug("No ResolVBUSfield found..returning -999 ");
			return -999;
		}
		
		// Calculate number of bytes from bitsize
		int byteLength = (field.getBitSize().intValue() + 7) / 8;
		
		int freeBits = byteLength * 8 - field.getBitSize().intValue();

		// bitSize beachten, alles da¸aber ignorieren
		for (int i = 1; i <= freeBits; i++) {
			fieldValue |= 1 << byteLength * 8 - i;
		}
//		System.out.println("Bytes to int: "+bytesToInt(vInputStream.getPayloadByte(), field.getOffset().intValue(), byteLength));
		fieldValue = ~fieldValue & bytesToInt(vInputStream.getPayloadByte(), field.getOffset().intValue(), byteLength);
		if (fieldValue > (int) (Math.pow(2, field.getBitSize().intValue()) - 1) / 2) {
			for (int i = field.getBitSize().intValue(); i < 32; i++) {
				fieldValue |= 1 << i;
			}
		}
		
		double factor;
		
		if (field.getFactor() == null)
			factor = 1;
		else
			factor = field.getFactor().doubleValue();
			
				
		return fieldValue * factor;
		
	}

	private static int bytesToInt(byte[] bytes, int offset, int length) {
		int value = 0;
		for (int i = 0; i < length; i++) {
			value |= ((int) (bytes[offset + i] & 0xff) << (8 * i));
		}
		return value;
	}
	
	public static String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String bytesToHexFormatted(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class to encode all eBus data types and telegrams.
 * 
 * @author Christian Sowada
 * @since 1.7.1
 */
public class EBusCodecUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusCodecUtils.class);

	/**
	 * Decodes a byte buffer to a number value
	 * @param type
	 * @param data
	 * @param replaceValue
	 * @return
	 */
	public static Number decode(String type, byte[] data, Number replaceValue) {

		Number n = null;
		Number repVal = 0;

		if(type.equals("data1b")) {
			n = decodeDATA1b(data[0]);
			repVal = -128;

		} else if(type.equals("data1c")) {
			n = decodeDATA1c(data[0]);
			repVal = 255;

		} else if(type.equals("data2b")) {
			n = decodeDATA2b(data);
			repVal = -128;

		} else if(type.equals("data2c")) {
			n = decodeDATA2c(data);
			repVal = -2048;

		} else if(type.equals("word") || type.equals("uint")) {
			n = decodeWord(data);
			repVal = 65535;

		} else if(type.equals("char")) {
			n = encodeChar(data[0]);
			repVal = 255;

		} else if(type.equals("int")) {
			n = decodeInt(data);
			repVal = 32768;

		} else if(type.equals("byte") || type.equals("uchar")) {
			n = decodeUChar(data[0]);
			repVal = 255;

		} else if(type.equals("bcd")) {
			n = decodeBCD(data[0]);
			repVal = 255;

		} else {
			throw new RuntimeException("Unable to encode data type \"" + type + "\", maybe currently not supported ...");
		}

		// if replace value parameter set
		if(replaceValue != null) {
			repVal = replaceValue;
		}

		BigDecimal b = NumberUtils.toBigDecimal(n);
		BigDecimal c = NumberUtils.toBigDecimal(repVal);
		
		// equals replace value, than return null
		if(b != null && c != null && b.compareTo(c) == 0) {
			logger.trace("Replace value found, skip value ...");
			n = null;
		}

		return n;
	}

	/**
	 * Encodes a number value to a byte buffer
	 * @param type
	 * @param value
	 * @return
	 */
	public static byte[] encode(String type, Object value) {

		if(value instanceof Number) {
			
			Number n = (Number) value;
			
			if(type.equals("data1b")) {
				return new byte[] {encodeDATA1b(n.byteValue())};

			} else if(type.equals("data1c")) {
				return new byte[] {encodeDATA1c(n.floatValue())};

			} else if(type.equals("data2b")) {
				return encodeDATA2b(n.floatValue());

			} else if(type.equals("data2c")) {
				return encodeDATA2c(n.floatValue());

			} else if(type.equals("word") || type.equals("uint")) {
				return encodeWord(n.shortValue());

			} else if(type.equals("int")) {
				return encodeInt(n.shortValue());

			} else if(type.equals("byte") || type.equals("uchar")) {
				return new byte[] {encodeUChar(n.byteValue())};

			} else if(type.equals("char")) {
				return new byte[] {encodeChar(n.byteValue())};

			} else if(type.equals("bcd")) {
				return new byte[] {encodeBCD(n.byteValue())};
			}
		}
		
		throw new RuntimeException("Unable to encode data type " + type + ", maybe currently not supported ...");
	}
	/**
	 * Convert the value to a bcd value
	 * @param data The encoded value
	 * @return The bcd value
	 */
	public static byte decodeBCD(byte data) {
		return (byte) ((data >> 4)*10 + (data & (byte) 0x0F));
	}

	/**
	 * Convert eBus Type CHAR
	 * @param data
	 * @return The decoded value
	 */
	public static short decodeChar(byte data) {
		return data;
	}

	/**
	 * Convert eBus Type DATA1B
	 * @param data The encoded value
	 * @return The decoded value
	 */
	public static short decodeDATA1b(byte data) {
		return decodeChar(data);
	}

	/**
	 * Convert eBus Type DATA1C
	 * @param data The encoded value
	 * @return The decoded value
	 */
	public static float decodeDATA1c(byte data) {
		return (float) (decodeUChar(data) / 2);
	}

	/**
	 * Convert eBus Type DATA2b
	 * @param data The encoded bytes
	 * @return The decoded value
	 */
	public static float decodeDATA2b(byte[] data) {
		return decodeInt(data) / 256f;
	}

	/**
	 * Convert eBus Type DATA2c
	 * @param data The encoded bytes
	 * @return The decoded value
	 */
	public static float decodeDATA2c(byte[] data) {
		return decodeInt(data) / 16f;
	}

	/**
	 * Convert eBus Type Int
	 * @param data The encoded bytes
	 * @return The decoded value
	 */
	public static short decodeInt(byte[] data) {
		return (short) (data[0]<<8 | data[1]&0xFF);
	}

	/**
	 * Convert eBus Type UCHAR
	 * @param data
	 * @return The decoded value
	 */
	public static short decodeUChar(byte data) {
		return (short) (data & 0xFF);
	}

	/**
	 * Convert eBus Type WORD
	 * @param The encoded bytes
	 * @return The decoded value
	 */
	public static short decodeWord(byte[] data) {
		return (short) (decodeInt(data) & 0xffff);
	}

	/**
	 * Encodes a BCD value
	 * @param data
	 * @return
	 */
	public static byte encodeBCD(byte data) {
		return (byte) ((byte) ((byte) (data / 10) << 4) | data % 10);
	}

	/**
	 * Encodes a CHAR value
	 * @param data
	 * @return
	 */
	public static byte encodeChar(byte data) {
		return (byte) data;
	}

	/**
	 * Encodes a DATA1b value
	 * @param data
	 * @return
	 */
	public static byte encodeDATA1b(byte data) {
		return encodeChar(data);
	}

	/**
	 * Encodes a DATA1c value
	 * @param data
	 * @return
	 */
	public static byte encodeDATA1c(float data) {
		return encodeUChar((short) (data*2));
	}

	/**
	 * Encodes a DATA2b value
	 * @param data
	 * @return
	 */
	public static byte[] encodeDATA2b(float data) {
		return encodeInt((short) (data * 256));
	}

	/**
	 * Encodes a DATA2c value
	 * @param data
	 * @return
	 */
	public static byte[] encodeDATA2c(float data) {
		return encodeInt((short) (data * 16));
	}

	/**
	 * Encodes a INT value
	 * @param data
	 * @return
	 */
	public static byte[] encodeInt(short data) {
		return new byte[] {(byte) (data>>8), (byte) data};
	}

	/**
	 * Encodes a UCHAR value
	 * @param data
	 * @return
	 */
	public static byte encodeUChar(short data) {
		return (byte) (data & 0xFF);
	}

	/**
	 * Encodes a WORD value
	 * @param data
	 * @return
	 */
	public static byte[] encodeWord(short data) {
		return encodeInt((short) (data & 0xFFFF));
	}
}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.response;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import org.openhab.binding.ekozefir.exception.ResponseBytesNumberException;

/**
 * Response from ahu driver - 51 bytes.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class Response {

	private final byte[] bytes;

	private final int expectedBytesNumber = 51;

	public Response(byte[] bytes) {
		Objects.requireNonNull(bytes);
		if (bytes.length != expectedBytesNumber) {
			throw new ResponseBytesNumberException(bytes.length);
		}
		this.bytes = bytes;
	}

	/**
	 * Check if two bytes representing temperature.
	 * 
	 * @param highByteNumber number of old byte from response
	 * @param lowByteNumber number of young byte from response
	 * @return
	 */
	public boolean isTempBytesOfNumbersOk(int highByteNumber, int lowByteNumber) {
		checkIndex(highByteNumber);
		checkIndex(lowByteNumber);
		byte highByte = bytes[highByteNumber];
		byte lowByte = bytes[lowByteNumber];
		if ((highByte & 0xFF) == 0xF0 && (lowByte & 0xFF) == 0xF0) {
			return false;
		}
		if ((highByte & 0xFF) == 0xF1 && (lowByte & 0xFF) == 0xF1) {
			return false;
		}
		return true;
	}

	/**
	 * Convert two bytes to float - temperature.
	 * 
	 * @param highNumberByte number of older byte from response
	 * @param lowNumberByte number of younger byte from response
	 * @return
	 */
	public float convertBytesOfNumberToFloat(int highNumberByte, int lowNumberByte) {
		checkIndex(highNumberByte);
		checkIndex(lowNumberByte);
		byte[] bytesValuesToConvert = new byte[2];
		bytesValuesToConvert[0] = bytes[highNumberByte];
		bytesValuesToConvert[1] = bytes[lowNumberByte];
		return ByteBuffer.wrap(bytesValuesToConvert).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10f;
	}

	/**
	 * Convert byte to int.
	 * 
	 * @param number number of byte from message
	 * @return value of byte
	 */
	public int convertByteOfNumberToInt(int number) {
		checkIndex(number);
		return bytes[number] & 0xFF;
	}

	private void checkIndex(int indexToCheck) {
		if (indexToCheck < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (indexToCheck >= 51) {
			throw new IndexOutOfBoundsException();
		}
	}
}

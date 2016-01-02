/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for parse data packets from Stiebel heat pumps
 * 
 * @author Peter Kreutzer
 * @param <T>
 *            original protocol parser was written by Robert Penz in python
 * @since 1.5.0
 * 
 *        Each response has the same structure as request header (four bytes),
 *        optional data and footer:
 * 
 *        Header: 01 Read/Write: 00 for Read (get) response, 80 for Write (set)
 *        response; in case of error during command exchange device stores error
 *        code here; know error code : 03 = unknown command Checksum: ? 1 byte -
 *        the same algorithm as for request Command: ? 1 byte - should match
 *        Request.Command Data: ? only when Read, length depends on data type
 *        Footer: 10 03
 */
public class DataParser {

	private static final Logger logger = LoggerFactory
			.getLogger(DataParser.class);

	public static byte ESCAPE = (byte) 0x10;
	public static byte HEADERSTART = (byte) 0x01;
	public static byte END = (byte) 0x03;
	public static byte GET = (byte) 0x00;
	public static byte SET = (byte) 0x80;
	public static byte STARTCOMMUNICATION = (byte) 0x02;
	public static byte[] FOOTER = { ESCAPE, END };
	public static byte[] DATAAVAILABLE = { ESCAPE, STARTCOMMUNICATION };

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public List<Request> parserConfiguration = new ArrayList<Request>();

	public DataParser() {
	}

	/**
	 * verifies response on availability of data
	 * 
	 * @param response
	 *            of heat pump
	 * @param request
	 *            request defined for heat pump response
	 * @return Map of Strings with name and values
	 */
	public Map<String, String> parseRecords(final byte[] response,
			Request request) throws StiebelHeatPumpException {

		Map<String, String> map = new HashMap<String, String>();

		logger.debug("Parse bytes: {}", DataParser.bytesToHex(response));

		if (response.length < 2) {
			logger.error("response does not have a valid length ogf bytes: {}",
					DataParser.bytesToHex(response));
			return map;
		}

		// parse response and fill map
		for (RecordDefinition recordDefinition : request.getRecordDefinitions()) {
			try {
				String value = parseRecord(response, recordDefinition);
				logger.debug("Parsed value {} -> {} with pos: {} , len: {}",
						recordDefinition.getName(), value,
						recordDefinition.getPosition(),
						recordDefinition.getLength());
				map.put(recordDefinition.getName(), value);
			} catch (StiebelHeatPumpException e) {
				continue;
			}
		}
		return map;
	}

	/**
	 * parses a single record
	 * 
	 * @param response
	 *            of heat pump
	 * @param RecordDefinition
	 *            that shall be used for parsing the heat pump response
	 * @return string value of the parse response
	 * @throws StiebelHeatPumpException
	 */
	public String parseRecord(byte[] response, RecordDefinition recordDefinition)
			throws StiebelHeatPumpException {
		try {
			if (response.length < 2) {
				logger.error(
						"response does not have a valid length of bytes: {}",
						DataParser.bytesToHex(response));
				throw new StiebelHeatPumpException();
			}
			ByteBuffer buffer = ByteBuffer.wrap(response);
			short number = 0;
			byte[] bytes = null;

			switch (recordDefinition.getLength()) {
			case 1:
				bytes = new byte[1];
				System.arraycopy(response, recordDefinition.getPosition(),
						bytes, 0, 1);
				number = Byte
						.valueOf(buffer.get(recordDefinition.getPosition()));
				break;
			case 2:
				bytes = new byte[2];
				System.arraycopy(response, recordDefinition.getPosition(),
						bytes, 0, 2);
				number = buffer
						.getShort(recordDefinition.getPosition());
				break;
			}

			if (recordDefinition.getBitPosition() > 0) {

				int returnValue = getBit(bytes,
						recordDefinition.getBitPosition());
				return String.valueOf(returnValue);
			}

			if (recordDefinition.getScale() != 1.0) {
				double myDoubleNumber = number * recordDefinition.getScale();
				myDoubleNumber = Math.round(myDoubleNumber * 100.0) / 100.0;
				String returnString = String.format("%s", myDoubleNumber);
				return returnString;
			}

			return String.valueOf(number);
		} catch (Exception e) {
			logger.error(
					"response {} could not be parsed for record definition {} ",
					DataParser.bytesToHex(response), recordDefinition.getName());
			throw new StiebelHeatPumpException();
		}
	}

	/**
	 * composes the new value of a record definition into a updated set command
	 * that can be send back to heat pump
	 * 
	 * @param response
	 *            of heat pump that should be updated with new value
	 * @param RecordDefinition
	 *            that shall be used for compose the new value into the heat
	 *            pump set command
	 * @param string
	 *            value to be compose
	 * @return byte[] ready to send to heat pump
	 * @throws StiebelHeatPumpException
	 */
	public byte[] composeRecord(String value, byte[] response,
			RecordDefinition recordDefinition) throws StiebelHeatPumpException {
		short newValue = 0;

		if (recordDefinition.getDataType() != Type.Settings) {
			logger.warn(
					"The record {} can not be set as it is not a setable value!",
					recordDefinition.getName());
			throw new StiebelHeatPumpException("record is not a setting!");
		}

		double number = Double.parseDouble(value);

		if (number > recordDefinition.getMax()
				|| number < recordDefinition.getMin()) {
			logger.warn(
					"The record {} can not be set to value {} as allowed range is {}<-->{} !",
					recordDefinition.getName(), value,
					recordDefinition.getMax(), recordDefinition.getMin());
			throw new StiebelHeatPumpException("invalid value !");
		}

		// change response byte to setting command
		response[1] = SET;

		// reverse the scale
		if (recordDefinition.getScale() != 1.0) {
			number = number / recordDefinition.getScale();
			newValue = (short) number;
		}

		// set new bit values in a byte
		if (recordDefinition.getBitPosition() > 0) {

			byte[] abyte = new byte[] { response[recordDefinition.getPosition()] };
			abyte = setBit(abyte, recordDefinition.getBitPosition(), newValue);
			response[recordDefinition.getPosition()] = abyte[0];
			return response;
		}

		// create byte values for single and double byte values
		// and update response
		switch (recordDefinition.getLength()) {
		case 1:
			byte newByteValue = (byte) number;
			response[recordDefinition.getPosition()] = newByteValue;
			break;
		case 2:
			byte[] newByteValues = shortToByte(newValue);
			int position = recordDefinition.getPosition();
			response[position] = newByteValues[1];
			response[position + 1] = newByteValues[0];
			break;
		}

		response[2] = this.calculateChecksum(response);
		response = this.addDuplicatedBytes(response);
		logger.debug("Updated record {} at position {} to value {}.",
				recordDefinition.getName(), recordDefinition.getPosition(),
				value);
		return response;
	}

	/**
	 * verifies response on availability of data
	 * 
	 * @param response
	 *            of heat pump
	 * @return true if the response of the heat pump indicates availability of
	 *         data
	 */
	public boolean dataAvailable(byte[] response)
			throws StiebelHeatPumpException {

		if (response.length == 0 || response.length > 2) {
			throw new StiebelHeatPumpException(
					"invalid response length on request of data "
							+ new String(response));
		}

		if (response[0] != ESCAPE) {
			throw new StiebelHeatPumpException(
					"invalid response on request of data "
							+ new String(response));
		}
		if (response.length == 2 && response[1] == DATAAVAILABLE[1]) {
			return true;
		}

		return false;
	}

	/**
	 * verifies the header of the heat pump response
	 * 
	 * @param response
	 *            of heat pump
	 */
	public void verifyHeader(byte[] response) throws StiebelHeatPumpException {

		if (response.length < 4) {
			throw new StiebelHeatPumpException(
					"invalide response length on request of data "
							+ new String(response));
		}

		if (response[0] != HEADERSTART) {
			throw new StiebelHeatPumpException(
					"invalid response on request of data, found no header start: "
							+ new String(response));
		}

		if (response[1] != GET & response[1] != SET) {
			throw new StiebelHeatPumpException(
					"invalid response on request of data, response is neither get nor set: "
							+ new String(response));
		}

		if (response[2] != calculateChecksum(response)) {
			throw new StiebelHeatPumpException(
					"invalid checksum on request of data "
							+ new String(response));
		}
	}

	/**
	 * verifies the header of the heat pump response
	 * 
	 * @param response
	 *            of heat pump
	 * @return true if header is valid
	 */
	public boolean headerCheck(byte[] response) {
		try {
			verifyHeader(response);
		} catch (StiebelHeatPumpException e) {
			logger.debug("verification of response failed " + e.toString());
			return false;
		}

		return true;
	}

	/**
	 * verifies the heat pump response after data has been updated
	 * 
	 * @param response
	 *            of heat pump
	 * @return true if data set has been confirmed
	 */
	public boolean setDataCheck(byte[] response) {
		try {
			verifyHeader(response);
		} catch (StiebelHeatPumpException e) {
			return false;
		}

		return true;
	}

	/**
	 * calculates the checksum of a byte data array
	 * 
	 * @param data
	 *            to calculate the checksum for
	 * @param withReplace
	 *            to set if the byte array shall be corrected by special replace
	 *            method
	 * @return calculated checksum as short
	 */
	public byte calculateChecksum(byte[] data) throws StiebelHeatPumpException {

		if (data.length < 5) {
			throw new StiebelHeatPumpException(
					"no valid byte[] for calulation of checksum!");
		}

		int checkSum = 0, i = 0;
		for (i = 0; i < data.length - 2; i++) {
			if (i == 2) {
				continue;
			}
			checkSum += (short) (data[i] & 0xFF);
		}

		return shortToByte((short) checkSum)[0];
	}

	/**
	 * converts short to byte[]
	 * 
	 * @return array of bytes
	 */
	public byte[] shortToByte(short value) {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte) (value & 0xff);
		returnByteArray[1] = (byte) ((value >> 8) & 0xff);

		return returnByteArray;
	}

	/**
	 * converts integer to byte[]
	 * 
	 * @return array of bytes
	 */
	public byte[] intToByte(int checkSum) {
		byte[] returnByteArray = ByteBuffer.allocate(4).putInt(checkSum)
				.array();
		return returnByteArray;
	}

	/**
	 * converts byte to short
	 * 
	 * @return short
	 */
	private short byteToShort(byte[] bytes) throws StiebelHeatPumpException {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	/**
	 * Search the data byte array for the first occurrence of the byte array
	 * pattern. raw data received from device have to be de-escaped before
	 * header evaluation and data use: - each sequence 2B 18 must be replaced
	 * with single byte 2B - each sequence 10 10 must be replaced with single
	 * byte 10
	 * 
	 * @param data
	 *            as byte array representing response from heat pump which shall
	 *            be fixed
	 * @return byte array with fixed byte entries
	 */
	public byte[] fixDuplicatedBytes(byte[] data) {
		
		// first copy the data except the last 2 bytes , the footer
		byte[] bytesToBeAnalyzed = new byte[data.length-2];
		System.arraycopy(data, 0, bytesToBeAnalyzed, 0, data.length-2);
		
		byte[] fixedData = findReplace(bytesToBeAnalyzed, new byte[] { (byte) 0x10,
				(byte) 0x10 }, new byte[] { (byte) 0x10 });
		fixedData = findReplace(fixedData, new byte[] { (byte) 0x2b, (byte) 0x18 },
				new byte[] { (byte) 0x2b });
		
		byte[] result = new byte[fixedData.length + FOOTER.length];
		// copy fixedData to result
		System.arraycopy(fixedData, 0, result, 0, fixedData.length);
		// copy footer to result
		System.arraycopy(FOOTER, 0, result, fixedData.length, FOOTER.length);
		
		return result;
	}

	/**
	 * Search the data byte array for the first occurrence of the byte array
	 * pattern. raw data received from device have to be de-escaped before
	 * header evaluation and data use: - each sequence 2B must be replaced with
	 * single byte 2B 18 - each sequence 10 must be replaced with single byte 10
	 * 10
	 * 
	 * @param data
	 *            as byte array representing response from heat pump which shall
	 *            be fixed
	 * @return byte array with fixed byte entries
	 */
	public byte[] addDuplicatedBytes(byte[] data) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 2);

		// add header without changes
		for (int i = 0; i < 2; i++) {
			byteBuffer.put(data[i]);
		}

		// add now duplicates
		for (int i = 2; i < data.length - 2; i++) {
			byteBuffer.put(data[i]);
			if (data[i] == (byte) 0x10) {
				byteBuffer.put(data[i]);
			}
			if (data[i] == (byte) 0x2b) {
				byteBuffer.put((byte) 0x18);
			}
		}

		// add footer without changes
		for (int i = data.length - 2; i < data.length; i++) {
			byteBuffer.put(data[i]);
		}

		byte[] newdata = new byte[byteBuffer.position()];
		byteBuffer.rewind();
		byteBuffer.get(newdata);

		return newdata;
	}

	/**
	 * Search the data byte array for the first occurrence of the byte array
	 * pattern.
	 * 
	 * @param data
	 *            as byte array to search into and to replace the pattern bytes
	 *            with replace bytes
	 * @param pattern
	 *            as byte array to search for
	 * @param replace
	 *            as byte array to replace with
	 * @return byte array which has pattern bytes been replaced with replace
	 *         bytes
	 */
	public byte[] findReplace(byte[] data, byte[] pattern, byte[] replace) {

		int position = indexOf(data, pattern);
		while (position >= 0) {
			byte[] newData = new byte[data.length - pattern.length
					+ replace.length];
			System.arraycopy(data, 0, newData, 0, position);
			System.arraycopy(replace, 0, newData, position, replace.length);
			System.arraycopy(data, position + pattern.length, newData, position
					+ replace.length, data.length - position - pattern.length);
			position = indexOf(newData, pattern);
			data = new byte[newData.length];
			System.arraycopy(newData, 0, data, 0, newData.length);
		}
		return data;
	}

	/**
	 * Search the data byte array for the first occurrence of the byte array
	 * pattern.
	 * 
	 * @param data
	 *            to find pattern in
	 * @param pattern
	 *            to be searched
	 * @return byte number were pattern was found in data
	 */
	private int indexOf(byte[] data, byte[] pattern) {
		int[] failure = computeFailure(pattern);
		int j = 0;
		for (int i = 0; i < data.length; i++) {
			while (j > 0 && pattern[j] != data[i]) {
				j = failure[j - 1];
			}
			if (pattern[j] == data[i]) {
				j++;
			}
			if (j == pattern.length) {
				return i - pattern.length + 1;
			}
		}
		return -1;
	}

	/**
	 * Computes the failure function using a boot-strapping process, where the
	 * pattern is matched against itself.
	 */
	private int[] computeFailure(byte[] pattern) {
		int[] failure = new int[pattern.length];
		int j = 0;
		for (int i = 1; i < pattern.length; i++) {
			while (j > 0 && pattern[j] != pattern[i]) {
				j = failure[j - 1];
			}
			if (pattern[j] == pattern[i]) {
				j++;
			}
			failure[i] = j;
		}
		return failure;
	}

	/**
	 * Gets one bit back from a bit string stored in a byte array at the
	 * specified position.
	 * 
	 * @param data
	 *            , byte array to pick short value from
	 * @param position
	 *            to get the bit value
	 * @return integer value 1 or 0 that represents the bit
	 */
	private int getBit(byte[] data, int pos) {
		int posByte = pos / 8;
		int posBit = pos % 8;
		byte valByte = data[posByte];
		int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
		return valInt;
	}

	/**
	 * Sets one bit to a bit string at the specified position with the specified
	 * bit value.
	 * 
	 * @param data
	 *            , byte array to pick short value from
	 * @param position
	 *            to set the bit
	 * @param value
	 *            to set the bit to (0 or 1)
	 */
	private byte[] setBit(byte[] data, int position, int value) {
		int posByte = position / 8;
		int posBit = position % 8;
		byte oldByte = data[posByte];
		oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
		byte newByte = (byte) ((value << (8 - (posBit + 1))) | oldByte);
		data[posByte] = newByte;
		return data;
	}

	/**
	 * Converts a byte array to good readable string.
	 * 
	 * @param bytes
	 *            to be converted
	 * @return string representing the bytes
	 */
	public static String bytesToHex(byte[] bytes) {
		int dwords = bytes.length / 4 + 1;
		char[] hexChars = new char[bytes.length * 3 + dwords * 4];
		int position = 0;
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			if (j % 4 == 0) {
				String str = "(" + String.format("%02d", j) + ")";
				char[] charArray = str.toCharArray();
				for (char character : charArray) {
					hexChars[position] = character;
					position++;
				}
			}
			hexChars[position] = hexArray[v >>> 4];
			position++;
			hexChars[position] = hexArray[v & 0x0F];
			position++;
			hexChars[position] = ' ';
			position++;
		}
		return new String(hexChars);
	}
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for parse data packets from Stiebel heat pumps
 * 
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @param <T>
 * @since 1.4.0
 */
public class StiebelHeatPumpDataParser {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpDataParser.class);
	
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

	public String version = "";

	public StiebelHeatPumpDataParser() {
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

		// parse response and fill map
		for (RecordDefinition recordDefinition : request.getRecordDefinitions()) {
			String value = parseRecord(response, recordDefinition);
			logger.debug("Parsed bytes {} from heatpump to {} -> {}",
					StiebelHeatPumpDataParser.bytesToHex(response), 
					recordDefinition.getName(),
					value);
			map.put(recordDefinition.getName(), value);
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
	 */
	public String parseRecord(byte[] response, RecordDefinition recordDefinition) {

		ByteBuffer buffer = ByteBuffer.wrap(response);
		short myNumber = 0;
		byte[] bytes = null;
		
		switch (recordDefinition.getLength()) {
		case 1:
			bytes = new byte[1];
			System.arraycopy(response, recordDefinition.getPosition(), bytes, 0, 1);
			myNumber = Byte.valueOf(buffer.get(recordDefinition.getPosition()));
			break;
		case 2:
			bytes = new byte[2];
			System.arraycopy(response, recordDefinition.getPosition(), bytes, 0, 2);
			myNumber = (short) buffer.getShort(recordDefinition.getPosition());
			break;
		}

		if (recordDefinition.getBitPosition() > 0) {
			
			int returnValue = getBit(bytes, recordDefinition.getBitPosition());
			return String.valueOf(returnValue);
		}

		if (recordDefinition.getScale() < 1.0) {
			double myDoubleNumber = myNumber * recordDefinition.getScale();
			myDoubleNumber = Math.round(myDoubleNumber * 100.0) / 100.0;
			String returnString = String.format("%s", myDoubleNumber);
			return returnString;
		}

		return String.valueOf(myNumber);
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
					"invalid response on request of data, response is wether get nor set: "
							+ new String(response));
		}

		if (response[2] != calculateChecksum(response)) {
			throw new StiebelHeatPumpException(
					"invalid checksum on request of data "
							+ new String(response));
		}
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
	public byte calculateChecksum(byte[] data)
			throws StiebelHeatPumpException {
		
		byte[] dataWithoutHeaderFooter = data;
		if (data.length>4){
			dataWithoutHeaderFooter = Arrays.copyOfRange(data, 3,
					data.length - 2);
		}
		
		short checkSum = 1, i = 0;
		for (i = 0; i < dataWithoutHeaderFooter.length; i++) {
			checkSum += (short) (dataWithoutHeaderFooter[i] & 0xFF);
		}

		return shortToByte(checkSum)[0];
	}

	/**
	 * converts short to byte
	 * 
	 * @return array of bytes
	 */
	public byte[] shortToByte(short value) throws StiebelHeatPumpException {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte) (value & 0xff);
		returnByteArray[1] = (byte) ((value >> 8) & 0xff);

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
	 * pattern.
	 * 
	 * @param data
	 *            as byte array representing response from heat pump
	 *            which shall be fixed
	 * @return byte array with fixed byte entries
	 */
	public byte[] fixDuplicatedBytes (byte[] data) {
		
		byte[] newdata = findReplace(data,
				new byte[] { (byte) 0x10, (byte) 0x10 },
				new byte[] { (byte) 0x10 });
		newdata = findReplace(newdata,
				new byte[] { (byte) 0x2b, (byte) 0x18 },
				new byte[] { (byte) 0x2b });
		
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
	 */
	private void setBit(byte[] data, int pos, int val) {
		int posByte = pos / 8;
		int posBit = pos % 8;
		byte oldByte = data[posByte];
		oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
		byte newByte = (byte) ((val << (8 - (posBit + 1))) | oldByte);
		data[posByte] = newByte;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}
}

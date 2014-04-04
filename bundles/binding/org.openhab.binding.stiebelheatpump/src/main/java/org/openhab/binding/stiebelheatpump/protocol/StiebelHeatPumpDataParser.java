/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;

/**
 * Class for parse data packets from Stiebel heat pumps
 * 
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.4.0
 */
public class StiebelHeatPumpDataParser {

	public static byte ESCAPE = (byte) 10;
	public static byte HEADERSTART = (byte) 01;
	public static byte END = (byte) 03;
	public static byte GET = (byte) 00;
	public static byte SET = (byte) 80;
	public static byte[] FOOTER = { ESCAPE, END };
	public static byte[] DATAAVAILABLE = { ESCAPE, (byte) 02 };

	public static List<RecordDefinition> versionRecordDefinition = new ArrayList<RecordDefinition>();
	
	public StiebelHeatPumpDataParser() {
		
	}
	
	/**
	 * verifies response on availability of data
	 * 
	 * @param response
	 *            of heat pump
	 */
	public Map<String, String> parseRecords(byte[] response, List<RecordDefinition> recordDefinitions)
			throws StiebelHeatPumpException {
		Map<String, String> map = new HashMap<String, String>(recordDefinitions.size());
		for (RecordDefinition recordDefinition : recordDefinitions) {
			String value = parseRecord(response, recordDefinition);
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
	String parseRecord(byte[] response, RecordDefinition recordDefinition) {
		
		ByteBuffer buffer = ByteBuffer.wrap(response);
		short myNumber = 0;
		
		switch (recordDefinition.getLength()) {
        case 1:  
        	myNumber = Byte.valueOf(buffer.get(recordDefinition.getPosition()));
        	break;
        case 2:  
        	myNumber = (short) buffer.getShort(recordDefinition.getPosition());
        	break;
		}
		
		if(recordDefinition.getScale()<1.0){
			double myDoubleNumber = myNumber * recordDefinition.getScale();
			myDoubleNumber= Math.round(myDoubleNumber * 100.0) / 100.0;
			String returnString = String.format("%s",myDoubleNumber);
			return returnString;
		}
		
		return String.valueOf(myNumber);
		
		// To be verified will real data
		//BigInteger bi =new BigInteger(1,ByteBuffer.wrap(byteValue).order(ByteOrder.BIG_ENDIAN).array());
		//BigDecimal bd = new BigDecimal(bi).scaleByPowerOfTen(recordDefinition.getScale());
		
		//return bd.toString();
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

		if (response[0] != DATAAVAILABLE[0]) {
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

		// 3rd byte will be the checksum
		byte[]  header = { (byte) 01, (byte) 00, (byte) 00, (byte) 0xfd };
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

		if (response[1] != GET || response[1] != SET) {
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
	 * @param data to calculate the checksum for
	 * @param withReplace to set if the byte array shall be corrected by special replace method
	 * @return calculated checksum as short
	 */
	byte calculateChecksum(byte[] data, boolean withReplace) throws StiebelHeatPumpException {
		byte[] dataWithoutHeaderFooter =  Arrays.copyOfRange(data, 3,data.length - 2);
		
		dataWithoutHeaderFooter = findReplace(dataWithoutHeaderFooter, new byte[] {(byte) 0x10, (byte) 0x10}, new byte[] {(byte) 0x10});
		dataWithoutHeaderFooter = findReplace(dataWithoutHeaderFooter, new byte[] {(byte) 0x2b, (byte) 0x18}, new byte[] {(byte) 0x2b});
		
		short checkSum = 1, i = 0;
		for (i = 0; i < dataWithoutHeaderFooter.length; i++) {
			checkSum += (short) (dataWithoutHeaderFooter[i] & 0xFF);
		}
		
		return shortToByte(checkSum)[0];
	}
	
	/**
	 * calculates the checksum of a byte data array
	 * 
	 * @param data to calculate the checksum for
	 * @return calculated checksum as short
	 */
	byte calculateChecksum(byte[] data) throws StiebelHeatPumpException {
		return calculateChecksum(data,true);
	}
	
	private byte[] shortToByte(short value)  {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte)(value & 0xff);
		returnByteArray[1] = (byte)((value>>8) & 0xff);
		
		return returnByteArray;
	}
		
	/**
	* Search the data byte array for the first occurrence
	* of the byte array pattern.
	*/
	public byte[] findReplace(byte[] data, byte[] pattern, byte[] replace) {
		
		int position = indexOf(data, pattern) ;
		while (position >= 0 ) {
			
			byte[] newData = new byte[data.length - pattern.length + replace.length];
			System.arraycopy(data,0, newData, 0, position);
			System.arraycopy(replace,0, newData, position, replace.length);
			System.arraycopy(data ,position + pattern.length, newData, position + replace.length, data.length - position - pattern.length );
			position = indexOf(newData, pattern) ;
			data = new byte[newData.length];
			System.arraycopy(newData,0, data, 0, newData.length);
		}	
		return data;
	}
	
	/**
	* Search the data byte array for the first occurrence
	* of the byte array pattern.
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
	* Computes the failure function using a boot-strapping process,
	* where the pattern is matched against itself.
	*/
	private int[] computeFailure(byte[] pattern) {
		int[] failure = new int[pattern.length];
		int j = 0;
		for (int i = 1; i < pattern.length; i++) {
			while (j>0 && pattern[j] != pattern[i]) {
				j = failure[j - 1];
				}
				if (pattern[j] == pattern[i]) {
				j++;
			}
			failure[i] = j;
		}		 
		return failure;
	}
}

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition.Type;

/**
 * Class for parse data packets from Stiebel heat pumps
 * 
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.4.0
 */
public class StiebelHeatPumpDataParser {

	public static byte ESCAPE = (byte) 10;
	public static byte END = (byte) 03;
	// 3rd byte will be the checksum
	public static byte[] HEADER = { (byte) 01, (byte) 00, (byte) 00,
			(byte) 0xfd };
	public static byte[] FOOTER = { ESCAPE, END };
	public static byte[] DATAAVAILABLE = { (byte) 10, (byte) 02 };

	public static List<RecordDefinition> versionRecordDefinition = new ArrayList<RecordDefinition>();
	
	public StiebelHeatPumpDataParser() {
		this.versionRecordDefinition.add(new RecordDefinition("version",4,1,100,Type.Status));
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
		byte[] byteValue = Arrays.copyOfRange(response,
				recordDefinition.getPosition(), recordDefinition.getPosition()
						+ recordDefinition.getLength());

		long value = ByteBuffer.wrap(byteValue).order(ByteOrder.LITTLE_ENDIAN)
				.getLong();

		value *= recordDefinition.getScale();

		return Long.toString(value);
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

		if (response.length < HEADER.length) {
			throw new StiebelHeatPumpException(
					"invalide response length on request of data "
							+ new String(response));
		}

		if (response[0] != HEADER[0] || response[1] != HEADER[1]
				|| response[3] != HEADER[3]) {
			throw new StiebelHeatPumpException(
					"invalid response on request of data "
							+ new String(response));
		}

		if (response[2] != calculateChecksum(Arrays.copyOfRange(response, 2,
				response.length - 2))) {
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
	 * @return calculated checksum as short
	 */
	short calculateChecksum(byte[] data) throws StiebelHeatPumpException {
		short checkSum = 1, i = 0;
		for (i = 0; i < data.length; i++) {
			checkSum += (short) (data[i] & 0xFF);
		}
		return checkSum;
	}
}

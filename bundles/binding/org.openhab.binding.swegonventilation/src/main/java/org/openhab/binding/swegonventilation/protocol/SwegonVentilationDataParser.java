/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import java.util.HashMap;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationCommandType;
import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;


/**
 * Class for parse data packets from Swegon ventilation system.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationDataParser {

	public static HashMap<SwegonVentilationCommandType, Integer> parseData(
			byte[] data) throws SwegonVentilationException {

		if (data[0] == (byte) 0x64) {

			@SuppressWarnings("unused")
			byte unknownByte = data[1];
			@SuppressWarnings("unused")
			byte destinationAddress = data[2];
			@SuppressWarnings("unused")
			byte sourceAddress = data[3];
			byte dataLen = data[4];
			byte msgType = data[5];

			byte[] d = new byte[dataLen - 3];

			for (int i = 8; i < (data.length - 3); i++)
				d[i - 8] = data[i];

			switch (msgType) {
			case (byte) 0x21:
				return parseMessage21(d);
			case (byte) 0x71:
				return parseMessage71(d);
			case (byte) 0x73:
				return parseMessage73(d);
			}

		} else {
			throw new SwegonVentilationException("Illegal data received, first byte mismatch!");
		}

		return null;
	}

	private static HashMap<SwegonVentilationCommandType, Integer> parseMessage21(
			byte[] data) {

		HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

		map.put(SwegonVentilationCommandType.FAN_SPEED, (int) (data[3] & 0x0F));

		return map;
	}

	private static HashMap<SwegonVentilationCommandType, Integer> parseMessage71(
			byte[] data) {

		HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

		int outdoorTemp = (int) data[0];
		int supplyTemp = (int) data[1];
		int extractTemp = (int) data[2];
		int supplyTempHeated = (int) data[3];
		int exhaustTemp = (int) data[7];

		map.put(SwegonVentilationCommandType.T1, outdoorTemp);
		map.put(SwegonVentilationCommandType.OUTDOOR_TEMP, outdoorTemp);
		map.put(SwegonVentilationCommandType.T2, supplyTemp);
		map.put(SwegonVentilationCommandType.SUPPLY_TEMP, supplyTemp);
		map.put(SwegonVentilationCommandType.T3, extractTemp);
		map.put(SwegonVentilationCommandType.EXTRACT_TEMP, extractTemp);
		map.put(SwegonVentilationCommandType.T4, (int) supplyTempHeated);
		map.put(SwegonVentilationCommandType.SUPPLY_TEMP_HEATED,
				supplyTempHeated);
		map.put(SwegonVentilationCommandType.T8, exhaustTemp);
		map.put(SwegonVentilationCommandType.EXHAUST_TEMP, exhaustTemp);

		map.put(SwegonVentilationCommandType.T5, (int) data[4]);
		map.put(SwegonVentilationCommandType.T6, (int) data[5]);
		map.put(SwegonVentilationCommandType.T7, (int) data[6]);

		map.put(SwegonVentilationCommandType.SUPPLY_AIR_FAN_SPEED,
				((int) data[10] & 0xFF) * 10);
		map.put(SwegonVentilationCommandType.EXTRACT_AIR_FAN_SPEED,
				((int) data[11] & 0xFF) * 10);
		map.put(SwegonVentilationCommandType.EFFICIENCY, (int) data[12]);

		// Calculate supply efficiency
		int efficiency = (int) (((double) supplyTemp - (double) outdoorTemp)
				/ ((double) extractTemp - (double) outdoorTemp) * 100);
		map.put(SwegonVentilationCommandType.EFFICIENCY_SUPPLY, efficiency);

		// Calculate extract efficiency
		efficiency = (int) (((double) extractTemp - (double) exhaustTemp)
				/ ((double) extractTemp - (double) outdoorTemp) * 100);
		map.put(SwegonVentilationCommandType.EFFICIENCY_EXTRACT, efficiency);

		return map;
	}

	private static HashMap<SwegonVentilationCommandType, Integer> parseMessage73(
			byte[] data) {

		HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

		map.put(SwegonVentilationCommandType.REHEAT_STATE,
				((int) data[0] & (int) 0x80) > 0 ? 1 : 0);
		map.put(SwegonVentilationCommandType.PREHEAT_STATE,
				((int) data[0] & (int) 0x10) > 0 ? 1 : 0);

		return map;
	}

}

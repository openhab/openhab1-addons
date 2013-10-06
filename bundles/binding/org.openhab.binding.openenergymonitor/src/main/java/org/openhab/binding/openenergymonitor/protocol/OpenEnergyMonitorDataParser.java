/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.openenergymonitor.protocol;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Class for parse data packets from Open Energy Monitor devices.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorDataParser {

	private HashMap<String, OpenEnergyMonitorParserRule> parsingRules = null;
	
	public OpenEnergyMonitorDataParser(
			HashMap<String, OpenEnergyMonitorParserRule> parsingRules) {
		
		this.parsingRules = parsingRules;
	}

	public HashMap<String, Long> parseData(byte[] data) {
		
		HashMap<String, Long> variables = new HashMap<String, Long>();
				
		byte address = data[0];
		
		for (Entry<String, OpenEnergyMonitorParserRule> entry : parsingRules.entrySet()) { 
			
			OpenEnergyMonitorParserRule rule = entry.getValue();
			
			if (rule.getAddress() == address) {
				
				variables.put(entry.getKey(), toLong(rule.getParseBytes(), data));
			}
		}
		
		return variables;
	}
	
	private long toLong(int[] byteIndexes, byte[] data) {
		long value = 0;

		for (int i=0; i< byteIndexes.length; i++) {
			value = (value << 8) | (data[ byteIndexes[i] ]  & 0xFF);
		}

		return value;
	}
	
	
}

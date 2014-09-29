/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;

/**
 * Class for present data parser rule.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorParserRule {
	
	public enum DataType {
		U8, U16, U32, S8, S16, S32, S64, FLOAT, DOUBLE;
	}

	/** RegEx to extract a parse a data type String <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern EXTRACT_DATA_TYPE_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");

	byte address = 0;
	DataType dataType = DataType.U32;
	int[] bytesIndex = null;
	
	public OpenEnergyMonitorParserRule(String rule) throws OpenEnergyMonitorException {
		try {
			
			String[] parts = rule.split(":");
			
			if(parts.length != 2) {
				throw new OpenEnergyMonitorException("Invalid parser rule '" + rule + "'");
			}
			
			// Address
			this.address = Byte.parseByte(parts[0]);

			// Data type
			Matcher matcher = EXTRACT_DATA_TYPE_PATTERN.matcher(parts[1]);
			
			if (!matcher.matches()) {
				throw new OpenEnergyMonitorException("Invalid parser rule '" + rule + "', given data type '" + parts[1] + "' does not follow the expected pattern '<DataType>(<pattern>)'");
			}
			
			matcher.reset();
			
			matcher.find();			
			String dataType = matcher.group(1);
			
			try {
				this.dataType = DataType.valueOf(dataType);	
			} catch(IllegalArgumentException e) {
				throw new OpenEnergyMonitorException("Invalid parser rule '" + rule + "', unknown data type");
			}
			
			// Byte indexes
			String[] b = matcher.group(2).split("\\|");
			
			if (b.length > 8) {
				throw new OpenEnergyMonitorException("Invalid parser rule '" + rule + "', too many bytes");
			}
			
			bytesIndex = new int[b.length];
			
			for (int i=0; i<b.length; i++) {
				bytesIndex[i] = Integer.parseInt(b[i]);
			}
			
		} catch (Exception e) {
			throw new OpenEnergyMonitorException("Invalid parser rule", e);
		}
	}
	
	public byte getAddress() {
		return address;
	}

	public DataType getDataType() {
		return dataType;
	}

	public int[] getParseBytes() {
		return bytesIndex;
	}

}
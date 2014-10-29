/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.datatypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Class to handle temperature values 
 * 1 byte, encoded in Fahrenheit with a offset of -90, result in °C
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */

public class DataTypeTemperatureExtra implements DavisDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {
		byte value = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).get(valueType.getDataOffset());		
		return new DecimalType(((double)value -90 - 32) * 5 / 9.0);
	}
	
}

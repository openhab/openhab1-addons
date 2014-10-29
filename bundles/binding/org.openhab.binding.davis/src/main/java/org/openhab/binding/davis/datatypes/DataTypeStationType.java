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

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * Class to handle voltage values
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeStationType implements DavisDataType {

	protected String getStationType(byte n) {
		String stationType = null;
		switch (n) {
			case 0:
				stationType = "Wizard III";
				break;
			case 1:
				stationType = "Wizard II";
				break;
			case 2:
				stationType = "Monitor";
				break;
			case 3:
				stationType = "Perception";
				break;
			case 16:
				stationType = "Vantage Pro or Vantage Pro 2";
				break;
		}
		return stationType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {
		byte value = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).get(valueType.getDataOffset());	
		return new StringType(getStationType(value));
	}

}

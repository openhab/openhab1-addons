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
public class DataTypeBarometricTrend implements DavisDataType {

	protected String getTrendText(int n) {
		String trend = null;
		switch (n) {
			case -60:
				trend = "Falling Rapidly";
				break;
			case -20:
				trend = "Falling Slowly";
				break;
			case 0:
				trend = "Steady";
				break;
			case 20:
				trend = "Rising Slowly";
				break;
			case 60:
				trend = "Rising Rapidly";
				break;
			case 80:
				trend = "no trend info available";
				break;
			default:
				trend = "unkown";
				break;
		}
		return trend;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {
		byte value = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).get(valueType.getDataOffset());	
		return new StringType(getTrendText(value));
	}

}

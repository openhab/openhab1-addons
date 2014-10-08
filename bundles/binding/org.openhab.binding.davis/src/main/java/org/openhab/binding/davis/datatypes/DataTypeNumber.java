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
 * Class to handle numeric values
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeNumber implements DavisDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {

		ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		DecimalType res = null;
		
		switch (valueType.getDataSize()) {
			case 1:
				res = new DecimalType(bb.get(valueType.getDataOffset()));
				break;
			case 2:
				res = new DecimalType(bb.getShort(valueType.getDataOffset()));
				break;
			case 4:
				res = new DecimalType(bb.getInt(valueType.getDataOffset()));
				break;
			default:
				res = null;
				break;
		}
		
		return res;
	}
	
}

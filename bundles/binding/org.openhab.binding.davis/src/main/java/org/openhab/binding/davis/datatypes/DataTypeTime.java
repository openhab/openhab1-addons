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
import java.util.Calendar;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.State;

/**
 * Class to handle date/time
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeTime implements DavisDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {
		ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1900 + bb.get(valueType.getDataOffset()+5));
        cal.set(Calendar.MONTH, bb.get(valueType.getDataOffset()+4));
        cal.set(Calendar.DAY_OF_MONTH, bb.get(valueType.getDataOffset()+3));
        cal.set(Calendar.HOUR_OF_DAY, bb.get(valueType.getDataOffset()+2));
        cal.set(Calendar.MINUTE, bb.get(valueType.getDataOffset()+1));
        cal.set(Calendar.SECOND, bb.get(valueType.getDataOffset()+0));
		return new DateTimeType(cal);
	}	
}

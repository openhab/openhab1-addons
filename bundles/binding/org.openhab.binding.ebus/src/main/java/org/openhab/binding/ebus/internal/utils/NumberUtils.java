/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.utils;

import java.math.BigDecimal;

/**
 * A utility class for numbers.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class NumberUtils {

	/**
	 * Convert number object to BigDecimal
	 * @param obj Any kind of primitive datatype
	 * @return A converted BigDecimal
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		
		if(obj instanceof Integer) {
			return BigDecimal.valueOf((Integer)obj);
		} else if (obj instanceof Long ) {
			return BigDecimal.valueOf((Long)obj);
		} else if (obj instanceof Short ) {
			return BigDecimal.valueOf((Short)obj);
		} else if (obj instanceof Byte ) {
			return BigDecimal.valueOf((Byte)obj);
		} else if (obj instanceof Double ) {
			return BigDecimal.valueOf((Double)obj);
		} else if (obj instanceof Float ) {
			return BigDecimal.valueOf((Float)obj);
		} else if(obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		}

		return null;
	}
	
}

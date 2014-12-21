/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.utils;

import java.math.BigDecimal;

/**
 * @author Christian Sowada
 * @since 1.7.0
 */
public class NumberUtils {

	/**
	 * Convert number object to BigDecimal
	 * @param obj
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		
		if(obj instanceof Integer) {
			return BigDecimal.valueOf((int)obj);
		} else if (obj instanceof Long ) {
			return BigDecimal.valueOf((long)obj);
		} else if (obj instanceof Short ) {
			return BigDecimal.valueOf((short)obj);
		} else if (obj instanceof Double ) {
			return BigDecimal.valueOf((double)obj);
		} else if (obj instanceof Float ) {
			return BigDecimal.valueOf((float)obj);
		} else if(obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		}

		return null;
	}
	
}

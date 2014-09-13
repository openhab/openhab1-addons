/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter;

/**
 * Definition of the ConverterTypes.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */

public enum ConverterType {
	NONE, AUTO, INTEGER, DOUBLE, STRING, PERCENT_INTEGER, UNIX_DATE, FRACTION_INTEGER, UTC_DATE, DATE, FULL_UTC_DATE, SIMPLE_DATE, MULTI_ID, WIND_MPS, DOUBLE_3H;
}

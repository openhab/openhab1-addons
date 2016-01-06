/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Converts pressure trends to values.
 * 
 * @author Gerhard Riegler
 * @since 1.7.0
 */

public class PressureTrendConverter implements Converter<String> {
	public static final String TREND_EQUAL = "equal";
	public static final String TREND_UP = "up";
	public static final String TREND_DOWN = "down";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convert(String value) throws Exception {
		if (StringUtils.isBlank(value)) {
			return null;
		} else if ("0".equals(value)) {
			return TREND_EQUAL;
		} else if ("+".equals(value)) {
			return TREND_UP;
		} else if ("-".equals(value)) {
			return TREND_DOWN;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.PRESSURE_TREND;
	}

}

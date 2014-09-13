/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Converts a string with a value for three hours to a one hour double value.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ThreeHoursDoubleConverter implements Converter<Double> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double convert(String value) {
		Double dblValue = Double.valueOf(value);
		if (dblValue != null) {
			return dblValue / 3;
		}
		return dblValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.DOUBLE_3H;
	}

}

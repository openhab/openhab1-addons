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
 * Converts a string containing a fraction number to a integer value.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class FractionIntegerConverter implements Converter<Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer convert(String value) {
		Double val = Double.valueOf(value) * 100;
		return val.intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.FRACTION_INTEGER;
	}

}

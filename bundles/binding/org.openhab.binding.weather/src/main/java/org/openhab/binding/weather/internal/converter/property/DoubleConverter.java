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
 * Converts a string to a double value.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class DoubleConverter implements Converter<Double> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double convert(String value) {
		return Double.valueOf(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.DOUBLE;
	}

}

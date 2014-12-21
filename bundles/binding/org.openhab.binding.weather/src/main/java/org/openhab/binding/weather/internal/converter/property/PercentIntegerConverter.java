/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Converts a string holding a percent value to a integer value.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PercentIntegerConverter extends IntegerConverter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer convert(String value) {
		return super.convert(StringUtils.remove(value, "%"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.PERCENT_INTEGER;
	}

}

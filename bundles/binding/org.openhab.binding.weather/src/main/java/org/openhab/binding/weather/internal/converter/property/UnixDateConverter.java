/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import java.util.Calendar;

import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter for a unix timestamp.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UnixDateConverter implements Converter<Calendar> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Calendar convert(String value) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.valueOf(value) * 1000);
		return cal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.UNIX_DATE;
	}

}

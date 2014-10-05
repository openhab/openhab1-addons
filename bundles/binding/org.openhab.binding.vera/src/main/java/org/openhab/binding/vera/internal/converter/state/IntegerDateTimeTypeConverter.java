/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter.state;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openhab.binding.vera.internal.converter.VeraStateConverter;
import org.openhab.core.library.types.DateTimeType;

/**
 * Converts an {@link Integer} value into a {@link DateTimeType} state.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class IntegerDateTimeTypeConverter extends VeraStateConverter<Integer, DateTimeType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DateTimeType convert(Integer value) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(value.longValue() * 1000);
		return new DateTimeType(calendar);
	}

}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import java.util.Calendar;
import java.util.Date;

import org.openhab.core.library.types.DateTimeType;

/**
 * Converts from a {@link Date} to a {@link DateTimeType}
 * @author Ben Jones
 * @since 1.4.0
 */
public class DateDateTimeTypeConverter extends
		ZWaveStateConverter<Date, DateTimeType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DateTimeType convert(Date value) {
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		return new DateTimeType(calendar);
	}

}

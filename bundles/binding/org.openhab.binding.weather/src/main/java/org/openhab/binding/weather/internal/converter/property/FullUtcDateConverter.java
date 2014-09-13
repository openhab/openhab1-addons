/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter with a full UTC pattern.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class FullUtcDateConverter extends AbstractDateConverter {
	private static final String DATE_PATTERN = "dd MMM yyyy hh:mm a";

	public FullUtcDateConverter() {
		super(DATE_PATTERN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Calendar convert(String value) throws ParseException {
		String val = StringUtils.substringAfter(value, ",");
		if ("".equals(val)) {
			val = value;
		}
		return super.convert(val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.FULL_UTC_DATE;
	}

}

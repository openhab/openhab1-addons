/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.cosem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.library.types.DateTimeType;

/**
 * CosemDate represents a datetime value
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class CosemDate extends CosemValue<DateTimeType> {

	/**
	 * Creates a new CosemDate
	 * 
	 * @param unit
	 *            the unit of the value
	 * @param bindingSuffix
	 *            the suffix to use for the DSMR binding identifier
	 */
	public CosemDate(String unit, String bindingSuffix) {
		super(unit, bindingSuffix);
	}

	/**
	 * Parses a String value to an openHAB DateTimeType
	 * <p>
	 * The input string must be in the format yyMMddHHmmssX
	 * <p>
	 * Based on the DSMR specification X is:
	 * <p>
	 * <ul>
	 * <li>''. Valid for DSMR v3 specification
	 * <li>'S'. Specifies a summer time (DST = 1) datetime
	 * <li>'W'. Specifies a winter time (DST = 0) datetime
	 * </ul>
	 * 
	 * @param cosemValue
	 *            the value to parse
	 * @return {@link DateTimeType} on success
	 * @throws ParseException
	 *             if parsing failed
	 */
	@Override
	protected DateTimeType parse(String cosemValue) throws ParseException {
		/*
		 * Ignore the DST setting. We use local time that has already DST
		 */
		Pattern p = Pattern.compile("(\\d{12})([S,W]?)");

		Matcher m = p.matcher(cosemValue);

		if (m.matches()) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

			Date date = formatter.parse(m.group(1));

			Calendar c = Calendar.getInstance();
			c.setTime(date);

			return new DateTimeType(c);
		} else {
			throw new ParseException("value: " + cosemValue
					+ " is not a valid CosemDate string (yyMMddHHmmss)", 0);
		}
	}
}

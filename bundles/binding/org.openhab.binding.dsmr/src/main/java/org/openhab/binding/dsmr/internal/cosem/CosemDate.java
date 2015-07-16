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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CosemDate represents a datetime value and will try to autodetect the format
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class CosemDate extends CosemValue<DateTimeType> {
	// logger
	private static final Logger logger = LoggerFactory
			.getLogger(CosemDate.class);

	/**
	 * This enum contains the known date formats for the DSMR-specification
	 */
	private enum CosemDateFormat {
		/*
		 * Ignore DST setting for general format. We use local time that is
		 * already DST
		 */
		COSEM_DATE_GENERAL("(\\d{12})([S,W]?)", "yyMMddHHmmss"), 
		COSEM_DATE_DSMR_V2("(\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})",
				"yy-MM-dd HH:mm:ss");

		/* Cached compiled pattern */
		final Pattern pattern;

		/* Cached java date formatter */
		final SimpleDateFormat formatter;

		/**
		 * Constructs a new CosemDateFormat
		 * 
		 * @param regex
		 *            String containing the regular expression to check the
		 *            value against (the date format should at least contain 1
		 *            regex group
		 * @param javaDateFormat
		 *            String containing the datetime format to use for parsing
		 */
		private CosemDateFormat(String regex, String javaDateFormat) {
			pattern = Pattern.compile(regex);
			formatter = new SimpleDateFormat(javaDateFormat);
		}
	}

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
		for (CosemDateFormat cosemDateFormat : CosemDateFormat.values()) {
			logger.debug("Trying pattern:" + cosemDateFormat.pattern);

			Matcher m = cosemDateFormat.pattern.matcher(cosemValue);

			if (m.matches()) {
				logger.debug(cosemValue + " matches pattern:"
						+ cosemDateFormat.pattern);

				Date date = cosemDateFormat.formatter.parse(m.group(1));

				Calendar c = Calendar.getInstance();
				c.setTime(date);

				return new DateTimeType(c);
			}
			logger.debug(cosemValue + " does not match pattern:"
					+ cosemDateFormat.pattern);
		}
		throw new ParseException("value: " + cosemValue
				+ " is not a known CosemDate string", 0);
	}
}

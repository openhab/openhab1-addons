/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static java.util.Calendar.HOUR_OF_DAY;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/**
 * @author Thomas.Eichstaedt-Engelen
 * @author Neil Renaud
 * @since 1.5.0
 */
public class DateTimeTypeTest {
	private final static String OTHER_TIMEZONE_CODE = "CET";
 	private final static String BROKEN_TIMEZONE_CODE = "Broken";

  	private final static TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();
 	private final static TimeZone OTHER_TIMEZONE = TimeZone
 			.getTimeZone(OTHER_TIMEZONE_CODE);

  	private SimpleDateFormat dateFormatterDefaultTz;
 	private SimpleDateFormat dateFormatterOtherTz;
 	private SimpleDateFormat hourFormatterDefaultTz;
 	private SimpleDateFormat hourFormatterOtherTz;

  	private String inputNoTz;
 	private String inputCET;
 	private String inputWithBrokenTZ;

  	long expectedHourOtherTimezone;
 	long expectedHourLocalTimezone;

  	@Before
	public void setup() {
 		dateFormatterDefaultTz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 		dateFormatterDefaultTz.setTimeZone(DEFAULT_TIMEZONE);

  		dateFormatterOtherTz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 		dateFormatterOtherTz.setTimeZone(OTHER_TIMEZONE);

  		hourFormatterDefaultTz = new SimpleDateFormat("HH");
 		hourFormatterDefaultTz.setTimeZone(DEFAULT_TIMEZONE);

  		hourFormatterOtherTz = new SimpleDateFormat("HH");
 		hourFormatterOtherTz.setTimeZone(OTHER_TIMEZONE);

  		Calendar now = Calendar.getInstance();
 		now.set(HOUR_OF_DAY, 11);
  		inputNoTz = dateFormatterDefaultTz.format(now.getTime());
 		inputCET = dateFormatterOtherTz.format(now.getTime())
 				+ OTHER_TIMEZONE.getID();
 		inputWithBrokenTZ = inputNoTz + BROKEN_TIMEZONE_CODE;

  		expectedHourLocalTimezone = Integer.valueOf(hourFormatterDefaultTz
 				.format(now.getTime()));
 		expectedHourOtherTimezone = Integer.valueOf(hourFormatterOtherTz
 				.format(now.getTime()));
 	}

  	@Test
  	@Ignore
 	public void createDate() {
 		DateTimeType dt = DateTimeType.valueOf(inputNoTz);
 		assertEquals(expectedHourLocalTimezone,
 				dt.getCalendar().get(HOUR_OF_DAY));
 	}

  	@Test
  	@Ignore
 	public void createDateWithTz() {
 		DateTimeType dt = DateTimeType.valueOf(inputCET);
 		assertEquals(expectedHourOtherTimezone,
 				dt.getCalendar().get(HOUR_OF_DAY));
 	}

  	@Test
  	@Ignore
 	public void createDateWithBrokenTz() {
 		DateTimeType dt = DateTimeType.valueOf(inputWithBrokenTZ);
 		assertEquals(expectedHourLocalTimezone,
 				dt.getCalendar().get(HOUR_OF_DAY));
 	}
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static java.util.Calendar.HOUR;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.5.0
 */
public class DateTimeTypeTest {
	
	Calendar calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	
	String inputUTC = "2014-03-30T04:58:47";
	String inputCET = inputUTC + "CET";
	String inputWithBrokenTZ = inputUTC + "brokenTZ";
	
	int expectedUTC = 4;
	int expectedCET = 5;
	
	@Before
	public void setup() {
		calendarUTC.set(2014, 2, 30, 4, 58, 47);
	}

	@Test
	public void createDate() {
		DateTimeType dt = DateTimeType.valueOf(inputUTC);
		assertEquals(expectedUTC, dt.getCalendar().get(HOUR));
	}
	
	@Test
	public void createDateWithTz() {
		DateTimeType dt = DateTimeType.valueOf(inputCET);
		assertEquals(expectedCET, dt.getCalendar().get(HOUR));
	}
	
	@Test
	public void createDateWithBrokenTz() {
		DateTimeType dt = DateTimeType.valueOf(inputWithBrokenTZ);
		assertEquals(expectedUTC, dt.getCalendar().get(HOUR));
	}
	
}

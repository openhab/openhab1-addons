/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.5.0
 */
public class DateTimeTypeTest {
	
	String input = "2014-03-30T04:58:47";
	String inputWithTZ = "2014-03-30T04:58:47UTC";
	String inputWithBrokenTZ = "2014-03-30T04:58:47UTS";
	
	String expectedDateTime = "2014-03-30T04:58:47";
	String expectedDateTimeWithTZ = "2014-03-30T06:58:47";

	@Test
	public void createDate() {
		DateTimeType dt = DateTimeType.valueOf(input);
		assertEquals(expectedDateTime, dt.toString());
	}
	
	@Test
	public void createDateWithTz() {
		DateTimeType dt = DateTimeType.valueOf(inputWithTZ);
		assertEquals(expectedDateTimeWithTZ, dt.toString());
	}
	
	@Test
	public void createDateWithBrokenTz() {
		DateTimeType dt = DateTimeType.valueOf(inputWithBrokenTZ);
		assertEquals(expectedDateTime, dt.toString());
	}
	
}

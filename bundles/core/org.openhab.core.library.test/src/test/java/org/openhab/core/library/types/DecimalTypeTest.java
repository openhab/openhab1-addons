/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.text.DecimalFormatSymbols;

import org.junit.Test;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.1.0
 */
public class DecimalTypeTest {

	@Test
	public void testEquals() {
		DecimalType dt1 = new DecimalType("142.8");
		DecimalType dt2 = new DecimalType("142.8");
		DecimalType dt3 = new DecimalType("99.7");
		PercentType pt = new PercentType("99.7");

		assertEquals(true, dt1.equals(dt2));
		assertEquals(false, dt1.equals(dt3));
		assertEquals(true, dt3.equals(pt));
		assertEquals(false, dt1.equals(pt));
	}

	@Test
	public void testIntFormat() {
		DecimalType dt;

		// Basic test with an integer value.
		dt = new DecimalType("87");
		assertEquals("87", dt.format("%d"));

		// Again an integer value, but this time an "advanced" pattern.
		dt = new DecimalType("87");
		assertEquals(" 87", dt.format("%3d"));

		// Again an integer value, but this time an "advanced" pattern.
		dt = new DecimalType("87");
		assertEquals("0x57", dt.format("%#x"));

		// A float value cannot be converted into hex.
		dt = new DecimalType("87.5");
		try {
			dt.format("%x");
			fail();
		} catch (Exception e) {
			// That's what we expect.
		}

		// An integer (with different representation) with int conversion.
		dt = new DecimalType("11.0");
		assertEquals("11", dt.format("%d"));
	}

	@Test
	public void testFloatFormat() {
		DecimalType dt;

		// We know that DecimalType calls "String.format()" without a locale. So
		// we have to do the same thing here in order to get the right decimal
		// separator.
		final char sep = (new DecimalFormatSymbols().getDecimalSeparator());

		// A float value with float conversion.
		dt = new DecimalType("11.123");
		assertEquals("11" + sep + "1", dt.format("%.1f")); // "11.1"

		// An integer value with float conversion. This has to work.
		dt = new DecimalType("11");
		assertEquals("11" + sep + "0", dt.format("%.1f")); // "11.0"

		// An integer value with float conversion. This has to work.
		dt = new DecimalType("11.0");
		assertEquals("11" + sep + "0", dt.format("%.1f")); // "11.0"
	}
}

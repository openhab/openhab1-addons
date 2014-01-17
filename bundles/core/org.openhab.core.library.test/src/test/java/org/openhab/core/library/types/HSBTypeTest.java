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
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class HSBTypeTest {

	@Test
	public void testEquals() {

		HSBType hsb1 = new HSBType("53,86,1");
		HSBType hsb2 = new HSBType("53,86,1");
		assertTrue(hsb1.equals(hsb2));

		hsb1 = new HSBType("0,0,0");
		hsb2 = new HSBType("0,0,0");
		assertTrue(hsb1.equals(hsb2));

	}

	@Test
	public void testHsbToRgbConversion() {

		compareValues("0,100,100", 255, 0, 0); // red
		compareValues("360,100,100", 255, 0, 0); // red
		compareValues("0,0,0", 0, 0, 0); // black
		compareValues("0,0,100", 255, 255, 255); // white
		compareValues("120,100,100", 0, 255, 0); // green
		compareValues("240,100,100", 0, 0, 255); // blue
		compareValues("229,37,62", 100, 110, 158); // blueish
		compareValues("316,69,47", 120, 37, 98); // purple
		compareValues("60,60,60", 153, 153, 61); // green
		compareValues("300,100,40", 102, 0, 102);

	}

	private int convertPercentToByte(PercentType percent) {
		return percent.value.multiply(BigDecimal.valueOf(255)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).intValue();
	}

	private void compareValues(String hsbValues, int red, int green, int blue) {

		HSBType hsb = new HSBType(hsbValues);

		System.out.println("HSB INPUT: " + hsbValues);
		System.out.println("RGB EXPECTED: " + red + "," + green + "," + blue);
		System.out.println("RGB ACTUAL (0-100): " + hsb.getRed() + "," + hsb.getGreen() + "," + hsb.getBlue());
		System.out.println("RGB ACTUAL (0-255): " + convertPercentToByte(hsb.getRed()) + "," + convertPercentToByte(hsb.getGreen()) + ","
				+ convertPercentToByte(hsb.getBlue()) + "\n");

		assertEquals(red, convertPercentToByte(hsb.getRed()));
		assertEquals(green, convertPercentToByte(hsb.getGreen()));
		assertEquals(blue, convertPercentToByte(hsb.getBlue()));

	}
}

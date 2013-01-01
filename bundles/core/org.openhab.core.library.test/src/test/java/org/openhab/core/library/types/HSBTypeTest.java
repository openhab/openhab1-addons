/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

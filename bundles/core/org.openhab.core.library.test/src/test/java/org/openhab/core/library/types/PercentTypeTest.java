/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public class PercentTypeTest {

	@Test(expected=IllegalArgumentException.class)	
	public void negativeNumber() {
		new PercentType(-3);
	}

	@Test(expected=IllegalArgumentException.class)	
	public void MoreThan100() {
		new PercentType("100.2");
	}

	@Test
	public void DoubleValue() {
		PercentType pt = new PercentType("0.0001");
		assertEquals("0.0001", pt.toString());
	}

	@Test
	public void IntValue() {
		PercentType pt = new PercentType(100);
		assertEquals("100", pt.toString());
	}

}

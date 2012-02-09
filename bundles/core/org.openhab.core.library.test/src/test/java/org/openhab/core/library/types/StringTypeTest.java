/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.junit.Test;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
public class StringTypeTest {

	@Test
	public void testEquals() {
		StringType expected1 = new StringType("expected1");
		StringType expected2 = new StringType("expected2");
		
		assertEquals(expected1.hashCode(), new StringType("expected1").hashCode());
		assertEquals(expected2.hashCode(), new StringType("expected2").hashCode());
		assertEquals(false, expected1.hashCode() == new StringType("expected2").hashCode());
		
		assertEquals(true, expected1.equals(new StringType("expected1")));
		assertEquals(true, expected2.equals(new StringType("expected2")));
		assertEquals(false, expected1.equals(new StringType("expected2")));
		assertEquals(false, expected2.equals(new StringType("expected1")));
	}

}

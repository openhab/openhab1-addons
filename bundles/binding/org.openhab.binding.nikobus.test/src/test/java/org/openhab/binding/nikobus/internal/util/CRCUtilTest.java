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
package org.openhab.binding.nikobus.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.nikobus.internal.util.CRCUtil;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class CRCUtilTest {

	@Test
	public void canAppendCRC() {

		assertEquals("156C94FF0000FFFF00FF0AF3",
				CRCUtil.appendCRC("156C94FF0000FFFF00FF"));
		assertEquals("16D3C30000000000FFFF46EA",
				CRCUtil.appendCRC("16D3C30000000000FFFF"));
		assertEquals("15D3C3FF0000000000FF51AB",
				CRCUtil.appendCRC("15D3C3FF0000000000FF"));
		assertEquals("15D3C3FF0000000000FF51AB",
				CRCUtil.appendCRC("15d3c3ff0000000000ff"));
		assertEquals("15D3C300FF00000000FFA065",
				CRCUtil.appendCRC("15D3C300FF00000000FF"));
		assertEquals("15D3C30000FF000000FFAE75",
				CRCUtil.appendCRC("15D3C30000FF000000FF"));
		assertEquals("15D3C3000000FF0000FFBF79",
				CRCUtil.appendCRC("15D3C3000000FF0000FF"));
		assertEquals("15D3C3000000FF0000FFBF79",
				CRCUtil.appendCRC("15D3C3000000FF0000FF"));
		assertEquals("15D3C300000000FF00FF3BB9",
				CRCUtil.appendCRC("15D3C300000000FF00FF"));
		assertEquals("15D3C30000000000FFFFF725",
				CRCUtil.appendCRC("15D3C30000000000FFFF"));
		assertEquals("16D3C3FF0000000000FFE064",
				CRCUtil.appendCRC("16D3C3FF0000000000FF"));
		assertEquals("16D3C300FF00000000FF11AA",
				CRCUtil.appendCRC("16D3C300FF00000000FF"));
		assertEquals("16D3C30000FF000000FF1FBA",
				CRCUtil.appendCRC("16D3C30000FF000000FF"));
		assertEquals("16D3C30000000000FFFF46EA",
				CRCUtil.appendCRC("16D3C30000000000FFFF"));
		assertEquals("16D3C300000000FF00FF8A76",
				CRCUtil.appendCRC("16D3C300000000FF00FF"));
		assertEquals("126C946CE5", CRCUtil.appendCRC("126C94"));
	}

}

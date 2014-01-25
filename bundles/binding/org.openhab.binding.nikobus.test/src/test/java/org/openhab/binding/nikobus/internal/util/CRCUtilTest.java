/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

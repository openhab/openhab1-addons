/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.em.internal;

import junit.framework.Assert;

import org.junit.Test;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;

/**
 * Class to test the parsing of the received binary messages
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class ParsingUtilsTest {

	private static String[] EM_DATA = { "E0205BFCB0402000300F7", "E0205C0CD0402000300FA", "E0205C4D5040200030004" };

	@Test
	public void testParsingEMData() throws Exception {

		String address = ParsingUtils.parseAddress(EM_DATA[0]);
		Assert.assertEquals("05", address);

		EMType type = ParsingUtils.parseType(EM_DATA[0]);
		Assert.assertEquals(EMType.EM100EM, type);

		int cumulatedValue = ParsingUtils.parseCumulatedValue(EM_DATA[0]);
		Assert.assertEquals(1227, cumulatedValue);

		int oldCumulatedValue = 0;
		for (String data : EM_DATA) {
			int cumValue = ParsingUtils.parseCumulatedValue(data);
			Assert.assertTrue(cumValue > oldCumulatedValue);
			oldCumulatedValue = cumValue;
		}
	}

}

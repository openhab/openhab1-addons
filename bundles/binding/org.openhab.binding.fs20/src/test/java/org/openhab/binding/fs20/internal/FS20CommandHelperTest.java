/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.fs20.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;

public class FS20CommandHelperTest {
	@Test
	public void testConvertHABCommandToFS20Command() {
		Command command = new PercentType(80);
		FS20Command fs20Command = FS20CommandHelper.convertHABCommandToFS20Command(command);
		
		// Test for issue: FS20 - Converting Value to raw-message fails #1635
		// see: https://github.com/openhab/openhab/issues/1635
		assertNotEquals(fs20Command, FS20Command.UNKNOWN);
		assertNotEquals(fs20Command.toString(), null);
		
		// excepted value:
		assertEquals(fs20Command, FS20Command.DIM_12);
	}
}

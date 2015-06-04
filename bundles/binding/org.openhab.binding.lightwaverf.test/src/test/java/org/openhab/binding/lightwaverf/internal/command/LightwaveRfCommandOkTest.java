/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfCommandOkTest {

	@Test
	public void test() throws Exception {
		String message = "10,OK\n";
		LightwaveRfCommandOk command = new LightwaveRfCommandOk(message);
		assertEquals("010,OK\n", command.getLightwaveRfCommandString());
	}

}

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

public class LightwaveRfVersionMessageTest {

	@Test
	public void testValidMessage() throws Exception {
		String message = "1,?V=\"U2.91Q\"\r\n";
		
		LightwaveRfVersionMessage versionMessage = new LightwaveRfVersionMessage(message);
		assertEquals("001,?V=\"U2.91Q\"\n", versionMessage.getLightwaveRfCommandString());
	}

}

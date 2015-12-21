/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import org.junit.Test;
import static org.junit.Assert.*;

public class LightwaveRfRequestHeatInfoTest {
	
	@Test
	public void test() throws Exception {
		String message = "105,!R3F*r\n";
		LightwaveRfRoomMessage command = new LightwaveRfHeatInfoRequest(message);
		assertEquals("3", command.getRoomId());
		assertEquals("105,!R3F*r\n",command.getLightwaveRfCommandString());
	}

}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import org.junit.Ignore;
import org.junit.Test;

public class LightwaveRFReceiverTest {

	@Test
	@Ignore(value="This is a functional test to check the code is working")
	public void test() throws Exception {
		LightwaveRFReceiver receiver9760 = new LightwaveRFReceiver(new LightwaverfConvertor(), 9760);
		receiver9760.start();
		Thread.sleep(5 * 1000 * 60);	
	}
}

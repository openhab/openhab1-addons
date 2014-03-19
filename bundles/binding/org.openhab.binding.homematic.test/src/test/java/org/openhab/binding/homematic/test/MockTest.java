/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;

public class MockTest {

    @Test
    public void testGetValueForNPEs() {
        CCUMock ccuMock = new CCUMock();
        String address = "mock";
        HMPhysicalDeviceMock device = ccuMock.getPhysicalDevice(address);
        int channelIndex = 1;
        HMChannel channel = device.getChannel(channelIndex);
        assertNotNull(channel);
    }
}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.model.item.binding.BindingConfigParseException;

public class ParameterAddressTest {

    @Test
    public void fromAddressAndParameterKey() throws BindingConfigParseException {
        HomematicParameterAddress address = HomematicParameterAddress.from("BLABLA:1", "KEY");
        assertEquals("BLABLA", address.getDeviceId());
        assertEquals("1", address.getChannelId());
        assertEquals("KEY", address.getParameterId());
    }

    @Test
    public void testToString() throws BindingConfigParseException {
        HomematicParameterAddress address = new HomematicParameterAddress("BLABLA", "1", "KEY");
        assertEquals("{deviceId=BLABLA, channelId=1, parameterId=KEY}", address.toString());
    }

    @Test
    public void testEquals() throws BindingConfigParseException {
        HomematicParameterAddress address1 = new HomematicParameterAddress("BLABLA", "1", "KEY");
        HomematicParameterAddress address2 = new HomematicParameterAddress("BLABLA", "1", "KEY");
        assertEquals("equals", address1, address2);
    }
}

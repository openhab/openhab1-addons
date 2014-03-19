/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.types.OpenClosedType;

public class RHSSensorContactItemTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new ContactItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
        provider.setParameterAddress(HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, ParameterKey.STATE.name()));
        ccu.getPhysicalDevice(MOCK_PARAM_ADDRESS).getDeviceDescription().setType("HM-Sec-RHS");
    }

    @Test
    public void allBindingsChangedClosed() {
        String sensor = "0";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.CLOSED);
    }

    @Test
    public void allBindingsChangedTilted() {
        String sensor = "1";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void allBindingsChangedOpen() {
        String sensor = "2";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void receiveSensorUpdateClosed() {
        String sensor = "0";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.CLOSED);
    }

    @Test
    public void receiveSensorUpdateTilted() {
        String sensor = "1";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void receiveSensorUpdateOpen() {
        String sensor = "2";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }
}

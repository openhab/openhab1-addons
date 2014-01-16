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
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class BrightnessTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedDark() {
        String sensor = "0";
        checkInitialValue(ParameterKey.BRIGHTNESS, new Integer(sensor), new DecimalType(sensor));
    }

    @Test
    public void allBindingsChangedBright() {
        checkInitialValue(ParameterKey.BRIGHTNESS, new Integer(255), new DecimalType(255));
    }

    @Test
    public void receiveSensorUpdateDark() {
        String sensor = "0";
        checkValueReceived(ParameterKey.BRIGHTNESS, new Integer(sensor), new DecimalType(sensor));
    }

    @Test
    public void receiveSensorUpdateBright() {
        checkValueReceived(ParameterKey.BRIGHTNESS, new Integer(255), new DecimalType(255));
    }

}

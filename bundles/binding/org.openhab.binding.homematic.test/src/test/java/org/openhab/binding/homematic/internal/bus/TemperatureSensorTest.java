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
import org.openhab.core.library.types.PercentType;

public class TemperatureSensorTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedQueryTemperture() {
        String temperature = "20.1";
        checkInitialValue(ParameterKey.TEMPERATURE, new Double(temperature), new DecimalType(temperature));
    }

    @Test
    public void allBindingsChangedQueryHumidity() {
        String humidity = "45";
        checkInitialValue(ParameterKey.HUMIDITY, new Integer(humidity), new PercentType(humidity));
    }

    @Test
    public void receiveTemperatureUpdate() {
        String temperature = "20.1";
        checkValueReceived(ParameterKey.TEMPERATURE, new Double(temperature), new DecimalType(temperature));
    }

    @Test
    public void receiveHumidityUpdate() {
        String humidity = "45";
        checkValueReceived(ParameterKey.HUMIDITY, new Integer(humidity), new PercentType(humidity));
    }
}

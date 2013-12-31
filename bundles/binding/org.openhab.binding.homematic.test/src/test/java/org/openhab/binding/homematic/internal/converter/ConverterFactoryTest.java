/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigLocator;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.test.CCUMock;
import org.openhab.binding.homematic.test.HMPhysicalDeviceMock;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class ConverterFactoryTest {
    private static final Class<DecimalType> OPEN_HAB_STATE = DecimalType.class;
    private static final Class<PercentType> OPEN_HAB_STATE_2 = PercentType.class;
    private static final Class<OnOffType> OPEN_HAB_COMMAND = OnOffType.class;
    private static final String PARAMETER_KEY = "TEST";

    private HomematicParameterAddress parameterAddress = new HomematicParameterAddress("TEST", null, PARAMETER_KEY);

    private CCU<HMPhysicalDeviceMock> ccu = new CCUMock();

    @Test
    public void testAddStateConverter() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
    }

    @Test
    public void testAddCommandConverter() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addCommandConverter(PARAMETER_KEY, OPEN_HAB_COMMAND, TestOnOffConverter.class);
    }

    @Test
    public void testGetToStateConverter() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(parameterAddress, new NumberItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestDecimalTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetToStateCustomConverter() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        factory.addCustomConverter(parameterAddress, TestPercentageTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(parameterAddress, new NumberItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestPercentageTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetToStateConverterMultipleOHTypesNumber() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE_2, TestPercentageTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(parameterAddress, new NumberItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestDecimalTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetToStateConverterMultipleOHTypesPercentage() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE_2, TestPercentageTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(parameterAddress, new DimmerItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestPercentageTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetCommandConverter() {
        ConverterFactory factory = new ConverterFactory(ccu);
        factory.addCommandConverter(PARAMETER_KEY, OPEN_HAB_COMMAND, TestOnOffConverter.class);
        CommandConverter<?, ?> toConverter = factory.getCommandConverter(parameterAddress, OnOffType.OFF);
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestOnOffConverter.class, toConverter.getClass());
    }

    @Test
    public void testAddConfiguredDevices() {
        ConverterFactory factory = new ConverterFactory(ccu);
        ccu.getPhysicalDevice(PARAMETER_KEY).getDeviceDescription().setType("HM-LC-Bl1-FM");
        List<ConfiguredDevice> configuredDevices = new DeviceConfigLocator("example_device_config.xml").findAll();
        factory.addConfiguredDevices(configuredDevices);
        StateConverter<?, ?> toStateConverter = factory.getToStateConverter(new HomematicParameterAddress("TEST", null, "LEVEL"),
                new RollershutterItem("TEST"));
        assertEquals(InvertedDoublePercentageConverter.class, toStateConverter.getClass());
    }
}

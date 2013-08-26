/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

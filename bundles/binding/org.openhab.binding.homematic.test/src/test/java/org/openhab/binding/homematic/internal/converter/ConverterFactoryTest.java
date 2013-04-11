/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.junit.Test;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class ConverterFactoryTest {
    private static final Class<DecimalType> OPEN_HAB_STATE = DecimalType.class;
    private static final Class<PercentType> OPEN_HAB_STATE_2 = PercentType.class;
    private static final Class<OnOffType> OPEN_HAB_COMMAND = OnOffType.class;
    private static final String PARAMETER_KEY = "TEST";

    @Test
    public void testAddStateConverter() {
        ConverterFactory factory = new ConverterFactory();
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
    }

    @Test
    public void testAddCommandConverter() {
        ConverterFactory factory = new ConverterFactory();
        factory.addCommandConverter(PARAMETER_KEY, OPEN_HAB_COMMAND, TestOnOffConverter.class);
    }

    @Test
    public void testGetToStateConverter() {
        ConverterFactory factory = new ConverterFactory();
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(PARAMETER_KEY, new NumberItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestDecimalTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetToStateConverterMultipleOHTypesNumber() {
        ConverterFactory factory = new ConverterFactory();
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE_2, TestPercentageTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(PARAMETER_KEY, new NumberItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestDecimalTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetToStateConverterMultipleOHTypesPercentage() {
        ConverterFactory factory = new ConverterFactory();
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        factory.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE_2, TestPercentageTypeConverter.class);
        StateConverter<?, ?> toConverter = factory.getToStateConverter(PARAMETER_KEY, new DimmerItem(PARAMETER_KEY));
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestPercentageTypeConverter.class, toConverter.getClass());
    }

    @Test
    public void testGetCommandConverter() {
        ConverterFactory factory = new ConverterFactory();
        factory.addCommandConverter(PARAMETER_KEY, OPEN_HAB_COMMAND, TestOnOffConverter.class);
        CommandConverter<?, ?> toConverter = factory.getCommandConverter(PARAMETER_KEY, OnOffType.OFF);
        assertNotNull("toConverter", toConverter);
        assertEquals("ToConverter Class", TestOnOffConverter.class, toConverter.getClass());
    }

}

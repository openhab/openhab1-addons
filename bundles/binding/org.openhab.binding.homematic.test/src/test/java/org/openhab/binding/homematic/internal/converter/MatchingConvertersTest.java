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

import java.util.List;

import org.junit.Test;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class MatchingConvertersTest {

    private static final Class<DecimalType> OPEN_HAB_STATE = DecimalType.class;
    private static final Class<OnOffType> OPEN_HAB_COMMAND = OnOffType.class;
    private static final String PARAMETER_KEY = "TEST";


    @Test
    public void testGetMatchingStates() {
        MatchingConverters converters = new MatchingConverters();
        converters.addStateConverter(PARAMETER_KEY, OPEN_HAB_STATE, TestDecimalTypeConverter.class);
        List<Class<? extends State>> openHABTypes = converters.getMatchingStates(PARAMETER_KEY);
        assertEquals("getOpenHABTypes.size", 1, openHABTypes.size());
        assertEquals("Type", OPEN_HAB_STATE, openHABTypes.get(0));
    }
    @Test
    public void testGetMatchingCommands() {
        MatchingConverters converters = new MatchingConverters();
        converters.addCommandConverter(PARAMETER_KEY, OPEN_HAB_COMMAND, TestOnOffConverter.class);
        List<Class<? extends Command>> openHABTypes = converters.getMatchingCommands(PARAMETER_KEY);
        assertEquals("getOpenHABTypes.size", 1, openHABTypes.size());
        assertEquals("Type", OPEN_HAB_COMMAND, openHABTypes.get(0));
    }

}

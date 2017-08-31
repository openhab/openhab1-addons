/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.openhab.binding.modbus.internal.ItemIOConnection.IOType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.UnDefType;

public class ItemIOConnectionTestCase {

    @Test
    public void testSupportsStateShouldReturnFalseWithCommandType() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, ItemIOConnection.TRIGGER_DEFAULT);

        assertFalse(connection.supportsState(new DecimalType(), false, false));
        assertFalse(connection.supportsState(new DecimalType(), false, true));
        assertFalse(connection.supportsState(new DecimalType(), true, false));
        assertFalse(connection.supportsState(new DecimalType(), true, true));

    }

    @Test
    public void testSupportsStateShouldReturnFalseWithCommandType2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, "*");

        assertFalse(connection.supportsState(new DecimalType(), false, false));
        assertFalse(connection.supportsState(new DecimalType(), false, true));
        assertFalse(connection.supportsState(new DecimalType(), true, false));
        assertFalse(connection.supportsState(new DecimalType(), true, true));

    }

    @Test
    public void testSupportsStateWithDefaultTriggerUnchangedValue() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, ItemIOConnection.TRIGGER_DEFAULT);

        // value not changed, slave setting updateunchanged=false, -> False
        assertFalse(connection.supportsState(new DecimalType(), false, false));
        // value not changed, slave setting updateunchanged=true, -> False
        assertTrue(connection.supportsState(new DecimalType(), false, true));
    }

    @Test
    public void testSupportsStateWithDefaultTriggerChangedValue() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, ItemIOConnection.TRIGGER_DEFAULT);

        // should always update changed values with default trigger
        assertTrue(connection.supportsState(new DecimalType(), true, false));
        assertTrue(connection.supportsState(new DecimalType(), true, true));
    }

    @Test
    public void testSupportsStateWithChangedTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE,
                ItemIOConnection.POLL_STATE_CHANGE_TRIGGER);

        assertTrue(connection.supportsState(new DecimalType(), true, false));
        assertTrue(connection.supportsState(new DecimalType(), true, true));

        assertFalse(connection.supportsState(new DecimalType(), false, false));
        assertFalse(connection.supportsState(new DecimalType(), false, true));
    }

    @Test
    public void testSupportsStateWithSpecificMatchingTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "5");

        assertTrue(connection.supportsState(new DecimalType(5), false, false));
        assertTrue(connection.supportsState(new DecimalType(5), false, true));
        assertTrue(connection.supportsState(new DecimalType(5), true, false));
        assertTrue(connection.supportsState(new DecimalType(5), true, true));

        assertTrue(connection.supportsState(new StringType("5"), false, false));
        assertTrue(connection.supportsState(new StringType("5"), false, true));
        assertTrue(connection.supportsState(new StringType("5"), true, false));
        assertTrue(connection.supportsState(new StringType("5"), true, true));
    }

    @Test
    public void testSupportsStateWithSpecificMatchingTrigger2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "ON");

        assertTrue(connection.supportsState(OnOffType.ON, false, false));
        assertTrue(connection.supportsState(OnOffType.ON, false, true));
        assertTrue(connection.supportsState(OnOffType.ON, true, false));
        assertTrue(connection.supportsState(new StringType("oN"), true, true));
    }

    @Test
    public void testSupportsStateWithWildcardTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "*");

        assertTrue(connection.supportsState(OnOffType.ON, false, false));
        assertTrue(connection.supportsState(new DecimalType(3.3), false, true));
        assertTrue(connection.supportsState(OnOffType.ON, true, false));
        assertTrue(connection.supportsState(new StringType("xxx"), true, true));
    }

    @Test
    public void testSupportsStateWithSpecificNonMatchingTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "5");

        assertFalse(connection.supportsState(new DecimalType(5.2), false, false));
        assertFalse(connection.supportsState(new DecimalType(5.4), false, true));
        assertFalse(connection.supportsState(new DecimalType(-5), true, false));
        assertFalse(connection.supportsState(new DecimalType(5.1), true, true));

        assertFalse(connection.supportsState(new StringType("5.1"), false, false));
        assertFalse(connection.supportsState(new StringType("5x"), false, true));
        assertFalse(connection.supportsState(new StringType("5a"), true, false));
        assertFalse(connection.supportsState(UnDefType.UNDEF, true, true));
    }

    @Test
    public void testSupportsStateWithSpecificNonMatchingTrigger2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "ON");

        assertFalse(connection.supportsState(OnOffType.OFF, false, false));
        assertFalse(connection.supportsState(OnOffType.OFF, false, true));
        assertFalse(connection.supportsState(OnOffType.OFF, true, false));
        assertFalse(connection.supportsState(new StringType("OFF"), true, true));
    }

    @Test
    public void testSupportsCommandShouldReturnFalseWithStateType() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, ItemIOConnection.TRIGGER_DEFAULT);
        assertFalse(connection.supportsCommand(new DecimalType()));
    }

    @Test
    public void testSupportsCommandShouldReturnFalseWithStateType2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "*");
        assertFalse(connection.supportsCommand(new DecimalType()));
    }

    @Test
    public void testSupportsCommandWithDefaultTriggerAlwaysTrue() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, ItemIOConnection.TRIGGER_DEFAULT);
        assertTrue(connection.supportsCommand(new DecimalType()));
        assertTrue(connection.supportsCommand(new StringType("ff")));
        assertTrue(connection.supportsCommand(OnOffType.OFF));
    }

    @Test
    public void testSupportsCommandWithMatchingTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, "5");

        assertTrue(connection.supportsCommand(new DecimalType(5)));
        assertTrue(connection.supportsCommand(new DecimalType(5)));
        assertTrue(connection.supportsCommand(new DecimalType(5)));
        assertTrue(connection.supportsCommand(new DecimalType(5)));

        assertTrue(connection.supportsCommand(new StringType("5")));
        assertTrue(connection.supportsCommand(new StringType("5")));
        assertTrue(connection.supportsCommand(new StringType("5")));
        assertTrue(connection.supportsCommand(new StringType("5")));
    }

    @Test
    public void testSupportsCommandWithMatchingTrigger2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, "ON");

        assertTrue(connection.supportsCommand(OnOffType.ON));
        assertTrue(connection.supportsCommand(new StringType("oN")));
    }

    @Test
    public void testSupportsCommandWithSpecificNonMatchingTrigger() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, "5");

        assertFalse(connection.supportsCommand(new DecimalType(5.2)));
        assertFalse(connection.supportsCommand(new DecimalType(5.4)));
        assertFalse(connection.supportsCommand(new DecimalType(-5)));
        assertFalse(connection.supportsCommand(new DecimalType(5.1)));

        assertFalse(connection.supportsCommand(new StringType("5.1")));
        assertFalse(connection.supportsCommand(new StringType("5x")));
        assertFalse(connection.supportsCommand(new StringType("5a")));
    }

    @Test
    public void testSupportsCommandWithSpecificNonMatchingTrigger2() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.COMMAND, "ON");

        assertFalse(connection.supportsCommand(OnOffType.OFF));
        assertFalse(connection.supportsCommand(OnOffType.OFF));
        assertFalse(connection.supportsCommand(OnOffType.OFF));
        assertFalse(connection.supportsCommand(new StringType("OFF")));
    }

    public void testGetEffectiveValueTypeWithNonDefaultValueType() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "ON", null, "foobar");
        assertThat("foobar", is(equalTo(connection.getEffectiveValueType("defval"))));
    }

    public void testGetEffectiveValueTypeWithDefaultValueType() {
        ItemIOConnection connection = new ItemIOConnection("", 0, IOType.STATE, "ON", null,
                ItemIOConnection.VALUETYPE_DEFAULT);
        assertThat("defval", is(equalTo(connection.getEffectiveValueType("defval"))));
    }

}

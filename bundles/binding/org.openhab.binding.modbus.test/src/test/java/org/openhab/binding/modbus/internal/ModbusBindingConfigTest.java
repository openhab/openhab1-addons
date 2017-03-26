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
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.internal.ItemIOConnection.IOType;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.model.item.binding.BindingConfigParseException;

@RunWith(Parameterized.class)
public class ModbusBindingConfigTest {

    private static StringItem stringItemWithState(String name, StringType state) {
        StringItem stringItem = new StringItem(name);
        stringItem.setState(state);
        return stringItem;
    }

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> parameters = Arrays
                .<Object[]> asList(
                        // Simple case, one index
                        new Object[] { new StringItem("item1"), "slave1:99",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE) },
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.COMMAND) }, null },

                        // Simple case, one index, initial state
                        new Object[] { stringItemWithState("item1", new StringType("foobar")), "slave1:99",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE) },
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.COMMAND) }, null },

                        // Simple, read and write index different
                        new Object[] { new StringItem("item1"), "slave1:<100:>99",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 100, IOType.STATE) },
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.COMMAND) }, null },

                        // Simple, read and write index different, order different
                        new Object[] { new StringItem("item1"), "slave1:>99:<100",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 100, IOType.STATE) },
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.COMMAND) }, null },
                        // invalid, index missing
                        new Object[] { new StringItem("item1"), "slave1", null, null,
                                new BindingConfigParseException(
                                        "Invalid number of registers in item 'item1' configuration") },
                        // invalid, two write index
                        new Object[] { new StringItem("item1"), "slave1:>99:>100", null, null,
                                new BindingConfigParseException("Register references should be either :X or :<X:>Y") },
                        // invalid, two read index
                        new Object[] { new StringItem("item1"), "slave1:<99:<100", null, null,
                                new BindingConfigParseException("Register references should be either :X or :<X:>Y") },
                        // invalid, one read index
                        new Object[] { new StringItem("item1"), "slave1:<99", null, null,
                                new BindingConfigParseException(
                                        "Item 'item1' config ('slave1:<99') parsing failed: java.lang.NumberFormatException: For input "
                                                + "string: \"<99\"") },

                        // extended, single read using keywords
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=TRANSFORMATION,valueType=int32]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                        new Transformation("TRANSFORMATION", null, null), "int32") },

                                new ItemIOConnection[0], null },

                        // extended, single read using keywords, some parameters omitted, and order different
                        new Object[] { new StringItem("item2"), "<[slave1:99:transformation=TRANSFORMATION]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE, "default",
                                        new Transformation("TRANSFORMATION", null, null), "default") },
                                new ItemIOConnection[0], null },
                        // extended, single read, default transformation
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=default,valueType=int32]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                        Transformation.IDENTITY_TRANSFORMATION, "int32") },
                                new ItemIOConnection[0], null },
                        // extended, single read, default transformation, case insensitive type
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=default,valueType=int32]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                        Transformation.IDENTITY_TRANSFORMATION, "int32") },
                                new ItemIOConnection[0], null },
                        // extended, single read, valid transformation
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                        new Transformation("", "JS", "getValue.js"), "int32") },
                                new ItemIOConnection[0], null },
                        // extended, two reads, valid transformations
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32],<[slave2:98:trigger=*,transformation=FUN(getValue2.js),valueType=uint16]",
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.STATE, "*",
                                                new Transformation("", "FUN", "getValue2.js"), "uint16") },
                                new ItemIOConnection[0], null },
                        // extended, two reads, valid transformations; whitespace around comma
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t ,"
                                        + "<[slave2:98:trigger=*,transformation=\"REGEX(,.*,)\",valueType=uint16]",
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.STATE, "*",
                                                new Transformation("", "REGEX", ",.*,"), "uint16") },
                                new ItemIOConnection[0], null },
                        // extended, three reads, valid transformations; whitespace around comma
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t ,"
                                        + "<[slave2:98:trigger=*,transformation=\"REGEX(,.*,)\",valueType=uint16],"
                                        + "<[slave3:97:trigger=*,transformation=\"REGEX(,.*,)\",valueType=float32]",
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.STATE, "*",
                                                new Transformation("", "REGEX", ",.*,"), "uint16"),
                                        new ItemIOConnection("slave3", 97, IOType.STATE, "*",
                                                new Transformation("", "REGEX", ",.*,"), "float32") },
                                new ItemIOConnection[0], null },
                        // extended, two reads, valid transformations; comma in the beginning,tricky transform
                        new Object[] { new StringItem("item2"),
                                ",<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t ,"
                                        + "<[slave2:98:trigger=*,transformation=\"REGEX(,.*[],(.*))\",valueType=uint16]",
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.STATE, "*",
                                                new Transformation("", "REGEX", ",.*[],(.*)"), "uint16") },
                                new ItemIOConnection[0], null },
                        // extended, two reads, valid transformations; comma in the beginning, transform with
                        // java-quoted
                        // characters
                        new Object[] { new StringItem("item2"), // FIXME
                                ",<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t  ,   <[slave2:98:trigger=*,transformation=\"REGEX(\\\",.*[:]),)\",valueType=uint16]",
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.STATE, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.STATE, "*",
                                                new Transformation("", "REGEX", "\",.*[:]),"), "uint16") },
                                new ItemIOConnection[0], null },
                        // extended, two reads, valid transformations; comma in the beginning, transform quoted but
                        // without
                        // proper java quoting. Parsing will fail
                        new Object[] { new StringItem("item2"),
                                "<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t  ,   <[slave2:98:trigger=*,transformation=\"REGEX(\",.*[:]),)\",valueType=uint16]",
                                null, null,
                                new BindingConfigParseException(
                                        "Parsing of item 'item2' configuration '\t  ,   <[slave2:98:trigger=*,transformation=\"REGEX(\",.*[:]' (as part of the whole config '<[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t  ,   <[slave2:98:trigger=*,transformation=\"REGEX(\",.*[:]),)\",valueType=uint16]') failed: org.openhab.model.item.binding.BindingConfigParseException Invalid token '.*[:', expecting key=value") },
                        // one write only, valid transformations; space in the beginning, transform quoted
                        new Object[] { new StringItem("item2"),
                                " >[slave3:97:trigger=ThisIsTrigger,transformation=\"FOOBAR(=\\\",.*[:]),)\",valueType=float32]",
                                new ItemIOConnection[] {},
                                new ItemIOConnection[] { new ItemIOConnection("slave3", 97, IOType.COMMAND,
                                        "ThisIsTrigger", new Transformation("", "FOOBAR", "=\",.*[:]),"), "float32") },
                                null },
                        // two writes, valid transformations; transform quoted
                        new Object[] { new StringItem("item2"),
                                ">[slave1:99:trigger=*,transformation=JS(getValue.js),valueType=int32]\t  ,   >[slave2:98:trigger=*,transformation=\"REGEX(\\\",.=*[:]),)\",valueType=uint16]",
                                new ItemIOConnection[0],
                                new ItemIOConnection[] {
                                        new ItemIOConnection("slave1", 99, IOType.COMMAND, "*",
                                                new Transformation("", "JS", "getValue.js"), "int32"),
                                        new ItemIOConnection("slave2", 98, IOType.COMMAND, "*",
                                                new Transformation("", "REGEX", "\",.=*[:]),"), "uint16") },
                                null },
                        // invalid valuetype
                        new Object[] { new StringItem("item2"), "<[slave1:99:trigger=*,valueType=invalidValueType]",
                                null, null,
                                new BindingConfigParseException(
                                        "Parsing of item 'item2' configuration '<[slave1:99:trigger=*,valueType=invalidValueType]' "
                                                + "(as part of the whole config '<[slave1:99:trigger=*,valueType=invalidValueType]') "
                                                + "failed: org.openhab.model.item.binding.BindingConfigParseException valuetype "
                                                + "'invalidValueType' does not match expected: "
                                                + "'bit, int8, uint8, int16, uint16, int32, uint32, float32, int32_swap, "
                                                + "uint32_swap, float32_swap or 'default'") },
                        new Object[] { new StringItem("item2"), "<[slave1:99:foobarKey=*]", null, null,
                                new BindingConfigParseException(
                                        "Parsing of item 'item2' configuration '<[slave1:99:foobarKey=*]' (as part of the whole "
                                                + "config '<[slave1:99:foobarKey=*]') failed: "
                                                + "org.openhab.model.item.binding.BindingConfigParseException Unexpected token "
                                                + "'foobarKey=*, expecting key to be one of: type, trigger, transformation, valueType") },
                        // all defaults
                        new Object[] { new StringItem("item2"), "<[slave1:101]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 101, IOType.STATE) },
                                new ItemIOConnection[0], null },
                        // all defaults, ending with colon
                        new Object[] { new StringItem("item2"), "<[slave1:101:]",
                                new ItemIOConnection[] { new ItemIOConnection("slave1", 101, IOType.STATE) },
                                new ItemIOConnection[0], null }

        );
        return parameters;

    }

    private Item item;
    private String configString;
    private ItemIOConnection[] expectedReadConnections;
    private ItemIOConnection[] expectedWriteConnections;
    private Exception expectedError;

    public ModbusBindingConfigTest(Item item, String configString, ItemIOConnection[] expectedReadConnections,
            ItemIOConnection[] expectedWriteConnections, Exception expectedError) {
        this.item = item;
        this.configString = configString;
        this.expectedReadConnections = expectedReadConnections;
        this.expectedWriteConnections = expectedWriteConnections;
        this.expectedError = expectedError;
    }

    @Test
    public void testParsing() throws BindingConfigParseException {
        ModbusBindingConfig config;
        try {
            config = new ModbusBindingConfig(item, configString);
        } catch (Exception e) {
            if (expectedError != null) {
                assertThat(e.getClass(), is(equalTo(expectedError.getClass())));
                assertThat(e.getMessage(), is(equalTo(expectedError.getMessage())));
                return;
            } else {
                // unexpected error
                throw e;
            }
        }
        assertThat(config.getItemClass(), is(equalTo(StringItem.class)));
        assertThat(config.getItemName(), is(equalTo(item.getName())));

        assertThat(config.getWriteConnections().toArray(), is(equalTo(expectedWriteConnections)));
        assertThat(config.getReadConnections().toArray(), is(equalTo(expectedReadConnections)));

        // Previously polled state should be initialized to null such that new updates are "changes"
        for (ItemIOConnection connection : config.getWriteConnections()) {
            assertThat(connection.getPreviouslyPolledState(), is(equalTo(null)));
        }
        for (ItemIOConnection connection : config.getReadConnections()) {
            assertThat(connection.getPreviouslyPolledState(), is(equalTo(null)));
        }

    }

}

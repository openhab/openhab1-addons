/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.ItemIOConnection.IOType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModbusBindingConfig stores configuration of the item bound to Modbus
 *
 * @author dbkrasn
 * @since 1.1.0
 */
public class ModbusBindingConfig implements BindingConfig {
    private static final Logger logger = LoggerFactory.getLogger(ModbusBindingConfig.class);

    private Class<? extends Item> itemClass = null;

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    private String itemName = null;

    public String getItemName() {
        return itemName;
    }

    private List<ItemIOConnection> readConnections = new ArrayList<>();
    private List<ItemIOConnection> writeConnections = new ArrayList<>();
    private List<Class<? extends Command>> itemAcceptedCommandTypes;
    private List<Class<? extends State>> itemAcceptedDataTypes;
    private static final List<String> VALID_EXTENDED_ITEM_CONFIG_KEYS = Arrays
            .asList(new String[] { "type", "trigger", "transformation", "valueType" });

    private static SimpleTokenizer keyValueTokenizer = new SimpleTokenizer('=');

    /**
     * Calculates new item state based on the new boolean value, current item state and item class
     * Used with item bound to "coil" type slaves
     *
     * Returns UnDefType.UNDEF for Number and other "uncompatible" item types
     *
     * @param previousState
     * @param b new boolean value
     * @param c class of the current item state
     * @param itemClass class of the item
     *
     * @return new item state
     */
    protected State translateBoolean2State(State previousState, boolean b) {
        Class<? extends State> c = null;
        if (previousState == null) {
            c = UnDefType.class;
        } else {
            c = previousState.getClass();
        }

        if (c == UnDefType.class && itemClass == SwitchItem.class) {
            return b ? OnOffType.ON : OnOffType.OFF;
        } else if (c == UnDefType.class && itemClass == ContactItem.class) {
            return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        } else if (c == OnOffType.class && itemClass == SwitchItem.class) {
            return b ? OnOffType.ON : OnOffType.OFF;
        } else if (c == OpenClosedType.class && itemClass == SwitchItem.class) {
            return b ? OnOffType.ON : OnOffType.OFF;
        } else if (c == OnOffType.class && itemClass == ContactItem.class) {
            return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        } else if (c == OpenClosedType.class && itemClass == ContactItem.class) {
            return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        } else {
            // Number items
            return UnDefType.UNDEF;
        }
    }

    /**
     * Constructor for config string
     *
     * Following simple formats are supported
     *
     * - slaveName:index
     * - slaveName:<readIndex:>writeIndex
     *
     *
     * In addition, extended format allows user to configure some additional details (inspired by http binding
     * configuration format):
     * - <[slaveName:readIndex:trigger=TRIGGER,transformation=TRANSFORMATION,valueType=VALUETYPE],>[slaveName:
     * readIndex:trigger=TRIGGER,transformation=TRANSFORMATION,valueType=
     * VALUETYPE]
     *
     * where one defines zero or more read/inbound entries (<) and zero or more write/outbound entries (>). These
     * entries
     * are separated by comma. Only
     * slaveName, index are mandatory
     *
     * (not supported) TYPE : command or state. On read entries tells whether state updates or commands are published.
     * On write entries
     * tells whether state updates or commands are listened. Specifying "default" is "state" with read entries, and
     * "command" with write entries.
     * TRIGGER : see javadoc on ItemIOConnection
     * TRANSFORMATION : see javadoc on ItemIOConnection
     * VALUETYPE : valuetype when encoding/decoding the modbus register data. Specifying "default" is slave's valuetype.
     *
     *
     * @param item
     * @param config
     * @param modbusGenericBindingProvider TODO
     * @throws BindingConfigParseException if
     */
    public ModbusBindingConfig(Item item, String config) throws BindingConfigParseException {
        itemClass = item.getClass();
        itemName = item.getName();
        itemAcceptedCommandTypes = item.getAcceptedCommandTypes();
        itemAcceptedDataTypes = item.getAcceptedDataTypes();

        if (config.contains("[")) {
            logger.debug("Since '[' in item '{}' config string '{}', trying to parse it using extended parser",
                    itemName, config);
            parseExtended(config);
            logger.debug("item '{}' parsed", itemName);
        } else {
            logger.debug("Since no '[' in item '{}' config string '{}', trying to parse it using simple syntax parser",
                    itemName, config);
            parseSimple(config);
            logger.debug("item '{}' parsed", itemName);
        }
    }

    public List<ItemIOConnection> getReadConnectionsBySlaveName(String slave) {
        List<ItemIOConnection> connections = new ArrayList<>();
        for (ItemIOConnection connection : getReadConnections()) {
            if (slave.equals(connection.getSlaveName())) {
                connections.add(connection);
            }
        }
        logger.trace("Item '{}' has the following read connections configured for slave {}: {}", itemName, slave,
                connections);
        return connections;
    }

    public List<ItemIOConnection> getWriteConnectionsByCommand(Command command) {
        List<ItemIOConnection> connections = new ArrayList<>();
        for (ItemIOConnection connection : getWriteConnections()) {
            if (connection.supportsCommand(command)) {
                connections.add(connection);
            }
        }
        logger.trace("Item '{}' write connections for command {}: {}", itemName, command, connections);
        return connections;
    }

    private static String findValueMatchingKey(List<String> tokens, String key, String defaultValue)
            throws BindingConfigParseException {
        for (String token : tokens) {
            if (token.trim().isEmpty()) {
                // skip empty tokens (e.g. empty token in "key=val,,key2=val2")
                continue;
            }
            List<String> keyValue = keyValueTokenizer.parse(token);
            if (keyValue.get(0).trim().equals(key)) {
                return keyValue.get(1);
            }
        }
        return defaultValue;
    }

    private static void assertValidConfigurationKeys(List<String> tokens) throws BindingConfigParseException {
        for (String token : tokens) {
            if (token.trim().isEmpty()) {
                // skip empty tokens (e.g. empty token in "key=val,,key2=val2")
                continue;
            }
            List<String> keyValue = keyValueTokenizer.parse(token);
            if (keyValue.size() != 2) {
                throw new BindingConfigParseException(String.format("Invalid token '%s', expecting key=value", token));
            }
            String key = keyValue.get(0).trim();
            if (!VALID_EXTENDED_ITEM_CONFIG_KEYS.stream().anyMatch(validKey -> validKey.equals(key))) {
                throw new BindingConfigParseException(
                        String.format("Unexpected token '%s, expecting key to be one of: %s", token,
                                StringUtils.join(VALID_EXTENDED_ITEM_CONFIG_KEYS, ", ")));
            }
        }
    }

    private void parseExtended(String config) throws BindingConfigParseException {
        List<String> definitions = new SimpleTokenizer(']').parse(config);
        for (String origBindingDefinition : definitions) {
            String bindingDefinition = origBindingDefinition.trim();
            if (bindingDefinition.isEmpty()) {
                // end of string
                continue;
            } else if (bindingDefinition.startsWith(",")) {
                bindingDefinition = bindingDefinition.substring(1).trim();
            }

            try {
                String[] colonSplitted = bindingDefinition.split(Pattern.quote("["), 2)[1].split(":", 3);
                boolean read = bindingDefinition.charAt(0) == '<';
                String slaveName = colonSplitted[0].trim();
                int index;
                try {
                    index = Integer.valueOf(colonSplitted[1].trim());
                } catch (NumberFormatException e) {
                    throw new BindingConfigParseException(String.format(
                            "Could not parse '%s' as number. Please check item config syntax.", colonSplitted[1]));
                }

                List<String> tokens = colonSplitted.length > 2 ? new SimpleTokenizer(',').parse(colonSplitted[2].trim())
                        : Arrays.asList();
                assertValidConfigurationKeys(tokens);

                IOType type;
                // Only "default" type supported. In the future we could add support to it a la mqtt (see constructor).
                String typeString = "default"; // findValueMatchingKey(tokens, "type", "default").trim().toUpperCase();
                if ("default".equalsIgnoreCase(typeString)) {
                    if (read) {
                        type = IOType.STATE;
                    } else {
                        type = IOType.COMMAND;
                    }
                } else {
                    try {
                        type = IOType.valueOf(typeString);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(String.format("Could not convert '%s' to one of %s",
                                findValueMatchingKey(tokens, "type", "default"), Arrays.toString(IOType.values())), e);
                    }
                }
                String trigger = findValueMatchingKey(tokens, "trigger", ItemIOConnection.TRIGGER_DEFAULT).trim();
                String transformationString = StringEscapeUtils.unescapeJava(
                        findValueMatchingKey(tokens, "transformation", Transformation.TRANSFORM_DEFAULT).trim());

                Transformation transformation = new Transformation(transformationString);
                String valueType = findValueMatchingKey(tokens, "valueType", ItemIOConnection.VALUETYPE_DEFAULT);
                if (!"default".equalsIgnoreCase(valueType)
                        && !Arrays.asList(ModbusBindingProvider.VALUE_TYPES).contains(valueType)) {
                    throw new BindingConfigParseException(
                            String.format("valuetype '%s' does not match expected: '%s or 'default'", valueType,
                                    String.join(", ", ModbusBindingProvider.VALUE_TYPES)));
                }

                ItemIOConnection connection = new ItemIOConnection(slaveName, index, type, trigger, transformation,
                        valueType);
                if (read) {
                    readConnections.add(connection);
                } else {
                    writeConnections.add(connection);
                }
                logger.debug("Parsed IOConnection (read={}) for item '{}': {}", read, itemName, connection);
            } catch (Exception e) {
                String msg = String.format(
                        "Parsing of item '%s' configuration '%s]' (as part of the whole config '%s') failed: %s %s",
                        itemName, origBindingDefinition, config, e.getClass().getName(), e.getMessage());
                logger.error(msg, e);
                BindingConfigParseException exception = new BindingConfigParseException(msg);
                exception.initCause(e);
                throw exception;
            }
        }

    }

    private void parseSimple(String config) throws BindingConfigParseException {
        int readIndex;
        int writeIndex;

        String slaveName;

        try {
            String[] items = config.split(":");
            slaveName = items[0];
            if (items.length == 2) {
                readIndex = Integer.valueOf(items[1]);
                writeIndex = Integer.valueOf(items[1]);
            } else if (items.length == 3) {
                validateSimpleIndexEntry(items[1], items[2]);
                if (items[1].charAt(0) == '<') {
                    // items[1] is inbound (read from slave); items[2] is outbound (write to slave)
                    readIndex = Integer.valueOf(items[1].substring(1, items[1].length()));
                    writeIndex = Integer.valueOf(items[2].substring(1, items[2].length()));
                } else {
                    readIndex = Integer.valueOf(items[2].substring(1, items[2].length()));
                    writeIndex = Integer.valueOf(items[1].substring(1, items[1].length()));
                }
            } else {
                throw new BindingConfigParseException(
                        String.format("Invalid number of registers in item '%s' configuration", itemName));
            }
        } catch (BindingConfigParseException e) {
            throw e;
        } catch (Exception e) {
            BindingConfigParseException exception = new BindingConfigParseException(
                    String.format("Item '%s' config ('%s') parsing failed: %s: %s", itemName, config,
                            e.getClass().getName(), e.getMessage()));
            exception.initCause(e);
            throw exception;
        }
        readConnections.add(new ItemIOConnection(slaveName, readIndex, IOType.STATE));
        writeConnections.add(new ItemIOConnection(slaveName, writeIndex, IOType.COMMAND));
    }

    private static void validateSimpleIndexEntry(String entry1, String entry2) throws BindingConfigParseException {
        if (!((entry1.startsWith("<") && entry2.startsWith(">"))
                || (entry1.startsWith(">") && entry2.startsWith("<")))) {
            throw new BindingConfigParseException("Register references should be either :X or :<X:>Y");
        }
    }

    public List<ItemIOConnection> getReadConnections() {
        return readConnections;
    }

    public List<ItemIOConnection> getWriteConnections() {
        return writeConnections;
    }

    public List<Class<? extends Command>> getItemAcceptedCommandTypes() {
        return itemAcceptedCommandTypes;
    }

    public List<Class<? extends State>> getItemAcceptedDataTypes() {
        return itemAcceptedDataTypes;
    }

}
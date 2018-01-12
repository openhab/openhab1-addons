/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.io.InvalidClassException;
import java.util.EnumSet;
import java.util.Set;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid command types which can be processed by this binding.
 *
 * @since 1.6.0
 * @author paphko
 */
public enum AnelCommandType {

    /** Device name (not changeable). */
    NAME("NAME", StringItem.class),
    /** Device temperature (not changeable). */
    TEMPERATURE("TEMPERATURE", NumberItem.class),
    /**
     * State of relay 1 (changeable if it is not locked:
     * {@link AnelCommandType#F1LOCKED}).
     */
    F1("F1", SwitchItem.class),
    /**
     * State of relay 2 (changeable if it is not locked:
     * {@link AnelCommandType#F2LOCKED}).
     */
    F2("F2", SwitchItem.class),
    /**
     * State of relay 3 (changeable if it is not locked:
     * {@link AnelCommandType#F3LOCKED}).
     */
    F3("F3", SwitchItem.class),
    /**
     * State of relay 4 (changeable if it is not locked:
     * {@link AnelCommandType#F4LOCKED}).
     */
    F4("F4", SwitchItem.class),
    /**
     * State of relay 5 (changeable if it is not locked:
     * {@link AnelCommandType#F5LOCKED}).
     */
    F5("F5", SwitchItem.class),
    /**
     * State of relay 6 (changeable if it is not locked:
     * {@link AnelCommandType#F6LOCKED}).
     */
    F6("F6", SwitchItem.class),
    /**
     * State of relay 7 (changeable if it is not locked:
     * {@link AnelCommandType#F7LOCKED}).
     */
    F7("F7", SwitchItem.class),
    /**
     * State of relay 8 (changeable if it is not locked:
     * {@link AnelCommandType#F8LOCKED}).
     */
    F8("F8", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F1} is locked (not
     * changeable).
     */
    F1LOCKED("F1LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F2} is locked (not
     * changeable).
     */
    F2LOCKED("F2LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F3} is locked (not
     * changeable).
     */
    F3LOCKED("F3LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F4} is locked (not
     * changeable).
     */
    F4LOCKED("F4LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F5} is locked (not
     * changeable).
     */
    F5LOCKED("F5LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F6} is locked (not
     * changeable).
     */
    F6LOCKED("F6LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F7} is locked (not
     * changeable).
     */
    F7LOCKED("F7LOCKED", SwitchItem.class),
    /**
     * Whether or not relay {@link AnelCommandType#F8} is locked (not
     * changeable).
     */
    F8LOCKED("F8LOCKED", SwitchItem.class),
    /** Name of relay {@link AnelCommandType#F1} (not changeable). */
    F1NAME("F1NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F2} (not changeable). */
    F2NAME("F2NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F3} (not changeable). */
    F3NAME("F3NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F4} (not changeable). */
    F4NAME("F4NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F5} (not changeable). */
    F5NAME("F5NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F6} (not changeable). */
    F6NAME("F6NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F7} (not changeable). */
    F7NAME("F7NAME", StringItem.class),
    /** Name of relay {@link AnelCommandType#F8} (not changeable). */
    F8NAME("F8NAME", StringItem.class),
    /**
     * IO1 input state (if {@link AnelCommandType#IO1ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO1("IO1", SwitchItem.class),
    /**
     * IO2 input state (if {@link AnelCommandType#IO2ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO2("IO2", SwitchItem.class),
    /**
     * IO3 input state (if {@link AnelCommandType#IO3ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO3("IO3", SwitchItem.class),
    /**
     * IO4 input state (if {@link AnelCommandType#IO4ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO4("IO4", SwitchItem.class),
    /**
     * IO5 input state (if {@link AnelCommandType#IO5ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO5("IO5", SwitchItem.class),
    /**
     * IO6 input state (if {@link AnelCommandType#IO6ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO6("IO6", SwitchItem.class),
    /**
     * IO7 input state (if {@link AnelCommandType#IO7ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO7("IO7", SwitchItem.class),
    /**
     * IO8 input state (if {@link AnelCommandType#IO8ISINPUT} is set), otherwise
     * changeable output state.
     */
    IO8("IO8", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO1} is input or output (not changeable). */
    IO1ISINPUT("IO1ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO2} is input or output (not changeable). */
    IO2ISINPUT("IO2ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO3} is input or output (not changeable). */
    IO3ISINPUT("IO3ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO4} is input or output (not changeable). */
    IO4ISINPUT("IO4ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO5} is input or output (not changeable). */
    IO5ISINPUT("IO5ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO6} is input or output (not changeable). */
    IO6ISINPUT("IO6ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO7} is input or output (not changeable). */
    IO7ISINPUT("IO7ISINPUT", SwitchItem.class),
    /** Whether {@link AnelCommandType#IO8} is input or output (not changeable). */
    IO8ISINPUT("IO8ISINPUT", SwitchItem.class),
    /** Name of {@link AnelCommandType#IO1} (not changeable). */
    IO1NAME("IO1NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO2} (not changeable). */
    IO2NAME("IO2NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO3} (not changeable). */
    IO3NAME("IO3NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO4} (not changeable). */
    IO4NAME("IO4NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO5} (not changeable). */
    IO5NAME("IO5NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO6} (not changeable). */
    IO6NAME("IO6NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO7} (not changeable). */
    IO7NAME("IO7NAME", StringItem.class),
    /** Name of {@link AnelCommandType#IO8} (not changeable). */
    IO8NAME("IO8NAME", StringItem.class),
    /** Sensor temperature (not changeable; only if device firmware >= 6.1). */
    SENSOR_TEMPERATURE("SENSOR_TEMPERATURE", NumberItem.class),
    /** Sensor humidity (not changeable; only if device firmware >= 6.1). */
    SENSOR_HUMIDITY("SENSOR_HUMIDITY", NumberItem.class),
    /** Sensor brightness (not changeable; only if device firmware >= 6.1). */
    SENSOR_BRIGHTNESS("SENSOR_BRIGHTNESS", NumberItem.class),;

    /** Set of all changeable switch (if not locked). */
    public static Set<AnelCommandType> SWITCHES = EnumSet.of(F1, F2, F3, F4, F5, F6, F7, F8);

    /** Set of all IOs. */
    public static Set<AnelCommandType> IOS = EnumSet.of(IO1, IO2, IO3, IO4, IO5, IO6, IO7, IO8);

    private final String text;
    private Class<? extends Item> itemClass;

    private AnelCommandType(final String text, Class<? extends Item> itemClass) {
        this.text = text;
        this.itemClass = itemClass;
    }

    @Override
    public String toString() {
        return text;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    /**
     * Validate command type string.
     *
     * @param commandTypeText
     *            Command string e.g. 'F1' for {@link AnelCommandType#F1}.
     * @return <code>true</code> if the item is valid.
     * @throws IllegalArgumentException
     *             Not valid command type.
     * @throws InvalidClassException
     *             Not valid class for command type.
     */
    public static boolean validateBinding(String commandTypeText, Class<? extends Item> itemClass)
            throws IllegalArgumentException, InvalidClassException {
        for (AnelCommandType c : AnelCommandType.values()) {
            if (c.text.equals(commandTypeText)) {
                if (c.getItemClass().equals(itemClass)) {
                    return true;
                } else {
                    throw new InvalidClassException("Invalid class for command type: " + itemClass);
                }
            }
        }
        throw new IllegalArgumentException("Invalid command type: " + commandTypeText);
    }

    /**
     * Convert command type string to command type class.
     *
     * @param commandTypeText
     *            Command string e.g. 'F1' for {@link AnelCommandType#F1}.
     * @return The corresponding command type.
     * @throws InvalidClassException
     *             Not valid class for command type.
     */
    public static AnelCommandType getCommandType(String commandTypeText) throws IllegalArgumentException {
        for (AnelCommandType c : AnelCommandType.values()) {
            if (c.text.equals(commandTypeText)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid command type: " + commandTypeText);
    }
}

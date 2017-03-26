/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * ItemIOConnection defines the translation of data from modbus to openhab, and vice versa.
 *
 *
 * @author Sami Salonen
 * @since 1.10.0
 *
 */
public class ItemIOConnection {

    private static StandardToStringStyle toStringStyle = new StandardToStringStyle();
    public static final String POLL_STATE_CHANGE_TRIGGER = "CHANGED";
    public static final String TRIGGER_DEFAULT = "default";
    public static final String VALUETYPE_DEFAULT = "default";

    static {
        toStringStyle.setUseShortClassName(true);
    }

    public static enum IOType {
        STATE,
        COMMAND
    };

    /**
     * Name of the ModbusSlave instance to read/write data
     */
    private String slaveName;

    /**
     * Index to read/write, relative to slave's start
     */
    private int index;

    /**
     * On write (outbound) IO connections, type determines whether the binding should listen for state updates or
     * commands
     * On read (inbound) IO connections, type determines whether the binding should emit state updates or commands
     *
     * Currently (2017/02) the binding does not implement this extended support, however. We always use STATE type for
     * read connections, and COMMAND type for write connections.
     */
    private ItemIOConnection.IOType type;
    /**
     * On write IO connections: string representation of the non-transformed command that are accepted by this IO
     * connection, and thus
     * should be written to modbus slave.
     *
     * On Read IO connections: string representation of the non-transformed state representing the polled data from
     * modbus that are accepted by this IO connection and thus should be sent to openHAB event bus.
     *
     * Use asterisk (*) to match all. Use "CHANGED" (case-insensitive) (applicable only with read connections) to
     * trigger only on changed values. With read connections, use "default" to refer to * or CHANGED depending on
     * updateunchangeditems slave setting. With write connections, use "default" to refer to *.
     */
    private String trigger;
    /**
     * Object representing transformation for the command or state
     */
    private Transformation transformation;
    /**
     * Use "default" to use slave's value type when interpreting data. Use any other known value type (e.g. int32) to
     * override the value type.
     */
    private String valueType;

    /**
     * Previously polled state(s) of Item, converted to state as defined by ItemIOConnection. Initialized to null so
     * that
     * UnDefType.UNDEF (which might be transmitted
     * in case of errors)
     * is considered unequal to the initial value.
     */
    private State polledState = null;
    /**
     * Relative poll number of this IO connection for comparing poll times of different IO connections. No two instances
     * of {@link ItemIOConnection} have the same poll number.
     *
     * Value of zero is used for connections that have no polls at all yet.
     */
    private long pollNumber = 0;
    /**
     * Global number indicating how many polls have taken place by all instances of ItemIOConnection (plus one).
     */
    private static AtomicLong globalPollNumber = new AtomicLong(1);

    public ItemIOConnection(String slaveName, int index, ItemIOConnection.IOType type) {
        this.slaveName = slaveName;
        this.index = index;
        this.type = type;
        this.trigger = TRIGGER_DEFAULT;
        this.transformation = Transformation.IDENTITY_TRANSFORMATION;
        this.valueType = VALUETYPE_DEFAULT;
    }

    public ItemIOConnection(String slaveName, int index, ItemIOConnection.IOType type, String trigger) {
        this(slaveName, index, type);
        this.trigger = trigger;
        this.transformation = Transformation.IDENTITY_TRANSFORMATION;
        this.valueType = VALUETYPE_DEFAULT;
    }

    public ItemIOConnection(String slaveName, int index, ItemIOConnection.IOType type, String trigger,
            Transformation transformation, String valueType) {
        this(slaveName, index, type, trigger);
        this.transformation = transformation;
        this.valueType = valueType;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public int getIndex() {
        return index;
    }

    public ItemIOConnection.IOType getType() {
        return type;
    }

    public String getTrigger() {
        return trigger;
    }

    /**
     * Whether trigger equals <code>TRIGGER_DEFAULT</code> (case-insensitive comparison)
     *
     * @return
     */
    private boolean isTriggerDefault() {
        return TRIGGER_DEFAULT.equalsIgnoreCase(this.trigger);
    }

    /**
     * Whether trigger equals <code>POLL_STATE_CHANGE_TRIGGER</code> (case-insensitive comparison)
     *
     *
     * @return
     */
    private boolean isTriggerOnPolledStateChange() {
        return POLL_STATE_CHANGE_TRIGGER.equalsIgnoreCase(trigger);
    }

    public String getEffectiveValueType(String defaultValueType) {
        return VALUETYPE_DEFAULT.equalsIgnoreCase(this.valueType) ? defaultValueType : this.valueType;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public String getValueType() {
        return valueType;
    }

    public State getPreviouslyPolledState() {
        return polledState;
    }

    /**
     * Return a number representing the relative time of the poll (greater number is more recent poll).
     *
     * Poll number of zero means that no polls have taken place.
     *
     * Poll numbers over all ItemIOConnections are guaranteed to be in order.
     *
     */
    public long getPollNumber() {
        return pollNumber;
    }

    public void setPreviouslyPolledState(State state) {
        this.pollNumber = ItemIOConnection.globalPollNumber.getAndIncrement();
        this.polledState = state;
    }

    /**
     * Check if this configuration "supports" the given State.
     *
     * If return value is true, the processing should continue with this {@link ItemIOConnection}
     *
     * @param state
     *            for which to check if we can process.
     * @param changed
     *            whether values was changed
     * @param slaveUpdateUnchanged
     *            whether to update unchanged if this.trigger is default
     * @return true if processing is supported.
     */
    public boolean supportsState(State state, boolean changed, boolean slaveUpdateUnchanged) {
        if (this.type.equals(IOType.COMMAND)) {
            return false;
        } else if (getTrigger().equals("*")) {
            return true;
        } else if (isTriggerDefault()) {
            if (changed) {
                // Value changed, "default" trigger is to update the state
                return true;
            } else {
                // Value not changed, update only if slave updates unchanged items
                return slaveUpdateUnchanged;
            }
        } else if (isTriggerOnPolledStateChange()) {
            return changed;
        } else {
            return trigger.equalsIgnoreCase(state.toString());
        }
    }

    /**
     * Check if this configuration supports the given Command.
     *
     * If return value is true, the processing should continue with this {@link ItemIOConnection}
     *
     * @param command
     *            for which to check if we can process.
     * @return true if processing is supported.
     */
    public boolean supportsCommand(Command command) {
        if (this.type.equals(IOType.STATE)) {
            return false;
        } else if (getTrigger().equals("*")) {
            return true;
        } else if (getTrigger().equals(TRIGGER_DEFAULT)) {
            return true;
        } else {
            return trigger.equalsIgnoreCase(command.toString());
        }
    }

    /**
     * for testing
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ItemIOConnection other = (ItemIOConnection) obj;
        return new EqualsBuilder().append(slaveName, other.slaveName).append(index, other.index)
                .append(type, other.type).append(trigger, other.trigger).append(transformation, other.transformation)
                .append(valueType, other.valueType).isEquals();
    }

    /**
     * Implemented since equals is there. Just for testing.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(91, 131).append(slaveName).append(index).append(type).append(trigger)
                .append(transformation).append(valueType).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, toStringStyle).append("slaveName", slaveName).append("index", index)
                .append("type", type).append("trigger", trigger).append("transformation", transformation)
                .append("valueType", valueType).toString();
    }

}
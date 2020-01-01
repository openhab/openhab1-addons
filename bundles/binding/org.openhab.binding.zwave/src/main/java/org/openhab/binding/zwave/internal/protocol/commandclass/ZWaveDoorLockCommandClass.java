/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the DoorLock command class.
 *
 * @author Dave Badia
 * @since TODO
 */
@XStreamAlias("doorLockCommandClass")
public class ZWaveDoorLockCommandClass extends ZWaveCommandClass
        implements ZWaveGetCommands, ZWaveSetCommands, ZWaveCommandClassDynamicState {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveDoorLockCommandClass.class);

    static final int DOORLOCK_SET = 0x01;
    /**
     * Request the state of the door lock, ie {@link #DOORLOCK_REPORT}
     */
    private static final int DOORLOCK_GET = 0x02;
    /**
     * Details about the state of the door lock (secured, unsecured)
     */
    private static final int DOORLOCK_REPORT = 0x03;
    private static final int DOORLOCK_CONFIG_SET = 0x04;
    /**
     * Request the config of the door lock, ie {@link #DOORLOCK_CONFIG_REPORT}
     */
    private static final int DOORLOCK_CONFIG_GET = 0x05;
    /**
     * Details about the config of the door lock (timed autolock, etc)
     */
    private static final int DOORLOCK_CONFIG_REPORT = 0x06;

    @XStreamOmitField
    private boolean dynamicDone = false;

    /**
     * Creates a new instance of the ZWaveDoorLockCommandClass class.
     *
     * @param node the node this command class belongs to
     * @param controller the controller to use
     * @param endpoint the endpoint this Command class belongs to
     */
    public ZWaveDoorLockCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.DOOR_LOCK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.debug(String.format("NODE %d: Received DoorLock Request", this.getNode().getNodeId()));
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case DOORLOCK_GET:
            case DOORLOCK_SET:
            case DOORLOCK_CONFIG_GET:
            case DOORLOCK_CONFIG_SET:
            case DOORLOCK_CONFIG_REPORT:
                logger.warn(String.format("Command 0x%02X not implemented.", command));
                return;
            case DOORLOCK_REPORT:
                logger.trace("Process Door Lock Report");
                dynamicDone = true;

                int lockState = serialMessage.getMessagePayloadByte(offset + 1);
                int handlesMode = serialMessage.getMessagePayloadByte(offset + 2);
                int doorCondition = serialMessage.getMessagePayloadByte(offset + 3);
                int lockTimeoutMinutes = serialMessage.getMessagePayloadByte(offset + 4);
                int lockTimeoutSeconds = serialMessage.getMessagePayloadByte(offset + 5);

                DoorLockStateType doorLockState = DoorLockStateType.getDoorLockStateType(lockState);
                logger.info(String.format(
                        "NODE %d: Door Lock report - lockState = %s,"
                                + "handlesMode = 0x%02x, doorCondition = 0x%02x, lockTimeoutMinutes = 0x%02x,"
                                + "lockTimeoutSeconds = 0x%02x",
                        this.getNode().getNodeId(), doorLockState.label, handlesMode, doorCondition, lockTimeoutMinutes,
                        lockTimeoutSeconds));
                ZWaveDoorLockValueEvent zEvent = new ZWaveDoorLockValueEvent(this.getNode().getNodeId(), endpoint,
                        lockState);
                this.getController().notifyEventListeners(zEvent);
                break;
            default:
                logger.warn(String.format("NODE %d: Unsupported Command 0x%02X for command class %s (0x%02X).",
                        this.getNode().getNodeId(), command, this.getCommandClass().getLabel(),
                        this.getCommandClass().getKey()));
                break;
        }
    }

    /**
     * Gets a SerialMessage with the DOORLOCK_GET command
     *
     * @return the serial message
     */
    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for application command DOORLOCK_GET", this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) getCommandClass().getKey(),
                (byte) DOORLOCK_GET, };
        result.setMessagePayload(newPayload);
        return result;
    }

    @Override
    public SerialMessage setValueMessage(int value) {
        logger.debug("NODE {}: Creating new message for application command DOORLOCK_SET", this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(),
                (byte) DOORLOCK_SET, (byte) value };
        result.setMessagePayload(newPayload);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SerialMessage> getDynamicValues(boolean refresh) {
        if (refresh) {
            dynamicDone = false;
        }

        if (dynamicDone) {
            return null;
        }

        ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
        result.add(getValueMessage());
        return result;
    }

    /**
     * Z-Wave DoorLockState enumeration. The door lock state type indicates
     * the state of the door that is reported.
     */
    @XStreamAlias("doorLockState")
    enum DoorLockStateType {
        UNSECURED(0x00, "Unsecured"),
        UNSECURED_TIMEOUT(0x01, "Unsecure with Timeout"),
        INSIDE_UNSECURED(0x10, "Inside Handle Unsecured"),
        INSIDE_UNSECURED_TIMEOUT(0x11, "Inside Handle Unsecured with Timeout"),
        OUTSIDE_UNSECURED(0x20, "Outside Handle Unsecured"),
        OUTSIDE_UNSECURED_TIMEOUT(0x21, "Outside Handle Unsecured with Timeout"),
        SECURED(0xFF, "Secured"),
        UNKNOWN(0xFE, "Unknown"), // This isn't per the spec, it's just our definition
        ;
        /**
         * A mapping between the integer code and its corresponding door
         * lock state type to facilitate lookup by code.
         */
        private static Map<Integer, DoorLockStateType> codeToDoorLockStateTypeMapping;

        private int key;
        private String label;

        private static void initMapping() {
            codeToDoorLockStateTypeMapping = new ConcurrentHashMap<Integer, DoorLockStateType>();
            for (DoorLockStateType d : values()) {
                codeToDoorLockStateTypeMapping.put(d.key, d);
            }
        }

        /**
         * Lookup function based on the fan mode type code.
         * Returns null if the code does not exist.
         *
         * @param i the code to lookup
         * @return enumeration value of the fan mode type.
         */
        public static DoorLockStateType getDoorLockStateType(int i) {
            if (codeToDoorLockStateTypeMapping == null) {
                initMapping();
            }
            return codeToDoorLockStateTypeMapping.get(i);
        }

        DoorLockStateType(int key, String label) {
            this.key = key;
            this.label = label;
        }

        /**
         * @return the key
         */
        public int getKey() {
            return key;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }
    }

    /**
     * Z-Wave Door Lock Event class. Indicates that a door lock value
     * changed.
     */
    public class ZWaveDoorLockValueEvent extends ZWaveCommandClassValueEvent {
        /**
         * Constructor. Creates a instance of the ZWaveAlarmValueEvent class.
         *
         * @param nodeId the nodeId of the event
         * @param endpoint the endpoint of the event.
         * @param value the value for the event.
         */
        private ZWaveDoorLockValueEvent(int nodeId, int endpoint, Object value) {
            super(nodeId, endpoint, CommandClass.DOOR_LOCK, value);
        }
    }
}

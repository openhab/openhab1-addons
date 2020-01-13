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

/**
 * Handles the Lock command class.
 *
 * @author Dave Badia
 * @since TODO
 */
@XStreamAlias("lockCommandClass")
public class ZWaveLockCommandClass extends ZWaveCommandClass implements ZWaveGetCommands, ZWaveSetCommands {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveLockCommandClass.class);

    private static final int LOCK_SET = 0x01;
    /**
     * Request the state of the lock, ie {@link #LOCK_REPORT}
     */
    private static final int LOCK_GET = 0x02;
    /**
     * Details about the state of the lock (locked, unlocked)
     */
    private static final int LOCK_REPORT = 0x03;

    /**
     * Creates a new instance of the ZWaveAlarmCommandClass class.
     *
     * @param node the node this command class belongs to
     * @param controller the controller to use
     * @param endpoint the endpoint this Command class belongs to
     */
    public ZWaveLockCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.LOCK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.trace("Handle Message Lock Request");
        logger.debug(String.format("NODE %d: Received Lock Request", this.getNode().getNodeId()));
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case LOCK_GET:
            case LOCK_SET:
                logger.warn(String.format("Command 0x%02X not implemented.", command));
                return;
            case LOCK_REPORT:
                logger.trace("Process Lock Report");

                int lockState = serialMessage.getMessagePayloadByte(offset + 1);

                logger.debug(String.format("NODE %d: Lock report - lockState = 0x%02x,", this.getNode().getNodeId(),
                        lockState));

                ZWaveLockValueEvent zEvent = new ZWaveLockValueEvent(this.getNode().getNodeId(), endpoint, lockState);
                this.getController().notifyEventListeners(zEvent);
                break;
            default:
                logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command,
                        this.getCommandClass().getLabel(), this.getCommandClass().getKey()));
                break;
        }
    }

    /**
     * Gets a SerialMessage with the LOCK_GET command
     *
     * @return the serial message
     */
    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for application command LOCK_GET", this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) getCommandClass().getKey(),
                (byte) LOCK_GET, };
        result.setMessagePayload(newPayload);
        return result;
    }

    @Override
    public SerialMessage setValueMessage(int value) {
        logger.debug("NODE {}: Creating new message for application command LOCK_SET", this.getNode().getNodeId());

        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(), (byte) LOCK_SET,
                (byte) value };
        result.setMessagePayload(newPayload);
        return result;
    }

    /**
     * Z-Wave LockState enumeration. The lock state type indicates
     * the state of the lock that is reported.
     */
    @XStreamAlias("lockState")
    enum LockStateType {
        UNLOCKED(0x00, "Unlocked"),
        LOCKED(0xFF, "Locked"),;
        /**
         * A mapping between the integer code and its corresponding door
         * lock state type to facilitate lookup by code.
         */
        private static Map<Integer, LockStateType> codeToLockStateTypeMapping;

        private int key;
        private String label;

        private static void initMapping() {
            codeToLockStateTypeMapping = new ConcurrentHashMap<Integer, LockStateType>();
            for (LockStateType d : values()) {
                codeToLockStateTypeMapping.put(d.key, d);
            }
        }

        /**
         * Lookup function based on the fan mode type code.
         * Returns null if the code does not exist.
         *
         * @param i the code to lookup
         * @return enumeration value of the fan mode type.
         */
        public static LockStateType getLockStateType(int i) {
            if (codeToLockStateTypeMapping == null) {
                initMapping();
            }
            return codeToLockStateTypeMapping.get(i);
        }

        LockStateType(int key, String label) {
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
     * Z-Wave Door Lock Event class. Indicates that a door lcok value
     * changed.
     */
    public class ZWaveLockValueEvent extends ZWaveCommandClassValueEvent {
        /**
         * Constructor. Creates a instance of the ZWaveAlarmValueEvent class.
         *
         * @param nodeId the nodeId of the event
         * @param endpoint the endpoint of the event.
         * @param value the value for the event.
         */
        private ZWaveLockValueEvent(int nodeId, int endpoint, Object value) {
            super(nodeId, endpoint, CommandClass.LOCK, value);
        }
    }
}

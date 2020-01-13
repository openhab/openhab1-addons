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
 * Handles the Protection command class. Protection is used to
 * restrict control of a device, for example, temporarily
 * disable all door lock codes expect for the master code at night
 *
 * @author Dave Badia
 * @since TODO
 */
@XStreamAlias("protectionCommandClass")
public class ZWaveProtectionCommandClass extends ZWaveCommandClass
        implements ZWaveBasicCommands, ZWaveCommandClassDynamicState {

    @XStreamOmitField
    private static final Logger logger = LoggerFactory.getLogger(ZWaveProtectionCommandClass.class);

    private static final int PROTECTION_SET = 0x01;
    private static final int PROTECTION_GET = 0x02;
    private static final int PROTECTION_REPORT = 0x03;

    @XStreamOmitField
    private boolean dynamicDone = false;

    /**
     * Creates a new instance of the ZWaveProtectionCommandClass class.
     *
     * @param node the node this command class belongs to
     * @param controller the controller to use
     * @param endpoint the endpoint this Command class belongs to
     */
    public ZWaveProtectionCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.PROTECTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.trace("Handle Message Switch Binary Request");
        logger.debug(String.format("Received Switch Binary Request for Node ID = %d", this.getNode().getNodeId()));
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case PROTECTION_GET:
                logger.warn("Command {} not implemented.", command);
                return;
            case PROTECTION_REPORT:
                logger.trace("Process Protection Report");
                int value = serialMessage.getMessagePayloadByte(offset + 1);
                ProtectionStateType protectionType = ProtectionStateType.getProtectionStateType(value);
                logger.debug(String.format("NODE %d: Protection report, value = %s", this.getNode().getNodeId(),
                        protectionType.label));
                ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(),
                        endpoint, this.getCommandClass(), value);
                this.getController().notifyEventListeners(zEvent);
                dynamicDone = true;
                break;
            case PROTECTION_SET:
            default:
                logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command,
                        this.getCommandClass().getLabel(), this.getCommandClass().getKey()));
        }
    }

    /**
     * Gets a SerialMessage with the SWITCH_BINARY_GET command
     *
     * @return the serial message
     */
    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for application command PROTECTION_GET",
                this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) getCommandClass().getKey(),
                (byte) PROTECTION_GET };
        result.setMessagePayload(newPayload);
        return result;
    }

    /**
     * Gets a SerialMessage with the SWITCH_BINARY_SET command
     *
     * @param the level to set. 0 is mapped to off, > 0 is mapped to on.
     * @return the serial message
     */
    @Override
    public SerialMessage setValueMessage(int level) {
        logger.debug("NODE {}: Creating new message for application command PROTECTION_SET",
                this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(),
                (byte) PROTECTION_SET, (byte) (level > 0 ? 0xFF : 0x00) };
        result.setMessagePayload(newPayload);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SerialMessage> getDynamicValues(boolean refresh) {
        ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();

        if (refresh == true || dynamicDone == false) {
            result.add(getValueMessage());
        }
        return result;
    }

    /**
     * Z-Wave DoorLockState enumeration. The door lock state type indicates
     * the state of the door that is reported.
     */
    @XStreamAlias("protectionState")
    enum ProtectionStateType {
        /**
         * Device is not protected, can be operated as normal
         */
        UNPROTECTED(0x00, "Unprotected"),
        /**
         * Device has it's protection restricted to a subset of access options.
         * For example, all user door codes are disabled except for the master code
         */
        PROTECTED_BY_SEQUENCE(0x01, "Protected by sequence"),
        PROTECTED(0x02, "Protected: No operation possible"),;
        /**
         * A mapping between the integer code and its corresponding door
         * lock state type to facilitate lookup by code.
         */
        private static Map<Integer, ProtectionStateType> codeToProtectionStateTypeMapping;

        private int key;
        private String label;

        private static void initMapping() {
            codeToProtectionStateTypeMapping = new ConcurrentHashMap<Integer, ProtectionStateType>();
            for (ProtectionStateType d : values()) {
                codeToProtectionStateTypeMapping.put(d.key, d);
            }
        }

        /**
         * Lookup function based on the type code.
         * Returns null if the code does not exist.
         *
         * @param i the code to lookup
         * @return enumeration value of the fan mode type.
         */
        public static ProtectionStateType getProtectionStateType(int i) {
            if (codeToProtectionStateTypeMapping == null) {
                initMapping();
            }
            return codeToProtectionStateTypeMapping.get(i);
        }

        ProtectionStateType(int key, String label) {
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
}

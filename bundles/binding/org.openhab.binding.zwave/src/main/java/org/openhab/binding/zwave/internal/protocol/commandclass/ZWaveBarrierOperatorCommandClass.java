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
 * Handles Barrier Operator Command Class for Items like Garage Door Opener etc...
 *
 * @author sankala
 *
 */
@XStreamAlias("barrierOperatorCommandClass")
public class ZWaveBarrierOperatorCommandClass extends ZWaveCommandClass implements ZWaveGetCommands, ZWaveSetCommands {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveBarrierOperatorCommandClass.class);

    public static final int BARRIER_OPERATOR_SET = 0x01;
    public static final int BARRIER_OPERATOR_GET = 0x02;
    public static final int BARRIER_OPERATOR_REPORT = 0x03;
    public static final int BARRIER_OPERATOR_SIGNAL_SUPPORTED_GET = 0x04;
    public static final int BARRIER_OPERATOR_SIGNAL_SUPPORTED_REPORT = 0x05;
    public static final int BARRIER_OPERATOR_SIGNAL_SET = 0x06;
    public static final int BARRIER_OPERATOR_SIGNAL_GET = 0x07;
    public static final int BARRIER_OPERATOR_SIGNAL_REPORT = 0x08;

    public ZWaveBarrierOperatorCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    @Override
    public SerialMessage setValueMessage(int value) {
        logger.debug("NODE {}: Creating new message for application command BARRIER_OPERATOR_SET",
                this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(),
                (byte) BARRIER_OPERATOR_SET, (byte) (value > 0 ? 0xFF : 0x00) };
        result.setMessagePayload(newPayload);
        return result;
    }

    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for command BARRIER_OPERATOR_GET", this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) getCommandClass().getKey(),
                (byte) BARRIER_OPERATOR_GET };
        result.setMessagePayload(newPayload);
        return result;
    }

    @Override
    public CommandClass getCommandClass() {
        return CommandClass.BARRIER_OPERATOR;
    }

    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.debug("NODE {}: Received Barrier Operator Request", this.getNode().getNodeId());
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case BARRIER_OPERATOR_GET:
            case BARRIER_OPERATOR_SET:
            case BARRIER_OPERATOR_SIGNAL_GET:
            case BARRIER_OPERATOR_SIGNAL_SET:
            case BARRIER_OPERATOR_SIGNAL_REPORT:
            case BARRIER_OPERATOR_SIGNAL_SUPPORTED_GET:
            case BARRIER_OPERATOR_SIGNAL_SUPPORTED_REPORT:
                logger.warn("Command {} not implemented.", command);
                return;
            case BARRIER_OPERATOR_REPORT:
                logger.trace("Process Barrier Operator Report");
                int value = serialMessage.getMessagePayloadByte(offset + 1);
                logger.debug("NODE {}: Barrier Operator report, value = {}", this.getNode().getNodeId(), value);

                ZWaveCommandClassValueEvent zEvent = new ZWaveBarrierOperatorValueEvent(this.getNode().getNodeId(),
                        endpoint, this.getCommandClass(), value,
                        BarrierOperatorStateType.getBarrierOperatorStateType(value));

                this.getController().notifyEventListeners(zEvent);
                break;
            default:
                logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command,
                        this.getCommandClass().getLabel(), this.getCommandClass().getKey()));
        }
    }

    @XStreamAlias("barrierOperatorState")
    public static enum BarrierOperatorStateType {
        STATE_CLOSED(0x00, "Closed"),
        STATE_CLOSING(0xFC, "Closing"),
        STATE_OPENED(0xFF, "Open"),
        STATE_OPENING(0xFE, "Opening"),
        STATE_STOPPED(0xFD, "Stopped");

        private static Map<Integer, BarrierOperatorStateType> codeToBarrierOperatorStateTypeMapping;

        private int key;
        private String label;

        private static void initMapping() {
            codeToBarrierOperatorStateTypeMapping = new ConcurrentHashMap<Integer, ZWaveBarrierOperatorCommandClass.BarrierOperatorStateType>();
            for (BarrierOperatorStateType s : values()) {
                codeToBarrierOperatorStateTypeMapping.put(s.key, s);
            }
        }

        public static BarrierOperatorStateType getBarrierOperatorStateType(int i) {
            if (codeToBarrierOperatorStateTypeMapping == null) {
                initMapping();
            }
            BarrierOperatorStateType barrierOperatorStateType = codeToBarrierOperatorStateTypeMapping.get(i);
            // If the state is stopped, then the value indicates what is the percentage of opening. So it will not
            // directly yeild us the "StateStopped" . We have to force it to StateStopped.
            if (barrierOperatorStateType == null && (i < 99 && i > 0)) {
                barrierOperatorStateType = STATE_STOPPED;
            }

            return barrierOperatorStateType;
        }

        private BarrierOperatorStateType(int key, String label) {
            this.key = key;
            this.label = label;
        }

        public static Map<Integer, BarrierOperatorStateType> getCodeToBarrierOperatorStateTypeMapping() {
            return codeToBarrierOperatorStateTypeMapping;
        }

        public int getKey() {
            return key;
        }

        public int getValue() {
            return key;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * Enhanced command class to provide all the various states possible for the BarrierOperator
     * 
     * @author sankala
     *
     */
    public static class ZWaveBarrierOperatorValueEvent extends ZWaveCommandClassValueEvent {

        private BarrierOperatorStateType barrierState;

        public ZWaveBarrierOperatorValueEvent(int nodeId, int endpoint, CommandClass commandClass, Object value,
                BarrierOperatorStateType barrierState) {
            super(nodeId, endpoint, commandClass, value);

            this.barrierState = barrierState;
        }

        public BarrierOperatorStateType getBarrierState() {
            return barrierState;
        }

    }
}

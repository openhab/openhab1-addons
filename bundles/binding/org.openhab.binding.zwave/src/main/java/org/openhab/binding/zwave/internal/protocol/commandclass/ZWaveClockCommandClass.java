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

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
 * Handles the clock command class.
 *
 * @author Chris Jackson
 * @author Jorg de Jong
 *
 */
@XStreamAlias("clockCommandClass")
public class ZWaveClockCommandClass extends ZWaveCommandClass
        implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

    @XStreamOmitField
    private static final Logger logger = LoggerFactory.getLogger(ZWaveClockCommandClass.class);

    private static final int CLOCK_SET = 4;
    private static final int CLOCK_GET = 5;
    private static final int CLOCK_REPORT = 6;

    /**
     * Creates a new instance of the ZWaveClockCommandClass class.
     *
     * @param node the node this command class belongs to
     * @param controller the controller to use
     * @param endpoint the endpoint this Command class belongs to
     */
    public ZWaveClockCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.CLOCK;
    }

    /**
     * Gets a SerialMessage with the CLOCK_GET command
     *
     * @return the serial message.
     */
    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for command CLOCK_GET", getNode().getNodeId());

        SerialMessage result = new SerialMessage(getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        ByteArrayOutputStream outputData = new ByteArrayOutputStream();
        outputData.write(getNode().getNodeId());
        outputData.write(2);
        outputData.write(getCommandClass().getKey());
        outputData.write(CLOCK_GET);
        result.setMessagePayload(outputData.toByteArray());
        return result;
    }

    /**
     * Gets a SerialMessage with the CLOCK_SET command
     *
     * @return the serial message.
     */
    public SerialMessage getSetMessage(Calendar cal) {
        logger.debug("NODE {}: Creating new message for command CLOCK_SET", getNode().getNodeId());

        int day = cal.get(Calendar.DAY_OF_WEEK) == 1 ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        SerialMessage result = new SerialMessage(getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Immediate);

        ByteArrayOutputStream outputData = new ByteArrayOutputStream();
        outputData.write(getNode().getNodeId());
        outputData.write(4);
        outputData.write(getCommandClass().getKey());
        outputData.write(CLOCK_SET);
        outputData.write((day << 5) | hour);
        outputData.write(minute);
        result.setMessagePayload(outputData.toByteArray());
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ZWaveSerialMessageException
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.debug("NODE {}: Received CLOCK command V{}", getNode().getNodeId(), getVersion());
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case CLOCK_REPORT:
                int day = serialMessage.getMessagePayloadByte(offset + 1) >> 5;
                int hour = serialMessage.getMessagePayloadByte(offset + 1) & 0x1f;
                int minute = serialMessage.getMessagePayloadByte(offset + 2);
                String days[] = new DateFormatSymbols().getWeekdays();
                int javaDay = day == 7 ? 1 : day + 1;
                logger.debug(String.format("NODE %d: Received clock report: %s %02d:%02d", getNode().getNodeId(),
                        days[javaDay], hour, minute));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, javaDay);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                Date nodeTime = cal.getTime();
                ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(getNode().getNodeId(), endpoint,
                        getCommandClass(), nodeTime);
                getController().notifyEventListeners(zEvent);
                break;
            default:
                logger.warn(String.format("NODE %d: Unsupported Command %d for command class %s (0x%02X).",
                        getNode().getNodeId(), command, getCommandClass().getLabel(), getCommandClass().getKey()));
                break;
        }
    }

    @Override
    public Collection<SerialMessage> getDynamicValues(boolean refresh) {
        ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
        if (refresh == true && getEndpoint() == null) {
            result.add(getValueMessage());
        }
        return result;
    }
}

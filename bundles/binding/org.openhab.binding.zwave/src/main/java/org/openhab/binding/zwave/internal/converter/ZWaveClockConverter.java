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
package org.openhab.binding.zwave.internal.converter;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveClockCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Handles interaction with the Clock command class
 *
 * @author Chris Jackson
 * @since 1.9.0
 */
public class ZWaveClockConverter extends ZWaveCommandClassConverter<ZWaveClockCommandClass> {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveClockConverter.class);
    private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the multi level switch;

    /**
     * Constructor. Creates a new instance of the {@link ZWaveClockConverter} class.
     *
     * @param controller the {@link ZWaveController} to use for sending messages.
     * @param eventPublisher the {@link EventPublisher} to use to publish events.
     */
    public ZWaveClockConverter(ZWaveController controller, EventPublisher eventPublisher) {
        super(controller, eventPublisher);

        // State and commmand converters used by this converter.
        this.addStateConverter(new IntegerDecimalTypeConverter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialMessage executeRefresh(ZWaveNode node, ZWaveClockCommandClass commandClass, int endpointId,
            Map<String, String> arguments) {
        logger.debug("NODE {}: Generating poll message for {}, endpoint {}", node.getNodeId(),
                commandClass.getCommandClass().getLabel(), endpointId);

        return node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String, String> arguments) {
        ZWaveStateConverter<?, ?> converter = getStateConverter(item, event.getValue());

        if (converter == null) {
            logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring event.", event.getNodeId(),
                    item.getName(), event.getEndpoint());
            return;
        }

        Object val = event.getValue();
        if (val == null) {
            return;
        }

        Date date = (Date) val;
        State state = converter.convertFromValueToState(date.getTime() - System.currentTimeMillis());
        getEventPublisher().postUpdate(item.getName(), state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveCommand(Item item, Command command, ZWaveNode node, ZWaveClockCommandClass commandClass,
            int endpointId, Map<String, String> arguments) {

        // It's not an ON command from a button switch, do not set
        if (command != OnOffType.ON) {
            return;
        }

        // get the set message - will return null if not supported
        SerialMessage serialMessage = node.encapsulate(commandClass.getSetMessage(Calendar.getInstance()), commandClass,
                endpointId);
        if (serialMessage == null) {
            logger.warn("NODE {}: Meter reset not supported for item = {}, endpoint = {}, ignoring event.",
                    node.getNodeId(), item.getName(), endpointId);
            return;
        }

        // send set message
        getController().sendData(serialMessage);

        // poll the device
        for (SerialMessage serialGetMessage : commandClass.getDynamicValues(true)) {
            getController().sendData(node.encapsulate(serialGetMessage, commandClass, endpointId));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int getRefreshInterval() {
        return REFRESH_INTERVAL;
    }
}

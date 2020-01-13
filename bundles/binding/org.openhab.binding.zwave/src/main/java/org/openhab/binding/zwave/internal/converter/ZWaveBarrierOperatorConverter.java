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

import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.BinaryOnOffCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.StringStringTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBarrierOperatorCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBarrierOperatorCommandClass.BarrierOperatorStateType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBarrierOperatorCommandClass.ZWaveBarrierOperatorValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZWaveBarrierOperatorConverter extends ZWaveCommandClassConverter<ZWaveBarrierOperatorCommandClass> {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveBarrierOperatorConverter.class);
    private static final int REFRESH_INTERVAL = 10;

    public ZWaveBarrierOperatorConverter(ZWaveController controller, EventPublisher eventPublisher) {
        super(controller, eventPublisher);

        this.addStateConverter(new StringStringTypeConverter());
        this.addStateConverter(new IntegerOpenClosedTypeConverter());
        this.addStateConverter(new IntegerOnOffTypeConverter());

        this.addCommandConverter(new BinaryOnOffCommandConverter());
    }

    @Override
    SerialMessage executeRefresh(ZWaveNode node, ZWaveBarrierOperatorCommandClass commandClass, int endpointId,
            Map<String, String> arguments) {
        logger.debug("NODE {}: Generating poll message for {}, endpoint {}", node.getNodeId(),
                commandClass.getCommandClass().getLabel(), endpointId);
        return node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
    }

    @Override
    void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String, String> arguments) {
        ZWaveStateConverter<?, ?> converter = this.getStateConverter(item, event.getValue());

        BarrierOperatorStateType barrierOperatorState = null;
        if (event instanceof ZWaveBarrierOperatorValueEvent) {
            barrierOperatorState = ((ZWaveBarrierOperatorValueEvent) event).getBarrierState();
            if (converter == null) {
                converter = this.getStateConverter(item, barrierOperatorState.getLabel());
            }
        }

        logger.debug("NODE {}: Converter for for item = {}, event value = {}", event.getNodeId(), item.getName(),
                event.getValue());

        if (converter == null) {
            logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring event.", event.getNodeId(),
                    item.getName(), event.getEndpoint());
            return;
        }

        State state = null;

        if (converter instanceof StringStringTypeConverter && barrierOperatorState != null) {
            barrierOperatorState = ((ZWaveBarrierOperatorValueEvent) event).getBarrierState();
            if (barrierOperatorState == BarrierOperatorStateType.STATE_STOPPED) {
                state = converter.convertFromValueToState(
                        barrierOperatorState.getLabel() + " - " + barrierOperatorState.getValue() + "%");
            } else {
                state = converter
                        .convertFromValueToState(((ZWaveBarrierOperatorValueEvent) event).getBarrierState().getLabel());
            }

        } else {
            state = converter.convertFromValueToState(event.getValue());
        }

        this.getEventPublisher().postUpdate(item.getName(), state);
    }

    @Override
    void receiveCommand(Item item, Command command, ZWaveNode node, ZWaveBarrierOperatorCommandClass commandClass,
            int endpointId, Map<String, String> arguments) {
        ZWaveCommandConverter<?, ?> converter = this.getCommandConverter(command.getClass());

        if (converter == null) {
            logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring command.", node.getNodeId(),
                    item.getName(), endpointId);
            return;
        }

        SerialMessage serialMessage = node.encapsulate(
                commandClass.setValueMessage((Integer) converter.convertFromCommandToValue(item, command)),
                commandClass, endpointId);

        if (serialMessage == null) {
            logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(),
                    commandClass.getCommandClass().getLabel(), endpointId);
            return;
        }

        this.getController().sendData(serialMessage);

        if (command instanceof State) {
            this.getEventPublisher().postUpdate(item.getName(), (State) command);
        }
    }

    @Override
    int getRefreshInterval() {
        return REFRESH_INTERVAL;
    }

}

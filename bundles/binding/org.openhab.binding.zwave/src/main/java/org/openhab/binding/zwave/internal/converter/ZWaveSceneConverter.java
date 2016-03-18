/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.BinaryDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveSceneActivationCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveSceneConverter class. Converters between binding items
 * and the Z-Wave API for scene controllers.
 *
 * @author Chris Jackson
 * @since 1.4.0
 */
public class ZWaveSceneConverter extends ZWaveCommandClassConverter<ZWaveSceneActivationCommandClass> {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveSceneConverter.class);

    /**
     * Constructor. Creates a new instance of the {@link ZWaveConverterBase}
     * class.
     *
     * @param controller the {@link ZWaveController} to use to send messages.
     * @param eventPublisher the {@link EventPublisher} that can be used to send updates.
     */
    public ZWaveSceneConverter(ZWaveController controller, EventPublisher eventPublisher) {
        super(controller, eventPublisher);

        // State converters used by this converter.
        this.addStateConverter(new BinaryDecimalTypeConverter());
        this.addStateConverter(new IntegerOnOffTypeConverter());
        this.addStateConverter(new IntegerOpenClosedTypeConverter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int getRefreshInterval() {
        return 0;
    }

    @Override
    SerialMessage executeRefresh(ZWaveNode node, ZWaveSceneActivationCommandClass commandClass, int endpointId,
            Map<String, String> arguments) {
        return null;
    }

    @Override
    void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String, String> arguments) {
        if (arguments.get("scene") == null || arguments.get("state") == null) {
            logger.warn("NODE {}: Scene arguments not set scene={} state={}", event.getNodeId(), arguments.get("scene"),
                    arguments.get("state"));
            return;
        }

        Integer scene = null;
        Integer state = null;
        try {
            scene = Integer.parseInt(arguments.get("scene"));
            state = Integer.parseInt(arguments.get("state"));
        } catch (NumberFormatException e) {
            logger.error("NODE {}: Number format exception {} {}", event.getNodeId(), arguments.get("scene"),
                    arguments.get("state"));
            return;
        }

        if (scene != (Integer) event.getValue()) {
            return;
        }

        ZWaveStateConverter<?, ?> converter = this.getStateConverter(item, state);
        if (converter == null) {
            logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(),
                    event.getNodeId(), event.getEndpoint());
            return;
        }

        State itemState = converter.convertFromValueToState(event.getValue());
        this.getEventPublisher().postUpdate(item.getName(), itemState);
    }

    @Override
    void receiveCommand(Item item, Command command, ZWaveNode node, ZWaveSceneActivationCommandClass commandClass,
            int endpointId, Map<String, String> arguments) {
    }
}

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
import java.util.Properties;

import org.openhab.binding.zwave.internal.converter.state.BinaryDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCentralSceneCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveSceneConverter class. Converters between binding items and the Z-Wave
 * API for scene controllers.
 *
 * @author Chris Jackson, Robert Savage
 */
public class ZWaveCentralSceneConverter extends ZWaveCommandClassConverter<ZWaveCentralSceneCommandClass> {

    private static final Logger logger = LoggerFactory.getLogger(ZWaveCentralSceneConverter.class);

    /**
     * Constructor. Creates a new instance of the
     * {@link ZWaveCentralSceneConverter} class.
     *
     * @param controller the {@link ZWaveController} to use to send messages.
     * @param eventPublisher the {@link EventPublisher} that can be used to send updates.
     */
    public ZWaveCentralSceneConverter(ZWaveController controller, EventPublisher eventPublisher) {
        super(controller, eventPublisher);

        // State converters used by this converter.
        this.addStateConverter(new BinaryDecimalTypeConverter());
        this.addStateConverter(new IntegerOnOffTypeConverter());
        this.addStateConverter(new IntegerOpenClosedTypeConverter());
        this.addStateConverter(new IntegerDecimalTypeConverter());
        this.addStateConverter(new IntegerPercentTypeConverter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int getRefreshInterval() {
        return 0;
    }

    @Override
    SerialMessage executeRefresh(ZWaveNode node, ZWaveCentralSceneCommandClass commandClass, int endpointId,
            Map<String, String> arguments) {
        return null;
    }

    @Override
    void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String, String> arguments) {
        org.openhab.core.types.State itemState = null;

        // get the central scene command value event properties
        Properties properties = (Properties) event.getValue();
        int event_scene = (Integer) properties.get("scene");
        int event_key = (Integer) properties.get("key");

        // if the optional "scene" argument was defined in the item definition,
        // then only forward the event for the specified scene number
        if (arguments.get("scene") != null) {

            // get the item specified scene number
            Integer scene = null;
            try {
                scene = Integer.parseInt(arguments.get("scene"));
            } catch (NumberFormatException e) {
                logger.error("NODE {}: Number format exception scene={}", event.getNodeId(), arguments.get("scene"));
                return;
            }

            // ensure the item specified scene number matches the scene number
            // from the received command event
            if (scene != null && scene != event_scene) {
                return;
            }

            // if the optional "key" argument was defined in the item
            // definition,
            // then only forward the event for the specified matching key state
            if (arguments.get("key") != null) {

                // get the item specified key (state)
                Integer key = null;
                try {
                    key = Integer.parseInt(arguments.get("key"));
                } catch (NumberFormatException e) {
                    logger.error("NODE {}: Number format exception key={}", event.getNodeId(), arguments.get("key"));
                    return;
                }

                // ensure the item specified key number matches the key number
                // from the received command event
                if (key != null && key != event_key) {
                    return;
                }

                // get state converter
                ZWaveStateConverter<?, ?> converter = this.getStateConverter(item, event_scene);

                if (converter == null) {
                    logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.",
                            item.getName(), event.getNodeId(), event.getEndpoint());
                    return;
                }

                // convert the scene number and key state to a binary value on a
                // successful match
                itemState = converter.convertFromValueToState((event_key == key) ? 1 : 0);
            } else {
                // get state converter
                ZWaveStateConverter<?, ?> converter = this.getStateConverter(item, event_key);

                if (converter == null) {
                    logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.",
                            item.getName(), event.getNodeId(), event.getEndpoint());
                    return;
                }

                // convert the scene's key number to an acceptable conversion
                // type and then publish the event
                itemState = converter.convertFromValueToState(event_key);
            }
        } else {
            // no scene argument provide, so we will convert the central scene
            // number
            itemState = new DecimalType(event_scene);
        }

        // publish the central scene update
        this.getEventPublisher().postUpdate(item.getName(), itemState);
    }

    @Override
    void receiveCommand(Item item, Command command, ZWaveNode node, ZWaveCentralSceneCommandClass commandClass,
            int endpointId, Map<String, String> arguments) {
    }
}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.Map;
import org.openhab.binding.zwave.internal.converter.ZWaveCommandClassConverter;
import org.openhab.binding.zwave.internal.converter.command.BinaryOnOffCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.BinaryDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.BinaryPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerUpDownTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveSwitchAllCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveSwitchAllConverter class. Converter for communication with the 
 * {@link ZWaveSwitchAllCommandClass}. Implements All On/All Off.
 * @author Pedro Paixao
 * @since 1.8.0
 */
public class ZWaveSwitchAllConverter extends ZWaveCommandClassConverter<ZWaveSwitchAllCommandClass> {
    private static final Logger logger = LoggerFactory.getLogger(ZWaveSwitchAllConverter.class);

    /**
	 * Constructor. Creates a new instance of the {@link ZWaveSwitchAllConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
    public ZWaveSwitchAllConverter(ZWaveController controller, EventPublisher eventPublisher) {
        super(controller, eventPublisher);
        this.addStateConverter(new BinaryDecimalTypeConverter());
        this.addStateConverter(new BinaryPercentTypeConverter());
        this.addStateConverter(new IntegerOnOffTypeConverter());
        this.addStateConverter(new IntegerOpenClosedTypeConverter());
        this.addStateConverter(new IntegerUpDownTypeConverter());
        this.addCommandConverter(new BinaryOnOffCommandConverter());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public SerialMessage executeRefresh(ZWaveNode node, 
    		ZWaveSwitchAllCommandClass commandClass, int endpointId, Map<String, String> arguments) {
    	return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String, String> arguments) {
    	ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());
    	
        if (converter == null) {
            logger.warn("NODE {}: No converter found for item = {}, node = {} endpoint = {}, ignoring event.", new Object[]{event.getNodeId(), item.getName(), event.getEndpoint()});
            return;
        }
        
        State state = converter.convertFromValueToState(event.getValue());
        this.getEventPublisher().postUpdate(item.getName(), state);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void receiveCommand(Item item, Command command, ZWaveNode node, ZWaveSwitchAllCommandClass commandClass, int endpointId, Map<String, String> arguments) {
    	ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
        
    	if (converter == null) {
            logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring command.", new Object[]{node.getNodeId(), item.getName(), endpointId});
            return;
        }
        
    	Integer cmd = (Integer)converter.convertFromCommandToValue(item, command);
        
    	if (cmd == 0x00) {
            this.getController().allOff();
        } else {
            this.getController().allOn();
        }
        
    	if (command instanceof State) {
            this.getEventPublisher().postUpdate(item.getName(), (State)command);
        }
    }
    
    /**
	 * {@inheritDoc}
	 */
    @Override
    int getRefreshInterval() {
        return 0;
    }
}


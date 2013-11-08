/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.DecimalCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatSetpointCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveThermostatSetpointConverter class. Converter for communication with the 
 * {@link ZWaveThermostatSetpointCommandClass}. Implements polling of the setpoint
 * status and receiving of setpoint events.
 * @author Matthew Bowman
 * @since 1.4.0
 */
public class ZWaveThermostatSetpointConverter extends
		ZWaveCommandClassConverter<ZWaveThermostatSetpointCommandClass> {
	
	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatSetpointConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the thermostat setpoint;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveThermostatSetpointConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveThermostatSetpointConverter(ZWaveController controller,
			EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		this.addCommandConverter(new DecimalCommandConverter());
		this.addStateConverter(new BigDecimalDecimalTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void executeRefresh(ZWaveNode node,
			ZWaveThermostatSetpointCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {

		logger.trace("executeRefresh()");
		
		logger.debug("Generating poll message for {} for node {} endpoint {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
		SerialMessage serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
		
		if (serialMessage == null) {
			logger.warn("Generating message failed for command class = {}, node = {}, endpoint = {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
			return;
		}
		
		this.getController().sendData(serialMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void handleEvent(ZWaveCommandClassValueEvent event, Item item,
			Map<String, String> arguments) {
		
		logger.trace("handleEvent()");
		
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), event.getNodeId(), event.getEndpoint());
			return;
		}
		
		State state = converter.convertFromValueToState(event.getValue());
		this.getEventPublisher().postUpdate(item.getName(), state);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveThermostatSetpointCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		
		logger.trace("receiveCommand()");

		ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring command.", item.getName(), node.getNodeId(), endpointId);
			return;
		}
		
		SerialMessage serialMessage = node.encapsulate(commandClass.setValueMessage((BigDecimal)converter.convertFromCommandToValue(item, command)), commandClass, endpointId);
		if (serialMessage == null) {
			logger.warn("Generating message failed for command class = {}, node = {}, endpoint = {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
			return;
		}

		logger.debug("Sending Message: {}", serialMessage);
		this.getController().sendData(serialMessage);

		// if we sent a THERMOSTAT_SETPOINT_SET
		if (command instanceof DecimalType) {
			// follow it with a THERMOSTAT_SETPOINT_GET (ie. refresh)
			executeRefresh(node, commandClass, endpointId, arguments);
		}
		
		if (command instanceof State)
			this.getEventPublisher().postUpdate(item.getName(), (State)command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}


}

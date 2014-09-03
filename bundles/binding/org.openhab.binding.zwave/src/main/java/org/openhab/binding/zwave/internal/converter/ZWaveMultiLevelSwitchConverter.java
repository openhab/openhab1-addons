/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.MultiLevelIncreaseDecreaseCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.MultiLevelOnOffCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.MultiLevelPercentCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.MultiLevelUpDownCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.RestoreValueMultiLevelOnOffCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerUpDownTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBatteryCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiLevelSwitchCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveBinarySwitchConverter class. Converter for communication with the 
 * {@link ZWaveBatteryCommandClass}. Implements polling of the battery
 * status and receiving of battery events.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveMultiLevelSwitchConverter extends ZWaveCommandClassConverter<ZWaveMultiLevelSwitchCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveMultiLevelSwitchConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the multi level switch;

	/**
	 * Normal On / Off converter converts on commands to 100%
	 */
	private final MultiLevelOnOffCommandConverter normalOnOffConverter = new MultiLevelOnOffCommandConverter();
	
	/**
	 * Restore On / Off converter converts ON commands to the last value;
	 */
	private final RestoreValueMultiLevelOnOffCommandConverter restoreValueOnOffConverter = new RestoreValueMultiLevelOnOffCommandConverter();
	
	/**
	 * Constructor. Creates a new instance of the {@link ZWaveMultiLevelSwitchConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveMultiLevelSwitchConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		
		// State and commmand converters used by this converter. 
		this.addStateConverter(new IntegerDecimalTypeConverter());
		this.addStateConverter(new IntegerPercentTypeConverter());
		this.addStateConverter(new IntegerOnOffTypeConverter());
		this.addStateConverter(new IntegerOpenClosedTypeConverter());
		this.addStateConverter(new IntegerUpDownTypeConverter());
		
		this.addCommandConverter(new MultiLevelIncreaseDecreaseCommandConverter());
		this.addCommandConverter(new MultiLevelPercentCommandConverter());
		this.addCommandConverter(new MultiLevelUpDownCommandConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeRefresh(ZWaveNode node, 
			ZWaveMultiLevelSwitchCommandClass commandClass, int endpointId, Map<String,String> arguments) {
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
	public void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String,String> arguments) {
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), event.getNodeId(), event.getEndpoint());
			return;
		}

		State state = converter.convertFromValueToState(event.getValue());
		if ("true".equalsIgnoreCase(arguments.get("invert_state"))) {
			// Support inversion of roller shutter UP/DOWN and percentages
			if (converter instanceof IntegerUpDownTypeConverter) {
				if(state == UpDownType.UP)
					state = UpDownType.DOWN;
				else
					state = UpDownType.UP;
			}
		}
		if ("true".equalsIgnoreCase(arguments.get("invert_percent")) &&	converter instanceof IntegerPercentTypeConverter) {
			state = new PercentType(100 - ((DecimalType)state).intValue());
		}
		this.getEventPublisher().postUpdate(item.getName(), state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveMultiLevelSwitchCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		SerialMessage serialMessage = null;
		String restoreLastValue = null;
		if (command instanceof StopMoveType && (StopMoveType)command == StopMoveType.STOP) {
			// special handling for the STOP command
			serialMessage = commandClass.stopLevelChangeMessage();
		} else {
			ZWaveCommandConverter<?,?> converter = null;
			if (command instanceof OnOffType) {
				restoreLastValue = arguments.get("restore_last_value");
				
				if ("true".equalsIgnoreCase(restoreLastValue))
					converter = this.restoreValueOnOffConverter;
				else 
					converter = this.normalOnOffConverter;
			} else {
				converter = this.getCommandConverter(command.getClass());				
			}
			
			if (converter == null) {
				logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring command.", item.getName(), node.getNodeId(), endpointId);
				return;
			}

			// Allow inversion of roller shutter UP/DOWN
			if (converter instanceof MultiLevelUpDownCommandConverter) {
				logger.debug("Multilevel Switch MultiLevelUpDownCommandConverter");
				if ("true".equalsIgnoreCase(arguments.get("invert_state"))) {
					logger.debug("Multilevel Switch MultiLevelUpDownCommandConverter - invert");
					if(command == UpDownType.UP)
						command = UpDownType.DOWN;
					else
						command = UpDownType.UP;
					logger.debug("Multilevel Switch MultiLevelUpDownCommandConverter - inverted: {}", command);
				}
			}
			
			// Alloe inversion of roller shutter PERCENT value
			if(converter instanceof MultiLevelPercentCommandConverter){
				logger.debug("Multilevel Switch MultiLevelPercentCommandConverter");
				if ("true".equalsIgnoreCase(arguments.get("invert_percent"))) {
					logger.debug("Multilevel Switch MultiLevelPercentCommandConverter - invert");
					command = new PercentType(100 - ((DecimalType)command).intValue());
					logger.debug("Multilevel Switch MultiLevelPercentCommandConverter - inverted: {}", command);
				}
			}
			

			Integer value = (Integer)converter.convertFromCommandToValue(item, command);
			logger.trace("Converted command '{}' to value {} for item = {}, node = {}, endpoint = {}.", command.toString(), value, item.getName(), node.getNodeId(), endpointId);

			serialMessage = commandClass.setValueMessage(value);
		}

		// encapsulate the message in case this is a multi-instance node
		serialMessage = node.encapsulate(serialMessage, commandClass, endpointId);
		
		if (serialMessage == null) {
			logger.warn("Generating message failed for command class = {}, node = {}, endpoint = {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
			return;
		}
		
		this.getController().sendData(serialMessage);

		// update the bus in case of normal dimming. schedule refresh in case of restore to last value dimming.
		if (!"true".equalsIgnoreCase(restoreLastValue) && command instanceof OnOffType && (OnOffType)command == OnOffType.ON)
			executeRefresh(node, commandClass, endpointId, arguments);
		else if (command instanceof State)
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

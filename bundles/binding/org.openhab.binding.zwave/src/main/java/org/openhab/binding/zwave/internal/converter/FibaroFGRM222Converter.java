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

import org.openhab.binding.zwave.internal.converter.command.MultiLevelPercentCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.MultiLevelUpDownCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.proprietary.FibaroFGRM222CommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wenzel
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class FibaroFGRM222Converter extends ZWaveCommandClassConverter<FibaroFGRM222CommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(FibaroFGRM222Converter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds

	public FibaroFGRM222Converter(final ZWaveController controller, final EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		this.addStateConverter(new IntegerPercentTypeConverter());
		this.addStateConverter(new IntegerOnOffTypeConverter());

		this.addCommandConverter(new MultiLevelPercentCommandConverter());
		this.addCommandConverter(new MultiLevelUpDownCommandConverter());
	}

	@Override
	SerialMessage executeRefresh(final ZWaveNode node, final FibaroFGRM222CommandClass commandClass,
			final int endpointId, final Map<String, String> arguments) {
		logger.debug("NODE {}: executeRefresh() -- nothing to do", node.getNodeId());
		return null;
	}

	@Override
	void handleEvent(final ZWaveCommandClassValueEvent event, final Item item, final Map<String, String> arguments) {
		logger.debug("NODE {}: handleEvent()", event.getNodeId());
		ZWaveStateConverter<?, ?> converter = this.getStateConverter(item, event.getValue());

		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring event.", event.getNodeId(),
					item.getName(), event.getEndpoint());
			return;
		}

		String sensorType = arguments.get("type");
		FibaroFGRM222CommandClass.FibaroFGRM222ValueEvent sensorEvent = (FibaroFGRM222CommandClass.FibaroFGRM222ValueEvent) event;
		// Don't trigger event if this item is bound to another sensor type
		if (sensorType != null && !sensorType.equalsIgnoreCase(sensorEvent.getSensorType().name())) {
			return;
		}
		State state = converter.convertFromValueToState(event.getValue());
		if (converter instanceof IntegerPercentTypeConverter) {
			state = new PercentType(100 - ((DecimalType) state).intValue());
		}
		this.getEventPublisher().postUpdate(item.getName(), state);

	}

	@Override
	void receiveCommand(final Item item, final Command command, final ZWaveNode node,
			final FibaroFGRM222CommandClass commandClass, final int endpointId, final Map<String, String> arguments) {
		logger.debug("NODE {}: receiveCommand()", node.getNodeId());
		Command internalCommand = command;
		SerialMessage serialMessage = null;
		if (internalCommand instanceof StopMoveType && (StopMoveType) internalCommand == StopMoveType.STOP) {
			// special handling for the STOP command
			serialMessage = commandClass.stopLevelChangeMessage(arguments.get("type"));
		} else {
			ZWaveCommandConverter<?, ?> converter = this.getCommandConverter(command.getClass());
			if (converter == null) {
				logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring command.",
						node.getNodeId(), item.getName(), endpointId);
				return;
			}
			if (converter instanceof MultiLevelPercentCommandConverter) {
				internalCommand = new PercentType(100 - ((DecimalType) command).intValue());
			}

			Integer value = (Integer) converter.convertFromCommandToValue(item, internalCommand);
			if (value == 0) {
				value = 1;
			}
			logger.trace("NODE {}: Converted command '{}' to value {} for item = {}, endpoint = {}.", node.getNodeId(),
					internalCommand.toString(), value, item.getName(), endpointId);

			serialMessage = commandClass.setValueMessage(value, arguments.get("type"));
		}

		// encapsulate the message in case this is a multi-instance node
		serialMessage = node.encapsulate(serialMessage, commandClass, endpointId);

		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, node = {}, endpoint = {}",
					node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}
		this.getController().sendData(serialMessage);

		if (command instanceof State) {
			this.getEventPublisher().postUpdate(item.getName(), (State)command);
		}
	}

	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}

}
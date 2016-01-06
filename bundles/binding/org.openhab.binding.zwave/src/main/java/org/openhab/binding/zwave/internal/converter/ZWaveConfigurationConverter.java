/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.List;
import java.util.Map;

import org.openhab.binding.zwave.internal.config.ZWaveDbConfigurationParameter;
import org.openhab.binding.zwave.internal.config.ZWaveProductDatabase;
import org.openhab.binding.zwave.internal.converter.command.IntegerCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.MultiLevelPercentCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.ConfigurationParameter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass.ZWaveConfigurationParameterEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveConfigurationConverter class. Converter for communication with the 
 * {@link ZWaveConfigurationCommandClass}.
 * @author Chris Jackson
 * @since 1.7.0
 */
public class ZWaveConfigurationConverter extends ZWaveCommandClassConverter<ZWaveConfigurationCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfigurationConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the binary switch;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveConfigurationConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveConfigurationConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);

		// State and commmand converters used by this converter.
		this.addStateConverter(new IntegerDecimalTypeConverter());
		this.addStateConverter(new IntegerPercentTypeConverter());
		this.addStateConverter(new BigDecimalDecimalTypeConverter());

		this.addCommandConverter(new IntegerCommandConverter());
		this.addCommandConverter(new MultiLevelPercentCommandConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage executeRefresh(ZWaveNode node, 
			ZWaveConfigurationCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		logger.debug("NODE {}: Generating poll message for {}, endpoint {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
		String parmNumber = arguments.get("parameter");
		if(parmNumber == null) {
			logger.error("NODE {}: 'parameter' option must be specified.", node.getNodeId());
			return null;
		}
		int parmValue = Integer.parseInt(parmNumber);
		if(parmValue < 0 || parmValue > 255) {
			logger.error("NODE {}: 'parameter' option must be between 0 and 255.", node.getNodeId());
			return null;			
		}
		return node.encapsulate(commandClass.getConfigMessage(parmValue), commandClass, endpointId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String,String> arguments) {
		String parmNumber = arguments.get("parameter");
		ZWaveConfigurationParameterEvent cfgEvent = (ZWaveConfigurationParameterEvent)event;
		// Make sure this is for the parameter we want
		if(cfgEvent.getParameter() != null && cfgEvent.getParameter().getIndex() != Integer.parseInt(parmNumber)) {
			return;
		}
		
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, cfgEvent.getParameter().getValue());
		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {}({}), endpoint = {}, ignoring event.",
					event.getNodeId(), item.getName(), item.getClass().getSimpleName(), event.getEndpoint());
			return;
		}

		State state = converter.convertFromValueToState(cfgEvent.getParameter().getValue());
		this.getEventPublisher().postUpdate(item.getName(), state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveConfigurationCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
		if (converter == null) {
			logger.warn("NODE {}: No converter found for item={}, type={}, endpoint={}, ignoring command.", node.getNodeId(), item.getName(), command.getClass().getSimpleName(), endpointId);
			return;
		}
		String parmNumber = arguments.get("parameter");
		if(parmNumber == null) {
			logger.error("NODE {}: 'parameter' option must be specified.", node.getNodeId());
			return;
		}

		int paramIndex = Integer.parseInt(parmNumber);
		if(paramIndex < 0 || paramIndex > 255) {
			logger.error("NODE {}: 'parameter' option must be between 0 and 255.", node.getNodeId());
			return;			
		}

		ZWaveProductDatabase database = new ZWaveProductDatabase();
		if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId(), node.getApplicationVersion()) == false) {
			logger.error("NODE {}: database can't find product.", node.getNodeId());
			return;						
		}

		List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
		if(configList == null) {
			logger.error("NODE {}: Device has no configuration.", node.getNodeId());
			return;
		}

		ZWaveDbConfigurationParameter dbParameter = null;
		for (ZWaveDbConfigurationParameter parameter : configList) {
			if (parameter.Index == paramIndex) {
				dbParameter = parameter;
				break;
			}
		}
		if(dbParameter == null) {
			logger.error("NODE {}: Device has no parameter {}.", node.getNodeId(), paramIndex);
			return;
		}
		ConfigurationParameter configurationParameter = new ConfigurationParameter(paramIndex, (Integer)converter.convertFromCommandToValue(item, command), dbParameter.Size);

		// Set the parameter
		SerialMessage serialMessage = commandClass.setConfigMessage(configurationParameter);
		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}

		this.getController().sendData(serialMessage);

		// And request a read-back
		serialMessage = commandClass.getConfigMessage(paramIndex);
		this.getController().sendData(serialMessage);

		if (command instanceof State) {
			this.getEventPublisher().postUpdate(item.getName(), (State)command);
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

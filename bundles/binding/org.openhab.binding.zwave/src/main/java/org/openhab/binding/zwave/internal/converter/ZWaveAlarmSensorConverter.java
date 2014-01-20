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

import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.BinaryDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.BinaryPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAlarmSensorCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAlarmSensorCommandClass.AlarmType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAlarmSensorCommandClass.ZWaveAlarmSensorValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveAlarmSensorConverter class. Converter for communication with the 
 * {@link ZWaveAlarmSensorCommandClass}. Implements polling of the alarm sensor
 * status and receiving of alarm sensor events.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveAlarmSensorConverter extends ZWaveCommandClassConverter<ZWaveAlarmSensorCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveAlarmSensorConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the binary switch;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveAlarmSensorConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveAlarmSensorConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		
		// State and commmand converters used by this converter. 
		this.addStateConverter(new BinaryDecimalTypeConverter());
		this.addStateConverter(new BinaryPercentTypeConverter());
		this.addStateConverter(new IntegerOnOffTypeConverter());
		this.addStateConverter(new IntegerOpenClosedTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeRefresh(ZWaveNode node, 
			ZWaveAlarmSensorCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		logger.debug("Generating poll message for {} for node {} endpoint {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
		SerialMessage serialMessage;
		String alarmType = arguments.get("alarm_type");
		
		if (alarmType != null) {
			serialMessage = node.encapsulate(commandClass.getMessage(AlarmType.getAlarmType(Integer.parseInt(alarmType))), commandClass, endpointId);
		} else {
			serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
		}
			
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
		String alarmType = arguments.get("alarm_type");
		ZWaveAlarmSensorValueEvent alarmEvent = (ZWaveAlarmSensorValueEvent)event;
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), event.getNodeId(), event.getEndpoint());
			return;
		}
		
		// Don't trigger event if this item is bound to another alarm type
		if (alarmType != null && AlarmType.getAlarmType(Integer.parseInt(alarmType)) != alarmEvent.getAlarmType())
			return;
		
		State state = converter.convertFromValueToState(event.getValue());
		this.getEventPublisher().postUpdate(item.getName(), state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveAlarmSensorCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring command.", item.getName(), node.getNodeId(), endpointId);
			return;
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

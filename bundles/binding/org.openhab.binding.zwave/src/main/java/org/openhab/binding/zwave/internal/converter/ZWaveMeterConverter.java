/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.BigDecimalOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.BigDecimalOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMeterCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMeterCommandClass.MeterScale;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMeterCommandClass.ZWaveMeterValueEvent;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiLevelSensorCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveMultiLevelSensorConverter class. Converter for communication with the 
 * {@link ZWaveMultiLevelSensorCommandClass}. Implements polling of the sensor
 * status and receiving of sensor events.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveMeterConverter extends ZWaveCommandClassConverter<ZWaveMeterCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveMeterConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the multi level switch;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveMeterConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveMeterConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		
		// State and commmand converters used by this converter. 
		this.addStateConverter(new BigDecimalDecimalTypeConverter());
		this.addStateConverter(new BigDecimalOnOffTypeConverter());
		this.addStateConverter(new BigDecimalOpenClosedTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeRefresh(ZWaveNode node, 
			ZWaveMeterCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		String meterScale = arguments.get("meter_scale");
		SerialMessage serialMessage;

		logger.debug("Generating poll message for {} for node {} endpoint {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
		
		if (meterScale != null) {
			serialMessage = node.encapsulate(commandClass.getMessage(MeterScale.getMeterScale(meterScale)), commandClass, endpointId);
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
		
		if (converter == null) {
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), event.getNodeId(), event.getEndpoint());
			return;
		}
		
		// we ignore any meter reports for item bindings configured with 'meter_reset=true' 
		// since we don't want to be updating the 'reset' switch
		if ("true".equalsIgnoreCase(arguments.get("meter_reset")))
			return;

		String meterScale = arguments.get("meter_scale");
		String meterZero = arguments.get("meter_zero");
		ZWaveMeterValueEvent meterEvent = (ZWaveMeterValueEvent)event;

		// Don't trigger event if this item is bound to another sensor type
		if (meterScale != null && MeterScale.getMeterScale(meterScale) != meterEvent.getMeterScale())
			return;

		Object val = event.getValue();

		// If we've set a zero, then anything below this value needs to be considered ZERO
		if (meterZero != null) {
			if(((BigDecimal)val).doubleValue() <= Double.parseDouble(meterZero))
				val = BigDecimal.ZERO;
		}

		State state = converter.convertFromValueToState(val);
		this.getEventPublisher().postUpdate(item.getName(), state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveMeterCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		
		// It's not an ON command from a button switch, do not reset
		if (command != OnOffType.ON)
			return;
		
		// send reset message
		SerialMessage serialMessage = node.encapsulate(commandClass.getResetMessage(), commandClass, endpointId);
		this.getController().sendData(serialMessage);
		
		// poll the device
		for (SerialMessage serialGetMessage : commandClass.getDynamicValues()) {
			this.getController().sendData(node.encapsulate(serialGetMessage, commandClass, endpointId));
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

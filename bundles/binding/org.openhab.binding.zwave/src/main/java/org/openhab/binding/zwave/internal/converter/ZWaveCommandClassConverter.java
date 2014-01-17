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

import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * ZWaveCommandClassConverter class. Base class for all converters
 * that convert between Z-Wave command classes and openHAB
 * items.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 * @param <COMMAND_CLASS_TYPE> the {@link ZWaveCommandClass} that this converter converts for.
 */
public abstract class ZWaveCommandClassConverter<COMMAND_CLASS_TYPE extends ZWaveCommandClass>
	extends ZWaveConverterBase {
	
	/**
	 * Constructor. Creates a new instance of the {@link ZWaveCommandClassConverter}
	 * class.
	 * @param controller the {@link ZWaveController} to use to send messages.
	 * @param eventPublisher the {@link EventPublisher} that can be used to send updates.
	 */
	public ZWaveCommandClassConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
	}

	/**
	 * Execute refresh method. This method is called every time a binding item is
	 * refreshed and the corresponding node should be sent a message.
	 * @param node the {@link ZWaveNode} that is bound to the item.
	 * @param commandClass the {@link ZWaveCommandClass} that will be used to send a polling message.
	 * @param endpointId the endpoint id to send the message.
	 */
	abstract void executeRefresh(ZWaveNode node, COMMAND_CLASS_TYPE commandClass, int endpointId, Map<String,String> arguments);

	/**
	 * Handles an incoming {@link ZWaveCommandClassValueEvent}. Implement
	 * this message in derived classes to convert the value and post an
	 * update on the openHAB bus.
	 * @param event the received {@link ZWaveCommandClassValueEvent}.
	 * @param item the {@link Item} that should receive the event.
	 */
	abstract void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String,String> arguments);
	
	/**
	 * Receives a command from openHAB and translates it to an operation
	 * on the Z-Wave network.
	 * @param command the received command
	 * @param node the {@link ZWaveNode} to send the command to
	 * @param commandClass the {@link ZWaveCommandClass} to send the command to.
	 * @param endpointId the endpoint ID to send the command to.
	 */
	abstract void receiveCommand(Item item, Command command, ZWaveNode node, COMMAND_CLASS_TYPE commandClass, int endpointId, Map<String,String> arguments);
	
}

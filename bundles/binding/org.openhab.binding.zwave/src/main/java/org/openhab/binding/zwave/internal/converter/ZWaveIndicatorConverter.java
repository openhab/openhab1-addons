/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.BinaryOnOffCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.IntegerCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveIndicatorCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveIndicatorCommandClassChangeEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveIndicatorConverter class. Converter for communication with the 
 * {@link ZWaveIndicatorCommandClass}.
 * @author Pedro Paixao
 * @since 1.8.0
 */
public class ZWaveIndicatorConverter extends ZWaveCommandClassConverter<ZWaveIndicatorCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveIndicatorConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the binary switch;
	
	private static final int ON = 1;  // A bit is set to 1 (ON)
	private static final int OFF = 0; // A bit is set to 0 (OFF)  
	
	private static final int PENDING_ON = 1;  //Pending ON change 
	private static final int PENDING_OFF = -1; // Pending OFF Change
	private static final int NO_PENDING_CHANGES = 0; // No pending changes for a bit
	
	private HashMap<String, HashMap<Integer,Integer>> pending;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveIndicatorConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveIndicatorConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		
		// State and command converters used by this converter. 
		this.addStateConverter(new IntegerDecimalTypeConverter());
		this.addStateConverter(new IntegerPercentTypeConverter());
		this.addStateConverter(new IntegerOnOffTypeConverter());
		this.addStateConverter(new IntegerOpenClosedTypeConverter());
		
		this.addCommandConverter(new IntegerCommandConverter());
		this.addCommandConverter(new BinaryOnOffCommandConverter());
		
		pending = new HashMap<String, HashMap<Integer,Integer>> ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage executeRefresh(ZWaveNode node, 
			ZWaveIndicatorCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		logger.debug("NODE {}: Generating poll message for {}, endpoint {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
		return node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Process Z-Wave Indicator Report events. Apply any pending changes (see receiveCommand) and if
	 * indicator is changed in any way issue an INDICATOR_SET message to update the node's indicator value
	 */
	@Override
	public void handleEvent(ZWaveCommandClassValueEvent event, Item item, Map<String,String> arguments) {
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());
		
		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {}, ignoring event.", event.getNodeId(), item.getName());
			return;
		}
		
		if( !arguments.containsKey("bit")) {
			logger.error("NODE {}: You must specify an Indicator bit for item = {}, ignoring event.", event.getNodeId(), item.getName());
			return;
		}
		
		int bit = Integer.parseInt(arguments.get("bit"));
		ZWaveIndicatorCommandClassChangeEvent e = (ZWaveIndicatorCommandClassChangeEvent) event;
		
		// Get INDICATOR Command Class to check if node supports it and later to generate the INDICATOR_SET message
		// to the node to set the new indicator value
		ZWaveNode node = this.getController().getNode(event.getNodeId());
		ZWaveIndicatorCommandClass commandClass = (ZWaveIndicatorCommandClass) node.getCommandClass(CommandClass.INDICATOR);
				
		if( commandClass == null ) {
			logger.warn("NODE {}: Indicator command Class not supported for Item = {}, ignoring event.", event.getNodeId(), item.getName());
			return;
		}
		
		// Assume no changes and get the actual indicator value just received from the node
		boolean madeChanges = false;
		int indicator = (Integer) e.getValue();
		
		logger.warn(String.format("NODE %d: Processing Indicator Event for Item %s indicator = 0x%02X, bit = %d.", event.getNodeId(), item.getName(), indicator, bit));
		
		// Check if the bound bit is ON (1) or OFF (0)
		int isOn = (((byte)indicator) >> bit) & 0x01;
		
		// Check for pending changes from previous receiveCommand() method calls
		if( pending.containsKey(item.getName()) ) {
			if( pending.get(item.getName()).containsKey(bit) ) {
				
				logger.warn(String.format("NODE %d: There are pending changes to Item %s bit = %d", event.getNodeId(), item.getName(), bit));
				
				// check if there are any pending set to ON for the bound bit, and if the current bit value is OFF
				if( pending.get(item.getName()).get(bit) == PENDING_ON && isOn == OFF) {
					// Set bit to ON in current indicator value
					byte buttonMask = (byte) (0x01 << bit);
					indicator = (byte) (indicator | buttonMask);
					
					HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
					h.put(bit, NO_PENDING_CHANGES);
					pending.put(item.getName(), h);
					
					madeChanges = true;
					logger.warn(String.format("NODE %d: Item %s indicator = 0x%02X, Set bit = %d to ON - DONE", event.getNodeId(), item.getName(), indicator, bit));
				}
				
				if( pending.get(item.getName()).get(bit) == PENDING_OFF && isOn == ON) {
					// Set bit to OFF in current indicator value
					byte buttonMask = (byte) ~(0x01 << bit);
					indicator = (byte) (indicator & buttonMask);
					
					HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
					h.put(bit, NO_PENDING_CHANGES);
					pending.put(item.getName(), h);
					
					madeChanges = true;
					logger.warn(String.format("NODE %d: Item %s indicator = 0x%02X, Set bit = %d to OFF - DONE", event.getNodeId(), item.getName(), indicator, bit));
				}
				
				// Check if changes were made send an INDICATOR_SET message to update the node's indicator value
				if( madeChanges ) {
					// recalculate because we made changes to Indicator
					isOn = (((byte) indicator) >> bit) & 0x01;

					// Create an Indicator SET message and send it to the node
					SerialMessage serialMessage = node.encapsulate(commandClass.setValueMessage(indicator), commandClass, 0);
					if (serialMessage == null) {
						logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel());
						return;
					}
					
					this.getController().sendData(serialMessage);
					logger.warn(String.format("NODE %d: Item %s indicator = 0x%02X, Send pending operations to node", event.getNodeId(), item.getName(), indicator, bit));
				}
			}
			else {
				logger.warn(String.format("NODE %d: No pending indicator changes to Item %s bit = %d", event.getNodeId(), item.getName(), bit));
			}
		}
		else {
			logger.warn(String.format("NODE %d: No pending Indicator changes to Item %s bit = %d", event.getNodeId(), item.getName(), bit));
		}
		
		if( isOn == ON) {
			this.getEventPublisher().postUpdate(item.getName(),converter.convertFromValueToState(0xFF));
		}
		else {
			this.getEventPublisher().postUpdate(item.getName(),converter.convertFromValueToState(0x00));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The node's indicator status can change at any time, and without the knowledge of the Z-Wave binding so
	 * before any changes are made to the indicator we need to get it's actual value from the device.
	 * Hence the receiveCommand method adds the desired command to a "pending" list, and issues an INDICATOR_GET
	 * it does not change the actual device Indicator value.
	 * 
	 * When the node responds with the INDICATOR_REPORT pending commands are applied to the indicator value
	 * and the result is then sent to the node. This is performed in the handleEvent() method.
	 * 
	 * Pending changes are stored in a hash of hashes with the following keys "ItemName" and "bit" value is 1
	 * for turn bit ON, -1 to turn the bound bit OFF, 0 no change.
	 *
	 * HashMap<ItemName, HashMap<bit, pendingOperationCode>>
	 */
	@Override
	public void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveIndicatorCommandClass commandClass, int endpointId, Map<String,String> arguments) {
		ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
		
		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring command.", node.getNodeId(), item.getName(), endpointId);
			return;
		}

		if( !arguments.containsKey("bit")) {
			logger.error("NODE {}: You must specify an Indicator bit for item = {}, endpoint = {}, ignoring command.", node.getNodeId(), item.getName(), endpointId);
			return;
		}
		
		int bit = Integer.parseInt(arguments.get("bit"));
		
		if( !(command instanceof OnOffType) )  {
			return;
		}
			
		if (command == OnOffType.ON) {
			HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
			h.put(bit, PENDING_ON);
			pending.put(item.getName(), h);
			logger.warn(String.format("NODE %d: Item %s Set bit = %d to ON - PENDING", node.getNodeId(), item.getName(), bit));
		}
		else {
			logger.warn(String.format("NODE %d: Item %s Set bit = %d to OFF - PENDING", node.getNodeId(), item.getName(), bit));
			HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
			h.put(bit, PENDING_OFF);
			pending.put(item.getName(), h);
		}
		
		// Issue a INDICATOR_GET message to the node to get the updated indicator value
		SerialMessage serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}
		
		this.getController().sendData(serialMessage);
		logger.warn(String.format("NODE %d: Item %s bit = %d Get current Indicator State", node.getNodeId(), item.getName(), bit));

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

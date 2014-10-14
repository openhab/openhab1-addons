/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.ZWaveBindingConfig;
import org.openhab.binding.zwave.ZWaveBindingProvider;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveConverterHandler class. Acts as a factory
 * and manager of all the converters the binding can
 * use to convert between the Z-Wave api and the binding.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveConverterHandler {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveConverterHandler.class);
	
	private final Map<CommandClass, ZWaveCommandClassConverter<?>> converters = new HashMap<CommandClass, ZWaveCommandClassConverter<?>>();
	private final Map<Class<? extends Item>, CommandClass[]> preferredCommandClasses = new HashMap<Class<? extends Item>, CommandClass[]>();
	private final ZWaveController controller;
	private final ZWaveInfoConverter infoConverter;
	
	/**
	 * Constructor. Creates a new instance of the {@ link ZWaveConverterHandler} class.
	 * @param controller the {@link ZWaveController} to use to send messages.
	 * @param eventPublisher the {@link EventPublisher} to use to post updates.
	 */
	public ZWaveConverterHandler(ZWaveController controller, EventPublisher eventPublisher) {
		this.controller = controller;

		// add converters here
		converters.put(CommandClass.THERMOSTAT_SETPOINT, new ZWaveThermostatSetpointConverter(controller, eventPublisher));
		converters.put(CommandClass.THERMOSTAT_MODE, new ZWaveThermostatModeConverter(controller, eventPublisher));
		converters.put(CommandClass.THERMOSTAT_FAN_MODE, new ZWaveThermostatFanModeConverter(controller, eventPublisher));
		converters.put(CommandClass.THERMOSTAT_OPERATING_STATE, new ZWaveThermostatOperatingStateConverter(controller, eventPublisher));
		converters.put(CommandClass.THERMOSTAT_FAN_STATE, new ZWaveThermostatFanStateConverter(controller, eventPublisher));
		converters.put(CommandClass.BATTERY, new ZWaveBatteryConverter(controller, eventPublisher));
		converters.put(CommandClass.SWITCH_BINARY, new ZWaveBinarySwitchConverter(controller, eventPublisher));
		converters.put(CommandClass.SWITCH_MULTILEVEL, new ZWaveMultiLevelSwitchConverter(controller, eventPublisher));
		converters.put(CommandClass.SENSOR_BINARY, new ZWaveBinarySensorConverter(controller, eventPublisher));
		converters.put(CommandClass.SENSOR_MULTILEVEL, new ZWaveMultiLevelSensorConverter(controller, eventPublisher));
		converters.put(CommandClass.SENSOR_ALARM, new ZWaveAlarmSensorConverter(controller, eventPublisher));
		converters.put(CommandClass.METER, new ZWaveMeterConverter(controller, eventPublisher));
		converters.put(CommandClass.BASIC, new ZWaveBasicConverter(controller, eventPublisher));
		converters.put(CommandClass.SCENE_ACTIVATION, new ZWaveSceneConverter(controller, eventPublisher));
		converters.put(CommandClass.ALARM, new ZWaveAlarmConverter(controller, eventPublisher));

		infoConverter = new ZWaveInfoConverter(controller, eventPublisher);
		
		// add preferred command classes per Item class here
		preferredCommandClasses.put(SwitchItem.class, new CommandClass[] { CommandClass.SWITCH_BINARY, CommandClass.SWITCH_MULTILEVEL, 
			CommandClass.METER, CommandClass.BASIC, CommandClass.SENSOR_BINARY, CommandClass.SENSOR_ALARM });
		preferredCommandClasses.put(DimmerItem.class, new CommandClass[] { CommandClass.SWITCH_MULTILEVEL, CommandClass.SWITCH_BINARY, 
			CommandClass.BASIC, CommandClass.SENSOR_MULTILEVEL, CommandClass.SENSOR_BINARY, CommandClass.SENSOR_ALARM });
		preferredCommandClasses.put(RollershutterItem.class, new CommandClass[] { CommandClass.SWITCH_MULTILEVEL, CommandClass.SWITCH_BINARY, 
			CommandClass.BASIC, CommandClass.SENSOR_MULTILEVEL, CommandClass.SENSOR_BINARY, CommandClass.SENSOR_ALARM });
		preferredCommandClasses.put(NumberItem.class, new CommandClass[] { CommandClass.SENSOR_MULTILEVEL, CommandClass.METER, 
			CommandClass.SWITCH_MULTILEVEL, CommandClass.BATTERY, CommandClass.BASIC, CommandClass.SENSOR_BINARY, 
			CommandClass.SENSOR_ALARM, CommandClass.SWITCH_BINARY, CommandClass.THERMOSTAT_SETPOINT, 
			CommandClass.THERMOSTAT_MODE, CommandClass.THERMOSTAT_FAN_MODE, CommandClass.THERMOSTAT_OPERATING_STATE,
			CommandClass.THERMOSTAT_FAN_STATE});
		preferredCommandClasses.put(ContactItem.class, new CommandClass[] { CommandClass.SENSOR_BINARY, CommandClass.SENSOR_ALARM, 
			CommandClass.SWITCH_BINARY, CommandClass.BASIC });
	}

	/**
	 * Returns a converter to convert between the Z-Wave API and the binding.
	 * @param commandClass the {@link CommandClass} to create a converter for.
	 * @return a {@link ZWaveCommandClassConverter} or null if a converter is not found.
	 */
	public ZWaveCommandClassConverter<?> getConverter(CommandClass commandClass) {
		return converters.get(commandClass);
	}
	
	/**
	 * Returns the command class that provides the best suitable converter to convert between the Z-Wave API and the binding.
	 * @param item the {@link item} to resolve a converter for.
	 * @param node the {@link ZWaveNode} node to resolve a Command class on.
	 * @param the enpoint ID to use to resolve a converter.
	 * @return the {@link ZWaveCommandClass} that can be used to get a converter suitable to do the conversion.
	 */
	private ZWaveCommandClass resolveConverter(Item item, ZWaveNode node, int endpointId) {
		if(item == null)
			return null;

		ZWaveMultiInstanceCommandClass multiInstanceCommandClass = null;
		ZWaveCommandClass result = null;
		
		if (endpointId != 1)
			multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass)node.getCommandClass(CommandClass.MULTI_INSTANCE);

		if (!preferredCommandClasses.containsKey(item.getClass())) {
			logger.warn("No preferred command classes found for item class = {}", item.getClass().toString());
			return null;
		}
		
		for (CommandClass commandClass : preferredCommandClasses.get(item.getClass())) {
			if (multiInstanceCommandClass != null && multiInstanceCommandClass.getVersion() == 2) {
				ZWaveEndpoint endpoint = multiInstanceCommandClass.getEndpoint(endpointId);
				
				if (endpoint != null) { 
					result = endpoint.getCommandClass(commandClass);
				} 
			}
			
			if (result == null)
				result = node.getCommandClass(commandClass);
			
			if (result == null)
				continue;
			
			if (multiInstanceCommandClass != null && multiInstanceCommandClass.getVersion() == 1 &&
					result.getInstances() < endpointId)
				continue;
			
			if (converters.containsKey(commandClass))
				return result;
		}
		
		logger.warn("No matching command classes found for item class = {}, node id = {}, endpoint id = {}", 
				item.getClass().toString(), node.getNodeId(), endpointId);
		return null;
	}
	
	/**
	 * Execute refresh method. This method is called every time a binding item is
	 * refreshed and the corresponding node should be sent a message.
	 * @param provider the {@link ZWaveBindingProvider} that provides the item
	 * @param itemName the name of the item to poll.
	 * @param forceRefresh indicates that a polling refresh should be forced.
	 */
	@SuppressWarnings("unchecked")
	public void executeRefresh(ZWaveBindingProvider provider, String itemName, boolean forceRefresh) {
		ZWaveBindingConfig bindingConfiguration = provider.getZwaveBindingConfig(itemName);
		ZWaveCommandClass commandClass;
		String commandClassName = bindingConfiguration.getArguments().get("command");
		
		// this binding is configured not to poll.
		if (!forceRefresh && bindingConfiguration.getRefreshInterval() != null && 0 == bindingConfiguration.getRefreshInterval())
			return;
		
		ZWaveNode node = this.controller.getNode(bindingConfiguration.getNodeId());
		
		// ignore nodes that are not initialized.
		if (node == null)
			return;
		
		if (commandClassName != null) {
			
			// this is a report item, handle it with the report info converter.
			if (commandClassName.equalsIgnoreCase("info")) {
				infoConverter.executeRefresh(provider.getItem(itemName), node, bindingConfiguration.getEndpoint(), bindingConfiguration.getArguments());
				return;
			}
			
			// ignore nodes that are not initialized or dead.
			if (node.getNodeStage() != NodeStage.DONE)
				return;
			
			commandClass = node.resolveCommandClass(CommandClass.getCommandClass(commandClassName), bindingConfiguration.getEndpoint());
			
			if (commandClass == null) {
				 logger.warn("No command class found for item = {}, command class name = {}, ignoring execute refresh.", itemName, commandClassName);
				 return;
			}
		} else {
			commandClass = resolveConverter(provider.getItem(itemName), node
				, bindingConfiguration.getEndpoint());
		}
		
		 if (commandClass == null) {
			 logger.warn("No converter found for item = {}, ignoring execute refresh.", itemName);
			 return;
		 }
		 
		 ZWaveCommandClassConverter<ZWaveCommandClass> converter = (ZWaveCommandClassConverter<ZWaveCommandClass>) getConverter(commandClass.getCommandClass());
		 
		 if (converter == null) {
			 logger.warn("No converter found for item = {}, ignoring execute refresh.", itemName);
			 return;
		 }
		 
		 if (bindingConfiguration.getRefreshInterval() == null) {
			 bindingConfiguration.setRefreshInterval(converter.getRefreshInterval());
	
			 // this binding is configured not to poll.
			if (!forceRefresh && 0 == bindingConfiguration.getRefreshInterval())
				return;
		 }

		 // not enough time has passed to refresh the item.
		 if (!forceRefresh && bindingConfiguration.getLastRefreshed() != null 
				 && (bindingConfiguration.getLastRefreshed().getTime() + (bindingConfiguration.getRefreshInterval() * 1000) 
						 > Calendar.getInstance().getTimeInMillis()))
			 return;
			 
		 bindingConfiguration.setLastRefreshed(Calendar.getInstance().getTime());
		 converter.executeRefresh(node, commandClass, bindingConfiguration.getEndpoint(), bindingConfiguration.getArguments());
	}

	/**
	 * Get the refresh interval for an item binding
	 * 
	 * @param provider
	 *            the {@link ZWaveBindingProvider} that provides the item
	 * @param itemName
	 *            the name of the item to poll.
	 */
	@SuppressWarnings("unchecked")
	public Integer getRefreshInterval(ZWaveBindingProvider provider, String itemName) {
		ZWaveBindingConfig bindingConfiguration = provider.getZwaveBindingConfig(itemName);
		ZWaveCommandClass commandClass;
		String commandClassName = bindingConfiguration.getArguments().get("command");

		// this binding is configured not to poll.
		if (bindingConfiguration.getRefreshInterval() != null && 0 == bindingConfiguration.getRefreshInterval())
			return 0;

		ZWaveNode node = this.controller.getNode(bindingConfiguration.getNodeId());

		// ignore nodes that are not initialized.
		if (node == null)
			return 0;

		if (commandClassName != null) {
			// this is a report item, handle it with the report info converter.
			if (commandClassName.equalsIgnoreCase("info")) {
				return infoConverter.getRefreshInterval();
			}

			commandClass = node.resolveCommandClass(CommandClass.getCommandClass(commandClassName),
					bindingConfiguration.getEndpoint());

			if (commandClass == null) {
				logger.warn("No command class found for item = {}, command class name = {}, using 0 refresh interval.",
						itemName, commandClassName);
				return 0;
			}
		} else {
			commandClass = resolveConverter(provider.getItem(itemName), node, bindingConfiguration.getEndpoint());
		}

		if (commandClass == null) {
			logger.warn("No converter found for item = {}, using 0 refresh interval.", itemName);
			return 0;
		}

		ZWaveCommandClassConverter<ZWaveCommandClass> converter = (ZWaveCommandClassConverter<ZWaveCommandClass>) getConverter(commandClass
				.getCommandClass());

		if (converter == null) {
			logger.warn("No converter found for item = {}, using 0 refresh interval.", itemName);
			return 0;
		}

		if (bindingConfiguration.getRefreshInterval() == null) {
			bindingConfiguration.setRefreshInterval(converter.getRefreshInterval());
		}

		return bindingConfiguration.getRefreshInterval();
	}

	/**
	 * Handles an incoming {@link ZWaveCommandClassValueEvent}. Implement
	 * this message in derived classes to convert the value and post an
	 * update on the openHAB bus.
	 * @param provider the {@link ZWaveBindingProvider} that provides the item
	 * @param itemName the name of the item that will receive the event.
	 * @param event the received {@link ZWaveCommandClassValueEvent}.
	 */
	 public void handleEvent(ZWaveBindingProvider provider, String itemName, ZWaveCommandClassValueEvent event) {
		ZWaveBindingConfig bindingConfiguration = provider.getZwaveBindingConfig(itemName);
		Item item = provider.getItem(itemName);
		String commandClassName = bindingConfiguration.getArguments().get("command");
		boolean respondToBasic = "true".equalsIgnoreCase(bindingConfiguration.getArguments().get("respond_to_basic"));

		logger.trace("Getting converter for item = {}, command class = {}, item command class = {}", itemName, event.getCommandClass().getLabel(), commandClassName);
		
		if (item == null) {
			return;
		}
		
		// check whether this item is bound to the right command class.
		
		if (commandClassName != null && !commandClassName.equalsIgnoreCase(event.getCommandClass().getLabel().toLowerCase()) &&
				!(respondToBasic && event.getCommandClass() == CommandClass.BASIC)) {
			return;
		}
		
		ZWaveCommandClassConverter<?> converter = this.getConverter(event.getCommandClass());
		 
		 
		if (converter == null) {
			logger.warn("No converter found for command class = {}, ignoring event.",event.getCommandClass().toString());
			return;
		}
		 
		converter.handleEvent(event, item, bindingConfiguration.getArguments());
	 }
	
	/**
	 * Receives a command from openHAB and translates it to an operation
	 * on the Z-Wave network.
	 * @param provider the {@link ZWaveBindingProvider} that provides the item
	 * @param itemName the name of the item that will receive the event.
	 * @param command the received {@link Command}
	 */
	@SuppressWarnings("unchecked")
	public void receiveCommand(ZWaveBindingProvider provider, String itemName, Command command) {
		ZWaveBindingConfig bindingConfiguration = provider.getZwaveBindingConfig(itemName);
		ZWaveNode node = this.controller.getNode(bindingConfiguration.getNodeId());
		if(node == null) {
			logger.error("Item {} has non existant node {}", itemName, bindingConfiguration.getNodeId());
			return;
		}
		ZWaveCommandClass commandClass;
		String commandClassName = bindingConfiguration.getArguments().get("command");
		
		if (commandClassName != null) {
			commandClass = node.resolveCommandClass(CommandClass.getCommandClass(commandClassName), bindingConfiguration.getEndpoint());
			
			if (commandClass == null) {
				 logger.warn("No command class found for item = {}, command class name = {}, ignoring command.", itemName, commandClassName);
				 return;
			}
		} else {
			commandClass = resolveConverter(provider.getItem(itemName), node
				, bindingConfiguration.getEndpoint());
		}
		
		 if (commandClass == null) {
			 logger.warn("No converter found for item = {}, ignoring command.", itemName);
			 return;
		 }
		 
		 ZWaveCommandClassConverter<ZWaveCommandClass> converter = (ZWaveCommandClassConverter<ZWaveCommandClass>) getConverter(commandClass.getCommandClass());
		 
		 if (converter == null) {
			 logger.warn("No converter found for item = {}, ignoring command.", itemName);
			 return;
		 }
		 
		 converter.receiveCommand(provider.getItem(itemName), command, node, commandClass, bindingConfiguration.getEndpoint(), bindingConfiguration.getArguments());
	}
		
}

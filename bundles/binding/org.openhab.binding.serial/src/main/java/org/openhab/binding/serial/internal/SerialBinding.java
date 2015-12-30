/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.serial.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

/**
 * <p>This class implements a binding of serial devices to openHAB.
 * The binding configurations are provided by the {@link GenericItemProvider}.</p>
 * 
 * <p>The format of the binding configuration is simple and looks like this:</p>
 * serial="&lt;port&gt;" where &lt;port&gt; is the identification of the serial port on the host system, e.g.
 * "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac
 * <p>Switch items with this binding will receive an ON-OFF update on the bus, whenever data becomes available on the serial interface<br/>
 * String items will receive the submitted data in form of a string value as a status update, while openHAB commands to a Switch item is
 * sent out as data through the serial interface.</p>
 * 
 * @author Kai Kreuzer
 *
 */
public class SerialBinding extends AbstractEventSubscriber implements BindingConfigReader {

	private Map<String, SerialDevice> serialDevices = new HashMap<String, SerialDevice>();

	/** stores information about the which items are associated to which port. The map has this content structure: itemname -> port */ 
	private Map<String, String> itemMap = new HashMap<String, String>();

	/** stores information about the context of items. The map has this content structure: context -> Set of itemNames */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	private EventPublisher eventPublisher = null;

	private TransformationService transformationService;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(eventPublisher);
		}
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(null);
		}
	}
	
	public void setTransformationService(TransformationService transformationService) {
		this.transformationService = transformationService;
		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setTransformationService(transformationService);
		}
	}

	public void unsetTransformationService(TransformationService transformationService) {
		this.transformationService = null;
		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setTransformationService(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveCommand(String itemName, Command command) {
		if(itemMap.keySet().contains(itemName)) {
			SerialDevice serialDevice = serialDevices.get(itemMap.get(itemName));
			if(command instanceof StringType) {
				serialDevice.writeString(command.toString());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveUpdate(String itemName, State newStatus) {
		// ignore any updates
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "serial";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Number- and StringItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		int indexOf = bindingConfig.indexOf(',');
		String serialPart = bindingConfig;
		String pattern = null;
		boolean base64 = false;

		if(indexOf != -1) {
			String substring = bindingConfig.substring(indexOf+1);
			serialPart = bindingConfig.substring(0, indexOf);

			if(substring.startsWith("REGEX(")) {
				pattern = substring.substring(6, substring.length()-1);
			}

			if(substring.equals("BASE64")) {
				base64 = true;
			}
		}

		String portConfig[] = serialPart.split("@");

		String port = portConfig[0];
		int baudRate = 0;

		if(portConfig.length > 1)
			baudRate = Integer.parseInt(portConfig[1]);

		SerialDevice serialDevice = serialDevices.get(port);
		if (serialDevice == null) {
			if(baudRate > 0)
				serialDevice = new SerialDevice(port, baudRate);
			else
				serialDevice = new SerialDevice(port);

			serialDevice.setTransformationService(transformationService);
			serialDevice.setEventPublisher(eventPublisher);
			try {
				serialDevice.initialize();
			} catch (InitializationException e) {
				throw new BindingConfigParseException(
						"Could not open serial port " + port + ": "
								+ e.getMessage());
			} catch (Throwable e) {
				throw new BindingConfigParseException(
						"Could not open serial port " + port + ": "
								+ e.getMessage());
			}
			itemMap.put(item.getName(), port);
			serialDevices.put(port, serialDevice);
		}

		serialDevice.addConfig(item.getName(), item.getClass(), pattern, base64);
		
		Set<String> itemNames = contextMap.get(context);
		if (itemNames == null) {
			itemNames = new HashSet<String>();
			contextMap.put(context, itemNames);
		}
		itemNames.add(item.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConfigurations(String context) {
		Set<String> itemNames = contextMap.get(context);
		if(itemNames!=null) {
			for(String itemName : itemNames) {
				// we remove all information in the serial devices
				SerialDevice serialDevice = serialDevices.get(itemMap.get(itemName));
				itemMap.remove(itemName);
				if(serialDevice==null) {
					continue;
				}

				serialDevice.removeConfig(itemName);
				
				// if there is no binding left, dispose this device
				if(serialDevice.isEmpty()) {
					serialDevice.close();
					serialDevices.remove(serialDevice.getPort());
				}
			}
			contextMap.remove(context);
		}
	}

}

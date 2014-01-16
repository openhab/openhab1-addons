/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bluetooth.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.bluetooth.BluetoothEventHandler;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

/**
 * <p>This class is the default implementation to link the bluetooth discovery service
 * to the openHAB event bus by parsing the binding configurations provided by the {@link GenericItemProvider}.</p>
 * 
 * <p>The format of the binding configuration is simple and looks like this:
 * <ul>
 * <li>for switch items: bluetooth="&lt;deviceAddress&gt;[!]" where &lt;deviceAddress&gt; is the technical address of the device, eg. EC935BD417C5
 * and the optional exclamation mark defines whether the devices needs to be paired/authenticated with the host or not</li> 
 * <li>for string items: bluetooth="[*|!|?]", where '!' only regards authenticated devices, '?' only regards un-authenticated devices and
 * '*' accepts any device.</li>
 * <li>for number items: bluetooth="[*|!|?]", where '!' only regards authenticated devices, '?' only regards un-authenticated devices and
 * '*' accepts any device.</li>
 * </p>
 * <p>Switch items will receive an ON / OFF update on the bus, String items will be sent a comma separated list of all device names and
 * Number items will show the number of bluetooth devices in range.
 * If a friendly name cannot be resolved for a device, its address will be used instead as its name when listing it on a String item.</p>
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class BluetoothBinding implements BluetoothEventHandler, BindingConfigReader {

	private static final String BLUETOOTH_BINDING_TYPE = "bluetooth";

	/** stores information about switch items. The map has this content structure: context -> { deviceAddress, itemName } */ 
	private Map<String, Map<String, String>> switchItems = new HashMap<String, Map<String, String>>();
	
	/** stores information about string items for authenticated devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> authStringItems = new HashMap<String, String>();

	/** stores information about string items for un-authenticated devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> unauthStringItems = new HashMap<String, String>();

	/** stores information about string items for all devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> allStringItems = new HashMap<String, String>();

	/** stores information about measurement items for authenticated devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> authMeasurementItems = new HashMap<String, String>();

	/** stores information about measurement items for un-authenticated devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> unauthMeasurementItems = new HashMap<String, String>();

	/** stores information about measurement items for all devices. The map has this content structure: context -> itemName */ 
	private Map<String, String> allMeasurementItems = new HashMap<String, String>();

	private EventPublisher eventPublisher;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void handleDeviceInRange(BluetoothDevice device) {
		if(eventPublisher!=null) {
			// find the items associated to this address, if any
			String itemName = null;
			for(Map<String, String> map : switchItems.values()) {
				itemName = map.get(device.getAddress());
				if(itemName==null && device.isPaired()) {
					itemName = map.get(device.getAddress() + "!");
				}
				if(itemName!=null) break;
			}
			if(itemName!=null) {
				eventPublisher.postUpdate(itemName, OnOffType.ON);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDeviceOutOfRange(BluetoothDevice device) {
		if(eventPublisher!=null) {
			// find the item associated to this address, if any
			String itemName = null;
			for(Map<String, String> map : switchItems.values()) {
				itemName = map.get(device.getAddress());
				if(itemName==null && device.isPaired()) {
					itemName = map.get(device.getAddress() + "!");
				}
				if(itemName!=null) break;
			}
			if(itemName!=null) {
				eventPublisher.postUpdate(itemName, OnOffType.OFF);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleAllDevicesInRange(Iterable<BluetoothDevice> devices) {
		if(eventPublisher!=null) {
			// build a comma separated list of all devices in range
			StringBuilder authSb = new StringBuilder();
			StringBuilder unauthSb = new StringBuilder();
			int noOfAuthDevices = 0;
			int noOfUnauthDevices = 0;
			for(BluetoothDevice device : devices) {
				handleDeviceInRange(device);
				if(device.isPaired()) {
					authSb.append(getName(device));
					authSb.append(", ");
					noOfAuthDevices++;
				} else {
					unauthSb.append(getName(device));
					unauthSb.append(", ");
					noOfUnauthDevices++;
				}
			}
			String authDeviceList = authSb.length() > 0 ? authSb.substring(0, authSb.length()-2) : "";
			String unauthDeviceList = unauthSb.length() > 0 ? unauthSb.substring(0, unauthSb.length()-2) : "";
			String allDeviceList = unauthDeviceList.isEmpty() ? authDeviceList : authSb.append(unauthDeviceList).toString();
			
			for(String itemName : authStringItems.values()) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(authDeviceList));
			}
			for(String itemName : unauthStringItems.values()) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(unauthDeviceList));
			}
			for(String itemName : allStringItems.values()) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(allDeviceList));
			}
			for(String itemName : authMeasurementItems.values()) {
				eventPublisher.postUpdate(itemName, new DecimalType(noOfAuthDevices));
			}
			for(String itemName : unauthMeasurementItems.values()) {
				eventPublisher.postUpdate(itemName, new DecimalType(noOfUnauthDevices));
			}
			for(String itemName : allMeasurementItems.values()) {
				eventPublisher.postUpdate(itemName, new DecimalType(noOfAuthDevices + noOfUnauthDevices));
			}
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return BLUETOOTH_BINDING_TYPE;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, String- and NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		if (item instanceof SwitchItem) {
			Map<String, String> entry = switchItems.get(context);
			if (entry == null) {
				entry = new HashMap<String, String>();
			}
			entry.put(bindingConfig, item.getName());
			switchItems.put(context, entry);
		} else if (item instanceof StringItem) {
			if (bindingConfig.equals("!")) {
				authStringItems.put(context, item.getName());
			} else if (bindingConfig.equals("?")) {
				unauthStringItems.put(context, item.getName());
			} else if (bindingConfig.equals("*")) {
				allStringItems.put(context, item.getName());
			}
		} else if (item instanceof NumberItem) {
			if (bindingConfig.equals("!")) {
				authMeasurementItems.put(context, item.getName());
			} else if (bindingConfig.equals("?")) {
				unauthMeasurementItems.put(context, item.getName());
			} else if (bindingConfig.equals("*")) {
				allMeasurementItems.put(context, item.getName());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isActive() {
		// only say that we are active if there are any items registered for that binding
		return switchItems.size() > 0 
			|| authStringItems.size() > 0 || unauthStringItems.size() > 0 || allStringItems.size() > 0 
			|| authMeasurementItems.size() > 0 || unauthMeasurementItems.size() > 0 || allMeasurementItems.size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConfigurations(String context) {
		switchItems.remove(context);
		authStringItems.remove(context);
		unauthStringItems.remove(context);
		allStringItems.remove(context);
		authMeasurementItems.remove(context);
		unauthMeasurementItems.remove(context);
		allMeasurementItems.remove(context);
	}

	private String getName(BluetoothDevice device) {
		if(!device.getFriendlyName().trim().isEmpty()) {
			return device.getFriendlyName();
		} else {
			return device.getAddress();
		}
	}

}

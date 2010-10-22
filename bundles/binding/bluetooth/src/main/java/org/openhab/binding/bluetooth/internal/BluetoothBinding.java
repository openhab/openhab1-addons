/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * <li>for switch items: bluetooth={<deviceAddress>} where &lt;deviceAddress&gt; is the technical address of the device, eg. EC935BD417C5</li> 
 * <li>for string items: bluetooth={*}</li>
 * </p>
 * <p>Switch items will receive an ON / OFF update on the bus, String items will be sent a comma separated list of all device names.
 * If a friendly name cannot be resolved for a device, its address will be used instead as its name.</p>
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class BluetoothBinding implements BluetoothEventHandler, BindingConfigReader {

	private static final String BLUETOOTH_BINDING_TYPE = "bluetooth";

	/** stores information about switch items. The map has this content structure: context -> { deviceAddress, itemName } */ 
	private Map<String, Map<String, String>> switchItems = new HashMap<String, Map<String, String>>();
	
	/** stores information about string items. The map has this content structure: context -> itemName */ 
	private Map<String, String> stringItems = new HashMap<String, String>();
	
	/** stores information about measurement items. The map has this content structure: context -> itemName */ 
	private Map<String, String> measurementItems = new HashMap<String, String>();

	private EventPublisher eventPublisher;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	
	@Override
	public void handleDeviceInRange(BluetoothDevice device) {
		if(eventPublisher!=null) {
			// find the item associated to this address, if any
			String itemName = null;
			for(Map<String, String> map : switchItems.values()) {
				itemName = map.get(device.getAddress());
				if(itemName!=null) break;
			}
			if(itemName!=null) {
				eventPublisher.postUpdate(itemName, OnOffType.ON);
			}
		}
	}

	@Override
	public void handleDeviceOutOfRange(BluetoothDevice device) {
		if(eventPublisher!=null) {
			// find the item associated to this address, if any
			String itemName = null;
			for(Map<String, String> map : switchItems.values()) {
				itemName = map.get(device.getAddress());
				if(itemName!=null) break;
			}
			if(itemName!=null) {
				eventPublisher.postUpdate(itemName, OnOffType.OFF);
			}
		}
	}

	@Override
	public void handleAllDevicesInRange(Iterable<BluetoothDevice> devices) {
		if(eventPublisher!=null) {
			// build a comma separated list of all devices in range
			StringBuilder sb = new StringBuilder();
			int noOfDevices = 0;
			for(BluetoothDevice device : devices) {
				noOfDevices++;
				if(!device.getFriendlyName().trim().isEmpty()) {
					sb.append(device.getFriendlyName());
				} else {
					sb.append(device.getAddress());
				}
				if(device.isPaired()) {
					sb.append(" !");
				}
				sb.append(", ");
			}
			String deviceList = sb.length() > 0 ? sb.substring(0, sb.length()-2) : "-";
			for(String itemName : stringItems.values()) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(deviceList));
			}
			for(String itemName : measurementItems.values()) {
				eventPublisher.postUpdate(itemName, new DecimalType(noOfDevices));
			}
			
		}
	}

	@Override
	public String getBindingType() {
		return BLUETOOTH_BINDING_TYPE;
	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		if(item instanceof SwitchItem) {
			Map<String, String> entry = switchItems.get(context);
			if(entry==null) {
				entry = new HashMap<String, String>();
			}
			entry.put(bindingConfig, item.getName());
			switchItems.put(context, entry);
		}
		
		if(item instanceof StringItem && bindingConfig.equals("*")) {
			stringItems.put(context, item.getName());
		}
		if(item instanceof NumberItem && bindingConfig.equals("*")) {
			measurementItems.put(context, item.getName());
		}
	}

	@Override
	public boolean isActive() {
		// only say that we are active if there are any items registered for that binding
		return switchItems.size() > 0 || stringItems.size() > 0 || measurementItems.size() > 0;
	}

	@Override
	public void removeConfigurations(String context) {
		switchItems.remove(context);
		stringItems.remove(context);
		measurementItems.remove(context);
	}

}

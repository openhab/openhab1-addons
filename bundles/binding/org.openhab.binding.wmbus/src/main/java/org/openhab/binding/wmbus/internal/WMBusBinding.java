/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

/**
 * <p>This class implements a binding of Wireless MBus devices through serial connections to openHAB.
 * The binding configurations are provided by the {@link GenericItemProvider}.</p>
 * 
 * <p>The format of the binding configuration is simple and looks like this:</p>
 * wmbus="&lt;port&gt;, &lt;desiredData&gt;" where &lt;port&gt; is the identification of the serial port on the host system, e.g.
 * "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac and &lt;desiredData&gt; is a value specifying the desired
 * data to be shown in the particular StringItem identified by the devices name. Possible values are "TotalVolume" and "VolumeFlow".<br>
 * String items will receive the submitted data in form of a string value as a status update, while openHAB commands to a Switch item is
 * sent out as data through the serial interface.</p>
 * 
 * @author Kai Kreuzer, Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class WMBusBinding extends AbstractEventSubscriber implements BindingConfigReader {

	private Map<String, WMBusDevice> wmbusDevices = new HashMap<String, WMBusDevice>();

	/** stores information about the which items are associated to which port. The map has this content structure: itemname -> port */ 
	private Map<String, String> itemMap = new HashMap<String, String>();
	
	/** stores information about the context of items. The map has this content structure: context -> Set of itemNames */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	private EventPublisher eventPublisher = null;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		
		for(WMBusDevice wmbusdevice : wmbusDevices.values()) {
			wmbusdevice.setEventPublisher(eventPublisher);
		}
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;

		for(WMBusDevice wmbusdevice : wmbusDevices.values()) {
			wmbusdevice.setEventPublisher(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void receiveCommand(String itemName, Command command) {
		if(itemMap.keySet().contains(itemName)) {
			WMBusDevice wmbusdevice = wmbusDevices.get(itemMap.get(itemName));
			if(command instanceof StringType) {
				wmbusdevice.writeString(command.toString());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveUpdate(String itemName, State newStatus) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "wmbus";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		//TODO: Add Serial Number of devices to listen to (item-specific)
		String port = "";
		for(int i=0; i<bindingConfig.length(); i++)
		{
			if(bindingConfig.charAt(i)==','){
				port = bindingConfig.substring(0, i);
				bindingConfig = bindingConfig.substring(i+2);
				break;
			}
		}
		String desiredData = bindingConfig;
		WMBusDevice wmbusdevice = wmbusDevices.get(port);
		if (wmbusdevice == null) {
			wmbusdevice = new WMBusDevice(port, desiredData);
			wmbusdevice.setEventPublisher(eventPublisher);
			try {
				wmbusdevice.initialize();
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
			wmbusDevices.put(port, wmbusdevice);
		}
		if (item instanceof StringItem) {
			if (wmbusdevice.getStringItemName() == null) {
				wmbusdevice.setStringItemName(item.getName());
			} else {
				throw new BindingConfigParseException(
						"There is already another StringItem assigned to serial port "
								+ port);
			}
		}
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
				// we remove all information in the wmbus devices
				WMBusDevice wmbusdevice = wmbusDevices.get(itemMap.get(itemName));
				itemMap.remove(itemName);
				if(wmbusdevice==null) {
					continue;
				}
				if(itemName.equals(wmbusdevice.getStringItemName())) {
					wmbusdevice.setStringItemName(null);
				}
				// if there is no binding left, dispose this device
				if(wmbusdevice.getStringItemName()==null) {
					wmbusdevice.close();
					wmbusDevices.remove(wmbusdevice.getPort());
				}
			}
			contextMap.remove(context);
		}
	}

}
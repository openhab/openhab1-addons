/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ipx800.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.ipx800.Ipx800BindingProvider;
import org.openhab.binding.ipx800.internal.Ipx800Config.Ipx800DeviceConfig;
import org.openhab.binding.ipx800.internal.Ipx800GenericBindingProvider.Ipx800BindingConfig;
import org.openhab.binding.ipx800.internal.command.Ipx800Port;
import org.openhab.binding.ipx800.internal.command.Ipx800PortType;
import org.openhab.binding.ipx800.internal.exception.Ipx800UnknownDeviceException;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Consumption;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800ConsumptionPeriod;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800AstableSwitch;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Counter;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Dimmer;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800DoubleClic;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Mirror;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800OutputItem;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800SimpleClic;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ipx800 binding. Handle the creation of items object linked to the devices.
 * 
 * @author Seebag
 * @since 1.8.0
 */
public class Ipx800Binding extends AbstractBinding<Ipx800BindingProvider>
		implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(Ipx800Binding.class);
	/**
	 * Store the all ipx800 devices DeviceName, Ipx800DeviceConnector
	 */
	private Map<String, Ipx800DeviceConnector> devices = new HashMap<String, Ipx800DeviceConnector>();

	public Ipx800Binding() {
	}

	@Override
	public void bindingChanged(BindingProvider prov, String itemName) {
		if (prov.getItemNames().contains(itemName)) {
			try {
				createInternalItem(itemName, prov);
			} catch (Ipx800UnknownDeviceException e) {
				logger.error("Item {} will be ignored", itemName);
			}
		} else {
			logger.debug("Removing item {}", itemName);
			for (String deviceName : devices.keySet()) {
				for (Ipx800Port slot : devices.get(deviceName).getAllPorts()) {
					slot.destroyItem(itemName);
				}
			}
		}
	}
	/**
	 * Reload and reattach all items to all devices
	 */
	public void reloadItems() {
		logger.trace("Size of providers : {}", providers.size());
		for (Ipx800BindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				try {
					createInternalItem(itemName, provider);
				} catch (Ipx800UnknownDeviceException e) {
					logger.error("Item {} will be ignored", itemName);
				}
			}
		}
	}
	
	private Ipx800DeviceAndPort getDeviceAndPort(String name, String portString) throws Ipx800UnknownDeviceException {
		if (name.isEmpty() || portString.isEmpty()) {
			return new Ipx800DeviceAndPort(null, null);
		}
		int extDelta = 0;
		Ipx800DeviceConnector device = devices.get(name);
		
		if (device == null) {
			// Try to find extensions
			for(Ipx800DeviceConnector dev : devices.values()) {
				extDelta = dev.getExtensionDelta(name);
				if (extDelta != 0) {
					device = dev;
					break;
				}
			}
			if (extDelta == 0) {
				throw new Ipx800UnknownDeviceException("Device '" + name + "' doesn't exist, please check your config/items");
			}
		}
		return new Ipx800DeviceAndPort(device, device.getPort(portString, extDelta));
	}
	
	/**
	 * Create and attach new item to device
	 * @param itemName 
	 * @param provider
	 * @throws Ipx800UnknownDeviceException 
	 */
	private void createInternalItem(String itemName, BindingProvider prov) throws Ipx800UnknownDeviceException {
		if (prov instanceof Ipx800GenericBindingProvider) {
			Ipx800GenericBindingProvider provider = (Ipx800GenericBindingProvider) prov;
			Ipx800BindingConfig config = provider.getBindingConfig(itemName);
			if (config != null) {
				Ipx800DeviceAndPort devPort = getDeviceAndPort(config.getDeviceName(), config.getPortField());
				Ipx800DeviceConnector device = devPort.getDevice();
				Ipx800Port port = devPort.getPort();
				
				
				
				Ipx800Item item;
				if (port.getCommandType() == Ipx800PortType.OUPUT) {
					boolean pulse = false;
					if (config.getExtra(0).equals("p")) {
						pulse = true;
					}
					item = new Ipx800OutputItem(pulse);
				} else if (port.getCommandType() == Ipx800PortType.COUNTER) {
					if (config.getExtra(0).equals("a")) {
						float unit = 1;
						if (!config.getExtra(1).equals("")) {
							unit =  Float.parseFloat(config.getExtra(1));
						}
						Ipx800ConsumptionPeriod period = Ipx800ConsumptionPeriod.getPeriod(config.getExtra(2));
						item = new Ipx800Consumption(unit, period);
					} else {
						item = new Ipx800Counter();
					}
				} else {
					if (config.getExtra(0).equals("D")) {
						item = new Ipx800DoubleClic();
						port.switchToMultiHandler();
					} else if (config.getExtra(0).equals("d")) {
						item = new Ipx800SimpleClic();
						port.switchToMultiHandler();
					} else if (config.getExtra(0).equals("v")) {
						if (config.getExtra(1).equals("")) {
							item = new Ipx800Dimmer();
						} else {
							item = new Ipx800Dimmer(new Integer(config.getExtra(1)));
						}
						port.switchToMultiHandler();
					} else if (config.getExtra(0).equals("m")) {
						item = new Ipx800Mirror();
					} else {
						item = new Ipx800AstableSwitch();
					}
				}
				item.setBinding(this);
				item.setItemName(itemName);
				
				// If no to action set : null is returned
				String toDeviceName = config.getToDeviceName();
				if(toDeviceName.isEmpty()) {
					toDeviceName = config.getDeviceName();
				}
				Ipx800DeviceAndPort devToPort = getDeviceAndPort(toDeviceName, config.getToPortField());
				Ipx800DeviceConnector toDevice = devToPort.getDevice();
				Ipx800Port toPort = devToPort.getPort();
				Ipx800OutputItem toItem = null;
				if (toDevice != null && toPort != null) {
					toItem = new Ipx800OutputItem(config.isToPulse());
					toItem.setItemName(itemName + "-out");
					toItem.setBinding(this);
					toItem.setFromItem(item);
					item.setToItem(toItem);
					toPort.attachItem(itemName + "-out", toItem);
					
				}
				
				port.attachItem(itemName, item);
				if(toItem != null) {
					logger.info("Item {} created using {}, attached to {} on port {} to item {}", itemName, item, device, port, toItem);
				} else {
					logger.info("Item {} created using {}, attached to {} on port {}", itemName, item, device, port);
				}
			}
		}
	}

	public void activate() {
		logger.debug("Activate called");
	}

	public void deactivate() {
		for (Ipx800DeviceConnector device : devices.values()) {
			device.destroyAndExit();
		}
		logger.debug("DeActivate called");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("Received command item='{}', command='{}'", itemName, command.toString());
		for (Ipx800DeviceConnector device : devices.values()) {
			for (Ipx800Port slot : device.getAllPorts()) {
				Ipx800Item itemSlot = slot.getItemSlot(itemName);
				if (itemSlot != null) {
					itemSlot.setState((State)command);
					if (itemSlot instanceof Ipx800OutputItem) {
						device.setOutput((Ipx800OutputItem)itemSlot);
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			// Unload items is not necessary as devices list is erased
			quitThreads();
			// read new config
			Ipx800Config ipx800Config = Ipx800Config.readConfig(config);
			logger.info("Ipx800 configuration read with "
					+ ipx800Config.getDevices().size() + " device(s) : ");

			// Start thread
			startThreads();
			// Reload items configuration
			reloadItems();
		}
	}
	
	/**
	 * Exit all the device threads
	 */
	private void quitThreads() {
		logger.info("Exiting IPX800 threads");
		for (Ipx800DeviceConnector device : devices.values()) {
			device.interrupt();
		}
		devices.clear();
	}
	
	/**
	 * Start devices threads
	 */
	private void startThreads() {
		for (Ipx800DeviceConfig config : Ipx800Config.INSTANCE.getDevices()
				.values()) {
			Ipx800DeviceConnector thread = new Ipx800DeviceConnector(config);
			thread.start();
			devices.put(config.name, thread);
		}
	}
	
	/**
	 * Post the state of an item to the event bus
	 * @param item
	 */
	public void postUpdate(Ipx800Item item) {
		if (item.getPort().getCommandType() == Ipx800PortType.OUPUT) {
			eventPublisher.postUpdate(item.getItemName(), item.getState());
		} else {
			eventPublisher.postCommand(item.getItemName(), (Command)item.getState());
		}
	}
}

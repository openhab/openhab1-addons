/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ucprelayboard.UCPRelayBoardBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;

/**
 * Binding for relay boards available from http://www.ucprojects.eu/
 * 
 * @author Robert Michalak
 * @since 1.8.0
 */
public class UCPRelayBoardBinding extends
		AbstractActiveBinding<UCPRelayBoardBindingProvider> {

	private Map<String, SerialDevice> serialDevices = new HashMap<String, SerialDevice>();
	
	private long refreshInterval = 60000;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {

		UCPRelayConfig config = getRelayConfigForItem(itemName);
		if (config != null) {
			SerialDevice serialDevice = serialDevices.get(config.getBoardName());
			if (command instanceof OnOffType) {
				byte[] relayBoardCommand = prepareRelayCommand(
						config, ((OnOffType) command));
				serialDevice.writeBytes(relayBoardCommand);
			}
		}
	}

	private UCPRelayConfig getRelayConfigForItem(String itemName) {
		for (UCPRelayBoardBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				return provider.getRelayConfigForItem(itemName);
			}
		}
		return null;
	}

	private byte[] prepareRelayCommand(UCPRelayConfig config, OnOffType onOffType) {
		
		switch (config.translateCommand(onOffType)) {
		case OFF:
			return UCPRelayBoardMessages.OFF[config.getRelayNumber()];
		case ON:
			return UCPRelayBoardMessages.ON[config.getRelayNumber()];
		}
		return null;
	}

	public void activate(final BundleContext bundleContext,
			final Map<String, Object> properties) throws InitializationException {
		if (properties == null) {
			return;
		}
		
		Map<String, SerialDeviceConfig> configs = prepareConfiguration(properties);
		for (SerialDeviceConfig config : configs.values()) {
			prepareSerialDevice(config);
		}
		if (serialDevices.size() > 0) {
			setProperlyConfigured(true);
		}
	}
	
	public void modified(final Map<String, Object> properties) throws InitializationException {
		if (properties == null) {
			return;
		}
		Map<String, SerialDeviceConfig> configs = prepareConfiguration(properties);
		//create new and modify existing ones
		for (Map.Entry<String, SerialDeviceConfig> config : configs.entrySet()) {
			if (serialDevices.containsKey(config.getKey())) {
				SerialDevice device = serialDevices.get(config.getKey());
				if (!config.equals(device.getConfig())) {
					device.close();
					prepareSerialDevice(config.getValue());
				}
			} else {
				prepareSerialDevice(config.getValue());
			}
		}
		//close removed ones
		for (String name : serialDevices.keySet()) {
			if (!configs.containsKey(name)) {
				SerialDevice device = serialDevices.get(name);
				device.close();
				serialDevices.remove(name);
			}
		}
		if (serialDevices.size() > 0) {
			setProperlyConfigured(true);
		} else {
			setProperlyConfigured(false);
		}

	}

	private void prepareSerialDevice(SerialDeviceConfig config)
			throws InitializationException {
		SerialDevice serialDevice = new SerialDevice(config);
		serialDevice.initialize();
		serialDevices.put(config.getName(), serialDevice);
	}

	private Map<String, SerialDeviceConfig> prepareConfiguration(
			final Map<String, Object> properties) {
		Map<String, SerialDeviceConfig> configs = new HashMap<String, SerialDeviceConfig>();

		for (String key : properties.keySet()) {

			Object property = properties.get(key);
			if (!(property instanceof String)) {
				continue;
			}
			
			String value = StringUtils.trimToNull((String) property);

			if ("refresh".equals(key)) {
				try {
					refreshInterval = Long.parseLong(value);
				} catch (NumberFormatException ignored) {
				}
				continue;
			}
			
			String[] split = StringUtils.split(key, ".");
			if (split.length != 3 || value == null || !"board".equals(split[0])) {
				continue;
			}

			SerialDeviceConfig config = configs.get(split[1]);
			if (config == null) {
				config = new SerialDeviceConfig();
				config.setName(split[1]);
				configs.put(split[1], config);
			}

			if ("port".equals(split[2])) {
				config.setPort(value);
			} else if ("baud".equals(split[2])) {
				config.setBaud(Integer.valueOf(value));
			}
		}
		return configs;
	}

	public void deactivate(final int reason) {
		for (SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.close();
		}
		serialDevices.clear();
	}

	private void retrieveStateFromRelayBoard(SerialDevice serialDevice) {
		//consume any bytes left in a stream
		serialDevice.readBytes(new byte[100]);
		serialDevice.writeBytes(UCPRelayBoardMessages.GET_STATE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}
		readAndDecodeResponse(serialDevice);
	}

	private void readAndDecodeResponse(SerialDevice serialDevice) {
		byte response[] = serialDevice.readBytes(new byte[5]);
		if (validateResponse(response)) {
			for (int relay = 0; relay < 8; relay++) {
				Item item = getItemForRelay(serialDevice, relay);
				if (item != null) {
					UCPRelayConfig config = getRelayConfigForItem(item.getName());
					State state = getRelayState(response, relay, config);
					eventPublisher.postUpdate(item.getName(), state);
				}
			}
		}
	}

	private boolean validateResponse(byte[] response) {
		if (response[0] != 85) {
			return false;
		}
		if (!checkCRC(response)) {
			return false;
		}
		return true;
	}

	private boolean checkCRC(byte[] response) {
		final byte[] tab = Arrays.copyOfRange(response, 1, 4);
		final byte crc = DowCRC.compute(tab);
		if (crc != response[4]) {
			return false;
		} else {
			return true;
		}
	}

	private OnOffType getRelayState(byte[] response, int relay, UCPRelayConfig config) {
		OnOffType state;
		if ((response[3] & (1 << relay)) > 0) {
			state = OnOffType.ON;
		} else {
			state = OnOffType.OFF;
		}
		return config.translateCommand(state);
	}

	private Item getItemForRelay(SerialDevice serialDevice, int relay) {
		for (UCPRelayBoardBindingProvider provider : providers) {
			Item item = provider.getItemForRelayConfig(new UCPRelayConfig(serialDevice.getConfig().getName(), relay));
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	@Override
	protected void execute() {
		for (SerialDevice serialDevice : serialDevices.values()) {
			retrieveStateFromRelayBoard(serialDevice);
		}
		
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "UCProjects.eu Relay Board Binding Thread";
	}
}

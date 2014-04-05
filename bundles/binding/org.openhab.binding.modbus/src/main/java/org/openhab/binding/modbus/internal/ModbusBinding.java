/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.util.BitVector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.ModbusGenericBindingProvider.ModbusBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Modbus binding allows to connect to multiple Modbus slaves as TCP master.
 * This implementation works with coils (boolean values) only.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusBinding extends AbstractActiveBinding<ModbusBindingProvider> implements ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(ModbusBinding.class);

	private static final String TCP_PREFIX = "tcp";
	private static final String SERIAL_PREFIX = "serial";

	private static final Pattern EXTRACT_MODBUS_CONFIG_PATTERN =
		Pattern.compile("^("+TCP_PREFIX+"|"+SERIAL_PREFIX+"|)\\.(.*?)\\.(connection|id|pollInterval|start|length|type)$");

	/** Stores instances of all the slaves defined in cfg file */
	private static Map<String, ModbusSlave> modbusSlaves = new ConcurrentHashMap<String, ModbusSlave>();

	/** slaves update interval in milliseconds, defaults to 200ms */
	public static int pollInterval = 200;

	
	public void activate() {
	}

	public void deactivate() {
	}
	
	
	@Override
	protected long getRefreshInterval() {
		return 	pollInterval;
	}

	@Override
	protected String getName() {
		return "Modbus Polling Service";
	}
	

	/**
	 * Parses configuration creating Modbus slave instances defined in cfg file
	 * {@inheritDoc}
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				ModbusSlave slave = modbusSlaves.get(config.slaveName);
				slave.executeCommand(command, config.readRegister, config.writeRegister);
			}
		}
	}

	/**
	 * Posts update event to OpenHAB bus for "holding" type slaves
	 * @param binding ModbusBinding to get item configuration from BindingProviding
	 * @param registers data received from slave device in the last pollInterval
	 * @param itemName item to update
	 */
	protected void internalUpdateItem(String slaveName, InputRegister[] registers,
			String itemName) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				if (config.slaveName.equals(slaveName)) {
					InputRegister value = registers[config.readRegister];
					if (config.getItem() instanceof SwitchItem) {
						if (value.getValue() == 0 && (provider.getConfig(itemName).getItemState() != OnOffType.OFF)) {
							eventPublisher.postUpdate(itemName, OnOffType.OFF);
						} else if (value.getValue() != 0 && (provider.getConfig(itemName).getItemState() != OnOffType.ON)) {
							eventPublisher.postUpdate(itemName, OnOffType.ON);							
						}
					} else {
					DecimalType newState = new DecimalType(value.getValue());
					if (!newState.equals(provider.getConfig(itemName).getItemState()))
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}

	/**
	 * Posts update event to OpenHAB bus for "coil" type slaves
	 * @param binding ModbusBinding to get item configuration from BindingProviding
	 * @param registers data received from slave device in the last pollInterval
	 * @param item item to update
	 */
	protected void internalUpdateItem(String slaveName, BitVector coils,
			String itemName) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				if (config.slaveName.equals(slaveName)) {
					boolean state = coils.getBit(config.readRegister);
					State currentState = provider.getConfig(itemName).getItemState();
					State newState = provider.getConfig(itemName).translateBoolean2State(state);
					if (!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}
	

	/**
	 * Returns names of all the items, registered with this binding
	 * @return list of item names
	 */
	public Collection<String> getItemNames() {
		Collection<String> items = null;
		for (BindingProvider provider : providers) {
			if (items == null)
				items = provider.getItemNames();
			else
				items.addAll(provider.getItemNames());
		}
		return items;
	}
	
	
	/**
	 * updates all slaves from the modbusSlaves
	 */
	@Override
	protected void execute() {
		Collection<ModbusSlave> slaves = new HashSet<ModbusSlave>();
		synchronized (slaves) {
			slaves.addAll(modbusSlaves.values());
		}
		for (ModbusSlave slave : slaves) {
			slave.update(this);
		}
	}
	

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		// remove all known items if configuration changed
		modbusSlaves.clear();

		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_MODBUS_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					if ("poll".equals(key)) {
						if (StringUtils.isNotBlank((String) config.get(key))) {
							pollInterval = Integer.valueOf((String) config.get(key));
						}
					} else if ("writemultipleregisters".equals(key)) {
						ModbusSlave.setWriteMultipleRegisters(Boolean.valueOf(config.get(key).toString()));
					} else {
						logger.debug("given modbus-slave-config-key '" + key
							+ "' does not follow the expected pattern 'pollInterval' or '<slaveId>.<connection|id|start|length|type>'");
					}
					continue;
				}

				matcher.reset();
				matcher.find();

				String slave = matcher.group(2);

				ModbusSlave modbusSlave = modbusSlaves.get(slave);
				if (modbusSlave == null) {
					if (matcher.group(1).equals(TCP_PREFIX)) {
						modbusSlave = new ModbusTcpSlave(slave);
					} else if (matcher.group(1).equals(SERIAL_PREFIX)) {
						modbusSlave = new ModbusSerialSlave(slave);
					} else {
						throw new ConfigurationException(slave, "the given slave type '" + slave + "' is unknown");
					}
					modbusSlaves.put(slave,modbusSlave);
				}

				String configKey = matcher.group(3);
				String value = (String) config.get(key);
				
				if ("connection".equals(configKey)) {
					String[] chunks = value.split(":");
					if (modbusSlave instanceof ModbusTcpSlave) {
						((ModbusTcpSlave) modbusSlave).setHost(chunks[0]);
						if (chunks.length == 2) {
							((ModbusTcpSlave) modbusSlave).setPort(Integer.valueOf(chunks[1]));
						}
					} else if (modbusSlave instanceof ModbusSerialSlave) {
						((ModbusSerialSlave) modbusSlave).setPort(chunks[0]);
						if (chunks.length == 2) {
							((ModbusSerialSlave) modbusSlave).setBaud(Integer.valueOf(chunks[1]));
						}
					}
				} else if ("start".equals(configKey)) {
					modbusSlave.setStart(Integer.valueOf(value));
				} else if ("length".equals(configKey)) {
					modbusSlave.setLength(Integer.valueOf(value));
				} else if ("id".equals(configKey)) {
					modbusSlave.setId(Integer.valueOf(value));
				} else if ("type".equals(configKey)) {
					if (ArrayUtils.contains(ModbusBindingProvider.SLAVE_DATA_TYPES, value)) {
						modbusSlave.setType(value);
					} else {
						throw new ConfigurationException(configKey, "the given slave type '" + value + "' is invalid");
					}
				} else {
					throw new ConfigurationException(configKey,
						"the given configKey '" + configKey + "' is unknown");
				}
			}

			// connect instances to modbus slaves
			for (ModbusSlave slave : modbusSlaves.values()) {
				slave.connect();
			}

			setProperlyConfigured(true);
		}
	}
	
}

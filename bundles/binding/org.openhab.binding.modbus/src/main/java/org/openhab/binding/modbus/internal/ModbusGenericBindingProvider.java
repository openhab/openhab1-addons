/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModbusBindingProvider provides binding for Openhab Items
 * There are two ways to bind an item to modbus coils/registers
 * 
 *  1) single coil/register per item
 *  Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:5"}
 *  
 *  This binds MySwitch to modbus slave defined as "slave1" in openhab.config reading/writing to the coil 5
 * 
 *  2) separate coils/registers for reading and writing
 *  Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:<6:>7"}
 *  
 *  In this case coil 6 is used as status coil (readonly) and commands are put to coil 7 by setting coil 7 to true. 
 *  You hardware should then set coil 7 back to false to allow further commands processing. 
 *  
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusGenericBindingProvider extends AbstractGenericBindingProvider implements ModbusBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(ModbusGenericBindingProvider.class);
	static final String BINDING_TYPE = "modbus";

	/**
	 * {@inheritDoc}
	 */
	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	public String getBindingType() {
		return BINDING_TYPE;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (item.getClass() == SwitchItem.class)
			return;
		if (item.getClass() == ContactItem.class)
			return;
		if (item.getClass() == NumberItem.class)
			return;

		throw new BindingConfigParseException("item '" + item.getName()
				+ "' is of type '" + item.getClass().getSimpleName()
				+ "', only Switch, Contact or Number are allowed - please check your *.items configuration");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			ModbusBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected ModbusBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		return new ModbusBindingConfig(item, bindingConfig);
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.modbus.tcp.master.ModbusBindingProvider#getConfig(java.lang.String)
	 */
	@Override
	public ModbusBindingConfig getConfig(String name) {
		return (ModbusBindingConfig) bindingConfigs.get(name);
	}

	/**
	 * ModbusBindingConfig stores configuration of the item bound to Modbus
	 * 
	 * @author dbkrasn
	 * @since 1.1.0
	 */
	public class ModbusBindingConfig implements BindingConfig {

		/**
		 * readRegister and writeRegister store references to the register in device data space
		 */
		int readRegister;
		int writeRegister;
		
		/**
		 * Name of the ModbusSlave instance to read/write data
		 */
		String slaveName;
		/**
		 * OpenHAB Item to be configured 
		 */
		private Item item = null;
		
		public Item getItem() {
			return item;
		}
		
		State getItemState() {
			return item.getState();
		}
	
		/**
		 * Calculates new item state based on the new boolean value, current item state and item class
		 * Used with item bound to "coil" type slaves
		 * 
		 * @param b new boolean value
		 * @param c class of the current item state
		 * @param itemClass class of the item
		 * 
		 * @return new item state
		 */
		protected State translateBoolean2State(boolean b) {
			
			Class<? extends State> c = item.getState().getClass();
			Class<? extends Item> itemClass = item.getClass();
			
			if (c == UnDefType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == UnDefType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			}
			else if (c == OnOffType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == OpenClosedType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == OnOffType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			}
			else if (c == OpenClosedType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else {
				return UnDefType.UNDEF;
			}
		}

		/**
		 * Constructor for config object
		 * @param item
		 * @param config
		 * @throws BindingConfigParseException if 
		 */
		ModbusBindingConfig(Item item, String config) throws BindingConfigParseException {
			this.item = item;
			
			try {
				String [] items = config.split(":");
				slaveName = items[0];
				if (items.length == 2) {
					readRegister = Integer.valueOf(items[1]);
					writeRegister = Integer.valueOf(items[1]);
				} else if (items.length == 3) {
					assignRegisters(items[1]);
					assignRegisters(items[2]);
				} else {
					throw new BindingConfigParseException("Invalid number of registers in item configuration");
				}
			} catch (Exception e) {
				throw new BindingConfigParseException(e.getMessage());
			}
		}

		/**
		 * Parses register reference string and assigns values to readRegister and writeRegister
		 * @param item
		 * @throws BindingConfigParseException if register description is invalid
		 */
		private void assignRegisters(String item)
				throws BindingConfigParseException {
			if (item.startsWith("<")) {
				readRegister = Integer.valueOf(item.substring(1, item.length()));
			} else if (item.startsWith(">")) {
				writeRegister = Integer.valueOf(item.substring(1, item.length()));
			} else {
				throw new BindingConfigParseException("Register references should be either :X or :<X:>Y");			
			}
		}

	}

	@Override
	public void removeConfigurations(String context) {
		super.removeConfigurations(context);
	}

}

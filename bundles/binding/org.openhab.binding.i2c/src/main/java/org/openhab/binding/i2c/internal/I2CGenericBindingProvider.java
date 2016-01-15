/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.i2c.internal;

import java.util.Map;
import org.openhab.binding.i2c.I2CBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Diego A. Fliess
 * @since 1.8.0
 */
public class I2CGenericBindingProvider extends AbstractGenericBindingProvider implements I2CBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(I2CGenericBindingProvider.class);

	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "i2c";
	}
	

	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		/* Only 'Switch' and 'Contact' types are allowed */
		if (!((item instanceof SwitchItem) || (item instanceof ContactItem))) {
			logger.error("Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
					"' while only 'Switch' or 'Contact' types are allowed");
			throw new BindingConfigParseException("Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
					"' while only 'Switch' or 'Contact' types are allowed");
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		I2CBindingConfig config = new I2CBindingConfig();
		
		//parse bindingConfig here ...
		/* Configuration string should be a json in the form: 
		 * Switch Test1 "Test 1" (Tests) { i2c="{ busAddress:20, pin:'A0', defaultState:'LOW', pinMode:'DIGITAL_OUTPUT'}" }
		 */
		
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonData=parser.parseJson(bindingConfig);
	
		
		try {
			
			logger.debug("processingBindingConfiguration in context " + context);
			config.setBusAddress(Integer.parseInt((String) jsonData.get("busAddress"),16));
			config.setDefaultState(PinState.valueOf((String) jsonData.get("defaultState")));
			config.setPinMode(PinMode.valueOf((String) jsonData.get("pinMode")));
			config.setPin((Pin) MCP23017Pin.class.getField("GPIO_" + (String) jsonData.get("pin")).get(null));
			
			
			
		} catch (IllegalArgumentException e) {
			logger.error("Illegal Argument Exception in configuration string '" + bindingConfig + "' " +  e.getMessage());
			throw new BindingConfigParseException("Illegal Access Exception  in configuration string '" + bindingConfig + "'");
		} catch (IllegalAccessException e) {
			logger.error("Illegal Argument Exception in configuration string '" + bindingConfig + "' " +  e.getMessage());
			throw new BindingConfigParseException("Illegal Argument Exception  in configuration string '" + bindingConfig + "'");
		} catch (NoSuchFieldException e) {
			logger.error("No Such Field Exception in configuration string '" + bindingConfig + "' " +  e.getMessage());
			throw new BindingConfigParseException("No Such Field Exception  in configuration string '" + bindingConfig + "'");
		} catch (SecurityException e) {
			logger.error("Security Exception in configuration string '" + bindingConfig + "' " +  e.getMessage());
			throw new BindingConfigParseException("Security Exception  in configuration string '" + bindingConfig + "'");
		}

		addBindingConfig(item, config);		
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Diego
	 * @since 1.0.0
	 */
	class I2CBindingConfig implements BindingConfig {
		/** Configured i2c Bus Address */
		private int busAddress;
		/** Configured pin number */
		private Pin pin;
		/** Configured pin default state*/
		private PinState defaultState;
		/** Pin pinMode. If item type is <code>Switch</code> the pin
		 * pinMode should be out, if <code>Contact</code> - in
		 */
		private PinMode pinMode;
		
		
		/**
		 * @return the busAddress
		 */
		public int getBusAddress() {
			return busAddress;
		}

		/**
		 * @param busAddress the busAddress to set
		 */
		public void setBusAddress(int busAddress) {
			this.busAddress = busAddress;
		}

		/**
		 * @return the pin
		 */
		public Pin getPin() {
			return pin;
		}

		/**
		 * @param pin the pin to set
		 */
		public void setPin(Pin pin) {
			this.pin = pin;
		}

		/**
		 * @return the defaultState
		 */
		public PinState getDefaultState() {
			return defaultState;
		}

		/**
		 * @param defaultState the defaultState to set
		 */
		public void setDefaultState(PinState defaultState) {
			this.defaultState = defaultState;
		}

		/**
		 * @return the pinMode
		 */
		public PinMode getPinMode() {
			return pinMode;
		}

		/**
		 * @param pinMode the pinMode to set
		 */
		public void setPinMode(PinMode pinMode) {
			this.pinMode = pinMode;
		}

		
	}
	
	/* Internal method for searching itemName config */
	private I2CBindingConfig getConfig(String itemName) {
		I2CBindingConfig config = (I2CBindingConfig) bindingConfigs.get(itemName);
		if (config == null) {
			throw new IllegalArgumentException("The item name '" + itemName + "'is invalid or the item isn't configured");
		}
		return config;
	}
	/**
	 * @return the busAddress
	 */
	public int getBusAddress(String itemName) {
		return getConfig(itemName).getBusAddress();
	}

	/**
	 * @return the pin
	 */
	public Pin getPin(String itemName) {
		return getConfig(itemName).getPin();
	}

	/**
	 * @return the defaultState
	 */
	public PinState getDefaultState(String itemName) {
		return getConfig(itemName).getDefaultState();
	}

	/**
	 * @return the pinMode
	 */
	public PinMode getPinMode(String itemName) {
		return getConfig(itemName).getPinMode();
	}

}

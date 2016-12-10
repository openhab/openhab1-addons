/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mcp3424.internal;

import java.util.Map;
import org.openhab.binding.mcp3424.MCP3424BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import com.pi4j.gpio.extension.mcp.MCP3424Pin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Alexander Falkenstern
 * @since 1.8.3
 */
public class MCP3424GenericBindingProvider extends AbstractGenericBindingProvider implements MCP3424BindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(MCP3424GenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mcp3424";
	}
	
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		/* Only 'Number' and 'Dimmer' types are allowed */
		if (!(item instanceof NumberItem) && !(item instanceof DimmerItem)) {
		    String name = item.getName();
		    String simpleName = item.getClass().getSimpleName();
			logger.error("Item '" + name + "' is of type '" + simpleName +
					"' while only 'Number' or 'Dimmer' type are allowed");
			throw new BindingConfigParseException("Item '" + name + "' is of type '" + simpleName +
					"' while only 'Number' or 'Dimmer' types are allowed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		MCP3424BindingConfig config = new MCP3424BindingConfig(item);
		
		//parse bindingConfig here ...
		/* Configuration string should be a json in the form: 
		 * Number Test1 "Test 1" (Tests) { mcp3424="{ address:6C, pin:'GPIO_CH0', gain:1, resolution:12" }
         * Dimmer Test2 "Test 2" (Tests) { mcp3424="{ address:6C, pin:'GPIO_CH1', gain:1, resolution:12" }
		 */
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonData=parser.parseJson(bindingConfig);
	
		try {
			logger.debug("processingBindingConfiguration in context " + context);
			config.setBusAddress(Integer.parseInt((String) jsonData.get("address"), 16));
			config.setPin((Pin) MCP3424Pin.class.getField("GPIO_" + (String) jsonData.get("pin")).get(null));
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

        try {
            config.setGain(Integer.parseInt((String) jsonData.get("gain"), 10));
        } catch (Exception e) {
            logger.info("No configuration for gain. Using default: " + config.getGain());
        }
        try {
            config.setResolution(Integer.parseInt((String) jsonData.get("resolution"), 10));
        } catch (Exception e) {
            logger.info("No configuration for resolution. Using default: " + config.getResolution());
        }
        addBindingConfig(item, config);		
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
     * @author Alexander Falkenstern
     * @since 1.8.3
	 */
	class MCP3424BindingConfig implements BindingConfig {

		/** Configured mcp3424 Bus Address */
		private int busAddress;
		
		/** Configured pin number */
		private Pin pin;
		
        /** Configured gain */
        private int gain = 1;

        /** Configured resolution */
        private int resolution = 12;
        
        private Item item = null;
        
        MCP3424BindingConfig(Item item) {
            this.item = item;
        }

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
         * @return the gain
         */
        public int getGain() {
            return gain;
        }

        /**
         * @param gain the gain to set
         */
        public void setGain(int gain) {
            this.gain = gain;
        }

        /**
         * @return the resolution
         */
        public int getResolution() {
            return resolution;
        }

        /**
         * @param pin the pin to set
         */
        public void setResolution(int resolution) {
            this.resolution = resolution;
        }

        /**
         * @return configured item
         */
        public Item getItem() {
            return this.item;
        }
    }
	
	/* Internal method for searching itemName config */
	private MCP3424BindingConfig getConfig(String itemName) {
		MCP3424BindingConfig config = (MCP3424BindingConfig)bindingConfigs.get(itemName);
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
     * @return the gain
     */
    public int getGain(String itemName) {
        return getConfig(itemName).getGain();
    }

    /**
     * @return the resolution
     */
    public int getResolution(String itemName) {
        return getConfig(itemName).getResolution();
    }

    /**
     * @return configured item
     */
    public Item getItem(String itemName) {
        return getConfig(itemName).getItem();
    }
}

/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Alexander Falkenstern
 * @since 1.9.0
 */
public class MCP3424GenericBindingProvider extends AbstractGenericBindingProvider implements MCP3424BindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(MCP3424GenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "mcp3424";
    }

    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        /* Only 'Number' and 'Dimmer' types are allowed */
        if (!(item instanceof NumberItem) && !(item instanceof DimmerItem)) {
            String message = "Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + "' ";
            message = message + "while only 'Number' or 'Dimmer' types are allowed";
            logger.error("{}", message);
            throw new BindingConfigParseException(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        MCP3424BindingConfig config = new MCP3424BindingConfig(item);

        // parse bindingConfig here ...
        /*
         * Configuration string should be a json in the form:
         * Number Test1 "Test 1" (Tests) { mcp3424="{ address:6C, pin:'CH0', gain:1, resolution:12" }
         * Dimmer Test2 "Test 2" (Tests) { mcp3424="{ address:6C, pin:'CH1', gain:1, resolution:12" }
         */
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();

        @SuppressWarnings("unchecked")
        Map<String, Object> jsonData = parser.parseJson(bindingConfig);

        try {
            logger.debug("processingBindingConfiguration in context {}", context);
            config.setBusAddress(Integer.parseInt((String) jsonData.get("address"), 16));
            config.setPin((Pin) MCP3424Pin.class.getField("GPIO_" + (String) jsonData.get("pin")).get(null));
        } catch (IllegalArgumentException exception) {
            final String message = "Illegal argument exception in configuration string ";
            logger.error("{} '{}': {}", message, bindingConfig, exception.getMessage());
            throw new BindingConfigParseException(message + "'" + bindingConfig + "'");
        } catch (IllegalAccessException exception) {
            final String message = "Illegal access exception in configuration string ";
            logger.error("{} '{}': {}", message, bindingConfig, exception.getMessage());
            throw new BindingConfigParseException(message + "'" + bindingConfig + "'");
        } catch (NoSuchFieldException exception) {
            final String message = "No such field exception in configuration string ";
            logger.error("{} '{}': {}", message, bindingConfig, exception.getMessage());
            throw new BindingConfigParseException(message + "'" + bindingConfig + "'");
        } catch (SecurityException exception) {
            final String message = "Security exception in configuration string ";
            logger.error("{} '{}': {}", message, bindingConfig, exception.getMessage());
            throw new BindingConfigParseException(message + "'" + bindingConfig + "'");
        }

        try {
            config.setGain(Integer.parseInt((String) jsonData.get("gain"), 10));
        } catch (Exception e) {
            logger.info("No configuration for gain. Using default: {}", config.getGain());
        }
        try {
            config.setResolution(Integer.parseInt((String) jsonData.get("resolution"), 10));
        } catch (Exception e) {
            logger.info("No configuration for resolution. Using default: {}", config.getResolution());
        }
        addBindingConfig(item, config);
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Alexander Falkenstern
     * @since 1.9.0
     */
    class MCP3424BindingConfig implements BindingConfig {

        /** Configured mcp3424 bus address */
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
         * @return the bus address
         */
        public int getBusAddress() {
            return busAddress;
        }

        /**
         * @param busAddress the bus address to set
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
         * @param resolution the resolution to set
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
        MCP3424BindingConfig config = (MCP3424BindingConfig) bindingConfigs.get(itemName);
        if (config == null) {
            throw new IllegalArgumentException(
                    "The item name '" + itemName + "' is invalid or the item isn't configured");
        }
        return config;
    }

    /**
     * @return the bus address
     */
    @Override
    public int getBusAddress(String itemName) {
        return getConfig(itemName).getBusAddress();
    }

    /**
     * @return the pin
     */
    @Override
    public Pin getPin(String itemName) {
        return getConfig(itemName).getPin();
    }

    /**
     * @return configured ADC gain
     */
    @Override
    public int getGain(String itemName) {
        return getConfig(itemName).getGain();
    }

    /**
     * @return configured ADC resolution
     */
    @Override
    public int getResolution(String itemName) {
        return getConfig(itemName).getResolution();
    }

    /**
     * @return configured item
     */
    @Override
    public Item getItem(String itemName) {
        return getConfig(itemName).getItem();
    }
}

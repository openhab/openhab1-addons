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
package org.openhab.binding.smarthomatic.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.smarthomatic.SmarthomaticBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author mcjobo
 * @author arohde
 * @since 1.9.0
 */
public class SmarthomaticGenericBindingProvider extends AbstractGenericBindingProvider
        implements SmarthomaticBindingProvider {

    private static final Pattern TRANSFORMATION_PATTERN = Pattern.compile("(.*):(.*)");
    // We find the id of an deviceId in this map
    // Therefore this map is static
    private static HashMap<String, Integer> devices = new HashMap<String, Integer>();
    private static final Pattern CONFIG_PATTERN = Pattern.compile(".\\[(.*)]");

    /**
     * setter for devices
     * 
     * @param name
     * @param deviceID
     */
    public static void addDevice(String name, int deviceID) {
        devices.put(name, deviceID);
    }

    /**
     * getter for device
     * 
     * @param name
     * @return
     */
    public static int getDevice(String name) {
        return devices.get(name);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public String getBindingType() {
        return "smarthomatic";
    }

    /**
     * @{inheritDoc
     * 
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
        // throw new BindingConfigParseException("item '" + item.getName()
        // + "' is of type '" + item.getClass().getSimpleName()
        // +
        // "', only Switch- and DimmerItems are allowed - please check your
        // *.items configuration");
        // }
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig.startsWith("<")) {
            SmarthomaticBindingConfig config = parseIncomingBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else if (bindingConfig.startsWith(">")) {
            SmarthomaticBindingConfig config = parseOutgoingBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else if (bindingConfig.startsWith("=")) {
            SmarthomaticBindingConfig config = parseBidirectionalBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else {
            throw new BindingConfigParseException("Item '" + item.getName() + "' does not start with <, > or =.");
        }

    }

    private SmarthomaticBindingConfig parseBidirectionalBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        SmarthomaticBindingConfig config = parseConfig(item, bindingConfig);

        return config;
    }

    private SmarthomaticBindingConfig parseOutgoingBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        SmarthomaticBindingConfig config = parseConfig(item, bindingConfig);

        return config;
    }

    private SmarthomaticBindingConfig parseIncomingBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        SmarthomaticBindingConfig config = parseConfig(item, bindingConfig);

        return config;
    }

    private SmarthomaticBindingConfig parseConfig(Item item, String bindingConfig) throws BindingConfigParseException {
        SmarthomaticBindingConfig config = new SmarthomaticBindingConfig();
        Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

        if (!matcher.matches()) {
            throw new BindingConfigParseException("Config for item '" + item.getName() + "' could not be parsed.");
        }

        bindingConfig = matcher.group(1);
        config.setItemName(item.getName());
        config.setItem(item);

        matcher = TRANSFORMATION_PATTERN.matcher(bindingConfig);
        if (matcher.matches()) {
            bindingConfig = matcher.group(1);
            String transformation = matcher.group(2);
            config.getConfigParams().put("transformation", transformation);
        }

        // parse bindingconfig here ...
        StringTokenizer confItems = new StringTokenizer(bindingConfig, ",");
        while (confItems.hasMoreTokens()) {
            String[] token = confItems.nextToken().split("=");
            String key = token[0];
            String value = token[1];
            config.getConfigParams().put(key, value);
            // Strip all whitespaces from token[0]
            key = key.replaceAll("\\s", "");
            if ("deviceId".equals(key)) {
                config.setDeviceId(value.replaceAll("\\s", ""));
            } else if ("messageGroupId".equals(key)) {
                config.setMessageGroupId(value.replaceAll("\\s", ""));
            } else if ("messageId".equals(key)) {
                config.setMessageId(value.replaceAll("\\s", ""));
            } else if ("messagePart".equals(key)) {
                config.setMessagePartId(value.replaceAll("\\s", ""));
            } else if ("messageItem".equals(key)) {
                config.setMessageItemId(value.replaceAll("\\s", ""));
            }

        }
        return config;
    }

    @Override
    public int getDeviceId(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getDeviceId();
    }

    @Override
    public int getMessageId(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getMessageId();
    }

    @Override
    public int getMessageGroupId(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getMessageGroupId();
    }

    @Override
    public int getMessagePartId(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getMessagePartId();
    }

    @Override
    public int getMessageItemId(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getMessagePartId();
    }

    @Override
    public Item getItem(String itemName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getItem();
    }

    @Override
    public String getConfigParam(String itemName, String paramName) {
        SmarthomaticBindingConfig config = (SmarthomaticBindingConfig) bindingConfigs.get(itemName);

        return config.getConfigParams().get(paramName);
    }

    /**
     * holds informations about the binding
     * 
     * @author mcjobo
     * @since 1.9.0
     */
    class SmarthomaticBindingConfig implements BindingConfig {
        // put member fields here which holds the parsed values
        private Map<String, String> configParams = new HashMap<String, String>();
        private String itemName;
        private int deviceId = -1;
        private int messagePart = 0;
        private int messageGroupId = 0;
        private int messageId = 0;
        private Item item;
        private int messageItemId = 0;

        /**
         * getter for config parameter
         * 
         * @return
         */
        public Map<String, String> getConfigParams() {
            return configParams;
        }

        /**
         * setter for config parameter
         * 
         * @param configParams
         */
        public void setConfigParams(Map<String, String> configParams) {
            this.configParams = configParams;
        }

        /**
         * getter for the device id
         * 
         * @return
         */
        public int getDeviceId() {
            return deviceId;
        }

        /**
         * setter for the device id
         * 
         * @param device
         */
        public void setDeviceId(String device) {
            // now there are two possibilities:
            // 1. we have a number in deviceId => store it directly
            try {
                this.deviceId = Integer.parseInt(device);
            } catch (NumberFormatException e) {
                this.deviceId = SmarthomaticGenericBindingProvider.getDevice(device);
            }
        }

        /**
         * getter for the message id
         * 
         * @return
         */
        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(String toggleTime) {
            try {
                this.messageId = Integer.parseInt(toggleTime);
            } catch (NumberFormatException e) {
                this.messageId = 0;
            }
        }

        /**
         * getter for the message item id
         * 
         * @return
         */
        public int getMessageItemId() {
            return messageItemId;
        }

        /**
         * setter for the message item id
         * 
         * @param toggleTime
         */
        public void setMessageItemId(String toggleTime) {
            try {
                this.messageItemId = Integer.parseInt(toggleTime);
            } catch (NumberFormatException e) {
                this.messageItemId = 0;
            }
        }

        /**
         * getter for the item name
         * 
         * @return
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * setter for the item name
         * 
         * @param itemName
         */
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        /**
         * getter for message part id
         * 
         * @return
         */
        public int getMessagePartId() {
            return messagePart;
        }

        /**
         * setter for the message part id
         * 
         * @param port
         */
        public void setMessagePartId(String port) {
            try {
                this.messagePart = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                this.messagePart = 0;
            }
        }

        /**
         * @return the MessageGroupID of the item
         */
        public int getMessageGroupId() {
            return this.messageGroupId;
        }

        /**
         * @param type
         *            is a MessageGroupID - number or a corresponding type like
         *            Generic, GPIO, Weather, Environment, Powerswitch, Dimmer
         *            or Digiboard For more information see the smarthomatic
         *            homepage
         */
        public void setMessageGroupId(String type) {
            try {
                this.messageGroupId = Integer.parseInt(type);
            } catch (NumberFormatException e) {
                if ("GENERIC".equals(type.toUpperCase())) {
                    this.messageGroupId = 0;
                } else if ("GPIO".equals(type.toUpperCase())) {
                    this.messageGroupId = 1;
                } else if ("WEATHER".equals(type.toUpperCase())) {
                    this.messageGroupId = 10;
                } else if ("ENVIRONMENT".equals(type.toUpperCase())) {
                    this.messageGroupId = 11;
                } else if ("POWERSWITCH".equals(type.toUpperCase())) {
                    this.messageGroupId = 20;
                } else if ("DIMMER".equals(type.toUpperCase())) {
                    this.messageGroupId = 60;
                } else if ("DIGIBOARD".equals(type.toUpperCase())) {
                    this.messageGroupId = 99;
                }
            }
        }

        /**
         * getter for the item
         * 
         * @return
         */
        public Item getItem() {
            return item;
        }

        /**
         * setter for the item
         * 
         * @param item
         */
        public void setItem(Item item) {
            this.item = item;
        }

    }

}

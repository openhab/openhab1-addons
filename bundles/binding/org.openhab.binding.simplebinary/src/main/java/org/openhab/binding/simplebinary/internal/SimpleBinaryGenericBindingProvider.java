/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.simplebinary.SimpleBinaryBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryGenericBindingProvider extends AbstractGenericBindingProvider
        implements SimpleBinaryBindingProvider {

    public enum DataDirectionFlow {
        INOUT,
        INPUT,
        OUTPUT
    }

    /**
     *
     *
     * @author tucek
     * @since 1.9.0
     *
     */
    public class DeviceConfig {
        public DeviceConfig(String portName, int deviceAddress, DataDirectionFlow dataDirection, int itemAddress) {
            this.deviceName = portName;
            this.deviceAddress = deviceAddress;
            this.dataDirection = dataDirection;
            this.itemAddress = itemAddress;
        }

        /**
         * Device(port) name
         */
        String deviceName;
        /**
         * Device(slave) address
         */
        int deviceAddress;
        /**
         * Data flow direction
         */
        DataDirectionFlow dataDirection = DataDirectionFlow.INOUT;
        /**
         * Item address
         */
        int itemAddress;

        /**
         * @return Device(port) name
         */
        public String getPortName() {
            return deviceName;
        }

        /**
         * @return Device(slave) address
         */
        public int getDeviceAddress() {
            return deviceAddress;
        }

        /**
         * @return Data flow direction
         */
        public DataDirectionFlow getDataDirection() {
            return dataDirection;
        }

        /**
         * @return Item address
         */
        public int getItemAddress() {
            return itemAddress;
        }

        @Override
        public String toString() {
            return "PortName=" + deviceName + "|DeviceAddress=" + deviceAddress + "|ItemAddress=" + itemAddress
                    + "|DataDirection=" + dataDirection;
        }
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author vita
     * @since 1.9.0
     */
    class SimpleBinaryBindingConfig implements BindingConfig {

        public Item item;
        SimpleBinaryDeviceConfigCollection devices;
        String datatype = "word";
        Type state = null;

        public SimpleBinaryBindingConfig(DeviceConfig device) {
            devices = new SimpleBinaryDeviceConfigCollection();
            devices.add(device);
        }

        public Class<? extends Item> getItemType() {
            return item.getClass();
        };

        public void setState(Type state) {
            this.state = state;
        }

        public Type getState() {
            return state;
        }

        /**
         * Return item data length
         *
         * @return
         */
        public int getDataLenght() {
            if (getDataType() == SimpleBinaryTypes.ARRAY) {
                Matcher matcher = Pattern.compile("^array\\[(\\d+)\\]$").matcher(datatype);

                return Integer.valueOf(matcher.group(1)).intValue();
            } else {
                return 1;
            }
        }

        /**
         * Return item data type
         *
         * @return
         */
        public SimpleBinaryTypes getDataType() {
            if (datatype.equals("byte")) {
                return SimpleBinaryTypes.BYTE;
            }
            if (datatype.equals("word")) {
                return SimpleBinaryTypes.WORD;
            }
            if (datatype.equals("dword")) {
                return SimpleBinaryTypes.DWORD;
            }
            if (datatype.equals("float")) {
                return SimpleBinaryTypes.FLOAT;
            }
            if (datatype.equals("hsb")) {
                return SimpleBinaryTypes.HSB;
            }
            if (datatype.equals("rgb")) {
                return SimpleBinaryTypes.RGB;
            }
            if (datatype.equals("rgbw")) {
                return SimpleBinaryTypes.RGBW;
            }

            Matcher matcher = Pattern.compile("^array\\[(\\d+)\\]$").matcher(datatype);
            if (matcher.matches()) {
                return SimpleBinaryTypes.ARRAY;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("getDataType() - unresolved type: {}", datatype);
            }
            return SimpleBinaryTypes.UNKNOWN;
        }

        @Override
        public String toString() {
            String text = item.getName() + "|DataType=" + this.getDataType() + "|Device count=" + this.devices.size()
                    + "( ";

            for (DeviceConfig d : devices) {
                text += "Port=" + d.getPortName() + " DeviceAddress=" + d.getDeviceAddress() + " MemAddress="
                        + d.getItemAddress() + " Direction=" + d.getDataDirection() + "|";
            }

            return text + ")";
        }
    }

    /**
     * This is a helper class holding binding info configuration details
     *
     * @author vita
     * @since 1.9.0
     */
    class SimpleBinaryInfoBindingConfig implements BindingConfig {

        /**
         * openHAB item instance
         */
        public Item item;
        /**
         * Contains device(port) name ex.: port01
         */
        public String device;
        /**
         * Contains slave address
         */
        public int busAddress;
        /**
         * Requested info type
         */
        public InfoType infoType;

        @Override
        public String toString() {
            String text = item.getName() + "|Device=" + this.device + "|DeviceAddress=" + this.busAddress + "|InfoType="
                    + infoType;

            return text;
        }
    }

    public enum InfoType {
        STATE,
        PREVIOUS_STATE,
        STATE_CHANGE_TIME,
        PACKET_LOST
    }

    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryGenericBindingProvider.class);

    /**
     * Return all configs map
     *
     * @return
     */
    public Map<String, BindingConfig> configs() {
        return bindingConfigs;
    }

    @Override
    public String getBindingType() {
        return "simplebinary";
    }

    /**
     * Return item config
     *
     * @param itemName
     *            Item name
     * @return
     */
    public BindingConfig getItemConfig(String itemName) {

        return bindingConfigs.get(itemName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        if (logger.isDebugEnabled()) {
            logger.debug("processBindingConfiguration() method is called!");
            logger.debug("Item:{}/Config:{}", item, bindingConfig);
        }

        super.processBindingConfiguration(context, item, bindingConfig);

        // config
        //
        // device:busAddress:address/ID:type:direction
        //
        // device - target device/port (ex. "port" , "port1", "port2")
        // device:busAddress - bus / device address (0-127)
        // address/ID - number
        // type - byte or word or dword or array[length] or rgb or rgbw or hsb
        // direction - "I" or "O" or "IO"
        Matcher matcher = Pattern.compile("^(port\\d*|tcpserver):(\\d+):(\\d+)((:[a-zA-Z0-9_]*)*)$")
                .matcher(bindingConfig);

        if (!matcher.matches()) {
            // look for info config
            matcher = Pattern
                    .compile(
                            "^(port\\d*|tcpserver)(:(\\d+))*:info:((state)|(previous_state)|(state_change_time)|(packet_lost))$")
                    .matcher(bindingConfig);

            if (!matcher.matches()) {
                throw new BindingConfigParseException("Illegal config format: " + bindingConfig
                        + ". Correct format: simplebinary=\"port:deviceAddress:itemAddress:dataType:ioDirection\". Example: simplebinary=\"port:1:1:byte:O\"");
            } else {
                // device info config
                SimpleBinaryInfoBindingConfig config = new SimpleBinaryInfoBindingConfig();

                config.item = item;
                config.device = matcher.group(1);

                if (matcher.group(3) != null) {
                    config.busAddress = Integer.valueOf(matcher.group(3)).intValue();
                } else {
                    config.busAddress = -1;
                }

                String param = matcher.group(4);

                if (param.equalsIgnoreCase("state")) {
                    config.infoType = InfoType.STATE;
                } else if (param.equalsIgnoreCase("previous_state")) {
                    config.infoType = InfoType.PREVIOUS_STATE;
                } else if (param.equalsIgnoreCase("state_change_time")) {
                    config.infoType = InfoType.STATE_CHANGE_TIME;
                } else if (param.equalsIgnoreCase("packet_lost")) {
                    config.infoType = InfoType.PACKET_LOST;
                } else {
                    throw new BindingConfigParseException("Unsupported info parameter " + param);
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Info item added: {}", config);
                }

                addBindingConfig(item, config);
            }
        } else {
            DataDirectionFlow datadirection = DataDirectionFlow.INOUT;
            String dataType = "";

            boolean dataTypeSpecified = false;

            // check if optional parameters are specified
            if (matcher.group(4).length() > 0) {

                String[] optionalConfigs = matcher.group(4).substring(1).split(":");

                for (int i = 0; i < optionalConfigs.length; i++) {
                    String param = optionalConfigs[i].toLowerCase();
                    // is direction?
                    if (param.equals("i") || param.equals("o") || param.equals("io")) {
                        datadirection = param.equals("io") ? DataDirectionFlow.INOUT
                                : param.equals("i") ? DataDirectionFlow.INPUT : DataDirectionFlow.OUTPUT;
                    } else {
                        // is datatype?
                        Matcher typematcher = Pattern.compile("^byte|word|dword|float|hsb|rgb|rgbw|array\\[\\d+\\]$")
                                .matcher(param);

                        if (typematcher.matches()) {
                            // datatype specified as optional parameter
                            dataTypeSpecified = true;

                            dataType = retreiveDataType(item, item.getClass(), param);
                        } else {
                            logger.warn("Item %s. Unsupported optional parameter %s", item.getName(),
                                    optionalConfigs[i]);
                        }
                    }
                }
            }

            // datatype not specified as optional parameter -> set default
            if (!dataTypeSpecified) {
                dataType = getDefaultDataType(item, item.getClass());
            }

            // device config for item
            DeviceConfig d = new DeviceConfig(matcher.group(1), Integer.valueOf(matcher.group(2)).intValue(),
                    datadirection, Integer.valueOf(matcher.group(3)).intValue());

            if (logger.isDebugEnabled()) {
                logger.debug("DeviceConfig:{}", d);
            }

            // item already exists? -> add only new device
            if (bindingConfigs.containsKey(item.getName())) {
                // retrieve config
                SimpleBinaryBindingConfig config = (SimpleBinaryBindingConfig) bindingConfigs.get(item.getName());

                if (logger.isDebugEnabled()) {
                    logger.debug("Required/New datatype={}/{}", config.datatype, dataType);
                }
                // has same datatype?
                if (!config.datatype.equals(dataType)) {
                    logger.warn(
                            "Device configuration ({}) for item {} is invalid. All configuration must have same data type.",
                            d, item.getName());
                    return;
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Updating:{}", config);
                }
                // add device config into device list
                config.devices.add(d);
            } else {
                // create new item with device config
                SimpleBinaryBindingConfig config = new SimpleBinaryBindingConfig(d);
                config.item = item;
                config.datatype = dataType;

                if (logger.isDebugEnabled()) {
                    logger.debug("Data item added:{}", config);
                }
                addBindingConfig(item, config);
            }
        }
    }

    /**
     * @param item
     * @param config
     * @throws BindingConfigParseException
     */
    public String getDefaultDataType(Item item, Class<? extends Item> itemType) throws BindingConfigParseException {
        if (itemType.isAssignableFrom(NumberItem.class)) {
            logger.warn("Item %s has not specified datatype. Setted to WORD.", item.getName());
            return "word";
        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
            return "byte";
        } else if (itemType.isAssignableFrom(DimmerItem.class)) {
            return "byte";
        } else if (itemType.isAssignableFrom(ColorItem.class)) {
            logger.warn("Item %s has not specified datatype. Setted to RGB.", item.getName());
            return "rgb";
        } else if (itemType.isAssignableFrom(StringItem.class)) {
            logger.warn("Item %s has not specified datatype with length. Setted to ARRAY with length 32.",
                    item.getName());
            return "array[32]";
        } else if (itemType.isAssignableFrom(ContactItem.class)) {
            return "byte";
        } else if (itemType.isAssignableFrom(RollershutterItem.class)) {
            return "word";
        } else {
            throw new BindingConfigParseException("Unsupported item type: " + item);
        }
    }

    /**
     * @param item
     * @param config
     * @param param
     * @throws BindingConfigParseException
     */
    public String retreiveDataType(Item item, Class<? extends Item> itemType, String param)
            throws BindingConfigParseException {
        if (itemType.isAssignableFrom(NumberItem.class)) {
            if (!param.equals("byte") && !param.equals("word") && !param.equals("dword") && !param.equals("float")) {
                logger.warn(
                        "Item %s supported datatypes: byte, word, dword or float. Type %s is ignored. Setted to word.",
                        item.getName(), param);
                return "word";
            } else {
                return param;
            }

        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
            if (!param.equals("byte")) {
                logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
            }
            return "byte";

        } else if (itemType.isAssignableFrom(DimmerItem.class)) {
            if (!param.equals("byte")) {
                logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
            }
            return "byte";

        } else if (itemType.isAssignableFrom(ColorItem.class)) {
            if (!param.equals("rgb") && !param.equals("rgbw") && !param.equals("hsb")) {
                logger.warn("Item %s supported datatypes: hsb, rgb or rgbw. Type %s is ignored. Setted to rgb.",
                        item.getName(), param);
                return "rgb";
            } else {
                return param;
            }

        } else if (itemType.isAssignableFrom(StringItem.class)) {
            if (!param.startsWith("array")) {
                logger.warn("Item %s support datatype array only. Type %s is ignored. Setted to ARRAY with length 32.",
                        item.getName(), param);
            }
            return "array[32]";

        } else if (itemType.isAssignableFrom(ContactItem.class)) {
            if (!param.equals("byte")) {
                logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
            }
            return "byte";

        } else if (itemType.isAssignableFrom(RollershutterItem.class)) {
            if (!param.equals("word")) {
                logger.warn("Item %s support datatype word only. Type %s is ignored.", item.getName(), param);
            }
            return "word";

        } else {
            throw new BindingConfigParseException("Unsupported item type: " + item);
        }
    }

    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // all types welcome

    }
}

/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.VeluxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for semantic checks of item definitions versus binding configuration,
 * the parsing and the internal storage of the item-related binding configuration.
 *
 * Valid bindings for the velux bridge KLF200 are:
 * <ul>
 * <li><code>{ velux="thing=&lt;thingIdentifier&gt;channel=&lt;channelIdentifier&gt;" }</code></li>
 * </ul>
 *
 * Therefore valid bindings are i.e.
 * <ul>
 * <li><code>{ velux="thing=bridge;channel=status" }</code></li>
 * <li><code>{ velux="thing=bridge;channel=firmware" }</code></li>
 * <li><code>{ velux="thing=scene;channel=action#OpenWindows" }</code></li>
 * <li><code>{ velux="thing=scene;channel=silentMode#OpenWindows" }</code></li>
 * </ul>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public class VeluxGenericBindingProvider extends AbstractGenericBindingProvider implements VeluxBindingProvider {

    private final Logger logger = LoggerFactory.getLogger(VeluxGenericBindingProvider.class);

    /**
     * Stores information about the which items are associated to which scene.
     * The map has this content structure: itemname -> VeluxBindingConfig
     */
    private Map<String, VeluxBindingConfig> itemMap = new HashMap<String, VeluxBindingConfig>();

    /**
     * Stores information about the context of items. The map has this
     * content structure: context -> Set of itemNames
     */
    private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

    /**
     * This class is responsible for parsing the item-specific binding configuration.
     *
     * It separates the binding string into two - or optionally three - parts:
     * <ul>
     * <li><code>VeluxGenericBindingParser.thingIdentifier</code> representing the Thing,</li>
     * <li><code>VeluxGenericBindingParser.channelIdentifier</code> representing the Channel,</li>
     * <li><code>VeluxGenericBindingParser.channelValue</code> optionally representing additional parameters of the
     * Channel.</li>
     * </ul>
     *
     * The constructor take one string and split it according to the different separator.
     */
    private class VeluxGenericBindingParser {
        public String thingIdentifier;
        public String channelIdentifier;
        public String channelValue;

        private VeluxGenericBindingParser(final String bindingConfig) {
            String[] configParts = bindingConfig.trim().split(VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR);
            if (configParts.length == 2) {
                String[] keyValue1 = configParts[0].trim().split(VeluxBindingConstants.BINDING_KV_SEPARATOR);
                String thingIdentifierKey = keyValue1[0];
                logger.trace("VeluxGenericBindingParser() thingIdentifierKey={}.", thingIdentifierKey);
                if (keyValue1.length == 2) {
                    this.thingIdentifier = keyValue1[1];
                    logger.trace("VeluxGenericBindingParser() thingIdentifier={}.", this.thingIdentifier);
                    if (!VeluxBindingConstants.BINDING_ID_THING.equals(thingIdentifierKey)) {
                        throw new IllegalArgumentException("Velux binding must contain several parts, separated by "
                                + VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR
                                + ": 1st part should start with identifier " + VeluxBindingConstants.BINDING_ID_THING
                                + " followed by separator " + VeluxBindingConstants.BINDING_KV_SEPARATOR
                                + "' (ignoring binding '" + bindingConfig + "').");
                    }
                } else {
                    throw new IllegalArgumentException("Velux binding must contain several parts, separated by "
                            + VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR + " (ignoring binding '" + bindingConfig
                            + "').");
                }
                String[] keyValue2 = configParts[1].trim().split(VeluxBindingConstants.BINDING_KV_SEPARATOR);
                String channelIdentifierKey = keyValue2[0];
                logger.trace("VeluxGenericBindingParser() channelIdentifierKey={}.", channelIdentifierKey);
                if (keyValue2.length == 2) {
                    this.channelIdentifier = keyValue2[1];
                    logger.trace("VeluxGenericBindingParser() channelIdentifier={}.", this.channelIdentifier);
                    if (!VeluxBindingConstants.BINDING_ID_CHANNEL.equals(channelIdentifierKey)) {
                        throw new IllegalArgumentException("Velux binding must contain several parts, separated by "
                                + VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR
                                + ": 2nd part should start with identifier " + VeluxBindingConstants.BINDING_ID_CHANNEL
                                + " followed by separator " + VeluxBindingConstants.BINDING_KV_SEPARATOR
                                + " (ignoring binding '" + bindingConfig + "').");
                    }
                    String[] value2 = this.channelIdentifier.trim()
                            .split(VeluxBindingConstants.BINDING_VALUE_SEPARATOR);
                    this.channelIdentifier = value2[0];
                    if (value2.length > 1) {
                        if (value2.length == 2) {
                            this.channelValue = value2[1];
                        } else {
                            String[] subvalueArray = Arrays.copyOfRange(value2, 1, value2.length);
                            this.channelValue = Arrays.toString(subvalueArray);
                        }
                        logger.trace("VeluxGenericBindingParser() channelValue={}.", this.channelValue);
                    } else {
                        this.channelValue = "";
                    }
                    logger.debug("VeluxGenericBindingParser() channelIdentifier={}, channelValue={}.",
                            this.channelIdentifier, this.channelValue);
                } else {
                    throw new IllegalArgumentException("Velux binding must contain several parts, separated by "
                            + VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR
                            + ": 2nd part should start with identifier " + VeluxBindingConstants.BINDING_ID_CHANNEL
                            + " followed by separator " + VeluxBindingConstants.BINDING_KV_SEPARATOR
                            + " (ignoring binding '" + bindingConfig + "').");
                }

            } else {
                throw new IllegalArgumentException("Velux binding must contain two parts separated by '"
                        + VeluxBindingConstants.BINDING_KVPAIR_SEPARATOR + "' (ignoring binding '" + bindingConfig
                        + "').");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        logger.trace("getBindingType() called, returning {}.", VeluxBindingConstants.BINDING_ID);
        return VeluxBindingConstants.BINDING_ID;
    }

    @Override
    public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
        logger.trace("validateItemType({},{}) called.", item.getName(), bindingConfig);

        VeluxGenericBindingParser thisBinding = null;
        try {
            thisBinding = new VeluxGenericBindingParser(bindingConfig);
        } catch (IllegalArgumentException e) {
            throw new BindingConfigParseException(e.getMessage());
        }

        VeluxItemType thisType = VeluxItemType.getByThingAndChannel(thisBinding.thingIdentifier,
                thisBinding.channelIdentifier);
        if (thisType == null) {
            String errorMessage = "Velux binding must contain a valid thing/channel combination (found thing '"
                    + thisBinding.thingIdentifier + "', channel '" + thisBinding.channelIdentifier
                    + "'; ignoring binding '" + bindingConfig + "')";
            throw new BindingConfigParseException(errorMessage);
        }

        if (!thisType.getItemClass().getSimpleName().equals(item.getClass().getSimpleName())) {
            String errorMessage = "Velux binding: item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName() + "', '" + thisType.getItemClass().getSimpleName()
                    + "' is allowed - please check your *.items configuration (ignoring binding '" + bindingConfig
                    + "')";
            throw new BindingConfigParseException(errorMessage);

        } else {
            logger.trace("found config '{}': Item {} is of type '{}', which is allowed.", bindingConfig, item.getName(),
                    item.getClass().getSimpleName());
        }
        logger.debug("validateItemType() returned w/o exception.");

    }

    @Override
    public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
            throws BindingConfigParseException {
        logger.trace("processBindingConfiguration({},{},{}) called.", context, item.getName(), bindingConfig);

        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig == null) {
            return;
        }

        VeluxBindingConfig config = null;
        VeluxGenericBindingParser thisBinding = null;
        try {
            thisBinding = new VeluxGenericBindingParser(bindingConfig);
        } catch (IllegalArgumentException e) {
            throw new BindingConfigParseException(e.getMessage());
        }

        logger.trace("processBindingConfiguration() working on thing={},channel={}.", thisBinding.thingIdentifier,
                thisBinding.channelIdentifier);

        switch (thisBinding.thingIdentifier) {
            case VeluxBindingConstants.THING_VELUX_SCENE:
                logger.trace("processBindingConfiguration() found THING_VELUX_SCENE w/ channelValue={}.",
                        thisBinding.channelValue);
                if (thisBinding.channelValue.length() == 0) {
                    throw new BindingConfigParseException(
                            "Velux binding must contain a scene specified as channel subvalue.");
                }
                switch (thisBinding.channelIdentifier) {
                    case VeluxBindingConstants.CHANNEL_SCENE_ACTION:
                        config = new VeluxBindingConfig(VeluxItemType.SCENE_ACTION, thisBinding.channelValue);
                        break;
                    case VeluxBindingConstants.CHANNEL_SCENE_SILENTMODE:
                        config = new VeluxBindingConfig(VeluxItemType.SCENE_SILENTMODE, thisBinding.channelValue);
                        break;
                    default:
                        throw new BindingConfigParseException("Velux binding must contain one of "
                                + VeluxItemType.getChannelIdentifiers(thisBinding.thingIdentifier)
                                + " as channel keyword");
                }
                break;

            case VeluxBindingConstants.BRIDGE_TYPE:
            case VeluxBindingConstants.THING_VELUX_BRIDGE:
                logger.trace("processBindingConfiguration() found THING_VELUX_BRIDGE");
                switch (thisBinding.channelIdentifier) {
                    case VeluxBindingConstants.CHANNEL_BRIDGE_STATUS:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_STATUS, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_DO_DETECTION:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_DO_DETECTION, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_FIRMWARE:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_FIRMWARE, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_IPADDRESS:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_IPADDRESS, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_SUBNETMASK:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_SUBNETMASK, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_DEFAULTGW:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_DEFAULTGW, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_DHCP:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_DHCP, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_WLANSSID:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_WLANSSID, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_PRODUCTS:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_PRODUCTS, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_SCENES:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_SCENES, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_CHECK:
                        config = new VeluxBindingConfig(VeluxItemType.BRIDGE_CHECK, bindingConfig);
                        break;
                    case VeluxBindingConstants.CHANNEL_BRIDGE_SHUTTER:
                        try {
                            config = new VeluxRSBindingConfig(VeluxItemType.BRIDGE_SHUTTER, thisBinding.channelValue);
                        } catch (IllegalArgumentException e) {
                            throw new BindingConfigParseException(e.getMessage());
                        }
                        break;
                    default:
                        throw new BindingConfigParseException("Velux binding must contain one of "
                                + Arrays.toString(VeluxItemType.getChannelIdentifiers(thisBinding.thingIdentifier))
                                + " as channel keyword");
                }
                break;

            default:
                throw new BindingConfigParseException("Velux binding must contain one of "
                        + VeluxItemType.getThingIdentifiers() + " as thing keyword");
        }

        itemMap.put(item.getName(), config);
        Set<String> itemNames = contextMap.get(context);
        if (itemNames == null) {
            itemNames = new HashSet<String>();
            contextMap.put(context, itemNames);
        }

        addBindingConfig(item, config);
        logger.debug("processBindingConfiguration({},{},{}) successfully finished.", context, item.getName(),
                bindingConfig);
    }

    @Override
    public VeluxBindingConfig getConfigForItemName(String itemName) {
        logger.trace("getConfigForItemName({}) called.", itemName);
        VeluxBindingConfig config = null;

        if (itemMap.keySet().contains(itemName)) {
            config = itemMap.get(itemName);
        } else if (super.bindingConfigs.containsKey(itemName)) {
            config = (VeluxBindingConfig) super.bindingConfigs.get(itemName);
        }
        logger.trace("getConfigForItemName() returns {}.", config);
        return config;
    }

    @Override
    public Boolean autoUpdate(final String itemName) {
        logger.trace("autoUpdate({}) called, returning true.", itemName);
        return true;
    }

    @Override
    public List<String> getInBindingItemNames() {
        List<String> inBindings = new ArrayList<String>();
        for (String itemName : bindingConfigs.keySet()) {
            inBindings.add(itemName);
        }
        logger.trace("getInBindingItemNames() returns {}.", inBindings);
        return inBindings;
    }

    @Override
    public void removeConfigurations(String context) {
        logger.trace("removeConfigurations({}) called.", context);
        Set<String> itemNames = contextMap.get(context);
        if (itemNames != null) {
            for (String itemName : itemNames) {
                logger.debug("removeConfigurations() removing {}.", itemName);
                itemMap.remove(itemName);
            }
            contextMap.remove(context);
        }
        logger.trace("removeConfigurations() done.");
    }

}

/**
 * end-of-internal/VeluxGenericBindingProvider.java
 */

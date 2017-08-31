/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.powerdoglocalapi.PowerDogLocalApiBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class parses the EcoData PowerDog LocalAPI item binding data. It
 * registers as a {@link PowerDogLocalAPIGenericBindingProvider} service as
 * well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li>
 * <code>{ powerdoglocalapi="<serverId:arithmetic_1234567890:300000" }</code></li>
 * <li><code>{ powerdoglocalapi="<powerdog:pv_global_1234567890:300000" }</code>
 * </li>
 * <li>
 * <code>{ powerdoglocalapi="<powerdog:pv_global_1234567890:300000:Current_Value" }</code>
 * </li>
 * <li>
 * <code>{ powerdoglocalapi="<powerdog:impulsecounter_1234567890:300000:Unit_1000000" }</code>
 * </li>
 * <li><code>{ powerdoglocalapi=">powerdog:powerapi_1234567890:300000" }</code></li>
 * </ul>
 * 
 * The 'serverId' referenced in the binding string is configured in the
 * openhab.cfg file -: powerdoglocalapi:serverId.host = powerdog
 * 
 * 'serverId' can be any alphanumeric string as long as it is the same in the
 * binding and configuration file. <b>NOTE</b>: The parameter is case sensitive!
 * 
 * @author wuellueb
 * @since 1.9.0
 */
public class PowerDogLocalApiGenericBindingProvider extends AbstractGenericBindingProvider
        implements PowerDogLocalApiBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(PowerDogLocalApiGenericBindingProvider.class);

    /** {@link Pattern} which matches a binding configuration part */
    private static final Pattern BASE_CONFIG_PATTERN = Pattern.compile("(<|>)([0-9._a-zA-Z]+:[0-9._a-zA-Z:]+)");

    /** {@link Pattern} which matches an In-Binding */
    private static final Pattern INNER_BINDING_PATTERN = Pattern
            .compile("([0-9._a-zA-Z]+):([0-9._a-zA-Z]+):([0-9]+)(:?[0-9._a-zA-Z]*)");

    /**
     * Artificial command for the PowerDog configuration
     */
    protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");
    protected static final Command OUT_BINDING_KEY = StringType.valueOf("OUT_BINDING");

    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "powerdoglocalapi";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ContactItem
                || item instanceof NumberItem || item instanceof StringItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch-, Dimmer-, Contact-, Number- and String-Items are allowed - please check your *.items configuration");
        }
        logger.debug("PowerDogLocalApi:validateItemType called");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        logger.debug("PowerDogLocalApi:processBindingConfiguration called");
        super.processBindingConfiguration(context, item, bindingConfig);

        // parse and accept binding configuration
        if (bindingConfig != null) {
            PowerDogLocalApiBindingConfig config = parseBindingConfig(item, bindingConfig);
            logger.debug("bindingConfig added (config={})", config.toString());
            addBindingConfig(item, config);
        } else {
            logger.warn("bindingConfig is NULL (item={}) -> process bindingConfig aborted!", item);
        }
    }

    /**
     * Delegates parsing the <code>bindingConfig</code> with respect to the
     * first character (<code>&lt;</code> or <code>&gt;</code>) to the
     * specialized parsing methods
     * 
     * @param item
     * @param bindingConfig
     * 
     * @throws BindingConfigParseException
     */
    protected PowerDogLocalApiBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {

        logger.debug("PowerDogLocalAPI:parseBindingConfig called");

        // create binding configuration
        PowerDogLocalApiBindingConfig config = new PowerDogLocalApiBindingConfig();
        config.itemType = item.getClass();

        // set regular expression
        Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);
        if (!matcher.matches()) {
            throw new BindingConfigParseException("PowerDogLocalAPI:bindingConfig '" + bindingConfig
                    + "' doesn't contain a valid binding configuration");
        }
        matcher.reset();

        // check for high level regular expression, if in- or out-binding is
        // asked
        while (matcher.find()) {
            String direction = matcher.group(1);
            String bindingConfigPart = matcher.group(2);

            if (direction.equals("<")) {
                // in-binding config line found
                config = parseInnerBindingConfig(item, bindingConfigPart, config, IN_BINDING_KEY);
            }
            //
            else if (direction.equals(">")) {
                // out-binding config line found
                config = parseInnerBindingConfig(item, bindingConfigPart, config, OUT_BINDING_KEY);
            } else {
                throw new BindingConfigParseException(
                        "Unknown command given! Configuration must start with '<' or '>' ");
            }
        }

        return config;
    }

    /**
     * Parses a PowerDog LocalAPI in configuration by using the regular
     * expression {@link INNER_BINDING_PATTERN}. Where the groups should contain
     * the following content:
     * <ul>
     * <li>1 - Server ID</li>
     * <li>2 - PowerDog Value ID</li>
     * <li>3 - Refresh Interval</li>
     * <li>4 - Variable name (optional, defaults to Current_Value)</li>
     * </ul>
     * 
     * @param item
     * @param bindingConfig
     *            the config string to parse
     * @param config
     * 
     * @return the filled {@link PowerDogLocalAPIBindingConfig}
     * @throws BindingConfigParseException
     *             if the regular expression doesn't match the given
     *             <code>bindingConfig</code>
     */
    protected PowerDogLocalApiBindingConfig parseInnerBindingConfig(Item item, String bindingConfig,
            PowerDogLocalApiBindingConfig config, Command key) throws BindingConfigParseException {

        logger.debug("PowerDogLocalAPI:parseInnerBindingConfig called");
        PowerDogLocalApiBindingConfigElement configElement;

        // Check if regex for in-binding matches
        Matcher matcher = INNER_BINDING_PATTERN.matcher(bindingConfig);
        if (!matcher.matches()) {
            throw new BindingConfigParseException("bindingConfig '" + bindingConfig
                    + "' doesn't represent a valid binding-configuration. A valid configuration is matched by the RegExp '"
                    + INNER_BINDING_PATTERN + "'");
        }
        matcher.reset();

        // parse regex and extract configuration data
        while (matcher.find()) {
            configElement = new PowerDogLocalApiBindingConfigElement();
            configElement.serverId = matcher.group(1);
            configElement.valueId = matcher.group(2);
            configElement.refreshInterval = Integer.valueOf(matcher.group(3));
            if (matcher.group(4).isEmpty()) { // group 4 is optional
                configElement.name = "Current_Value";
            } else {
                configElement.name = matcher.group(4).substring(1);
            }

            logger.debug("PowerDogLocalAPI: {}", configElement);
            config.put(key, configElement);
        }

        return config;
    }

    /**
     * @{inheritDoc
     */
    @Override
    public Class<? extends Item> getItemType(String itemName) {
        PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.itemType : null;
    }

    /**
     * {@inheritDoc}
     */
    public String getServerId(String itemName) {
        PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);

        String returnValue = null;
        if (config != null) {
            if (config.get(IN_BINDING_KEY) != null) {
                returnValue = config.get(IN_BINDING_KEY).serverId;
            } else if (config.get(OUT_BINDING_KEY) != null) {
                returnValue = config.get(OUT_BINDING_KEY).serverId;
            }
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    public String getValueId(String itemName) {
        PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);

        String returnValue = null;
        if (config != null) {
            if (config.get(IN_BINDING_KEY) != null) {
                returnValue = config.get(IN_BINDING_KEY).valueId;
            } else if (config.get(OUT_BINDING_KEY) != null) {
                returnValue = config.get(OUT_BINDING_KEY).valueId;
            }
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    public String getName(String itemName) {
        PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
        return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).name : null;
    }

    /**
     * {@inheritDoc}
     */
    public int getRefreshInterval(String itemName) {
        PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);

        int returnValue = 0;
        if (config != null) {
            if (config.get(IN_BINDING_KEY) != null) {
                returnValue = config.get(IN_BINDING_KEY).refreshInterval;
            } else if (config.get(OUT_BINDING_KEY) != null) {
                returnValue = config.get(OUT_BINDING_KEY).refreshInterval;
            }
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getInBindingItemNames() {
        List<String> inBindings = new ArrayList<String>();
        for (String itemName : bindingConfigs.keySet()) {
            PowerDogLocalApiBindingConfig pdConfig = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
            if (pdConfig.containsKey(IN_BINDING_KEY)) {
                inBindings.add(itemName);
            }
        }
        return inBindings;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getOutBindingItemNames() {
        List<String> outBindings = new ArrayList<String>();
        for (String itemName : bindingConfigs.keySet()) {
            PowerDogLocalApiBindingConfig pdConfig = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
            if (pdConfig.containsKey(OUT_BINDING_KEY)) {
                outBindings.add(itemName);
            }
        }
        return outBindings;
    }

    /**
     * This is a helper class holding binding specific configuration details to
     * map commands to {@link PowerDogLocalAPIBindingConfigElement }. There will
     * be a map like <code>ON->PowerDogLocalAPIBindingConfigElement</code>
     * 
     * @author wuellueb
     * @since 1.9.0
     */
    class PowerDogLocalApiBindingConfig extends HashMap<Command, PowerDogLocalApiBindingConfigElement>
            implements BindingConfig {
        private static final long serialVersionUID = -3746900828632519633L;
        Class<? extends Item> itemType;
    }

    /**
     * This is an internal data structure to store information from the binding
     * config strings and use it to answer the requests to the binding provider.
     */
    static class PowerDogLocalApiBindingConfigElement implements BindingConfig {
        public String serverId; // as used in the openhab configuration file
        public String valueId; // PowerDog value ID, e.g.
                               // 'impulsecounter_1234567890'
        public String name; // Parameter name, e.g. 'Current_Value' (not valid
                            // for out-binding)
        public int refreshInterval; // Refresh rate for the specific item, will
                                    // not be queried faster than set for the
                                    // corresponding PowerDog

        @Override
        public String toString() {
            return "PowerDogLocalAPIBindingConfigElement [serverId=" + serverId + ", valueId=" + valueId + ", name="
                    + name + ", refreshInterval=" + refreshInterval + "]";
        }
    }

}

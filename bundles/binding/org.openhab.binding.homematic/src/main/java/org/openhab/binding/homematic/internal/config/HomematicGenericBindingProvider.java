/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and provides Homematic binding information from it. It registers as a
 * {@link HomematicBindingProvider} service as well.
 * 
 * The syntax of the binding configuration strings accepted is the following: <br>
 * <br>
 * <b>Device items:</b><br>
 * <code>
 * 	homematic="{id=XXXXX, channel=X, parameter=XXXXXX}" (The {} brackets are optional)
 * </code> <br>
 * Examples:
 * <ul>
 * <li>
 * <code>homematic="homematic="{id=IEQ00XXXX, channel=1, parameter=TEMPERATURE}"</code></li>
 * <li>
 * <code>homematic="homematic="{id=IEQ00XXXX, channel=1, parameter=HUMIDITY}"</code></li>
 * </ul>
 * <br>
 * <br>
 * <b>Admin items:</b><br>
 * <code>
 *  homematic="{admin=command}"
 * </code><br>
 * Examples:
 * <ul>
 * <li><code>homematic="{admin=DUMP_UNCONFIGURED_DEVICES}"</code></li>
 * </ul>
 * The old format is deprecated, but still supported
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicGenericBindingProvider extends AbstractGenericBindingProvider implements HomematicBindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(HomematicGenericBindingProvider.class);

    private static final String PACKAGE_PREFIX_CONVERTERS = "org.openhab.binding.homematic.internal.converter.";
    private Map<String, Item> items = new HashMap<String, Item>();

    @Override
    public String getBindingType() {
        return "homematic";
    }

    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        HomematicBindingConfig config = new HomematicBindingConfig();
        BindingConfigParser parser = new BindingConfigParser();
        parser.parse(bindingConfig, config);
    }

    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        HomematicBindingConfig config = new HomematicBindingConfig();
        BindingConfigParser parser = new BindingConfigParser();
        parser.parse(bindingConfig, config);
        addBindingConfig(item, config);
    }

    @Override
    protected void addBindingConfig(Item item, BindingConfig config) {
        items.put(item.getName(), item);
        super.addBindingConfig(item, config);
    }

    @Override
    public void removeConfigurations(String context) {
        Set<Item> configuredItems = contextMap.get(context);
        if (configuredItems != null) {
            for (Item item : configuredItems) {
                items.remove(item.getName());
            }
        }
        super.removeConfigurations(context);
    }

    @Override
    public HomematicParameterAddress getParameterAddress(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        if (config == null) {
            return null;
        }
        return new HomematicParameterAddress(config.id, config.channel, config.parameter);
    }

    @Override
    public AdminItem getAdminItem(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        if (config == null || !isAdminItem(itemName)) {
            return null;
        }
        return new AdminItem(config.admin);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CONVERTER extends StateConverter<?, ?>> Class<CONVERTER> getConverter(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        if (config == null || StringUtils.isBlank(config.converter)) {
            return null;
        }
        String fullClassName = config.converter;
        if (!fullClassName.contains(".")) {
            fullClassName = PACKAGE_PREFIX_CONVERTERS + fullClassName;
        }
        Class<CONVERTER> converterClass = null;
        try {
            converterClass = (Class<CONVERTER>) Class.forName(fullClassName);
        } catch (Exception e) {
            logger.error("Could not instanciate covnerter " + config.converter, e);
        }
        return converterClass;
    }

    @Override
    public boolean isAdminItem(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        return (config != null && config.admin != null);
    }

    @Override
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    /**
     * This is an internal data structure to store information from the binding config strings and use it to answer the requests to the
     * Homematic binding provider.
     * 
     * @author Thomas Letsch (contact@thomas-letsch.de)
     */
    public static class HomematicBindingConfig implements BindingConfig {
        public String id;
        public String channel;
        public String parameter;
        public String converter;
        public String admin;
    }

}

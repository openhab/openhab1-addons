/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.opencean.core.address.EnoceanId;
import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.EEPId;
import org.openhab.binding.enocean.EnoceanBindingProvider;
import org.openhab.binding.enocean.internal.profiles.Profile;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and provides
 * Enocean binding information from it. It registers as a
 * {@link EnoceanBindingProvider} service as well.
 * 
 * The syntax of the binding configuration strings accepted is the following: <br>
 * <br>
 * enocean="{id="&lt;id_of_enocean_device&gt;" [, EEP = "&lt;EEP_name&gt;
 * "][, CHANNEL = "
 * &lt;channelName&gt;"][, PARAMETER = "&lt;parameterName&gt;"]}" The device id
 * is printed on the device package. When the EEP is needed, the eep (e.g.
 * "F6.02.01") is also printed on the device package. All parameters in [] are
 * optional and only used for some devices.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */
public class EnoceanGenericBindingProvider extends AbstractGenericBindingProvider implements EnoceanBindingProvider {

    private static final String PACKAGE_PREFIX_PROFILES = "org.openhab.binding.enocean.internal.profiles.";
    private static final Logger logger = LoggerFactory.getLogger(EnoceanGenericBindingProvider.class);
    private Map<String, Item> items = new HashMap<String, Item>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "enocean";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    }

    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        EnoceanBindingConfig config = new EnoceanBindingConfig();
        BindingConfigParser<EnoceanBindingConfig> parser = new BindingConfigParser<EnoceanBindingConfig>();
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
    public EnoceanParameterAddress getParameterAddress(String itemName) {
        EnoceanBindingConfig config = (EnoceanBindingConfig) bindingConfigs.get(itemName);
        return config != null ? new EnoceanParameterAddress(EnoceanId.fromString(config.id), config.channel, config.parameter) : null;
    }

    @Override
    public EEPId getEEP(String itemName) {
        EnoceanBindingConfig config = (EnoceanBindingConfig) bindingConfigs.get(itemName);
        return config != null ? new EEPId(config.eep) : null;
    }

    @Override
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    @Override
    public Class<Profile> getCustomProfile(String itemName) {
        EnoceanBindingConfig config = (EnoceanBindingConfig) bindingConfigs.get(itemName);
        if (config == null || config.profile == null) {
            return null;
        }
        String fullClassName = config.profile;
        if (!fullClassName.contains(".")) {
            fullClassName = PACKAGE_PREFIX_PROFILES + fullClassName;
        }
        Class<Profile> profileClass = null;
        try {
            profileClass = (Class<Profile>) Class.forName(fullClassName);
        } catch (Exception e) {
            logger.error("Could not create class for profile " + config.profile, e);
        }
        return profileClass;

    }

    /**
     * This is an internal data structure to store information from the binding
     * config strings and use it to answer the requests to the EnOcean binding
     * provider.
     * 
     * @author Thomas Letsch (contact@thomas-letsch.de)
     */
    public class EnoceanBindingConfig implements BindingConfig {
        public String id;
        public String channel;
        public String parameter;
        public String eep;
        public String profile;
    }

}

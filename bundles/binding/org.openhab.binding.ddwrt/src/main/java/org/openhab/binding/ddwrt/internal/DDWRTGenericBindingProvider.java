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
package org.openhab.binding.ddwrt.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.ddwrt.DDWRTBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and
 * provides DD-WRT binding information from it. It registers as a
 * {@link DDWRTBindingProvider} service as well.
 *
 * @author Kai Kreuzer
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */
public class DDWRTGenericBindingProvider extends AbstractGenericBindingProvider implements DDWRTBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(DDWRTGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "ddwrt";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof CallItem || item instanceof StringItem
                || item instanceof NumberItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only Switch- and CallItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        DDWRTBindingConfig config = parseBindingConfig(item, bindingConfig);
        addBindingConfig(item, config);

        super.processBindingConfiguration(context, item, bindingConfig);
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
     *
     * @param item
     * @param bindingConfig
     *
     * @throws BindingConfigParseException if bindingConfig is no valid binding type
     */
    protected DDWRTBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        if (ArrayUtils.contains(DDWRTBindingProvider.TYPES, bindingConfig)
                || ArrayUtils.contains(DDWRTBindingProvider.TYPES, bindingConfig.substring(0, 3))) {
            return new DDWRTBindingConfig(item.getClass(), bindingConfig);
        } else {
            throw new BindingConfigParseException("'" + bindingConfig + "' is not a valid binding type");
        }
    }

    @Override
    public Class<? extends Item> getItemType(String itemName) {
        DDWRTBindingConfig config = (DDWRTBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItemType() : null;
    }

    @Override
    public String getType(String itemName) {
        DDWRTBindingConfig config = (DDWRTBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getType() : null;
    }

    @Override
    public String[] getItemNamesForType(String eventType) {
        Set<String> itemNames = new HashSet<>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            DDWRTBindingConfig fbConfig = (DDWRTBindingConfig) entry.getValue();
            if (fbConfig.getType().equals(eventType)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    static class DDWRTBindingConfig implements BindingConfig {

        final private Class<? extends Item> itemType;
        final private String type;

        public DDWRTBindingConfig(Class<? extends Item> itemType, String type) {
            this.itemType = itemType;
            this.type = type;
        }

        public Class<? extends Item> getItemType() {
            return itemType;
        }

        public String getType() {
            return type;
        }
    }

}

/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ddwrt.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.ddwrt.ddwrtBindingProvider;
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
 * <p>
 * This class can parse information from the generic binding format and
 * provides ddwrt binding information from it. It registers as a
 * {@link ddwrtBindingProvider} service as well.
 * </p>
 *
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{ ddwrt="inbound" }</code> - receives status updates on incoming calls</li>
 * <li><code>{ ddwrt="outbound" }</code> - receives status updates on outgoing calls</li>
 * <li><code>{ ddwrt="active" }</code> - receives status updates on active calls</li>
 * </ul>
 * These binding configurations can be used on either SwitchItems or StringItems. For SwitchItems,
 * it will obviously receive ON at the beginning and OFF at the end. StringItems will be filled
 * with the external phone number or an empty string.
 * </p>
 *
 * @author Kai Kreuzer
 *
 * @since 0.1.0
 */
public class ddwrtGenericBindingProvider extends AbstractGenericBindingProvider implements ddwrtBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(ddwrtGenericBindingProvider.class);

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
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig != null) {
            ddwrtBindingConfig config = parseBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else {
            logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
        }
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
     *
     * @param item
     * @param bindingConfig
     *
     * @throws BindingConfigParseException if bindingConfig is no valid binding type
     */
    protected ddwrtBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        if (ArrayUtils.contains(ddwrtBindingProvider.TYPES, bindingConfig)
                || ArrayUtils.contains(ddwrtBindingProvider.TYPES, bindingConfig.substring(0, 3))) {
            return new ddwrtBindingConfig(item.getClass(), bindingConfig);
        } else {
            throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
        }
    }

    @Override
    public Class<? extends Item> getItemType(String itemName) {
        ddwrtBindingConfig config = (ddwrtBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItemType() : null;
    }

    @Override
    public String getType(String itemName) {
        ddwrtBindingConfig config = (ddwrtBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getType() : null;
    }

    @Override
    public String[] getItemNamesForType(String eventType) {
        Set<String> itemNames = new HashSet<>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            ddwrtBindingConfig fbConfig = (ddwrtBindingConfig) entry.getValue();
            if (fbConfig.getType().equals(eventType)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    static class ddwrtBindingConfig implements BindingConfig {

        final private Class<? extends Item> itemType;
        final private String type;

        public ddwrtBindingConfig(Class<? extends Item> itemType, String type) {
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

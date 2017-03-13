/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.wr3223.WR3223BindingProvider;
import org.openhab.binding.wr3223.WR3223CommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Michael Fraefel
 * @since 1.10.0
 */
public class WR3223GenericBindingProvider extends AbstractGenericBindingProvider implements WR3223BindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(WR3223GenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "wr3223";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        WR3223CommandType commandType = WR3223CommandType.fromString(bindingConfig);
        if (commandType != null) {
            if (WR3223CommandType.validateBinding(commandType, item.getClass())) {
                WR3223BindingConfig config = new WR3223BindingConfig(commandType);
                addBindingConfig(item, config);
            } else {
                throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
            }
        } else {
            logger.warn("bindingConfig is NULL (item={}) -> processing bindingConfig aborted!", item);
        }
    }

    @Override
    public String[] getItemNamesForType(WR3223CommandType eventType) {
        Set<String> itemNames = new HashSet<String>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            WR3223BindingConfig wr3223Config = (WR3223BindingConfig) entry.getValue();
            if (wr3223Config.getType().equals(eventType)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    @Override
    public WR3223CommandType getWR3223CommandTypeForItemName(String itemName) {
        WR3223BindingConfig config = (WR3223BindingConfig) bindingConfigs.get(itemName);
        if (config != null) {
            return config.type;
        }
        return null;
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Michael Fraefel
     * @since 1.8.0
     */
    class WR3223BindingConfig implements BindingConfig {
        private WR3223CommandType type;

        public WR3223BindingConfig(WR3223CommandType type) {
            super();
            this.type = type;
        }

        public WR3223CommandType getType() {
            return type;
        }

    }

}

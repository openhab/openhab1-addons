/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

import org.openhab.binding.hideki.HidekiBindingConfig;
import org.openhab.binding.hideki.HidekiBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public class HidekiGenericBindingProvider extends AbstractGenericBindingProvider implements HidekiBindingProvider {
    private static final Logger logger = LoggerFactory.getLogger(HidekiBinding.class);

    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "hideki";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Item getItem(String itemName) {
        HidekiBindingConfig config = (HidekiBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItem() : null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // @TODO may add additional checking based on the memloc
        if (!(item instanceof NumberItem)) {
            throw new BindingConfigParseException("item '" + item.getName() +
                    "' is of type '" + item.getClass().getSimpleName() + "', " +
                    "only Number items are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        // the config string has the format "sensor:value"
        String[] config = bindingConfig.split(":");
        if (config.length != 2) {
            throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + "should be sensor:value");
        }

        addBindingConfig(item, new HidekiBindingConfig(item, config[0], config[1]));
    }

    @Override
    public HidekiBindingConfig getBindingConfig(String itemName) {
        return (HidekiBindingConfig) this.bindingConfigs.get(itemName);

    }

}

/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import org.openhab.binding.plclogo.PLCLogoBindingConfig;
import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Lehane Kellett
 * @since 1.9.0
 */
public class PLCLogoGenericBindingProvider extends AbstractGenericBindingProvider implements PLCLogoBindingProvider {
    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    @Override
    public String getBindingType() {
        return "plclogo";
    }

    @Override
    public Item getItem(String itemName) {
        PLCLogoBindingConfig config = (PLCLogoBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItem() : null;
    }

    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // @TODO may add additional checking based on the memloc
        if (!(item instanceof SwitchItem || item instanceof ContactItem || item instanceof NumberItem)) {
            String itemName = item.getName();
            String className = item.getClass().getSimpleName();
            logger.error("Item '{}' is of type '{}', only Contact, Switch and Number items are allowed", itemName,
                    className);
            throw new BindingConfigParseException("item '" + itemName + "' is of type '" + className
                    + "', only Contact, Switch and Number are allowed");
        }
    }

    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        addBindingConfig(item, new PLCLogoBindingConfig(item, bindingConfig));
    }

    @Override
    public PLCLogoBindingConfig getBindingConfig(String itemName) {
        return (PLCLogoBindingConfig) this.bindingConfigs.get(itemName);
    }
}

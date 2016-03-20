/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.powermax.PowerMaxBindingConfig;
import org.openhab.binding.powermax.PowerMaxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author lolodomo
 * @since 1.9.0
 */
public class PowerMaxGenericBindingProvider extends AbstractGenericBindingProvider implements PowerMaxBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "powermax";
    }

    /**
     * {@inheritDoc}
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

        String selector[] = StringUtils.split(StringUtils.trim(bindingConfig), ':');
        try {
            PowerMaxSelectorType selectorType = PowerMaxSelectorType.fromString(selector[0]);
            String selectorParam = null;
            if (selector.length == 2) {
                selectorParam = selector[1];
            }
            PowerMaxBindingConfig config = new PowerMaxBindingConfig(selectorType, selectorParam, item);

            addBindingConfig(item, config);
        } catch (IllegalArgumentException e) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "': parse error of string " + bindingConfig);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxBindingConfig getConfig(String itemName) {
        return (PowerMaxBindingConfig) this.bindingConfigs.get(itemName);
    }

}

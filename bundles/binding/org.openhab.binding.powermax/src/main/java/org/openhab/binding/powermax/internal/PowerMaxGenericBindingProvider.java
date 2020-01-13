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
 * @author Laurent Garnier
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

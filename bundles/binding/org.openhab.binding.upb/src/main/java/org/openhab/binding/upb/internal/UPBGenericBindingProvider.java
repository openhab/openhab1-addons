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
package org.openhab.binding.upb.internal;

import org.openhab.binding.upb.UPBBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class UPBGenericBindingProvider extends AbstractGenericBindingProvider implements UPBBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "UPB";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        // parse bindingconfig here ...
        String[] properties = bindingConfig.split(" ");
        UPBBindingConfig config = new UPBBindingConfig(properties, item instanceof DimmerItem);

        if (config.getId() == null) {
            throw new BindingConfigParseException("item config must have an id value");
        }
        addBindingConfig(item, config);
    }

    @Override
    public UPBBindingConfig getConfig(String itemName) {
        if (itemName != null) {
            return (UPBBindingConfig) bindingConfigs.get(itemName);
        } else {
            return null;
        }
    }
}

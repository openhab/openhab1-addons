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
package org.openhab.binding.garadget.internal;

import org.openhab.binding.garadget.GaradgetBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * The binding provider implementation for the Garadget binding.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetGenericBindingProvider extends AbstractGenericBindingProvider implements GaradgetBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "garadget";
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
        GaradgetBindingConfig config = new GaradgetBindingConfig(item, bindingConfig);
        addBindingConfig(item, config);
        super.processBindingConfiguration(context, item, bindingConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GaradgetBindingConfig getItemBindingConfig(String itemName) {
        return (GaradgetBindingConfig) bindingConfigs.get(itemName);
    }

    /**
     * The Garadget binding doesn't use autoUpdate as we only update state on poll.
     */
    @Override
    public Boolean autoUpdate(String itemName) {
        if (providesBindingFor(itemName)) {
            return false;
        } else {
            return null;
        }
    }
}

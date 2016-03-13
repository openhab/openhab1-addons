/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.items.Item;

/**
 * StateConverterLookupByCustomConverter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class StateConverterLookupByCustomConverter implements StateConverterLookup {

    private Map<String, StateConverter<?, ?>> customConverters = new HashMap<String, StateConverter<?, ?>>();

    public void addCustomConverter(String itemName, Class<? extends StateConverter<?, ?>> customConverter) {
        customConverters.put(itemName, ConverterInstanciation.instantiate(customConverter));
    }

    @Override
    public StateConverterMap getStateToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        if (!customConverters.containsKey(item.getName())) {
            return null;
        }
        return new StateConverterMap(customConverters.get(item.getName()));
    }

    @Override
    public StateConverter<?, ?> getBindingValueToStateConverter(Item item, ParameterAddress parameterAddress) {
        return customConverters.get(item.getName());
    }

    @Override
    public CommandConverterMap getCommandToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        return null;
    }

}

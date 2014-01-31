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
import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConverterLookup.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class ConverterLookup {

    private static final Logger logger = LoggerFactory.getLogger(ConverterLookup.class);

    private StateConverterLookup converterLookupByParameterId;
    private Map<String, ItemConverter> itemConverterMap = new HashMap<String, ItemConverter>();

    private StateConverterLookupByCustomConverter converterLookupByCustomConverter;

    private StateConverterLookupByConfiguredDevices stateConverterLookupByConfiguredDevices;

    public StateConverter<?, ?> getBindingValueToStateConverter(String itemName) {
        logger.debug("getBindingValueToStateConverter({})", itemName);
        ItemConverter itemConverter = itemConverterMap.get(itemName);
        if (itemConverter == null) {
            logger.warn("No ItemConverter found for item {}", itemName);
            return null;
        }
        StateConverter<?, ?> converter = itemConverter.getBindingValueToStateConverter();
        logger.debug("getBindingValueToStateConverter({}): {}", itemName, converter);
        return converter;
    }

    public StateConverter<?, ?> getStateToBindingValueConverter(String itemName, Class<? extends State> stateClass) {
        logger.debug("getStateToBindingValueConverter({}, {})", itemName, stateClass);
        ItemConverter itemConverter = itemConverterMap.get(itemName);
        StateConverter<?, ?> converter = itemConverter.getStateToBindingValueConverter(stateClass);
        logger.debug("getStateToBindingValueConverter({}, {}): {}", itemName, stateClass, converter);
        return converter;
    }

    public CommandConverter<?, ?> getCommandToBindingValueConverter(String itemName, Class<? extends Command> commandClass) {
        logger.debug("getCommandToBindingValueConverter({}, {})", itemName, commandClass);
        ItemConverter itemConverter = itemConverterMap.get(itemName);
        CommandConverter<?, ?> converter = itemConverter.getCommandToBindingValueConverter(commandClass);
        logger.debug("getCommandToBindingValueConverter({}, {}): {}", itemName, commandClass, converter);
        return converter;
    }

    public void configureItem(Item item, ParameterAddress parameterAddress) {
        logger.debug("Configuring item {} on address {}", item.getName(), parameterAddress);
        if (!itemConverterMap.containsKey(item.getName())) {
            itemConverterMap.put(item.getName(), new ItemConverter());
        }
        ItemConverter itemConverter = itemConverterMap.get(item.getName());
        setBindingValueToStateConverter(item, parameterAddress, itemConverter);
        setStateToBindingValueConverter(item, parameterAddress, itemConverter);
        setCommandToBindingValueConverter(item, parameterAddress, itemConverter);
    }

    private void setCommandToBindingValueConverter(Item item, ParameterAddress parameterAddress, ItemConverter itemConverter) {
        CommandConverterMap converterMap = converterLookupByParameterId.getCommandToBindingValueConverter(item, parameterAddress);
        itemConverter.setCommandToBindingValueConverter(converterMap);
        logger.debug("CommandConverterMap {} initialised.", converterMap);
    }

    private void setStateToBindingValueConverter(Item item, ParameterAddress parameterAddress, ItemConverter itemConverter) {
        StateConverterMap toBindingValueConverter;
        if (converterLookupByCustomConverter.getStateToBindingValueConverter(item, parameterAddress) != null) {
            toBindingValueConverter = converterLookupByCustomConverter.getStateToBindingValueConverter(item, parameterAddress);
        } else if (stateConverterLookupByConfiguredDevices.getStateToBindingValueConverter(item, parameterAddress) != null) {
            toBindingValueConverter = stateConverterLookupByConfiguredDevices.getStateToBindingValueConverter(item, parameterAddress);
        } else {
            toBindingValueConverter = converterLookupByParameterId.getStateToBindingValueConverter(item, parameterAddress);
        }
        itemConverter.setStateToBindingValueConverter(toBindingValueConverter);
        logger.debug("StateConverterMap {} initialised for StateToBinding.", toBindingValueConverter);
    }

    private void setBindingValueToStateConverter(Item item, ParameterAddress parameterAddress, ItemConverter itemConverter) {
        StateConverter<?, ?> toStateConverter;
        if (converterLookupByCustomConverter.getBindingValueToStateConverter(item, parameterAddress) != null) {
            toStateConverter = converterLookupByCustomConverter.getBindingValueToStateConverter(item, parameterAddress);
        } else if (stateConverterLookupByConfiguredDevices.getBindingValueToStateConverter(item, parameterAddress) != null) {
            toStateConverter = stateConverterLookupByConfiguredDevices.getBindingValueToStateConverter(item, parameterAddress);
        } else {
            toStateConverter = converterLookupByParameterId.getBindingValueToStateConverter(item, parameterAddress);
        }
        itemConverter.setBindingValueToStateConverter(toStateConverter);
        logger.debug("StateConverterMap {} initialised for BindingToState.", toStateConverter);
    }

    public void setConverterLookupByParameterId(StateConverterLookup converterLookupByParameterId) {
        this.converterLookupByParameterId = converterLookupByParameterId;
    }

    public void setConverterLookupByCustomConverter(StateConverterLookupByCustomConverter converterLookupByCustomConverter) {
        this.converterLookupByCustomConverter = converterLookupByCustomConverter;
    }

    public void setConverterLookupByConfiguredDevices(StateConverterLookupByConfiguredDevices stateConverterLookupByConfiguredDevices) {
        this.stateConverterLookupByConfiguredDevices = stateConverterLookupByConfiguredDevices;

    }

}

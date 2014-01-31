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
import java.util.List;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.config.ConfiguredChannel;
import org.openhab.binding.homematic.internal.config.ConfiguredConverter;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.ConfiguredParameter;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StateConverterLookupByConfiguredDevices.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class StateConverterLookupByConfiguredDevices extends HashMap<String, StateConverterMap> implements StateConverterLookup {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StateConverterLookupByConfiguredDevices.class);

    private static final String OPEN_HAB_TYPES_PACKAGE = "org.openhab.core.library.types";

    private HashMap<String, StateConverterLookupByParameterId> configuredDevicesLookup = new HashMap<String, StateConverterLookupByParameterId>();
    private CCU<?> ccu;

    public StateConverterLookupByConfiguredDevices() {
    }

    public StateConverterLookupByConfiguredDevices(CCU<?> ccu) {
        this.ccu = ccu;
    }

    /**
     * Adds configured devices usually coming from a list of parsed xml files.
     * 
     * @param configuredDevices
     */
    @SuppressWarnings("unchecked")
    public void addConfiguredDevices(List<ConfiguredDevice> configuredDevices) {
        for (ConfiguredDevice configuredDevice : configuredDevices) {
            ConfiguredChannel configuredChannel = configuredDevice.getChannels().get(0);
            StateConverterLookupByParameterId parameterLookup = new StateConverterLookupByParameterId();
            configuredDevicesLookup.put(configuredDevice.getName(), parameterLookup);
            for (ConfiguredParameter configuredParameter : configuredChannel.getParameter()) {
                Class<? extends State> openHABType;
                try {
                    StateConverterMap converterMap = new StateConverterMap();
                    for (ConfiguredConverter configuredConverter : configuredParameter.getConverter()) {
                        Class<StateConverter<?, ?>> converterClass;
                        converterClass = (Class<StateConverter<?, ?>>) Class.forName(configuredConverter.getClassName());
                        String forType = configuredConverter.getForType();
                        if (!forType.contains(".")) {
                            forType = OPEN_HAB_TYPES_PACKAGE + "." + forType;
                        }
                        openHABType = (Class<? extends State>) Class.forName(forType);
                        converterMap.add(openHABType, ConverterInstanciation.instantiate(converterClass));
                    }
                    parameterLookup.put(configuredParameter.getName(), converterMap);
                } catch (ClassNotFoundException e) {
                    logger.error("Could not import configuredDevices", e);
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public StateConverterMap getStateToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        String type = ccu.getPhysicalDevice(parameterAddress.getDeviceId()).getDeviceDescription().getType();
        if (!configuredDevicesLookup.containsKey(type)) {
            return null;
        }
        StateConverterLookupByParameterId stateConverterLookupByParameterId = configuredDevicesLookup.get(type);
        StateConverterMap stateToBindingValueConverter = stateConverterLookupByParameterId.getStateToBindingValueConverter(item,
                parameterAddress);
        if (type.startsWith("HM-LC-Bl1")) {
            HomematicParameterAddress newAddress = new HomematicParameterAddress(parameterAddress.getDeviceId(),
                    parameterAddress.getChannelId(), "STOP");
            StateConverterMap additionalConverter = stateConverterLookupByParameterId.getStateToBindingValueConverter(item, newAddress);
            stateToBindingValueConverter.addAll(additionalConverter);
        }
        return stateToBindingValueConverter;
    }

    @Override
    public StateConverter<?, ?> getBindingValueToStateConverter(Item item, ParameterAddress parameterAddress) {
        String type = ccu.getPhysicalDevice(parameterAddress.getDeviceId()).getDeviceDescription().getType();
        if (!configuredDevicesLookup.containsKey(type)) {
            return null;
        }
        return configuredDevicesLookup.get(type).getBindingValueToStateConverter(item, parameterAddress);
    }

    public void setCcu(CCU<?> ccu) {
        this.ccu = ccu;
    }

    @Override
    public CommandConverterMap getCommandToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        // TODO Auto-generated method stub
        return null;
    }
}

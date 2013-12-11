/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.config.ConfiguredChannel;
import org.openhab.binding.homematic.internal.config.ConfiguredConverter;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.ConfiguredParameter;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the factory to get the converter for a state or a command. It creates
 * converters for {@link Command}s and for {@link State}s in both directions.
 * The {@link #getFromStateConverter(String, State)} returns an converter for
 * converting a {@link State} into a protocol value, the
 * {@link #getFromStateConverter(String, State)} a converter for converting a
 * protocol value into a {@link State}. These can be the same class.
 * 
 * The normal usage has three parts:
 * 
 * Instantiate it:
 * <code>private ConverterFactory converterFactory = new ConverterFactory();</code>
 * 
 * Configure it:
 * <code>converterFactory.addCommandConverter("LEVEL", OnOffType.class, OnOffPercentageCommandConverter.class);</code>
 * <code>converterFactory.addStateConverter("LEVEL", PercentType.class, DoublePercentageConverter.class);</code>
 * 
 * Use it:
 * <code>StateConverter<?, ?> converter = converterFactory.getFromStateConverter(parameterAddress.getParameterKey(), newState);
 *            Object value = converter.convertFrom(newState);
 * </code>
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class ConverterFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConverterFactory.class);

    private static final String OPEN_HAB_TYPES_PACKAGE = "org.openhab.core.library.types";

    private MatchingConverters converters = new MatchingConverters();

    private Map<String, Class<? extends StateConverter<?, ?>>> customConverters = new HashMap<String, Class<? extends StateConverter<?, ?>>>();

    private Map<String, MatchingConverters> configuredDevicesConverters = new HashMap<String, MatchingConverters>();

    private CCU<? extends HMPhysicalDevice> ccu;

    public ConverterFactory(CCU<? extends HMPhysicalDevice> ccu) {
        this.ccu = ccu;
    }

    public ConverterFactory() {
    }

    /**
     * Adds a new {@link StateConverter} for the parameterId.
     * 
     * @param parameterId
     *            The value key for the binding specific protocol.
     * @param state
     *            The state it can convert.
     * @param converter
     *            The actual converter.
     */
    public void addStateConverter(String parameterId, Class<? extends State> state, Class<? extends StateConverter<?, ?>> converter) {
        converters.addStateConverter(parameterId, state, converter);
    }

    /**
     * Adds a new {@link CommandConverter} for the parameterId.
     * 
     * @param parameterId
     *            The value key for the binding specific protocol.
     * @param command
     *            The {@link Command} it can convert.
     * @param converter
     *            The actual converter.
     */
    public void addCommandConverter(String parameterId, Class<? extends Command> command, Class<? extends CommandConverter<?, ?>> converter) {
        converters.addCommandConverter(parameterId, command, converter);
    }

    public void addCustomConverter(HomematicParameterAddress parameterAddress, Class<? extends StateConverter<?, ?>> customConverter) {
        customConverters.put(parameterAddress.getAsString(), customConverter);
    }

    /**
     * Returns the first matching converter for the given parameterId and the
     * item. It considers the possible types (states) the item can accept.
     * 
     * This method is to be used for getting a converter for the direction from
     * a parameterId to a State.
     * 
     * @param parameterId
     *            The value key for the binding specific protocol.
     * @param item
     *            The item for the converter.
     * @return A new {@link StateConverter} to convert a value of the
     *         protocolKey to a state for the item.
     */
    public StateConverter<?, ?> getToStateConverter(HomematicParameterAddress parameterAddress, Item item) {
        Class<?> stateConverter;
        String type = ccu.getPhysicalDevice(parameterAddress.getDeviceId()).getDeviceDescription().getType();

        if (customConverters.containsKey(parameterAddress.getAsString())) {
            stateConverter = customConverters.get(parameterAddress.getAsString());
        } else if (configuredDevicesConverters.containsKey(type)) {
            stateConverter = getMatchingConverter(parameterAddress, item, configuredDevicesConverters.get(type));
        } else {
            stateConverter = getMatchingConverter(parameterAddress, item, converters);
        }
        if (stateConverter == null) {
            return null;
        }
        return (StateConverter<?, ?>) instantiate(stateConverter);
    }

    private Class<?> getMatchingConverter(HomematicParameterAddress parameterAddress, Item item, MatchingConverters matchingConverters) {
        Class<?> stateConverter;
        List<Class<? extends State>> acceptedTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());
        acceptedTypes.retainAll(matchingConverters.getMatchingStates(parameterAddress.getParameterId()));
        if (acceptedTypes.isEmpty()) {
            return null;
        }
        // Take best matching as accepted Type. Best matching is calculated
        // by ordering by importance of state.
        Collections.sort(acceptedTypes, new StateComparator());
        Class<? extends State> acceptedType = acceptedTypes.get(acceptedTypes.size() - 1);
        stateConverter = matchingConverters.getStateConverter(parameterAddress.getParameterId(), acceptedType);
        return stateConverter;
    }

    /**
     * Returns the first matching converter for the given parameterId and the
     * state.
     * 
     * This method is to be used for getting a converter for the direction from
     * a state (the given state to be exact) to a parameterId.
     * 
     * @param parameterId
     *            The parameter id for the binding specific protocol.
     * @param state
     *            The state to convert.
     * @return A new {@link StateConverter} to convert a value of the
     *         protocolKey to a state for the item.
     */
    public <STATE extends State> StateConverter<?, ?> getFromStateConverter(HomematicParameterAddress parameterAddress, STATE state) {
        Class<?> stateConverter;
        String type = ccu.getPhysicalDevice(parameterAddress.getDeviceId()).getDeviceDescription().getType();
        if (customConverters.containsKey(parameterAddress.getAsString())) {
            stateConverter = customConverters.get(parameterAddress.getAsString());
        } else if (configuredDevicesConverters.containsKey(type)) {
            stateConverter = configuredDevicesConverters.get(type).getStateConverter(parameterAddress.getParameterId(), state.getClass());
        } else {
            stateConverter = converters.getStateConverter(parameterAddress.getParameterId(), state.getClass());
            if (stateConverter == null) {
                return null;
            }
        }
        return (StateConverter<?, ?>) instantiate(stateConverter);
    }

    /**
     * Returns the first matching converter for the given parameterId and the
     * command.
     * 
     * This method is to be used for getting a converter for the direction from
     * a command (the given command to be exact) to a state.
     * 
     * @param parameterId
     *            The parameter id for the binding specific protocol.
     * @param command
     *            The command to convert.
     * @return A new {@link CommandConverter} to convert a command to a state.
     */
    public <COMMAND extends Command> CommandConverter<?, ?> getCommandConverter(HomematicParameterAddress parameterAddress, COMMAND command) {
        Class<?> commandConverter = converters.getCommandConverter(parameterAddress.getParameterId(), command.getClass());
        if (commandConverter == null && command instanceof State) {
            // If the command is also a State, return a converter that just
            // returns the State
            commandConverter = StateCommandConverter.class;
        }
        return (CommandConverter<?, ?>) instantiate(commandConverter);
    }

    private Object instantiate(Class<?> converter) {
        if (converter == null) {
            return null;
        }
        try {
            return converter.newInstance();
        } catch (InstantiationException e) {
            logger.error("Could not instanciate " + converter, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("Could not instanciate " + converter, e);
            throw new RuntimeException(e);
        }
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
            MatchingConverters matchingConverter = new MatchingConverters();
            configuredDevicesConverters.put(configuredDevice.getName(), matchingConverter);
            for (ConfiguredParameter configuredParameter : configuredChannel.getParameter()) {
                Class<? extends StateConverter<?, ?>> converter;
                Class<? extends State> openHABType;
                try {
                    for (ConfiguredConverter configuredConverter : configuredParameter.getConverter()) {
                        converter = (Class<? extends StateConverter<?, ?>>) Class.forName(configuredConverter.getClassName());
                        String forType = configuredConverter.getForType();
                        if (!forType.contains(".")) {
                            forType = OPEN_HAB_TYPES_PACKAGE + "." + forType;
                        }
                        openHABType = (Class<? extends State>) Class.forName(forType);
                        matchingConverter.addStateConverter(configuredParameter.getName(), openHABType, converter);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("Could not import configuredDevices", e);
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void setCcu(CCU<? extends HMPhysicalDevice> ccu) {
        this.ccu = ccu;
    }
}

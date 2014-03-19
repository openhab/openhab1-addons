/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * @since 1.3.0
 * 
 */
public class ConverterFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConverterFactory.class);

    private MatchingConverters converters = new MatchingConverters();

    /**
     * Adds a new {@link StateConverter} for the protocolValue.
     * 
     * @param protocolValue
     *            The value key for the binding specific protocol.
     * @param state
     *            The state it can convert.
     * @param converter
     *            The actual converter.
     */
    public void addStateConverter(String protocolValue, Class<? extends State> state, Class<? extends StateConverter<?, ?>> converter) {
        converters.addStateConverter(protocolValue, state, converter);
    }

    /**
     * Adds a new {@link CommandConverter} for the protocolValue.
     * 
     * @param protocolValue
     *            The value key for the binding specific protocol.
     * @param command
     *            The {@link Command} it can convert.
     * @param converter
     *            The actual converter.
     */
    public void addCommandConverter(String protocolValue, Class<? extends Command> command,
            Class<? extends CommandConverter<?, ?>> converter) {
        converters.addCommandConverter(protocolValue, command, converter);
    }

    /**
     * Returns the first matching converter for the given protocolValue and the
     * item. It considers the possible types (states) the item can accept.
     * 
     * This method is to be used for getting a converter for the direction from
     * a protocolValue to a State.
     * 
     * @param protocolValue
     *            The value key for the binding specific protocol.
     * @param item
     *            The item for the converter.
     * @return A new {@link StateConverter} to convert a value of the
     *         protocolKey to a state for the item.
     */
    public StateConverter<?, ?> getToStateConverter(String protocolValue, Item item) {
        List<Class<? extends State>> acceptedTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());
        acceptedTypes.retainAll(converters.getMatchingStates(protocolValue));
        if (acceptedTypes.isEmpty()) {
            return null;
        }
        // Take best matching as accepted Type. Best matching is calculated by
        // ordering by importance of state.
        Collections.sort(acceptedTypes, new StateComparator());
        Class<? extends State> acceptedType = acceptedTypes.get(acceptedTypes.size() - 1);
        return (StateConverter<?, ?>) instantiate(converters.getStateConverter(protocolValue, acceptedType));
    }

    /**
     * Returns the first matching converter for the given protocolValue and the
     * state.
     * 
     * This method is to be used for getting a converter for the direction from
     * a state (the given state to be exact) to a protocolValue.
     * 
     * @param protocolValue
     *            The value key for the binding specific protocol.
     * @param state
     *            The state to convert.
     * @return A new {@link StateConverter} to convert a value of the
     *         protocolKey to a state for the item.
     */
    public <STATE extends State> StateConverter<?, ?> getFromStateConverter(String protocolValue, STATE state) {
        Class<?> stateConverter = converters.getStateConverter(protocolValue, state.getClass());
        if (stateConverter == null) {
            return null;
        }
        return (StateConverter<?, ?>) instantiate(stateConverter);
    }

    /**
     * Returns the first matching converter for the given protocolValue and the
     * command.
     * 
     * This method is to be used for getting a converter for the direction from
     * a command (the given command to be exact) to a state.
     * 
     * @param protocolValue
     *            The value key for the binding specific protocol.
     * @param command
     *            The command to convert.
     * @return A new {@link CommandConverter} to convert a command to a state.
     */
    public <COMMAND extends Command> CommandConverter<?, ?> getCommandConverter(String protocolValue, COMMAND command) {
        Class<?> commandConverter = converters.getCommandConverter(protocolValue, command.getClass());
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

}

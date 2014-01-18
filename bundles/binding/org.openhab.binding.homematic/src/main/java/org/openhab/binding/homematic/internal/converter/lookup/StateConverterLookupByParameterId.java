/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * StateConverterLookupByParameterId.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class StateConverterLookupByParameterId extends HashMap<String, StateConverterMap> implements StateConverterLookup {

    private static final long serialVersionUID = 1L;

    private Map<String, CommandConverterMap> commandConverter = new HashMap<String, CommandConverterMap>();

    public void addStateConverter(String parameterId, Class<? extends State> state, Class<? extends StateConverter<?, ?>> converter) {
        if (!containsKey(parameterId)) {
            put(parameterId, new StateConverterMap());
        }
        StateConverterMap converterList = get(parameterId);
        converterList.add(state, ConverterInstanciation.instantiate(converter));
    }

    public void addCommandConverter(String parameterId, Class<? extends Command> command, Class<? extends CommandConverter<?, ?>> converter) {
        if (!commandConverter.containsKey(parameterId)) {
            commandConverter.put(parameterId, new CommandConverterMap());
        }
        CommandConverterMap converterList = commandConverter.get(parameterId);
        converterList.add(command, ConverterInstanciation.instantiate(converter));
    }

    @Override
    public StateConverterMap getStateToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        StateConverterMap stateConverterMap = get(parameterAddress.getParameterId());
        return stateConverterMap;
    }

    @Override
    public StateConverter<?, ?> getBindingValueToStateConverter(Item item, ParameterAddress parameterAddress) {
        List<Class<? extends State>> acceptedTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());
        acceptedTypes.addAll(getCommandStates(item));
        StateConverterMap stateConverterMap = get(parameterAddress.getParameterId());
        if (stateConverterMap == null) {
            return null;
        }
        StateConverter<?, ?> toStateConverter = matchItemTypesAndConverters(acceptedTypes, stateConverterMap);
        return toStateConverter;
    }

    private List<Class<? extends State>> getCommandStates(Item item) {
        List<Class<? extends State>> acceptedTypes = new ArrayList<Class<? extends State>>();
        for (Class<? extends Command> commandClass : item.getAcceptedCommandTypes()) {
            if (commandClass.isAssignableFrom(State.class)) {
                acceptedTypes.add((Class<State>) commandClass);
            }
        }
        return acceptedTypes;
    }

    private StateConverter<?, ?> matchItemTypesAndConverters(List<Class<? extends State>> itemAcceptedTypes,
            StateConverterMap stateConverterMap) {
        List<Class<? extends State>> acceptedTypes = new ArrayList<Class<? extends State>>(itemAcceptedTypes);
        acceptedTypes.retainAll(stateConverterMap.keySet());
        if (acceptedTypes.isEmpty()) {
            return null;
        }
        // Take best matching as accepted Type. Best matching is calculated
        // by ordering by importance of state.
        Collections.sort(acceptedTypes, new StateComparator());
        Class<? extends State> acceptedType = acceptedTypes.get(acceptedTypes.size() - 1);
        return stateConverterMap.get(acceptedType);
    }

    @Override
    public CommandConverterMap getCommandToBindingValueConverter(Item item, ParameterAddress parameterAddress) {
        CommandConverterMap commandConverterMap = commandConverter.get(parameterAddress.getParameterId());
        return commandConverterMap;
    }

}

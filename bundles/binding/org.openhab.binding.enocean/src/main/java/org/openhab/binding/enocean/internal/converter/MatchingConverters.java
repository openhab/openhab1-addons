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
import java.util.HashMap;
import java.util.List;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Map of all stateConverters and commandConverters. The map keys are in both
 * maps the protocolValue.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class MatchingConverters {

    private static final Logger logger = LoggerFactory.getLogger(MatchingConverters.class);

    private HashMap<String, StateToConverterMap> stateConverters = new HashMap<String, StateToConverterMap>();
    private HashMap<String, CommandToConverterMap> commandConverters = new HashMap<String, CommandToConverterMap>();

    public void addCommandConverter(String protocolValue, Class<? extends Command> command,
            Class<? extends CommandConverter<?, ?>> converter) {
        if (!commandConverters.containsKey(protocolValue)) {
            commandConverters.put(protocolValue, new CommandToConverterMap());
        }
        CommandToConverterMap parameterConverters = commandConverters.get(protocolValue);
        parameterConverters.put(command, converter);
    }

    @SuppressWarnings("unchecked")
    public <COMMAND extends Command> Class<? extends CommandConverter<?, COMMAND>> getCommandConverter(String protocolValue,
            Class<COMMAND> command) {
        CommandToConverterMap commandToConverterMap = commandConverters.get(protocolValue);
        if (commandToConverterMap == null) {
            logger.debug("ProtocolValue " + protocolValue + " is not configured. If the command is a state this will result in no error.");
            return null;
        }
        return (Class<? extends CommandConverter<?, COMMAND>>) commandToConverterMap.get(command);
    }

    public List<Class<? extends Command>> getMatchingCommands(String protocolValue) {
        return new ArrayList<Class<? extends Command>>(commandConverters.get(protocolValue).keySet());
    }

    public void addStateConverter(String protocolValue, Class<? extends State> openHABType, Class<? extends StateConverter<?, ?>> converter) {
        if (!stateConverters.containsKey(protocolValue)) {
            stateConverters.put(protocolValue, new StateToConverterMap());
        }
        StateToConverterMap parameterConverters = stateConverters.get(protocolValue);
        parameterConverters.put(openHABType, converter);
    }

    @SuppressWarnings("unchecked")
    public <STATE extends State> Class<? extends StateConverter<?, STATE>> getStateConverter(String protocolValue, Class<STATE> state) {
        return (Class<? extends StateConverter<?, STATE>>) stateConverters.get(protocolValue).get(state);
    }

    public List<Class<? extends State>> getMatchingStates(String protocolValue) {
        StateToConverterMap stateToConverterMap = stateConverters.get(protocolValue);
        if (stateToConverterMap == null) {
            return new ArrayList<Class<? extends State>>();
        }
        ArrayList<Class<? extends State>> list = new ArrayList<Class<? extends State>>(stateToConverterMap.keySet());
        return list;
    }

}

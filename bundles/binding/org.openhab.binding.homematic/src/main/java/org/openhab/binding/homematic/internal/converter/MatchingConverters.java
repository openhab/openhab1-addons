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
import java.util.HashMap;
import java.util.List;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Map of all stateConverters and commandConverters. The map keys are in both
 * maps the parameterId.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class MatchingConverters {

    private static final Logger logger = LoggerFactory.getLogger(MatchingConverters.class);

    private HashMap<String, StateToConverterMap> stateConverters = new HashMap<String, StateToConverterMap>();
    private HashMap<String, CommandToConverterMap> commandConverters = new HashMap<String, CommandToConverterMap>();

    public void addCommandConverter(String parameterId, Class<? extends Command> command, Class<? extends CommandConverter<?, ?>> converter) {
        if (!commandConverters.containsKey(parameterId)) {
            commandConverters.put(parameterId, new CommandToConverterMap());
        }
        CommandToConverterMap parameterConverters = commandConverters.get(parameterId);
        parameterConverters.put(command, converter);
    }

    @SuppressWarnings("unchecked")
    public <COMMAND extends Command> Class<? extends CommandConverter<?, COMMAND>> getCommandConverter(String parameterId,
            Class<COMMAND> command) {
        CommandToConverterMap commandToConverterMap = commandConverters.get(parameterId);
        if (commandToConverterMap == null) {
            logger.debug("ParameterId " + parameterId + " is not configured. If the command is a state this will result in no error.");
            return null;
        }
        return (Class<? extends CommandConverter<?, COMMAND>>) commandToConverterMap.get(command);
    }

    public List<Class<? extends Command>> getMatchingCommands(String parameterId) {
        return new ArrayList<Class<? extends Command>>(commandConverters.get(parameterId).keySet());
    }

    public void addStateConverter(String parameterId, Class<? extends State> openHABType, Class<? extends StateConverter<?, ?>> converter) {
        if (!stateConverters.containsKey(parameterId)) {
            stateConverters.put(parameterId, new StateToConverterMap());
        }
        StateToConverterMap parameterConverters = stateConverters.get(parameterId);
        parameterConverters.put(openHABType, converter);
    }

    @SuppressWarnings("unchecked")
    public <STATE extends State> Class<? extends StateConverter<?, STATE>> getStateConverter(String parameterId, Class<STATE> state) {
        StateToConverterMap map = stateConverters.get(parameterId);
        if (map == null) {
            return null;
        }
        return (Class<? extends StateConverter<?, STATE>>) map.get(state);
    }

    public List<Class<? extends State>> getMatchingStates(String parameterId) {
        StateToConverterMap stateToConverterMap = stateConverters.get(parameterId);
        if (stateToConverterMap == null) {
            return new ArrayList<Class<? extends State>>();
        }
        ArrayList<Class<? extends State>> list = new ArrayList<Class<? extends State>>(stateToConverterMap.keySet());
        return list;
    }

}

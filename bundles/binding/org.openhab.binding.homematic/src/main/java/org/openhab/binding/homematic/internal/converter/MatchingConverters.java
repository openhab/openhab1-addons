/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

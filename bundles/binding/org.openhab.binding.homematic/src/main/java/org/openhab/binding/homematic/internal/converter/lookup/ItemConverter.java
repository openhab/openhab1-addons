/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * ItemConverter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class ItemConverter {

    private StateConverter<?, ?> bindingValueToStateConverter;
    private StateConverterMap stateToBindingValueConverter;
    private CommandConverterMap commandToBindingValueConverter;

    public StateConverter<?, ?> getBindingValueToStateConverter() {
        return bindingValueToStateConverter;
    }

    public void setBindingValueToStateConverter(StateConverter<?, ?> bindingValueToStateConverter) {
        this.bindingValueToStateConverter = bindingValueToStateConverter;
    }

    public StateConverter<?, ?> getStateToBindingValueConverter(Class<? extends State> stateClass) {
        if (stateToBindingValueConverter == null) {
            return null;
        }
        return stateToBindingValueConverter.get(stateClass);
    }

    public void addStateToBindingValueConverter(StateConverter<?, ?> stateToBindingValueConverter, Class<? extends State> stateClass) {
        this.stateToBindingValueConverter.put(stateClass, stateToBindingValueConverter);
    }

    public void setStateToBindingValueConverter(StateConverterMap stateToBindingValueConverter) {
        this.stateToBindingValueConverter = stateToBindingValueConverter;
    }

    public void setCommandToBindingValueConverter(CommandConverterMap converterMap) {
        this.commandToBindingValueConverter = converterMap;
    }

    public CommandConverter<?, ?> getCommandToBindingValueConverter(Class<? extends Command> commandClass) {
        if (commandToBindingValueConverter == null) {
            return null;
        }
        return commandToBindingValueConverter.get(commandClass);
    }

}

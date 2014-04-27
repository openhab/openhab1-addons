/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * A generic converter for all {@link Command}s that are also {@link State}s. If
 * the given command is a state, it is just returned as such. This state can
 * later be set on the item / binding protocolValue.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class StateCommandConverter extends CommandConverter<State, Command> {

    @Override
    protected State convertImpl(State actualState, Command command) {
        if (command instanceof State) {
            State state = (State) command;
            return state;
        }
        return null;
    }

}

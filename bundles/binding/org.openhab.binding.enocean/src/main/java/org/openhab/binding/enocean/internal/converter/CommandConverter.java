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
 * A {@link CommandConverter} converts between an openHAB {@link State} and an
 * openHAB {@link Command}.<br>
 * This is one of the two central converter types. To implement an own converter
 * you have to subclass this one and implement the abstract method
 * {@link #convertImpl(State, Command)}. <br>
 * The command receiving has two parts:<br>
 * The command is first converter to a state with this converter. This new state
 * is then set on the corresponding binding (normally by converting it to a
 * binding protocol value and sending it).
 * 
 * A {@link CommandConverter} is used like
 * <code>converterFactory.addCommandConverter("LEVEL", OnOffType.class, OnOffPercentageCommandConverter.class);</code>
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 * @param <OPENHAB_STATE>
 *            The OpenHAB State to convert into (the conversion target, a
 *            subclass of {@link State})
 * @param <OPENHAB_COMMAND>
 *            The OpenHAB {@link Command} to convert (the conversion source, a
 *            subclass of {@link Command})
 * @since 1.3.0
 */
public abstract class CommandConverter<OPENHAB_STATE extends State, OPENHAB_COMMAND extends Command> {

    /**
     * The method to overwrite.
     * 
     * @param actualState
     *            The current state.
     * @param command
     *            The command to receive. Is never null.
     * @return The new state of the item.
     */
    protected abstract OPENHAB_STATE convertImpl(State actualState, OPENHAB_COMMAND command);

    /**
     * Applies a Command to the actualState.
     * 
     * E.g. this method can apply an IncreaseDecreaseType Command to a decimal
     * value and increase / decrease it.
     * 
     * @param actualState
     *            The current state.
     * @param command
     *            The command to receive. If null, the actualState is returned
     *            back unchanged.
     * @return The newly calculated state by applying the command to the
     *         actualState.
     */
    @SuppressWarnings("unchecked")
    public OPENHAB_STATE convertFrom(State actualState, Command command) {
        if (command == null) {
            return null;
        }
        return convertImpl(actualState, (OPENHAB_COMMAND) command);
    }

}

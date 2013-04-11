/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

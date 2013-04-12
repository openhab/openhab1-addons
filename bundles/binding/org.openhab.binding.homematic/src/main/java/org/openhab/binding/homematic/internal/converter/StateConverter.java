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

import org.openhab.core.types.State;

/**
 * A {@link StateConverter} converts between a protocolValue and an openHAB
 * {@link State}.<br>
 * This is one of the two central converter types. To implement an own converter
 * you have to subclass this one and implement the abstract methods
 * {@link #convertFromImpl(State)} and {@link #convertToImpl(Object)}
 * 
 * The converter works in two direction, it can convert a protocolValue to an
 * openHAB state and vice versa.
 * 
 * A {@link StateConverter} is used like
 * <code>converterFactory.addStateConverter("LEVEL", PercentType.class, DoublePercentageConverter.class);</code>
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 * @param <OPENHAB_TYPE>
 *            The OpenHAB State to convert (a subclass of {@link State})
 * @param <BINDING_TYPE>
 *            The protocolValue type. Can be any Java Type.
 */

public abstract class StateConverter<BINDING_TYPE, OPENHAB_TYPE extends State> {

    /**
     * The method to overwrite for conversion of protocolValue -> state.
     * 
     * @param source
     *            The protocolValue to convert to a state (will never be null).
     * @return The state which corresponds to the given protocolValue.
     */
    protected abstract OPENHAB_TYPE convertToImpl(BINDING_TYPE source);

    /**
     * The method to overwrite for conversion of state -> protocolValue.
     * 
     * @param source
     *            The openHAB state to convert.
     * @return The resulting protocolValue to be sent to the real device.
     */
    protected abstract BINDING_TYPE convertFromImpl(OPENHAB_TYPE source);

    /**
     * Converts the given protocolValue into an OpenHAB {@link State}.
     * 
     * @param source The protocolValue to be converted.
     * @return The state which corresponds to the given protocolValue. Returns NULL if the given source is NULL.
     */
    @SuppressWarnings("unchecked")
    public OPENHAB_TYPE convertTo(Object source) {
        if (source == null) {
            return null;
        }
        return convertToImpl((BINDING_TYPE) source);
    }

    /**
     * Converts the given OpenHAB {@link State} to a protocolValue which can be sent to the physical device.
     * 
     * @param source The OpenHAB {@link State} to be converted.
     * @return The protocolValue corresponding to the state. Returns NULL if the given source is NULL.
     */
    @SuppressWarnings("unchecked")
    public BINDING_TYPE convertFrom(State source) {
        if (source == null) {
            return null;
        }
        return convertFromImpl((OPENHAB_TYPE) source);
    }

}

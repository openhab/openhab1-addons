/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

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
 * @since 1.3.0
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
     * @param source
     *            The protocolValue to be converted.
     * @return The state which corresponds to the given protocolValue. Returns
     *         NULL if the given source is NULL.
     */
    @SuppressWarnings("unchecked")
    public OPENHAB_TYPE convertTo(Object source) {
        if (source == null) {
            return null;
        }
        return convertToImpl((BINDING_TYPE) source);
    }

    /**
     * Converts the given OpenHAB {@link State} to a protocolValue which can be
     * sent to the physical device.
     * 
     * @param source
     *            The OpenHAB {@link State} to be converted.
     * @return The protocolValue corresponding to the state. Returns NULL if the
     *         given source is NULL.
     */
    @SuppressWarnings("unchecked")
    public BINDING_TYPE convertFrom(State source) {
        if (source == null) {
            return null;
        }
        return convertFromImpl((OPENHAB_TYPE) source);
    }

}

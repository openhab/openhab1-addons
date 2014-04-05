/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import java.util.HashMap;

import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.types.State;

/**
 * StateConverterMap.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class StateConverterMap extends HashMap<Class<? extends State>, StateConverter<?, ?>> {

    private static final long serialVersionUID = 1L;
    private StateConverter<?, ?> converter;

    public StateConverterMap() {
    }

    public StateConverterMap(StateConverter<?, ?> converter) {
        this.converter = converter;
    }

    public void add(Class<? extends State> state, StateConverter<?, ?> converter) {
        put(state, converter);
    }

    public void addAll(StateConverterMap other) {
        for (java.util.Map.Entry<Class<? extends State>, StateConverter<?, ?>> entry : other.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public StateConverter<?, ?> get(Object key) {
        if (converter != null) {
            return converter;
        }
        return super.get(key);
    }

}

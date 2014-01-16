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

import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.core.types.Command;

/**
 * CommandConverterMap.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class CommandConverterMap extends HashMap<Class<? extends Command>, CommandConverter<?, ?>> {

    private static final long serialVersionUID = 1L;
    private CommandConverter<?, ?> converter;

    public CommandConverterMap() {
    }

    public CommandConverterMap(CommandConverter<?, ?> converter) {
        this.converter = converter;
    }

    public void add(Class<? extends Command> state, CommandConverter<?, ?> converter) {
        put(state, converter);
    }

    @Override
    public CommandConverter<?, ?> get(Object key) {
        if (converter != null) {
            return converter;
        }
        return super.get(key);
    }

}

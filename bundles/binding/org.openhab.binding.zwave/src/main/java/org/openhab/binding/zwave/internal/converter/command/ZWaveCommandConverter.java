/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.zwave.internal.converter.command;

import java.lang.reflect.ParameterizedType;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Abstract base class for converting item commands to
 * Z-Wave command class commands.
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 * @param <OPENHAB_TYPE> the command to convert
 * @param <ZWAVE_TYPE> the Z-Wave type to convert to.
 */
public abstract class ZWaveCommandConverter<OPENHAB_TYPE extends Command, ZWAVE_TYPE> {

    /**
     * Returns the type of the openHAB {@link Command} that this {@link ZWaveCommandConverter} converts to.
     *
     * @return the supported {@link Command}
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Command> getCommand() {
        return (Class<? extends Command>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * Converts a an OpenHab command to a Z-Wave value.
     *
     * @param command the {@link Command} to convert.
     * @param item the item to convert the command for.
     * @return the Z-Wave value to convert to.
     */
    protected abstract ZWAVE_TYPE convert(Item item, OPENHAB_TYPE command);

    /**
     * Converts a an OpenHab command to a Z-Wave value.
     *
     * @param command the {@link Command} to convert.
     * @param item the item to convert the command for.
     * @return the Z-Wave value to convert to.
     */
    @SuppressWarnings("unchecked")
    public Object convertFromCommandToValue(Item item, Command command) {

        if (command == null) {
            return null;
        }

        return convert(item, (OPENHAB_TYPE) command);
    }
}

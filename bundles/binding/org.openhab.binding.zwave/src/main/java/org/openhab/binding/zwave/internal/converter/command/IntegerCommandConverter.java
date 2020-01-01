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

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;

/**
 * Converts from {@link DecimalType} command to a Z-Wave value.
 *
 * @author Chris Jackson
 * @since 1.7.0
 */
public class IntegerCommandConverter extends ZWaveCommandConverter<DecimalType, Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer convert(Item item, DecimalType command) {
        return command.intValue();
    }

}

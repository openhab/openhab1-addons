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
import org.openhab.core.library.types.OnOffType;

/**
 * Converts from {@link OnOffType} command to a Z-Wave value.
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class MultiLevelOnOffCommandConverter extends ZWaveCommandConverter<OnOffType, Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer convert(Item item, OnOffType command) {
        return command == OnOffType.ON ? 0x63 : 0x00;
    }

}
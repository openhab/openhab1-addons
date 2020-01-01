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
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.UpDownType;

/**
 * Converts from a {@link Integer} to a {@link UpDownType}
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class IntegerUpDownTypeConverter extends ZWaveStateConverter<Integer, UpDownType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected UpDownType convert(Integer value) {
        return value != 0x00 ? UpDownType.DOWN : UpDownType.UP;
    }
}

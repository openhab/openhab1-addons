/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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

import org.openhab.core.library.types.PercentType;

/**
 * Converts from a binary Z-Wave value to a {@link PercentType}
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class BinaryPercentTypeConverter extends ZWaveStateConverter<Integer, PercentType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected PercentType convert(Integer value) {
        return value != 0x00 ? PercentType.HUNDRED : PercentType.ZERO;
    }

}

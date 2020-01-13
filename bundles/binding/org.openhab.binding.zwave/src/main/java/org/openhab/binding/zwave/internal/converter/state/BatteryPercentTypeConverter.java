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

import org.openhab.core.library.types.PercentType;

/**
 * Converts from a {@link Integer} to a {@link PercentType}
 * Only processes for the BATTERY command class.
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class BatteryPercentTypeConverter extends ZWaveStateConverter<Integer, PercentType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected PercentType convert(Integer value) {
        if (value <= 0 || value == 0xFF) {
            return PercentType.ZERO;
        } else if (value > 0 && value < 100) {
            return new PercentType(value);
        } else {
            return PercentType.HUNDRED;
        }
    }
}

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

import org.openhab.core.library.types.OnOffType;

/**
 * Converts from Z-Wave boolean value to a {@link OnOffType}
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class BooleanOnOffTypeConverter extends ZWaveStateConverter<Boolean, OnOffType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected OnOffType convert(Boolean value) {
        return value ? OnOffType.ON : OnOffType.OFF;
    }

}

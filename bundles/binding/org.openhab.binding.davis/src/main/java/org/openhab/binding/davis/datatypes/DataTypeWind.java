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
package org.openhab.binding.davis.datatypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Class to handle wind speeds
 * 1 byte, encoded in mph, result in km/h
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeWind implements DavisDataType {

    /**
     * {@inheritDoc}
     */
    public State convertToState(byte[] data, DavisValueType valueType) {
        byte value = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).get(valueType.getDataOffset());
        return new DecimalType(value * 1.609344);
    }

}

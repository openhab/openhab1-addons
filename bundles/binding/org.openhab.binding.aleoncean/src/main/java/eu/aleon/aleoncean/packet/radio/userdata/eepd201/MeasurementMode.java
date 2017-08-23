/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet.radio.userdata.eepd201;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum MeasurementMode {

    ENERGY((byte) 0x00),
    POWER((byte) 0x01);

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementMode.class);

    public static MeasurementMode fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return MeasurementMode.ENERGY;
            case 0x01:
                return MeasurementMode.POWER;
            default:
                LOGGER.warn("Invalid MeasurementMode: {}", value);
                return MeasurementMode.POWER;
        }
    }

    final byte value;

    private MeasurementMode(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }
}

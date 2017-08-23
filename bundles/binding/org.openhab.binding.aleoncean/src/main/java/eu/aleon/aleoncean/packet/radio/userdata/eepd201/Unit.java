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
public enum Unit {

    ENERGY_WS((byte) 0x00),
    ENERGY_WH((byte) 0x01),
    ENERGY_KWH((byte) 0x02),
    POWER_W((byte) 0x03),
    POWER_KW((byte) 0x04),
    UNDEF((byte) 0x07);

    private static final Logger LOGGER = LoggerFactory.getLogger(Unit.class);

    public static Unit fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return Unit.ENERGY_WS;
            case 0x01:
                return Unit.ENERGY_WH;
            case 0x02:
                return Unit.ENERGY_KWH;
            case 0x03:
                return Unit.POWER_W;
            case 0x04:
                return Unit.POWER_KW;
            default:
                LOGGER.warn("Invalid Unit: {}", value);
                return Unit.UNDEF;
        }
    }

    final byte value;

    private Unit(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }
}

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
public enum ErrorLevel {

    ERROR_LEVEL_0_HARDWARE_OK((byte) 0),
    ERROR_LEVEL_1_HARDWARE_WARNING((byte) 1),
    ERROR_LEVEL_2_HARDWARE_FAILURE((byte) 2),
    ERROR_LEVEL_NOT_SUPPORTED((byte) 3);

    private final byte value;

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorLevel.class);

    public static ErrorLevel fromByte(final byte value) {
        switch (value) {
            case 0:
                return ErrorLevel.ERROR_LEVEL_0_HARDWARE_OK;
            case 1:
                return ErrorLevel.ERROR_LEVEL_1_HARDWARE_WARNING;
            case 2:
                return ErrorLevel.ERROR_LEVEL_2_HARDWARE_FAILURE;
            case 3:
                return ErrorLevel.ERROR_LEVEL_NOT_SUPPORTED;
            default:
                LOGGER.warn("Invalid ErrorLevel: {}", value);
                return ErrorLevel.ERROR_LEVEL_NOT_SUPPORTED;
        }
    }

    private ErrorLevel(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }

}

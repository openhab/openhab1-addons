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
public enum DefaultState {

    OFF_OR_0_PERCENT((byte) 0),
    ON_OR_100_PERCENT((byte) 1),
    REMEMBER_PREVIOUS_STATE((byte) 2),
    NOT_USED((byte) 3);

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultState.class);

    public static DefaultState fromByte(final byte value) {
        switch (value) {
            case 0:
                return DefaultState.OFF_OR_0_PERCENT;
            case 1:
                return DefaultState.ON_OR_100_PERCENT;
            case 2:
                return DefaultState.REMEMBER_PREVIOUS_STATE;
            case 3:
                return DefaultState.NOT_USED;
            default:
                LOGGER.warn("Invalid DefaultState: {}", value);
                return DefaultState.NOT_USED;
        }
    }

    private final byte value;

    private DefaultState(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }

}

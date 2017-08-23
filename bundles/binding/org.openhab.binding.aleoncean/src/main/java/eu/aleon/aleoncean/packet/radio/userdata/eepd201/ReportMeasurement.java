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
public enum ReportMeasurement {

    QUERY_ONLY((byte) 0x00),
    QUERY_AND_AUTO_REPORTING((byte) 0x01);

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMeasurement.class);

    public static ReportMeasurement fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return ReportMeasurement.QUERY_ONLY;
            case 0x01:
                return ReportMeasurement.QUERY_AND_AUTO_REPORTING;
            default:
                LOGGER.warn("Invalid ReportMeasurement: {}", value);
                return ReportMeasurement.QUERY_AND_AUTO_REPORTING;
        }
    }

    final byte value;

    private ReportMeasurement(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }
}

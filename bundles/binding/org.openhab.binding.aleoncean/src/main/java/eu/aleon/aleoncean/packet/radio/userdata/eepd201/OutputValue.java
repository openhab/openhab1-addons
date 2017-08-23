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

import eu.aleon.aleoncean.util.CalculationUtil;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class OutputValue {

    public static final byte NOT_VALID_OR_NOT_APPLICABLE = (byte) 0x7F;

    public static boolean isRawValueEEPConform(final byte value) {
        return value >= 0x00 && value <= 0x64 || value == 0x7F;
    }

    public static int dimValueRawToScale(final byte value) {
        return value;
    }

    public static byte dimValueScaleToRaw(final int value) {
        return CalculationUtil.fitInRange(value, 0, 100).byteValue();
    }

    public static boolean onOffValueRawToScale(final byte value) {
        return value != 0;
    }

    public static byte onOffValueScaleToRaw(final boolean on) {
        return on ? (byte) 1 : (byte) 0;
    }

    private OutputValue() {
    }

}

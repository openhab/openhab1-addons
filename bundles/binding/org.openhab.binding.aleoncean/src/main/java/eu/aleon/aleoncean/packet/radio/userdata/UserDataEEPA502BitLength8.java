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
package eu.aleon.aleoncean.packet.radio.userdata;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA502BitLength8 extends UserDataEEPA502 {

    private static final long TEMPERATURE_RANGE_MIN = 255;
    private static final long TEMPERATURE_RANGE_MAX = 0;

    /**
     * Class constructor specifying the values of the eep data array, the scale min and max value.
     *
     * @param tempScaleMin
     * @param tempScaleMax
     */
    public UserDataEEPA502BitLength8(final double tempScaleMin, final double tempScaleMax){
        super(1, 7, 1, 0, TEMPERATURE_RANGE_MIN, TEMPERATURE_RANGE_MAX, tempScaleMin, tempScaleMax);
    }

    /**
     * Class constructor specifying the values of the eep data array, the scale min and max value.
     *
     * @param eepData
     * @param tempScaleMin
     * @param tempScaleMax
     */
    public UserDataEEPA502BitLength8(final byte[] eepData,
                                     final double tempScaleMin, final double tempScaleMax) {
        super(eepData, 1, 7, 1, 0, TEMPERATURE_RANGE_MIN, TEMPERATURE_RANGE_MAX, tempScaleMin, tempScaleMax);
    }

}

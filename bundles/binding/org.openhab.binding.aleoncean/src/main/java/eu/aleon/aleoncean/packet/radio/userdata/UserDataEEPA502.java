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

import eu.aleon.aleoncean.values.Unit;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA502 extends UserData4BS {

    private final int tempStartDB;
    private final int tempStartBit;
    private final int tempEndDB;
    private final int tempEndBit;
    private final long tempRangeMin;
    private final long tempRangeMax;
    private final double tempScaleMin;
    private final double tempScaleMax;
    public static final Unit TEMPERATURE_UNIT = Unit.DEGREE_CELSIUS;

    /**
     * Class constructor specifying the values of
     * the start byte and bit, the end byte and bit, the range and the scale.
     *
     * @param tempStartDB
     * @param tempStartBit
     * @param tempEndDB
     * @param tempEndBit
     * @param tempRangeMin
     * @param tempRangeMax
     * @param tempScaleMin
     * @param tempScaleMax
     */
    public UserDataEEPA502(final int tempStartDB, final int tempStartBit, final int tempEndDB, final int tempEndBit,
                           final long tempRangeMin, final long tempRangeMax,
                           final double tempScaleMin, final double tempScaleMax) {
        super();
        this.tempStartDB = tempStartDB;
        this.tempStartBit = tempStartBit;
        this.tempEndDB = tempEndDB;
        this.tempEndBit = tempEndBit;
        this.tempRangeMin = tempRangeMin;
        this.tempRangeMax = tempRangeMax;
        this.tempScaleMin = tempScaleMin;
        this.tempScaleMax = tempScaleMax;
    }

    /**
     * Class constructor specifying the values of the eep data array,
     * the start byte and bit, the end byte and bit, the range and the scale.
     *
     * @param eepData
     * @param tempStartDB
     * @param tempStartBit
     * @param tempEndDB
     * @param tempEndBit
     * @param tempRangeMin
     * @param tempRangeMax
     * @param tempScaleMin
     * @param tempScaleMax
     */
    public UserDataEEPA502(final byte[] eepData,
                           final int tempStartDB, final int tempStartBit, final int tempEndDB, final int tempEndBit,
                           final long tempRangeMin, final long tempRangeMax,
                           final double tempScaleMin, final double tempScaleMax) {
        super(eepData);
        this.tempStartDB = tempStartDB;
        this.tempStartBit = tempStartBit;
        this.tempEndDB = tempEndDB;
        this.tempEndBit = tempEndBit;
        this.tempRangeMin = tempRangeMin;
        this.tempRangeMax = tempRangeMax;
        this.tempScaleMin = tempScaleMin;
        this.tempScaleMax = tempScaleMax;
    }

    /**
     * Delivering the temperature value, which is current set in the user data.
     *
     * @return The temperature value of the user data.
     * @throws UserDataScaleValueException
     */
    public double getTemperature() throws UserDataScaleValueException {
        return getScaleValue(tempStartDB, tempStartBit, tempEndDB, tempEndBit,
                             tempRangeMin, tempRangeMax,
                             tempScaleMin, tempScaleMax);
    }

    /**
     * Set the given temperature value to user data
     *
     * @param temperature The temperature value to be set in the user data
     * @throws UserDataScaleValueException
     */
    public void setTemperature(final double temperature) throws UserDataScaleValueException {
        final long range = getRangeValue(temperature, tempScaleMin, tempScaleMax,
                                   tempRangeMin, tempRangeMax);
        setDataRange(range, tempStartDB, tempStartBit, tempEndDB, tempEndBit);
    }
}

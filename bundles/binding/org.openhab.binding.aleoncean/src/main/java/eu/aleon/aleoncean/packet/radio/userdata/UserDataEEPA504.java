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
public class UserDataEEPA504 extends UserData4BS {

    public static final long HUMIDITY_RANGE_MIN = 0;
    public static final long HUMIDITY_RANGE_MAX = 250;
    public static final Unit HUMIDITY_UNIT = Unit.PERCENT;

    public static final long TEMPERATURE_RANGE_MIN = 0;
    public static final long TEMPERATURE_RANGE_MAX = 250;
    public static final Unit TEMPERATURE_UNIT = Unit.DEGREE_CELSIUS;

    private final double humidityScaleMin;
    private final double humidityScaleMax;
    private final double temperatureScaleMin;
    private final double temperatureScaleMax;

    public UserDataEEPA504(final byte[] eepData,
                           final double humidityScaleMin, final double humidityScaleMax,
                           final double temperatureScaleMin, final double temperatureScaleMax) {
        super(eepData);
        this.humidityScaleMin = humidityScaleMin;
        this.humidityScaleMax = humidityScaleMax;
        this.temperatureScaleMin = temperatureScaleMin;
        this.temperatureScaleMax = temperatureScaleMax;
    }

    private long getHumidityRAW() {
        return getDataRange(2, 7, 2, 0);
    }

    public double getHumidity() throws UserDataScaleValueException {
        return getScaleValue(getHumidityRAW(), HUMIDITY_RANGE_MIN, HUMIDITY_RANGE_MAX, humidityScaleMin, humidityScaleMax);
    }

    public Unit getHumidityUnit() {
        return HUMIDITY_UNIT;
    }

    private long getTemperatureRAW() {
        return getDataRange(1, 7, 1, 0);
    }

    public double getTemperature() throws UserDataScaleValueException {
        return getScaleValue(getTemperatureRAW(), TEMPERATURE_RANGE_MIN, TEMPERATURE_RANGE_MAX, temperatureScaleMin, temperatureScaleMax);
    }

    public Unit getTemperatureUnit() {
        return TEMPERATURE_UNIT;
    }

    public boolean isTemperatureAvailable() {
        return getDataBit(0, 1) == 1;
    }

}

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

import eu.aleon.aleoncean.util.CalculationUtil;
import eu.aleon.aleoncean.values.Unit;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class UserDataEEPA508 extends UserData4BS {

    public static final int SUPPLY_VOLTAGE_RANGE_MIN = 0;
    public static final int SUPPLY_VOLTAGE_RANGE_MAX = 255;
    public static final double SUPPLY_VOLTAGE_SCALE_MIN = 0;
    public static final double SUPPLY_VOLTAGE_SCALE_MAX = 5.1;
    public static final Unit SUPPLY_VOLTAGE_UNIT = Unit.VOLTAGE;

    public static final int ILLUMINATION_RANGE_MIN = 0;
    public static final int ILLUMINATION_RANGE_MAX = 255;
    public static final Unit ILLUMINATION_UNIT = Unit.LUX;

    public static final int TEMPERATURE_RANGE_MIN = 0;
    public static final int TEMPERATURE_RANGE_MAX = 255;
    public static final Unit TEMPERATURE_UNIT = Unit.DEGREE_CELSIUS;

    private final double illuminationScaleMin;
    private final double illuminationScaleMax;
    private final double temperatureScaleMin;
    private final double temperatureScaleMax;

    public UserDataEEPA508(final byte[] eepData,
                           final double illuminationScaleMin,
                           final double illuminationScaleMax,
                           final double temperatureScaleMin,
                           final double temperatureScaleMax) {
        super(eepData);
        this.illuminationScaleMin = illuminationScaleMin;
        this.illuminationScaleMax = illuminationScaleMax;
        this.temperatureScaleMin = temperatureScaleMin;
        this.temperatureScaleMax = temperatureScaleMax;
    }

    public double getSupplyVoltage() {
        final long range = getDataRange(3, 7, 3, 0);
        final double scale = CalculationUtil.rangeToScale(range, SUPPLY_VOLTAGE_RANGE_MIN, SUPPLY_VOLTAGE_RANGE_MAX, SUPPLY_VOLTAGE_SCALE_MIN, SUPPLY_VOLTAGE_SCALE_MAX);
        return scale;
    }

    public double getIllumination() {
        final long range = getDataRange(2, 7, 2, 0);
        final double scale = CalculationUtil.rangeToScale(range, ILLUMINATION_RANGE_MIN, ILLUMINATION_RANGE_MAX, illuminationScaleMin, illuminationScaleMax);
        return scale;
    }

    public double getTemperature() {
        final long range = getDataRange(1, 7, 1, 0);
        final double scale = CalculationUtil.rangeToScale(range, TEMPERATURE_RANGE_MIN, TEMPERATURE_RANGE_MAX, temperatureScaleMin, temperatureScaleMax);
        return scale;
    }

    public boolean isPIRStatusOn() {
        return getDataBit(0, 1) == 0;
    }

    public boolean isOccupancyButtonPressed() {
        return getDataBit(0, 0) == 0;
    }
}

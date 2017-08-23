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
public class UserDataEEPA50703 extends UserData4BS {

    public static final long SUPPLY_VOLTAGE_RANGE_MIN = 0;
    public static final long SUPPLY_VOLTAGE_RANGE_MAX = 250;
    public static final double SUPPLY_VOLTAGE_SCALE_MIN = 0;
    public static final double SUPPLY_VOLTAGE_SCALE_MAX = 5;
    public static final Unit SUPPLY_VOLTAGE_UNIT = Unit.VOLTAGE;

    public static final long ILLUMINATION_RANGE_MIN = 0;
    public static final long ILLUMINATION_RANGE_MAX = 1000;
    public static final double ILLUMINATION_SCALE_MIN = 0;
    public static final double ILLUMINATION_SCALE_MAX = 1000;
    public static final Unit ILLUMINATION_UNIT = Unit.LUX;

    public UserDataEEPA50703(final byte[] eepData) {
        super(eepData);
    }

    public double getSupplyVoltage() throws UserDataScaleValueException {
        return getScaleValue(3, 7, 3, 0, SUPPLY_VOLTAGE_RANGE_MIN, SUPPLY_VOLTAGE_RANGE_MAX, SUPPLY_VOLTAGE_SCALE_MIN, SUPPLY_VOLTAGE_SCALE_MAX);
    }

    public Unit getSupplyVoltageUnit() {
        return SUPPLY_VOLTAGE_UNIT;
    }

    public double getIllumination() throws UserDataScaleValueException {
        return getScaleValue(2, 7, 1, 6, ILLUMINATION_RANGE_MIN, ILLUMINATION_RANGE_MAX, ILLUMINATION_SCALE_MIN, ILLUMINATION_SCALE_MAX);
    }

    public Unit getIlluminationUnit() {
        return ILLUMINATION_UNIT;
    }

    public boolean isPIRStatusOn() {
        return getDataBit(0, 7) == 1;
    }
}

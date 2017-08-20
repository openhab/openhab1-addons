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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import eu.aleon.aleoncean.values.Unit;
import eu.aleon.aleoncean.values.VOCIdentification;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA50905 extends UserData4BS {

    public static final long VOC_CONCENTRATION_RANGE_MIN = 0;
    public static final long VOC_CONCENTRATION_RANGE_MAX = 65535;
    public static final double VOC_CONCENTRATION_SCALE_MIN = 0;
    public static final double VOC_CONCENTRATION_SCALE_MAX = 65535;
    public static final Unit VOC_CONCENTRATION_UNIT = Unit.PPB;

    private static final Map<Byte, VOCIdentification> VOC_IDENTIFICATION_MAP;

    static {
        final Map<Byte, VOCIdentification> map = new HashMap<>();
        map.put((byte) 0, VOCIdentification.VOCT_TOTAL);
        map.put((byte) 1, VOCIdentification.FORMALDEHYDE);
        map.put((byte) 2, VOCIdentification.BENZENE);
        map.put((byte) 3, VOCIdentification.STYRENE);
        map.put((byte) 4, VOCIdentification.TOLUENE);
        map.put((byte) 5, VOCIdentification.TETRACHLOROETHYLENE);
        map.put((byte) 6, VOCIdentification.XYLENE);
        map.put((byte) 7, VOCIdentification.HEXANE_N);
        map.put((byte) 8, VOCIdentification.OCTANE_N);
        map.put((byte) 9, VOCIdentification.CYCLOPENTANE);
        map.put((byte) 10, VOCIdentification.METHANOL);
        map.put((byte) 11, VOCIdentification.ETHANOL);
        map.put((byte) 12, VOCIdentification.PENTANOL_1);
        map.put((byte) 13, VOCIdentification.ACETONE);
        map.put((byte) 14, VOCIdentification.ETHYLENE_OXIDE);
        map.put((byte) 15, VOCIdentification.ACETALDEHYDE_UE);
        map.put((byte) 16, VOCIdentification.ACETIC_ACID);
        map.put((byte) 17, VOCIdentification.PROPIONICE_ACID);
        map.put((byte) 18, VOCIdentification.VALERIC_ACID);
        map.put((byte) 19, VOCIdentification.BUTYRIC_ACID);
        map.put((byte) 20, VOCIdentification.AMMONIAC);
        map.put((byte) 22, VOCIdentification.HYDROGEN_SULFIDE);
        map.put((byte) 23, VOCIdentification.DIMETHYLSULFIDE);
        map.put((byte) 24, VOCIdentification.BUTANOL_2);
        map.put((byte) 25, VOCIdentification.METHYLPROPANOL_2);
        map.put((byte) 26, VOCIdentification.DIETHYL_ETHER);
        map.put((byte) 255, VOCIdentification.OZONE);
        VOC_IDENTIFICATION_MAP = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer, Float> SCALE_MULTIPLIER_MAP;

    static {
        final Map<Integer, Float> map = new HashMap<>();
        map.put(0, 0.01f);
        map.put(1, 0.1f);
        map.put(2, 1f);
        map.put(3, 10f);
        SCALE_MULTIPLIER_MAP = Collections.unmodifiableMap(map);
    }

    public UserDataEEPA50905(final byte[] eepData) {
        super(eepData);
    }

    public long getVOCConcentrationRaw() {
        /*
         * The valid range and the scale use the same range, the whole 16 bits.
         * No conversion or range check necessary.
         */
        final long range = getDataRange(3, 7, 2, 0);
        return range;
    }

    public double getVOCConcentration() throws UserDataScaleValueException {
        return getVOCConcentrationRaw() * getScaleMultiplier();
    }

    public Unit getVOCConcentrationUnit() {
        return VOC_CONCENTRATION_UNIT;
    }

    public VOCIdentification getVOCIdentification() throws UserDataScaleValueException {
        final byte range = (byte) getDataRange(1, 7, 1, 0);
        final VOCIdentification id = VOC_IDENTIFICATION_MAP.get(range);
        if (id == null) {
            throw new UserDataScaleValueException(String.format("VOC identification unknown (%d).", range));
        }
        return id;
    }

    public double getScaleMultiplier() throws UserDataScaleValueException {
        final int range = (int) getDataRange(0, 1, 0, 0);
        final Float mult = SCALE_MULTIPLIER_MAP.get(range);
        if (mult == null) {
            throw new UserDataScaleValueException(String.format("Scale multiplier unknown (%d).", range));
        }
        return mult;
    }
}

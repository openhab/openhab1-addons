/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum holds all the different measures and states available to be
 * retrieved by the TACmi binding, including the scale factors needed to convert the received values to the real
 * numbers.
 *
 * @author Timo Wendt
 * @author Wolfgang Klimt
 * @since 1.8.0
 */
public enum TACmiMeasureType {
    NONE(0, 0),
    TEMPERATURE(1, 1),
    UNKNOWN2(2, 0),
    UNKNOWN3(3, 0),
    SECONDS(4, 0),
    UNKNOWN5(5, 0),
    UNKNOWN6(6, 0),
    UNKNOWN7(7, 0),
    UNKNOWN8(8, 0),
    UNKNOWN9(9, 0),
    KILOWATT(10, 2),
    KILOWATTHOURS(11, 1),
    MEGAWATTHOURS(12, 0),
    UNKNOWN13(13, 0),
    UNKNOWN14(14, 0),
    UNKNOWN15(15, 0),
    UNKNOWN16(16, 0),
    UNKNOWN17(17, 0),
    UNKNOWN18(18, 0),
    UNKNOWN19(19, 0),
    UNKNOWN20(20, 0),
    UNKNOWN21(21, 0),

    UNSUPPORTED(-1, 0);

    private int typeval;
    private int scale;

    private static final Logger logger = LoggerFactory.getLogger(TACmiMeasureType.class);

    private TACmiMeasureType(int typeval, int scale) {
        this.typeval = typeval;
        this.scale = scale;
    }

    public int getTypeValue() {
        return typeval;
    }

    public int getScale() {
        return scale;
    }

    /**
     * Return the measure type for the specified name.
     *
     * @param measure
     * @return
     */
    public static TACmiMeasureType fromString(String measure) {
        if (!StringUtils.isEmpty(measure)) {
            return TACmiMeasureType.valueOf(measure.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid measure: " + measure);
    }

    /**
     * Return measure type for a specific int value
     */
    public static TACmiMeasureType fromInt(int type) {
        for (TACmiMeasureType mtype : TACmiMeasureType.values()) {
            if (mtype.getTypeValue() == type) {
                return mtype;
            }
        }
        logger.warn("Received unexpected measure type {}", type);
        return TACmiMeasureType.UNSUPPORTED;
    }
}

/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal.message;

import java.math.BigDecimal;

import org.openhab.binding.tacmi.internal.TACmiMeasureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles analog values as used in the analog message.
 *
 * @author Timo Wendt
 * @author Wolfgang Klimt
 * @since 1.8.0
 */
public final class AnalogValue {
    public BigDecimal value;
    public TACmiMeasureType measureType;

    private static Logger logger = LoggerFactory.getLogger(AnalogValue.class);

    /**
     * Create new AnalogValue with specified value and type
     */
    public AnalogValue(int rawValue, int type) {
        measureType = TACmiMeasureType.fromInt(type);
        value = new BigDecimal(rawValue).movePointLeft(measureType.getScale());
        if (measureType.equals(TACmiMeasureType.UNSUPPORTED)) {
            logger.warn("Unsupported measure type {}, value is {}", type, value);
        } else {
            logger.debug("Got measure: type {}, raw value {}, converted: {}, {}", type, rawValue, measureType.name(),
                    value);
        }
    }
}

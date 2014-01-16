/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.openhab.core.library.types.PercentType;

/**
 * Converts a Double value into a {@link PercentType}. The resulting
 * {@link PercentType} is inverted (e.g. a double value of 20 gets 80 percent, a
 * 40 gets 60) and rounded to 3 digits.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3
 */
public class InvertedDoublePercentageConverter extends StateConverter<Double, PercentType> {

    @Override
    protected PercentType convertToImpl(Double source) {
        BigDecimal bValue = BigDecimal.valueOf(100 - source * 100);
        bValue.setScale(3, RoundingMode.HALF_UP);
        return new PercentType(bValue);
    }

    @Override
    protected Double convertFromImpl(PercentType source) {
        Double value = (double) (100 - source.intValue()) / 100;
        return value;
    }

}

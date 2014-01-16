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

import org.openhab.core.library.types.DecimalType;

/**
 * A converter to convert a Double into a {@link DecimalType}. The resulting decimal is rounded up to three digits.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class TemperatureConverter extends StateConverter<Double, DecimalType> {

    @Override
    protected DecimalType convertToImpl(Double source) {
        BigDecimal bValue = BigDecimal.valueOf(source);
        bValue.setScale(3, RoundingMode.HALF_UP);
        return new DecimalType(bValue);
    }

    @Override
    protected Double convertFromImpl(DecimalType source) {
        return new Double(source.floatValue());
    }

}

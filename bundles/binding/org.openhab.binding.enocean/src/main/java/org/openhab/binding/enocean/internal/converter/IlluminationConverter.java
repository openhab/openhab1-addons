/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import java.math.BigDecimal;

import org.enocean.java.common.values.NumberWithUnit;
import org.enocean.java.common.values.Unit;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;

/**
 * A converter to convert a NumberWithUnit ILLUMINATION to a DecimalType
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 * 
 */
public class IlluminationConverter extends StateConverter<NumberWithUnit, DecimalType> {

    @Override
    protected NumberWithUnit convertFromImpl(DecimalType source) {
        return new NumberWithUnit(Unit.LUX, source.toBigDecimal());
    }

    @Override
    protected DecimalType convertToImpl(NumberWithUnit source) {
        return new PercentType((BigDecimal) source.getValue());
    }

}

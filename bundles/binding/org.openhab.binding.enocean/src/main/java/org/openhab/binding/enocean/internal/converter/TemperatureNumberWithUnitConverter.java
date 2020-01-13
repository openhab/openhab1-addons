/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.enocean.internal.converter;

import java.math.BigDecimal;

import org.opencean.core.common.values.NumberWithUnit;
import org.opencean.core.common.values.Unit;
import org.openhab.core.library.types.DecimalType;

/**
 * A converter to convert a NumberWithUnit type to a DecimalNumber
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 *
 */
public class TemperatureNumberWithUnitConverter extends StateConverter<NumberWithUnit, DecimalType> {

    @Override
    protected NumberWithUnit convertFromImpl(DecimalType source) {
        return new NumberWithUnit(Unit.DEGREE_CELSIUS, source.toBigDecimal());
    }

    @Override
    protected DecimalType convertToImpl(NumberWithUnit source) {
        return new DecimalType((BigDecimal) source.getValue());
    }

}

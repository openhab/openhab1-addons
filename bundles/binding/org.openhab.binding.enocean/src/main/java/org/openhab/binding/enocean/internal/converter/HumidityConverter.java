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
import org.openhab.core.library.types.PercentType;

/**
 * A converter to convert a NumberWithUnit HUMIDITY to a PercentType
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 *
 */
public class HumidityConverter extends StateConverter<NumberWithUnit, PercentType> {

    @Override
    protected NumberWithUnit convertFromImpl(PercentType source) {
        return new NumberWithUnit(Unit.HUMIDITY, source.toBigDecimal());
    }

    @Override
    protected PercentType convertToImpl(NumberWithUnit source) {
        return new PercentType((BigDecimal) source.getValue());
    }

}

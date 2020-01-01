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
package org.openhab.binding.weather.internal.converter.property;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Converts a string holding a percent value to a integer value.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PercentIntegerConverter extends IntegerConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer convert(String value) {
        return super.convert(StringUtils.remove(value, "%"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.PERCENT_INTEGER;
    }

}

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

import java.util.Calendar;

import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter for a unix timestamp.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UnixDateConverter implements Converter<Calendar> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar convert(String value) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) (Double.valueOf(value) * 1000));
        return cal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.UNIX_DATE;
    }

}

/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;
import org.openhab.binding.weather.internal.utils.UnitUtils;

/**
 * Converts feet to meter.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class FeetConverter implements Converter<Double> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double convert(String value) throws Exception {
        value = StringUtils.trim(StringUtils.remove(value, "ft"));
        if (value != null) {
            return UnitUtils.feetToMeter(Double.valueOf(value));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.FEET;
    }

}

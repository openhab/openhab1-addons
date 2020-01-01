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

import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Simple date converter with a dd MMM yyyy pattern.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SimpleDateConverter extends AbstractDateConverter {
    private static final String DATE_PATTERN = "dd MMM yyyy";

    public SimpleDateConverter() {
        super(DATE_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.SIMPLE_DATE;
    }

}

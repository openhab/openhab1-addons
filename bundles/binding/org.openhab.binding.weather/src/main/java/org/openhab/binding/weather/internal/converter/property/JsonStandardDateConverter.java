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
 * Date converter with a UTC pattern.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class JsonStandardDateConverter extends AbstractDateConverter {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";

    public JsonStandardDateConverter() {
        super(DATE_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.JSON_DATE;
    }

}

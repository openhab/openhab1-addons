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
package org.openhab.binding.weather.internal.converter;

/**
 * Definition of the ConverterTypes.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */

public enum ConverterType {
    NONE,
    AUTO,
    INTEGER,
    DOUBLE,
    STRING,
    PERCENT_INTEGER,
    UNIX_DATE,
    FRACTION_INTEGER,
    UTC_DATE,
    DATE,
    FULL_UTC_DATE,
    SIMPLE_DATE,
    MULTI_ID,
    WIND_MPS,
    DOUBLE_3H,
    PRESSURE_TREND,
    JSON_DATE,
    FEET;
}

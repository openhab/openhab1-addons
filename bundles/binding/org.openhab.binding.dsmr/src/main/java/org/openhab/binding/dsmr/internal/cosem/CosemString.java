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
package org.openhab.binding.dsmr.internal.cosem;

import java.text.ParseException;

import org.openhab.core.library.types.StringType;

/**
 * CosemString represents a string value
 *
 * @author M. Volaart
 * @since 1.7.0
 */
public class CosemString extends CosemValue<StringType> {
    /**
     * Creates a new CosemString
     * 
     * @param unit
     *            the unit of the value
     * @param bindingSuffix
     *            the suffix to use for the DSMR binding identifier
     */
    public CosemString(String unit, String bindingSuffix) {
        super(unit, bindingSuffix);
    }

    /**
     * Parses a String value (that represents an integer) to an openHAB
     * StringType
     * 
     * @param cosemValue
     *            the value to parse
     * @return {@link StringType} on success
     * @throws ParseException
     *             if parsing failed
     */
    @Override
    protected StringType parse(String cosemValue) throws ParseException {
        return new StringType(cosemValue);
    }
}

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
package org.openhab.binding.homematic.internal.converter.state;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;

/**
 * Converts between openHAB DecimalType and Homematic values.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class DecimalTypeConverter extends AbstractNumberTypeConverter<DecimalType> {

    /**
     * Creates a DecimalType from the value.
     */
    @Override
    protected DecimalType createType(BigDecimal value) {
        return new DecimalType(value);
    }

}

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
package org.openhab.binding.tinkerforge.internal.types;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

public class PercentValue extends PercentType implements TinkerforgeValue {

    public PercentValue(BigDecimal bigDecimal) {
        super(bigDecimal);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 8087283524157935305L;

}

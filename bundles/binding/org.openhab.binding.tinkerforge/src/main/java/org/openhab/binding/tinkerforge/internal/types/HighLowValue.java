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

/**
 * A enumeration with values HIGH, LOW, UNDEF, commonly used to describe the
 * state of io devices. UNDEF should be used for the unknown state.
 *
 * @author Theo Weiss
 * @since 1.4.0
 */
public enum HighLowValue implements TinkerforgeValue {
    HIGH,
    LOW,
    UNDEF;

    public String format(String pattern) {
        return String.format(pattern, this.toString());
    }

}

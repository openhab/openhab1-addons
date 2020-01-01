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
package org.openhab.binding.sapp.internal.model;

import org.apache.commons.lang.StringUtils;

/**
 * The list of supported address types for the Sapp binding
 *
 * @author Paolo Denti
 * @since 1.8.0
 */
public enum SappAddressType {

    INPUT("I"),
    OUTPUT("O"),
    VIRTUAL("V");

    String address;

    /**
     * Constructor
     */
    private SappAddressType(String address) {
        this.address = address;
    }

    /**
     * address getter
     */
    public String getAddress() {
        return address;
    }

    /**
     * builds a SappAddressType from the String representation
     */
    public static SappAddressType fromString(String address) {
        if (!StringUtils.isEmpty(address)) {
            for (SappAddressType addressType : SappAddressType.values()) {
                if (addressType.getAddress().equals(address)) {
                    return addressType;
                }
            }
        }

        throw new IllegalArgumentException("Invalid or unsupported Sapp address: " + address);
    }
}

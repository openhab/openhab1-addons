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

/**
 * Rollershutter Status Address model
 *
 * @author Paolo Denti
 * @since 1.8.0
 *
 */
public class SappAddressRollershutterStatus extends SappAddress {

    private int openValue;
    private int closedValue;

    /**
     * Constructor
     */
    public SappAddressRollershutterStatus(String pnmasId, SappAddressType addressType, int address, String subAddress,
            int openValue, int closedValue) {
        super(pnmasId, addressType, address, subAddress);
        this.openValue = openValue;
        this.closedValue = closedValue;
    }

    /**
     * openValue getter
     */
    public int getOpenValue() {
        return openValue;
    }

    /**
     * closedValue getter
     */
    public int getClosedValue() {
        return closedValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(),
                openValue, closedValue);
    }
}

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
 * Address Range model
 *
 * @author Paolo Denti
 * @since 1.8.0
 *
 */
public class SappAddressRange {

    private int loRange;
    private int hiRange;

    /**
     * Constructor
     */
    public SappAddressRange(int loRange, int hiRange) {
        this.loRange = loRange;
        this.hiRange = hiRange;
    }

    /**
     * loRange getter
     */
    public int getLoRange() {
        return loRange;
    }

    /**
     * hiRange getter
     */
    public int getHiRange() {
        return hiRange;
    }
}

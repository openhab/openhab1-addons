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
package org.openhab.binding.rpircswitch.internal;

import java.util.BitSet;

import org.openhab.core.binding.BindingConfig;

/**
 * An {@link RPiRcSwitchBindingConfig} stores group and device address of one RC
 * switch.
 *
 * @author Matthias Röckl
 * @since 1.8.0
 */
public class RPiRcSwitchBindingConfig implements BindingConfig {

    private BitSet groupAddress;
    private int deviceAddress;

    /**
     * Creates a new {@link RPiRcSwitchBindingConfig} with the given group and
     * device address.
     * 
     * @param groupAddress
     *            the group address, e.g. 10101
     * @param deviceAddress
     *            the device address, e.g. 4
     */
    public RPiRcSwitchBindingConfig(BitSet groupAddress, int deviceAddress) {
        this.groupAddress = groupAddress;
        this.deviceAddress = deviceAddress;
    }

    /**
     * Returns the group address, e.g. 10101.
     * 
     * @return the group address
     */
    public BitSet getGroupAddress() {
        return groupAddress;
    }

    /**
     * Sets the group address, e.g. 10101.
     * 
     * @param groupAddress
     *            the group address
     */
    public void setGroupAddress(BitSet groupAddress) {
        this.groupAddress = groupAddress;
    }

    /**
     * Returns the device address, e.g. 4.
     * 
     * @return the device address
     */
    public int getDeviceAddress() {
        return deviceAddress;
    }

    /**
     * Sets the device address, e.g. 4.
     * 
     * @param deviceAddress
     *            the device address
     */
    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

}

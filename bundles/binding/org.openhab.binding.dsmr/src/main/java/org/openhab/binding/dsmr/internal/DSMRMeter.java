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
package org.openhab.binding.dsmr.internal;

/**
 * DSMR Meter represents a meter for this binding.
 * <p>
 * The main Electricity meter {@link DSMRMeterType}.ELECTRICTY is available
 * implicit and an instance of this class for this meter is not necessary.
 *
 * @author M. Volaart
 * @since 1.7.0
 */
public class DSMRMeter {
    // Meter type
    private final DSMRMeterType meterType;

    // M-Bus channel
    private final int channel;

    /**
     * Creates a new DSMRMeter
     * 
     * @param meterType
     *            {@link DSMRMeterType}
     * @param channel
     *            integer specifying on which M-Bus channel the meter is
     *            configured
     */
    public DSMRMeter(DSMRMeterType meterType, int channel) {
        this.meterType = meterType;
        this.channel = channel;
    }

    /**
     * Returns the DSMRMeterType
     * 
     * @return the DSMRMeterType
     */
    public DSMRMeterType getMeterType() {
        return meterType;
    }

    /**
     * Returns the channel
     * 
     * @return the channel
     */
    public int getChannel() {
        return channel;
    }
}

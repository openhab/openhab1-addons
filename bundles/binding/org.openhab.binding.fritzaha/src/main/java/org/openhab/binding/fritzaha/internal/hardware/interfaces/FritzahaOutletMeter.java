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
package org.openhab.binding.fritzaha.internal.hardware.interfaces;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;

/**
 * Interface for handling outlet meters.
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaOutletMeter extends FritzahaDevice {
    /**
     * Type of meter handled by a config.
     * 
     * @author Christian Brauers
     * @since 1.3.0
     */
    public static enum MeterType {
        VOLTAGE,
        CURRENT,
        POWER,
        ENERGY,
        TEMPERATURE;
    };

    public static enum TimeDef {
        MINUTES,
        DAY,
        MONTH,
        YEAR;
    }

    /**
     * Getter for type of meter
     * 
     * @return Meter type
     */
    public MeterType getMeterType();

    /**
     * Inquires about the meter value and updates item state accordingly. Should
     * be asynchronous if possible.
     * 
     * @param ItemName
     *            Item to update
     * @param WebIface
     *            Web interface to use
     */
    public void updateMeterValue(String itemName, FritzahaWebInterface webIface);

}

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
package org.openhab.binding.maxcube.internal.message;

/**
 * MAX!Cube wall mounted thermostat.
 *
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class WallMountedThermostat extends HeatingThermostat {

    /**
     * Class constructor.
     * 
     * @param c
     */
    public WallMountedThermostat(Configuration c) {
        super(c);
    }

    @Override
    public DeviceType getType() {
        return DeviceType.WallMountedThermostat;
    }
}

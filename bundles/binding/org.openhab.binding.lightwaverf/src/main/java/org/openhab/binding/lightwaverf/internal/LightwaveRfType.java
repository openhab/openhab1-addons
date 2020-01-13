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
package org.openhab.binding.lightwaverf.internal;

/**
 * The types of LightwaveRF Devicies (Dimmer, Switch) or the parameters on a
 * (HEATING_*) device for multi paramter devices like the Radiator valves.
 *
 * @author Neil Renaud
 * @since 1.7.0
 */
public enum LightwaveRfType {
    DIMMER,
    SWITCH,
    RELAY,

    VERSION,
    UPDATETIME,
    SIGNAL,

    HEATING_CURRENT_TEMP,
    HEATING_BATTERY,
    HEATING_SET_TEMP,
    HEATING_MODE,
    HEATING_OUTPUT,

    ENERGY_YESTERDAY_USAGE,
    ENERGY_TODAY_USAGE,
    ENERGY_MAX_USAGE,
    ENERGY_CURRENT_USAGE,

    WIFILINK_IP,
    WIFILINK_FIRMWARE,
    WIFILINK_DUSK_TIME,
    WIFILINK_DAWN_TIME,
    WIFILINK_UPTIME,
    WIFILINK_LONGITUDE,
    WIFILINK_LATITUDE,

    MOOD,
    ALL_OFF;
}

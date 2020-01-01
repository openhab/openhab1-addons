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
package org.openhab.binding.sonance.internal;

/**
 * IP codes to send to the amplifier. List of all possible commands: http://www.
 * sonance.com/assets/media/files/downloads/Sonance_IP_Codes_-_June_2015_
 * -_V1_2_110612.xlsx
 *
 * @author Laurens Van Acker
 *
 */
final class SonanceConsts {
    protected static final String VOLUME_UP_3 = "FF55020E";
    protected static final String VOLUME_DOWN_3 = "FF55020F";
    protected static final String VOLUME_UP = "FF550204";
    protected static final String VOLUME_DOWN = "FF550205";
    protected static final String VOLUME_QUERY = "FF550210";
    protected static final String MUTE_ON = "FF550207";
    protected static final String MUTE_OFF = "FF550208";
    protected static final String MUTE_QUERY = "FF550212";
    protected static final String POWER_ON = "FF550101";
    protected static final String POWER_OFF = "FF550102";
    protected static final String POWER_QUERY = "FF550170";
    protected static final String DIRECT_VOLUME_QUERY = "FF5502";
}

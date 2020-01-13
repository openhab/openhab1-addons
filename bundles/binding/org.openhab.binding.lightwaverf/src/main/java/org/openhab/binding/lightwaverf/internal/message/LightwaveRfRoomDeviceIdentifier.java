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
package org.openhab.binding.lightwaverf.internal.message;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfRoomDeviceIdentifier implements LightwaveRfDeviceId {

    private final String roomId;
    private final String deviceId;

    public LightwaveRfRoomDeviceIdentifier(String roomId, String deviceId) {
        this.roomId = roomId;
        this.deviceId = deviceId;
    }

    @Override
    public String getDeviceIdentifier() {
        return "R" + roomId + "D" + deviceId;
    }

}

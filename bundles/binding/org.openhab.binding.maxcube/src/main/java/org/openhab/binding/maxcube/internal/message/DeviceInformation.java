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
 * Device information provided b the M message meta information.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class DeviceInformation {

    private DeviceType deviceType = DeviceType.Invalid;
    private String serialNumber = "";
    private String rfAddress = "";
    private String name = "";
    private int roomId = -1;

    public DeviceInformation(DeviceType deviceType, String serialNumber, String rfAddress, String name, int roomId) {
        this.deviceType = deviceType;
        this.serialNumber = serialNumber;
        this.rfAddress = rfAddress;
        this.name = name;
        this.roomId = roomId;
    }

    public String getRFAddress() {
        return rfAddress;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
}

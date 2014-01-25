/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

/**
 * A {@link HomematicParameterAddress} is a locator to uniquely identify a
 * device parameter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicParameterAddress implements ParameterAddress {

    private String deviceId;
    private String channelId;
    private String parameterId;

    public static HomematicParameterAddress from(String address, String parameterKey) {
        String[] configParts = address.trim().split(":");
        HomematicParameterAddress parameterAddress = new HomematicParameterAddress();
        parameterAddress.deviceId = configParts[0];
        parameterAddress.channelId = configParts[1];
        parameterAddress.parameterId = parameterKey;
        return parameterAddress;
    }

    public HomematicParameterAddress(String physicalDeviceAddress, String channel, String parameterKey) {
        this.deviceId = physicalDeviceAddress;
        this.channelId = channel;
        this.parameterId = parameterKey;
    }

    private HomematicParameterAddress() {
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    public String getAddress() {
        return deviceId + ":" + channelId;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    public Integer getChannelNumber() {
        return Integer.valueOf(channelId);
    }

    @Override
    public String getParameterId() {
        return parameterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }

    @Override
    public String getAsString() {
        return "{" + "deviceId=" + deviceId + ", channelId=" + channelId + ", parameterId=" + parameterId + "}";
    }

    @Override
    public String toString() {
        return getAsString();
    }

}

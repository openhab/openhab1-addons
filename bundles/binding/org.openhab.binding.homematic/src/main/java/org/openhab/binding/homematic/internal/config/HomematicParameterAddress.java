/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

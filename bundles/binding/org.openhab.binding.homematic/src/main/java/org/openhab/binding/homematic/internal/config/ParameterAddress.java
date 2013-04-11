/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * A {@link ParameterAddress} is a locator to uniquely identify a device parameter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class ParameterAddress {

    private String physicalDeviceAddress;
    private Integer channel;
    private String parameterKey;

    public static ParameterAddress fromBindingConfig(String bindingConfig) throws BindingConfigParseException {
        String[] configParts = bindingConfig.trim().split("[:#]");
        if (configParts.length != 3) {
            throw new BindingConfigParseException("Homematic device configurations must contain three parts "
                    + "<physicalDeviceAddress>:<channel>#<parameterKey>");
        }
        ParameterAddress parameterAddress = new ParameterAddress();
        parameterAddress.physicalDeviceAddress = configParts[0];
        parameterAddress.channel = Integer.valueOf(configParts[1]);
        parameterAddress.parameterKey = configParts[2];
        return parameterAddress;
    }

    public static ParameterAddress from(String address, String parameterKey) {
        String[] configParts = address.trim().split(":");
        ParameterAddress parameterAddress = new ParameterAddress();
        parameterAddress.physicalDeviceAddress = configParts[0];
        parameterAddress.channel = Integer.valueOf(configParts[1]);
        parameterAddress.parameterKey = parameterKey;
        return parameterAddress;
    }

    public ParameterAddress(String physicalDeviceAddress, Integer channel, String parameterKey) {
        this.physicalDeviceAddress = physicalDeviceAddress;
        this.channel = channel;
        this.parameterKey = parameterKey;
    }

    private ParameterAddress() {
    }

    public String getPhysicalDeviceAddress() {
        return physicalDeviceAddress;
    }

    public String getAddress() {
        return physicalDeviceAddress + ":" + channel;
    }

    public Integer getChannel() {
        return channel;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return getAddress() + "#" + parameterKey;
    }

}

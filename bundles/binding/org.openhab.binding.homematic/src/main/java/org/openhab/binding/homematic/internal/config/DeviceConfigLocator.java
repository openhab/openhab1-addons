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

import java.util.ArrayList;
import java.util.List;

/**
 * Locates and load xml based device configs.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3
 * 
 */
public class DeviceConfigLocator {

    public static final String PATH = "devices";

    private String[] files;

    private DeviceConfigParser deviceConfigParser = new DeviceConfigParser();

    public DeviceConfigLocator(String... files) {
        this.files = files;
    }

    /**
     * Searches for the given files in a subdirectory "devices" in the
     * classpath.
     * 
     * @return All found and parsed ConfiguredDevices
     */
    public List<ConfiguredDevice> findAll() {
        List<ConfiguredDevice> result = new ArrayList<ConfiguredDevice>();
        for (String file : files) {
            ConfiguredDevice config = deviceConfigParser.parseDeviceConfig(PATH + "/" + file);
            result.add(config);
        }
        return result;
    }
}

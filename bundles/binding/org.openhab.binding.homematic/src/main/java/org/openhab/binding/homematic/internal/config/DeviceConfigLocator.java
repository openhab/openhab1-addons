/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Locates and load xml based device configs.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
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

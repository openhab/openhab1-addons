package org.openhab.binding.homematic.internal.config;

import java.util.ArrayList;
import java.util.List;

public class DeviceConfigLocator {

    public static final String PATH = "devices";

    private String[] files;

    private DeviceConfigParser deviceConfigParser = new DeviceConfigParser();

    public DeviceConfigLocator(String... files) {
        this.files = files;
    }

    public List<ConfiguredDevice> findAll() {
        List<ConfiguredDevice> result = new ArrayList<ConfiguredDevice>();
        for (String file : files) {
            ConfiguredDevice config = deviceConfigParser.parseDeviceConfig(PATH + "/" + file);
            result.add(config);
        }
        return result;
    }
}

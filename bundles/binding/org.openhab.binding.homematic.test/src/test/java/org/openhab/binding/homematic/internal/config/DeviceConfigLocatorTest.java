package org.openhab.binding.homematic.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class DeviceConfigLocatorTest {

    @Test
    public void testFindAll() {
        DeviceConfigLocator configLocator = new DeviceConfigLocator("example_device_config.xml");
        List<ConfiguredDevice> devices = configLocator.findAll();
        assertNotNull(devices);
        assertEquals("List size = 1", 1, devices.size());
        assertNotNull("", devices.get(0));
    }

}

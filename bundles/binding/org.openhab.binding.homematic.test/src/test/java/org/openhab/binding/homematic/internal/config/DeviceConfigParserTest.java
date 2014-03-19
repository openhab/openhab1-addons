/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class DeviceConfigParserTest {

    @Test
    public void testParseDeviceConfig() {
        DeviceConfigParser parser = new DeviceConfigParser();
        ConfiguredDevice deviceConfig = parser.parseDeviceConfig("devices/example_device_config.xml");
        assertNotNull("result", deviceConfig);
        assertEquals("HM-LC-Bl1-FM", deviceConfig.getName());
        assertEquals("rollershutter", deviceConfig.getType());
        List<ConfiguredChannel> channels = deviceConfig.getChannels();
        assertNotNull("channels must exist", channels);
        assertEquals("1 channel", 1, channels.size());
        List<ConfiguredParameter> parameter = channels.get(0).getParameter();
        assertNotNull("parameter", parameter);
        assertEquals("2 parameters", 2, parameter.size());
        ConfiguredParameter configuredParameter = parameter.get(0);
        assertNotNull("parameter name", configuredParameter.getName());
        List<ConfiguredConverter> converter = configuredParameter.getConverter();
        assertNotNull("converter", converter);
        assertEquals("2 converter", 2, converter.size());
        ConfiguredConverter configuredConverter = converter.get(0);
        assertNotNull("forType", configuredConverter.getForType());
        assertNotNull("className", configuredConverter.getClassName());
    }
}

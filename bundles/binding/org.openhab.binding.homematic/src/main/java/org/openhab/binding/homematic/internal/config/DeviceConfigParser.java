/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses xml device configs.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */
public class DeviceConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(DeviceConfigParser.class);

    public DeviceConfigParser() {

    }

    public ConfiguredDevice parseDeviceConfig(String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(ConfiguredDevice.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            ConfiguredDevice configuredDevice = (ConfiguredDevice) unmarshaller.unmarshal(stream);
            return configuredDevice;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}

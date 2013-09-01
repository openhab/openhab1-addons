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
 * @since 1.3
 * 
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

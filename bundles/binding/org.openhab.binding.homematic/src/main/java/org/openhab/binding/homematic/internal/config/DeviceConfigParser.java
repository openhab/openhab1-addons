package org.openhab.binding.homematic.internal.config;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

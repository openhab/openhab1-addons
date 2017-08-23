/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * Use this factory to create devices by their identifier.
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class DeviceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFactory.class);

    public static StandardDevice createFromClass(final Class<? extends StandardDevice> clazz,
            final ESP3Connector connector, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        Constructor<? extends StandardDevice> deviceConstructor;
        try {
            deviceConstructor = clazz.getConstructor(ESP3Connector.class, EnOceanId.class, EnOceanId.class);
        } catch (final NoSuchMethodException e) {
            LOGGER.warn("Device constructor not found.");
            return null;
        } catch (final SecurityException e) {
            LOGGER.warn("Search for device constructor throws security exception.");
            return null;
        }
        try {
            return deviceConstructor.newInstance(connector, addressRemote, addressLocal);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.warn("Device creation failed", ex);
            return null;
        }
    }

    public static StandardDevice createFromType(final String type, final ESP3Connector connector,
            final EnOceanId addressRemote, final EnOceanId addressLocal) {
        final Class<? extends StandardDevice> clazz = SupportedDevice.getClassForIdent(type);
        if (clazz != null) {
            return createFromClass(clazz, connector, addressRemote, addressLocal);
        } else {
            return null;
        }
    }

    private DeviceFactory() {
    }
}

/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles all CULHandler. You can only obtain CULHandlers via this
 * manager.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULManager {

    private final static Logger logger = LoggerFactory.getLogger(CULManager.class);

    private static CULManager instance = new CULManager();

    private Map<String, Class<? extends CULHandlerInternal<?>>> deviceTypeClasses = new HashMap<String, Class<? extends CULHandlerInternal<?>>>();
    private Map<String, CULConfigFactory> deviceTypeConfigFactories = new HashMap<String, CULConfigFactory>();

    private Map<String, CULHandlerInternal<?>> openDevices = new HashMap<String, CULHandlerInternal<?>>();

    public static CULManager getInstance() {
        return instance;
    }

    /**
     * Retrieve the config factory for a certain device type.
     *
     * @param deviceType device type
     * @return config factory for the device type
     */
    public CULConfigFactory getConfigFactory(String deviceType) {
        logger.trace("Requesting deviceTypeFactory for type '{}'", deviceType);
        logger.trace("{} factories registered", deviceTypeConfigFactories.size());
        return deviceTypeConfigFactories.get(deviceType);
    }

    /**
     * Get CULHandler for the given device in the given mode. The same
     * CULHandler can be returned multiple times if you ask multiple times for
     * the same device in the same mode. It is not possible to obtain a
     * CULHandler of an already openend device for another RF mode.
     *
     * @param config
     *            The configuration for the handler.
     * @return A CULHandler to communicate with the culfw based device.
     * @throws CULDeviceException
     */
    public <T extends CULConfig> CULHandlerInternal<T> getOpenCULHandler(T config) throws CULDeviceException {
        CULMode mode = config.getMode();
        String deviceName = config.getDeviceName();
        logger.debug("Trying to open device {} in mode {}", deviceName, mode.toString());
        synchronized (openDevices) {

            if (openDevices.containsKey(deviceName)) {
                @SuppressWarnings("unchecked")
                CULHandlerInternal<T> handler = (CULHandlerInternal<T>) openDevices.get(deviceName);
                if (handler.getConfig().equals(config)) {
                    logger.debug("Device {} is already open in mode {}, returning already openend handler", deviceName, mode.toString());
                    return handler;
                } else {
                    throw new CULDeviceException(
                            "The device " + deviceName + " is already open in mode " + mode.toString());
                }
            }
            CULHandlerInternal<T> handler = createNewHandler(config);
            openDevices.put(deviceName, handler);
            return handler;
        }
    }

    /**
     * Return a CULHandler to the manager. The CULHandler will only be closed if
     * there aren't any listeners registered with it. So it is save to call this
     * methods as soon as you don't need the CULHandler any more.
     *
     * @param handler
     */
    public void close(CULHandlerInternal<?> handler) {
        if (handler != null) {
            synchronized (openDevices) {
                if (!handler.hasListeners()) {
                    openDevices.remove(handler);
                    try {
                        handler.send("X00");
                    } catch (CULCommunicationException e) {
                        logger.warn("Couldn't reset rf mode to X00");
                    }
                    handler.close();
                } else {
                    logger.warn("Can't close device because it still has listeners");
                }
            }
        }
    }

    public void registerHandlerClass(String deviceType, Class<? extends CULHandlerInternal<?>> clazz,
            CULConfigFactory configFactory) {
        logger.debug("Registering class {} for device type {}", clazz.getCanonicalName(), deviceType);
        deviceTypeClasses.put(deviceType, clazz);
        deviceTypeConfigFactories.put(deviceType, configFactory);
    }

    private <T extends CULConfig> CULHandlerInternal<T> createNewHandler(T config) throws CULDeviceException {
        String deviceType = config.getDeviceType();
        CULMode mode = config.getMode();
        logger.debug("Searching class for device type {}", deviceType);
        @SuppressWarnings("unchecked")
        Class<? extends CULHandlerInternal<T>> culHandlerclass = (Class<? extends CULHandlerInternal<T>>) deviceTypeClasses
                .get(deviceType);
        if (culHandlerclass == null) {
            throw new CULDeviceException("No class for the device type " + deviceType + " is registred");
        }

        Class<?>[] constructorParametersTypes = { CULConfig.class };
        Object[] parameters = { config };

        try {
            Constructor<? extends CULHandlerInternal<T>> culHanlderConstructor = culHandlerclass
                    .getConstructor(constructorParametersTypes);

            CULHandlerInternal<T> culHandler = culHanlderConstructor.newInstance(parameters);
            List<String> initCommands = mode.getCommands();
            if (!(culHandler instanceof CULHandlerInternal)) {
                logger.error("Class {} does not implement the internal interface",
                        culHandlerclass.getCanonicalName());
                throw new CULDeviceException("This CULHandler class does not implement the internal interface: "
                        + culHandlerclass.getCanonicalName());
            }
            CULHandlerInternal<?> internalHandler = culHandler;
            internalHandler.open();
            for (String command : initCommands) {
                internalHandler.sendWithoutCheck(command);
            }
            return culHandler;
        } catch (SecurityException e1) {
            throw new CULDeviceException("Not allowed to access the constructor ", e1);
        } catch (NoSuchMethodException e1) {
            throw new CULDeviceException("Can't find the constructor to build the CULHandler", e1);
        } catch (IllegalArgumentException e) {
            throw new CULDeviceException("Invalid arguments for constructor. CULConfig: " + config, e);
        } catch (InstantiationException e) {
            throw new CULDeviceException("Can't instantiate CULHandler object", e);
        } catch (IllegalAccessException e) {
            throw new CULDeviceException("Can't instantiate CULHandler object", e);
        } catch (InvocationTargetException e) {
            throw new CULDeviceException("Can't instantiate CULHandler object", e);
        } catch (CULCommunicationException e) {
            throw new CULDeviceException("Can't initialise RF mode", e);
        }
    }
}

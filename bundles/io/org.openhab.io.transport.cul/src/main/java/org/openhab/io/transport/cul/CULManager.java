/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.io.transport.cul.internal.CULHandlerInternal;
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

	private static Map<String, CULHandler> openDevices = new HashMap<String, CULHandler>();

	private static Map<String, Class<? extends CULHandler>> deviceTypeClasses = new HashMap<String, Class<? extends CULHandler>>();

	/**
	 * Get CULHandler for the given device in the given mode. The same
	 * CULHandler can be returned multiple times if you ask multiple times for
	 * the same device in the same mode. It is not possible to obtain a
	 * CULHandler of an already openend device for another RF mode.
	 * 
	 * @param deviceName
	 *            String representing the device. Currently only serial ports
	 *            are supported.
	 * @param mode
	 *            The RF mode for which the device will be configured.
	 * @return A CULHandler to communicate with the culfw based device.
	 * @throws CULDeviceException
	 */
	public static CULHandler getOpenCULHandler(String deviceName, CULMode mode) throws CULDeviceException {
		logger.debug("Trying to open device " + deviceName + " in mode " + mode.toString());
		synchronized (openDevices) {

			if (openDevices.containsKey(deviceName)) {
				CULHandler handler = openDevices.get(deviceName);
				if (handler.getCULMode() == mode) {
					logger.debug("Device " + deviceName + " is already open in mode " + mode.toString()
							+ ", returning already openend handler");
					return handler;
				} else {
					throw new CULDeviceException("The device " + deviceName + " is already open in mode "
							+ mode.toString());
				}
			}
			CULHandler handler = createNewHandler(deviceName, mode);
			openDevices.put(deviceName, handler);
			return handler;
		}
	}

	private static String getPrefix(String deviceName) {
		int index = deviceName.indexOf(':');
		return deviceName.substring(0, index);
	}

	private static String getRawDeviceName(String deviceName) {
		int index = deviceName.indexOf(':');
		return deviceName.substring(index + 1);
	}

	/**
	 * Return a CULHandler to the manager. The CULHandler will only be closed if
	 * there aren't any listeners registered with it. So it is save to call this
	 * methods as soon as you don't need the CULHandler any more.
	 * 
	 * @param handler
	 */
	public static void close(CULHandler handler) {
		synchronized (openDevices) {

			if (handler instanceof CULHandlerInternal) {
				CULHandlerInternal internalHandler = (CULHandlerInternal) handler;
				if (!internalHandler.hasListeners()) {
					openDevices.remove(handler);
					try {
						handler.send("X00");
					} catch (CULCommunicationException e) {
						logger.warn("Couldn't reset rf mode to X00");
					}
					internalHandler.close();
				} else {
					logger.warn("Can't close device because it still has listeners");
				}
			}
		}
	}

	public static void registerHandlerClass(String deviceType, Class<? extends CULHandler> clazz) {
		logger.debug("Registering class " + clazz.getCanonicalName() + " for device type " + deviceType);
		deviceTypeClasses.put(deviceType, clazz);
	}

	private static CULHandler createNewHandler(String deviceName, CULMode mode) throws CULDeviceException {
		String deviceType = getPrefix(deviceName);
		String deviceAddress = getRawDeviceName(deviceName);
		logger.debug("Searching class for device type " + deviceType);
		Class<? extends CULHandler> culHandlerclass = deviceTypeClasses.get(deviceType);
		if (culHandlerclass == null) {
			throw new CULDeviceException("No class for the device type " + deviceType + " is registred");
		}
		Class<?>[] constructorParametersTypes = { String.class, CULMode.class };
		try {
			Constructor<? extends CULHandler> culHanlderConstructor = culHandlerclass
					.getConstructor(constructorParametersTypes);
			Object[] parameters = { deviceAddress, mode };
			CULHandler culHandler = culHanlderConstructor.newInstance(parameters);
			List<String> initCommands = mode.getCommands();
			if (!(culHandler instanceof CULHandlerInternal)) {
				logger.error("Class " + culHandlerclass.getCanonicalName()
						+ " does not implement the internal interface");
				throw new CULDeviceException("This CULHandler class does not implement the internal interface: "
						+ culHandlerclass.getCanonicalName());
			}
			CULHandlerInternal internalHandler = (CULHandlerInternal) culHandler;
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
			throw new CULDeviceException("Invalid arguments for constructor. Device name: " + deviceAddress
					+ " CULMode " + mode, e);
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

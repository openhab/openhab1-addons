/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.serial.internal;

import java.io.IOException;


import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.transport.serial.SerialDeviceException;
import org.openhab.io.transport.serial.SerialDeviceHandler;
import org.openhab.io.transport.serial.SerialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for Serial.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.6.0
 */
public class Serial {

	private static final Logger logger = LoggerFactory.getLogger(Serial.class);

	// provide public static methods here
	
	// Example
	@ActionDoc(text="Method to send data via serial devices.", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean sendSerial(@ParamDoc(name="deviceName", text="Serial device name.") String deviceName, 
			@ParamDoc(name="message", text="Serial command")String message) {
		
		logger.debug("Send '" + message + "' to '" + deviceName + "'.");
		if(!SerialService.isProperlyConfigured()) {
			logger.debug("Serial device action not properly configured.");
			return false;
		}
		
		SerialDeviceHandler device = SerialService.getHandler(deviceName);
		try {
			device.open();
			device.send(message);
		} catch (SerialDeviceException e) {
			logger.error("Can't open device " + deviceName);
			return false;
		} catch (IOException e) {
			logger.error("Can't send to '" + deviceName + "': " + e);
		}
		return true;
	}
}

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
		if (!SerialActionService.isProperlyConfigured) {
			logger.debug("Serial device action not properly configured.");
			return false;
		}
		
		SerialActionHandler device = SerialActionService.getHandler(deviceName);
		try {
			device.openHardware();
			device.send(message);
		} catch (SerialActionException e) {
			logger.error("Can't open device " + deviceName);
			return false;
		} catch (IOException e) {
			logger.error("Can't send to '" + deviceName + "': " + e);
		}
		return true;
	}
	
	@ActionDoc(text="Method to set lock flag for serial device (works only with devices configured in openhab.cfg)", returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean lockSerial(@ParamDoc(name="deviceName", text="Serial device name.") String deviceName){
		SerialActionHandler device = SerialActionService.getHandler(deviceName);
		return device.lock();
	}
	
	@ActionDoc(text="Method to remove lock flag from serial device (works only with devices configured in openhab.cfg)", returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean unlockSerial(@ParamDoc(name="deviceName", text="Serial device name.") String deviceName){
		SerialActionHandler device = SerialActionService.getHandler(deviceName);
		return device.unlock();
	}

}

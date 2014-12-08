/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.serial;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the Serial devices.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.6.0
 */
public class SerialService implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SerialService.class);

	/**
	 * Indicates whether this device is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	private static boolean isProperlyConfigured = false;
	
	private static HashMap<Integer, SerialDeviceHandler> serialConnections = new HashMap<Integer, SerialDeviceHandler>();
	
	
	
	public SerialService() {
	}
	
	public void activate() {
		logger.debug("Load Serial transport Service");
	}
	
	public void deactivate() {
		for(Iterator<SerialDeviceHandler> it = serialConnections.values().iterator(); it.hasNext();){
			it.next().close();
		}
	}



	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if(!key.isEmpty() && key.matches(".*\\.\\d+")){
					String value = (String) config.get(key);
					logger.debug("Import key: " + key + " value: " + value);
					Integer index = Integer.parseInt(key.split(Pattern.quote("."))[1]);
					if(serialConnections.containsKey(index)){
						logger.debug("Update serial connection (Index: " + index + " Key: " + key + " Value: " + value + ")");
						serialConnections.get(index).setProperty(key.split(Pattern.quote("."))[0], value);
					} else {
						logger.debug("New serial connection (Index: " + index + " Device: " + value + ")");
						SerialDeviceHandler handler = new SerialDeviceHandler(key.split(Pattern.quote("."))[0], value);
						serialConnections.put(index, handler);
					}
				}
			}
			try{
				for(Iterator<SerialDeviceHandler> it = serialConnections.values().iterator(); it.hasNext();){
					it.next().open();
				}
			} catch(SerialDeviceException e) {
				isProperlyConfigured = false;
				logger.error("Can not configure serial device", e.toString());
				throw new ConfigurationException("Can not configure serial device", e.toString());
			}
			isProperlyConfigured = true;
		}
	}
	
	public static SerialDeviceHandler getHandler(String deviceName){
		logger.debug("Get serial handler: " + deviceName);
		for(Iterator<SerialDeviceHandler> it = serialConnections.values().iterator(); it.hasNext();){
			SerialDeviceHandler connection = it.next();
			if(connection.getDeviceName().equals(deviceName)){
				logger.debug("Found preconfigured serial device");
				return connection;
			}
			
		}
		return new SerialDeviceHandler(deviceName);
	}
	
	
	public static Boolean isProperlyConfigured(){
		return isProperlyConfigured;
	}
}
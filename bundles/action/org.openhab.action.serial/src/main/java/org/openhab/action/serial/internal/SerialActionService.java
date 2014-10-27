/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.serial.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the Serial action.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.6.0
 */
public class SerialActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SerialActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	static boolean isProperlyConfigured = false;
	
	private static HashMap<Integer, SerialActionHandler> serialConnections = new HashMap<Integer, SerialActionHandler>();
	
	
	
	public SerialActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Serial.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Serial.class;
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
						SerialActionHandler handler = new SerialActionHandler(key.split(Pattern.quote("."))[0], value);
						serialConnections.put(index, handler);
					}
				}
			}
			try{
				for(Iterator<SerialActionHandler> it = serialConnections.values().iterator(); it.hasNext();){
					it.next().openHardware();
				}
			} catch(SerialActionException e) {
				isProperlyConfigured = false;
				throw new ConfigurationException("Can not configure serial action", e.toString());
			}
			isProperlyConfigured = true;
		}
	}
	
	public static SerialActionHandler getHandler(String deviceName){
		logger.debug("Get serial handler: " + deviceName);
		for(Iterator<SerialActionHandler> it = serialConnections.values().iterator(); it.hasNext();){
			SerialActionHandler connection = it.next();
			if(connection.getDeviceName().equals(deviceName)){
				logger.debug("Found preconfigured serial device");
				return connection;
			}
			
		}
		return new SerialActionHandler(deviceName);
	}
	
}

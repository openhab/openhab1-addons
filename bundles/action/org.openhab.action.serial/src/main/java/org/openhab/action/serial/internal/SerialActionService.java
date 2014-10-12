/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.serial.internal;

import gnu.io.SerialPort;

import java.io.SerializablePermission;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


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
	
	static Map<String, SerialPort> serialPorts = new HashMap<String, SerialPort>(); 
	
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
			while (config.keys().hasMoreElements()) {
				String key = (String) config.keys().nextElement();
				logger.debug("Key: " + key + "Value: " + config.get(key));
			}
			

			isProperlyConfigured = true;
		}
	}
	
}

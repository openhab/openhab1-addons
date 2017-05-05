/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package be.devlaminck.openwebnet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtocolRead for OpenWebNet - OpenHab binding Based on code from Mauro
 * Cicolella (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic
 * /freedomotic/tree/master/plugins/devices/openwebnet) and on code of Flavio
 * Fcrisciani released as EPL
 * (https://github.com/fcrisciani/java-myhome-library)
 *
 * @author Tom De Vlaminck, LAGO Moreno
 * @serial 1.0
 * @since 1.7.0
 */
public class ProtocolRead {

	// ------------------------------------------------------------------------

	private static final Logger logger = LoggerFactory.getLogger(ProtocolRead.class);

	// ------------------------------------------------------------------------

	private String message = null;
	private Map<String, String> properties = new HashMap<String, String>();

	// ------------------------------------------------------------------------

	/**
	 * Create a protocol read with specified message
	 * 
	 * @param message
	 */
	public ProtocolRead(String message) {
		this.message = message;
		logger.info("Instance created for message [" + message + "]");
	}

	// ------------------------------------------------------------------------

	/**
	 * Add a key value property to message
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		properties.put(key, value);
		logger.info("addProperty Key : " + key + ", Value : " + value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the property with specified key
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return ("ProtocolRead, Message[" + this.message + "]");
	}

	// ------------------------------------------------------------------------
}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol;

import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents Satel INT-RS module. Implements method required to connect and
 * communicate with that module over serial protocol.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntRSModule extends SatelModule {
	private static final Logger logger = LoggerFactory.getLogger(Ethm1Module.class);

	/**
	 * Creates new instance with port and timeout set to specified values.
	 * 
	 * @param port
	 *            serial port the module is connected to
	 * @param timeout
	 *            timeout value in milliseconds for connect/read/write
	 *            operations
	 * @throws ConfigurationException
	 *             unconditionally throws this exception as it is not
	 *             implemented yet
	 */
	public IntRSModule(String port, int timeout) throws ConfigurationException {
		super(timeout);
		throw new ConfigurationException("port", "INT-RS module not supported yet");
	}

	@Override
	protected CommunicationChannel connect() {
		logger.error("Not implemented");
		return null;
	}
}

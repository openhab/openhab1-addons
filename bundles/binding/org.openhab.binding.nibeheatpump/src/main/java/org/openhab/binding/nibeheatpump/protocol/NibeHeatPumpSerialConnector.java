/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.protocol;

import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for serial port communication.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpSerialConnector extends NibeHeatPumpConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(NibeHeatPumpSerialConnector.class);

	public NibeHeatPumpSerialConnector(String portName) {
		
		logger.debug("Nibe heatpump Serial Port message listener started");
	}

	@Override
	public void connect() throws NibeHeatPumpException {
		
		throw new NibeHeatPumpException("Not implemented");
	}

	@Override
	public void disconnect() throws NibeHeatPumpException {
		
		throw new NibeHeatPumpException("Not implemented");
	}

	@Override
	public byte[] receiveDatagram() throws NibeHeatPumpException {

		throw new NibeHeatPumpException("Not implemented");
	}
}

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
 * Connector for testing purposes.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpSimulator extends NibeHeatPumpConnector {

	private static final Logger logger = LoggerFactory.getLogger(NibeHeatPumpSimulator.class);

	public NibeHeatPumpSimulator() {
		logger.debug("Nibe heatpump Test message listener started");
	}

	@Override
	public void connect() throws NibeHeatPumpException {
	}

	@Override
	public void disconnect() throws NibeHeatPumpException {
	}

	@Override
	public byte[] receiveDatagram() throws NibeHeatPumpException {
		try {
			Thread.sleep(10000);

			final byte[] testdata = new byte[] {
					(byte) 0x5C, (byte) 0x00, (byte) 0x20, (byte) 0x68,
					(byte) 0x50, (byte) 0x01, (byte) 0xA8, (byte) 0x1F,
					(byte) 0x01, (byte) 0x00, (byte) 0xA8, (byte) 0x64,
					(byte) 0x00, (byte) 0xFD, (byte) 0xA7, (byte) 0xD0,
					(byte) 0x03, (byte) 0x44, (byte) 0x9C, (byte) 0x1E,
					(byte) 0x00, (byte) 0x4F, (byte) 0x9C, (byte) 0xA0,
					(byte) 0x00, (byte) 0x50, (byte) 0x9C, (byte) 0x78,
					(byte) 0x00, (byte) 0x51, (byte) 0x9C, (byte) 0x03,
					(byte) 0x01, (byte) 0x52, (byte) 0x9C, (byte) 0x1B,
					(byte) 0x01, (byte) 0x87, (byte) 0x9C, (byte) 0x14,
					(byte) 0x01, (byte) 0x4E, (byte) 0x9C, (byte) 0xC6,
					(byte) 0x01, (byte) 0x47, (byte) 0x9C, (byte) 0x01,
					(byte) 0x01, (byte) 0x15, (byte) 0xB9, (byte) 0xB0,
					(byte) 0xFF, (byte) 0x3A, (byte) 0xB9, (byte) 0x4B,
					(byte) 0x00, (byte) 0xC9, (byte) 0xAF, (byte) 0x00,
					(byte) 0x00, (byte) 0x48, (byte) 0x9C, (byte) 0x0D,
					(byte) 0x01, (byte) 0x4C, (byte) 0x9C, (byte) 0xE7,
					(byte) 0x00, (byte) 0x4B, (byte) 0x9C, (byte) 0x00,
					(byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00,
					(byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00,
					(byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00,
					(byte) 0x00, (byte) 0x45 };

			return testdata;

		} catch (InterruptedException e) {
			throw new NibeHeatPumpException(e);
		}
	}
	
}

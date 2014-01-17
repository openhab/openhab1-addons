/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulation connector for testing purposes.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationSimulator extends SwegonVentilationConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(SwegonVentilationSimulator.class);

	private int counter = 0;

	public SwegonVentilationSimulator() {
	}

	@Override
	public void connect() throws SwegonVentilationException {
		logger.debug("Swegon ventilation simulator started");
	}

	@Override
	public void disconnect() throws SwegonVentilationException {
	}

	@Override
	public byte[] receiveDatagram() throws SwegonVentilationException {

		try {

			Thread.sleep(5000);

			final byte[] testdata21 = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x0B, (byte) 0x21, (byte) 0x01, (byte) 0xA0,
					(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x03,
					(byte) 0x0A, (byte) 0x04, (byte) 0x00, (byte) 0x00,
					(byte) 0x8C, (byte) 0x17

			};

			final byte[] testdata71 = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x13, (byte) 0x71, (byte) 0x00, (byte) 0xA0,
					(byte) 0xFD, (byte) 0x09, (byte) 0x15, (byte) 0x09,
					(byte) 0x0A, (byte) 0x04, (byte) 0xFB, (byte) 0x09,
					(byte) 0x00, (byte) 0x00, (byte) 0x9B, (byte) 0xA8,
					(byte) 0x2D, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x04, (byte) 0x97

			};

			final byte[] testdata73a = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x18, (byte) 0x73, (byte) 0x00, (byte) 0xA0,
					(byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x00,
					(byte) 0x21, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
					(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
					(byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x07, (byte) 0x01, (byte) 0x49 };

			final byte[] testdata73b = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x18, (byte) 0x73, (byte) 0x00, (byte) 0xA0,
					(byte) 0x80, (byte) 0x00, (byte) 0x14, (byte) 0x00,
					(byte) 0x21, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
					(byte) 0x00, (byte) 0x01, (byte) 0x86, (byte) 0x00,
					(byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x07, (byte) 0x4F, (byte) 0xFB };

			final byte[] testdata73c = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x18, (byte) 0x73, (byte) 0x00, (byte) 0xA0,
					(byte) 0x10, (byte) 0x00, (byte) 0x14, (byte) 0x00,
					(byte) 0x21, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
					(byte) 0x00, (byte) 0x01, (byte) 0xE3, (byte) 0x00,
					(byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40,
					(byte) 0x07, (byte) 0x9D, (byte) 0xB7

			};

			final byte[] testdata73d = new byte[] {
					// (byte) 0xCC,
					(byte) 0x64, (byte) 0x85, (byte) 0xFD, (byte) 0x0A,
					(byte) 0x18, (byte) 0x73, (byte) 0x00, (byte) 0xA0,
					(byte) 0x90, (byte) 0x00, (byte) 0x14, (byte) 0x00,
					(byte) 0x21, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
					(byte) 0x00, (byte) 0x01, (byte) 0xE3, (byte) 0x00,
					(byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40,
					(byte) 0x07, (byte) 0xAB, (byte) 0x5B };

			final byte[][] messages = new byte[][] { testdata21, testdata71,
					testdata73a, testdata73b, testdata73c, testdata73d };

			if (++counter >= messages.length)
				counter = 0;

			byte[] message = messages[counter];

			int msgLen = 0;

			if (message[0] == (byte) 0x64) {
				msgLen = message[4];
			}

			int calculatedCRC = calculateCRC(message, message.length - 2);
			int msgCRC = toInt(message[5 + msgLen], message[5 + msgLen + 1]);

			if (msgCRC == calculatedCRC) {

				byte[] data = new byte[5 + msgLen];

				for (int j = 0; j < (5 + msgLen); j++)
					data[j] = message[j];

				return data;

			} else {
				throw new SwegonVentilationException("CRC does not match");
			}

		} catch (InterruptedException e) {

			throw new SwegonVentilationException(e);
		}

	}
}

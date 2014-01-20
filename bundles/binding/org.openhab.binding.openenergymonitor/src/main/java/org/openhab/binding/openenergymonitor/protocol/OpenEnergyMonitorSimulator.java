/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.protocol;

import java.nio.ByteBuffer;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector simulator for testing purposes.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorSimulator extends OpenEnergyMonitorConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorSimulator.class);

	private int counter = 0;

	public OpenEnergyMonitorSimulator() {
	}

	@Override
	public void connect() throws OpenEnergyMonitorException {
		logger.debug("Open Energy Monitor simulator started");
	}

	@Override
	public void disconnect() throws OpenEnergyMonitorException {
	}

	@Override
	public byte[] receiveDatagram() throws OpenEnergyMonitorException {

		try {

			Thread.sleep(5000);

			String testData1 = new String("10 224 0 73 1 139 0 68 0 11 0 66 0 28 0 17 0 141 0 175 0 74 0 80 0 122 1 59 2 236 0 227 34 0 0 60 0");
			String testData2 = new String("10 221 0 74 1 139 0 66 0 10 0 63 0 26 0 16 0 136 0 167 0 70 0 81 0 113 1 50 2 236 0 227 34 0 0 60 0");
			String testData3 = new String("10 223 0 71 1 139 0 68 0 11 0 57 0 24 0 19 0 132 0 163 0 69 0 81 0 110 1 36 2 235 0 227 34 0 0 60 0");
			String testData4 = new String("10 129 15 17 17 199 7 90 0 133 15 208 16 170 7 92 0 49 15 10 17 196 7 89 0 55 46 237 50 219 0 222 14 1 0 233 8");
			String testData5 = new String("10 232 15 51 17 203 7 92 0 98 15 140 16 127 7 92 0 227 14 251 16 178 7 87 0 46 46 187 50 220 0 226 14 1 0 248 8");
			String testData6 = new String("10 222 19 192 20 75 8 95 0 17 19 234 19 245 7 95 0 130 19 183 20 71 8 94 0 114 58 99 61 250 0 230 14 1 0 240 8");
			String testData7 = new String("10 78 16 156 17 223 7 92 0 229 15 230 16 141 7 94 0 46 15 224 16 139 7 89 0 97 47 99 51 223 0 235 14 1 0 255 8"); 
			
			@SuppressWarnings("unused")
			String testData8 = new String("10 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36");
			@SuppressWarnings("unused")
			String testData9 = new String("10 255 255 255 255 255 255 255 255 255 255 255 255 0 0 0 64 0 0 0 0 0 0 0 64");
			
			final String[] messages = new String[] { testData1, testData2, testData3, testData4, testData5, testData6, testData7 };

			if (++counter >= messages.length)
				counter = 0;

			String[] bytes = messages[counter].split(" ");
			
			ByteBuffer bytebuf = ByteBuffer.allocate(bytes.length);
			
			for (int i=0; i<bytes.length; i++) {
				byte b = (byte) Integer.parseInt(bytes[i]);
				bytebuf.put(b);
			}
			
			return bytebuf.array();

		} catch (InterruptedException e) {

			throw new OpenEnergyMonitorException(e);
		}

	}
}

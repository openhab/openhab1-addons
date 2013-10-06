/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.openenergymonitor.protocol;

import java.nio.ByteBuffer;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for testing purposes.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorSimulator extends OpenEnergyMonitorConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorSimulator.class);

	private int counter = 0;

	public OpenEnergyMonitorSimulator() {

		logger.debug("Swegon ventilation simulator started");
	}

	@Override
	public void connect() throws OpenEnergyMonitorException {
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

/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.plcbus.internal.protocol;

import java.io.InputStream;

import gnu.io.NRSerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ByteProvider from SerialPort
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortByteProvider implements IByteProvider {

	private static Logger logger = 
		LoggerFactory.getLogger(SerialPortGateway.class);
	private NRSerialPort serialPort;

	/**
	 * Constructor
	 * 
	 * @param serialPort
	 */
	private SerialPortByteProvider(NRSerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * Create a new instance
	 */
	public static IByteProvider create(NRSerialPort serialPort) {
		return new SerialPortByteProvider(serialPort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
	 * getByte()
	 */
	@Override
	public byte getByte() {
		return getBytes(1)[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
	 * getBytes(int)
	 */
	@Override
	public byte[] getBytes(int count) {
		int counter = 0;

		while (counter < 1000) {
			try {
				InputStream in = serialPort.getInputStream();

				if (serialPort.isConnected() && in.available() >= count) {
					byte[] result = new byte[count];
					in.read(result);
					return result;
				}
			} catch (Exception e) {
				logger.info("error in readthread {}", e.getMessage());
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("error while handling thread lifecycle", e);
			}
			counter++;
		}

		return new byte[0];
	}

}

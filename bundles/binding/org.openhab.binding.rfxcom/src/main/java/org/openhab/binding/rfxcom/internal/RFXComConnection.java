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

package org.openhab.binding.rfxcom.internal;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Dictionary;
import java.util.EventObject;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.internal.connector.RFXComEventListener;
import org.openhab.binding.rfxcom.internal.connector.RFXComSerialConnector;
import org.openhab.binding.rfxcom.internal.messages.RFXComMessageUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class establishes the connection to the RFXCOM controller.
 * 
 * @author Pauli Anttila, Evert van Es
 * @since 1.2.0
 */
public class RFXComConnection implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(RFXComConnection.class);

	private static String serialPort = null;
	private static byte[] setMode = null;

	static RFXComSerialConnector connector = null;

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
	}

	/**
	 * Returns the RFXCOM client for communicating to the controller. The link
	 * can be null, if it has not (yet) been established successfully.
	 * 
	 * @return instance to current RFXCOM client.
	 */
	public static synchronized RFXComSerialConnector getCommunicator() {
		return connector;
	}

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true
				: false);

		if (config != null) {

			serialPort = (String) config.get("serialPort");
			String setModeStr = (String) config.get("setMode");

			if (setModeStr != null && setModeStr.isEmpty() == false) {
				
				try {
					setMode = DatatypeConverter.parseHexBinary(setModeStr);
				} catch (IllegalArgumentException e) {
					throw new ConfigurationException("setMode", e.getMessage());
				}
				
				if (setMode.length != 14) {
					throw new ConfigurationException("setMode", "hexBinary value lenght should be 14 bytes (28 characters)");
				}
			}

			try {
				connect();

			} catch (Exception e) {
				logger.error("Connection to RFXCOM controller failed.", e);
			}
		}

	}

	private void connect() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException, InterruptedException, ConfigurationException {

		logger.info("Connecting to RFXCOM [serialPort='{}' ].",
				new Object[] { serialPort });

		connector = new RFXComSerialConnector();

		connector.addEventListener(new RFXComEventListener() {

			@Override
			public void packetReceived(EventObject event, byte[] packet) {

				try {
					Object obj = RFXComMessageUtils.decodePacket(packet);
					logger.debug("Data received:\n{}", obj.toString());
				} catch (IllegalArgumentException e) {
					logger.debug("Unknown data received, data: {}",
							DatatypeConverter.printHexBinary(packet));
				}
			}
		});

		connector.connect(serialPort);

		logger.debug("Reset controller");
		connector.sendMessage(RFXComMessageUtils.CMD_RESET);
		
		// controller does not response immediately after reset,
		// so wait a while
		Thread.sleep(1000);

		if (setMode != null) {
			try {
				logger.debug("Set mode: {}",
						DatatypeConverter.printHexBinary(setMode));
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("setMode", e.getMessage());
			}
			
			connector.sendMessage(setMode);
		} else {
			connector.sendMessage(RFXComMessageUtils.CMD_STATUS);
		}
	}
	

}

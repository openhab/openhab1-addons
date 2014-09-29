/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.anel.internal.AnelBinding.IInternalAnelBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MessageListener runs as a separate thread.
 * 
 * Thread listening message from Anel Net-PwrCtrl devices and send updates to
 * openHAB bus.
 * 
 * @since 1.6.0
 * @author paphko
 */
class AnelConnectorThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(AnelConnectorThread.class);

	private static final String UDP_STATE_REQUEST = "wer da?";

	private boolean interrupted = false;

	private final AnelState state; // cached states of switches and I/O
	private final AnelUDPConnector connector;
	private final IInternalAnelBinding binding;
	private final String user;
	private final String password;

	/**
	 * Initialize a new thread for listening on UDP packages of an Anel device.
	 * 
	 * @param host
	 *            The IP address / host name of an Anel device.
	 * @param udpReceivePort
	 *            The UDP receiver port.
	 * @param udpSendPort
	 *            The UDP sender port.
	 * @param user
	 *            Login for the Anel device.
	 * @param password
	 *            The password for the Anel device.
	 * @param binding
	 *            A facade to the binding for sending updates to the openHAB
	 *            event bus.
	 */
	AnelConnectorThread(String host, int udpReceivePort, int udpSendPort, String user, String password,
			IInternalAnelBinding binding) {
		this.binding = binding;
		this.password = password;
		this.user = user;
		state = new AnelState(host);
		connector = new AnelUDPConnector(host, udpReceivePort, udpSendPort);
	}

	/**
	 * Switch relay on or off.
	 * 
	 * @param switchNr
	 *            The relay number to switch.
	 * @param newState
	 *            The new state.
	 */
	void sendSwitch(int switchNr, boolean newState) {
		final Boolean switchState;
		final Boolean switchLocked;
		synchronized (state) {
			switchState = state.switchState[switchNr - 1];
			switchLocked = state.switchLocked[switchNr - 1];
		}

		// check via Boolean object because current state may be null
		if (!Boolean.valueOf(newState).equals(switchState)) {

			// check that this switch is not locked!
			if (switchLocked != null) {
				if (!switchLocked) {

					// Format to switch on: Sw_on<nr><user><pwd>
					// Format to switch off: Sw_off<nr><user><pwd>
					// Example: Sw_on3adminanel
					final String cmd = "Sw_" + (newState ? "on" : "off") + switchNr + user + password;
					logger.debug("Sending to " + state.host + ": " + cmd);
					try {
						connector.sendDatagram(cmd.getBytes());
					} catch (Exception e) {
						logger.error("Error occured when sending UDP data to Anel device: " + cmd, e);
					}
				} else {
					logger.debug("switch is locked, nothing sent.");
				}
			} else {
				logger.debug("switch lock state not yet initialized, nothing sent.");
			}
		} else {
			logger.debug("switch has already the requested state, nothing sent.");
		}
	}

	/**
	 * Switch IO on or off, assuming that it is set to input. Otherwise nothing
	 * happens.
	 * 
	 * @param ioNr
	 *            The IO number to switch.
	 * @param newState
	 *            The new state.
	 */
	protected void sendIO(int ioNr, boolean newState) {
		final Boolean isInput;
		final Boolean ioState;
		synchronized (state) {
			isInput = state.ioIsInput[ioNr - 1];
			ioState = state.ioState[ioNr - 1];
		}

		// check via Boolean object because current state may be null
		if (!Boolean.valueOf(newState).equals(ioState)) {

			// check whether IO is of direction output
			if (isInput == null || !isInput) {
				logger.debug("Attempted to change IO" + ioNr + " to " + (newState ? "ON" : "OFF")
						+ " but it's direction is " + (isInput == null ? "unknown" : "input"));
				return; // better not send anything if direction is not
						// 'out'
			}

			// Format to switch on: IO_on<nr><user><pwd>
			// Format to switch off: IO_off<nr><user><pwd>
			// Example: IO_on3adminanel
			final String cmd = "IO_" + (newState ? "on" : "off") + ioNr + user + password;
			logger.debug("Sending to " + state.host + ": " + cmd);
			try {
				connector.sendDatagram(cmd.getBytes());
			} catch (Exception e) {
				if (e.getCause() instanceof UnknownHostException) {
					logger.error("Could not check status of Anel device '" + state.host + "'");
				} else {
					logger.error("Error occured when sending UDP data to Anel device: " + cmd, e);
				}
			}
		} // else: IO has already the requested state
	}

	/**
	 * Stop this connector thread.
	 */
	public void setInterrupted() {
		this.interrupted = true;
		connector.disconnect(); // interrupt any blocking UDP listener!
	}

	/**
	 * Explicitly update state.
	 */
	void requestRefresh() {
		if (interrupted || isInterrupted())
			return; // do not refresh if thread is interrupted!

		// we could add a check that refreshes are not too frequent...
		// final long now = System.currentTimeMillis();
		// if (state.lastUpdate + refreshInterval < now) {
		logger.debug("Sending to " + state.host + ": " + UDP_STATE_REQUEST);
		try {
			connector.sendDatagram(UDP_STATE_REQUEST.getBytes());
		} catch (Exception e) {
			logger.error("Error occured when sending UDP data to Anel device: '" + UDP_STATE_REQUEST + "'", e);
		}
		// }
	}

	@Override
	public void run() {
		logger.debug("Anel NET-PwrCtrl message listener started");

		try {
			connector.connect();
		} catch (Exception e) {
			logger.error("Error occured when connecting to NET-PwrCtrl device", e);
			logger.warn("Closing NET-PwrCtrl message listener");

			interrupted = true; // exit
		}

		// as long as no interrupt is requested, continue running
		while (!interrupted) {
			try {
				// Wait for a packet (blocking)
				logger.trace("Listening on " + state.host + "...");
				final byte[] data = connector.receiveDatagram();

				logger.trace("Received data (len={}): {}", data.length, DatatypeConverter.printString(new String(data)));

				final Map<AnelCommandType, org.openhab.core.types.State> newValues;
				synchronized (state) {
					newValues = AnelDataParser.parseData(data, state);
				}

				if (newValues != null && !newValues.isEmpty()) {
					logger.debug("newValues (len={}): {}", newValues.size(), newValues);

					for (AnelCommandType cmd : newValues.keySet()) {
						final org.openhab.core.types.State state = newValues.get(cmd);

						final Collection<String> itemNames = binding.getItemNamesForCommandType(cmd);
						for (String itemName : itemNames) {
							binding.getEventPublisher().postUpdate(itemName, state);
						}
					}
				}
			} catch (InterruptedException e) {
				// blocking call seems to be interrupted...
				// interrupted is probably false, so we can properly cleanup
			} catch (SocketTimeoutException e) {
				// nothing received after timeout... continue with loop
			} catch (Exception e) {
				logger.error("Error occured when received data from Anel device: " + state.host, e);
			}
		}
		try {
			connector.disconnect();
		} catch (Exception e) {
			logger.error("Error occured when disconnecting form Anel device: " + state.host, e);
		}
	}
}

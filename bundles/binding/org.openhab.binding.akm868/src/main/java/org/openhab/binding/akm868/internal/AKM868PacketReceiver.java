/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.akm868.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for listening to the network to receive packets
 * from the LAN-T Adapter
 * 
 * @author Michael Heckmann
 * @since 1.8.0
 */

public class AKM868PacketReceiver implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(AKM868PacketReceiver.class);
	private AKM868Listener listener;
	private boolean running;
	private Socket akmSocket = null;
	private long timeout;
	private BufferedReader akmReader;
	private List<AKM868Timer> timerList;

	private String deliminator = ","; // default value

	public AKM868PacketReceiver(AKM868Listener listener) {

		this.listener = listener;
	}

	public void initializeReceiver(String host, int port, long timeout) {

		try {
			akmSocket = new Socket(host, port);
			akmReader = new BufferedReader(
					new InputStreamReader(akmSocket.getInputStream()));
			logger.debug("Connected to: " + host + ":" + port);
			timerList = new ArrayList<AKM868Timer>();
			this.timeout = timeout;
		} catch (IOException e) {

			logger.debug(e.getMessage());
		}
	}

	/**
	 * Stop the thread
	 */
	public void stopListener() {
		running = false;
		if (akmSocket != null)
			try {
				akmSocket.close();
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		akmSocket = null;
	}

	@Override
	public void run() {
		running = true; // start loop
		if (akmSocket == null)
			throw new IllegalStateException(
					"Cannot access socket. You must call"
							+ " call initializeListener(..) first!");

		while (running) {

			try {
				if (akmReader.ready()) {
					logger.debug("Receiving data...");
					String line = akmReader.readLine();
					logger.debug("Reading from socket: " + line);
					String[] elements = line.split(deliminator);
					if (elements.length > 5) // Packet not valid
						return;
					String id = elements[2];
					String action = elements[3];
					String packetValid = elements[4];

					if (!packetValid.equalsIgnoreCase("OK")) {
						logger.debug("Packet not valid: " + line);
						return;
					}

					if (action.equals("1")) {
						logger.debug("Found 1 => KeyPressedShort");
						listener.publishKeyPressedShort(id);

					}

					if (action.equals("5")) {
						logger.debug("Found 5 => KeyPressedLong");
						listener.publishKeyPressedLong(id);

					}

					int timerCount = timerList.size();
					boolean containsTimer = false;
					if (timerList.size() > 0) {
						for (int i = 0; i < timerCount; i++) {
							if (timerList.get(i).getId() == new Integer(id)
									.intValue()) {
								logger.debug(
										"Timer found....restarting timer for id: "
												+ id);
								timerList.get(i).restart();
								containsTimer = true;
								break;
							} else {
								containsTimer = false;
							}
						}
						if (!containsTimer) {
							logger.debug(
									"Timer not found....starting new timer for id: "
											+ id);
							timerList.add(new AKM868Timer(
									new Integer(id).intValue()));
							listener.publishUpdate(new Integer(id).toString(),
									true);
						}

					} else {
						logger.debug(
								"Timer not found....starting new timer for id: "
										+ id);
						timerList.add(
								new AKM868Timer(new Integer(id).intValue()));
						listener.publishUpdate(new Integer(id).toString(),
								true);
					}
				}

				int i = 0;
				while (!timerList.isEmpty() & i < timerList.size()) {
					if (timerList.get(i).hasTimedOut(timeout)) {
						logger.debug("Timed out: " + timerList.get(i).getId());
						listener.publishUpdate("" + timerList.get(i).getId(),
								false);
						timerList.remove(i);
						i = 0;
					} else {
						i++;
					}
				}

			} catch (NumberFormatException e) {
				logger.debug(e.getMessage());
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.debug("Exiting");

	}

}

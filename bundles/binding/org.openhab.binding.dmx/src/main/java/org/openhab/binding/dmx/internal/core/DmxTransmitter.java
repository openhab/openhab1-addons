/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.TimerTask;

import org.openhab.binding.dmx.DmxConnection;
import org.openhab.binding.dmx.DmxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DmxTransmitter, which is responsible for continuously sending all value
 * changes to the DMX connection.
 * 
 * This transmitter should always run in a separate thread to allow for smooth
 * transmissions.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public final class DmxTransmitter extends TimerTask {

	private static Logger logger = LoggerFactory
			.getLogger(DmxTransmitter.class);

	private DmxUniverse universe = new DmxUniverse();

	private DmxService service;

	private boolean running;

	private boolean suspended;

	/**
	 * Default constructor.
	 */
	public DmxTransmitter(DmxService service) {
		this.service = service;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void run() {

		if (suspended) {
			return;
		}

		running = true;
		try {
			byte[] b = universe.calculateBuffer();
			if (universe.getBufferChanged()) {
				DmxConnection conn = service.getConnection();
				if (conn != null) {
					conn.sendDmx(b);
					universe.notifyStatusListeners();
				}
			}
		} catch (Exception e) {
			logger.error("Error sending dmx values.", e);
		} finally {
			running = false;
		}
	}

	/**
	 * @return true if the transmitter is calculating values and transmitting
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Suspend/resume transmittting.
	 * 
	 * @param suspend
	 *            true to suspend
	 */
	public void setSuspend(boolean suspend) {
		this.suspended = suspend;
	}

	/**
	 * Get the DMX channel in the current universe.
	 * 
	 * @param channel
	 *            number
	 * @return DMX channel
	 */
	public DmxChannel getChannel(int channel) {
		return universe.getChannel(channel);
	}

	/**
	 * @return DMX universe
	 */
	public DmxUniverse getUniverse() {
		return universe;
	}
}

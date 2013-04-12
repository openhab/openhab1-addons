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

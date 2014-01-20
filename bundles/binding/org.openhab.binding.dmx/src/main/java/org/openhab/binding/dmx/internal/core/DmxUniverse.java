/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.openhab.binding.dmx.DmxStatusUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Universe. Can contain up to 512 DMX channels.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxUniverse {

	private static final Logger logger = LoggerFactory
			.getLogger(DmxUniverse.class);

	private Vector<DmxChannel> channels = new Vector<DmxChannel>();

	private short[] buffer = new short[512];

	private boolean bufferChanged = false;

	private int minimumBufferSize = 32;

	private List<DmxStatusUpdateListener> updateListeners = new ArrayList<DmxStatusUpdateListener>();

	/**
	 * Change the buffer value at the given index.
	 * 
	 * @param index
	 * @param value
	 */
	private void setBufferValue(int index, short value) {

		if (buffer[index] == value) {
			return;
		} else {
			buffer[index] = value;
			bufferChanged = true;
		}
	}

	/**
	 * Calculate the current DMX buffer state.
	 * 
	 * @return DMX buffer.
	 */
	public byte[] calculateBuffer() {

		bufferChanged = false;
		long calculationTime = System.currentTimeMillis();

		for (DmxChannel channel : channels) {
			setBufferValue(channel.getChannelId() - 1,
					channel.getNextValue(calculationTime).shortValue());
		}

		byte[] b = new byte[minimumBufferSize];
		for (int i = 0; i < minimumBufferSize; i++) {
			b[i] = (byte) (buffer[i]);
		}
		return b;
	}

	/**
	 * Add a new DMX channel.
	 * 
	 * @param channel
	 *            to add.
	 */
	private synchronized void addChannel(DmxChannel channel) {

		logger.trace("Adding channel {}", channel.getChannelId());
		channels.add(channel);
		Collections.sort(channels);

		if (channel.getChannelId() > minimumBufferSize) {
			minimumBufferSize = channel.getChannelId();
		}
	}

	/**
	 * @return true if the buffer was changed since the last calculation.
	 */
	public boolean getBufferChanged() {
		return bufferChanged;
	}

	/**
	 * Find a channel by id. If it doesn't exist, it is created.
	 * 
	 * @param channelId
	 *            int
	 * @return channel
	 */
	public DmxChannel getChannel(int channelId) {
		for (DmxChannel c : channels) {
			if (c.getChannelId() == channelId) {
				return c;
			}
		}
		DmxChannel c = new DmxChannel(channelId);
		addChannel(c);
		return c;
	}

	/**
	 * Clear all channel values.
	 */
	public void clear() {

		for (DmxChannel c : channels) {
			c.setValue(0);
		}
	}

	/**
	 * Add a new status update listener, which can receive values when a channel
	 * is changed.
	 * 
	 * @param listener
	 *            status listener to add.
	 */
	public void addStatusListener(DmxStatusUpdateListener listener) {
		updateListeners.add(listener);
	}

	/**
	 * Stop a given status update listener from receiving updates.
	 * 
	 * @param listener
	 *            status listener to remove.
	 */
	public void removeStatusListener(DmxStatusUpdateListener listener) {
		updateListeners.remove(listener);
	}

	/**
	 * Broadcast status update to all listeners.
	 */
	public void notifyStatusListeners() {

		for (DmxStatusUpdateListener listener : updateListeners) {

			if (System.currentTimeMillis() > listener.getLastUpdateTime()
					+ listener.getUpdateDelay()) {

				int values[] = new int[listener.getFootPrint()];
				for (int i = 0; i < listener.getFootPrint(); i++) {
					values[i] = getChannel(listener.getChannel() + i)
							.getValue();
				}
				listener.processStatusUpdate(values);
			}
		}
	}

}

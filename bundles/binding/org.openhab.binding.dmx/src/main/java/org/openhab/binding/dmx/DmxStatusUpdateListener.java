/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx;

/**
 * DMX Status update listener. Objects which implement this interface can
 * register them for receiving updates when a DMX channel value changes.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxStatusUpdateListener {

	/**
	 * @return channel for which to receive updates
	 */
	public int getChannel();

	/**
	 * @return number of channels to receive updates for
	 */
	public int getFootPrint();

	/**
	 * @return minimum delay in ms between updates
	 */
	public int getUpdateDelay();

	/**
	 * Callback for processing status updates.
	 * 
	 * @param channelValues
	 *            updated values.
	 */
	public void processStatusUpdate(int[] channelValues);

	/**
	 * @return time last status update was sent
	 */
	public long getLastUpdateTime();
}

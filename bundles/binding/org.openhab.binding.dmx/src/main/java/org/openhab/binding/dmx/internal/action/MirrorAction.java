/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.action;

import org.openhab.binding.dmx.internal.core.DmxChannel;

/**
 * Mirror action. Makes a channel mimic the behavior of another one. If
 * mirroring channel has a lower id than the channel being mirrored, there will
 * be a delay of 1 frame rate between the 1 channels.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class MirrorAction extends BaseAction {

	/** channel to mirror **/
	private DmxChannel sourceChannel;

	/** Time in ms to keep mimicking. -1 is indefinite **/
	private long holdTime;

	/**
	 * Create new mirror action.
	 * 
	 * @param sourceChannel
	 *            channel whose behavior to mirror.
	 * @param holdTime
	 *            time in ms to keep mirroring the other channel. -1 is
	 *            indefinite.
	 */
	public MirrorAction(DmxChannel sourceChannel, int holdTime) {

		this.sourceChannel = sourceChannel;
		this.holdTime = holdTime;

		if (holdTime < -1) {
			this.holdTime = -1;
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected int calculateNewValue(DmxChannel channel, long currentTime) {

		if (startTime == 0) {
			startTime = currentTime;
		}

		if (holdTime != -1 && (currentTime - startTime > holdTime)) {
			// mark action as completed
			completed = true;
		}

		return sourceChannel.getValue();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void decrease(int decrement) {
		// noop. decrease should have been performed on channel being mirrored.
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void increase(int increment) {
		// noop. increase should have been performed on channel being mirrored.
	}

}

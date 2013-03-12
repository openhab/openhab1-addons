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

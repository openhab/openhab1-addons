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
package org.openhab.binding.dmx.internal.action;

import org.openhab.binding.dmx.internal.core.DmxChannel;
import org.openhab.binding.dmx.internal.core.DmxUtil;

/**
 * Fade action. Fades a given channel from its current state to the requested
 * state in the given amount of time. After the fade, the new state is held for
 * a given or indefinite time.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class FadeAction extends BaseAction {

	/** Time in ms to hold the target value. -1 is indefinite */
	private long holdTime;

	/** Time in ms to fade from current value to new target value */
	private long fadeTime;

	/** Channel output value on action start. **/
	private int startValue;

	/** Desired channel output value. **/
	private int targetValue;

	private long stepDuration;

	private FadeDirection fadeDirection;

	/**
	 * Create new fading action.
	 * 
	 * @param fadeTime
	 *            time in ms to fade from the current value to the new value.
	 * @param targetValue
	 *            new value 0-255 for this channel.
	 * @param holdTime
	 *            time in ms to hold the color before moving to the next action.
	 *            -1 is indefinite.
	 */
	public FadeAction(int fadeTime, int targetValue, int holdTime) {

		super();

		this.fadeTime = fadeTime;
		this.targetValue = DmxUtil.capDmxValue(targetValue);
		this.holdTime = holdTime;

		if (holdTime < -1) {
			this.holdTime = -1;
		}
		if (fadeTime < 0) {
			this.fadeTime = 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int calculateNewValue(DmxChannel channel, long currentTime) {

		int newValue = channel.getValue();

		if (startTime == 0) {

			startTime = currentTime;

			if (fadeTime != 0) {

				startValue = channel.getValue();

				// calculate fade details
				if (startValue == targetValue) {
					stepDuration = 1;
				} else if (startValue > targetValue) {
					fadeDirection = FadeDirection.down;
					stepDuration = fadeTime / (startValue - targetValue);
				} else {
					fadeDirection = FadeDirection.up;
					stepDuration = fadeTime / (targetValue - startValue);
				}

			} else {
				newValue = targetValue;
			}
		}

		long duration = currentTime - startTime;

		if (fadeTime != 0 && newValue != targetValue) {

			// calculate new fade value
			if (stepDuration == 0) {
				stepDuration = 1;
			}
			int currentStep = (int) (duration / stepDuration);

			if (fadeDirection == FadeDirection.up) {
				newValue = DmxUtil.capDmxValue(startValue + currentStep);
				if (newValue > targetValue) {
					newValue = targetValue;
				}
			} else {
				newValue = DmxUtil.capDmxValue(startValue - currentStep);
				if (newValue < targetValue) {
					newValue = targetValue;
				}
			}
		}

		if (newValue == targetValue && holdTime > -1) {
			// we reached the target already, check if we need to hold longer
			if (((holdTime > 0 || fadeTime > 0) && (duration >= fadeTime
					+ holdTime))
					|| (holdTime == 0 && fadeTime == 0)) {
				// mark action as completed
				completed = true;
			}
		}

		return newValue;
	}

}

enum FadeDirection {
	up, down
}

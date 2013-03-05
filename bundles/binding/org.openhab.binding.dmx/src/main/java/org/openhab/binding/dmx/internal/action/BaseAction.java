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
import org.openhab.binding.dmx.internal.core.DmxUtil;

/**
 * Base class for Channel Actions like faders, chasers, etc..
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public abstract class BaseAction {

	protected boolean completed = false;

	protected long startTime = 0;

	/** Minimum output value **/
	public static int MIN_OUTPUT_LEVEL = 0;

	/** Maximum output value **/
	public static int MAX_OUTPUT_LEVEL = 100;

	/** Output level = 0 is no output, 100 is max output **/
	private int outputLevel = 100;

	/**
	 * Calculate the new output value of the channel.
	 * 
	 * @param channel
	 * @param currentTime
	 *            time to use as current time
	 * @return value 0 - 255
	 */
	protected abstract int calcuateNewValue(DmxChannel channel, long currentTime);

	/**
	 * Get the new output value of the channel which has the outputlevel
	 * applied.
	 * 
	 * @param channel
	 * @param currentTime
	 *            time to use as current time
	 * @return value 0 - 255
	 */
	public final int getNewValue(DmxChannel channel, long currentTime) {
		return DmxUtil.getOutputValue(calcuateNewValue(channel, currentTime),
				outputLevel);
	}

	/**
	 * @return true if the action was completed.
	 */
	public final boolean isCompleted() {
		return completed;
	}

	/**
	 * Reset the action to start from the beginning.
	 */
	public void reset() {
		startTime = 0;
		completed = false;
	}

	/**
	 * Decrease the output level with specified decrement.
	 * 
	 * @param decrement
	 */
	public void decrease(int decrement) {
		if (outputLevel - decrement < MIN_OUTPUT_LEVEL) {
			outputLevel = MIN_OUTPUT_LEVEL;
		} else {
			outputLevel -= decrement;
		}
	}

	/**
	 * Decrease the output level with specified increment.
	 * 
	 * @param increment
	 */
	public void increase(int increment) {
		if (outputLevel + increment > MAX_OUTPUT_LEVEL) {
			outputLevel = MAX_OUTPUT_LEVEL;
		} else {
			outputLevel += increment;
		}
	}

	/**
	 * Set action fade output level.
	 * 
	 * @param intValue
	 *            0-100
	 */
	public final void setOutputLevel(int intValue) {
		outputLevel = intValue;
	}

}

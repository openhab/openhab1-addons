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
	protected abstract int calculateNewValue(DmxChannel channel, long currentTime);

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
		return DmxUtil.getOutputValue(calculateNewValue(channel, currentTime),
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

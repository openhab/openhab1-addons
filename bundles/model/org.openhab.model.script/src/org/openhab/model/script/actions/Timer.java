/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.actions;

import org.joda.time.base.AbstractInstant;

/**
 * A timer is a handle for a block of code that is scheduled for future execution. A timer
 * can be canceled or rescheduled.
 * The script action "createTimer" returns a {@link Timer} instance.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public interface Timer {

	/**
	 * Cancels the timer
	 * 
	 * @return true, if cancellation was successful
	 */
	public boolean cancel();

	/**
	 * Determines whether the scheduled code is currently executed.
	 * 
	 * @return true, if the code is being executed, false otherwise
	 */
	public boolean isRunning();

	/**
	 * Determines whether the scheduled execution has already terminated.
	 * 
	 * @return true, if the scheduled execution has already terminated, false otherwise
	 */
	public boolean hasTerminated();

	/**
	 * Reschedules a timer to a new starting time.
	 * This can also be called after a timer has terminated, which will result in another
	 * execution of the same code.
	 * 
	 * @param newTime the new time to execute the code
	 * @return true, if the rescheduling was done successful
	 */
	public boolean reschedule(AbstractInstant newTime);

}
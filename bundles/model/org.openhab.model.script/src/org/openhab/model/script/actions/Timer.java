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
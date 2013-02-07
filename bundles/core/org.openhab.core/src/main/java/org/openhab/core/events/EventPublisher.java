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
package org.openhab.core.events;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * An EventPublisher is used to send commands or status updates to the openHAB event bus.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 */
public interface EventPublisher {

	/**
	 * Initiate synchronous sending of a command.
	 * This method does not return to the caller until all subscribers have processed the command.
	 * 
	 * @param itemName name of the item to send the command for
	 * @param command the command to send
	 */
	public abstract void sendCommand(String itemName, Command command);

	/**
	 * Initiate asynchronous sending of a command.
	 * This method returns immediately to the caller.
	 * 
	 * @param itemName name of the item to send the command for
	 * @param command the command to send
	 */
	public abstract void postCommand(String itemName, Command command);

	/**
	 * Initiate asynchronous sending of a status update.
	 * This method returns immediately to the caller.
	 * 
	 * @param itemName name of the item to send the update for
	 * @param newState the new state to send
	 */
	public abstract void postUpdate(String itemName, State newState);

}

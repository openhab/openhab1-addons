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
 * An EventSubscriber receives events from the openHAB event bus for further processing.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 */
public interface EventSubscriber {

	/**
	 * Callback method if a command was sent on the event bus
	 * 
	 * @param itemName the item for which a command was sent
	 * @param command the command that was sent
	 */
	public void receiveCommand(String itemName, Command command);
	
	/**
	 * Callback method if a state update was sent on the event bus
	 * 
	 * @param itemName the item for which a state update was sent
	 * @param state the state that was sent
	 */
	public void receiveUpdate(String itemName, State newStatus);

}

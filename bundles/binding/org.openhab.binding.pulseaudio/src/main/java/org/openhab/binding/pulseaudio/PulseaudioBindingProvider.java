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
package org.openhab.binding.pulseaudio;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Pulseaudio items (sinks).
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public interface PulseaudioBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding pulseaudio item name to the given <code>itemName</code>
	 */
	public String getItemName(String itemName);
	
	/**
	 * @return the corresponding openHAB item type to the given <code>itemName</code>
	 */
	public Class<? extends Item> getItemType(String itemName);
	
	/**
	 * @return the corresponding pulseaudio server id name to the given <code>itemName</code>
	 */
	public String getServerId(String itemName);
	
	/**
	 * @return the corresponding additional command to the given <code>itemName</code>
	 */
	public String getCommand(String itemName);
	
}

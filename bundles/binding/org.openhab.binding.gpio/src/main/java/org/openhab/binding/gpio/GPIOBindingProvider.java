/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
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


package org.openhab.binding.gpio;

import org.openhab.core.binding.BindingProvider;

/**
 * GPIO binding providers interface.
 * 
 * @author Dancho Penev
 * @since 1.3.1
 */
public interface GPIOBindingProvider extends BindingProvider {

	/**
	 * Query for configured pin number.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured pin number
	 */
	public int getPinNumber(String itemName);

	/**
	 * Query for configured activelow state.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured activelow state
	 */
	public int getActiveLow(String itemName);

	/**
	 * Query for configured pin direction.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured pin direction
	 */
	public int getDirection(String itemName);

	/**
	 * Query has item configuration or not.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return <code>true</code> if the item has configuration,
	 * 		<code>false</code> otherwise
	 */
	public boolean isItemConfigured(String itemName);
}

/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.items;

import java.util.Collection;

/**
 * This exception can be thrown whenever a search pattern does not uniquely identify
 * an item. The list of matching items must be made available through this exception.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class ItemNotUniqueException extends ItemLookupException {

	private static final long serialVersionUID = 5154625234283910124L;

	private final Collection<Item> matchingItems; 
	
	public ItemNotUniqueException(String string, Collection<Item> items) {
		super("Item cannot be uniquely identified by '" + string + "'");
		this.matchingItems = items;
	}

	/**
	 * Returns all items that match the search pattern
	 * 
	 * @return collection of items matching the search pattern
	 */
	public Collection<Item> getMatchingItems() {
		return matchingItems;
	}

}

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

package org.openhab.core.library;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemFactory;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;


/**
 * {@link CoreItemFactory}-Implementation for the core ItemTypes 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class CoreItemFactory implements ItemFactory {
	
	private static String[] ITEM_TYPES = new String[] { "Switch", "Rollershutter", "Contact", "String", "Number", "Dimmer", "DateTime" };

	/**
	 * @{inheritDoc}
	 */
	public GenericItem createItem(String itemTypeName, String itemName) {
		if (itemTypeName.equals(ITEM_TYPES[0]))
			return new SwitchItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[1]))
			return new RollershutterItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[2]))
			return new ContactItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[3]))
			return new StringItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[4]))
			return new NumberItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[5]))
			return new DimmerItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[6]))
			return new DateTimeItem(itemName);
		else
			return null;
	}
	
	/**
	 * @{inheritDoc}
	 */
	public String[] getSupportedItemTypes() {
		return ITEM_TYPES;
	}

}

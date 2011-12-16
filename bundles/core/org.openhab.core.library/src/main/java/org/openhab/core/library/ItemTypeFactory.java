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
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;


/**
 * {@link ItemTypeFactory}-Implementation for the core ItemTypes 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class ItemTypeFactory implements org.openhab.core.items.ItemTypeFactory {

	/**
	 * @{inheritDoc}
	 */
	public GenericItem createItemType(String itemTypeName, String itemName) {
		if (itemTypeName.equals("Switch"))
			return new SwitchItem(itemName);
		if (itemTypeName.equals("Rollershutter"))
			return new RollershutterItem(itemName);
		if (itemTypeName.equals("Contact"))
			return new ContactItem(itemName);
		if (itemTypeName.equals("String"))
			return new StringItem(itemName);
		if (itemTypeName.equals("Number"))
			return new NumberItem(itemName);
		if (itemTypeName.equals("Dimmer"))
			return new DimmerItem(itemName);
		if (itemTypeName.equals("DateTime"))
			return new DateTimeItem(itemName);
		else
			return null;
	}

}

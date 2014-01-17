/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemFactory;
import org.openhab.core.library.items.ColorItem;
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
	
	private static String[] ITEM_TYPES = new String[] { "Switch", "Rollershutter", "Contact", "String", "Number", "Dimmer", "DateTime", "Color" };

	/**
	 * @{inheritDoc}
	 */
	public GenericItem createItem(String itemTypeName, String itemName) {
		if (itemTypeName.equals(ITEM_TYPES[0])) return new SwitchItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[1])) return new RollershutterItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[2])) return new ContactItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[3])) return new StringItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[4])) return new NumberItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[5])) return new DimmerItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[6])) return new DateTimeItem(itemName);
		if (itemTypeName.equals(ITEM_TYPES[7])) return new ColorItem(itemName);
		else return null;
	}
	
	/**
	 * @{inheritDoc}
	 */
	public String[] getSupportedItemTypes() {
		return ITEM_TYPES;
	}

}

/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.tel;

import org.openhab.core.items.GenericItem;
import org.openhab.library.tel.items.CallItem;


/**
 * {@link ItemFactory}-Implementation for this library's ItemTypes
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class TelItemFactory implements org.openhab.core.items.ItemFactory {
	
	private static final String[] ITEM_TYPES = new String[] { "Call" };

	/**
	 * @{inheritDoc}
	 */
	public GenericItem createItem(String itemTypeName, String itemName) {
		if (itemTypeName.equals(ITEM_TYPES[0]))
			return new CallItem(itemName);
		else {
			return null;
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	public String[] getSupportedItemTypes() {
		return ITEM_TYPES;
	}

}

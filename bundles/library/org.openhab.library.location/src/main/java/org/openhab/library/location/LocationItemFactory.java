/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.location;

import org.openhab.core.items.GenericItem;
import org.openhab.library.location.items.LocationItem;


/**
 * {@link ItemFactory}-Implementation for this library's ItemTypes
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class LocationItemFactory implements org.openhab.core.items.ItemFactory {
	
	private static final String[] ITEM_TYPES = new String[] { "Location" };

	/**
	 * @{inheritDoc}
	 */
	public GenericItem createItem(String itemTypeName, String itemName) {
		if (itemTypeName.equals(ITEM_TYPES[0]))
			return new LocationItem(itemName);
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

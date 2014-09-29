/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;


/**
 * This Factory creates concrete instances of the known ItemTypes.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public interface ItemFactory {
	
	/**
	 * Creates a new Item instance of type <code>itemTypeName</code> and the name
	 * <code>itemName</code> 
	 * 
	 * @param itemTypeName
	 * @param itemName
	 * 
	 * @return a new Item of type <code>itemTypeName</code> or
	 * <code>null</code> if no matching class is known.
	 */
	GenericItem createItem(String itemTypeName, String itemName);
	
	/**
	 * Returns the list of all supported ItemTypes of this Factory.
	 * 
	 * @return the supported ItemTypes
	 */
	String[] getSupportedItemTypes();
}

/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.radius;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import java.util.List;

/**
 * @author Jan N. Klug
 * @since 1.8.0
 */
public interface RadiusBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the RADIUS packet type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the RADIUS packet type of the Item identified by {@code itemName}
	 */
	public byte getRadiusType(String itemName);

	/**
	 * Returns the RADIUS attribute of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the attribute for
	 * @return the RADIUS attribute of the Item identified by {@code itemName}
	 */
	public byte getRadiusAttribute(String itemName);

	/**
	 * Returns the username for the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the username for
	 * @return the username for the Item identified by {@code itemName}
	 */

	public String getUserName(String itemName);

	/**
	 * Returns the password for the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the password for
	 * @return the password for the Item identified by {@code itemName}
	 */
	public String getPassword(String itemName);

	/**
	 * Returns all items which are mapped to a RADIUS-In-Binding
	 * 
	 * @return item which are mapped to a RADIUS-In-Binding
	 */
	List<String> getInBindingItemNames();

}
